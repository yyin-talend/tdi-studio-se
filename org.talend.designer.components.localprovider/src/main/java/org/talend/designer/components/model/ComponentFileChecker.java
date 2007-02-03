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
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.xml.XSDValidator;
import org.talend.core.model.ModelPlugin;
import org.xml.sax.SAXException;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ComponentFileChecker {

    private static final Bundle XSD_CONTAINER_BUNDLE = Platform.getBundle(ModelPlugin.MODEL_PLUGIN_ID);

    private static final String XSD_PATH = "model/Component.xsd"; //$NON-NLS-1$

    public static void checkComponentFolder(File currentFolder, String languageSuffix) throws BusinessException {
        checkFiles(currentFolder, languageSuffix);
        File xmlMainFile = new File(currentFolder, ComponentFilesNaming.getInstance().getMainXMLFileName(currentFolder.getName(),
                languageSuffix));
        checkXSD(xmlMainFile);
    }

    private static void checkXSD(File file) throws BusinessException {
        Path path = new Path(XSD_PATH);
        try {
            URL url = FileLocator.toFileURL(FileLocator.find(XSD_CONTAINER_BUNDLE, path, null));
            File schema = new File(url.getPath());
            XSDValidator.checkXSD(file, schema);
        } catch (IOException e) {
            throw new BusinessException("Cannot find xsd (" + path.lastSegment() + ")", e); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (SAXException e) {
            throw new BusinessException("Does not match xsd (" + path.lastSegment() + ")", e); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (ParserConfigurationException e) {
            throw new BusinessException("Cannot check xsd (" + path.lastSegment() + ")", e); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static void checkFiles(File folder, String languageSuffix) throws BusinessException {
        for (String currentFileToCheck : ComponentFilesNaming.getInstance().getRequiredFilesNames(folder.getName(),
                languageSuffix)) {
            File file = new File(folder, currentFileToCheck);
            if (!file.exists()) {
                throw new BusinessException("Cannot find file \"" + file.getName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }
}
