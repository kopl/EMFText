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

import static de.devboost.codecomposers.java.ClassNameConstants.LIST;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.BUNDLE;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.BYTE_ARRAY_INPUT_STREAM;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.CORE_EXCEPTION;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.FILE;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.FILE_LOCATOR;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.FILE_NOT_FOUND_EXCEPTION;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.FILE_OUTPUT_STREAM;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.INPUT_STREAM;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.INPUT_STREAM_READER;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.IO_EXCEPTION;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.I_ADAPTABLE;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.I_COMMAND;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.I_FILE;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.I_PATH;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.I_PROGRESS_MONITOR;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.I_PROJECT;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.I_PROJECT_DESCRIPTION;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.I_WORKSPACE;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.OUTPUT_STREAM;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.OUTPUT_STREAM_WRITER;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.PATH;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.PLATFORM;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.RESOURCES_PLUGIN;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.SUB_PROGRESS_MONITOR;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.URL;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.ZIP_ENTRY;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.ZIP_FILE;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.ARRAY_LIST;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.ENUMERATION;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_SELECTION;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_SELECTION_SERVICE;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_STRUCTURED_SELECTION;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_WORKBENCH;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_WORKBENCH_WINDOW;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_WORKING_SET;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_WORKING_SET_MANAGER;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.PLATFORM_UI;

import org.emftext.sdk.codegen.parameters.ArtifactParameter;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.TextResourceArtifacts;
import org.emftext.sdk.codegen.resource.ui.generators.UIJavaBaseGenerator;

import de.devboost.codecomposers.java.JavaComposite;

public class NewProjectWizardLogicGenerator extends UIJavaBaseGenerator<ArtifactParameter<GenerationContext>> {

	public void generateJavaContents(JavaComposite sc) {
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		sc.addImportsPlaceholder();
		sc.addLineBreak();

		sc.addJavadoc(
				"This class is used to create an example project via the new dialog of Eclipse. " +
						"The contents of the example project are obtained from a ZIP file named <code>newProject.zip</code> that " +
						"must be located in the resource.ui plug-in. If not such ZIP file can be found, an empty project " +
						"containing an example file of the DSL is created."
				);
		sc.add("public class " + getResourceClassName() + " {");
		sc.addLineBreak();
		addMethods(sc);
		sc.add("}");
	}

	private void addMethods(JavaComposite sc) {
		addCreateExampleProjectMethod(sc);
		addAddProjectToSelectedWorkingSet(sc);
		addExtractProjectMethod(sc);
		addUnzipMethod(sc);
		addRenameProjectMethod(sc);
		addGetTaskNameMethod(sc);
		addCreateDefaultNewFile(sc);
	}

	private void addCreateDefaultNewFile(JavaComposite jc) {
		jc.add("protected void createDefaultNewFile(" + I_PROJECT(jc) + " project, boolean createDefaultNewFile) throws " + CORE_EXCEPTION(jc) + " {");
		jc.add(I_FILE(jc) + " defaultNewFile = project.getFile(\"NEW_FILE_PLACEHOLDER\");");
		jc.add("if (createDefaultNewFile) {");
		jc.add("defaultNewFile.create(new " + BYTE_ARRAY_INPUT_STREAM(jc) + "(new byte[0]), true, null);");
		jc.add("}");
		jc.add("if (defaultNewFile.exists()) {");
		jc.add(metaInformationClassName + " info = new " + metaInformationClassName + "();");
		jc.add("String fileName = \"new_file.\" + info.getSyntaxName();");
		jc.add("String content = info.getNewFileContentProvider().getNewFileContent(\"new_file.\" + info.getSyntaxName());");
		jc.add("defaultNewFile.setContents(new " + BYTE_ARRAY_INPUT_STREAM(jc) + "(content.getBytes()), " + I_FILE(jc) + ".FORCE, null);");
		jc.add("defaultNewFile.move(project.getProjectRelativePath().append(fileName), true, null);");
		jc.add("}");
		jc.add("}");
		jc.addLineBreak();
	}

