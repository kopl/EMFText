package org.emftext.sdk.concretesyntax.resource.cs.ui;

// This class provides the configuration for all EMFText editors. It registers
// content assistance and syntax highlighting.
public class CsEditorConfiguration extends org.eclipse.jface.text.source.SourceViewerConfiguration {
	
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsColorManager colorManager;
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsEditor theEditor;
	
	// Create a new editor configuration.
	//
	// @param editor
	// @param colorManager
	///
	public CsEditorConfiguration(org.emftext.sdk.concretesyntax.resource.cs.ui.CsEditor editor, org.emftext.sdk.concretesyntax.resource.cs.ui.CsColorManager colorManager) {
		this.theEditor = editor;
		this.colorManager = colorManager;
	}
	
	public org.eclipse.jface.text.contentassist.IContentAssistant getContentAssistant(org.eclipse.jface.text.source.ISourceViewer sourceViewer) {
		
		org.eclipse.jface.text.contentassist.ContentAssistant assistant = new org.eclipse.jface.text.contentassist.ContentAssistant();
		assistant.setContentAssistProcessor(new org.emftext.sdk.concretesyntax.resource.cs.ui.CsCompletionProcessor(theEditor), org.eclipse.jface.text.IDocument.DEFAULT_CONTENT_TYPE);
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(500);
		assistant.setProposalPopupOrientation(org.eclipse.jface.text.contentassist.IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(org.eclipse.jface.text.contentassist.IContentAssistant.CONTEXT_INFO_ABOVE);
		
		return assistant;
	}
	
	public String[] getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer sourceViewer) {
		return new String[] {
			org.eclipse.jface.text.IDocument.DEFAULT_CONTENT_TYPE,
		};
	}
	
	// @param fileExtension
	// @return
	///
	protected org.eclipse.jface.text.rules.ITokenScanner getScanner(String fileName) {
		org.eclipse.ui.part.FileEditorInput input = (org.eclipse.ui.part.FileEditorInput) theEditor.getEditorInput();
		String path = input.getFile().getFullPath().toString();
		org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource thisResource = (org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource) theEditor.getResourceSet().getResource(org.eclipse.emf.common.util.URI.createPlatformResourceURI(path, true), true);
		
		return new org.emftext.sdk.concretesyntax.resource.cs.ui.CsTokenScanner(thisResource, colorManager);
	}
	
	public org.eclipse.jface.text.presentation.IPresentationReconciler getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer sourceViewer) {
		
		org.eclipse.jface.text.presentation.PresentationReconciler reconciler = new org.eclipse.jface.text.presentation.PresentationReconciler();
		String fileName = theEditor.getEditorInput().getName();
		
		org.eclipse.jface.text.rules.DefaultDamagerRepairer repairer = new org.eclipse.jface.text.rules.DefaultDamagerRepairer(getScanner(fileName));
		reconciler.setDamager(repairer, org.eclipse.jface.text.IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(repairer, org.eclipse.jface.text.IDocument.DEFAULT_CONTENT_TYPE);
		
		return reconciler;
	}
	
	public org.eclipse.jface.text.source.IAnnotationHover getAnnotationHover(org.eclipse.jface.text.source.ISourceViewer sourceViewer) {
		return new org.eclipse.jface.text.source.DefaultAnnotationHover();
	}
	
	public org.eclipse.jface.text.ITextHover getTextHover(org.eclipse.jface.text.source.ISourceViewer sourceViewer, String contentType) {
		return new org.emftext.sdk.concretesyntax.resource.cs.ui.CsTextHover(theEditor);
	}
	
	public org.eclipse.jface.text.hyperlink.IHyperlinkDetector[] getHyperlinkDetectors(org.eclipse.jface.text.source.ISourceViewer sourceViewer) {
		if (sourceViewer == null) {
			return null;
		}
		return new org.eclipse.jface.text.hyperlink.IHyperlinkDetector[] { new org.emftext.sdk.concretesyntax.resource.cs.ui.CsHyperlinkDetector(theEditor.getResource()) };
	}
	
}