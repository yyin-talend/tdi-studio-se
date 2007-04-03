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
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class RepositoryChangeMetadataCommand extends ChangeMetadataCommand {

    private String propName;

    private Object oldPropValue, newPropValue;

    private Node node;

    private Object oldQuery;

    private IMetadataTable newOutputMetadata;

    private Map<String, String> dbNameAndDbTypeMap;

    private Map<String, String> dbNameAndSchemaMap;

    private Map<String, IMetadataTable> repositoryTableMap;

    public RepositoryChangeMetadataCommand(Node node, String propName, Object propValue,
            IMetadataTable newOutputMetadata) {
        super(node, null, newOutputMetadata);
        this.newOutputMetadata = newOutputMetadata;
        this.propName = propName;
        oldPropValue = node.getPropertyValue(propName);
        newPropValue = propValue;
        this.node = node;
        this.setRepositoryMode(true);
    }

    @Override
    public void execute() {
        node.setPropertyValue(propName, newPropValue);
        refreshPropertyView();

        // used for generating new Query.
        String schemaSelected = (String) node.getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        IMetadataTable repositoryMetadata = null;
        if (repositoryTableMap != null && repositoryTableMap.containsKey(schemaSelected)) {
            repositoryMetadata = repositoryTableMap.get(schemaSelected);
        } else {
            repositoryMetadata = new MetadataTable();
        }

        String dbId = repositoryMetadata.getId();
        String dbType = "";
        if (dbId != null && this.dbNameAndDbTypeMap.containsKey(dbId)) {
            dbType = this.dbNameAndDbTypeMap.get(dbId);
        }

        String schema = this.dbNameAndSchemaMap.get(dbId);

        String newQuery = "";
        boolean isBuildIn = EmfComponent.BUILTIN.equals(newPropValue);
        if (isBuildIn) {
            newQuery = TalendTextUtils.addQuotes("");
        } else {
            newQuery = TalendTextUtils.addQuotes(generateNewQuery(repositoryMetadata, dbType, schema));
        }
        boolean isChangeQueryType = false;
        for (IElementParameter param : (List<IElementParameter>) node.getElementParameters()) {
            if (param.getField() == EParameterFieldType.MEMO_SQL) {
                oldQuery = node.getPropertyValue(param.getName());
                node.setPropertyValue(param.getName(), newQuery);
                param.setRepositoryValueUsed(true);
                isChangeQueryType = true;
            }
        }
        if (isChangeQueryType) {
            node.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
        }
        refreshPropertyView();
        // Ends
        super.execute();
    }

    @Override
    public void undo() {
        node.setPropertyValue(propName, oldPropValue);

        // Added by ftang
        for (IElementParameter param : (List<IElementParameter>) node.getElementParameters()) {
            if (param.getField() == EParameterFieldType.MEMO_SQL) {
                node.setPropertyValue(param.getName(), oldQuery);
            }
        }// Ends

        refreshPropertyView();
        super.undo();
    }

    protected void updateColumnList(IMetadataTable oldTable, IMetadataTable newTable) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
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
                    currentSection.updateColumnList(columnNameChanged);
                }
            }
        }
    }

    /**
     * Sets a set of maps what used for generating new Query.
     * 
     * @param dbNameAndDbTypeMap
     * @param dbNameAndSchemaMap
     * @param getRepositoryTableMap
     */
    public void setMaps(Map<String, String> dbNameAndDbTypeMap, Map<String, String> dbNameAndSchemaMap,
            Map<String, IMetadataTable> repositoryTableMap) {
        this.dbNameAndDbTypeMap = dbNameAndDbTypeMap;
        this.dbNameAndSchemaMap = dbNameAndSchemaMap;
        this.repositoryTableMap = repositoryTableMap;

    }
}
