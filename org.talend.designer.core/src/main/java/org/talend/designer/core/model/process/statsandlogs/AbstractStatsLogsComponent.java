// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
    
    protected boolean useConsole = false;

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
        return "Virtual"; //$NON-NLS-1$
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
    public Boolean isMultiplyingOutputs() {
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
        newParam.setName("HOST"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("SERVER"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("DSN"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("PORT"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("DBNAME"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("SCHEMA_DB"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("USER"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("PASS"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("TABLE"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("TABLE_ACTION"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue("CREATE_IF_NOT_EXISTS"); //$NON-NLS-1$
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("DATA_ACTION"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue("INSERT"); //$NON-NLS-1$
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("COMMIT_EVERY"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue("100"); //$NON-NLS-1$
        elemParamList.add(newParam);
    }

    protected void addFileOutputParameters(List<IElementParameter> elemParamList, INode node) {
        // parameters for file output.
        IElementParameter newParam = new ElementParameter(node);
        newParam.setName("FILENAME"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("ROWSEPARATOR"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(TalendTextUtils.addQuotes("\\n", TalendTextUtils.QUOTATION_MARK)); //$NON-NLS-1$
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("FIELDSEPARATOR"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(TalendTextUtils.addQuotes(";")); //$NON-NLS-1$
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("APPEND"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.CHECK);
        newParam.setValue(Boolean.TRUE);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("INCLUDEHEADER"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.CHECK);
        newParam.setValue(Boolean.FALSE);
        elemParamList.add(newParam);

        newParam = new ElementParameter(node);
        newParam.setName("ENCODING"); //$NON-NLS-1$
        newParam.setField(EParameterFieldType.TEXT);
        newParam.setValue(TalendTextUtils.addQuotes("ISO-8859-15")); //$NON-NLS-1$
        elemParamList.add(newParam);
    }

    protected void loadMultipleComponentManager() {
        String lastComponent= null;
        if (useConsole) {
            lastComponent = "CONSOLE"; //$NON-NLS-1$
        } else if(useDb) {
            lastComponent = "DB"; //$NON-NLS-1$
        }else{
            lastComponent = "FILE"; //$NON-NLS-1$
        }
        // create base items
        multipleComponentManager = new MultipleComponentManager(componentId, lastComponent);
        IMultipleComponentItem currentItem = multipleComponentManager.addItem(componentId, subComponent);
        if (useFile) {
            currentItem.getOutputConnections().add(new MultipleComponentConnection("FLOW", "FILE")); //$NON-NLS-1$ //$NON-NLS-2$
            currentItem = multipleComponentManager.addItem("FILE", "tFileOutputDelimited"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (useDb) {
            currentItem.getOutputConnections().add(new MultipleComponentConnection("FLOW", "DB")); //$NON-NLS-1$ //$NON-NLS-2$
            currentItem = multipleComponentManager.addItem("DB", dbComponent); //$NON-NLS-1$
        }
        if (useConsole) {
            currentItem.getOutputConnections().add(new MultipleComponentConnection("FLOW", "CONSOLE")); //$NON-NLS-1$ //$NON-NLS-2$
            currentItem = multipleComponentManager.addItem("CONSOLE", "tLogRow"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        createMultipleComponentsParameters();
        multipleComponentManager.validateItems();
    }

    protected void createMultipleComponentsParameters() {
        // create parameters
        if (useFile) {
            multipleComponentManager.addParam("self.FILENAME", "FILE.FILENAME"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.ROWSEPARATOR", "FILE.ROWSEPARATOR"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.FIELDSEPARATOR", "FILE.FIELDSEPARATOR"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.APPEND", "FILE.APPEND"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.INCLUDEHEADER", "FILE.INCLUDEHEADER"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.ENCODING", "FILE.ENCODING"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (useDb) {
            multipleComponentManager.addParam("self.HOST", "DB.HOST"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.HOST", "DB.SERVER"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.HOST", "DB.DSN"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.PORT", "DB.PORT"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.DBNAME", "DB.DBNAME"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.USER", "DB.USER"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.PASS", "DB.PASS"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.TABLE", "DB.TABLE"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.TABLE_ACTION", "DB.TABLE_ACTION"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.DATA_ACTION", "DB.DATA_ACTION"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.COMMIT_EVERY", "DB.COMMIT_EVERY"); //$NON-NLS-1$ //$NON-NLS-2$
            multipleComponentManager.addParam("self.SCHEMA_DB", "DB.SCHEMA_DB"); //$NON-NLS-1$ //$NON-NLS-2$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getMultipleComponentManager()
     */
    public IMultipleComponentManager getMultipleComponentManager() {
        return multipleComponentManager;
    }

}
