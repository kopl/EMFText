package org.emftext.sdk.codegen.generators.util;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.COLLECTION;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_ATTRIBUTE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_CLASS;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_CLASSIFIER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_OBJECT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_PACKAGE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_REFERENCE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_STRUCTURAL_FEATURE;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.INTERNAL_E_OBJECT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.LIST;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.OBJECT;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.URI;

import java.io.PrintWriter;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.generators.BaseGenerator;

public class MinimalModelHelperGenerator extends BaseGenerator {

	private String eClassUtilClassName;
	private String stringUtilClassName;
	private String listUtilClassName;

	public MinimalModelHelperGenerator() {
		super();
	}

	private MinimalModelHelperGenerator(GenerationContext context) {
		super(context, EArtifact.MINIMAL_MODEL_HELPER);
		eClassUtilClassName = getContext().getQualifiedClassName(EArtifact.E_CLASS_UTIL);
		stringUtilClassName = getContext().getQualifiedClassName(EArtifact.STRING_UTIL);
		listUtilClassName = getContext().getQualifiedClassName(EArtifact.LIST_UTIL);
	}

	public IGenerator newInstance(GenerationContext context) {
		return new MinimalModelHelperGenerator(context);
	}

	public boolean generate(PrintWriter out) {
		org.emftext.sdk.codegen.composites.StringComposite sc = new org.emftext.sdk.codegen.composites.JavaComposite();
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.add("// A helper class that is able to create minimal model instances for Ecore");
		sc.add("// models.");
		sc.add("//");
		// TODO mseifert: add cross references where possible
		//                Proxies are now added with a pointer to nowhere. This way we have
		//                at least something that can be printed even if it results in an error.
		sc.add("public class " + getResourceClassName() + " {");
		sc.addLineBreak();
		sc.add("private final static " + eClassUtilClassName + " eClassUtil = new " + eClassUtilClassName + "();");
		sc.addLineBreak();
		sc.add("public " + E_OBJECT + " getMinimalModel(" + E_CLASS + " eClass, " + COLLECTION + "<" + E_CLASS + "> allAvailableClasses) {");
		sc.add("return getMinimalModel(eClass, allAvailableClasses.toArray(new " + E_CLASS + "[allAvailableClasses.size()]), null);");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public " + E_OBJECT + " getMinimalModel(" + E_CLASS + " eClass, " + E_CLASS + "[] allAvailableClasses) {");
		sc.add("return getMinimalModel(eClass, allAvailableClasses, null);");
		sc.add("}");
		sc.addLineBreak();
		sc.add("public " + E_OBJECT + " getMinimalModel(" + E_CLASS + " eClass, " + E_CLASS + "[] allAvailableClasses, String name) {");
		sc.add(E_PACKAGE + " ePackage = eClass.getEPackage();");
		sc.add("if (ePackage == null) {");
		sc.add("return null;");
		sc.add("}");
		sc.add(E_OBJECT + " root = ePackage.getEFactoryInstance().create(eClass);");
		sc.add(LIST + "<" + E_STRUCTURAL_FEATURE + "> features = eClass.getEAllStructuralFeatures();");
		sc.add("for (" + E_STRUCTURAL_FEATURE + " feature : features) {");
		sc.add("if (feature instanceof " + E_REFERENCE + ") {");
		sc.add("" + E_REFERENCE + " reference = (" + E_REFERENCE + ") feature;");
		sc.add("if (reference.isUnsettable()) {");
		sc.add("continue;");
		sc.add("}");
		sc.add("if (!reference.isChangeable()) {");
		sc.add("continue;");
		sc.add("}");
		sc.addLineBreak();
		sc.add(E_CLASSIFIER + " type = reference.getEType();");
		sc.add("if (type instanceof " + E_CLASS + ") {");
		sc.add(E_CLASS + " typeClass = (" + E_CLASS + ") type;");
		sc.add("if (eClassUtil.isNotConcrete(typeClass)) {");
		sc.add("// find subclasses");
		sc.add(LIST + "<" + E_CLASS + "> subClasses = eClassUtil.getSubClasses(typeClass, allAvailableClasses);");
		sc.add("if (subClasses.size() == 0) {");
		sc.add("continue;");
		sc.add("} else {");
		sc.add("// pick the first subclass");
		sc.add("typeClass = subClasses.get(0);");
		sc.add("}");
		sc.add("}");
		sc.add("int lowerBound = reference.getLowerBound();");
		sc.add("for (int i = 0; i < lowerBound; i++) {");
		sc.add(E_OBJECT + " subModel = null;");
		sc.add("if (reference.isContainment()) {");
		sc.add("subModel = getMinimalModel(typeClass, allAvailableClasses);");
		sc.add("}");
		sc.add("else {");
		sc.add("// TODO jjohannes: can we actually do this? proxies with");
		sc.add("// URIs that can not be resolved cause problem when printing");
		sc.add("// them. I think we should rather use object that exists in");
		sc.add("// the model and fill non-containment references with them.");
		sc.add("//");
		sc.add("// the code below prevents the NewFileWizard for the CS language");
		sc.add("// to work");
		sc.add("//");
		sc.add("subModel = typeClass.getEPackage().getEFactoryInstance().create(typeClass);");
		sc.add("//set some proxy URI to make this object a proxy");
		sc.add("String initialValue = \"#some\" + " + stringUtilClassName + ".capitalize(typeClass.getName());");
		sc.add(URI + " proxyURI = " + URI + ".createURI(initialValue);");
		sc.add("((" + INTERNAL_E_OBJECT + ")subModel).eSetProxyURI(proxyURI);");
		sc.add("}");
		sc.add("if (subModel == null) {");
		sc.add("continue;");
		sc.add("}");
		sc.addLineBreak();
		sc.add(OBJECT + " value = root.eGet(reference);");
		sc.add("if (value instanceof " + LIST + "<?>) {");
		sc.add(LIST + "<" + E_OBJECT + "> list = " + listUtilClassName + ".castListUnchecked(value);");
		sc.add("list.add(subModel);");
		sc.add("} else {");
		sc.add("root.eSet(reference, subModel);");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("} else if (feature instanceof " + E_ATTRIBUTE + ") {");
		sc.add(E_ATTRIBUTE + " attribute = (" + E_ATTRIBUTE + ") feature;");
		sc.add("if (\"EString\".equals(attribute.getEType().getName())) {");
		sc.add("String initialValue;");
		sc.add("if(attribute.getName().equals(\"name\") && name != null) {");
		sc.add("initialValue = name;");
		sc.add("}");
		sc.add("else {");
		sc.add("initialValue = \"some\" + " + stringUtilClassName + ".capitalize(attribute.getName());");
		sc.add("}");
		sc.add(OBJECT + " value = root.eGet(attribute);");
		sc.add("if (value instanceof " + LIST + "<?>) {");
		sc.add(LIST + "<String> list = " + listUtilClassName + ".castListUnchecked(value);");
		sc.add("list.add(initialValue);");
		sc.add("} else {");
		sc.add("root.eSet(attribute, initialValue);");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("}");
		sc.add("return root;");
		sc.add("}");
		sc.add("}");
		out.print(sc.toString());
		return true;
	}
}