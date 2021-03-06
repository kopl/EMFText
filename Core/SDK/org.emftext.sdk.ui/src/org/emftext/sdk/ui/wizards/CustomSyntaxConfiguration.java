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
package org.emftext.sdk.ui.wizards;

/**
 * This class can be used to parameterize the CustomSyntaxGenerator.
 */
public class CustomSyntaxConfiguration {

	// keyword options
	public enum KeywordStyle {
		NO_KEYWORDS,
		LOWER,
		UPPER,
		CAPITALIZED
	}
	
	private KeywordStyle keywordStyle;
	// comment options
	private boolean useSingleLineComments;
	private String singleLineCommentPrefix;
	private boolean useMultiLineComments;
	private String multiLineCommentPrefix;
	private String multiLineCommentSuffix;
	// enclose
	private boolean encloseContainments;
	private String containmentOpener;
	private String containmentCloser;
	
	private boolean qualifyContainments;
	private boolean qualifyAttributes;
	private boolean qualifyCrossReferences;
	private String qualificationDelimiter;
	
	private boolean terminateTerminalElements; // add keyword after elements that do not have containments
	private String terminatingKeyword;

	private boolean modifierStyleForBooleanAttributes;
	private boolean modifierStyleForEnumAttributes;
	private boolean quoteStringAttributes;
	private String stringAttributeQuote;
	private boolean quoteIdentifierAttributes;
	private String identifierAttributeQuote;
	
	// TODO implement GUI and syntax generation for subsequent attributes
	// identifier style
	private boolean identifiersWithUnderscore;
	private boolean identifiersWithDash;
	private boolean identifiersWithDigitsFirst;
	
	private boolean separateIdentifyingAttributes;
	
	private boolean overrideExistingRules;
	
	private boolean generateRulesForImportedModels;
	
	public boolean isQuoteStringAttributes() {
		return quoteStringAttributes;
	}

	public void setQuoteStringAttributes(boolean quoteStringAttributes) {
		this.quoteStringAttributes = quoteStringAttributes;
	}

	public String getStringAttributeQuote() {
		return stringAttributeQuote;
	}

	public void setStringAttributeQuote(String stringAttributeQuote) {
		this.stringAttributeQuote = stringAttributeQuote;
	}

	public boolean isOverrideExistingRules() {
		return overrideExistingRules;
	}

	public void setOverrideExistingRules(boolean overrideExistingRules) {
		this.overrideExistingRules = overrideExistingRules;
	}

	public KeywordStyle getKeywordStyle() {
		return keywordStyle;
	}

	public void setKeywordStyle(KeywordStyle newStyle) {
		this.keywordStyle = newStyle;
	}

	public boolean isUseSingleLineComments() {
		return useSingleLineComments;
	}

	public void setUseSingleLineComments(boolean useSingleLineComments) {
		this.useSingleLineComments = useSingleLineComments;
	}

	public String getSingleLineCommentPrefix() {
		return singleLineCommentPrefix;
	}

	public void setSingleLineCommentPrefix(String singleLineCommentPrefix) {
		this.singleLineCommentPrefix = singleLineCommentPrefix;
	}

	public boolean isUseMultiLineComments() {
		return useMultiLineComments;
	}

	public void setUseMultiLineComments(boolean useMultiLineComments) {
		this.useMultiLineComments = useMultiLineComments;
	}

	public String getMultiLineCommentPrefix() {
		return multiLineCommentPrefix;
	}

	public void setMultiLineCommentPrefix(String multiLineCommentPrefix) {
		this.multiLineCommentPrefix = multiLineCommentPrefix;
	}

	public String getMultiLineCommentSuffix() {
		return multiLineCommentSuffix;
	}

	public void setMultiLineCommentSuffix(String multiLineCommentSuffix) {
		this.multiLineCommentSuffix = multiLineCommentSuffix;
	}

	public boolean isEncloseContainments() {
		return encloseContainments;
	}

	public void setEncloseContainments(boolean encloseContainments) {
		this.encloseContainments = encloseContainments;
	}

	public String getContainmentOpener() {
		return containmentOpener;
	}

