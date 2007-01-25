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
package org.talend.designer.codegen;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.IRepositoryService;

/**
 * Activator for Code Generator.
 * 
 * $Id$
 * 
 */
public class CodeGeneratorActivator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.designer.codegen";

    // The shared instance
    private static CodeGeneratorActivator plugin;

    /**
     * Default Constructor.
     */
    public CodeGeneratorActivator() {
        plugin = this;
    }

    public static CodeGeneratorActivator getDefault() {
        return plugin;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        Preferences preferences = JavaCore.getPlugin().getPluginPreferences();

        if (!isRequiredJREVersion(preferences.getString(JavaCore.COMPILER_COMPLIANCE))) {
            preferences.setValue(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
        }

        if (!isRequiredJREVersion(preferences.getString(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM))) {
            preferences.setValue(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
        }
        if (!isRequiredJREVersion(preferences.getString(JavaCore.COMPILER_SOURCE))) {
            preferences.setValue(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
        }
    }

    private boolean isRequiredJREVersion(String version) {
        if (version.equals(JavaCore.VERSION_1_5) || version.equals(JavaCore.VERSION_1_6)) {
            return true;
        }
        return false;
    }

    /**
     * DOC get a implement of RunProcessService.
     * 
     * @return
     */
    public IRunProcessService getRunProcessService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        return (IRunProcessService) service;
    }

    /**
     * DOC qian Gets the RepositoryService.
     * 
     * @return IRepositoryService
     */
    public IRepositoryService getRepositoryService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        return (IRepositoryService) service;
    }

}
