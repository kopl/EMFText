package org.emftext.sdk.codegen;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.core.resources.IFolder;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;

/**
 * A resource package provides all information that is needed by the 
 * ResourcePackageGenerator. This includes a resolved concrete syntax, 
 * a package name for parser and printer, a package name for resolvers 
 * (proxy and token resolvers) and a resource target folder.
 * 
 * @see org.emftext.sdk.codegen.ResourcePackageGenerator
 * 
 * @author skarol
 */
public class ResourcePackage {
	
	private ConcreteSyntax csSource;
	private String csPackageName;
	private String resolverPackageName;
	private IFolder targetFolder;
	private Collection<String> generatedResolverClasses = new LinkedHashSet<String>();
	
	public ResourcePackage(ConcreteSyntax csSource, String csPackageName, IFolder targetFolder) {
		if (csSource==null || targetFolder==null) {
			throw new IllegalArgumentException("A ConcreteSyntax and an IFolder have to be specified!");
		}
		this.csSource = csSource;
		this.targetFolder = targetFolder;
		this.csPackageName = csPackageName;
		resolverPackageName = (csPackageName==null || csPackageName.equals("") ? "" : csPackageName + ".") + "analysis";
	}
	
	/**
	 * @return The concrete syntax to be processed and which is 
	 * assumed to contain all resolved information.
	 */
	public ConcreteSyntax getConcreteSyntax(){
		return csSource;
	}
	
	/**
	 * @return The base package where parser and lexer will go to.
	 */
	public String getCsPackageName(){
		return csPackageName;
	}
	
	/**	 
	 * @return The base package where token and proxy resolvers go to.
	 */
	public String getResolverPackageName(){
		return resolverPackageName;
	}
	
	/**
	 * @return The base folder to which generated packages are printed.
	 */
	public IFolder getTargetFolder(){
		return targetFolder;
	}
	
	public void addGeneratedResolverClass(String className) {
		generatedResolverClasses.add(className);
	}

	public Collection<String> getGeneratedResolverClasses() {
		return generatedResolverClasses;
	}
}
