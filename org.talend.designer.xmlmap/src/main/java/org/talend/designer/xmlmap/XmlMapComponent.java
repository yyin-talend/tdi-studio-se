// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.swt.cursor.CursorHelper;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IExternalData;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapComponent extends AbstractExternalNode {

    private XmlMapData emfMapData;

    private MapperMain mapprMain;

    private List<IMetadataTable> metadataListOut;

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
        // TODO Auto-generated method stub

    }

    public void renameOutputConnection(String oldName, String newName) {
        // TODO Auto-generated method stub

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

    }

    // @Override
    // public IExternalData getExternalData() {
    // // ExternalData is no use for xmlmap , but need to return a no-null value
    // return new IExternalData() {
    //
    // public List<String> getJoinedTableNames(String mainTable) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // public Map<IExternalMapTable, List<IExternalMapEntry>> getExpressionColumns(String expression,
    // ExternalDataType... types) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // public IExternalData clone() throws CloneNotSupportedException {
    // return null;
    // }
    // };
    // }

    @Override
    public void buildExternalData(AbstractExternalData abstractData) {
        if (abstractData instanceof XmlMapData) {
            this.emfMapData = (XmlMapData) abstractData;
        }
    }

    @Override
    public AbstractExternalData getExternalEmfData() {
        if (this.emfMapData == null) {
            this.emfMapData = XmlmapFactory.eINSTANCE.createXmlMapData();
        }
        return this.emfMapData;
    }

    public void setEmfMapData(XmlMapData emfMapData) {
        this.emfMapData = emfMapData;
    }
}
