package org.emftext.sdk.codegen.resource.generators.code_completion;

import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_ATTRIBUTE;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.OBJECT;

import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.TextResourceArtifacts;
import org.emftext.sdk.codegen.resource.generators.JavaBaseGenerator;

public class AttributeValueProviderGenerator extends JavaBaseGenerator<Object> {

	public AttributeValueProviderGenerator() {
		super();
	}

	private AttributeValueProviderGenerator(GenerationContext context) {
		super(context, TextResourceArtifacts.ATTRIBUTE_VALUE_PROVIDER);
	}

	public IGenerator<GenerationContext, Object> newInstance(GenerationContext context, Object parameters) {
		return new AttributeValueProviderGenerator(context);
	}

	@Override
	public boolean generateJavaContents(JavaComposite sc) {
		
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.addJavadoc("This class provides sets of values for attributes. It is used by the code completion processor.");
		sc.add("public class " + getResourceClassName() + " {");
		sc.addLineBreak();
		addGetDefaultValuesMethod(sc);
		sc.add("}");
		return true;
	}

	private void addGetDefaultValuesMethod(StringComposite sc) {
		sc.add("public " + OBJECT + "[] getDefaultValues(" + E_ATTRIBUTE + " attribute) {");
		sc.add("String typeName = attribute.getEType().getName();");
		sc.add("if (\"EString\".equals(typeName)) {");
		sc.add("return new " + OBJECT + "[] {\"some\" + " + stringUtilClassName + ".capitalize(attribute.getName())};");
		sc.add("}");
		sc.add("if (\"EBoolean\".equals(typeName)) {");
		sc.add("return new " + OBJECT + "[] {Boolean.TRUE, Boolean.FALSE};");
		sc.add("}");
		// TODO mseifert: add more default values for other types
		sc.add("return new " + OBJECT + "[] {attribute.getDefaultValue()};");
		sc.add("}");
		sc.addLineBreak();
	}
}