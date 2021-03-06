/*******************************************************************************
 * Copyright (c) 2006-2014
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
package org.emftext.sdk.ui.handlers;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.handlers.HandlerUtil;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsMetaInformation;
import org.emftext.sdk.concretesyntax.resource.cs.ui.CsUIPlugin;
import org.emftext.sdk.ui.jobs.GenerateResourcePluginsJob;

public class GenerateAllHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = ((IStructuredSelection) currentSelection);
			Iterator<?> it = selection.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				try {
					if (o instanceof IAdaptable) {
						IAdaptable a = (IAdaptable) o;
						IResource adapter = (IResource) a.getAdapter(IResource.class);
						if (adapter != null) {
							traverse(adapter);
						} else if (a instanceof IWorkingSet) {
							IWorkingSet workingSet = (IWorkingSet) a;
							traverse(workingSet);
						}
					}
				} catch (CoreException e) {
					CsUIPlugin.logError("Exception while traversing selection", e);
				}
			}
		}
		return null;
	}

	private void traverse(IWorkingSet workingSet) throws CoreException {
		IAdaptable[] elements = workingSet.getElements();
		for (IAdaptable adaptable : elements) {
			IResource resource = (IResource) adaptable.getAdapter(IResource.class);
			if (resource != null) {
				traverse(resource);
			}
		}
	}

	private void traverse(IResource resource) throws CoreException {
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			process(file);
		} else {
			IProject project = resource.getProject();
			if (project != null && project.isOpen()) {
				resource.accept(new IResourceVisitor() {

					@Override
					public boolean visit(IResource resource) throws CoreException {
						process(resource);
						return true;
					}
				});
			}
		}
	}

	private void process(IResource resource) throws CoreException {
		if (resource instanceof IFile) {
			final IFile file = (IFile) resource;
			String fileExtension = file.getFileExtension();
			if (fileExtension == null) {
				return;
			}

			Job job = null;
			
			if (fileExtension.equals(new CsMetaInformation().getSyntaxName())) {
				job = new GenerateResourcePluginsJob("Generating resource project for " + file.getName(), file);
			} else if ("genmodel".equals(fileExtension)) {
				job = new Job("Generate metamodel code job") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
						ResourceSet rs = new ResourceSetImpl();
						
						@SuppressWarnings("deprecation")
						Map<URI, URI> platformURIMap = EcorePlugin.computePlatformURIMap();
						rs.getURIConverter().getURIMap().putAll(platformURIMap);

						Resource genModelResource = rs.getResource(uri, true);
						List<EObject> contents = genModelResource.getContents();
						for (EObject eObject : contents) {
							if (eObject instanceof GenModel) {
								GenModel genModel = (GenModel) eObject;
								genModel.reconcile();
								genModel.setCanGenerate(true);
								
								Generator generator = new Generator();
								generator.setInput(genModel);
								Set<String> types = getGeneratorTypes(file, genModel);
								for (String type : types) {
									generator.generate(genModel, type, new BasicMonitor());
								}
							}
						}
						return Status.OK_STATUS;
					}
				};
			}
			
			if (job != null) {
				job.setUser(true);
				job.schedule();
			}
		}
	}

	private Set<String> getGeneratorTypes(IFile file, GenModel genModel) {
		Set<String> typeSet = new LinkedHashSet<String>();
		typeSet.add(GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE);
		
		IProject project = file.getProject();
		IWorkspace workspace = project.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		String editPluginDirectory = genModel.getEditPluginDirectory();
		IProject editProject = getProject(root, editPluginDirectory);
		if (editProject.exists()) {
			typeSet.add(GenBaseGeneratorAdapter.EDIT_PROJECT_TYPE);
		}
		
		String editorPluginDirectory = genModel.getEditorPluginDirectory();
		IProject editorProject = getProject(root, editorPluginDirectory);
		if (editorProject.exists()) {
			typeSet.add(GenBaseGeneratorAdapter.EDITOR_PROJECT_TYPE);
		}
		return typeSet;
	}

	private IProject getProject(IWorkspaceRoot root, String directory) {
		Path directoryPath = new Path(directory);
		IFolder folder = root.getFolder(directoryPath);
		IProject project = folder.getProject();
		return project;
	}
}
