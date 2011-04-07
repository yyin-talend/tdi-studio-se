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
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.HashConfiguration;
import org.talend.core.model.process.HashableColumn;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IHashConfiguration;
import org.talend.core.model.process.IHashableColumn;
import org.talend.core.model.process.IHashableInputConnections;
import org.talend.core.model.process.ILookupMode;
import org.talend.core.model.process.IMatchingMode;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.components.lookup.common.ICommonLookup.MATCHING_MODE;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapComponent extends AbstractExternalNode implements IHashableInputConnections{

    private AbstractExternalData emfMapData;

    private MapperMain mapprMain;

    public XmlMapComponent() {
    }

    public int open(Display display) {

        // TimeMeasure.start("Total open");
        // TimeMeasure.display = false;

        Shell parentShell = display.getActiveShell();
        mapprMain = new MapperMain(this);

        CursorHelper.changeCursor(parentShell, SWT.CURSOR_WAIT);

        Shell shell = null;
        try {
            shell = mapprMain.createUI(display);

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

        return mapprMain.getMapperDialogResponse();
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

}