	public void setContainmentOpener(String containmentOpener) {
		this.containmentOpener = containmentOpener;
	}

	public String getContainmentCloser() {
		return containmentCloser;
	}

	public void setContainmentCloser(String containmentCloser) {
		this.containmentCloser = containmentCloser;
	}

	public boolean isIdentifiersWithUnderscore() {
		return identifiersWithUnderscore;
	}

	public void setIdentifiersWithUnderscore(boolean identifiersWithUnderscore) {
		this.identifiersWithUnderscore = identifiersWithUnderscore;
	}

	public boolean isIdentifiersWithDash() {
		return identifiersWithDash;
	}

	public void setIdentifiersWithDash(boolean identifiersWithDash) {
		this.identifiersWithDash = identifiersWithDash;
	}

	public boolean isIdentifiersWithDigitsFirst() {
		return identifiersWithDigitsFirst;
	}

	public void setIdentifiersWithDigitsFirst(boolean identifiersWithDigitsFirst) {
		this.identifiersWithDigitsFirst = identifiersWithDigitsFirst;
	}

	public boolean isTerminateTerminalElements() {
		return terminateTerminalElements;
	}

	public void setTerminateTerminalElements(boolean terminateTerminalElements) {
		this.terminateTerminalElements = terminateTerminalElements;
	}

	public String getTerminatingKeyword() {
		return terminatingKeyword;
	}

	public void setTerminatingKeyword(String terminatingKeyword) {
		this.terminatingKeyword = terminatingKeyword;
	}

	public boolean isQualifyContainments() {
		return qualifyContainments;
	}

	public void setQualifyContainments(boolean qualifyContainments) {
		this.qualifyContainments = qualifyContainments;
	}

	public boolean isQualifyAttributes() {
		return qualifyAttributes;
	}

	public void setQualifyAttributes(boolean qualifyAttributes) {
		this.qualifyAttributes = qualifyAttributes;
	}

	public boolean isQualifyCrossReferences() {
		return qualifyCrossReferences;
	}

	public void setQualifyCrossReferences(boolean qualifyCrossReferences) {
		this.qualifyCrossReferences = qualifyCrossReferences;
	}

	public String getQualificationDelimiter() {
		return qualificationDelimiter;
	}

	public void setQualificationDelimiter(String qualificationDelimiter) {
		this.qualificationDelimiter = qualificationDelimiter;
	}

	public boolean isSeparateIdentifyingAttributes() {
		return separateIdentifyingAttributes;
	}

	public void setSeparateIdentifyingAttributes(
			boolean separateIdentifyingAttributes) {
		this.separateIdentifyingAttributes = separateIdentifyingAttributes;
	}

	public boolean isModifierStyleForBooleanAttributes() {
		return modifierStyleForBooleanAttributes;
	}

	public void setModifierStyleForBooleanAttributes(
			boolean modifierStyleForBooleanAttributes) {
		this.modifierStyleForBooleanAttributes = modifierStyleForBooleanAttributes;
	}

	public boolean isModifierStyleForEnumAttributes() {
		return modifierStyleForEnumAttributes;
	}

	public void setModifierStyleForEnumAttributes(
			boolean modifierStyleForEnumAttributes) {
		this.modifierStyleForEnumAttributes = modifierStyleForEnumAttributes;
	}

	public boolean isQuoteIdentifierAttributes() {
		return quoteIdentifierAttributes;
	}

	public void setQuoteIdentifierAttributes(boolean quoteIdentifierAttributes) {
		this.quoteIdentifierAttributes = quoteIdentifierAttributes;
	}

	public String getIdentifierAttributeQuote() {
		return identifierAttributeQuote;
	}

	public void setIdentifierAttributeQuote(String identifierAttributeQuote) {
		this.identifierAttributeQuote = identifierAttributeQuote;
	}

	public void setGenerateRulesForImportedModels(
			boolean generateRulesForImportedModels) {
		this.generateRulesForImportedModels = generateRulesForImportedModels;
	}

	public boolean isGenerateRulesForImportedModels() {
		return generateRulesForImportedModels;
	}
}
