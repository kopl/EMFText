package org.reuseware.emftextedit.runtime.ui.editor;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.reuseware.emftextedit.runtime.resource.TextResource;
import org.reuseware.emftextedit.runtime.ui.ColorManager;
import org.reuseware.emftextedit.runtime.ui.EMFTextEditorConfiguration;
import org.reuseware.emftextedit.runtime.ui.MarkerHelper;
import org.reuseware.emftextedit.runtime.ui.IOptionProvider;
import org.reuseware.emftextedit.runtime.ui.ISaveListener;
import org.reuseware.emftextedit.runtime.ui.outline.EMFTextOutlinePage;

/**
 * <i>
 * This is a text editor for models for which text resources were generated by EMFTextEdit.
 * This is only a very basic implementation that needs extension! Parts of the configurations
 * pages were taken from the JDT Java Editor
 * </i>
 * 
 * @author Jendrik Johannes (jj2)
 *
 */
public class EMFTextEditor extends TextEditor /*implements IEditingDomainProvider*/ {

	private final class MarkerAdapter extends AdapterImpl {
		private boolean enabled = true;
		
		public boolean isAdapterForType(Object type) {
			return type == EMFTextEditor.class;
		}

		public void notifyChanged(Notification notification) {
			if (!enabled) {
				return;
			}
			try {
				MarkerHelper.unmark((Resource)getTarget());
				MarkerHelper.mark((Resource)getTarget());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	private static final String SAVE_PERFORMED_EXTENSION_POINT_ID = "org.reuseware.emftextedit.runtime.ui.perform_save";
	private static final String DEFAULT_LOAD_OPTIONS_EXTENSION_POINT_ID = "org.reuseware.emftextedit.runtime.ui.default_load_options";

	private ColorManager colorManager;

	private ResourceSet resourceSet;

	private EMFTextOutlinePage emfTextEditorOutlinePage;
	
	private Resource resource;

	private MarkerAdapter markerAdapter = new MarkerAdapter();

	private PropertySheetPage propertySheetPage;

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (emfTextEditorOutlinePage == null) {
				emfTextEditorOutlinePage = new EMFTextOutlinePage(this);
				emfTextEditorOutlinePage
						.addSelectionChangedListener(new ISelectionChangedListener() {
							// This ensures that we handle selections correctly.
							//
							public void selectionChanged(
									SelectionChangedEvent event) {
								handleContentOutlineSelection(event
										.getSelection());
							}
						});
			}
			return emfTextEditorOutlinePage;
		} else if (required.equals(IPropertySheetPage.class)) {
				return getPropertySheetPage();
		}
		return super.getAdapter(required);
	}
	
	
	public EMFTextEditor() {
		super();
		colorManager = new ColorManager();
		resourceSet = new ResourceSetImpl();
		addDefaultLoadOptions(resourceSet.getLoadOptions());
		
        setDocumentProvider(new FileDocumentProvider());
		setSourceViewerConfiguration(new EMFTextEditorConfiguration(this,colorManager));
	}
	