	private void addGetTaskNameMethod(JavaComposite sc) {
		sc.add("protected String getTaskName() {");
		sc.add("return \"Creating Example Project\";");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCreateExampleProjectMethod(JavaComposite sc) {
		sc.addJavadoc(
				"Creates the example project by unzipping the contents of <code>newProjectZip</code>."
				);
		sc.add("public void createExampleProject(" + I_PROGRESS_MONITOR(sc) + " monitor, " + I_PATH(sc) + " projectPath, String projectName, String bundleName, String newProjectZip) throws InterruptedException {");
		sc.add("try {");
		sc.add("monitor.beginTask(getTaskName(), 120);");
		sc.addLineBreak();
		sc.addComment("Create the project folder");

		sc.add("String projectFolder = projectPath.toOSString() + " + FILE(sc) + ".separator + projectName;");
		sc.add(FILE(sc) + " projectFolderFile = new " + FILE(sc) + "(projectFolder);");
		sc.addLineBreak();
		sc.add(I_WORKSPACE(sc) + " workspace = " + RESOURCES_PLUGIN(sc) + ".getWorkspace();");
		sc.add(I_PROJECT(sc) + " project = workspace.getRoot().getProject(projectName);");
		sc.addLineBreak();
		sc.addComment("If the project does not exist, we will create it and populate it.");
		sc.add("if (!project.exists()) {");
		sc.add("projectFolderFile.mkdirs();");
		sc.add("monitor.worked(10);");
		sc.addLineBreak();
		sc.add(BUNDLE(sc) + " bundle = " + PLATFORM(sc) + ".getBundle(bundleName);");
		sc.add(URL(sc) + " newProjectZipURL = bundle.getEntry(newProjectZip);");
		sc.addLineBreak();
		sc.add("if (newProjectZipURL != null) {");
		sc.addComment("Copy plug-in project code");
		sc.add("extractProject(projectFolderFile, newProjectZipURL, new " + SUB_PROGRESS_MONITOR(sc) + "(monitor, 100));");
		sc.add("}");
		sc.addLineBreak();
		sc.add("if (monitor.isCanceled()) {");
		sc.add("throw new InterruptedException();");
		sc.add("}");
		sc.addLineBreak();
		sc.add(I_PROJECT_DESCRIPTION(sc) + " desc = workspace.newProjectDescription(project.getName());");
		sc.add("if (!projectPath.equals(workspace.getRoot().getLocation())) {");
		sc.add("desc.setLocation(new " + PATH(sc) + "(projectFolder));");
		sc.add("}");
		sc.addLineBreak();

		String natureClassName = getContext().getQualifiedClassName(TextResourceArtifacts.NATURE);

		sc.add("String natureID = " + natureClassName + ".NATURE_ID;");
		sc.add(LIST(sc) + "<" + I_COMMAND(sc) + "> buildCommands = new " + ARRAY_LIST(sc) +"<" + I_COMMAND(sc) + ">();");
		sc.add("for (String builderID : " + natureClassName + ".BUILDER_IDS) {");
		sc.add(I_COMMAND(sc) + " command = desc.newCommand();");
		sc.add("command.setBuilderName(builderID);");
		sc.add("buildCommands.add(command);");
		sc.add("}");
		sc.addLineBreak();
		sc.add("desc.setNatureIds(new String[] {natureID});");
		sc.add("desc.setBuildSpec(buildCommands.toArray(new " + I_COMMAND(sc) + "[buildCommands.size()]));");
		sc.add("addProjectToSelectedWorkingSet(project);");
		sc.add("project.create(desc, monitor);");
		sc.addComment("Now, we ensure that the project is open.");
		sc.add("project.open(monitor);");
		sc.add("renameProject(project, projectName);");
		sc.addLineBreak();
		sc.add("createDefaultNewFile(project, newProjectZipURL == null);");
		sc.add("}");
		sc.addLineBreak();
		sc.add("monitor.worked(10);");
		sc.add("if (monitor.isCanceled()) {");
		sc.add("throw new InterruptedException();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("} catch (" + IO_EXCEPTION(sc) + " e) {");
		sc.add("throw new RuntimeException(e);");
		sc.add("} catch (" + CORE_EXCEPTION(sc) + " e) {");
		sc.add("throw new RuntimeException(e);");
		sc.add("} finally {");
		sc.add("monitor.done();");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addAddProjectToSelectedWorkingSet(JavaComposite sc) {
		sc.addJavadoc(
				"Adds the newly created project to the currently selected working set.",
				"@param project the project to be added to the selected working set");
		sc.add("private void addProjectToSelectedWorkingSet(" + I_PROJECT(sc) + " project) {");
		sc.add(I_WORKBENCH(sc) + " workbench = " + PLATFORM_UI(sc) + ".getWorkbench();");
		sc.add(I_WORKBENCH_WINDOW(sc) + " workbenchWindow = workbench.getActiveWorkbenchWindow();");
		sc.add("if (workbenchWindow == null) {");
		sc.add("return;");
		sc.add("}");
		sc.add(I_SELECTION_SERVICE(sc) + " selectionService = workbenchWindow.getSelectionService();");
		sc.add(I_SELECTION(sc) + " selection = selectionService.getSelection();");
		sc.add("if (selection instanceof " + I_STRUCTURED_SELECTION(sc) + ") {");
		sc.add(I_STRUCTURED_SELECTION(sc) + " structuredSelection = (" + I_STRUCTURED_SELECTION(sc) + ") selection;");
		sc.add("Object firstElement = structuredSelection.getFirstElement();");
		sc.add("if (firstElement instanceof " + I_ADAPTABLE(sc) + ") {");
		sc.add(I_ADAPTABLE(sc) + " adaptable = (" + I_ADAPTABLE(sc) + ") firstElement;");
		sc.add(I_WORKING_SET(sc) + " workingSet = (" + I_WORKING_SET(sc) + ") adaptable.getAdapter(" + I_WORKING_SET(sc) + ".class);");
		sc.add("if (workingSet != null) {");
		sc.addComment("new project wizard was invoked by right-clicking a working set");
		sc.add(I_WORKING_SET_MANAGER(sc) + " workingSetManager = workbench.getWorkingSetManager();");
		sc.add("workingSetManager.addToWorkingSets(project, new " + I_WORKING_SET(sc) + "[]{workingSet});");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addExtractProjectMethod(JavaComposite sc) {
		sc.addJavadoc(
				"Unzip the project archive to the specified folder",
				"@param projectFolderFile The folder where to unzip the project archive",
				"@param monitor Monitor to display progress and/or cancel operation",
				"@throws " + IO_EXCEPTION(sc),
				"@throws InterruptedException",
				"@throws " + FILE_NOT_FOUND_EXCEPTION(sc));

		sc.add("private void extractProject(" + FILE(sc) + " projectFolderFile, " + URL(sc) + " url, " + I_PROGRESS_MONITOR(sc) + " monitor) throws " + FILE_NOT_FOUND_EXCEPTION(sc) + ", " + IO_EXCEPTION(sc) + ", InterruptedException {");
		sc.addLineBreak();
		sc.addComment("Get project archive");
		sc.add(URL(sc) + " urlZipLocal = " + FILE_LOCATOR(sc) + ".toFileURL(url);");
		sc.addLineBreak();
		sc.addComment("Walk each element and unzip");

		sc.add(ZIP_FILE(sc) + " zipFile = new " + ZIP_FILE(sc) + "(urlZipLocal.getPath());");
		sc.addLineBreak();
		sc.add("try {");
		sc.addComment("Allow for a hundred work units");
		sc.add("monitor.beginTask(\"Extracting Project\", zipFile.size());");
		sc.addLineBreak();
		sc.add("unzip(zipFile, projectFolderFile, monitor);");
		sc.add("} finally {");
		sc.add("zipFile.close();");
		sc.add("monitor.done();");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addUnzipMethod(JavaComposite sc) {
		sc.addJavadoc(
				"Unzips the platform formatted zip file to specified folder",
				"@param zipFile The platform formatted zip file",
				"@param projectFolderFile The folder where to unzip the project archive",
				"@param monitor Monitor to display progress and/or cancel operation",
				"@throws " + IO_EXCEPTION(sc),
				"@throws " + FILE_NOT_FOUND_EXCEPTION(sc),
				"@throws InterruptedException"
				);
		sc.add("protected void unzip(" + ZIP_FILE(sc) + " zipFile, " + FILE(sc) + " projectFolderFile, " + I_PROGRESS_MONITOR(sc) + " monitor) throws " + IO_EXCEPTION(sc) + ", " + FILE_NOT_FOUND_EXCEPTION(sc) + ", InterruptedException {");
		sc.addLineBreak();
		sc.add(ENUMERATION(sc) + "<? extends " + ZIP_ENTRY(sc) + "> e = zipFile.entries();");
		sc.addLineBreak();
		sc.add("while (e.hasMoreElements()) {");
		sc.add(ZIP_ENTRY(sc) + " zipEntry = (" + ZIP_ENTRY(sc) + ") e.nextElement();");
		sc.add(FILE(sc) + " file = new " + FILE(sc) + "(projectFolderFile, zipEntry.getName());");
		sc.addLineBreak();
		sc.add("if (zipEntry.isDirectory()) {");
		sc.add("file.mkdirs();");
		sc.add("} else {");
		sc.addLineBreak();
		sc.addComment("Copy files (and make sure parent directory exist)");

		sc.add(FILE(sc) + " parentFile = file.getParentFile();");
		sc.add("if (null != parentFile && false == parentFile.exists()) {");
		sc.add("parentFile.mkdirs();");
		sc.add("}");
		sc.addLineBreak();
		sc.add(PATH(sc) + " path = new " + PATH(sc) + "(file.getPath());");
		sc.add("if (\"java\".equals(path.getFileExtension())) {");
		sc.add(INPUT_STREAM_READER(sc) + " is = null;");
		sc.add(OUTPUT_STREAM_WRITER(sc) + " os = null;");
		sc.addLineBreak();
		sc.add("try {");
		// TODO The encoding might not be necessary ISO-8859-1
		sc.add("is = new " + INPUT_STREAM_READER(sc) + "(zipFile.getInputStream(zipEntry), \"ISO-8859-1\");");
		sc.add("os = new " + OUTPUT_STREAM_WRITER(sc) + "(new " + FILE_OUTPUT_STREAM(sc) + "(file), " + RESOURCES_PLUGIN(sc) + ".getEncoding());");
		sc.add("char[] buffer = new char[102400];");
		sc.add("while (true) {");
		sc.add("int len = is.read(buffer);");
		sc.add("if (len < 0) {");
		sc.add("break;");
		sc.add("}");
		sc.add("os.write(buffer, 0, len);");
		sc.add("}");
		sc.add("} finally {");
		sc.add("if (null != is) {");
		sc.add("is.close();");
		sc.add("}");
		sc.add("if (null != os) {");
		sc.add("os.close();");
		sc.add("}");
		sc.add("}");
		sc.add("} else {");
		sc.add(INPUT_STREAM(sc) + " is = null;");
		sc.add(OUTPUT_STREAM(sc) + " os = null;");
		sc.addLineBreak();
		sc.add("try {");
		sc.add("is = zipFile.getInputStream(zipEntry);");
		sc.add("os = new " + FILE_OUTPUT_STREAM(sc) + "(file);");
		sc.addLineBreak();
		sc.add("byte[] buffer = new byte[102400];");
		sc.add("while (true) {");
		sc.add("int len = is.read(buffer);");
		sc.add("if (len < 0) {");
		sc.add("break;");
		sc.add("}");
		sc.add("os.write(buffer, 0, len);");
		sc.add("}");
		sc.add("} finally {");
		sc.add("if (null != is) {");
		sc.add("is.close();");
		sc.add("}");
		sc.add("if (null != os) {");
		sc.add("os.close();");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
		sc.add("monitor.worked(1);");
		sc.addLineBreak();
		sc.add("if (monitor.isCanceled()) {");
		sc.add("throw new InterruptedException();");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addRenameProjectMethod(JavaComposite sc) {
		sc.addJavadoc(
				"Renames the specified project to the specified name.",
				"@param project a project to rename",
				"@param projectName a new name for the project",
				"@throws " + CORE_EXCEPTION(sc) + " if something goes wrong"
				);
		sc.add("protected void renameProject(" + I_PROJECT(sc) + " project, String projectName) throws " + CORE_EXCEPTION(sc) + " {");
		sc.add(I_PROJECT_DESCRIPTION(sc) + " description = project.getDescription();");
		sc.add("description.setName(projectName);");
		//sc.add("project.move(description, " + I_RESOURCE + ".FORCE | " + I_RESOURCE + ".SHALLOW, null);");
		sc.add("}");
		sc.addLineBreak();
	}
}
