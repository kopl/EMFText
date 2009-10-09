package org.emftext.sdk.codegen.generators;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_CLASS;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.util.GenClassUtil;
import org.emftext.sdk.concretesyntax.Annotation;
import org.emftext.sdk.concretesyntax.AnnotationType;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Rule;

public class FoldingInformationProviderGenerator extends BaseGenerator {

	private final GenClassUtil genClassUtil = new GenClassUtil();

	public FoldingInformationProviderGenerator() {
		super();
	}

	public FoldingInformationProviderGenerator(GenerationContext context) {
		super(context, EArtifact.FOLDING_INFORMATION_PROVIDER);
	}

	@Override
	public boolean generate(PrintWriter out) {
		StringComposite sc = new JavaComposite();
		
        sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
        
        sc.add("public class " + getResourceClassName()+ " {");
        sc.addLineBreak();
        addGetFoldableClassesMethod(sc);
		sc.add("}");
		
		out.write(sc.toString());
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