	private void addDefaultLoadOptions(Map<Object, Object> loadOptions) {
		// find default load option providers
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement configurationElements[] = extensionRegistry.getConfigurationElementsFor(EMFTextEditor.DEFAULT_LOAD_OPTIONS_EXTENSION_POINT_ID);
		for (IConfigurationElement element : configurationElements) {
			try {
				IOptionProvider listener = (IOptionProvider) element.createExecutableExtension("class");//$NON-NLS-1$
				final Map<?, ?> options = listener.getOptions();
				final Collection<?> keys = options.keySet();
				for (Object key : keys) {
					// TODO mseifert: check if there is already an option set
					loadOptions.put(key, options.get(key));
				}
			} catch (CoreException ce) {
				// TODO log this to the error view
				ce.printStackTrace();
			}
		}
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	 public void handleContentOutlineSelection(ISelection selection) {
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			Object selectedElement = ((IStructuredSelection) selection)
					.getFirstElement();
			if (selectedElement instanceof EObject) {
				EObject selectedEObject = (EObject) selectedElement;
				Resource resource = selectedEObject.eResource();
				if (resource instanceof TextResource) {
					TextResource textResource = (TextResource) resource;
					int elementCharStart = textResource.getElementCharStart(selectedEObject);
					int elementCharEnd = textResource.getElementCharEnd(selectedEObject);
					selectAndReveal(elementCharStart, elementCharEnd - elementCharStart + 1);
				}
			}
		}
	}
	@Override
	protected void performSave(boolean overwrite,
			IProgressMonitor progressMonitor) {
		
		super.performSave(overwrite, progressMonitor); 
		FileEditorInput input = (FileEditorInput) getEditorInput();
		String path = input.getFile().getFullPath().toString();
		TextResource thisFile = (TextResource) resourceSet.getResource(URI.createPlatformResourceURI(path, true), true);
		thisFile.unload();
		try {
			markerAdapter.setEnabled(false);
			thisFile.load(resourceSet.getLoadOptions());
			markerAdapter.setEnabled(true);
			
			fireSaveEvent(thisFile);
			
			MarkerHelper.unmark(thisFile);
			MarkerHelper.mark(thisFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fireSaveEvent(TextResource resource) {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement configurationElements[] = extensionRegistry.getConfigurationElementsFor(EMFTextEditor.SAVE_PERFORMED_EXTENSION_POINT_ID);
		for (IConfigurationElement element : configurationElements) {
			try {
				ISaveListener listener = (ISaveListener) element.createExecutableExtension("class");//$NON-NLS-1$
				listener.savePerformed(resource);
			} catch (CoreException ce) {
				// TODO log this to the error view
				ce.printStackTrace();
			}
		}
	}


	@Override
	protected void performSaveAs(IProgressMonitor progressMonitor) {
		FileEditorInput input = (FileEditorInput) getEditorInput();
		String path = input.getFile().getFullPath().toString();
		Resource oldFile = resourceSet.getResource(URI.createPlatformResourceURI(path, true), true);
		
		super.performSaveAs(progressMonitor);
		
		//load and resave
		input = (FileEditorInput) getEditorInput();
		path = input.getFile().getFullPath().toString();
		Resource newFile = resourceSet.createResource(URI.createPlatformResourceURI(path, true));
		newFile.getContents().clear();
		newFile.getContents().addAll(oldFile.getContents());
		try {
			oldFile.unload();
			if (newFile.getErrors().isEmpty()) {
				newFile.save(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResourceSet getResourceSet() {
		FileEditorInput input = (FileEditorInput) getEditorInput();
		String path = input.getFile().getFullPath().toString();
		Resource thisFile = resourceSet.getResource(URI.createPlatformResourceURI(path, true), false);
		if (thisFile == null)
			try {
				thisFile = resourceSet.getResource(URI.createPlatformResourceURI(path, true), true);
				MarkerHelper.unmark(thisFile);
				MarkerHelper.mark(thisFile);
				thisFile.eAdapters().add(markerAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return resourceSet;
	}
	
	public void markResource() {
		try {
			MarkerHelper.unmark(resource);
			MarkerHelper.mark(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public IPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null) {
			propertySheetPage = new PropertySheetPage() {

				@Override
				public void handleEntrySelection(ISelection selection) {
					// TODO Auto-generated method stub
					super.handleEntrySelection(selection);
					propertySheetPage.getControl().setEnabled(false);
				}
				
			};
			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

			adapterFactory
					.addAdapterFactory(new ResourceItemProviderAdapterFactory());
			adapterFactory
					.addAdapterFactory(new EcoreItemProviderAdapterFactory());
			adapterFactory
					.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

			propertySheetPage
					.setPropertySourceProvider(new AdapterFactoryContentProvider(
							adapterFactory));
			
		}
		
		
		
		return propertySheetPage;
	}
}
