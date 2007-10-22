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
package org.talend.designer.components.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.designer.core.model.components.EmfComponent;

/**
 * Component factory that look for each component and load their information. <br/>
 * 
 * $Id$
 */
public class ComponentsFactory implements IComponentsFactory {

    private static Logger log = Logger.getLogger(ComponentsFactory.class);

    private static final String COMPONENTS_FOLDER_NAME = "components" + File.separatorChar;//$NON-NLS-1$

    private static List<IComponent> componentList = null;

    private ECodeLanguage codeLanguage;

    public ComponentsFactory() {
    }

    public void init() {
        TimeMeasure.measureActive = false;
        TimeMeasure.begin("ComponentsFactory.init");
        long startTime = System.currentTimeMillis();
        componentList = new ArrayList<IComponent>();

        // 1. Load system components:
        loadComponentsFromFolder(IComponentsFactory.COMPONENTS_INNER_FOLDER);
        String userPath = IComponentsFactory.COMPONENTS_INNER_FOLDER + File.separatorChar
                + IComponentsFactory.COMPONENTS_USER_INNER_FOLDER;
        TimeMeasure.step("ComponentsFactory.init", "after system component");

        // 2. Retrieve user components from file system:
        ComponentsRetriever.retrieveComponents(getComponentsLocation(userPath));
        // 3. Load user components:
        loadComponentsFromFolder(userPath);

        TimeMeasure.end("ComponentsFactory.init");
        log.debug(componentList.size() + " components loaded in " + (System.currentTimeMillis() - startTime) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$

        try {
            CorePlugin.getDefault().getRunProcessService().updateLibraries();
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
        TimeMeasure.measureActive = false;
    }

    private void loadComponentsFromFolder(String pathSource) {
        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder");

        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder.checkFiles");
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.checkFiles");

        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder.emf1");
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.emf1");

        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder.emf2");
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.emf2");

        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder.loadIcons");
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.loadIcons");

        // TimeMeasure.display=false;

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
                    TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.checkFiles");
                    ComponentFileChecker.checkComponentFolder(currentFolder, getCodeLanguageSuffix());
                    TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.checkFiles");
                    TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.emf1");
                    File xmlMainFile = new File(currentFolder, ComponentFilesNaming.getInstance().getMainXMLFileName(
                            currentFolder.getName(), getCodeLanguageSuffix()));
                    TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.emf1");
                    TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.emf2");
                    EmfComponent currentComp = new EmfComponent(xmlMainFile, pathSource);
                    TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.emf2");

                    if (componentList.contains(currentComp)) {
                        log.warn("Component " + currentComp.getName() + " already exists. Cannot load user version."); //$NON-NLS-1$ //$NON-NLS-2$
                    } else {
                        currentComp.setResourceBundle(getComponentResourceBundle(currentComp, pathSource));
                        TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.loadIcons");
                        loadIcons(currentFolder, currentComp);
                        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.loadIcons");
                        componentList.add(currentComp);
                    }
                } catch (MissingMainXMLComponentFileException e) {
                    log.trace(currentFolder.getName() + " is not a " + getCodeLanguageSuffix() + " component", e);
                } catch (BusinessException e) {
                    BusinessException ex = new BusinessException(
                            "Cannot load component \"" + currentFolder.getName() + "\": " //$NON-NLS-1$ //$NON-NLS-2$
                                    + e.getMessage(), e);
                    ExceptionHandler.process(ex, Level.WARN);
                }
            }
        }
        // TimeMeasure.display=true;
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.checkFiles");
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.emf1");
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.emf2");
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.loadIcons");
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder");
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

    /**
     * 
     * 
     * Needs to create our own class loader in order to clear the cache for a ResourceBundle. Without using a new class
     * loader each time the values would not be reread from the .properties file
     * 
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4212439
     * 
     * yzhang ComponentsFactory class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private static class ResClassLoader extends ClassLoader {

        ResClassLoader(ClassLoader parent) {
            super(parent);
        }
    }

    private ResourceBundle getComponentResourceBundle(IComponent currentComp, String source) {
        String label = ComponentFilesNaming.getInstance().getBundleName(currentComp.getName(), source);
        // String pluginFullName = currentComp.getPluginFullName();
        // System.out.println(pluginFullName);
        // Bundle bundle = Platform.getBundle(pluginFullName);
        // ClassLoader classLoader = bundle.getClass().getClassLoader();
        // return ResourceBundle.getBundle(label, Locale.getDefault(), classLoader);

        ResourceBundle bundle = ResourceBundle.getBundle(label, Locale.getDefault(), new ResClassLoader(getClass()
                .getClassLoader()));

        return bundle;
    }

    private String getCodeLanguageSuffix() {
        if (codeLanguage == null) {
            RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY);
            codeLanguage = repositoryContext.getProject().getLanguage();
        }
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getComponentPath()
     */
    public URL getComponentPath() throws IOException {
        Bundle b = Platform.getBundle(IComponentsFactory.COMPONENTS_LOCATION);
        URL url = FileLocator
                .toFileURL(FileLocator.find(b, new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER), null));
        return url;
    }
}
