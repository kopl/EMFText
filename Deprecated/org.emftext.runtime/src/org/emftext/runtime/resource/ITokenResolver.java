/*******************************************************************************
 * Copyright (c) 2006-2009 
 * Software Technology Group, Dresden University of Technology
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany 
 *      - initial API and implementation
 ******************************************************************************/
package org.emftext.runtime.resource;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Basic Interface to directly manipulate parsed tokens and convert them into the attribute type in the meta model.
 * All generated TokenResolvers inherit from JavaBasedTokenResolver which does the standard conversion to Java Types.
 * 
 * @see org.emftext.runtime.resource.impl.JavaBasedTokenResolver
 * @see org.emftext.sdk.codegen.TokenResolverGenerator
 * 
 * @author Sven Karol (Sven.Karol@tu-dresden.de)
 */
public interface ITokenResolver extends IConfigurable {
	
	/**
	 * Converts a token into an Object (might be a String again). 
	 * 
	 * @param lexem the parsed String
	 * @param feature the corresponding feature in the meta model
	 * @param result the result of resolving the lexem, can be used to add processing errors
	 */
	public void resolve(String lexem, EStructuralFeature feature, ITokenResolveResult result);
	
	/**
	 * Does the inverse mapping from Object to a lexem which can be printed.
	 * 
	 * @param value the Object to be printed as String
	 * @param feature the corresponding feature
	 * @param container the container of the object
	 * @return the String representation or null if a problem occurred
	 */
	public String deResolve(Object value, EStructuralFeature feature, EObject container);
}
