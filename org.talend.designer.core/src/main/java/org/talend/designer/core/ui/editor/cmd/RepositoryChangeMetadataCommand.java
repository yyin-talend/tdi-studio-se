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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.controllers.ColumnListController;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class RepositoryChangeMetadataCommand extends ChangeMetadataCommand {

    private final String propName;

    private final Object oldPropValue, newPropValue;

    private final Node node;

    public RepositoryChangeMetadataCommand(Node node, String propName, Object propValue,
            IMetadataTable newOutputMetadata) {
        super(node, node.getElementParameter(propName).getParentParameter(), null, newOutputMetadata);
        this.propName = propName;
        oldPropValue = node.getPropertyValue(propName);
        newPropValue = propValue;
        this.node = node;
        this.setRepositoryMode(true);
    }

    @Override
    public void execute() {
        node.setPropertyValue(propName, newPropValue);
        // boolean repositoryModel = true;
        // String propertyTypeModel = (String) node.getElementParameter(
        // EParameterName.PROPERTY_TYPE.getName()).getValue();
        //
        // if (EmfComponent.BUILTIN.equals(newPropValue)
        // && EmfComponent.BUILTIN.equals(propertyTypeModel)) {
        // repositoryModel = false;
        // }
        // for (IElementParameter p : node.getElementParameters()) {
        // p.setRepositoryValueUsed(repositoryModel);
        // }

        if (node.isExternalNode() && !node.isELTComponent()) {
            for (IElementParameter parameter : node.getElementParameters()) {
                if (parameter.getField() == EParameterFieldType.TABLE) {
                    if (!node.getMetadataList().isEmpty()
                            && !node.getMetadataList().get(0).sameMetadataAs(newOutputMetadata)) {
                        parameter.setValue(new ArrayList<Map<String, Object>>());
                    }
                }
            }
        }
        IElementParameter schemaTypeParameter = node.getElementParameter(propName).getParentParameter()
                .getChildParameters().get(EParameterName.SCHEMA_TYPE.getName());
        IElementParameter repositorySchemaTypeParameter = node.getElementParameter(propName).getParentParameter()
                .getChildParameters().get(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        String schemaType = (String) schemaTypeParameter.getValue();
        if (schemaType != null && schemaType.equals(EmfComponent.REPOSITORY)) {
            repositorySchemaTypeParameter.setShow(true);
        } else {
            repositorySchemaTypeParameter.setShow(false);
        }

        node.getElementParameter(EParameterName.UPDATE_COMPONENTS.getName()).setValue(true);
        setDBTableFieldValue(node, newOutputMetadata.getLabel(), oldOutputMetadata.getTableName());
        refreshPropertyView();
        super.execute();
    }

    @Override
    public void undo() {
        node.setPropertyValue(propName, oldPropValue);
        refreshPropertyView();
        super.undo();
    }

    @Override
    protected void updateColumnList(IMetadataTable oldTable, IMetadataTable newTable) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        if (tabbedPropertySheetPage.getCurrentTab() == null) {
            return;
        }
        ISection[] sections = tabbedPropertySheetPage.getCurrentTab().getSections();
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] instanceof DynamicTabbedPropertySection) {
                DynamicTabbedPropertySection currentSection = (DynamicTabbedPropertySection) sections[i];
                if (currentSection.getElement().equals(node)) {
                    List<ColumnNameChanged> columnNameChanged = new ArrayList<ColumnNameChanged>();
                    for (int j = 0; j < oldTable.getListColumns().size(); j++) {
                        if (newTable.getListColumns().size() > j) {
                            String oldName = oldTable.getListColumns().get(j).getLabel();
                            String newName = newTable.getListColumns().get(j).getLabel();
                            if (!oldName.equals(newName)) {
                                columnNameChanged.add(new ColumnNameChanged(oldName, newName));
                            }
                        }
                    }
                    ColumnListController.updateColumnList(node, null);
                    currentSection.refresh();
                }
            }
        }
    }

}
