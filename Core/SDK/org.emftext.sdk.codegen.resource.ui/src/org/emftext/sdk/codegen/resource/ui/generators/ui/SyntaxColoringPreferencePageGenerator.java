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

import static de.devboost.codecomposers.java.ClassNameConstants.ITERATOR;
import static de.devboost.codecomposers.java.ClassNameConstants.LINKED_HASH_MAP;
import static de.devboost.codecomposers.java.ClassNameConstants.LIST;
import static de.devboost.codecomposers.java.ClassNameConstants.MAP;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.COLLECTION;
import static org.emftext.sdk.codegen.resource.ClassNameConstants.COLLECTIONS;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.ARRAY_LIST;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.BUTTON;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.COLOR_SELECTOR;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.COMPOSITE;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.CONTROL;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.GRID_DATA;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.GRID_LAYOUT;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_EDITOR_PART;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_PREFERENCE_STORE;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_SELECTION_CHANGED_LISTENER;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_STRUCTURED_SELECTION;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_TREE_CONTENT_PROVIDER;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_WORKBENCH;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.I_WORKBENCH_PREFERENCE_PAGE;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.LABEL;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.LABEL_PROVIDER;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.PREFERENCE_CONVERTER;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.PREFERENCE_PAGE;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.RGB;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.SCROLLABLE;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.SCROLL_BAR;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.SELECTION_CHANGED_EVENT;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.SELECTION_EVENT;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.SELECTION_LISTENER;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.STRUCTURED_SELECTION;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.STRUCTURED_VIEWER;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.SWT;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.TREE_VIEWER;
import static org.emftext.sdk.codegen.resource.ui.UIClassNameConstants.VIEWER;

import org.emftext.sdk.codegen.parameters.ArtifactParameter;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.ui.generators.UIJavaBaseGenerator;

import de.devboost.codecomposers.StringComposite;
import de.devboost.codecomposers.java.JavaComposite;

public class SyntaxColoringPreferencePageGenerator extends UIJavaBaseGenerator<ArtifactParameter<GenerationContext>> {

	public void generateJavaContents(JavaComposite sc) {
		
		sc.add("package " + getResourcePackageName() + ";");sc.addLineBreak();sc.addImportsPlaceholder();
		sc.addLineBreak();
		sc.addJavadoc(
			"Preference page for configuring syntax coloring.",
			"<p>" +
			"<i>Parts of the code were taken from the JDT Java Editor</i>"
		);
		
		sc.add("public class " + getResourceClassName() + " extends " + PREFERENCE_PAGE(sc) + " implements " + I_WORKBENCH_PREFERENCE_PAGE(sc) + " {");
		sc.addLineBreak();
		addConstants(sc);
		addConstructor(sc);
		addInnerClasses(sc);
		addFields(sc);
		addMethods(sc);
		sc.add("}");
	}

	private void addInnerClasses(JavaComposite sc) {
		addIChangePreferenceInterface(sc);
		addAbstractChangedPreferenceClass(sc);
		addChangedBooleanPreferenceClass(sc);
		addChangedRGBPreferenceClass(sc);
		addHighlightingColorListItemClass(sc);
		addColorListLabelProviderClass(sc);
		addColorListContentProviderClass(sc);
	}

	private void addMethods(JavaComposite sc) {
		addDisposeMethod(sc);
		addHandleSyntaxColorListSelectionMethod(sc);
		addCreateSyntaxPageMethod(sc);
		addCreateEditorCompositeMethod(sc);
		addCreateListViewerMethod(sc);
		addAddListenersToStyleButtonsMethod(sc);
		addCreateStylesCompositeMethod(sc);
		addAddFillerMethod(sc);
		addGetHighlightingColorListItemMethod(sc);
		addInitMethod(sc);
		addCreateContentsMethod(sc);
		addPerformOkMethod(sc);
		addPerformCancelMethod(sc);
		addPerformApplyMethod(sc);
		addPerformDefaultsMethod(sc);
		addRestoreDefaultBooleanValueMethod(sc);
		addRestoreDefaultStringValueMethod(sc);
		addUpdateActiveEditorMethod(sc);
	}

