// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.runprocess;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.core.GlobalServiceRegister;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.repository.model.IRepositoryService;

/**
 * The activator class controls the plug-in life cycle.
 */
public class RunProcessPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.designer.runprocess"; //$NON-NLS-1$

    // The shared instance
    private static RunProcessPlugin plugin;

    /** The running process contexts manager. */
    private RunProcessContextManager runProcessContextManager;

    /**
     * Constructs a new Activator.
     */
    public RunProcessPlugin() {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        runProcessContextManager = new RunProcessContextManager();
        GlobalServiceRegister.registerRunProcessService(new RunProcessService());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     * 
     * @return the shared instance
     */
    public static RunProcessPlugin getDefault() {
        return plugin;
    }

    /**
     * Getter for runProcessContextManager.
     * 
     * @return the runProcessContextManager
     */
    public RunProcessContextManager getRunProcessContextManager() {
        return this.runProcessContextManager;
    }

    public IRepositoryService getRepositoryService() {
        return GlobalServiceRegister.getRepositoryService();
    }
    
    /**
     * DOC get a implement of  CodeGeneratorService.
     * @return a implement of  CodeGeneratorService
     */
    public ICodeGeneratorService getCodeGeneratorService() {
        return GlobalServiceRegister.getCodeGeneratorService();
    }

}
