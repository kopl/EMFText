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

import org.emftext.runtime.resource.IReferenceMapping;

/**
 * A base class for all reference mappings. Instances store the identifier
 * that was resolves and a warning message.
 * 
 * @param <ReferenceType> the type of the reference the identifier can be mapped to.
 */
public abstract class AbstractReferenceMapping<ReferenceType> implements IReferenceMapping<ReferenceType> {

	private String identifier;
	private String warning;
	
	public AbstractReferenceMapping(String identifier, String warning) {
		super();
		this.identifier = identifier;
		this.warning = warning;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getWarning() {
		return warning;
	}

}
