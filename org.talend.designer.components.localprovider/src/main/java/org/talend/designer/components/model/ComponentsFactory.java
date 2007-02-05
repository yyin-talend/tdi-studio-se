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
package org.talend.designer.components.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.core.model.components.EmfComponent;

/**
 * Component factory that look for each component and load their information. <br/>
 * 
 * $Id$
 */
public class ComponentsFactory implements IComponentsFactory {

    private static Logger log = Logger.getLogger(ComponentsFactory.class);

    private static List<IComponent> componentList = null;

    public ComponentsFactory() {
    }

    public void init() {
        long startTime = System.currentTimeMillis();
        componentList = new ArrayList<IComponent>();

        // 1. Load system components:
        loadComponentsFromFolder(IComponentsFactory.COMPONENTS_INNER_FOLDER);
        String userPath = IComponentsFactory.COMPONENTS_INNER_FOLDER + File.separatorChar
                + IComponentsFactory.COMPONENTS_USER_INNER_FOLDER;
        // 2. Retrieve user components from file system:
        ComponentsRetriever.retrieveComponents(getComponentsLocation(userPath));
        // 3. Load user components:
        loadComponentsFromFolder(userPath);

        log.trace(componentList.size() + " components loaded in " + (System.currentTimeMillis() - startTime) + " ms");  //$NON-NLS-1$ //$NON-NLS-2$
    }

    private void loadComponentsFromFolder(String pathSource) {
        File source = getComponentsLocation(pathSource);
        File[] childDirectories;

        FileFilter fileFilter = new FileFilter() {

            public boolean accept(final File file) {
                return file.isDirectory() && file.getName().charAt(0) != '.'
                        && !file.getName().equals(IComponentsFactory.COMPONENTS_USER_INNER_FOLDER);
            }

        };
        childDirectories = source.listFiles(fileFilter);

        if (childDirectories != null) {
            for (File currentFolder : childDirectories) {
                try {
                    ComponentFileChecker.checkComponentFolder(currentFolder, getCodeLanguageSuffix());
                    File xmlMainFile = new File(currentFolder, ComponentFilesNaming.getInstance().getMainXMLFileName(
                            currentFolder.getName(), getCodeLanguageSuffix()));
                    EmfComponent currentComp = new EmfComponent(xmlMainFile, pathSource);

                    if (componentList.contains(currentComp)) {
                        log.warn("Component " + currentComp.getName() + " already exists. Cannot load user version."); //$NON-NLS-1$ //$NON-NLS-2$
                    } else {
                        currentComp.setResourceBundle(getComponentResourceBundle(currentComp, pathSource));
                        loadIcons(currentFolder, currentComp);
                        componentList.add(currentComp);
                    }
                } catch (BusinessException e) {
                    BusinessException ex = new BusinessException("Cannot load component \"" + currentFolder.getName() + "\": " //$NON-NLS-1$ //$NON-NLS-2$
                            + e.getMessage(), e);
                    ExceptionHandler.process(ex, Level.WARN);
                }
            }
        }
    }

    /**
     * DOC smallet Comment method "checkComponentFolder".
     * 
     * @param currentFolder
     * @return
     * @throws BusinessException
     */

    private File getComponentsLocation(String folder) {
        Bundle b = Platform.getBundle(IComponentsFactory.COMPONENTS_LOCATION);

        URL url = null;
        try {
            url = FileLocator.toFileURL(FileLocator.find(b, new Path(folder), null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File dir = new File(url.getPath());
        return dir;
    }

    private ResourceBundle getComponentResourceBundle(IComponent currentComp, String source) {
        String label = ComponentFilesNaming.getInstance().getBundleName(currentComp.getName(), source);
        // String pluginFullName = currentComp.getPluginFullName();
        // System.out.println(pluginFullName);
        // Bundle bundle = Platform.getBundle(pluginFullName);
        // ClassLoader classLoader = bundle.getClass().getClassLoader();
        // return ResourceBundle.getBundle(label, Locale.getDefault(), classLoader);
        return ResourceBundle.getBundle(label);
    }

    private String getCodeLanguageSuffix() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        ECodeLanguage codeLanguage = repositoryContext.getProject().getLanguage();
        return codeLanguage.getName();
    }

    private void loadIcons(File folder, IComponent component) {
        ComponentIconLoading cil = new ComponentIconLoading(folder);

        component.setIcon32(cil.getImage32());
        component.setIcon24(cil.getImage24());
        component.setIcon16(cil.getImage16());
    }

    public int size() {
        if (componentList == null) {
            init();
        }
        return componentList.size();
    }

    public IComponent get(final int i) {
        if (componentList == null) {
            init();
        }
        return componentList.get(i);
    }

    public IComponent get(final String name) {
        IComponent comp = null;
        if (componentList == null) {
            init();
        }

        for (int i = 0; i < componentList.size(); i++) {
            comp = componentList.get(i);
            if (comp != null) {
                if (comp.getName().equals(name)) {
                    return comp;
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getComponents()
     */
    public List<IComponent> getComponents() {
        if (componentList == null) {
            init();
        }
        return componentList;
    }
}
