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
package org.emftext.sdk.concretesyntax.resource.cs.ui;

import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsPlugin;

/**
 * A singleton class for the text resource UI plug-in.
 */
public class CsUIPlugin extends org.eclipse.ui.plugin.AbstractUIPlugin {
	
	public static final String PLUGIN_ID = "org.emftext.sdk.concretesyntax.resource.cs.ui";
	public static final String EDITOR_ID = "org.emftext.sdk.concretesyntax.resource.cs.ui.CsEditor";
	public static final String EMFTEXT_SDK_VERSION = CsPlugin.EMFTEXT_SDK_VERSION;
	public static final String EP_DEFAULT_LOAD_OPTIONS_ID = PLUGIN_ID + ".default_load_options";
	public static final String EP_ADDITIONAL_EXTENSION_PARSER_ID = PLUGIN_ID + ".additional_extension_parser";
	
	private static CsUIPlugin plugin;
	
	public CsUIPlugin() {
		super();
	}
	
	public void start(org.osgi.framework.BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		// begin customization
		CsOutlinePage.AUTO_EXPAND_LEVEL = 2;
		// end of customization
	}
	
	public void stop(org.osgi.framework.BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public static CsUIPlugin getDefault() {
		return plugin;
	}
	
	public static void showErrorDialog(final String title, final String message) {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				org.eclipse.swt.widgets.Shell parent = new org.eclipse.swt.widgets.Shell();
				org.eclipse.jface.dialogs.MessageDialog dialog = new org.eclipse.jface.dialogs.MessageDialog(parent, title, null, message, org.eclipse.jface.dialogs.MessageDialog.ERROR, new String[] { org.eclipse.jface.dialogs.IDialogConstants.OK_LABEL }, 0) {
				};
				dialog.open();
			}
		});
	}
	
	/**
	 * Helper method for error logging.
	 * 
	 * @param message the error message to log
	 * @param exception the exception that describes the error in detail
	 * 
	 * @return the status object describing the error
	 */
	public static org.eclipse.core.runtime.IStatus logError(String message, Throwable exception) {
		org.eclipse.core.runtime.IStatus status;
		if (exception != null) {
			status = new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.IStatus.ERROR, CsUIPlugin.PLUGIN_ID, 0, message, exception);
		} else {
			status = new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.IStatus.ERROR, CsUIPlugin.PLUGIN_ID, message);
		}
		final CsUIPlugin pluginInstance = CsUIPlugin.getDefault();
		if (pluginInstance == null) {
			System.err.println(message);
			if (exception != null) {
				exception.printStackTrace();
			}
		} else {
			pluginInstance.getLog().log(status);
		}
		return status;
	}
	
}
