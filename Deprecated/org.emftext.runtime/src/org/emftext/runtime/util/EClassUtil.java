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
package org.emftext.runtime.util;

import org.eclipse.emf.ecore.EClass;

/**
 * A utility class that provides methods to handle EClasses.
 */
@Deprecated public class EClassUtil {
	
	public boolean isSubClass(EClass subClassCandidate, EClass superClass) {
		for (EClass superClassCandidate : subClassCandidate.getEAllSuperTypes()) {
			// There seem to be multiple instances of meta classes when accessed
			// through the generator model. Therefore, we compare by name.
			if (namesAndPackageURIsAreEqual(superClassCandidate, superClass)) {
				return true;
			}
		}
		return false;
	}

	public boolean namesAndPackageURIsAreEqual(EClass classA,
			EClass classB) {
		return namesAreEqual(classA, classB) && 
			packageURIsAreEqual(classA, classB);
	}

	public boolean packageURIsAreEqual(EClass classA,
			EClass classB) {
		String nsURI_A = classA.getEPackage().getNsURI();
		String nsURI_B = classB.getEPackage().getNsURI();
		return (nsURI_A == null && nsURI_B == null) || nsURI_A.equals(nsURI_B);
	}

	public boolean namesAreEqual(EClass classA, EClass classB) {
		return classA.getName().equals(classB.getName());
	}

	public boolean isConcrete(EClass eClass) {
		return !eClass.isAbstract() && !eClass.isInterface();
	}

	public boolean isNotConcrete(EClass eClass) {
		return !isConcrete(eClass);
	}
}
