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
import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentFileNaming;
import org.talend.core.model.temp.ECodePart;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public final class ComponentFilesNaming implements IComponentFileNaming {

    private static ComponentFilesNaming singleton;

    private ComponentFilesNaming() {

    }

    public static ComponentFilesNaming getInstance() {
        if (singleton == null) {
            singleton = new ComponentFilesNaming();
        }
        return singleton;
    }

    public List<String> getRequiredFilesNames(String componentName, String languageSuffix) {
        List<String> toReturn = new ArrayList<String>();
      //  toReturn.add(getMainXMLFileName(componentName, languageSuffix));
        toReturn.add(getPropertiesFileName(componentName));
        toReturn.add(getIcon32FileName(componentName));
        return toReturn;
    }

    public String getMainXMLFileName(String componentName, String languageSuffix) {
        return componentName + "_" + languageSuffix + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String getPropertiesFileName(String componentName) {
        return componentName + "_messages.properties"; //$NON-NLS-1$
    }

    public String getBundleName(String componentName, String source) {
        String baseName = source.replace(File.separatorChar, '.');
        return baseName + "." + componentName + "." + componentName + "_messages"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public String getJetFileName(IComponent component, String languageSuffix, ECodePart codePart) {
        return component.getName() + "_" + codePart + "." + languageSuffix + "jet"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public String getIcon32FileName(String componentName) {
        return componentName + "_icon32.png"; //$NON-NLS-1$
    }

    public String getIcon24FileName(String componentName) {
        return componentName + "_icon24.png"; //$NON-NLS-1$
    }

    public String getIcon16FileName(String componentName) {
        return componentName + "_icon16.png"; //$NON-NLS-1$
    }
}
