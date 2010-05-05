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
package org.emftext.sdk.codegen.generators;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_CLASS;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.emftext.sdk.codegen.TextResourceArtifacts;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.util.GenClassUtil;
import org.emftext.sdk.concretesyntax.Annotation;
import org.emftext.sdk.concretesyntax.AnnotationType;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Rule;

public class FoldingInformationProviderGenerator extends JavaBaseGenerator {

	private final GenClassUtil genClassUtil = new GenClassUtil();

	public FoldingInformationProviderGenerator() {
		super();
	}

	public FoldingInformationProviderGenerator(GenerationContext context) {
		super(context, TextResourceArtifacts.FOLDING_INFORMATION_PROVIDER);
	}

	@Override
	public boolean generateJavaContents(JavaComposite sc) {
		
        sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
        
        sc.add("public class " + getResourceClassName()+ " {");
        sc.addLineBreak();
        addGetFoldableClassesMethod(sc);
		sc.add("}");
		
		return true;
	}

	private void addGetFoldableClassesMethod(StringComposite sc) {
		sc.add("public " + E_CLASS + "[] getFoldableClasses() {");
		
		List<GenClass> foldableClasses = new ArrayList<GenClass>();
		ConcreteSyntax syntax = getContext().getConcreteSyntax();
		for (Rule rule : syntax.getAllRules()) {
			for (Annotation annotation : rule.getAnnotations()) {
				if (AnnotationType.FOLDABLE == annotation.getType()) {
					// found rule for foldable class
					foldableClasses.add(rule.getMetaclass());
				}
			}
		}
		sc.add("return new " + E_CLASS + "[] {");
		for (GenClass foldableClass : foldableClasses) {
			sc.add(genClassUtil.getAccessor(foldableClass) + ",");
		}
		sc.add("};");
		sc.add("}");
		sc.addLineBreak();
	}

	public IGenerator newInstance(GenerationContext context) {
		return new FoldingInformationProviderGenerator(context);
	}
}
