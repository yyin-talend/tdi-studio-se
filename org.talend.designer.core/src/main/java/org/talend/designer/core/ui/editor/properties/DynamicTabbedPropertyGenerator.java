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
package org.talend.designer.core.ui.editor.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IControllerGenerator;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: DynamicTabbedPropertyGenerator.java 1 2006-12-22 上午11:24:10 +0000 (上午11:24:10) yzhang $
 * 
 */
public class DynamicTabbedPropertyGenerator {

    private static DynamicTabbedPropertyGenerator instance = null;

    private boolean initialized = false;

    private Map<EParameterFieldType, AbstractElementPropertySectionController> dtpControls;

    public static DynamicTabbedPropertyGenerator getDefault() {
        if (instance == null) {
            instance = new DynamicTabbedPropertyGenerator();
        }
        return instance;
    }

    /**
     * DOC yzhang Comment method "initController".
     */
    public void initController(DynamicTabbedPropertySection dtp) {
        if (!initialized) {
            dtpControls = new HashMap<EParameterFieldType, AbstractElementPropertySectionController>();
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            IConfigurationElement[] extensionElements = registry
                    .getConfigurationElementsFor("org.talend.designer.core.generators"); //$NON-NLS-1$

            for (int i = 0; i < extensionElements.length; i++) {
                IConfigurationElement element = extensionElements[i];

                try {
                    String controllerName = element.getAttribute("mapping"); //$NON-NLS-1$
                    EParameterFieldType key = EParameterFieldType.getFieldTypeByName(controllerName);
                    if (!dtpControls.containsKey(key)) {
                        if (!controllerName.equals(key.toString())) {
                            throw new RuntimeException("Mapping attribute " + controllerName //$NON-NLS-1$
                                    + " not included in eumn EParameterFieldType"); //$NON-NLS-1$
                        }
                        IControllerGenerator generator = (IControllerGenerator) element
                                .createExecutableExtension("class"); //$NON-NLS-1$
                        generator.setDynamicTabbedPropertySection(dtp);
                        AbstractElementPropertySectionController controller = generator.generate();
                        dtpControls.put(key, controller);
                    }
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }

            }
            initialized = true;
        }
    }

    /**
     * DOC yzhang Comment method "getController".
     * 
     * @param controllerName
     * @param dtp
     * @return
     */
    public AbstractElementPropertySectionController getController(EParameterFieldType controllerName,
            DynamicTabbedPropertySection dtp) {

        AbstractElementPropertySectionController controller = null;
        // Map<EParameterFieldType, AbstractElementPropertySectionController> dtpControls = controllers.get(dtp);
        if (dtpControls.containsKey(controllerName)) {
            controller = dtpControls.get(controllerName);
            if (controller != null) {
                controller.init(dtp);
                return controller;
            } else {
                dtpControls.remove(controllerName);
            }
        }
        return controller;
    }

}
