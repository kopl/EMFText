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
 * A representation of the model object '<em><b>Normal Token Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines a token with a name and a regular expression.
 * <!-- end-model-doc -->
 *
 *
 * @see org.emftext.sdk.concretesyntax.ConcretesyntaxPackage#getNormalTokenDefinition()
 * @model
 * @generated
 */
public interface NormalTokenDefinition extends CompleteTokenDefinition, Annotable, RegexComposite {
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

} // NormalTokenDefinition
