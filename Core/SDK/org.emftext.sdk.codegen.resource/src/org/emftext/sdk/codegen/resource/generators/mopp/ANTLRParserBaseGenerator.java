/*******************************************************************************
 * Copyright (c) 2006-2014
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
package org.emftext.sdk.codegen.resource.generators.mopp;

import static de.devboost.codecomposers.java.ClassNameConstants.LIST;
import static de.devboost.codecomposers.java.ClassNameConstants.MAP;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.ANTLR_PARSER;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.COLLECTION;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.COMMON_TOKEN;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.E_OBJECT_IMPL;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.INTERNAL_E_OBJECT;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.INVOCATION_HANDLER;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.METHOD;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.PROXY;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.RECOGNIZER_SHARED_STATE;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.TOKEN;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.TOKEN_STREAM;

import org.emftext.sdk.codegen.parameters.ArtifactParameter;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.GeneratorUtil;
import org.emftext.sdk.codegen.resource.generators.JavaBaseGenerator;

import de.devboost.codecomposers.StringComposite;
import de.devboost.codecomposers.java.JavaComposite;

public class ANTLRParserBaseGenerator extends JavaBaseGenerator<ArtifactParameter<GenerationContext>> {

	private final GeneratorUtil generatorUtil = new GeneratorUtil();

	@Override
	public void generateJavaContents(JavaComposite sc) {
		
		sc.add("package " + getResourcePackageName() + ";");sc.addLineBreak();sc.addImportsPlaceholder();
		sc.addLineBreak();
		sc.add("public abstract class " + getResourceClassName() + " extends " + ANTLR_PARSER(sc) + " implements " + iTextParserClassName + " {");
		sc.addLineBreak();
		addFields(sc);
		addConstructors(sc);
		addMethods(sc);
		sc.add("}");
	}

	private void addFields(JavaComposite sc) {
		sc.addJavadoc("The index of the last token that was handled by retrieveLayoutInformation().");
		sc.add("private int lastPosition2;");
		sc.addLineBreak();
		
		sc.addJavadoc("A collection to store all anonymous tokens.");
		sc.add("protected " + sc.declareArrayList("anonymousTokens",COMMON_TOKEN(sc)));
		sc.addLineBreak();
		
		sc.addJavadoc(
			"A collection that is filled with commands to be executed after parsing. " +
			"This collection is cleared before parsing starts and returned as part of " +
			"the parse result object."
		);
		sc.add("protected " + COLLECTION(sc) + "<" + iCommandClassName + "<" + iTextResourceClassName + ">> postParseCommands;");
		sc.addLineBreak();
		
		sc.addJavadoc(
			"A copy of the options that were used to load the text resource. " +
			"This map is filled when the parser is created."
		);
		sc.add("private " + MAP(sc) + "<?, ?> options;");
		sc.addLineBreak();
		
		sc.addJavadoc(
				"A flag that indicates whether this parser runs in a special mode " +
				"where the location map is not filled. If this flag is set to true, " +
				"copying localization information for elements is not performed. " +
				"This improves time and memory consumption."
			);
		sc.add("protected boolean disableLocationMap = false;");
		sc.addLineBreak();
		
		sc.addJavadoc(
				"A flag that indicates whether this parser runs in a special mode " +
				"where layout information is not recorded. If this flag is set to true, " +
				"no layout information adapters are created. " +
				"This improves time and memory consumption."
			);
		sc.add("protected boolean disableLayoutRecording = false;");
		sc.addLineBreak();
			
		sc.addJavadoc(
			"A flag to indicate that the parser should stop parsing as soon as possible. " +
			"The flag is set to false before parsing starts. It can be set to true by invoking " +
			"the terminateParsing() method from another thread. This feature is used, when documents " +
			"are parsed in the background (i.e., while editing them). In order to cancel running " +
			"parsers, the parsing process can be terminated. This is done whenever a document " +
			"changes, because the previous content of the document is not valid anymore and parsing " +
			"the old content is not necessary any longer."
		);
		sc.add("protected boolean terminateParsing;");
		sc.addLineBreak();
		
		
		sc.addJavadoc(
			"A reusable container for the result of resolving tokens."
		);
		sc.add("private " + tokenResolveResultClassName
				+ " tokenResolveResult = new "
				+ tokenResolveResultClassName + "();");
		sc.addLineBreak();

    	generatorUtil.addMetaInformationField(sc, getContext());
	}

	private void addConstructors(JavaComposite sc) {
		addConstructor1(sc);
		addConstructor2(sc);
	}

	private void addMethods(JavaComposite sc) {
		GenerationContext context = getContext();
		addRetrieveLayoutInformationMethod(sc);
		generatorUtil.addGetLayoutInformationAdapterMethod(sc, layoutInformationAdapterClassName);
		generatorUtil.addRegisterContextDependentProxyMethod(sc, true, context);
		addGetOptionsMethod(sc);
		addSetOptionsMethod(sc);
		addCreateDynamicProxyMethod(sc);
		addTerminateMethod(sc);
		generatorUtil.addAddMapEntryMethod(sc, context);
		generatorUtil.addAddObjectToListMethod1(sc);
		generatorUtil.addAddObjectToListMethod2(sc);
		addApplyMethod(sc);
		generatorUtil.addGetFreshTokenResolveResultMethod(sc, tokenResolveResultClassName);
		generatorUtil.addGetReferenceResolverSwitchMethod(sc, context);
		generatorUtil.addIsLayoutInformationRecordingEnabled(sc, context, "options");
		generatorUtil.addIsLocationMapEnabledMethod(sc, context, "options");
	}

	private void addConstructor1(JavaComposite sc) {
		sc.add("public " + getResourceClassName() + "(" + TOKEN_STREAM(sc) +" input) {");
		sc.add("super(input);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addConstructor2(JavaComposite sc) {
		sc.add("public " + getResourceClassName() + "(" + TOKEN_STREAM(sc) +" input, " + RECOGNIZER_SHARED_STATE(sc) + " state) {");
		sc.add("super(input, state);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addRetrieveLayoutInformationMethod(JavaComposite sc) {
		sc.add("protected void retrieveLayoutInformation(" + E_OBJECT(sc) + " element, " + syntaxElementClassName + " syntaxElement, Object object, boolean ignoreTokensAfterLastVisibleToken) {");
		sc.add("if (disableLayoutRecording || element == null) {");
		sc.add("return;");
		sc.add("}");
		sc.addComment(
			"null must be accepted, since the layout information that is found " +
			"at the end of documents (just before the EOF character) is not associated " +
			"with a particular syntax element.");
		sc.add("boolean isElementToStore = syntaxElement == null;");
		sc.add("isElementToStore |= syntaxElement instanceof " + placeholderClassName + ";");
		sc.add("isElementToStore |= syntaxElement instanceof " + keywordClassName + ";");
		sc.add("isElementToStore |= syntaxElement instanceof " + enumerationTerminalClassName + ";");
		sc.add("isElementToStore |= syntaxElement instanceof " + booleanTerminalClassName + ";");
		sc.add("if (!isElementToStore) {");
		sc.add("return;");
		sc.add("}");
		sc.add(layoutInformationAdapterClassName + " layoutInformationAdapter = getLayoutInformationAdapter(element);");
		sc.add("StringBuilder anonymousText = new StringBuilder();");
		sc.add("for (" + COMMON_TOKEN(sc) + " anonymousToken : anonymousTokens) {");
		sc.add("anonymousText.append(anonymousToken.getText());");
		sc.add("}");
		sc.add("int currentPos = getTokenStream().index();");
		sc.add("if (currentPos == 0) {");
		sc.add("return;");
		sc.add("}");
		sc.add("int endPos = currentPos - 1;");
		sc.add("if (ignoreTokensAfterLastVisibleToken) {");
		sc.add("for (; endPos >= this.lastPosition2; endPos--) {");
		sc.add(TOKEN(sc) + " token = getTokenStream().get(endPos);");
		sc.add("int _channel = token.getChannel();");
		sc.add("if (_channel != 99) {");
		sc.add("break;");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("StringBuilder hiddenTokenText = new StringBuilder();");
		sc.add("hiddenTokenText.append(anonymousText);");
		sc.add("StringBuilder visibleTokenText = new StringBuilder();");
		sc.add(COMMON_TOKEN(sc) + " firstToken = null;");
		sc.add("for (int pos = this.lastPosition2; pos <= endPos; pos++) {");
		sc.add(TOKEN(sc) + " token = getTokenStream().get(pos);");
		sc.add("if (firstToken == null) {");
		sc.add("firstToken = (" + COMMON_TOKEN(sc) + ") token;");
		sc.add("}");
		sc.add("if (anonymousTokens.contains(token)) {");
		sc.add("continue;");
		sc.add("}");
		sc.add("int _channel = token.getChannel();");
		sc.add("if (_channel == 99) {");
		sc.add("hiddenTokenText.append(token.getText());");
		sc.add("} else {");
		sc.add("visibleTokenText.append(token.getText());");
		sc.add("}");
		sc.add("}");
		sc.add("int offset = -1;");
		sc.add("if (firstToken != null) {");
		sc.add("offset = firstToken.getStartIndex();");
		sc.add("}");
		sc.add("layoutInformationAdapter.addLayoutInformation(new " + layoutInformationClassName + "(syntaxElement, object, offset, hiddenTokenText.toString(), visibleTokenText.toString()));");
		sc.add("this.lastPosition2 = (endPos < 0 ? 0 : endPos + 1);");
		sc.add("anonymousTokens.clear();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetOptionsMethod(de.devboost.codecomposers.java.JavaComposite sc) {
		sc.add("protected " + MAP(sc) + "<?,?> getOptions() {");
		sc.add("return options;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addSetOptionsMethod(de.devboost.codecomposers.java.JavaComposite sc) {
		sc.add("public void setOptions(" + MAP(sc) + "<?,?> options) {");
		sc.add("this.options = options;");
		sc.add("this.disableLocationMap = !isLocationMapEnabled();");
		sc.add("this.disableLayoutRecording = !isLayoutInformationRecordingEnabled();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCreateDynamicProxyMethod(JavaComposite sc) {
		sc.addJavadoc("Creates a dynamic Java proxy that mimics the interface of the given class.");
		sc.add("@SuppressWarnings(\"unchecked\")");
		sc.add("public <T> T createDynamicProxy(Class<T> clazz) {");
		sc.add("Object proxy = " + PROXY(sc) + ".newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{clazz, " + E_OBJECT(sc) + ".class, " + INTERNAL_E_OBJECT(sc) + ".class}, new " + INVOCATION_HANDLER(sc) + "() {");
		sc.addLineBreak();
		sc.add("private " + E_OBJECT(sc) + " dummyObject = new " + E_OBJECT_IMPL(sc) + "() {};");
		sc.addLineBreak();
		sc.add("public Object invoke(Object object, " + METHOD(sc) + " method, Object[] args) throws Throwable {");
		sc.addComment("search in dummyObject for the requested method");
		sc.add(METHOD(sc) + "[] methodsInDummy = dummyObject.getClass().getMethods();");
		sc.add("for (" + METHOD(sc) + " methodInDummy : methodsInDummy) {");
		sc.add("boolean matches = true;");
		sc.add("if (methodInDummy.getName().equals(method.getName())) {");
		sc.add("Class<?>[] parameterTypes = method.getParameterTypes();");
		sc.add("Class<?>[] parameterTypesInDummy = methodInDummy.getParameterTypes();");
		sc.add("if (parameterTypes.length == parameterTypesInDummy.length) {");
		sc.add("for (int p = 0; p < parameterTypes.length; p++) {");
		sc.add("Class<?> parameterType = parameterTypes[p];");
		sc.add("Class<?> parameterTypeInDummy = parameterTypesInDummy[p];");
		sc.add("if (!parameterType.equals(parameterTypeInDummy)) {");
		sc.add("matches = false;");
		sc.add("}");
		sc.add("}");
		sc.add("} else {");
		sc.add("matches = false;");
		sc.add("}");
		sc.add("} else {");
		sc.add("matches = false;");
		sc.add("}");
		sc.add("if (matches) {");
		sc.add("return methodInDummy.invoke(dummyObject, args);");
		sc.add("}");
		sc.add("}");
		sc.add("return null;");
		sc.add("}");
		sc.add("});");
		sc.add("return (T) proxy;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addTerminateMethod(StringComposite sc) {
		sc.add("public void terminate() {");
		sc.add("terminateParsing = true;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addApplyMethod(de.devboost.codecomposers.java.JavaComposite sc) {
		// TODO cwende: document this method
		sc.add("protected " + E_OBJECT(sc) + " apply(" + E_OBJECT(sc) + " target, " + LIST(sc) + "<" + E_OBJECT(sc) + "> dummyEObjects) {");
		sc.add(E_OBJECT(sc) + " currentTarget = target;");
		sc.add("for (" + E_OBJECT(sc) + " object : dummyEObjects) {");
		sc.add("assert(object instanceof " + dummyEObjectClassName + ");");
		sc.add(dummyEObjectClassName + " dummy = (" + dummyEObjectClassName + ") object;");
		sc.add(E_OBJECT(sc) + " newEObject = dummy.applyTo(currentTarget);");
		sc.add("currentTarget = newEObject;");
		sc.add("}");
		sc.add("return currentTarget;");
		sc.add("}");
		sc.addLineBreak();
	}
}
