package org.emftext.sdk.concretesyntax.resource.cs.ui;



//*
// A text editor for models for which text resources were generated by EMFText.
///
public class CsEditor extends org.eclipse.ui.editors.text.TextEditor implements org.eclipse.emf.edit.domain.IEditingDomainProvider {
	
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsHighlighting highlighting;
	private org.eclipse.jface.text.source.projection.ProjectionSupport projectionSupport;
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsCodeFoldingManager codeFoldingManager;
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsBackgroundParsingStrategy bgParsingStrategy = new org.emftext.sdk.concretesyntax.resource.cs.ui.CsBackgroundParsingStrategy();
	private java.util.Collection<org.emftext.sdk.concretesyntax.resource.cs.ICsBackgroundParsingListener> bgParsingListeners = new java.util.ArrayList<org.emftext.sdk.concretesyntax.resource.cs.ICsBackgroundParsingListener>();
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsColorManager colorManager = new org.emftext.sdk.concretesyntax.resource.cs.ui.CsColorManager();
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsOutlinePage emfTextEditorOutlinePage;
	private org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource resource;
	private MarkerAdapter markerAdapter = new MarkerAdapter();
	private org.eclipse.core.resources.IResourceChangeListener resourceChangeListener = new ModelResourceChangeListener();
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsPropertySheetPage propertySheetPage;
	private org.eclipse.emf.edit.domain.EditingDomain editingDomain;
	private org.eclipse.emf.edit.provider.ComposedAdapterFactory adapterFactory;
	
