package org.emftext.sdk.codegen.generators;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.COLLECTION;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.E_CLASS;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.INPUT_STREAM;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.I_TOKEN_SCANNER;
import static org.emftext.sdk.codegen.generators.IClassNameConstants.STRING;

import java.io.PrintWriter;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.OptionManager;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;

public class MetaInformationGenerator extends BaseGenerator {

	public MetaInformationGenerator() {
		super();
	}

	private MetaInformationGenerator(GenerationContext context) {
		super(context, EArtifact.META_INFORMATION);
	}
	
	@Override
	public boolean generate(PrintWriter out) {
		StringComposite sc = new JavaComposite();
		
        sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
        
        sc.add("public class " + getResourceClassName()+ " implements " + getClassNameHelper().getI_TEXT_RESOURCE_PLUGIN_META_INFORMATION() + " {");
        sc.addLineBreak();
    	addGetConcreteSyntaxName(sc);
    	addGetURIMethod(sc);
    	addCreateLexerMethod(sc);
		addCreateParserMethod(sc);
		addGetClassesWithSyntaxMethod(sc);
        addGetReferenceResolverSwitchMethod(sc);
        addGetTokenResolverFactoryMethod(sc);
        addGetPathTOCSDefinitionMethod(sc);
        addGetTokenNamesMethod(sc);
        addGetDefaultStyleMethod(sc);
        addGetBracketPairsMethod(sc);
        addGetFoldableClassesMethod(sc);
    	addGetHoverTextProviderMethod(sc);
    	
    	
    	String colorManagerClassName = getContext().getQualifiedClassName(EArtifact.COLOR_MANAGER);
        String tokenScannerClassName = getContext().getQualifiedClassName(EArtifact.TOKEN_SCANNER);
        
		sc.add("public " + colorManagerClassName + " createColorManager() {");
        sc.add("return new " + colorManagerClassName + "();");
        sc.add("}");
        sc.addLineBreak();

        sc.add("public " + I_TOKEN_SCANNER + " createTokenScanner(" + colorManagerClassName + " colorManager) {");
		sc.add("return new " + tokenScannerClassName + "(colorManager);");
        sc.add("}");
        sc.addLineBreak();

        sc.add("}");
    	
		out.print(sc.toString());
    	return true;	
	}

