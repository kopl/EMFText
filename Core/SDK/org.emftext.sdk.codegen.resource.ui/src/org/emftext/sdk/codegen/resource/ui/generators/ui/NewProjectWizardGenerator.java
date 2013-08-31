/*******************************************************************************
 * Copyright (c) 2006-2013
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

import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.FILE_LOCATOR;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.I_EXECUTABLE_EXTENSION;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.BASIC_NEW_PROJECT_RESOURCE_WIZARD;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.BUNDLE;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.CORE_EXCEPTION;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.IMAGE_DESCRIPTOR;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.I_CONFIGURATION_ELEMENT;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.I_NEW_WIZARD;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.I_PATH;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.I_PROGRESS_MONITOR;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.I_RUNNABLE_WITH_PROGRESS;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.I_STRUCTURED_SELECTION;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.I_WORKBENCH;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.PATH;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.URL;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.WIZARD;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.WIZARD_NEW_PROJECT_CREATION_PAGE;
import static org.emftext.sdk.codegen.resource.ui.IUIClassNameConstants.WORKSPACE_MODIFY_OPERATION;

import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.parameters.ArtifactParameter;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.ui.UIConstants;
import org.emftext.sdk.codegen.resource.ui.generators.UIJavaBaseGenerator;

public class NewProjectWizardGenerator extends UIJavaBaseGenerator<ArtifactParameter<GenerationContext>> {

	public void generateJavaContents(JavaComposite sc) {
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();

		sc.addJavadoc(
			"This class is based on: " +
			"<i>org.eclipse.gef.examples.ui.pde.internal.wizards.ProjectUnzipperNewWizard</i>.",
			"It is responsible for offering an example project via the new dialog of Eclipse."
		);
		sc.add("public class " + getResourceClassName() + " extends " + WIZARD + " implements " + I_NEW_WIZARD + ", " + I_EXECUTABLE_EXTENSION + " {");
		sc.addLineBreak();
		addFields(sc);
		addConstructor(sc);
		addMethods(sc);
		sc.add("}");
	}

	private void addFields(JavaComposite sc) {
		sc.addJavadoc(
			"The single page provided by this base implementation. It provides all the " + 
			"functionality required to capture the name and location of the target " +
			"project."
		);
		sc.add("private " + WIZARD_NEW_PROJECT_CREATION_PAGE + " wizardNewProjectCreationPage;");
		sc.addLineBreak();
		
		sc.addJavadoc("The name of the project creation page");
		sc.add("private String pageName = \"New \" + new " + metaInformationClassName + "().getSyntaxName() + \" Project\";");
		sc.addLineBreak();
		
		sc.addJavadoc("The title of the project creation page");
		sc.add("private String pageTitle = pageName;");
		sc.addLineBreak();
		
		sc.addJavadoc("The description of the project creation page");
		sc.add("private String pageDescription = \"Enter a name and select a location where the new project shall be created.\";");
		sc.addLineBreak();
		
		sc.addJavadoc(" The name of the project in the project creation page");
		sc.add("private String  pageProjectName = \"\";");
		sc.addLineBreak();
		
		sc.addJavadoc("The name of the new project zip file (relative to the UI plugin's root)");
		sc.add("private final static String NEW_PROJECT_ZIP_FILE_NAME = \"newProject.zip\";");
		sc.addLineBreak();
		
		sc.addJavadoc("The configuration element associated with this new project wizard");		
		sc.add("private " + I_CONFIGURATION_ELEMENT + " config;");
		sc.addLineBreak();
	}

	private void addConstructor(JavaComposite sc) {
		sc.addJavadoc("The constructor.");
		sc.add("public " + getResourceClassName() + "() {");
		sc.add("super();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addMethods(JavaComposite sc) {
		addPerformFinishMethod(sc);
		addInitMethod(sc);
		addSetInitializationDataMethod(sc);
	}

	private void addPerformFinishMethod(JavaComposite sc) {
		sc.addJavadoc("Creates the example project.");
		sc.add("public boolean performFinish() {");
		sc.addLineBreak();
		sc.add("try {");
		sc.add(I_RUNNABLE_WITH_PROGRESS + " operation = new " + WORKSPACE_MODIFY_OPERATION + "() {");
		sc.addLineBreak();
		sc.add("public void execute(" + I_PROGRESS_MONITOR + " monitor) throws InterruptedException {");
		sc.add("try {");
		sc.add("new " + newProjectWizardLogicClassName + "().createExampleProject(monitor, wizardNewProjectCreationPage.getLocationPath(), wizardNewProjectCreationPage.getProjectName(), NEW_PROJECT_ZIP_FILE_NAME);");
		sc.add("} catch (Exception e) {");
		sc.add("throw new RuntimeException(e);");
		sc.add("}");
		sc.add("}");
		sc.add("};");
		sc.addLineBreak();
		sc.add("getContainer().run(false, true, operation);");
		sc.addLineBreak();
		sc.addComment("Set perspective");
		sc.add(BASIC_NEW_PROJECT_RESOURCE_WIZARD + ".updatePerspective(config);");
		sc.add("} catch (InterruptedException e) {");
		sc.add("return false;");
		sc.add("} catch (Exception e) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("return true;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addInitMethod(JavaComposite sc) {
		sc.addJavadoc(
			"Creates the sole wizard page contributed by this base implementation; the " +
			"standard Eclipse WizardNewProjectCreationPage.",
			"@see " + WIZARD_NEW_PROJECT_CREATION_PAGE + "#" + WIZARD_NEW_PROJECT_CREATION_PAGE + "(String)"
		);
		sc.add("public void init(" + I_WORKBENCH + " workbench, " + I_STRUCTURED_SELECTION + " selection) {");
		sc.addComment("Set default image for all wizard pages");
		sc.add(I_PATH + " path = new " + PATH + "(\"icons/" + UIConstants.Icon.DEFAULT_NEW_PROJECT_WIZBAN.getFilename() + "\");");
		sc.add(BUNDLE + " bundle = " + uiPluginActivatorClassName + ".getDefault().getBundle();");
		sc.add(URL + " url = " + FILE_LOCATOR + ".find(bundle, path, null);");
		sc.add(IMAGE_DESCRIPTOR + " descriptor = " + IMAGE_DESCRIPTOR + ".createFromURL(url);");
		sc.add("setDefaultPageImageDescriptor(descriptor);");
		sc.addLineBreak();
		sc.add("wizardNewProjectCreationPage = new " + WIZARD_NEW_PROJECT_CREATION_PAGE + "(pageName);");
		sc.add("wizardNewProjectCreationPage.setTitle(pageTitle);");
		sc.add("wizardNewProjectCreationPage.setDescription(pageDescription);");
		sc.add("wizardNewProjectCreationPage.setInitialProjectName(pageProjectName);");
		sc.addLineBreak();
		sc.add("this.addPage(wizardNewProjectCreationPage);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addSetInitializationDataMethod(JavaComposite sc) {
		sc.add("public void setInitializationData(" + I_CONFIGURATION_ELEMENT + " configIn, String propertyName, Object data) throws " + CORE_EXCEPTION + " {");
		sc.add("config = configIn;");
		sc.add("}");
		sc.addLineBreak();
	}
}
