/*******************************************************************************
 * Copyright (c) 2006-2012
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
package org.emftext.sdk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.GenPackageDependentElement;
import org.emftext.sdk.concretesyntax.Import;
import org.emftext.sdk.finders.GenPackageResolveResult;
import org.emftext.sdk.finders.IConcreteSyntaxFinder;
import org.emftext.sdk.finders.IConcreteSyntaxFinderResult;
import org.emftext.sdk.finders.IGenPackageFinder;
import org.emftext.sdk.finders.IResolvedGenPackage;

/**
 * The MetamodelManager uses finders to search for generator packages and
 * concrete syntaxes. The found package are stored in a cache. Attention:
 * This can cause problems, if multiple generator packages with the same
 * name space URI exist, because only the first one that is found is stored
 * in the cache and used subsequently.
 */
public class MetamodelManager implements IGenPackageFinder {
	
	private List<IGenPackageFinder> genPackageFinders = new ArrayList<IGenPackageFinder>();
	private List<IConcreteSyntaxFinder> concreteSyntaxFinders = new ArrayList<IConcreteSyntaxFinder>();
	
	private MetamodelCache cache = new MetamodelCache();
	
	/**
	 * The MetamodelCache maps namespace URIs and location hints to generator packages.
	 * The key for this map is a combination of name space URI and location hint to make
	 * sure that duplicate generator package (i.e., package with the same name space URI)
	 * are correctly looked up.
	 */
	private class MetamodelCache {
		
		private Map<String, IResolvedGenPackage> internalCache = new HashMap<String, IResolvedGenPackage>();
		private Set<String> invalidUris = new HashSet<String>();
		
		public boolean isCached(String nsURI) {
			IResolvedGenPackage result = internalCache.get(nsURI);
			if (result != null) {
				return !result.hasChanged();
			}
			return false;
		}
		
		public IResolvedGenPackage lookUp(String nsURI) {
			return internalCache.get(nsURI);
		}
		
		public void store(String nsURI, IResolvedGenPackage foundPackage) {
			internalCache.put(nsURI, foundPackage);
		}
		
		public boolean isInvalidUri(String uri) {
			return this.invalidUris.contains(uri);
		}

		public void addInvalidUri(String nsURI) {
			this.invalidUris.add(nsURI);			
		}
	}
	
	public MetamodelManager() {
		super();
	}
	
	public void addConcreteSyntaxFinder(IConcreteSyntaxFinder finder) {
		if (finder == null) {
			return;
		}
		concreteSyntaxFinders.add(finder);
	}
	
	public void addGenPackageFinder(IGenPackageFinder finder) {
		if (finder == null) {
			return;
		}
		genPackageFinders.add(finder);
	}

	public GenPackageResolveResult findGenPackages(String nsURI,
			String locationHint, GenPackageDependentElement container,
			Resource resource, boolean resolveFuzzy) {

		if (nsURI == null) {
			return new GenPackageResolveResult(); // empty
		}
		if (cache.isInvalidUri(nsURI) && !resolveFuzzy) {
			return new GenPackageResolveResult(); // empty
		}
		if (cache.isCached(nsURI) && !resolveFuzzy) {
			IResolvedGenPackage genPackageFromCache = cache.lookUp(nsURI);
			Collection<IResolvedGenPackage> result = new LinkedHashSet<IResolvedGenPackage>(1);
			result.add(genPackageFromCache);
			return new GenPackageResolveResult(result); // empty
		}
		
		GenPackageResolveResult result = new GenPackageResolveResult();
		boolean foundPackage = false;
		for (IGenPackageFinder finder : genPackageFinders) {
			GenPackageResolveResult finderResult = finder.findGenPackages(nsURI, locationHint, container, resource, resolveFuzzy);
			if (finderResult != null) {
				if (finderResult.isLocationHintCorrect()) {
					result.setLocationHintCorrect();
				}
				for (IResolvedGenPackage nextResult : finderResult.getResolvedPackages()) {
					cache.store(nsURI, nextResult);
					result.addResolvedGenPackage(nextResult);
					foundPackage = true;
				}
			}
			if (foundPackage && !resolveFuzzy) {
				break;
			}
		}
		if (!foundPackage) {
			this.cache.addInvalidUri(nsURI);
		}
		return result;
	}

	public ConcreteSyntax findConcreteSyntax(String csName, String locationHint, Import container, GenPackage genPackage, Resource textResource) {
		if (csName == null || genPackage == null || genPackage.getEcorePackage() == null) {
			return null;
		}
		
		String csURI = getConcreteSyntaxURI(csName, genPackage);
		for (IConcreteSyntaxFinder finder : concreteSyntaxFinders) {
			IConcreteSyntaxFinderResult finderResult = finder.findConcreteSyntax(csURI, locationHint, container, textResource);
			if (finderResult != null) {
				ConcreteSyntax foundSyntax = finderResult.getConcreteSyntax();
				if (foundSyntax != null) {
					return foundSyntax;
				}
			}
		}
		return null;
	}

	public static String getConcreteSyntaxURI(String csName, GenPackage genPackage) {
		return genPackage.getNSURI() + "%%" + csName;
	}

	public static Map<String, GenPackage> getGenPackages(GenModel genModel) {
		Map<String, GenPackage> genPackages = new LinkedHashMap<String, GenPackage>();
		for (GenPackage genPackage : genModel.getGenPackages()) {
			genPackages.putAll(getNestedPackages(genPackage));
		}
		
		// added to resolve imported GenPackages too. 
		for (GenPackage genPackage : genModel.getUsedGenPackages()) {
			if (genPackage.getEcorePackage() != null) {
				genPackages.putAll(getNestedPackages(genPackage));
			}
		}
		return genPackages;
	}


	private static Map<String, GenPackage> getNestedPackages(GenPackage genPackage) {
		
		Map<String, GenPackage> result = new HashMap<String, GenPackage>();
		result.put(genPackage.getNSURI(), genPackage);
		for (GenPackage nextNested : genPackage.getNestedGenPackages()) {
			result.putAll(getNestedPackages(nextNested));
		}
		return result;
	}

	public void clearFinders() {
		genPackageFinders.clear();
		concreteSyntaxFinders.clear();
	}
}
