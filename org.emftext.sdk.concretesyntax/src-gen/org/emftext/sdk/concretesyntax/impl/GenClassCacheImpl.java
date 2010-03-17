/**
 * Copyright (c) 2006-2010 
 * Software Technology Group, Dresden University of Technology
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Software Technology Group - TU Dresden, Germany 
 *       - initial API and implementation
 * 
 *
 * $Id$
 */
package org.emftext.sdk.concretesyntax.impl;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.emftext.sdk.concretesyntax.ConcretesyntaxPackage;
import org.emftext.sdk.concretesyntax.GenClassCache;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Gen Class Cache</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.emftext.sdk.concretesyntax.impl.GenClassCacheImpl#get_qualifiedInterfaceNameCache <em>qualified Interface Name Cache</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GenClassCacheImpl extends EObjectImpl implements GenClassCache {
	/**
	 * The cached value of the '{@link #get_qualifiedInterfaceNameCache() <em>qualified Interface Name Cache</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #get_qualifiedInterfaceNameCache()
	 * @generated
	 * @ordered
	 */
	protected EMap<GenClass, String> _qualifiedInterfaceNameCache;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GenClassCacheImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConcretesyntaxPackage.Literals.GEN_CLASS_CACHE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EMap<GenClass, String> get_qualifiedInterfaceNameCache() {
		if (_qualifiedInterfaceNameCache == null) {
			_qualifiedInterfaceNameCache = new EcoreEMap<GenClass,String>(ConcretesyntaxPackage.Literals.GEN_CLASS_CACHE_ENTRY, GenClassCacheEntryImpl.class, this, ConcretesyntaxPackage.GEN_CLASS_CACHE__QUALIFIED_INTERFACE_NAME_CACHE);
		}
		return _qualifiedInterfaceNameCache;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getQualifiedInterfaceName(GenClass genClass) {
		if ( ! get_qualifiedInterfaceNameCache ( ) .containsKey ( genClass ) ) { 
			java.lang.String qualifiedInterfaceName = genClass .getQualifiedInterfaceName ( ) ; 
			get_qualifiedInterfaceNameCache ( ) .put ( genClass , qualifiedInterfaceName ) ; 
		} 
		return get_qualifiedInterfaceNameCache ( ) .get ( genClass ) ; 
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConcretesyntaxPackage.GEN_CLASS_CACHE__QUALIFIED_INTERFACE_NAME_CACHE:
				return ((InternalEList<?>)get_qualifiedInterfaceNameCache()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConcretesyntaxPackage.GEN_CLASS_CACHE__QUALIFIED_INTERFACE_NAME_CACHE:
				if (coreType) return get_qualifiedInterfaceNameCache();
				else return get_qualifiedInterfaceNameCache().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ConcretesyntaxPackage.GEN_CLASS_CACHE__QUALIFIED_INTERFACE_NAME_CACHE:
				((EStructuralFeature.Setting)get_qualifiedInterfaceNameCache()).set(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ConcretesyntaxPackage.GEN_CLASS_CACHE__QUALIFIED_INTERFACE_NAME_CACHE:
				get_qualifiedInterfaceNameCache().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ConcretesyntaxPackage.GEN_CLASS_CACHE__QUALIFIED_INTERFACE_NAME_CACHE:
				return _qualifiedInterfaceNameCache != null && !_qualifiedInterfaceNameCache.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //GenClassCacheImpl