/*******************************************************************************
 * Copyright (c) 2006-2009 
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
package org.emftext.runtime.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.emftext.runtime.resource.ITokenResolver;
import org.emftext.runtime.resource.ITokenResolverFactory;

/**
 * A base class for all generated token resolver factories.
 * For subclasses it is sufficient to register the token 
 * resolvers. 
 */
@Deprecated public abstract class AbstractTokenResolverFactory implements ITokenResolverFactory {

	private Map<String,ITokenResolver> tokenName2TokenResolver;
	private Map<String,ITokenResolver> featureName2CollectInTokenResolver;
	private static ITokenResolver defaultResolver = new JavaBasedTokenResolver();
	
	public AbstractTokenResolverFactory(){
		tokenName2TokenResolver = new HashMap<String,ITokenResolver>();
		featureName2CollectInTokenResolver = new HashMap<String,ITokenResolver>();
	}
	
	public ITokenResolver createTokenResolver(String tokenName) {		
		return internalCreateResolver(tokenName2TokenResolver, tokenName);
	}

	public ITokenResolver createCollectInTokenResolver(String featureName) {
		return internalCreateResolver(featureName2CollectInTokenResolver, featureName);
	}
	
	protected boolean registerTokenResolver(String tokenName, ITokenResolver resolver){
		return internalRegisterTokenResolver(tokenName2TokenResolver, tokenName, resolver);
	}

	protected boolean registerCollectInTokenResolver(String featureName, ITokenResolver resolver){
		return internalRegisterTokenResolver(featureName2CollectInTokenResolver, featureName, resolver);
	}
	
	protected ITokenResolver deRegisterTokenResolver(String tokenName){
		return tokenName2TokenResolver.remove(tokenName);
	}

	private ITokenResolver internalCreateResolver(Map<String,ITokenResolver> resolverMap, String key) {
		if(resolverMap.containsKey(key)){
			return resolverMap.get(key);
		}
		else{
			return defaultResolver;
		}
	}

	private boolean internalRegisterTokenResolver(Map<String,ITokenResolver> resolverMap, 
			String key,
			ITokenResolver resolver) {
		if(!resolverMap.containsKey(key)){
			resolverMap.put(key,resolver);
			return true;
		}
		return false;
	}
}
