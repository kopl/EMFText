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

package org.emftext.sdk.concretesyntax.resource.cs.mopp;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * <p>
 * A factory for ContextDependentURIFragments. Given a feasible reference
 * resolver, this factory returns a matching fragment that used the resolver to
 * resolver proxy objects.
 * </p>
 * 
 * @param <ContainerType> the type of the class containing the reference to be
 * resolved
 * @param <ReferenceType> the type of the reference to be resolved
 */
public class CsContextDependentURIFragmentFactory<ContainerType extends EObject, ReferenceType extends EObject>  implements org.emftext.sdk.concretesyntax.resource.cs.ICsContextDependentURIFragmentFactory<ContainerType, ReferenceType> {
	
	private final org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolver<ContainerType, ReferenceType> resolver;
	
	public CsContextDependentURIFragmentFactory(org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolver<ContainerType, ReferenceType> resolver) {
		this.resolver = resolver;
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsContextDependentURIFragment<?> create(String identifier, ContainerType container, EReference reference, int positionInReference, EObject proxy) {
		
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsContextDependentURIFragment<ContainerType, ReferenceType>(identifier, container, reference, positionInReference, proxy) {
			public org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolver<ContainerType, ReferenceType> getResolver() {
				return resolver;
			}
		};
	}
}