	private void addGetFoldableClassesMethod(StringComposite sc) {
		sc.add("public " + E_CLASS + "[] getFoldableClasses() {");
		sc.add("return new " + getContext().getQualifiedClassName(EArtifact.FOLDING_INFORMATION_PROVIDER) + "().getFoldableClasses();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetBracketPairsMethod(StringComposite sc) {
		sc.add("public " + COLLECTION + "<" + getClassNameHelper().getI_BRACKET_PAIR() + "> getBracketPairs() {");
		sc.add("return new " + getContext().getQualifiedClassName(EArtifact.BRACKET_INFORMATION_PROVIDER) + "().getBracketPairs();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetDefaultStyleMethod(StringComposite sc) {
		sc.add("public " + getClassNameHelper().getI_TOKEN_STYLE() + " getDefaultTokenStyle(" + STRING + " tokenName) {");
		sc.add("return new " + getContext().getQualifiedClassName(EArtifact.TOKEN_STYLE_INFORMATION_PROVIDER) + "().getDefaultTokenStyle(tokenName);");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetClassesWithSyntaxMethod(StringComposite sc) {
		sc.add("public " + E_CLASS + "[] getClassesWithSyntax() {");
		sc.add("return new " + getContext().getQualifiedClassName(EArtifact.SYNTAX_COVERAGE_INFORMATION_PROVIDER) + "().getClassesWithSyntax();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addGetHoverTextProviderMethod(StringComposite sc) {
		sc.add("public " + getClassNameHelper().getI_HOVER_TEXT_PROVIDER() + " getHoverTextProvider() {");
		sc.add("return new " + getContext().getQualifiedClassName(EArtifact.HOVER_TEXT_PROVIDER)+"();");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addCreateLexerMethod(StringComposite sc) {
		sc.add("public " + getClassNameHelper().getI_TEXT_SCANNER()+ " createLexer() {");
		if (OptionManager.INSTANCE.useScalesParser(getContext().getConcreteSyntax())) {
			sc.add("return new " + getContext().getQualifiedClassName(EArtifact.SCANNERLESS_SCANNER) + "();");
		} else {
			final String qualifiedAntlrScannerClassName = getContext().getQualifiedClassName(EArtifact.ANTLR_SCANNER);
			final String qualifiedAntlrLexerClassName = getContext().getQualifiedClassName(EArtifact.ANTLR_LEXER);
			sc.add("return new " + qualifiedAntlrScannerClassName + "(new " + qualifiedAntlrLexerClassName + "());");
		}
        sc.add("}");
        sc.addLineBreak();
	}

	private void addGetTokenNamesMethod(StringComposite sc) {
		sc.add("public " + STRING +"[] getTokenNames() {");
		if (OptionManager.INSTANCE.useScalesParser(getContext().getConcreteSyntax())) {
			sc.add("return new " + getContext().getQualifiedClassName(EArtifact.SCANNERLESS_PARSER) + "().getTokenNames();");
		} else {
			sc.add("return new " + getContext().getQualifiedClassName(EArtifact.ANTLR_PARSER) + "(null).getTokenNames();");
		}
        sc.add("}");
        sc.addLineBreak();
	}

	private void addGetPathTOCSDefinitionMethod(StringComposite sc) {
		sc.add("public " + STRING +" getPathToCSDefinition() {");
        sc.add("return \"" + getContext().getSyntaxProjectName() + "/" + getContext().getProjectRelativePathToSyntaxFile() + "\";");
        sc.add("}");
        sc.addLineBreak();
	}

	private void addGetURIMethod(StringComposite sc) {
		sc.add("public " + STRING +" getURI() {");
		sc.add("return \"" + getContext().getConcreteSyntax().getPackage().getNSURI() + "\";");
		sc.add("}");
        sc.addLineBreak();
	}

	private void addGetConcreteSyntaxName(StringComposite sc) {
		sc.add("public " + STRING +" getSyntaxName() {");
    	sc.add("return \"" + getContext().getConcreteSyntax().getName() + "\";");
    	sc.add("}");
        sc.addLineBreak();
	}

	private void addCreateParserMethod(StringComposite sc) {
		String parserClassName = getContext().getQualifiedClassName(EArtifact.ANTLR_PARSER);
	    if (OptionManager.INSTANCE.useScalesParser(getContext().getConcreteSyntax())) {
	    	parserClassName = getContext().getQualifiedClassName(EArtifact.SCANNERLESS_PARSER);
	    }
		
		sc.add("public " + getClassNameHelper().getI_TEXT_PARSER() + " createParser(" + INPUT_STREAM + " inputStream, " + STRING + " encoding) {");
		sc.add("return new " + parserClassName + "().createInstance(inputStream, encoding);");
		sc.add("}");
        sc.addLineBreak();
	}

	private void addGetReferenceResolverSwitchMethod(StringComposite sc) {
		String resolverSwitchClassName = getContext().getQualifiedClassName(EArtifact.REFERENCE_RESOLVER_SWITCH);

		sc.add("public " + getClassNameHelper().getI_REFERENCE_RESOLVER_SWITCH() + " getReferenceResolverSwitch() {");
		sc.add("return new " + resolverSwitchClassName + "();");
		sc.add("}");
        sc.addLineBreak();
	}

	private void addGetTokenResolverFactoryMethod(StringComposite sc) {
		String tokenResolverFactoryClassName = getContext().getQualifiedClassName(EArtifact.TOKEN_RESOLVER_FACTORY);

		sc.add("public " + getClassNameHelper().getI_TOKEN_RESOLVER_FACTORY() + " getTokenResolverFactory() {");
		sc.add("return new " + tokenResolverFactoryClassName + "();");
		sc.add("}");
        sc.addLineBreak();
	}

	public IGenerator newInstance(GenerationContext context) {
		return new MetaInformationGenerator(context);
	}
}
