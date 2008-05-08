package org.reuseware.emftextedit.codegen;

import java.io.PrintWriter;


/**
 * Generates basic stubs for proxy resolvers.
 *
 */
public class ProxyResolverGenerator extends BaseGenerator {
	
	public ProxyResolverGenerator(String resolveClassName, String resolvePackageName){
		super(resolveClassName,resolvePackageName);
	}
	
	@Override
	public boolean generate(PrintWriter out) {     
	    out.println("package " + this.getResourcePackageName() + ";");	
	    out.println();
	    out.println("import org.reuseware.emftextedit.resource.*;");
	    out.println("import org.reuseware.emftextedit.resource.impl.*;");
	    out.println();    
		out.println("public class " + this.getResourceClassName() + " extends ProxyResolverImpl {");
		out.println();
		out.println("}");
		
		return true;
	}
	


}
