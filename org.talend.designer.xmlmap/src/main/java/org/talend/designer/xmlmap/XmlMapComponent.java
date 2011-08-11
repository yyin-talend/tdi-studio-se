// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.cursor.CursorHelper;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.BlockCode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.HashConfiguration;
import org.talend.core.model.process.HashableColumn;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IHashConfiguration;
import org.talend.core.model.process.IHashableColumn;
import org.talend.core.model.process.IHashableInputConnections;
import org.talend.core.model.process.ILookupMode;
import org.talend.core.model.process.IMatchingMode;
import org.talend.core.model.process.Problem;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.components.lookup.common.ICommonLookup.MATCHING_MODE;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;
import org.talend.designer.xmlmap.generation.GenerationManager;
import org.talend.designer.xmlmap.generation.GenerationManagerFactory;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.model.tree.XML_MAP_LOOKUP_MODE;
import org.talend.designer.xmlmap.util.MapDataHelper;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapComponent extends AbstractExternalNode implements IHashableInputConnections {

    private AbstractExternalData emfMapData;

    private MapperMain mapperMain;

    private GenerationManager generationManager;

    private MapDataHelper mapperHelper;

    public XmlMapComponent() {
        mapperHelper = new MapDataHelper();
    }

    public int open(Display display) {

        // TimeMeasure.start("Total open");
        // TimeMeasure.display = false;

        Shell parentShell = display.getActiveShell();
        mapperMain = new MapperMain(this);

        CursorHelper.changeCursor(parentShell, SWT.CURSOR_WAIT);

        Shell shell = null;
        try {
            shell = mapperMain.createUI(display);

        } finally {
            parentShell.setCursor(null);
        }
        while (shell != null && !shell.isDisposed()) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            } catch (Throwable e) {
                ExceptionHandler.process(e);
            }
        }

        return mapperMain.getMapperDialogResponse();
    }

    public void initialize() {
    }

    public int open(Composite parent) {
        return open(parent.getDisplay());
    }

    public void setExternalData(IExternalData persistentData) {
        // TODO Auto-generated method stub

    }

    public void renameInputConnection(String oldName, String newName) {
        XmlMapData externalEmfData = (XmlMapData) getExternalEmfData();
        for (InputXmlTree inputTree : externalEmfData.getInputTrees()) {
            if (inputTree.getName() != null && inputTree.getName().equals(oldName)) {
                inputTree.setName(newName);
                XmlMapUtil.updateXPathAndExpression(externalEmfData, inputTree.getNodes(), inputTree.getName(), 1);
            }
        }
    }

    public void renameOutputConnection(String oldName, String newName) {
        XmlMapData externalEmfData = (XmlMapData) getExternalEmfData();
        for (OutputXmlTree outputTree : externalEmfData.getOutputTrees()) {
            if (outputTree.getName() != null && outputTree.getName().equals(oldName)) {
                outputTree.setName(newName);
            }
        }
    }

    public IComponentDocumentation getComponentDocumentation(String componentName, String tempFolderPath) {
        // TODO Auto-generated method stub
        return null;
    }

    public IExternalData getTMapExternalData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void renameMetadataColumnName(String conectionName, String oldColumnName, String newColumnName) {
        // TODO Auto-generated method stub
        System.out.println();
    }

    @Override
    public void buildExternalData(AbstractExternalData abstractData) {
        if (abstractData instanceof XmlMapData) {
            this.emfMapData = (XmlMapData) abstractData;
        }
    }

    public AbstractExternalData getExternalEmfData() {
        if (this.emfMapData == null) {
            this.emfMapData = XmlmapFactory.eINSTANCE.createXmlMapData();
        }

        return this.emfMapData;
    }

    public void setExternalEmfData(AbstractExternalData emfMapData) {
        this.emfMapData = emfMapData;
    }

    public IHashConfiguration getHashConfiguration(String connectionName) {

        IHashConfiguration hashConfigurationForMapper = null;
        XmlMapData externalData = (XmlMapData) getExternalEmfData();
        List<InputXmlTree> inputTables = externalData.getInputTrees();
        List<IHashableColumn> hashableColumns = new ArrayList<IHashableColumn>();
        for (InputXmlTree inputTable : inputTables) {
            if (inputTable.getName().equals(connectionName)) {

                List<TreeNode> metadataTableEntries = inputTable.getNodes();
                if (metadataTableEntries != null) {
                    int metadataTableEntriesListSize = metadataTableEntries.size();
                    for (int i = 0; i < metadataTableEntriesListSize; i++) {
                        TreeNode entry = metadataTableEntries.get(i);
                        if (entry.getExpression() != null && !entry.getExpression().trim().equals("")) { //$NON-NLS-1$
                            hashableColumns.add(new HashableColumn(entry.getName(), i));
                        }
                    }
                }

                IMatchingMode matchingMode = MATCHING_MODE.parse(inputTable.getMatchingMode());
                if (matchingMode == null) {
                    matchingMode = MATCHING_MODE.UNIQUE_MATCH;
                }

                ILookupMode lookupMode = org.talend.designer.xmlmap.model.tree.LOOKUP_MODE.parse(inputTable.getLookupMode());
                if (lookupMode == null) {
                    lookupMode = org.talend.designer.xmlmap.model.tree.LOOKUP_MODE.LOAD_ONCE;
                }

                IElementParameter tempFolderElem = getElementParameter("TEMPORARY_DATA_DIRECTORY"); //$NON-NLS-1$
                String tempFolder = null;
                if (tempFolderElem != null) {
                    tempFolder = (String) tempFolderElem.getValue();
                }
                if (("").equals(tempFolder)) {
                    tempFolder = (String) this.getProcess().getElementParameter("COMP_DEFAULT_FILE_DIR").getValue() + "/temp"; //$NON-NLS-1$ //$NON-NLS-2$
                    tempFolder = TalendTextUtils.addQuotes(tempFolder);
                }

                IElementParameter rowsBufferSizeElem = getElementParameter("ROWS_BUFFER_SIZE"); //$NON-NLS-1$
                String rowsBufferSize = null;
                if (rowsBufferSizeElem != null) {
                    rowsBufferSize = (String) rowsBufferSizeElem.getValue();
                }
                hashConfigurationForMapper = new HashConfiguration(hashableColumns, matchingMode, inputTable.isPersistent(),
                        tempFolder, rowsBufferSize);
                break;
            }
        }

        return hashConfigurationForMapper;
    }

    public GenerationManager initGenerationManager() {
        this.generationManager = GenerationManagerFactory.getInstance().getGenerationManager();
        return this.generationManager;
    }

    public GenerationManager getGenerationManager() {
        return this.generationManager;
    }

    public List<BlockCode> getBlocksCodeToClose() {
        if (generationManager == null) {
            throw new IllegalStateException(); //$NON-NLS-1$
        }
        return this.generationManager.getBlocksCodeToClose();
    }

    public List<String> checkNeededRoutines(List<String> possibleRoutines, String additionalString) {
        List<String> routinesToAdd = new ArrayList<String>();
        XmlMapData xmlMapData = (XmlMapData) getExternalEmfData();
        for (String routine : possibleRoutines) {
            List<OutputXmlTree> listOutput = xmlMapData.getOutputTrees();
            for (OutputXmlTree outTable : listOutput) {
                List<OutputTreeNode> listOutEntry = (List<OutputTreeNode>) outTable.getNodes();
                if (listOutEntry != null && !listOutEntry.isEmpty()) {
                    for (OutputTreeNode outEntry : listOutEntry) {
                        String expression = outEntry.getExpression();
                        if (expression != null && !routinesToAdd.contains(routine)
                                && expression.contains(routine + additionalString)) {
                            routinesToAdd.add(routine);
                        }
                    }
                }
                String filter = outTable.getExpressionFilter();
                if (filter != null && !routinesToAdd.contains(routine) && filter.contains(routine + additionalString)) {
                    routinesToAdd.add(routine);
                }
            }
            List<InputXmlTree> listInput = (List<InputXmlTree>) xmlMapData.getInputTrees();
            for (InputXmlTree inputTable : listInput) {
                List<TreeNode> listInEntry = (List<TreeNode>) inputTable.getNodes();
                if (listInEntry != null && !listInEntry.isEmpty()) {
                    for (TreeNode inEntry : listInEntry) {
                        String expression = inEntry.getExpression();
                        if (expression != null && !routinesToAdd.contains(routine)
                                && expression.contains(routine + additionalString)) {
                            routinesToAdd.add(routine);
                        }
                    }
                }
                String filter = inputTable.getExpressionFilter();
                if (filter != null && !routinesToAdd.contains(routine) && filter.contains(routine + additionalString)) {
                    routinesToAdd.add(routine);
                }
            }
            List<VarTable> listVar = (List<VarTable>) xmlMapData.getVarTables();
            for (VarTable varTable : listVar) {
                List<VarNode> listVarEntry = (List<VarNode>) varTable.getNodes();
                if (listVarEntry != null && !listVarEntry.isEmpty()) {
                    for (VarNode varEntry : listVarEntry) {
                        String expression = varEntry.getExpression();
                        if (expression != null && !routinesToAdd.contains(routine)
                                && expression.contains(routine + additionalString)) {
                            routinesToAdd.add(routine);
                        }
                    }
                }
            }
        }
        return routinesToAdd;

    }

    public List<Problem> getProblems() {
        initMapperMain();
        return mapperMain.getMapperManager().getProblemsAnalyser().getProblems();
    }

    private void initMapperMain() {
        if (mapperMain == null) {
            mapperMain = new MapperMain(this);
        }
    }

    @Override
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        if (dataComponent.getTable() != null) {
            XmlMapData externalEmfData = (XmlMapData) getExternalEmfData();
            for (InputXmlTree inputTree : externalEmfData.getInputTrees()) {
                if (inputTree.getName() != null && inputTree.getName().equals(connectionToApply)) {
                    mapperHelper.rebuildInputTree(inputTree, dataComponent.getTable(), externalEmfData);
                }
            }
        }
    }

    @Override
    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
        List<IMetadataTable> list = new ArrayList<IMetadataTable>();
        if (dataComponent.getTable() != null) {
            list.add(dataComponent.getTable());
        }
        mapperHelper.rebuildModelOutputs(list, (XmlMapData) getExternalEmfData());
    }

    @Override
    public void connectionStatusChanged(EConnectionType newValue, String connectionToApply) {
        XmlMapData externalEmfData = (XmlMapData) getExternalEmfData();
        for (InputXmlTree inputTree : externalEmfData.getInputTrees()) {
            if (inputTree.getName() != null && inputTree.getName().equals(connectionToApply)) {
                inputTree.setLookup(EConnectionType.FLOW_MAIN != newValue);
            } else if (EConnectionType.FLOW_MAIN == newValue) {
                inputTree.setLookup(true);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.AbstractNode#removeInput(org.talend.core.model.process.IConnection)
     */
    @Override
    public void removeInput(IConnection connection) {
        XmlMapData externalEmfData = (XmlMapData) getExternalEmfData();
        InputXmlTree toRemove = null;
        for (InputXmlTree inputTree : externalEmfData.getInputTrees()) {
            if (inputTree.getName() != null && inputTree.getName().equals(connection.getUniqueName())) {
                toRemove = inputTree;
                break;
            }
        }
        if (toRemove != null) {
            for (TreeNode treeNode : toRemove.getNodes()) {
                XmlMapUtil.detachNodeConnections(treeNode, externalEmfData, true);
            }
            XmlMapUtil.detachFilterSource(toRemove, externalEmfData);
            externalEmfData.getInputTrees().remove(toRemove);
        }
    }

    @Override
    public boolean isRunRefSubProcessAtStart(String connectionName) {
        XmlMapData externalEmfData = (XmlMapData) getExternalEmfData();
        List<InputXmlTree> inputTrees = new ArrayList<InputXmlTree>(externalEmfData.getInputTrees());
        for (InputXmlTree table : inputTrees) {
            if (table.getName().equals(connectionName)) {
                String lookupMode = table.getLookupMode();
                if (XML_MAP_LOOKUP_MODE.RELOAD.name().equals(lookupMode)
                        || XML_MAP_LOOKUP_MODE.CACHE_OR_RELOAD.name().equals(lookupMode)) {
                    return false;
                }
            }
        }
        return true;
    }

}
