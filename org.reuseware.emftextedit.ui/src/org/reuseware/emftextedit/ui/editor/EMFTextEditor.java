package org.reuseware.emftextedit.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.reuseware.emftextedit.resource.TextResource;
import org.reuseware.emftextedit.ui.MarkerHelper;

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

	private ColorManager colorManager;

	private ResourceSet resourceSet;

	public EMFTextEditor() {
		super();
		colorManager = new ColorManager();
		resourceSet = new ResourceSetImpl();
		
        setDocumentProvider(new FileDocumentProvider());
		setSourceViewerConfiguration(new EMFTextEditorConfiguration(this,colorManager));
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
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
			thisFile.load(null);
			
			//TODO Test warnings / errors
			/*thisFile.addWarning(
					"hups",
					thisFile.getContents().get(0).eContents().get(0).eContents().get(0).eContents().get(4)
			);*/
			MarkerHelper.unmark(thisFile);
			MarkerHelper.mark(thisFile);
		} catch (Exception e) {
			e.printStackTrace();
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
				thisFile.eAdapters().add(new AdapterImpl() {

					public boolean isAdapterForType(Object type) {
						return type == EMFTextEditor.class;
					}
					
					public void notifyChanged(Notification notification) {
						try {
							MarkerHelper.unmark((Resource)getTarget());
							MarkerHelper.mark((Resource)getTarget());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		return resourceSet;
	}


}
