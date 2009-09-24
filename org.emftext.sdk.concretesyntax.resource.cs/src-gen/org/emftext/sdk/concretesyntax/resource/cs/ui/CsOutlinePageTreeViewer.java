package org.emftext.sdk.concretesyntax.resource.cs.ui;

// This custom implementation of a TreeViewer expands the tree
// automatically up to a specified depth.
public class CsOutlinePageTreeViewer extends org.eclipse.jface.viewers.TreeViewer {
	
	public CsOutlinePageTreeViewer(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
	}
	
	public void setSelection(org.eclipse.jface.viewers.ISelection selection, boolean reveal) {
		if (selection instanceof org.emftext.sdk.concretesyntax.resource.cs.ui.CsEObjectSelection) {
			super.setSelection(selection, reveal);
		}
	}
	
	public void refresh(Object element, boolean updateLabels) {
		super.refresh(element, updateLabels);
		expandToLevel(getAutoExpandLevel());
	}
	
	public void refresh(Object element) {
		super.refresh(element);
		expandToLevel(getAutoExpandLevel());
	}
	
	public void refresh() {
		super.refresh();
		expandToLevel(getAutoExpandLevel());
	}
	
	public void refresh(boolean updateLabels) {
		super.refresh(updateLabels);
		expandToLevel(getAutoExpandLevel());
	}
}
