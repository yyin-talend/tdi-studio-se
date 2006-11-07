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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.designer.core.model.components.EmfComponent;

/**
 * Component factory that look for each component and load their information. <br/>
 * 
 * $Id$
 */
public class ComponentsFactory implements IComponentsFactory {

    private static List<IComponent> componentList = null;

    public ComponentsFactory() {
    }

    public void init() {
        File dir;
        IComponent currentComp;
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
                return name.endsWith(".xml");
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
                if (childXmlFiles != null) {
                    for (int j = 0; j < childXmlFiles.length; j++) {
                        try {
                            currentComp = new EmfComponent(childXmlFiles[j]);
                            componentList.add(currentComp);
                        } catch (SystemException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
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
        return comp;
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
