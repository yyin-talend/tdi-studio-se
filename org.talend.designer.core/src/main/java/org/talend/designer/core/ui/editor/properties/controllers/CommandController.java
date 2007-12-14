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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.ui.editor.cmd.ExecuteSystemCommandCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class CommandController extends AbstractElementPropertySectionController {

    private static final String VALUES = "VALUES"; //$NON-NLS-1$

    /**
     * DOC ggu ButtonController constructor comment.
     * 
     * @param dp
     */
    public CommandController(IDynamicProperty dp) {
        super(dp);

    }

    @Override
    public Control createControl(Composite subComposite, final IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {
        this.paramFieldType = param.getField();
        FormData data;
        // button
        Button btnCmd = getWidgetFactory().createButton(subComposite, null, SWT.PUSH);
        btnCmd.setImage(CorePlugin.getImageDescriptor(DOTS_BUTTON).createImage());

        int btnWidth = 0;
        if (btnCmd.getImage() != null) {
            btnWidth = btnCmd.getImage().getBounds().width + ITabbedPropertyConstants.HSPACE * 2;
        }
        if (btnCmd.getText() != null && !"".equals(btnCmd.getText())) { //$NON-NLS-1$
            GC gc = new GC(btnCmd);
            Point cmdSize = gc.stringExtent(btnCmd.getText());
            gc.dispose();
            btnWidth += (cmdSize.x + ITabbedPropertyConstants.HSPACE * 2);
        }
        if (btnWidth < STANDARD_BUTTON_WIDTH) {
            btnWidth = STANDARD_BUTTON_WIDTH;
        } else {
            btnWidth += ITabbedPropertyConstants.HSPACE;
        }

        btnCmd.setData(PARAMETER_NAME, param.getName());
        btnCmd.setData(VALUES, param.getDefaultValues());
        btnCmd.setEnabled(!param.isReadOnly());
        btnCmd.addSelectionListener(listenerSelection);
        if (elem instanceof Node) {
            btnCmd.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());
        }
        // label
        CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, param.getDisplayName());
        data = new FormData();
        if (lastControl != null) {
            data.left = new FormAttachment(lastControl, 0);
        } else {
            data.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
        }
        data.top = new FormAttachment(0, top);
        labelLabel.setLayoutData(data);
        if (numInRow != 1) {
            labelLabel.setAlignment(SWT.RIGHT);
        }
        // **************************
        data = new FormData();
        int currentLabelWidth = STANDARD_LABEL_WIDTH;
        GC gc = new GC(labelLabel);
        Point labelSize = gc.stringExtent(param.getDisplayName());
        gc.dispose();

        if ((labelSize.x + ITabbedPropertyConstants.HSPACE) > currentLabelWidth) {
            currentLabelWidth = labelSize.x + ITabbedPropertyConstants.HSPACE;
        }

        if (numInRow == 1) {
            if (lastControl != null) {
                data.left = new FormAttachment(lastControl, currentLabelWidth);
                data.right = new FormAttachment(lastControl, currentLabelWidth + btnWidth);
            } else {
                data.left = new FormAttachment(0, currentLabelWidth);
                data.right = new FormAttachment(0, currentLabelWidth + btnWidth);
            }
        } else {
            data.left = new FormAttachment(labelLabel, 0, SWT.RIGHT);
            data.right = new FormAttachment(labelLabel, btnWidth, SWT.RIGHT);
        }
        data.top = new FormAttachment(0, top);
        data.height = STANDARD_HEIGHT;
        btnCmd.setLayoutData(data);
        // **************************
        hashCurControls.put(param.getName(), btnCmd);

        Point initialSize = btnCmd.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        dynamicProperty.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return btnCmd;
    }

    @Override
    public int estimateRowSize(Composite subComposite, IElementParameter param) {
        Button btnEdit = getWidgetFactory().createButton(subComposite, null, SWT.PUSH); //$NON-NLS-1$
        btnEdit.setImage(CorePlugin.getImageDescriptor(DOTS_BUTTON).createImage());
        Point initialSize = btnEdit.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        btnEdit.dispose();
        return initialSize.y + ITabbedPropertyConstants.VSPACE;
    }

    @Override
    public void refresh(IElementParameter param, boolean check) {

    }

    public void propertyChange(PropertyChangeEvent evt) {

    }

    private SelectionListener listenerSelection = new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent e) {

        }

        public void widgetSelected(SelectionEvent event) {
            Command command = createCommand(event);
            if (command != null) {
                getCommandStack().execute(command);
            }

        }

    };

    private Command createCommand(SelectionEvent event) {
        Control ctrl = (Control) event.getSource();
        if (ctrl instanceof Button) {
            List<String> commandsList = new ArrayList<String>();
            List<IElementParameterDefaultValue> values = (List<IElementParameterDefaultValue>) ctrl.getData(VALUES);
            for (IElementParameterDefaultValue eleParam : values) {
                String value = TalendTextUtils.removeQuotes((String) eleParam.getDefaultValue());
                if (!"".equals(value.trim())) {
                    commandsList.add(value.trim());
                }
            }
            if (!commandsList.isEmpty()) {
                return new ExecuteSystemCommandCommand(commandsList);
            }
        }
        return null;
    }
}
