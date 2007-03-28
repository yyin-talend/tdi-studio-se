// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Modes;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.core.ui.ActiveProcessTracker;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;

/**
 * The designer plugin class to be used in the desktop. <br/>
 * 
 * $Id$
 * 
 */
/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DesignerPlugin extends AbstractUIPlugin {

    public static final String ID = "org.talend.designer.core"; //$NON-NLS-1$

    private static DesignerPlugin plugin;

    public DesignerPlugin() {
        plugin = this;
        Modes.getMode("perl.xml"); //$NON-NLS-1$
        Modes.getMode("tsql.xml"); //$NON-NLS-1$
        Modes.getMode("java.xml"); //$NON-NLS-1$
        /*
         * // plugins list IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry(); String[] namespaces =
         * extensionRegistry.getNamespaces(); System.out.println("Liste des plugins talend:"); for (int i = 0; i <
         * namespaces.length; i++) { if (namespaces[i].startsWith("org.talend")) { String message = namespaces[i];
         * Bundle bundle = Platform.getBundle(namespaces[i]);
         * 
         * if (bundle == null) { message += " pb"; } else { String version = (String)
         * bundle.getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION); String[] subVersions =
         * version.split("\\."); version = subVersions[0] + "." + subVersions[1] + "." + subVersions[2]; message += "
         * version:" + version; } System.out.println(message); } }
         */
    }

    public void start(final BundleContext context) throws Exception {
        super.start(context);
        IWorkbenchWindow activeWorkbenchWindow = null;
        activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            IPartService partService = activeWorkbenchWindow.getPartService();
            partService.addPartListener(new ActiveProcessTracker());
        }
    }

    public void stop(final BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    public static DesignerPlugin getDefault() {
        return plugin;
    }

    /**
     * DOC get a implement of RunProcessService.
     * 
     * @return a implement of RunProcessService
     */
    public IRunProcessService getRunProcessService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        return (IRunProcessService) service;
    }

    /**
     * DOC get a implement of CodeGeneratorService.
     * 
     * @return a implement of CodeGeneratorService
     */
    public ICodeGeneratorService getCodeGeneratorService() {
        IService service = GlobalServiceRegister.getDefault().getService(ICodeGeneratorService.class);
        return (ICodeGeneratorService) service;
    }

    /**
     * DOC get a implement of IRepositoryService.
     * 
     * @return a implement of IRepositoryService
     */
    public IRepositoryService getRepositoryService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        return (IRepositoryService) service;
    }

    public IProxyRepositoryFactory getProxyRepositoryFactory() {
        IRepositoryService service = getRepositoryService();
        return service.getProxyRepositoryFactory();
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(final String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.talend.designer.core", path); //$NON-NLS-1$
    }

}
