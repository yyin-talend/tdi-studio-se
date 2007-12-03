// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.views.CodeView;

/**
 * Command that changes a given property. It will call the set or get property value in an element. This element can be
 * either a node, a connection or a process. <br/>
 * 
 * $Id$
 * 
 */
public class PropertyChangeCommand extends Command {

    private final Element elem;

    private final String propName;

    private Object newValue;

    private Object oldValue;

    private boolean repositoryValueWasUsed;

    private boolean toUpdate;

    private final Map<IElementParameter, Object> oldElementValues;

    private ChangeMetadataCommand changeMetadataCommand;

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
        setLabel(Messages.getString("PropertyChangeCommand.Label")); //$NON-NLS-1$
    }

    private void refreshPropertyView() {
        // if (!elem.getElementParameter(propName).getCategory().equals(EComponentCategory.STATSANDLOGS)) {
        // IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        // IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        // PropertySheet sheet = (PropertySheet) view;
        // if (sheet != null && sheet.getCurrentPage() != null && sheet.getCurrentPage() instanceof
        // TabbedPropertySheetPage) {
        // TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        // if (tabbedPropertySheetPage.getCurrentTab() != null) {
        // tabbedPropertySheetPage.refresh();
        // }
        // }
        // }
    }

    private void refreshCodeView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView(CodeView.ID);
        if (view != null) {
            CodeView codeView = (CodeView) view;
            codeView.refresh();
        }
    }

    @Override
    public void execute() {
        IElementParameter currentParam = elem.getElementParameter(propName);
        oldElementValues.clear();

        if (currentParam == null) {
            return;
        }

        if (currentParam.isRepositoryValueUsed()) {
            if (currentParam.getField() == EParameterFieldType.MEMO_SQL) {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
                currentParam.setRepositoryValueUsed(false);
            } else {
                elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                for (IElementParameter param : elem.getElementParameters()) {
                    param.setRepositoryValueUsed(false);
                }
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
            for (int i = 0; i < elem.getElementParameters().size(); i++) {
                IElementParameter testedParam = elem.getElementParameters().get(i);

                String showIf = testedParam.getShowIf();
                String notShowIf = testedParam.getNotShowIf();

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
                    if (testedParam.getListItemsShowIf() != null) {
                        for (int j = 0; j < testedParam.getListItemsShowIf().length && !toUpdate; j++) {
                            showIf = testedParam.getListItemsShowIf()[j];
                            notShowIf = testedParam.getListItemsNotShowIf()[j];
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

                setDefaultValues(currentParam, testedParam);
            }
        }
        if (toUpdate) {
            elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));
        }
        refreshPropertyView();
        refreshCodeView();

        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }
    }

    /**
     * Set the values to default if needed.
     * 
     * @param currentParam Current parameter that has been modified in the interface
     * @param testedParam Tested parameter, to know if there is a link for the default values between this parameter and
     * the current.
     */
    private void setDefaultValues(IElementParameter currentParam, IElementParameter testedParam) {
        boolean contains = false;
        for (IElementParameterDefaultValue value : testedParam.getDefaultValues()) {
            if (value.getIfCondition() != null) {
                if (value.getIfCondition().contains(currentParam.getName())) {
                    contains = true;
                    break;
                }
            }
            if (value.getNotIfCondition() != null) {
                if (value.getNotIfCondition().contains(currentParam.getName())) {
                    contains = true;
                    break;
                }
            }
        }

        if (testedParam.getDefaultValues().size() > 0 && contains) {
            oldElementValues.put(testedParam, testedParam.getValue());

            // if the field is not a schema type, then use standard "set value".
            if (!testedParam.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                String oldMapping = "";
                if (!testedParam.getField().equals(EParameterFieldType.CHECK)) {
                    oldMapping = (String) testedParam.getValue();
                }
                testedParam.setValueToDefault(elem.getElementParameters());
                if (testedParam.getField().equals(EParameterFieldType.MAPPING_TYPE)) {
                    String newMapping = (String) testedParam.getValue();
                    if (!oldMapping.equals(newMapping)) {
                        Node node = (Node) elem;
                        if (node.getMetadataList().size() > 0) {
                            // to change with:
                            // IMetadataTable metadataTable = node.getMetadataFromConnector(testedParam.getContext());
                            IMetadataTable metadataTable = node.getMetadataList().get(0);
                            metadataTable.setDbms(newMapping);
                        }
                    }
                }
            } else {
                // See issue 975, update the schema.
                Node node = (Node) elem;
                if (node.getMetadataList().size() > 0) {
                    IMetadataTable metadataTable = node.getMetadataFromConnector(testedParam.getContext());
                    testedParam.setValueToDefault(node.getElementParameters());
                    IMetadataTable newMetadataTable = (IMetadataTable) testedParam.getValue();
                    newMetadataTable.setTableName(metadataTable.getTableName());
                    newMetadataTable.setAttachedConnector(metadataTable.getAttachedConnector());

                    metadataTable.setListColumns(newMetadataTable.clone(true).getListColumns());

                    changeMetadataCommand = new ChangeMetadataCommand(node, null, metadataTable, newMetadataTable);
                    changeMetadataCommand.execute(true);
                }
            }
        }
    }

    @Override
    public void undo() {
        if (repositoryValueWasUsed) {
            IElementParameter currentParam = elem.getElementParameter(propName);
            if (currentParam.getField() == EParameterFieldType.MEMO_SQL) {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.REPOSITORY);
            } else {
                elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.REPOSITORY);
            }
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
        if (changeMetadataCommand != null) {
            changeMetadataCommand.undo();
        }
        refreshPropertyView();
        refreshCodeView();
        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }
    }

    @Override
    public void redo() {
        IElementParameter currentParam = elem.getElementParameter(propName);
        if (repositoryValueWasUsed) {
            if (currentParam.getField() == EParameterFieldType.MEMO_SQL) {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
            } else {
                elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
            }

            for (IElementParameter param : elem.getElementParameters()) {
                param.setRepositoryValueUsed(false);
            }
        }

        elem.setPropertyValue(propName, newValue);

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

        if (changeMetadataCommand != null) {
            changeMetadataCommand.redo();
        }
        refreshPropertyView();
        refreshCodeView();
        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }
    }

    public String getPropName() {
        return this.propName;
    }

    public Element getElement() {
        return this.elem;
    }

    public Object getOldValue() {
        return this.oldValue;
    }

    public Object getNewValue() {
        return this.newValue;
    }

}
