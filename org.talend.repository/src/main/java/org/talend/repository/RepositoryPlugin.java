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
package org.talend.repository;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.designer.codegen.perlmodule.IPerlModuleService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.repository";

    // The shared instance
    private static RepositoryPlugin plugin;

    /** Context. */
    private Context userContext;

    public RepositoryPlugin() {
        plugin = this;
        userContext = new Context();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
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

    public static RepositoryPlugin getDefault() {
        return plugin;
    }

    /**
     * Getter for context.
     * 
     * @return the context
     */
    public Context getContext() {
        return this.userContext;
    }

    public IPerlModuleService getPerlModuleService() {
        return (IPerlModuleService) GlobalServiceRegister.getDefault().getService(IPerlModuleService.class);
    }

    public IDesignerCoreService getDesignerCoreService() {
        return (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(IDesignerCoreService.class);
    }

    public IRepositoryService getRepositoryService() {
        return (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
    }

}
