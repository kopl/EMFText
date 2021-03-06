/*******************************************************************************
 * Copyright (c) 2006-2015
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Dresden, Amtsgericht Dresden, HRB 34001
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Dresden, Germany
 *      - initial API and implementation
 ******************************************************************************/

package org.emftext.sdk.concretesyntax.resource.cs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractCsOutlinePageAction extends Action {
	
	private String preferenceKey = this.getClass().getSimpleName() + ".isChecked";
	
	private org.emftext.sdk.concretesyntax.resource.cs.ui.CsOutlinePageTreeViewer treeViewer;
	
	public AbstractCsOutlinePageAction(org.emftext.sdk.concretesyntax.resource.cs.ui.CsOutlinePageTreeViewer treeViewer, String text, int style) {
		super(text, style);
		this.treeViewer = treeViewer;
	}
	
	public void initialize(String imagePath) {
		ImageDescriptor descriptor = org.emftext.sdk.concretesyntax.resource.cs.ui.CsImageProvider.INSTANCE.getImageDescriptor(imagePath);
		setDisabledImageDescriptor(descriptor);
		setImageDescriptor(descriptor);
		setHoverImageDescriptor(descriptor);
		boolean checked = org.emftext.sdk.concretesyntax.resource.cs.ui.CsUIPlugin.getDefault().getPreferenceStore().getBoolean(preferenceKey);
		valueChanged(checked, false);
	}
	
	@Override
	public void run() {
		if (keepState()) {
			valueChanged(isChecked(), true);
		} else {
			runBusy(true);
		}
	}
	
	public void runBusy(final boolean on) {
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
			public void run() {
				runInternal(on);
			}
		});
	}
	
	public abstract void runInternal(boolean on);
	
	private void valueChanged(boolean on, boolean store) {
		setChecked(on);
		runBusy(on);
		if (store) {
			org.emftext.sdk.concretesyntax.resource.cs.ui.CsUIPlugin.getDefault().getPreferenceStore().setValue(preferenceKey, on);
		}
	}
	
	public boolean keepState() {
		return true;
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ui.CsOutlinePageTreeViewer getTreeViewer() {
		return treeViewer;
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ui.CsOutlinePageTreeViewerComparator getTreeViewerComparator() {
		return (org.emftext.sdk.concretesyntax.resource.cs.ui.CsOutlinePageTreeViewerComparator) treeViewer.getComparator();
	}
	
}
