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
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ChangeValuesFromRepository extends Command {

    private Map<String, Object> oldValues;

    private Element elem;

    private Connection connection;

    private String value;

    private String propertyName;

    private String oldMetadata;

    private Map<String, List<String>> tablesmap;

    private Map<String, List<String>> queriesmap;

    public ChangeValuesFromRepository(Element elem, Connection connection, String propertyName, String value) {
        this.elem = elem;
        this.connection = connection;
        this.value = value;
        this.propertyName = propertyName;
        oldValues = new HashMap<String, Object>();

        setLabel(Messages.getString("PropertyChangeCommand.Label")); //$NON-NLS-1$
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
    }

    @Override
    public void execute() {
        // Force redraw of Commponents propoerties
        elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));

        if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName()) && (EmfComponent.BUILTIN.equals(value))) {
            for (IElementParameter param : elem.getElementParameters()) {
                param.setRepositoryValueUsed(false);
            }
        } else {
            oldValues.clear();
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)
                        && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                    Object objectValue = (Object) RepositoryToComponentProperty.getValue(connection, repositoryValue);
                    if (objectValue != null) {
                        oldValues.put(param.getName(), param.getValue());

                        if (param.getField().equals(EParameterFieldType.CLOSED_LIST)
                                && param.getRepositoryValue().equals("TYPE")) {
                            boolean found = false;
                            String[] list = param.getListRepositoryItems();
                            for (int i = 0; (i < list.length) && (!found); i++) {
                                if (objectValue.equals(list[i])) {
                                    found = true;
                                    elem.setPropertyValue(param.getName(), param.getListItemsValue()[i]);
                                }
                            }
                        } else {
                            elem.setPropertyValue(param.getName(), objectValue);
                        }
                        param.setRepositoryValueUsed(true);
                    } else {
                        if (param.getField().equals(EParameterFieldType.TABLE)
                                && param.getRepositoryValue().equals("XML_MAPPING")) { //$NON-NLS-1$

                            List<Map<String, Object>> table = (List<Map<String, Object>>) elem.getPropertyValue(param
                                    .getName());
                            IMetadataTable metaTable = ((Node) elem).getMetadataList().get(0);
                            RepositoryToComponentProperty.getTableXmlFileValue(connection, "XML_MAPPING", param, //$NON-NLS-1$
                                    table, metaTable);
                            param.setRepositoryValueUsed(true);
                        }
                    }
                }
            }
        }
        if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName())) {
            elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), value);
            setOtherProperties();
        } else {
            oldMetadata = (String) elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
            elem.setPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), value);
            if (elem instanceof Node) {
                Node node = (Node) elem;
                boolean metadataInput = false;
                if (node.getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) > 0
                        || node.getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) > 0
                        || node.getCurrentActiveLinksNbInput(EConnectionType.TABLE) > 0) {
                    metadataInput = true;
                }
                if (!metadataInput) {
                    elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.REPOSITORY);
                }
            }
            elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.REPOSITORY);
            if (queriesmap == null || queriesmap.get(value).isEmpty()) {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
            }
            if (tablesmap == null || tablesmap.get(value).isEmpty()) {
                elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
            }

        }
        refreshPropertyView();
    }

    /**
     * qzhang Comment method "setOtherProperties".
     */
    private void setOtherProperties() {
        boolean metadataInput = false;
        if (elem instanceof Node) {
            Node node = (Node) elem;
            if (node.getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) > 0
                    || node.getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) > 0
                    || node.getCurrentActiveLinksNbInput(EConnectionType.TABLE) > 0) {
                metadataInput = true;
            }
        }
        if (value.equals(EmfComponent.BUILTIN)) {
            if (!metadataInput) {
                elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), value);
            }
            elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), value);
        } else {
            if (!metadataInput) {
                if (tablesmap != null
                        && !tablesmap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()))
                                .isEmpty()) {
                    elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), value);
                }
            }
            if (queriesmap != null
                    && !queriesmap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()))
                            .isEmpty()) {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), value);
            }
        }
    }

    @Override
    public void undo() {
        // Force redraw of Commponents propoerties
        elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));

        if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName()) && (EmfComponent.BUILTIN.equals(value))) {
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)
                        && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                    param.setRepositoryValueUsed(true);
                }
            }
        } else {
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)) {
                    Object objectValue = (Object) RepositoryToComponentProperty.getValue(connection, repositoryValue);
                    if (objectValue != null) {
                        elem.setPropertyValue(param.getName(), oldValues.get(param.getName()));
                        param.setRepositoryValueUsed(false);
                    }
                }
            }
        }
        if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName())) {
            if (value.equals(EmfComponent.BUILTIN)) {
                elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.REPOSITORY);
            } else {
                elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
            }
        } else {
            elem.setPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), oldMetadata);
        }
        refreshPropertyView();
    }

    public void setMaps(Map<String, List<String>> tablesmap, Map<String, List<String>> queriesmap) {
        this.tablesmap = tablesmap;
        this.queriesmap = queriesmap;
    }
}
