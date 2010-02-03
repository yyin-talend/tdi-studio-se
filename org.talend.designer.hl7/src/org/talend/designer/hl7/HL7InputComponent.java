// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.hl7;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class HL7InputComponent extends AbstractExternalNode {

    public static final String ROOT = "ROOT"; //$NON-NLS-1$

    public static final String GROUP = "GROUP"; //$NON-NLS-1$

    public static final String LOOP = "LOOP"; //$NON-NLS-1$

    public static final String PATH = "PATH"; //$NON-NLS-1$

    public static final String VALUE = "VALUE"; //$NON-NLS-1$

    public static final String TYPE = "TYPE"; //$NON-NLS-1$

    public static final String COLUMN = "COLUMN"; //$NON-NLS-1$

    public static final String ATTRIBUTE = "ATTRIBUTE"; //$NON-NLS-1$s

    public static final String ORDER = "ORDER"; //$NON-NLS-1$

    private HL7Main hl7main;

    @Override
    protected void renameMetadataColumnName(String conectionName, String oldColumnName, String newColumnName) {

    }

    @Override
    public IComponentDocumentation getComponentDocumentation(String componentName, String tempFolderPath) {
        return null;
    }

    @Override
    public IExternalData getExternalData() {
        return null;
    }

    @Override
    public IExternalData getTMapExternalData() {
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void loadDataIn(InputStream inputStream, Reader reader) throws IOException, ClassNotFoundException {

    }

    @Override
    public void loadDataOut(OutputStream out, Writer writer) throws IOException {

    }

    @Override
    public int open(Display display) {
        hl7main = new HL7Main(this);
        if (hl7main.getHl7Manager().isHasFile()) {
            Shell shell = hl7main.createUI(display);
            while (!shell.isDisposed()) {
                try {
                    if (!display.readAndDispatch()) {
                        display.sleep();
                    }
                } catch (Throwable e) {
                    if (hl7main.isStandAloneMode()) {
                        e.printStackTrace();
                    } else {
                        ExceptionHandler.process(e);
                    }
                }
            }
            if (hl7main.isStandAloneMode()) {
                display.dispose();
            }
        } else {
            MessageBox message = new MessageBox(new Shell(), SWT.APPLICATION_MODAL | SWT.OK);
            message.setText("The file is not exist"); //$NON-NLS-1$
            message.setMessage("Please check the file path and select the file again"); //$NON-NLS-1$
            if (message.open() == SWT.OK) {
                message.getParent().getShell().close();
            }
        }
        return hl7main.getHl7Manager().getUiManager().getUiResponse();
    }

    @Override
    public int open(Composite parent) {
        return open(parent.getDisplay());
    }

    @Override
    public void renameInputConnection(String oldName, String newName) {

    }

    @Override
    public void renameOutputConnection(String oldName, String newName) {

    }

    @Override
    public void setExternalData(IExternalData persistentData) {

    }

    public IMetadataTable getMetadataTable() {
        try {
            return getMetadataList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public List<Map<String, String>> getTableList(String paraName) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<IElementParameter> eps = (List<IElementParameter>) this.getElementParameters();
        if (eps == null) {
            return list;
        }
        for (int i = 0; i < eps.size(); i++) {
            IElementParameter parameter = eps.get(i);
            if (parameter.getField() == EParameterFieldType.TABLE && parameter.getName().equals(paraName)) {
                list = (List<Map<String, String>>) parameter.getValue();
                break;
            }
        }
        return list;
    }

    public boolean istFileInputHL7() {
        return getComponent().getName().equals("tFileInputHL7"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public boolean setTableElementParameter(List<Map<String, String>> epsl, String paraName) {
        List<IElementParameter> eps = (List<IElementParameter>) this.getElementParameters();
        boolean result = true;
        for (int i = 0; i < eps.size(); i++) {
            IElementParameter parameter = eps.get(i);
            if (parameter.getField() == EParameterFieldType.TABLE && parameter.getName().equals(paraName)) {
                List<Map<String, String>> newValues = new ArrayList<Map<String, String>>();
                for (Map<String, String> map : epsl) {
                    Map<String, String> newMap = new HashMap<String, String>();
                    newMap.putAll(map);
                    newValues.add(newMap);
                }

                if (result) {
                    parameter.setValue(newValues);
                }
                break;
            }
        }
        return result;
    }

}
