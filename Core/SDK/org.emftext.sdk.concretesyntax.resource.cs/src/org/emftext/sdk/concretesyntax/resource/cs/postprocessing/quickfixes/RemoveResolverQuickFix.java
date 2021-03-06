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

import java.io.File;

import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.resource.cs.ICsQuickFix;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsQuickFix;

/**
 * A quick fix that removes an unused resolver class from the analysis package.
 */
public class RemoveResolverQuickFix extends CsQuickFix implements ICsQuickFix {

	private File resolverFile;

	public RemoveResolverQuickFix(ConcreteSyntax syntax, File resolverFile) {
		super("Remove unused resolver class", "IMG_ELCL_REMOVE", syntax);
		this.resolverFile = resolverFile;
	}

	@Override
	public void applyChanges() {
		resolverFile.delete();
		// TODO refresh resolver directory in workspace
	}
}
