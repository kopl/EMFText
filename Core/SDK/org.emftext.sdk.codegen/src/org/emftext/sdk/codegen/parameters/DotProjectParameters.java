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
package org.emftext.sdk.codegen.parameters;

import org.emftext.sdk.IPluginDescriptor;
import org.emftext.sdk.codegen.ArtifactDescriptor;
import org.emftext.sdk.codegen.IContext;

public class DotProjectParameters<ContextType extends IContext<ContextType>> extends AbstractArtifactParameter<ContextType, DotProjectParameters<ContextType>> {

	private final IPluginDescriptor plugin;

	public DotProjectParameters(ArtifactDescriptor<ContextType, DotProjectParameters<ContextType>> artifact, IPluginDescriptor plugin) {
		super(artifact);
		this.plugin = plugin;
	}

	public IPluginDescriptor getPlugin() {
		return plugin;
	}
}
