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
package org.emftext.sdk.concretesyntax.resource.cs.postprocessing.syntax_analysis;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.ecore.EClass;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.ConcretesyntaxPackage;
import org.emftext.sdk.concretesyntax.Containment;
import org.emftext.sdk.concretesyntax.EClassUtil;
import org.emftext.sdk.concretesyntax.Rule;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsAnalysisProblemType;
import org.emftext.sdk.concretesyntax.resource.cs.postprocessing.AbstractPostProcessor;
import org.emftext.sdk.util.EObjectUtil;

public class SubclassRestrictionAnalyser extends AbstractPostProcessor {

	@Override
	public void analyse(ConcreteSyntax syntax) {
		if (syntax.isAbstract()) {
			return;
		}
		List<Rule> rules = syntax.getAllRules();
		for (Rule rule : rules) {
			Collection<Containment> containments = EObjectUtil
					.getObjectsByType(rule.eAllContents(),
							ConcretesyntaxPackage.eINSTANCE.getContainment());
			for (Containment containment : containments) {
				boolean hasSyntax = analyseContainment(rules, containment);
				if (!hasSyntax) {
					CsAnalysisProblemType problemType = CsAnalysisProblemType.NO_SYNTAX_FOR_CONTAINMENT_REFERENCE;
					String message = "There is no syntax defined for the type of the containment reference or any of the allowed subclasses.";
					addProblem(problemType, message, containment);
				}
			}
		}
	}

	/**
	 * Checks whether the given containment refers to a type for which syntax is
	 * defined. If the containment has subclass restrictions, this methods does
	 * only consider syntax rules for the subclasses.
	 * 
	 * @param rules
	 * @param containment
	 * @return
	 */
	private boolean analyseContainment(List<Rule> rules, Containment containment) {
		List<GenClass> allowedTypes = containment.getAllowedSubTypes();
		for (GenClass allowedType : allowedTypes) {
			EClass allowedEType = allowedType.getEcoreClass();
			for (Rule existingRule : rules) {
				EClass metaclass = existingRule.getMetaclass().getEcoreClass();
				EClassUtil eClassUtil = existingRule.getSyntax().getEClassUtil();
				boolean isEqual = eClassUtil.namesAndPackageURIsAreEqual(metaclass, allowedEType);
				boolean isSubclass = eClassUtil.isSubClass(metaclass, allowedEType);
				if (isEqual || isSubclass) {
					return true;
				}
			}
		}
		return false;
	}
}
