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
package org.emftext.sdk.concretesyntax.resource.cs.postprocessing.quickfixes;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsQuickFix;

/**
 * A quick fix that removes references to a collection of target
 * objects.
 */
public class RemoveReferenceQuickFix extends CsQuickFix {

	private EObject container;
	private EReference reference;
	private List<? extends EObject> targets;

	public RemoveReferenceQuickFix(
			String label, 
			EObject container,
			EReference reference, 
			List<? extends EObject> targets) {
		super(label, "IMG_ETOOL_DELETE", container);
		this.container = container;
		this.reference = reference;
		this.targets = targets;
	}

	@Override
	public void applyChanges() {
		for (EObject target : targets) {
			EcoreUtil.remove(container, reference, target);
		}
	}
}
