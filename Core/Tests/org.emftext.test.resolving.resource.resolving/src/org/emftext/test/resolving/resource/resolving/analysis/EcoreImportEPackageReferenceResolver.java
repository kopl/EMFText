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
/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.emftext.test.resolving.resource.resolving.analysis;

public class EcoreImportEPackageReferenceResolver implements org.emftext.test.resolving.resource.resolving.IResolvingReferenceResolver<org.emftext.test.resolving.EcoreImport, org.eclipse.emf.ecore.EPackage> {
	
	private org.emftext.test.resolving.resource.resolving.analysis.ResolvingDefaultResolverDelegate<org.emftext.test.resolving.EcoreImport, org.eclipse.emf.ecore.EPackage> delegate = new org.emftext.test.resolving.resource.resolving.analysis.ResolvingDefaultResolverDelegate<org.emftext.test.resolving.EcoreImport, org.eclipse.emf.ecore.EPackage>();
	
	public void resolve(String identifier, org.emftext.test.resolving.EcoreImport container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.emftext.test.resolving.resource.resolving.IResolvingReferenceResolveResult<org.eclipse.emf.ecore.EPackage> result) {
		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
	}
	
	public String deResolve(org.eclipse.emf.ecore.EPackage element, org.emftext.test.resolving.EcoreImport container, org.eclipse.emf.ecore.EReference reference) {
		return delegate.deResolve(element, container, reference);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
