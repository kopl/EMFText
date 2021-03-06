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
package org.emftext.runtime;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;
import org.emftext.runtime.resource.ITextDiagnostic;

/**
 * Helper class to add markers to test files based on EMF's <code>Resource.Diagnostic</code>.
 * If a resource contains <code>TextDiagnostic</code>s it uses the more precise information of
 * this extended diagnostic type.
 * 
 * @author Jendrik Johannes
 */
@Deprecated public class MarkerHelper {
    
    public static final String MARKER_TYPE = "org.emftext.runtime.ui.problem";
    
    /**
     * Marks a file with markers.
     * 
     * @param resource The resource that is the file to mark.
     * @throws CoreException 
     */
    public static void mark(Resource resource) throws CoreException {
    	if (resource == null) return;
    	
        IMarker marker;
        IFile file = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(resource.getURI().toPlatformString(true));
        
        //URI might not point at a platform file
        if (file != null) for(Resource.Diagnostic diagnostic : resource.getErrors()) {
            marker = file.createMarker(MARKER_TYPE);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            marker.setAttribute(IMarker.MESSAGE, diagnostic.getMessage());
        	if (diagnostic instanceof ITextDiagnostic) {
        		marker.setAttribute(IMarker.LINE_NUMBER, ((ITextDiagnostic)diagnostic).getLine());
                marker.setAttribute(IMarker.CHAR_START, ((ITextDiagnostic)diagnostic).getCharStart());
                marker.setAttribute(IMarker.CHAR_END, ((ITextDiagnostic)diagnostic).getCharEnd() + 1);		
        	}
        	else {
                marker.setAttribute(IMarker.CHAR_START, 0);
                marker.setAttribute(IMarker.CHAR_END, 1);
        	}
        }

        for(Resource.Diagnostic diagnostic : resource.getWarnings()) {
            marker = file.createMarker(MARKER_TYPE);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
            marker.setAttribute(IMarker.MESSAGE, diagnostic.getMessage());
        	if (diagnostic instanceof ITextDiagnostic) {
        		marker.setAttribute(IMarker.LINE_NUMBER, ((ITextDiagnostic)diagnostic).getLine());
                marker.setAttribute(IMarker.CHAR_START, ((ITextDiagnostic)diagnostic).getCharStart());
                marker.setAttribute(IMarker.CHAR_END, ((ITextDiagnostic)diagnostic).getCharEnd() + 1);		
        	}
        	else {
                marker.setAttribute(IMarker.CHAR_START, 0);
                marker.setAttribute(IMarker.CHAR_END, 1);
        	}
        }
    }
    
    /**
     * Removes all markers from a file.
     * 
     * @param resource The resource that is the file.
     * @throws CoreException
     */
    public static void unmark(Resource resource) throws CoreException {
        IFile file = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(resource.getURI().toPlatformString(true));
        file.deleteMarkers(MarkerHelper.MARKER_TYPE, false, IResource.DEPTH_ZERO);    
    }

}
