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
package org.emftext.sdk.codegen.resource.ui.generators.ui;

import org.emftext.sdk.codegen.parameters.ArtifactParameter;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.GeneratorUtil;
import org.emftext.sdk.codegen.resource.ui.UIConstants;
import org.emftext.sdk.codegen.resource.ui.generators.UIJavaBaseGenerator;

import de.devboost.codecomposers.java.JavaComposite;

public class UIResourceBundleGenerator extends UIJavaBaseGenerator<ArtifactParameter<GenerationContext>> {

	// A list of all constant names so they can be used by other generators
	public static final String NEW_PROJECT_WIZARD_WINDOW_TITLE = "NEW_PROJECT_WIZARD_WINDOW_TITLE";
	public static final String NEW_PROJECT_WIZARD_PAGE_NAME = "NEW_PROJECT_WIZARD_PAGE_NAME";
	public static final String NEW_PROJECT_ZIP_FILE_NAME = "NEW_PROJECT_ZIP_FILE_NAME";
	public static final String NEW_PROJECT_WIZARD_PAGE_DESCRIPTION = "NEW_PROJECT_WIZARD_PAGE_DESCRIPTION";
	public static final String NEW_PROJECT_WIZARD_PROJECT_NAME = "NEW_PROJECT_WIZARD_PROJECT_NAME";
	public static final String NEW_PROJECT_WIZARD_PAGE_ICON = "NEW_PROJECT_WIZARD_PAGE_ICON";
	public static final String NEW_FILE_WIZARD_FILE_NAME = "NEW_FILE_WIZARD_FILE_NAME";
	public static final String NEW_FILE_WIZARD_WINDOW_TITLE = "NEW_FILE_WIZARD_WINDOW_TITLE";
	public static final String NEW_FILE_WIZARD_PAGE_TITLE = "NEW_FILE_WIZARD_PAGE_TITLE";
	public static final String NEW_FILE_WIZARD_DESCRIPTION = "NEW_FILE_WIZARD_DESCRIPTION";
	public static final String ROOT_PREFERENCE_PAGE_DESCRIPTION = "ROOT_PREFERENCE_PAGE_DESCRIPTION";
	public static final String ROOT_PREFERENCE_PAGE_TEXT = "ROOT_PREFERENCE_PAGE_TEXT";

	public void generateJavaContents(JavaComposite jc) {
		
		jc.add("package " + getResourcePackageName() + ";");
		jc.addLineBreak();
		jc.addImportsPlaceholder();
		jc.addLineBreak();
		
		jc.addJavadoc("A class to hold all resources (e.g., text constants) for the resource UI plug-in.");
		jc.add("public class " + getResourceClassName() + " {");
		jc.addLineBreak();
		addConstants(jc);
		addStaticInitializer(jc);
		jc.add("}");
	}

	private void addConstants(JavaComposite jc) {
		String syntaxName = getContext().getConcreteSyntax().getName();

		jc.addJavadoc("The title of the NewProjectWizard window.");
		jc.add("public static String " + NEW_PROJECT_WIZARD_WINDOW_TITLE + " = \"New " + syntaxName + " Project\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The name of the main page for the NewProjectWizard.");
		jc.add("public static String " + NEW_PROJECT_WIZARD_PAGE_NAME + " = \"Create new " + syntaxName + " Project\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The name of the ZIP file that is used as content for the new project (relative to the root of the resource UI plug-in).");
		jc.add("public static String " + NEW_PROJECT_ZIP_FILE_NAME + " = \"newProject.zip\";");
		jc.addLineBreak();

		jc.addJavadoc("The description of the project creation page");
		jc.add("public static String " + NEW_PROJECT_WIZARD_PAGE_DESCRIPTION + " = \"Enter a name and select a location where the new project shall be created.\";");
		jc.addLineBreak();

		jc.addJavadoc("The name of the project in the project creation page");
		jc.add("public static String " + NEW_PROJECT_WIZARD_PROJECT_NAME + " = \"\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The path of the icon to be used for the pages of the NewProjectWizard.");
		jc.add("public static String " + NEW_PROJECT_WIZARD_PAGE_ICON + " = \"icons/" + UIConstants.Icon.DEFAULT_NEW_PROJECT_WIZBAN.getFilename() + "\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The default file name for the new file wizard.");
		jc.add("public static String " + NEW_FILE_WIZARD_FILE_NAME + " = \"new_file." + syntaxName + "\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The title for the NewFileWizard window.");
		jc.add("public static String " + NEW_FILE_WIZARD_WINDOW_TITLE + " = \"New " + syntaxName + " File\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The title for the NewFileWizard page.");
		jc.add("public static String " + NEW_FILE_WIZARD_PAGE_TITLE + " = \"Create new " + syntaxName + " file\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The description for the NewFileWizard.");
		jc.add("public static String " + NEW_FILE_WIZARD_DESCRIPTION + " = \"This wizard creates a new file with *." + syntaxName + " extension.\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The description for the root preference page.");
		jc.add("public static String " + ROOT_PREFERENCE_PAGE_DESCRIPTION + " = \"" + getContext().getCapitalizedConcreteSyntaxName() + " Text Editor Preferences\";");
		jc.addLineBreak();
		
		jc.addJavadoc("The text for the root preference page.");
		jc.add("public static String " + ROOT_PREFERENCE_PAGE_TEXT + " = \"Go to <a href=\\\"http://www.emftext.org\\\">www.emftext.org</a> for more information.\";");
		jc.addLineBreak();
	}

	private void addStaticInitializer(JavaComposite jc) {
		String resourceClassName = getResourceClassName();
		new GeneratorUtil().addStaticResourceInitializer(jc, resourceClassName);
	}
}
