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
package org.talend.designer.core.ui.editor.cmd;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that changes a given property. It will call the set or get property value in an element. This element can be
 * either a node, a connection or a process. <br/>
 * 
 * $Id$
 * 
 */
public class PropertyChangeCommand extends Command {

    private Element elem;

    private String propName;

    private Object newValue;

    private Object oldValue;

    private boolean repositoryValueWasUsed;

    private boolean toUpdate;

    private Map<IElementParameter, Object> oldElementValues;

    /**
     * The property is defined in an element, which can be either a node or a connection.
     * 
     * @param elem
     * @param propName
     * @param propValue
     */
    public PropertyChangeCommand(Element elem, String propName, Object propValue) {
        this.elem = elem;
        this.propName = propName;
        newValue = propValue;
        toUpdate = false;
        oldElementValues = new HashMap<IElementParameter, Object>();
        setLabel(Messages.getString("PropertyChangeCommand.0")); //$NON-NLS-1$
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet");
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
    }

    @Override
    public void execute() {
        IElementParameter currentParam = elem.getElementParameter(propName);
        oldElementValues.clear();

        if (currentParam.isRepositoryValueUsed()) {
            elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);

            for (IElementParameter param : elem.getElementParameters()) {
                param.setRepositoryValueUsed(false);
            }
            repositoryValueWasUsed = true;
        } else {
            repositoryValueWasUsed = false;
        }

        oldValue = elem.getPropertyValue(propName);
        elem.setPropertyValue(propName, newValue);

        if (currentParam.getField().equals(EParameterFieldType.CLOSED_LIST)
                || currentParam.getField().equals(EParameterFieldType.CHECK)) {
            toUpdate = false;
            for (int i = 0; i < elem.getElementParameters().size() && !toUpdate; i++) {
                IElementParameter param = elem.getElementParameters().get(i);

                String showIf = param.getShowIf();
                String notShowIf = param.getNotShowIf();
                if (showIf != null) {
                    if (showIf.contains(currentParam.getName())) {
                        toUpdate = true;
                    }
                } else {
                    if (notShowIf != null) {
                        if (notShowIf.contains(currentParam.getName())) {
                            toUpdate = true;
                        }
                    }
                }
                if (currentParam.getField().equals(EParameterFieldType.CLOSED_LIST)) {
                    if (param.getListItemsShowIf() != null) {
                        for (int j = 0; j < param.getListItemsShowIf().length && !toUpdate; j++) {
                            showIf = param.getListItemsShowIf()[j];
                            notShowIf = param.getListItemsNotShowIf()[j];
                            if (showIf != null) {
                                if (showIf.contains(currentParam.getName())) {
                                    toUpdate = true;
                                }
                            } else {
                                if (notShowIf != null) {
                                    if (notShowIf.contains(currentParam.getName())) {
                                        toUpdate = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (param.getDefaultValues().size() > 0) {
                    oldElementValues.put(param, param.getValue());
                    param.setValueToDefault(elem.getElementParameters());
                }
            }
        }
        if (toUpdate) {
            elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));
        }
        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }

        refreshPropertyView();
    }

    @Override
    public void undo() {
        if (repositoryValueWasUsed) {
            elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.REPOSITORY);
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)) {
                    param.setRepositoryValueUsed(true);
                }
            }
        }
        elem.setPropertyValue(propName, oldValue);

        for (IElementParameter param : oldElementValues.keySet()) {
            param.setValue(oldElementValues.get(param));
        }

        if (toUpdate) {
            elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));
        }
        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }
        refreshPropertyView();
    }

    @Override
    public void redo() {
        if (repositoryValueWasUsed) {
            elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);

            for (IElementParameter param : elem.getElementParameters()) {
                param.setRepositoryValueUsed(false);
            }
        }

        elem.setPropertyValue(propName, newValue);

        IElementParameter currentParam = elem.getElementParameter(propName);
        if (currentParam.getField().equals(EParameterFieldType.CLOSED_LIST)) {
            for (int i = 0; i < elem.getElementParameters().size(); i++) {
                IElementParameter param = elem.getElementParameters().get(i);
                if (param.getDefaultValues().size() > 0) {
                    param.setValueToDefault(elem.getElementParameters());
                }
            }
        }

        if (toUpdate) {
            elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));
        }
        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }
        refreshPropertyView();
    }

    public void modifyValue(String value) {
        newValue = value;
        elem.setPropertyValue(propName, value);
    }

    public String getPropName() {
        return this.propName;
    }

}
