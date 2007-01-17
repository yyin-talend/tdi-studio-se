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
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.xml.XSDValidator;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.ModelPlugin;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.core.model.components.EmfComponent;
import org.xml.sax.SAXException;

/**
 * Component factory that look for each component and load their information. <br/>
 * 
 * $Id$
 */
public class ComponentsFactory implements IComponentsFactory {

    private static final Bundle XSD_CONTAINER_BUNDLE = Platform.getBundle(ModelPlugin.MODEL_PLUGIN_ID);

    private static final String XSD_PATH = "model/Component.xsd";

    private static List<IComponent> componentList = null;

    public ComponentsFactory() {
    }

    public void init() {
        File dir;
        File[] childDirectories, childXmlFiles;
        componentList = new ArrayList<IComponent>();
        Bundle b = Platform.getBundle(IComponentsFactory.COMPONENTS_LOCATION);

        URL url = null;
        try {
            url = FileLocator.toFileURL(FileLocator.find(b, new Path(IComponentsFactory.COMPONENTS_DIRECTORY), null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dir = new File(url.getPath());

        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(final File dir, final String name) {
                String pattern = dir.getName() + "_" + getCodeLanguageSuffix() + ".xml";
                return name.equals(pattern);
            }
        };

        FileFilter fileFilter = new FileFilter() {

            public boolean accept(final File file) {
                return file.isDirectory();
            }

        };
        childDirectories = dir.listFiles(fileFilter);

        if (childDirectories != null) {
            for (int i = 0; i < childDirectories.length; i++) {
                childXmlFiles = childDirectories[i].listFiles(filter);
                if (childXmlFiles != null && childXmlFiles.length > 0) {
                    try {
                        checkFiles(childDirectories[i]);
                        for (int j = 0; j < childXmlFiles.length; j++) {
                            checkXSD(childXmlFiles[j]);
                            IComponent currentComp = new EmfComponent(childXmlFiles[j], ComponentFilesNaming
                                    .getBundleName(childDirectories[i].getName()));
                            loadIcons(childDirectories[i], currentComp);
                            componentList.add(currentComp);
                        }
                    } catch (BusinessException e) {
                        BusinessException ex = new BusinessException("Cannot load component \"" + childDirectories[i].getName()
                                + "\": " + e.getMessage(), e);
                        ExceptionHandler.process(ex, Level.WARN);
                    }
                }
            }
        }
    }

    private void checkXSD(File file) throws BusinessException {
        Path path = new Path(XSD_PATH);
        try {
            URL url = FileLocator.toFileURL(FileLocator.find(XSD_CONTAINER_BUNDLE, path, null));
            File schema = new File(url.getPath());
            XSDValidator.checkXSD(file, schema);
        } catch (IOException e) {
            throw new BusinessException("Cannot find xsd (" + path.lastSegment() + ")", e);
        } catch (SAXException e) {
            throw new BusinessException("Does not match xsd (" + path.lastSegment() + ")", e);
        } catch (ParserConfigurationException e) {
            throw new BusinessException("Cannot check xsd (" + path.lastSegment() + ")", e);
        }
    }

    private String getCodeLanguageSuffix() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        ECodeLanguage codeLanguage = repositoryContext.getProject().getLanguage();
        return codeLanguage.getName();
    }

    private void checkFiles(File folder) throws BusinessException {
        for (String currentFileToCheck : ComponentFilesNaming.getRequiredFilesNames(folder.getName(), getCodeLanguageSuffix())) {
            File file = new File(folder, currentFileToCheck);
            if (!file.exists()) {
                throw new BusinessException("Cannot find file \"" + file.getName() + "\"");
            }
        }
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

        // modified to make the old tRunProcess compatible
        // this test must be removed once the migration tool will be finished.
        // FIXME SML Move to migration tool
        String loadedName = name;
        if (name.equals("tRunProcess")) {
            loadedName = "tRunJob";
        }

        for (int i = 0; i < componentList.size(); i++) {
            comp = componentList.get(i);
            if (comp != null) {
                if (comp.getName().equals(loadedName)) {
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
