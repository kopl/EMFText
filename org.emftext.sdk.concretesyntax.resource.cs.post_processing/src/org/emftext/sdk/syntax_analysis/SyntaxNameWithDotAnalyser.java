/*******************************************************************************
 * Copyright (c) 2006-2010 
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
package org.emftext.sdk.syntax_analysis;

import org.emftext.sdk.AbstractPostProcessor;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Option;
import org.emftext.sdk.concretesyntax.OptionTypes;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsResource;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.ECsProblemType;

/**
 * An analyser that checks if the "baseResourcePlugin" 
 * option is set if the syntax name contains one dot. And the the extension does
 * not contain more than one dot.
 */
public class SyntaxNameWithDotAnalyser extends AbstractPostProcessor {

	private static final String SYNTAX_NAME_MAY_CONTAIN_ONE_DOT_MAX = 
		"The syntax name contains more than one dot.";
	
	private static final String BASE_RESOURCE_PLUGIN_OPTION_MISSING = 
		"If the syntax contains one dot, the 'baseResourcePlugin' needs to be specified.";

	@Override
	public void analyse(CsResource resource, ConcreteSyntax syntax) {
		int numberOfDots = syntax.getName().split("\\.").length - 1;
		
		if (numberOfDots == 1) {
			//look for baseResourcePlugin option
			for(Option option : syntax.getOptions()) {
				if (option.getType() == OptionTypes.BASE_RESOURCE_PLUGIN) {
					return;
				}
			}
			addProblem(
					resource,
					ECsProblemType.SYNTAX_NAME_CONTAINS_DOTS,
					BASE_RESOURCE_PLUGIN_OPTION_MISSING,
					syntax);
			return;
		}
		if (numberOfDots > 1) {
			addProblem(
					resource,
					ECsProblemType.SYNTAX_NAME_CONTAINS_DOTS,
					SYNTAX_NAME_MAY_CONTAIN_ONE_DOT_MAX,
					syntax);
			return;
		}
	}

}