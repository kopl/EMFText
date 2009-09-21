package org.emftext.sdk.codegen.generators.interfaces;

import static org.emftext.sdk.codegen.generators.IClassNameConstants.INPUT_STREAM;

import java.io.PrintWriter;

import org.emftext.sdk.codegen.EArtifact;
import org.emftext.sdk.codegen.GenerationContext;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.generators.BaseGenerator;

public class InputStreamProcessorGenerator extends BaseGenerator {

	public InputStreamProcessorGenerator() {
		super();
	}

	private InputStreamProcessorGenerator(GenerationContext context) {
		super(context, EArtifact.INPUT_STREAM_PROCESSOR);
	}

	public IGenerator newInstance(GenerationContext context) {
		return new InputStreamProcessorGenerator(context);
	}

	public boolean generate(PrintWriter out) {
		org.emftext.sdk.codegen.composites.StringComposite sc = new org.emftext.sdk.codegen.composites.JavaComposite();

		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		
		sc.add("// A InputStreamProcessor can be used like a normal " + INPUT_STREAM + ",");
		sc.add("// but provides information about the encoding that is used to");
		sc.add("// represent characters as bytes.");
		sc.add("public abstract class " + getResourceClassName() + " extends " + INPUT_STREAM + " {");
		sc.addLineBreak();
		
		sc.add("// Returns the encoding of the characters that can be read");
		sc.add("// from this InputStreamProcessor. This encoding is passed");
		sc.add("// to subsequent streams (e.g., the ANTLRInputStream).");
		sc.add("public abstract String getOutputEncoding();");
		
		sc.add("}");
		
		out.print(sc.toString());
		return true;
	}
}