	private final class MarkerUpdateListener implements org.emftext.sdk.concretesyntax.resource.cs.ICsBackgroundParsingListener {
		public void parsingCompleted(org.eclipse.emf.ecore.resource.Resource resource) {
			try {
				org.emftext.sdk.concretesyntax.resource.cs.ui.CsMarkerHelper.unmark(resource);
				org.emftext.sdk.concretesyntax.resource.cs.ui.CsMarkerHelper.mark(resource);
			} catch (org.eclipse.core.runtime.CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	// A custom document listener that triggers background parsing if needed.
	private final class DocumentListener implements org.eclipse.jface.text.IDocumentListener {
		
		public void documentAboutToBeChanged(org.eclipse.jface.text.DocumentEvent event) {
		}
		
		public void documentChanged(org.eclipse.jface.text.DocumentEvent event) {
			bgParsingStrategy.parse(event, resource, CsEditor.this);
		}
	}
	
	// The MarkerAdapter is attached to all resources opened in EMFText editors.
	// When changes are applied to the resource all existing (potentially
	// invalid) markers are removed and replaced by new ones. Further the
	// adapter can be disabled to avoid unnecessary marking when a set of
	// changes is applied.
	///
	private final class MarkerAdapter extends org.eclipse.emf.common.notify.impl.AdapterImpl {
		
		private boolean enabled = true;
		
		public boolean isAdapterForType(java.lang.Object type) {
			return type == CsEditor.class;
		}
		
		public void notifyChanged(org.eclipse.emf.common.notify.Notification notification) {
			if (!enabled) {
				return;
			}
			java.lang.Object notifier = notification.getNotifier();
			if (notifier != null && notifier instanceof org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) {
				org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource resource = (org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) notifier;
				if (!resource.isLoaded()) {
					return;
				}
				try {
					org.emftext.sdk.concretesyntax.resource.cs.ui.CsMarkerHelper.unmark(resource);
					org.emftext.sdk.concretesyntax.resource.cs.ui.CsMarkerHelper.mark(resource);
				} catch (java.lang.Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}
	
	//*
	// Reacts to changes of the text resource displayed in the editor and
	// resources cross-referenced by it. Cross-referenced resources are
	// unloaded, the displayed resource is reloaded. An attempt to resolve all
	// proxies in the displayed resource is made after each change.
	// <p>
	// The code pretty much corresponds to what EMF generates for a tree editor
	///
	private class ModelResourceChangeListener implements org.eclipse.core.resources.IResourceChangeListener {
		public void resourceChanged(org.eclipse.core.resources.IResourceChangeEvent event) {
			org.eclipse.core.resources.IResourceDelta delta = event.getDelta();
			try {
				class ResourceDeltaVisitor implements org.eclipse.core.resources.IResourceDeltaVisitor {
					protected org.eclipse.emf.ecore.resource.ResourceSet resourceSet = editingDomain.getResourceSet();
					
					public boolean visit(org.eclipse.core.resources.IResourceDelta delta) {
						if (delta.getResource().getType() != org.eclipse.core.resources.IResource.FILE) {
							return true;
						}
						int deltaKind = delta.getKind();
						if (deltaKind == org.eclipse.core.resources.IResourceDelta.REMOVED || deltaKind == org.eclipse.core.resources.IResourceDelta.CHANGED && delta.getFlags() != org.eclipse.core.resources.IResourceDelta.MARKERS) {
							org.eclipse.emf.ecore.resource.Resource changedResource = resourceSet.getResource(org.eclipse.emf.common.util.URI.createURI(delta.getFullPath().toString()), false);
							if (changedResource != null) {
								markerAdapter.setEnabled(false);
								changedResource.unload();
								if (changedResource.equals(resource)) {
									// reload the resource displayed in the editor
									resourceSet.getResource(resource.getURI(), true);
								}
								org.eclipse.emf.ecore.util.EcoreUtil.resolveAll(resource);
								markerAdapter.setEnabled(true);
								// reset the selected element in outline and
								// properties by text position
								highlighting.setEObjectSelection();
							}
						}
						
						return true;
					}
				}
				
				ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
				delta.accept(visitor);
			} catch (org.eclipse.core.runtime.CoreException exception) {
				org.emftext.sdk.concretesyntax.resource.cs.mopp.CsPlugin.logError("Unexpected Error: ", exception);
			}
		}
	}
	
	public java.lang.Object getAdapter(Class required) {
		if (org.eclipse.ui.views.contentoutline.IContentOutlinePage.class.equals(required)) {
			return getOutlinePage();
		} else if (required.equals(org.eclipse.ui.views.properties.IPropertySheetPage.class)) {
			return getPropertySheetPage();
		}
		return super.getAdapter(required);
	}
	
	public CsEditor() {
		super();
		setDocumentProvider(new org.eclipse.ui.editors.text.FileDocumentProvider());
		setSourceViewerConfiguration(new org.emftext.sdk.concretesyntax.resource.cs.ui.CsEditorConfiguration(this, colorManager));
		initializeEditingDomain();
		addBackgroundParsingListener(new MarkerUpdateListener());
		org.eclipse.core.resources.ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, org.eclipse.core.resources.IResourceChangeEvent.POST_CHANGE);
	}
	
	public void createPartControl(org.eclipse.swt.widgets.Composite parent) {
		super.createPartControl(parent);
		
		// Code Folding
		org.eclipse.jface.text.source.projection.ProjectionViewer viewer = (org.eclipse.jface.text.source.projection.ProjectionViewer) getSourceViewer();
		// Occurrence initiation, need ITextResource and ISourceViewer.
		highlighting = new org.emftext.sdk.concretesyntax.resource.cs.ui.CsHighlighting(resource, viewer, colorManager, this);
		
		projectionSupport = new org.eclipse.jface.text.source.projection.ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		
		// turn projection mode on
		viewer.doOperation(org.eclipse.jface.text.source.projection.ProjectionViewer.TOGGLE);
		codeFoldingManager = new org.emftext.sdk.concretesyntax.resource.cs.ui.CsCodeFoldingManager(viewer, this);
	}
	
	protected void doSetInput(org.eclipse.ui.IEditorInput editorInput) throws org.eclipse.core.runtime.CoreException {
		super.doSetInput(editorInput);
		initializeResourceObject(editorInput);
		org.eclipse.jface.text.IDocument document = getDocumentProvider().getDocument(getEditorInput());
		document.addDocumentListener(new DocumentListener());
	}
	
	private void initializeResourceObject(org.eclipse.ui.IEditorInput editorInput) {
		org.eclipse.ui.part.FileEditorInput input = (org.eclipse.ui.part.FileEditorInput) editorInput;
		String path = input.getFile().getFullPath().toString();
		org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createPlatformResourceURI(path, true);
		org.eclipse.emf.ecore.resource.ResourceSet resourceSet = editingDomain.getResourceSet();
		resource = (org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) resourceSet.getResource(uri, false);
		if (resource == null) {
			try {
				org.eclipse.emf.ecore.resource.Resource loadedResource = resourceSet.getResource(uri, true);
				if (loadedResource instanceof org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) {
					setResource(loadedResource);
				} else {
					// the resource was not loaded by an EMFText resource, but
					// some other EMF resource
					org.emftext.sdk.concretesyntax.resource.cs.mopp.CsPlugin.getDefault().showErrorDialog("No EMFText resource.", "Sorry, no registered EMFText resource can handle this file type.");
				}
			} catch (java.lang.Exception e) {
				org.emftext.sdk.concretesyntax.resource.cs.mopp.CsPlugin.logError("Exception while loading resource in " + this.getClass().getSimpleName() + ".", e);
			}
		}
	}
	
	protected void setResource(org.eclipse.emf.ecore.resource.Resource loadedResource) throws org.eclipse.core.runtime.CoreException {
		resource = (org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) loadedResource;
		org.eclipse.emf.ecore.util.EcoreUtil.resolveAll(resource);
		org.emftext.sdk.concretesyntax.resource.cs.ui.CsMarkerHelper.unmark(resource);
		org.emftext.sdk.concretesyntax.resource.cs.ui.CsMarkerHelper.mark(resource);
		resource.eAdapters().add(markerAdapter);
	}
	
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	protected void performSave(boolean overwrite, org.eclipse.core.runtime.IProgressMonitor progressMonitor) {
		
		super.performSave(overwrite, progressMonitor);
		// update markers after the resource has been reloaded
		try {
			org.emftext.sdk.concretesyntax.resource.cs.ui.CsMarkerHelper.unmark(resource);
			org.emftext.sdk.concretesyntax.resource.cs.ui.CsMarkerHelper.mark(resource);
		} catch (org.eclipse.core.runtime.CoreException e) {
			org.emftext.sdk.concretesyntax.resource.cs.mopp.CsPlugin.logError("java.lang.Exception while updating markers on resource", e);
		}
		
		// Save code folding state
		codeFoldingManager.saveCodeFoldingStateFile(resource.getURI().toString());
	}
	
	public void registerTextPresentationListener(org.eclipse.jface.text.ITextPresentationListener listener) {
		org.eclipse.jface.text.source.ISourceViewer viewer = getSourceViewer();
		if (viewer instanceof org.eclipse.jface.text.TextViewer) {
			((org.eclipse.jface.text.TextViewer) viewer).addTextPresentationListener(listener);
		}
	}
	
	public void invalidateTextRepresentation() {
		org.eclipse.jface.text.source.ISourceViewer viewer = getSourceViewer();
		if (viewer != null) {
			viewer.invalidateTextPresentation();
		}
		highlighting.resetValues();
	}
	
	public void setFocus() {
		super.setFocus();
		this.invalidateTextRepresentation();
	}
	
	protected void performSaveAs(org.eclipse.core.runtime.IProgressMonitor progressMonitor) {
		org.eclipse.ui.part.FileEditorInput input = (org.eclipse.ui.part.FileEditorInput) getEditorInput();
		String path = input.getFile().getFullPath().toString();
		org.eclipse.emf.ecore.resource.ResourceSet resourceSet = editingDomain.getResourceSet();
		org.eclipse.emf.common.util.URI platformURI = org.eclipse.emf.common.util.URI.createPlatformResourceURI(path, true);
		org.eclipse.emf.ecore.resource.Resource oldFile = resourceSet.getResource(platformURI, true);
		
		super.performSaveAs(progressMonitor);
		
		// load and resave
		input = (org.eclipse.ui.part.FileEditorInput) getEditorInput();
		path = input.getFile().getFullPath().toString();
		org.eclipse.emf.ecore.resource.Resource newFile = resourceSet.createResource(platformURI);
		newFile.getContents().clear();
		newFile.getContents().addAll(oldFile.getContents());
		try {
			oldFile.unload();
			if (newFile.getErrors().isEmpty()) {
				newFile.save(null);
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		// Save code folding state, is it possible with a new name
		codeFoldingManager.saveCodeFoldingStateFile(resource.getURI().toString());
	}
	
	public org.eclipse.emf.ecore.resource.ResourceSet getResourceSet() {
		return editingDomain.getResourceSet();
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource getResource() {
		return resource;
	}
	
	protected void setResource(org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource resource) {
		this.resource = resource;
	}
	
	private java.lang.Object getOutlinePage() {
		if (emfTextEditorOutlinePage == null) {
			emfTextEditorOutlinePage = new org.emftext.sdk.concretesyntax.resource.cs.ui.CsOutlinePage(this);
			emfTextEditorOutlinePage.addSelectionChangedListener(highlighting);
			highlighting.addSelectionChangedListener(emfTextEditorOutlinePage);
		}
		return emfTextEditorOutlinePage;
	}
	
	public org.eclipse.ui.views.properties.IPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null) {
			propertySheetPage = new org.emftext.sdk.concretesyntax.resource.cs.ui.CsPropertySheetPage();
			// add a slightly modified adapter factory that does not return any
			// editors for properties
			// this way, a model can never be modified through the properties
			// view
			propertySheetPage.setPropertySourceProvider(new org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider(adapterFactory) {
				protected org.eclipse.ui.views.properties.IPropertySource createPropertySource(java.lang.Object object, org.eclipse.emf.edit.provider.IItemPropertySource itemPropertySource) {
					return new org.eclipse.emf.edit.ui.provider.PropertySource(object, itemPropertySource) {
						protected org.eclipse.ui.views.properties.IPropertyDescriptor createPropertyDescriptor(org.eclipse.emf.edit.provider.IItemPropertyDescriptor itemPropertyDescriptor) {
							return new org.eclipse.emf.edit.ui.provider.PropertyDescriptor(object, itemPropertyDescriptor) {
								public org.eclipse.jface.viewers.CellEditor createPropertyEditor(org.eclipse.swt.widgets.Composite composite) {
									return null;
								}
							};
						}
					};
				}
			});
			highlighting.addSelectionChangedListener(propertySheetPage);
		}
		return propertySheetPage;
	}
	
	protected void createActions() {
		super.createActions();
		
	}
	
	public org.eclipse.emf.edit.domain.EditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	private void initializeEditingDomain() {
		adapterFactory = new org.eclipse.emf.edit.provider.ComposedAdapterFactory(org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory());
		
		org.eclipse.emf.common.command.BasicCommandStack commandStack = new org.eclipse.emf.common.command.BasicCommandStack();
		// CommandStackListeners can listen for changes. Not sure whether this
		// is needed.
		
		editingDomain = new org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain(adapterFactory,commandStack, new java.util.HashMap<org.eclipse.emf.ecore.resource.Resource, Boolean>());
	}
	
	//*
	// java.util.Set the caret to the offset of the given element.
	//
	// @param element
	//            has to be contained in the org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource of this editor.
	///
	public void setCaret(org.eclipse.emf.ecore.EObject element, String text) {
		try {
			if (element == null || text == null || text.equals("")) {
				return;
			}
			org.eclipse.jface.text.source.ISourceViewer viewer = getSourceViewer();
			org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource textResource = (org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) element.eResource();
			org.emftext.sdk.concretesyntax.resource.cs.ICsLocationMap locationMap = textResource.getLocationMap();
			int destination = locationMap.getCharStart(element);
			int length = locationMap.getCharEnd(element) + 1 - destination;
			
			org.emftext.sdk.concretesyntax.resource.cs.ICsTextScanner lexer = resource.getMetaInformation().createLexer();
			try {
				lexer.setText(viewer.getDocument().get(destination, length));
				org.emftext.sdk.concretesyntax.resource.cs.ICsTextToken token = lexer.getNextToken();
				String tokenText = token.getText();
				while (tokenText != null) {
					if (token.getText().equals(text)) {
						destination += token.getOffset();
						break;
					}
					token = lexer.getNextToken();
					tokenText = token.getText();
				}
			} catch (org.eclipse.jface.text.BadLocationException e) {
			}
			destination = ((org.eclipse.jface.text.source.projection.ProjectionViewer) viewer).modelOffset2WidgetOffset(destination);
			if (destination < 0) {
				destination = 0;
			}
			viewer.getTextWidget().setSelection(destination);
		} catch (java.lang.Exception e) {
			org.emftext.sdk.concretesyntax.resource.cs.mopp.CsPlugin.logError("java.lang.Exception in setCaret()", e);
		}
	}
	
	protected org.eclipse.jface.text.source.ISourceViewer createSourceViewer(org.eclipse.swt.widgets.Composite parent, org.eclipse.jface.text.source.IVerticalRuler ruler, int styles) {
		
		org.eclipse.jface.text.source.ISourceViewer viewer = new org.eclipse.jface.text.source.projection.ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);
		return viewer;
	}
	
	public void addBackgroundParsingListener(org.emftext.sdk.concretesyntax.resource.cs.ICsBackgroundParsingListener listener) {
		bgParsingListeners.add(listener);
	}
	
	public void notifyBackgroundParsingFinished() {
		for (org.emftext.sdk.concretesyntax.resource.cs.ICsBackgroundParsingListener listener : bgParsingListeners) {
			listener.parsingCompleted(resource);
		}
	}
}