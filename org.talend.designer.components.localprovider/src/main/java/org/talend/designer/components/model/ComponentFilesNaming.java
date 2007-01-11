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

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.temp.ECodePart;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ComponentFilesNaming {

    public static List<String> getRequiredFilesNames(String componentName, String languageSuffix) {
        List<String> toReturn = new ArrayList<String>();
        toReturn.add(getMainXMLFileName(componentName, languageSuffix));
        toReturn.add(getPropertiesFileName(componentName));
        toReturn.add(getIcon32FileName(componentName));
        // toReturn.add(getBeginJetFileName(componentName, languageSuffix));
        // toReturn.add(getMainJetFileName(componentName, languageSuffix));
        // toReturn.add(getEndJetFileName(componentName, languageSuffix));
        return toReturn;
    }

    public static String getMainXMLFileName(String componentName, String languageSuffix) {
        return componentName + "_" + languageSuffix + ".xml";
    }

    public static String getPropertiesFileName(String componentName) {
        return componentName + "_messages.properties";
    }

    public static String getBundleName(String componentName) {
        return IComponentsFactory.COMPONENTS_DIRECTORY + "." + componentName + "." + componentName + "_messages";
    }

    public static String getBeginJetFileName(String componentName, String languageSuffix) {
        return componentName + "_" + ECodePart.BEGIN + "." + languageSuffix + "jet";
    }

    public static String getMainJetFileName(String componentName, String languageSuffix) {
        return componentName + "_" + ECodePart.MAIN + "." + languageSuffix + "jet";
    }

    public static String getEndJetFileName(String componentName, String languageSuffix) {
        return componentName + "_" + ECodePart.END + "." + languageSuffix + "jet";
    }

    public static String getIcon32FileName(String componentName) {
        return componentName + "_icon32.png";
    }

    public static String getIcon24FileName(String componentName) {
        return componentName + "_icon24.png";
    }

    public static String getIcon16FileName(String componentName) {
        return componentName + "_icon16.png";
    }
}
