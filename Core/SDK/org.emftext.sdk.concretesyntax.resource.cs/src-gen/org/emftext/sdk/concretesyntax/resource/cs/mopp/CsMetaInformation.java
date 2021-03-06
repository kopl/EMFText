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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource.Factory;

public class CsMetaInformation implements org.emftext.sdk.concretesyntax.resource.cs.ICsMetaInformation {
	
	public String getSyntaxName() {
		return "cs";
	}
	
	public String getURI() {
		return "http://www.emftext.org/sdk/concretesyntax";
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsTextScanner createLexer() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsAntlrScanner(new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsLexer());
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsTextParser createParser(InputStream inputStream, String encoding) {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsParser().createInstance(inputStream, encoding);
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsTextPrinter createPrinter(OutputStream outputStream, org.emftext.sdk.concretesyntax.resource.cs.ICsTextResource resource) {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsPrinter2(outputStream, resource);
	}
	
	public EClass[] getClassesWithSyntax() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsSyntaxCoverageInformationProvider().getClassesWithSyntax();
	}
	
	public EClass[] getStartSymbols() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsSyntaxCoverageInformationProvider().getStartSymbols();
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsReferenceResolverSwitch getReferenceResolverSwitch() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsReferenceResolverSwitch();
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsTokenResolverFactory getTokenResolverFactory() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsTokenResolverFactory();
	}
	
	public String getPathToCSDefinition() {
		return "org.emftext.sdk.concretesyntax/metamodel/concretesyntax.cs";
	}
	
	public String[] getTokenNames() {
		return org.emftext.sdk.concretesyntax.resource.cs.mopp.CsParser.tokenNames;
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsTokenStyle getDefaultTokenStyle(String tokenName) {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsTokenStyleInformationProvider().getDefaultTokenStyle(tokenName);
	}
	
	public Collection<org.emftext.sdk.concretesyntax.resource.cs.ICsBracketPair> getBracketPairs() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsBracketInformationProvider().getBracketPairs();
	}
	
	public EClass[] getFoldableClasses() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsFoldingInformationProvider().getFoldableClasses();
	}
	
	public Factory createResourceFactory() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsResourceFactory();
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.mopp.CsNewFileContentProvider getNewFileContentProvider() {
		return new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsNewFileContentProvider();
	}
	
	public void registerResourceFactory() {
		// if no resource factory registered, register delegator
		if (Factory.Registry.INSTANCE.getExtensionToFactoryMap().get(getSyntaxName()) == null) {
			Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(getSyntaxName(), new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsResourceFactoryDelegator());
		}
	}
	
	/**
	 * Returns the key of the option that can be used to register a preprocessor that
	 * is used as a pipe when loading resources. This key is language-specific. To
	 * register one preprocessor for multiple resource types, it must be registered
	 * individually using all keys.
	 */
	public String getInputStreamPreprocessorProviderOptionKey() {
		return getSyntaxName() + "_" + "INPUT_STREAM_PREPROCESSOR_PROVIDER";
	}
	
	/**
	 * Returns the key of the option that can be used to register a post-processors
	 * that are invoked after loading resources. This key is language-specific. To
	 * register one post-processor for multiple resource types, it must be registered
	 * individually using all keys.
	 */
	public String getResourcePostProcessorProviderOptionKey() {
		return getSyntaxName() + "_" + "RESOURCE_POSTPROCESSOR_PROVIDER";
	}
	
	public String getLaunchConfigurationType() {
		return "org.emftext.sdk.concretesyntax.resource.cs.ui.launchConfigurationType";
	}
	
	public org.emftext.sdk.concretesyntax.resource.cs.ICsNameProvider createNameProvider() {
		return new org.emftext.sdk.concretesyntax.resource.cs.analysis.CsDefaultNameProvider();
	}
	
	public String[] getSyntaxHighlightableTokenNames() {
		org.emftext.sdk.concretesyntax.resource.cs.mopp.CsAntlrTokenHelper tokenHelper = new org.emftext.sdk.concretesyntax.resource.cs.mopp.CsAntlrTokenHelper();
		List<String> highlightableTokens = new ArrayList<String>();
		String[] parserTokenNames = getTokenNames();
		for (int i = 0; i < parserTokenNames.length; i++) {
			// If ANTLR is used we need to normalize the token names
			if (!tokenHelper.canBeUsedForSyntaxHighlighting(i)) {
				continue;
			}
			String tokenName = tokenHelper.getTokenName(parserTokenNames, i);
			if (tokenName == null) {
				continue;
			}
			highlightableTokens.add(tokenName);
		}
		highlightableTokens.add(org.emftext.sdk.concretesyntax.resource.cs.mopp.CsTokenStyleInformationProvider.TASK_ITEM_TOKEN_NAME);
		return highlightableTokens.toArray(new String[highlightableTokens.size()]);
	}
	
}
