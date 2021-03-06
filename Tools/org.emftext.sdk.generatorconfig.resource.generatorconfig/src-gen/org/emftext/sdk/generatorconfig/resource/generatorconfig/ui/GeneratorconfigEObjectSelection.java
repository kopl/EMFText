/*******************************************************************************
 * Copyright (c) 2006-2012
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Berlin, Germany
 *      - initial API and implementation
 ******************************************************************************/
package org.emftext.sdk.generatorconfig.resource.generatorconfig.ui;

public class GeneratorconfigEObjectSelection implements org.eclipse.jface.viewers.IStructuredSelection {

	private final org.eclipse.emf.ecore.EObject selectedObject;
	private final boolean highlighting;

	public GeneratorconfigEObjectSelection(org.eclipse.emf.ecore.EObject selectedObject, boolean highlighting) {
		super();
		this.selectedObject = selectedObject;
		this.highlighting = highlighting;
	}

	public org.eclipse.emf.ecore.EObject getSelectedObject() {
		return selectedObject;
	}

	public boolean doHighlighting() {
		return highlighting;
	}

	public boolean isEmpty() {
		return false;
	}

	public java.lang.Object getFirstElement() {
		return selectedObject;
	}

	public java.util.Iterator<?> iterator() {
		return new java.util.Iterator<org.eclipse.emf.ecore.EObject>() {

			private boolean hasNext = true;

			public boolean hasNext() {
				return hasNext;
			}

			public org.eclipse.emf.ecore.EObject next(){
				hasNext = false;
				return selectedObject;
			}

			public void remove() {
			}
		};
	}

	public int size() {
		return 1;
	}

	public java.lang.Object[] toArray() {
		return new java.lang.Object[] {selectedObject};
	}

	public java.util.List<?> toList() {
		java.util.ArrayList<org.eclipse.emf.ecore.EObject> list = new java.util.ArrayList<org.eclipse.emf.ecore.EObject>();
		list.add(selectedObject);
		return list;
	}
}
