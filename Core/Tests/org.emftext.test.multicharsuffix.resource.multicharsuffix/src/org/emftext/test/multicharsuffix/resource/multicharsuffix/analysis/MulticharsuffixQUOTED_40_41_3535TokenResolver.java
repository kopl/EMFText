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

package org.emftext.test.multicharsuffix.resource.multicharsuffix.analysis;

public class MulticharsuffixQUOTED_40_41_3535TokenResolver implements org.emftext.test.multicharsuffix.resource.multicharsuffix.IMulticharsuffixTokenResolver {
	
	private org.emftext.test.multicharsuffix.resource.multicharsuffix.analysis.MulticharsuffixDefaultTokenResolver defaultTokenResolver = new org.emftext.test.multicharsuffix.resource.multicharsuffix.analysis.MulticharsuffixDefaultTokenResolver(true);
	
	public String deResolve(Object value, org.eclipse.emf.ecore.EStructuralFeature feature, org.eclipse.emf.ecore.EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, "(", ")", "##");
		return result;
	}
	
	public void resolve(String lexem, org.eclipse.emf.ecore.EStructuralFeature feature, org.emftext.test.multicharsuffix.resource.multicharsuffix.IMulticharsuffixTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, "(", ")", "##");
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
