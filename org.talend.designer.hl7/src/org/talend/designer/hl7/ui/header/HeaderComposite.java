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
package org.talend.designer.hl7.ui.header;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.hl7.managers.HL7Manager;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericParser;

/**
 * DOC xzhang class global comment. Detailled comment
 */
public class HeaderComposite extends Composite {

    private LabelledFileField fileField;

    private LabelledText startChar;

    private String startCharValue;

    private String endCharValue;

    private LabelledText endChar;

    private String messageContent = "";

    private String filePath;

    private HL7Manager hl7Manager;

    public HeaderComposite(Composite parent, int style, String filePath, String startCharValue, String endCharValue,
            HL7Manager hl7Manager) {
        super(parent, style);
        this.filePath = filePath;
        this.startCharValue = startCharValue;
        this.endCharValue = endCharValue;
        this.hl7Manager = hl7Manager;
        createComponents();
    }

    private void createComponents() {
        this.setLayout(new GridLayout(3, false));
        GridData headerCompositeGridData = new GridData(GridData.FILL_HORIZONTAL);
        this.setLayoutData(headerCompositeGridData);
        String[] extensions = new String[] { "*.*" }; //$NON-NLS-N$
        fileField = new LabelledFileField(this, "File path:", extensions, 1, SWT.BORDER) {

            protected void setFileFieldValue(String result) {
                if (result != null) {
                    getTextControl().setText(TalendTextUtils.addQuotes(PathUtils.getPortablePath(result)));
                }
            }
        };
        fileField.setText(filePath);

        final Group group = Form.createGroup(this, 4, "File Setting", 30);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        group.setLayoutData(gridData);
        startChar = new LabelledText(group, "Start character");
        startChar.setText(startCharValue);
        endChar = new LabelledText(group, "End character");
        endChar.setText(endCharValue);
        addListeners();
    }

    private void addListeners() {
        fileField.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                filePath = fileField.getText();
                doRefresh();
            }
        });

        startChar.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                startCharValue = startChar.getText();
            }
        });

        endChar.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                endCharValue = endChar.getText();
            }
        });
    }

    public void doRefresh() {
        filePath = fileField.getText();
        String filePathNoQuotes = TalendTextUtils.removeQuotes(filePath);
        File file = Path.fromOSString(filePathNoQuotes).toFile();
        if (file.exists()) {
            ByteArray array = PropertiesFactory.eINSTANCE.createByteArray();
            try {
                array.setInnerContentFromFile(file);
                TalendHL7Reader talendHL7Reader = new TalendHL7Reader(new java.io.FileInputStream(file), "ISO-8859-15");
                String HL7InputTem = null;
                String messageText = "";
                // talendHL7Reader.setStartMsgChar(startChar.getText().toCharArray()[0]);
                // talendHL7Reader.setEndMsgChar(endChar.getText().toCharArray()[0]);

                while ((HL7InputTem = talendHL7Reader.getMessage()) != null) {
                    messageText = messageText + HL7InputTem + "\r";
                }
                if (messageText == null || "".equals(messageText)) {
                    messageText = new String(array.getInnerContent());
                }
                this.messageContent = messageText;
                Message message = getHL7MessageInput(messageText);
                hl7Manager.getUiManager().getHl7UI().getContentProvider().getAllSegmentsForMessage().clear();
                hl7Manager.getUiManager().getHl7UI().getMessageViewer().setInput(new Message[] { message });
                hl7Manager.getUiManager().getHl7UI().initSchemaCombo();
                hl7Manager.getUiManager().getHl7UI().getMetadataEditor().removeAll();
                hl7Manager.getUiManager().getHl7UI().redrawLinkers();
            } catch (IOException ex) {
                ExceptionHandler.process(ex);
            }
        } else {
            MessageBox message = new MessageBox(new Shell(), SWT.APPLICATION_MODAL | SWT.OK);
            message.setText("The file is not exist"); //$NON-NLS-1$
            message.setMessage("Please check the file path and select the file again"); //$NON-NLS-1$
            if (message.open() == SWT.OK) {
                message.getParent().getShell().close();
            }
        }
    }

    private Message getHL7MessageInput(String messageText) {
        GenericParser p = new GenericParser();
        Message message = null;
        try {
            if (messageText != null) {
                message = p.parse(messageText);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            MessageBox errorBox = new MessageBox(new Shell(), SWT.APPLICATION_MODAL | SWT.OK);
            errorBox.setText("Parse error"); //$NON-NLS-1$
            errorBox.setMessage("The content can't be parsed correctly,please check the file"); //$NON-NLS-1$
            if (errorBox.open() == SWT.OK) {
                errorBox.getParent().getShell().close();
            }
        }
        return message;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public String getEndCharValue() {
        return this.endCharValue;
    }

    public String getStartCharValue() {
        return this.startCharValue;
    }

}