	private void addUpdateActiveEditorMethod(JavaComposite sc) {
		sc.add("private void updateActiveEditor() {");
		sc.add(I_WORKBENCH(sc) + " workbench = org.eclipse.ui.PlatformUI.getWorkbench();");
		sc.add(I_EDITOR_PART(sc) + " editor = workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();");
		sc.add("if (editor != null && editor instanceof " + editorClassName + ") {");
		sc.add(editorClassName + " emfTextEditor = (" + editorClassName + ") editor;");
		sc.add("emfTextEditor.invalidateTextRepresentation();");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addRestoreDefaultStringValueMethod(JavaComposite sc) {
		sc.add("private void restoreDefaultStringValue(" + I_PREFERENCE_STORE(sc) + " preferenceStore, String key) {");
		sc.add("preferenceStore.setValue(key, preferenceStore.getDefaultString(key));");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addRestoreDefaultBooleanValueMethod(JavaComposite sc) {
		sc.add("private void restoreDefaultBooleanValue(" + I_PREFERENCE_STORE(sc) + " preferenceStore, String key) {");
		sc.add("preferenceStore.setValue(key, preferenceStore.getDefaultBoolean(key));");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addPerformDefaultsMethod(JavaComposite sc) {
		sc.add("public void performDefaults() {");
		sc.add("super.performDefaults();");
		sc.addLineBreak();
		sc.add(I_PREFERENCE_STORE(sc) + " preferenceStore = getPreferenceStore();");
		sc.addComment("reset all preferences to their default values");
		sc.add("for (String languageID : content.keySet()) {");
		sc.add(LIST(sc) + "<HighlightingColorListItem> items = content.get(languageID);");
		sc.add("for (HighlightingColorListItem item : items) {");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getBoldKey());");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getEnableKey());");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getItalicKey());");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getStrikethroughKey());");
		sc.add("restoreDefaultBooleanValue(preferenceStore, item.getUnderlineKey());");
		sc.add("restoreDefaultStringValue(preferenceStore, item.getColorKey());");
		sc.add("}");
		sc.add("}");
		sc.add("handleSyntaxColorListSelection();");
		sc.add("updateActiveEditor();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addPerformApplyMethod(JavaComposite sc) {
		sc.add("protected void performApply() {");
		sc.add("for (IChangedPreference changedPreference : changedPreferences) {");
		sc.add("changedPreference.apply(getPreferenceStore());");
		sc.add("}");
		sc.add("changedPreferences.clear();");
		sc.add("updateActiveEditor();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addPerformCancelMethod(JavaComposite sc) {
		sc.add("public boolean performCancel() {");
		sc.add("if (!super.performCancel()) {");
		sc.add("return false;");
		sc.add("}");
		sc.addComment("discard all changes that were made");
		sc.add("changedPreferences.clear();");
		sc.add("return true;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addPerformOkMethod(JavaComposite sc) {
		sc.add("public boolean performOk() {");
		sc.add("if (!super.performOk()) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("performApply();");
		sc.add("return true;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCreateContentsMethod(JavaComposite sc) {
		sc.add("protected " + CONTROL(sc) + " createContents(" + COMPOSITE(sc) + " parent) {");
		sc.add(CONTROL(sc) + " content = createSyntaxPage(parent);");
		sc.add("return content;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addInitMethod(JavaComposite sc) {
		sc.add("public void init(" + I_WORKBENCH(sc) + " workbench) {");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addConstructor(JavaComposite sc) {
		sc.add("public " + getResourceClassName() + "() {");
		sc.add("super();");
		sc.addLineBreak();
		sc.add(metaInformationClassName + " metaInformation = new " + metaInformationClassName + "();");
		sc.addLineBreak();
		sc.add("String languageId = metaInformation.getSyntaxName();");
		sc.addLineBreak();
		sc.add(LIST(sc) + "<HighlightingColorListItem> terminals = new " + ARRAY_LIST(sc) + "<HighlightingColorListItem>();");
		sc.add("String[] tokenNames = metaInformation.getSyntaxHighlightableTokenNames();");
		sc.addLineBreak();
		sc.add("for (int i = 0; i < tokenNames.length; i++) {");
		sc.add("HighlightingColorListItem item = new HighlightingColorListItem(languageId, tokenNames[i]);");
		sc.add("terminals.add(item);");
		sc.add("}");
		sc.add(COLLECTIONS(sc) + ".sort(terminals);");
		sc.add("content.put(languageId, terminals);");
		sc.addLineBreak();
		sc.add("setPreferenceStore(" + uiPluginActivatorClassName + ".getDefault().getPreferenceStore());");
		sc.add("setDescription(\"Configure syntax coloring for .\" + languageId + \" files.\");");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetHighlightingColorListItemMethod(JavaComposite sc) {
		sc.addJavadoc("Returns the current highlighting color list item.");
		sc.add("private HighlightingColorListItem getHighlightingColorListItem() {");
		sc.add(I_STRUCTURED_SELECTION(sc) + " selection = (" + I_STRUCTURED_SELECTION(sc) + ") fListViewer.getSelection();");
		sc.add("Object element = selection.getFirstElement();");
		sc.add("if (element instanceof String) {");
		sc.add("return null;");
		sc.add("}");
		sc.add("return (HighlightingColorListItem) element;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addAddFillerMethod(JavaComposite sc) {
		sc.add("private void addFiller(" + COMPOSITE(sc) + " composite, int horizontalSpan) {");
		sc.add(pixelConverterClassName + " pixelConverter = new " + pixelConverterClassName + "(composite);");
		sc.add(LABEL(sc) + " filler = new " + LABEL(sc) + "(composite, " + SWT(sc) + ".LEFT);");
		sc.add(GRID_DATA(sc) + " gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".HORIZONTAL_ALIGN_FILL);");
		sc.add("gd.horizontalSpan = horizontalSpan;");
		sc.add("gd.heightHint = pixelConverter.convertHeightInCharsToPixels(1) / 2;");
		sc.add("filler.setLayoutData(gd);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCreateStylesCompositeMethod(JavaComposite sc) {
		sc.add("private void createStylesComposite(" + COMPOSITE(sc) + " editorComposite) {");
		sc.add(GRID_LAYOUT(sc) + " layout;");
		sc.add(GRID_DATA(sc) + " gd;");
		sc.add(COMPOSITE(sc) + " stylesComposite = new " + COMPOSITE(sc) + "(editorComposite, " + SWT(sc) + ".NONE);");
		sc.add("layout = new " + GRID_LAYOUT(sc) + "();");
		sc.add("layout.marginHeight = 0;");
		sc.add("layout.marginWidth = 0;");
		sc.add("layout.numColumns = 2;");
		sc.add("stylesComposite.setLayout(layout);");
		sc.add("stylesComposite.setLayoutData(new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".END, " + GRID_DATA(sc) + ".FILL, false, true));");
		sc.addLineBreak();
		sc.add("fEnableCheckbox = new " + BUTTON(sc) + "(stylesComposite, " + SWT(sc) + ".CHECK);");
		sc.add("fEnableCheckbox.setText(\"Enable\");");
		sc.add("gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".FILL_HORIZONTAL);");
		sc.add("gd.horizontalAlignment = " + GRID_DATA(sc) + ".BEGINNING;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fEnableCheckbox.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fColorEditorLabel = new " + LABEL(sc) + "(stylesComposite, " + SWT(sc) + ".LEFT);");
		sc.add("fColorEditorLabel.setText(\"Color:\");");
		sc.add("gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("fColorEditorLabel.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fSyntaxForegroundColorEditor = new " + COLOR_SELECTOR(sc) + "(stylesComposite);");
		sc.add("fForegroundColorButton = fSyntaxForegroundColorEditor.getButton();");
		sc.add("gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("fForegroundColorButton.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fBoldCheckBox = new " + BUTTON(sc) + "(stylesComposite, " + SWT(sc) + ".CHECK);");
		sc.add("fBoldCheckBox.setText(\"Bold\");");
		sc.add("gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fBoldCheckBox.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fItalicCheckBox = new " + BUTTON(sc) + "(stylesComposite, " + SWT(sc) + ".CHECK);");
		sc.add("fItalicCheckBox.setText(\"Italic\");");
		sc.add("gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fItalicCheckBox.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fStrikethroughCheckBox = new " + BUTTON(sc) + "(stylesComposite, " + SWT(sc) + ".CHECK);");
		sc.add("fStrikethroughCheckBox.setText(\"Strikethrough\");");
		sc.add("gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fStrikethroughCheckBox.setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fUnderlineCheckBox = new " + BUTTON(sc) + "(stylesComposite, " + SWT(sc) + ".CHECK);");
		sc.add("fUnderlineCheckBox.setText(\"Underlined\");");
		sc.add("gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".HORIZONTAL_ALIGN_BEGINNING);");
		sc.add("gd.horizontalIndent = 20;");
		sc.add("gd.horizontalSpan = 2;");
		sc.add("fUnderlineCheckBox.setLayoutData(gd);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addAddListenersToStyleButtonsMethod(JavaComposite sc) {
		sc.add("private void addListenersToStyleButtons() {");
		sc.add("fForegroundColorButton.addSelectionListener(new " + SELECTION_LISTENER(sc) + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.addComment("do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.addLineBreak();
		sc.add("changedPreferences.add(new ChangedRGBPreference(item.getColorKey(), fSyntaxForegroundColorEditor.getColorValue()));");
		sc.add("}");
		sc.add("});");
		sc.addLineBreak();
		sc.add("fBoldCheckBox.addSelectionListener(new " + SELECTION_LISTENER(sc) + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.addComment("do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getBoldKey(),");
		sc.add("fBoldCheckBox.getSelection()));");
		sc.add("}");
		sc.add("});");
		sc.addLineBreak();
		sc.add("fItalicCheckBox.addSelectionListener(new " + SELECTION_LISTENER(sc) + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.addComment("do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getItalicKey(),");
		sc.add("fItalicCheckBox.getSelection()));");
		sc.add("}");
		sc.add("});");
		sc.add("fStrikethroughCheckBox.addSelectionListener(new " + SELECTION_LISTENER(sc) + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.addComment("do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getStrikethroughKey(),");
		sc.add("fStrikethroughCheckBox.getSelection()));");
		sc.add("}");
		sc.add("});");
		sc.addLineBreak();
		sc.add("fUnderlineCheckBox.addSelectionListener(new " + SELECTION_LISTENER(sc) + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.addComment("do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getUnderlineKey(),");
		sc.add("fUnderlineCheckBox.getSelection()));");
		sc.add("}");
		sc.add("});");
		sc.addLineBreak();
		sc.add("fEnableCheckbox.addSelectionListener(new " + SELECTION_LISTENER(sc) + "() {");
		sc.add("public void widgetDefaultSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.addComment("do nothing");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void widgetSelected(" + SELECTION_EVENT(sc) + " e) {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.addLineBreak();
		sc.add("boolean enable = fEnableCheckbox.getSelection();");
		sc.add("changedPreferences.add(new ChangedBooleanPreference(item.getEnableKey(), enable));");
		sc.add("fEnableCheckbox.setSelection(enable);");
		sc.add("fSyntaxForegroundColorEditor.getButton().setEnabled(enable);");
		sc.add("fColorEditorLabel.setEnabled(enable);");
		sc.add("fBoldCheckBox.setEnabled(enable);");
		sc.add("fItalicCheckBox.setEnabled(enable);");
		sc.add("fStrikethroughCheckBox.setEnabled(enable);");
		sc.add("fUnderlineCheckBox.setEnabled(enable);");
		sc.addComment("uninstallSemanticHighlighting();");
		sc.addComment("installSemanticHighlighting();");
		sc.add("}");
		sc.add("});");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCreateListViewerMethod(JavaComposite sc) {
		sc.add("private void createListViewer(" + COMPOSITE(sc) + " editorComposite) {");
		sc.add("fListViewer = new " + TREE_VIEWER(sc) + "(editorComposite, " + SWT(sc) + ".SINGLE | " + SWT(sc) + ".BORDER);");
		sc.add("fListViewer.setLabelProvider(new ColorListLabelProvider());");
		sc.add("fListViewer.setContentProvider(new ColorListContentProvider());");
		sc.addLineBreak();
		sc.add(GRID_DATA(sc) + " gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".FILL, " + GRID_DATA(sc) + ".FILL, true, true);");
		sc.add("gd.heightHint = convertHeightInCharsToPixels(26);");
		sc.add("int maxWidth = 0;");
		sc.add("for (" + ITERATOR(sc) + "<" + LIST(sc) + "<HighlightingColorListItem>> it = content.values().iterator(); it.hasNext();) {");
		sc.add("for (" + ITERATOR(sc) + "<HighlightingColorListItem> j = it.next().iterator(); j.hasNext();) {");
		sc.add("HighlightingColorListItem item = j.next();");
		sc.add("maxWidth = Math.max(maxWidth, convertWidthInCharsToPixels(item.getDisplayName().length()));");
		sc.add("}");
		sc.add("}");
		sc.add(SCROLL_BAR(sc) + " vBar = ((" + SCROLLABLE(sc) + ") fListViewer.getControl()).getVerticalBar();");
		sc.add("if (vBar != null) {");
		sc.addComment("scrollbars and tree indentation guess");
		sc.add("maxWidth += vBar.getSize().x * 3;");
		sc.add("}");
		sc.add("gd.widthHint = maxWidth;");
		sc.addLineBreak();
		sc.add("fListViewer.getControl().setLayoutData(gd);");
		sc.addLineBreak();
		sc.add("fListViewer.setInput(content);");
		sc.add("fListViewer.setSelection(new " + STRUCTURED_SELECTION(sc) + "(content.values().iterator().next()));");
		sc.add("fListViewer.addSelectionChangedListener(new " + I_SELECTION_CHANGED_LISTENER(sc) + "() {");
		sc.add("public void selectionChanged(" + SELECTION_CHANGED_EVENT(sc) + " event) {");
		sc.add("handleSyntaxColorListSelection();");
		sc.add("}");
		sc.add("});");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCreateEditorCompositeMethod(JavaComposite sc) {
		sc.add("private " + COMPOSITE(sc) + " createEditorComposite(" + COMPOSITE(sc) + " colorComposite) {");
		sc.add(GRID_LAYOUT(sc) + " layout;");
		sc.add(COMPOSITE(sc) + " editorComposite = new " + COMPOSITE(sc) + "(colorComposite, " + SWT(sc) + ".NONE);");
		sc.add("layout = new " + GRID_LAYOUT(sc) + "();");
		sc.add("layout.numColumns = 2;");
		sc.add("layout.marginHeight = 0;");
		sc.add("layout.marginWidth = 0;");
		sc.add("editorComposite.setLayout(layout);");
		sc.add(GRID_DATA(sc) + " gd = new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".FILL, " + GRID_DATA(sc) + ".FILL, true, true);");
		sc.add("editorComposite.setLayoutData(gd);");
		sc.add("return editorComposite;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCreateSyntaxPageMethod(JavaComposite sc) {
		sc.add("private " + CONTROL(sc) + " createSyntaxPage(final " + COMPOSITE(sc) + " parent) {");
		sc.addLineBreak();
		sc.add(COMPOSITE(sc) + " colorComposite = new " + COMPOSITE(sc) + "(parent, " + SWT(sc) + ".NONE);");
		sc.add(GRID_LAYOUT(sc) + " layout = new " + GRID_LAYOUT(sc) + "();");
		sc.add("layout.marginHeight = 0;");
		sc.add("layout.marginWidth = 0;");
		sc.add("colorComposite.setLayout(layout);");
		sc.addLineBreak();
		sc.add("addFiller(colorComposite, 1);");
		sc.addLineBreak();
		sc.add(LABEL(sc) + " label = new " + LABEL(sc) + "(colorComposite, " + SWT(sc) + ".LEFT);");
		sc.add("label.setText(\"Element:\");");
		sc.add("label.setLayoutData(new " + GRID_DATA(sc) + "(" + GRID_DATA(sc) + ".FILL, " + GRID_DATA(sc) + ".BEGINNING, true, false));");
		sc.addLineBreak();
		sc.add(COMPOSITE(sc) + " editorComposite = createEditorComposite(colorComposite);");
		sc.add("createListViewer(editorComposite);");
		sc.add("createStylesComposite(editorComposite);");
		sc.addLineBreak();
		sc.add("addListenersToStyleButtons();");
		sc.add("colorComposite.layout(false);");
		sc.add("handleSyntaxColorListSelection();");
		sc.addLineBreak();
		sc.add("return colorComposite;");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addHandleSyntaxColorListSelectionMethod(JavaComposite sc) {
		sc.add("private void handleSyntaxColorListSelection() {");
		sc.add("HighlightingColorListItem item = getHighlightingColorListItem();");
		sc.add("if (item == null) {");
		sc.add("fEnableCheckbox.setEnabled(false);");
		sc.add("fSyntaxForegroundColorEditor.getButton().setEnabled(false);");
		sc.add("fColorEditorLabel.setEnabled(false);");
		sc.add("fBoldCheckBox.setEnabled(false);");
		sc.add("fItalicCheckBox.setEnabled(false);");
		sc.add("fStrikethroughCheckBox.setEnabled(false);");
		sc.add("fUnderlineCheckBox.setEnabled(false);");
		sc.add("return;");
		sc.add("}");
		sc.add(RGB(sc) + " rgb = " + PREFERENCE_CONVERTER(sc) + ".getColor(getPreferenceStore(), item.getColorKey());");
		sc.add("fSyntaxForegroundColorEditor.setColorValue(rgb);");
		sc.add("fBoldCheckBox.setSelection(getPreferenceStore().getBoolean(item.getBoldKey()));");
		sc.add("fItalicCheckBox.setSelection(getPreferenceStore().getBoolean(item.getItalicKey()));");
		sc.add("fStrikethroughCheckBox.setSelection(getPreferenceStore().getBoolean(item.getStrikethroughKey()));");
		sc.add("fUnderlineCheckBox.setSelection(getPreferenceStore().getBoolean(item.getUnderlineKey()));");
		sc.addLineBreak();
		sc.add("fEnableCheckbox.setEnabled(true);");
		sc.add("boolean enable = getPreferenceStore().getBoolean(item.getEnableKey());");
		sc.add("fEnableCheckbox.setSelection(enable);");
		sc.add("fSyntaxForegroundColorEditor.getButton().setEnabled(enable);");
		sc.add("fColorEditorLabel.setEnabled(enable);");
		sc.add("fBoldCheckBox.setEnabled(enable);");
		sc.add("fItalicCheckBox.setEnabled(enable);");
		sc.add("fStrikethroughCheckBox.setEnabled(enable);");
		sc.add("fUnderlineCheckBox.setEnabled(enable);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addDisposeMethod(JavaComposite sc) {
		sc.add("public void dispose() {");
		sc.add("super.dispose();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addColorListContentProviderClass(JavaComposite sc) {
		sc.addJavadoc("Color list content provider.");
		sc.add("private class ColorListContentProvider implements " + I_TREE_CONTENT_PROVIDER(sc) + " {");
		sc.addLineBreak();
		sc.add("public ColorListContentProvider() {");
		sc.add("super();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public Object[] getElements(Object inputElement) {");
		sc.add(LIST(sc) + "<HighlightingColorListItem> contentsList = new " + ARRAY_LIST(sc) + "<HighlightingColorListItem>();");
		sc.add("for (" + LIST(sc) + "<HighlightingColorListItem> l : content.values()) {");
		sc.add("contentsList.addAll(l);");
		sc.add("}");
		sc.add("return contentsList.toArray();");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void dispose() {");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void inputChanged(" + VIEWER(sc) + " viewer, Object oldInput, Object newInput) {");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public Object[] getChildren(Object parentElement) {");
		sc.add("return new Object[0];");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public Object getParent(Object element) {");
		sc.add("return null;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public boolean hasChildren(Object element) {");
		sc.add("return false;");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addColorListLabelProviderClass(JavaComposite sc) {
		sc.addJavadoc("Color list label provider.");
		sc.add("private class ColorListLabelProvider extends " + LABEL_PROVIDER(sc) + " {");
		sc.addLineBreak();
		sc.add("public String getText(Object element) {");
		sc.add("if (element instanceof String) {");
		sc.add("return (String) element;");
		sc.add("}");
		sc.add("String displayName = ((HighlightingColorListItem) element).getDisplayName();");
		sc.add("if (displayName.startsWith(\"QUOTED_\")) {");
		sc.add("String[] parts = displayName.split(\"_\");");
		sc.add("if (parts.length == 3) {");
		sc.add("if (parts[1].length() == 2 && parts[2].length() == 2) {");
		sc.addComment("Convert decimal ascii codes to string");
		sc.add("try {");
		sc.add("char prefix = (char) Integer.parseInt(parts[1]);");
		sc.add("char suffix = (char) Integer.parseInt(parts[2]);");
		sc.add("displayName = prefix + \"...\" + suffix;");
		sc.add("} catch (NumberFormatException nfe) {");
		sc.addComment("ignore");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("return displayName;");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addHighlightingColorListItemClass(JavaComposite sc) {
		sc.addJavadoc("Item in the highlighting color list.");
		sc.add("private static class HighlightingColorListItem implements Comparable<HighlightingColorListItem> {");
		sc.addJavadoc("Display name");
		sc.add("private String fDisplayName;");
		sc.addJavadoc("Color preference key");
		sc.add("private String fColorKey;");
		sc.addJavadoc("Bold preference key");
		sc.add("private String fBoldKey;");
		sc.addJavadoc("Italic preference key");
		sc.add("private String fItalicKey;");
		sc.addJavadoc("Strikethrough preference key.");
		sc.add("private String fStrikethroughKey;");
		sc.addJavadoc("Underline preference key.");
		sc.add("private String fUnderlineKey;");
		sc.add("private String fEnableKey;");
		sc.addLineBreak();
		sc.addJavadoc("Initializes a new item with the given values.");
		sc.add("public HighlightingColorListItem(String languageID, String tokenName) {");
		sc.add("fDisplayName = tokenName;");
		sc.add("fColorKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.COLOR);");
		sc.add("fBoldKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.BOLD);");
		sc.add("fItalicKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.ITALIC);");
		sc.add("fStrikethroughKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.STRIKETHROUGH);");
		sc.add("fUnderlineKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.UNDERLINE);");
		sc.add("fEnableKey = " + syntaxColoringHelperClassName + ".getPreferenceKey(languageID, tokenName, " + syntaxColoringHelperClassName + ".StyleProperty.ENABLE);");
		sc.add("}");
		sc.addLineBreak();
		
		sc.addJavadoc("@return the bold preference key");
		sc.add("public String getBoldKey() {");
		sc.add("return fBoldKey;");
		sc.add("}");
		sc.addLineBreak();
		
		sc.addJavadoc("@return the italic preference key");
		sc.add("public String getItalicKey() {");
		sc.add("return fItalicKey;");
		sc.add("}");
		sc.addLineBreak();
		
		sc.addJavadoc("@return the strikethrough preference key");
		sc.add("public String getStrikethroughKey() {");
		sc.add("return fStrikethroughKey;");
		sc.add("}");
		sc.addLineBreak();
		
		sc.addJavadoc("@return the underline preference key");
		sc.add("public String getUnderlineKey() {");
		sc.add("return fUnderlineKey;");
		sc.add("}");
		sc.addLineBreak();
		
		sc.addJavadoc("@return the color preference key");
		sc.add("public String getColorKey() {");
		sc.add("return fColorKey;");
		sc.add("}");
		sc.addLineBreak();
		
		sc.addJavadoc("@return the display name");
		sc.add("public String getDisplayName() {");
		sc.add("return fDisplayName;");
		sc.add("}");
		sc.addLineBreak();
		
		sc.add("public String getEnableKey() {");
		sc.add("return fEnableKey;");
		sc.add("}");
		sc.addLineBreak();
		
		sc.add("public int compareTo(HighlightingColorListItem o) {");
		sc.add("return fDisplayName.compareTo(o.getDisplayName());");
		sc.add("}");
		sc.addLineBreak();
		
		sc.add("}");
		sc.addLineBreak();
	}

	private void addChangedRGBPreferenceClass(JavaComposite sc) {
		sc.add("private static class ChangedRGBPreference extends AbstractChangedPreference {");
		sc.addLineBreak();
		sc.add("private " + RGB(sc) + " newValue;");
		sc.addLineBreak();
		sc.add("public ChangedRGBPreference(String key, " + RGB(sc) + " newValue) {");
		sc.add("super(key);");
		sc.add("this.newValue = newValue;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void apply(" + I_PREFERENCE_STORE(sc) + " store) {");
		sc.add(PREFERENCE_CONVERTER(sc) + ".setValue(store, getKey(), newValue);");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addChangedBooleanPreferenceClass(JavaComposite sc) {
		sc.add("private static class ChangedBooleanPreference extends AbstractChangedPreference {");
		sc.addLineBreak();
		sc.add("private boolean newValue;");
		sc.addLineBreak();
		sc.add("public ChangedBooleanPreference(String key, boolean newValue) {");
		sc.add("super(key);");
		sc.add("this.newValue = newValue;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public void apply(" + I_PREFERENCE_STORE(sc) + " store) {");
		sc.add("store.setValue(getKey(), newValue);");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addAbstractChangedPreferenceClass(StringComposite sc) {
		sc.add("private abstract static class AbstractChangedPreference implements IChangedPreference {");
		sc.addLineBreak();
		sc.add("private String key;");
		sc.addLineBreak();
		sc.add("public AbstractChangedPreference(String key) {");
		sc.add("super();");
		sc.add("this.key = key;");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public String getKey() {");
		sc.add("return key;");
		sc.add("}");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addIChangePreferenceInterface(JavaComposite sc) {
		sc.add("private interface IChangedPreference {");
		sc.add("public void apply(" + I_PREFERENCE_STORE(sc) + " store);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addFields(JavaComposite sc) {
		sc.add("private " + COLOR_SELECTOR(sc) + " fSyntaxForegroundColorEditor;");
		sc.add("private " + LABEL(sc) + " fColorEditorLabel;");
		sc.add("private " + BUTTON(sc) + " fBoldCheckBox;");
		sc.add("private " + BUTTON(sc) + " fEnableCheckbox;");
		sc.addJavadoc("Check box for italic preference.");
		sc.add("private " + BUTTON(sc) + " fItalicCheckBox;");
		sc.addJavadoc("Check box for strikethrough preference.");
		sc.add("private " + BUTTON(sc) + " fStrikethroughCheckBox;");
		sc.addJavadoc("Check box for underline preference.");
		sc.add("private " + BUTTON(sc) + " fUnderlineCheckBox;");
		sc.add("private " + BUTTON(sc) + " fForegroundColorButton;");
		sc.addLineBreak();
		sc.addJavadoc("Highlighting color list viewer");
		sc.add("private " + STRUCTURED_VIEWER(sc) + " fListViewer;");
		sc.addLineBreak();
	}

	private void addConstants(JavaComposite sc) {
		sc.add("private final static " + MAP(sc) + "<String, " + LIST(sc) + "<HighlightingColorListItem>> content = new " + LINKED_HASH_MAP(sc) + "<String, " + LIST(sc) + "<HighlightingColorListItem>>();");
		sc.add("private final static " + COLLECTION(sc) + "<IChangedPreference> changedPreferences = new " + ARRAY_LIST(sc) + "<IChangedPreference>();");
		sc.addLineBreak();
	}
}
