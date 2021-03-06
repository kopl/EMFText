/**
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
 *  
 */
package org.emftext.sdk.concretesyntax;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Token Redefinition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Redefines the regular expression of an imported token. Also, a new name is assigned to the token.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.emftext.sdk.concretesyntax.TokenRedefinition#getRedefinedToken <em>Redefined Token</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.emftext.sdk.concretesyntax.ConcretesyntaxPackage#getTokenRedefinition()
 * @model
 * @generated
 */
public interface TokenRedefinition extends Annotable, RegexComposite, CompleteTokenDefinition {
	/**
	 * Returns the value of the '<em><b>Redefined Token</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Redefined Token</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Redefined Token</em>' reference.
	 * @see #setRedefinedToken(CompleteTokenDefinition)
	 * @see org.emftext.sdk.concretesyntax.ConcretesyntaxPackage#getTokenRedefinition_RedefinedToken()
	 * @model required="true"
	 * @generated
	 */
	CompleteTokenDefinition getRedefinedToken();

	/**
	 * Sets the value of the '{@link org.emftext.sdk.concretesyntax.TokenRedefinition#getRedefinedToken <em>Redefined Token</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Redefined Token</em>' reference.
	 * @see #getRedefinedToken()
	 * @generated
	 */
	void setRedefinedToken(CompleteTokenDefinition value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='org.emftext.sdk.concretesyntax.RegexComposer composer = org.emftext.sdk.concretesyntax.ConcretesyntaxFactory.eINSTANCE.createRegexComposer();\nreturn composer.getComposedRegex(this, new org.eclipse.emf.common.util.BasicEList< org.emftext.sdk.concretesyntax.AbstractTokenDefinition>());'"
	 * @generated
	 */
	String getRegex();

} // TokenRedefinition
