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
package org.talend.designer.core.model.process.statsandlogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.temp.ECodePart;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.components.MultipleComponentConnection;
import org.talend.designer.core.model.components.MultipleComponentManager;

/**
 * These components won't be used in the designer part, only for the generation. <br/>
 * 
 */
public abstract class AbstractStatsLogsComponent implements IComponent {

    protected boolean useDb = false;

    protected String dbComponent;

    protected boolean useFile = false;

    protected String componentId;

    protected String subComponent;
    
    protected IMultipleComponentManager multipleComponentManager;

    // no use for virtual component
    public List<? extends INodeConnector> createConnectors() {
        return null;
    }

    // no use for virtual component
    public List<? extends INodeReturn> createReturns() {
        return null;
    }

    // no use for virtual component
    public List<ECodePart> getAvailableCodeParts() {
        return null;
    }

    // no use for virtual component
    public String getFamily() {
        return "Virtual";
    }

    // no use for virtual component
    public ImageDescriptor getIcon16() {
        return null;
    }

    // no use for virtual component
    public ImageDescriptor getIcon24() {
        return null;
    }

    // no use for virtual component
    public ImageDescriptor getIcon32() {
        return null;
    }

    // no use for virtual component
    public String getLongName() {
        return null;
    }

    // no use for virtual component ?
    public List<ModuleNeeded> getModulesNeeded() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getName()
     */
    public String getName() {
        return this.getClass().getName();
    }

    // no use for virtual component
    public String getPathSource() {
        return null;
    }

    // no use for virtual component
    public List<String> getPluginDependencies() {
        return null;
    }

    // no use for virtual component
    public String getPluginFullName() {
        return null;
    }

    // no use for virtual component
    public String getTranslatedName() {
        return null;
    }

    // no use for virtual component
    public Boolean hasConditionalOutputs() {
        return null;
    }

    // no use for virtual component
    public boolean isDataAutoPropagated() {
        return false;
    }

    // no use for virtual component
    public boolean isLoaded() {
        return true;
    }

    // no use for virtual component
    public boolean isSchemaAutoPropagated() {
        return false;
    }

    // no use for virtual component
    public boolean isVisible() {
        return false;
    }

    // no use for virtual component
    public void setIcon16(ImageDescriptor icon16) {
    }

    // no use for virtual component
    public void setIcon24(ImageDescriptor icon24) {
    }

    // no use for virtual component
    public void setIcon32(ImageDescriptor icon32) {
    }

    // no use for virtual component
    public boolean useMerge() {
        return false;
    }

    public List<? extends IElementParameter> createElementParameters(INode node) {
        List<IElementParameter> elemParamList = new ArrayList<IElementParameter>();

        addFileOutputParameters(elemParamList, node);
        addDbParameters(elemParamList, node);
        return elemParamList;
    }

    protected void addDbParameters(List<IElementParameter> elemParamList, INode node) {
        // parameters for db output.
        IElementParameter newParam = new ElementParameter(node);
        newParam.setName("HOST");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("SERVER");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);
        
        newParam = new ElementParameter(node);
        newParam.setName("DSN");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("PORT");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("DBNAME");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("SCHEMA_DB");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("USER");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("PASS");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("TABLE");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("TABLE_ACTION");
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue("CREATE_IF_NOT_EXISTS");
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("DATA_ACTION");
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue("INSERT");
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("COMMIT_EVERY");
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue("100");
        elemParamList.add(newParam);
    }

    protected void addFileOutputParameters(List<IElementParameter> elemParamList, INode node) {
        // parameters for file output.
        IElementParameter newParam = new ElementParameter(node);
        newParam.setName("FILENAME");
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("ROWSEPARATOR");
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(TalendTextUtils.addQuotes("\\n", TalendTextUtils.QUOTATION_MARK));
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("FIELDSEPARATOR");
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(TalendTextUtils.addQuotes(";"));
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("APPEND");
        newParam.setField(EParameterFieldType.CHECK);
        newParam.setValue(Boolean.TRUE);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("INCLUDEHEADER");
        newParam.setField(EParameterFieldType.CHECK);
        newParam.setValue(Boolean.FALSE);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("ENCODING");
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(TalendTextUtils.addQuotes("ISO-8859-15"));
        elemParamList.add(newParam);
    }

    protected void loadMultipleComponentManager() {
        String lastComponent;
        if (useDb) {
            lastComponent = "DB";
        } else {
            lastComponent = "FILE";
        }
        // create base items
        multipleComponentManager = new MultipleComponentManager(componentId, lastComponent);
        IMultipleComponentItem currentItem = multipleComponentManager.addItem(componentId, subComponent);
        if (useFile) {
            currentItem.getOutputConnections().add(new MultipleComponentConnection("FLOW", "FILE"));
            currentItem = multipleComponentManager.addItem("FILE", "tFileOutputDelimited");
        }
        if (useDb) {
            currentItem.getOutputConnections().add(new MultipleComponentConnection("FLOW", "DB"));
            currentItem = multipleComponentManager.addItem("DB", dbComponent);
        }

        createMultipleComponentsParameters();
        multipleComponentManager.validateItems();
    }

    protected void createMultipleComponentsParameters() {
        // create parameters
        if (useFile) {
            multipleComponentManager.addParam("self.FILENAME", "FILE.FILENAME");
            multipleComponentManager.addParam("self.ROWSEPARATOR", "FILE.ROWSEPARATOR");
            multipleComponentManager.addParam("self.FIELDSEPARATOR", "FILE.FIELDSEPARATOR");
            multipleComponentManager.addParam("self.APPEND", "FILE.APPEND");
            multipleComponentManager.addParam("self.INCLUDEHEADER", "FILE.INCLUDEHEADER");
            multipleComponentManager.addParam("self.ENCODING", "FILE.ENCODING");
        }

        if (useDb) {
            multipleComponentManager.addParam("self.HOST", "DB.HOST");
            multipleComponentManager.addParam("self.HOST", "DB.SERVER");
            multipleComponentManager.addParam("self.HOST", "DB.DSN");
            multipleComponentManager.addParam("self.PORT", "DB.PORT");
            multipleComponentManager.addParam("self.DBNAME", "DB.DBNAME");
            multipleComponentManager.addParam("self.USER", "DB.USER");
            multipleComponentManager.addParam("self.PASS", "DB.PASS");
            multipleComponentManager.addParam("self.TABLE", "DB.TABLE");
            multipleComponentManager.addParam("self.TABLE_ACTION", "DB.TABLE_ACTION");
            multipleComponentManager.addParam("self.DATA_ACTION", "DB.DATA_ACTION");
            multipleComponentManager.addParam("self.COMMIT_EVERY", "DB.COMMIT_EVERY");
            multipleComponentManager.addParam("self.SCHEMA_DB", "DB.SCHEMA_DB");
        }
    }

    /* (non-Javadoc)
     * @see org.talend.core.model.components.IComponent#getMultipleComponentManager()
     */
    public IMultipleComponentManager getMultipleComponentManager() {
        return multipleComponentManager;
    }

}
