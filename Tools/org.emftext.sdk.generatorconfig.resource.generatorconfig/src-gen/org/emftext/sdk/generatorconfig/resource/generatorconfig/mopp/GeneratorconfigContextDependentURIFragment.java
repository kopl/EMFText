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
package org.emftext.sdk.generatorconfig.resource.generatorconfig.mopp;

// Standard implementation of <code>IContextDependentURIFragment</code>.
//
// @param <ContainerType> the type of the object that contains the reference which shall be resolved by this fragment.
// @param <ReferenceType> the type of the reference which shall be resolved by this fragment.
//
public abstract class GeneratorconfigContextDependentURIFragment<ContainerType extends org.eclipse.emf.ecore.EObject, ReferenceType extends org.eclipse.emf.ecore.EObject> implements org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigContextDependentURIFragment<ReferenceType> {

	protected String identifier;
	protected ContainerType container;
	protected org.eclipse.emf.ecore.EReference reference;
	protected int positionInReference;
	protected org.eclipse.emf.ecore.EObject proxy;
	protected org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigReferenceResolveResult<ReferenceType> result;

	private boolean resolving;

	public GeneratorconfigContextDependentURIFragment(String identifier, ContainerType container, org.eclipse.emf.ecore.EReference reference, int positionInReference, org.eclipse.emf.ecore.EObject proxy) {
		this.identifier = identifier;
		this.container = container;
		this.reference = reference;
		this.positionInReference = positionInReference;
		this.proxy = proxy;
	}

	public boolean isResolved() {
		return result != null;
	}

	public synchronized org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigReferenceResolveResult<ReferenceType> resolve() {
		if (resolving) {
			return null;
		}
		resolving = true;
		if (result == null || !result.wasResolved()) {
			result = new org.emftext.sdk.generatorconfig.resource.generatorconfig.mopp.GeneratorconfigReferenceResolveResult<ReferenceType>(false);
			//set an initial default error message
			result.setErrorMessage(getStdErrorMessage());

			org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigReferenceResolver<ContainerType, ReferenceType> resolver = getResolver();
			//do the actual resolving
			resolver.resolve(getIdentifier(), getContainer(), getReference(), getPositionInReference(), false, result);

			//EMFText allows proxies to resolve to multiple objects
			//the first is returned, the others are added here to the reference
			if(result.wasResolvedMultiple()) {
				handleMultipleResults();
			}
		}
		resolving = false;
		return result;
	}

	public abstract org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigReferenceResolver<ContainerType, ReferenceType> getResolver();

	private void handleMultipleResults() {
		org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> list = null;
		java.lang.Object temp = container.eGet(reference);
		if (temp instanceof org.eclipse.emf.common.util.EList<?>) {
			list = org.emftext.sdk.generatorconfig.resource.generatorconfig.util.GeneratorconfigCastUtil.cast(temp);
		}

		boolean first = true;
		for(org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigReferenceMapping<ReferenceType> mapping : result.getMappings()) {
			if (first) {
				first = false;
			} else if (list != null) {
				addResultToList(mapping, proxy, list);
			} else {
				org.emftext.sdk.generatorconfig.resource.generatorconfig.mopp.GeneratorconfigPlugin.logError(container.eClass().getName() + "." + reference.getName() + " has multiplicity 1 but was resolved to multiple elements", null);
			}
		}
	}

	private void addResultToList(org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigReferenceMapping<ReferenceType> mapping, org.eclipse.emf.ecore.EObject proxy, org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> list) {
		org.eclipse.emf.ecore.EObject target = null;
		int proxyPosition = list.indexOf(proxy);

		if (mapping instanceof org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigElementMapping<?>) {
			target = ((org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigElementMapping<ReferenceType>) mapping).getTargetElement();
		} else if (mapping instanceof org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigURIMapping<?>) {
			target = org.eclipse.emf.ecore.util.EcoreUtil.copy(proxy);
			org.eclipse.emf.common.util.URI uri = ((org.emftext.sdk.generatorconfig.resource.generatorconfig.IGeneratorconfigURIMapping<ReferenceType>) mapping).getTargetIdentifier();
			((org.eclipse.emf.ecore.InternalEObject) target).eSetProxyURI(uri);
		} else {
			assert false;
		}
		try {
			// if target is an another proxy and list is "unique"
			// add() will try to resolve the new proxy to check for uniqueness.
			// There seems to be no way to avoid that. Until now this does not
			// cause any problems.
			if (proxyPosition + 1 == list.size()) {
				list.add(target);
			} else {
				list.add(proxyPosition + 1, target);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private String getStdErrorMessage() {
		String typeName = this.getReference().getEType().getName();
		String msg = typeName + " '" + identifier + "' not declared";
		return msg;
	}

	public String getIdentifier() {
		return identifier;
	}

	public ContainerType getContainer() {
		return container;
	}

	public org.eclipse.emf.ecore.EReference getReference() {
		return reference;
	}

	public int getPositionInReference() {
		return positionInReference;
	}

	public org.eclipse.emf.ecore.EObject getProxy() {
		return proxy;
	}

}
