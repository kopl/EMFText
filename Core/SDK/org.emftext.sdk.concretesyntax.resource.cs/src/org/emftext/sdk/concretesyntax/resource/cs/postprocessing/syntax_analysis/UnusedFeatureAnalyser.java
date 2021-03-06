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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.emftext.sdk.CollectInFeatureHelper;
import org.emftext.sdk.concretesyntax.Choice;
import org.emftext.sdk.concretesyntax.CompoundDefinition;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Definition;
import org.emftext.sdk.concretesyntax.Rule;
import org.emftext.sdk.concretesyntax.Sequence;
import org.emftext.sdk.concretesyntax.Terminal;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsAnalysisProblemType;
import org.emftext.sdk.concretesyntax.resource.cs.postprocessing.AbstractPostProcessor;
import org.emftext.sdk.concretesyntax.resource.cs.postprocessing.quickfixes.AddSuppressWarningsAnnotationQuickFix;
import org.emftext.sdk.concretesyntax.resource.cs.util.CsLayoutUtil;

/**
 * A analyser that looks for features defined in the meta model that do
 * no have concrete syntax.
 */
public class UnusedFeatureAnalyser extends AbstractPostProcessor {

	@Override
	public void analyse(ConcreteSyntax syntax) {
		// get collect-in feature to tag them as used

		Map<EStructuralFeature, Rule> unusedReferencesWithOpposite = new LinkedHashMap<EStructuralFeature, Rule>();

		final EList<Rule> rules = syntax.getRules();
		for (Rule rule : rules) {
			GenClass genClass = rule.getMetaclass();
			if (genClass == null || genClass.eIsProxy()) {
				continue;
			}
			for (GenFeature genFeature : genClass.getAllGenFeatures()) {
				final EStructuralFeature ecoreFeature = genFeature.getEcoreFeature();
				if (ecoreFeature == null) {
					continue;
				}
				if (ecoreFeature.isDerived()) {
					continue;
				}
				if (new CollectInFeatureHelper().isCollectInFeature(rule.getSyntax(), ecoreFeature)) {
					continue;
				}
				Choice choice = rule.getDefinition();
				final boolean isUsed = isUsed(choice, genFeature);
				final EReference opposite = getOpposite(ecoreFeature);
				if (!isUsed) {
					if (opposite != null) {
						unusedReferencesWithOpposite.put(ecoreFeature, rule);
					} else {
						//do not warn if the layout reference has no syntax
						if (new CsLayoutUtil().findLayoutReference(genFeature.getGenClass().getEcoreClass()) != genFeature.getEcoreFeature()) {
							CsAnalysisProblemType problemType = CsAnalysisProblemType.FEATURE_WITHOUT_SYNTAX;
							addProblem(
									problemType, 
									"Feature " + genFeature.getGenClass().getName() + "." + genFeature.getName() + " has no syntax.", 
									rule,
									new AddSuppressWarningsAnnotationQuickFix(rule, problemType)
							);
						}
					}
				}
			}
		}

		List<EStructuralFeature> handledFeatures = new ArrayList<EStructuralFeature>();
		for (EStructuralFeature feature : unusedReferencesWithOpposite.keySet()) {
			if (handledFeatures.contains(feature)) {
				continue;
			}
			final EReference opposite = getOpposite(feature);
			handledFeatures.add(opposite);
			if (unusedReferencesWithOpposite.containsKey(opposite)) {
				// both are not defined
				Rule rule1 = unusedReferencesWithOpposite.get(feature);
				Rule rule2 = unusedReferencesWithOpposite.get(opposite);

				addProblem(CsAnalysisProblemType.OPPOSITE_FEATURE_WITHOUT_SYNTAX, "Feature " + getFeatureString(feature) + " (Opposite is " + getFeatureString(opposite) + ") has no syntax.", rule1);
				addProblem(CsAnalysisProblemType.OPPOSITE_FEATURE_WITHOUT_SYNTAX, "Feature " + getFeatureString(opposite) + " (Opposite is " + getFeatureString(feature) + ") has no syntax.", rule2);
			}
		}
	}

	private EReference getOpposite(EStructuralFeature ecoreFeature) {
		if (ecoreFeature instanceof EReference) {
			EReference ecoreReference = (EReference) ecoreFeature;
			return ecoreReference.getEOpposite();
		}
		return null;
	}


	private boolean isUsed(Choice choice, GenFeature genFeature) {
		for (Sequence sequence : choice.getOptions()) {
			for (Definition part : sequence.getParts()) {
				if (part instanceof Terminal) {
					Terminal terminal = (Terminal) part;
					GenFeature terminalGenFeature = terminal.getFeature();
					if (terminalGenFeature != null) {
						if (!terminalGenFeature.eIsProxy()) {
							String genFeatureName = genFeature.getName();
							if (genFeatureName == null) {
								continue;
							}
							if (genFeatureName.equals(terminalGenFeature.getName())) {
								return true;
							}
						}
					}
				} else if (part instanceof CompoundDefinition) {
					CompoundDefinition compound = (CompoundDefinition) part;
					Choice subChoice = compound.getDefinition();
					boolean isUsedInSubChoice = isUsed(subChoice, genFeature);
					if (isUsedInSubChoice) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private String getFeatureString(EStructuralFeature feature) {
		EClass containerClass = feature.getEContainingClass();
		return containerClass.getName() + "." + feature.getName();
	}
}
