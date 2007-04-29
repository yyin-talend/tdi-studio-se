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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.TextControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.cmd.EncodingTypeChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * This class is used for adding a selected combo box for encoding type.
 * 
 * $Id: EncodingTypeController.java 2007-2-11,上午10:23:00 ftang $
 * 
 */
public class EncodingTypeController extends AbstractElementPropertySectionController {

    @Override
    protected Command getTextCommandForHelper(String paramName, String value) {
        return new EncodingTypeChangeCommand(elem, paramName, value);
    }

    /**
     * 
     */
    private SelectionEvent selectionEvent;

    /**
     * DOC ftang EncodingTypeController constructor comment.
     * 
     * @param dtp
     */
    public EncodingTypeController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#createCommand()
     */
    @Override
    public Command createCommand() {
        return null;// do nothing

    }

    /**
     * DOC ftang Comment method "createComboCommand".
     * 
     * @param combo
     * @return
     */
    private Command createComboCommand(CCombo combo) {
        String paramName;

        paramName = EParameterName.ENCODING_TYPE.getName();
        String value = new String(""); //$NON-NLS-1$
        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(paramName)) {
                for (int j = 0; j < param.getListItemsValue().length; j++) {
                    if (combo.getText().equals(param.getListItemsDisplayName()[j])) {
                        value = (String) param.getListItemsValue()[j];
                    }
                }
            }
        }
        return new EncodingTypeChangeCommand(elem, paramName, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#createControl(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter, int, int, int, org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(Composite subComposite, IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {
        CCombo combo;
        Control lastControlUsed = lastControl;

        if (elem instanceof Node) {
            combo = new CCombo(subComposite, SWT.BORDER);
            IElementParameter encodingTypeParameter = elem.getElementParameter(EParameterName.ENCODING_TYPE.getName());
            FormData data;
            String[] originalList = encodingTypeParameter.getListItemsDisplayName();
            List<String> stringToDisplay = new ArrayList<String>();
            String[] itemsShowIf = encodingTypeParameter.getListItemsShowIf();
            if (itemsShowIf != null) {
                String[] itemsNotShowIf = encodingTypeParameter.getListItemsNotShowIf();
                for (int i = 0; i < originalList.length; i++) {
                    if (encodingTypeParameter.isShow(itemsShowIf[i], itemsNotShowIf[i], elem.getElementParameters())) {
                        stringToDisplay.add(originalList[i]);
                    }
                }
            } else {
                for (int i = 0; i < originalList.length; i++) {
                    stringToDisplay.add(originalList[i]);
                }
            }
            combo.setItems(stringToDisplay.toArray(new String[0]));
            combo.setEditable(false);
            combo.setEnabled(!encodingTypeParameter.isReadOnly());
            if (elem instanceof Node) {
                combo.setToolTipText(VARIABLE_TOOLTIP + encodingTypeParameter.getVariableName());
            }

            CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, encodingTypeParameter.getDisplayName());
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
            // *********************
            data = new FormData();
            int currentLabelWidth = STANDARD_LABEL_WIDTH;
            GC gc = new GC(labelLabel);
            Point labelSize = gc.stringExtent(encodingTypeParameter.getDisplayName());
            gc.dispose();

            if ((labelSize.x + ITabbedPropertyConstants.HSPACE) > currentLabelWidth) {
                currentLabelWidth = labelSize.x + ITabbedPropertyConstants.HSPACE;
            }

            if (numInRow == 1) {
                if (lastControl != null) {
                    data.left = new FormAttachment(lastControl, currentLabelWidth);
                } else {
                    data.left = new FormAttachment(0, currentLabelWidth);
                }

            } else {
                data.left = new FormAttachment(labelLabel, 0, SWT.RIGHT);
            }
            data.top = new FormAttachment(0, top);
            combo.setLayoutData(data);
            combo.addSelectionListener(selectionChangeListener);
            lastControlUsed = combo;
            String encodingType = (String) encodingTypeParameter.getValue();
            if (encodingType != null && encodingType.equals(EmfComponent.ENCODING_TYPE_CUSTOM)) {
                lastControlUsed = addCustomEncodingTypeText(subComposite, param, lastControlUsed, numInRow, nbInRow,
                        top);
            }

            // **********************
            hashCurControls.put(EParameterName.ENCODING_TYPE.getName(), combo);

            Point initialSize = combo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);

        }
        return lastControlUsed;
    }

    /**
     * DOC ftang Comment method "addCustomEncodingTypeText".
     * 
     * @param subComposite
     * @param param
     * @param lastControl
     * @param numInRow
     * @param nbInRow
     * @param top
     * @return
     */
    private Control addCustomEncodingTypeText(Composite subComposite, IElementParameter param, Control lastControl,
            int numInRow, int nbInRow, int top) {
        FormData data;
        Text labelText;

        IElementParameter encodingTypeParameter = elem.getElementParameter(EParameterName.REPOSITORY_ENCODING_TYPE
                .getName());

        final DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, new TextControlCreator());
        if (encodingTypeParameter.isRequired()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_REQUIRED);
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.TOP, false);
        }
        if (encodingTypeParameter.isRepositoryValueUsed()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
            decoration.setDescription(Messages.getString("TextController.decoration.description")); //$NON-NLS-1$
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.BOTTOM, false);
        }
        Control cLayout = dField.getLayoutControl();
        labelText = (Text) dField.getControl();
        labelText.setData(PARAMETER_NAME, encodingTypeParameter.getName());

        editionControlHelper.register(encodingTypeParameter.getName(), labelText, true);

        cLayout.setBackground(subComposite.getBackground());
        labelText.setEditable(!encodingTypeParameter.isReadOnly());
        labelText.setToolTipText(VARIABLE_TOOLTIP + encodingTypeParameter.getVariableName());
        data = new FormData();
        data.left = new FormAttachment(lastControl, 0);
        data.right = new FormAttachment((numInRow * MAX_PERCENT) / nbInRow, 0);
        data.top = new FormAttachment(0, top);
        cLayout.setLayoutData(data);
        hashCurControls.put(encodingTypeParameter.getName(), labelText);

        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#refresh(org.talend.core.model.process.IElementParameter,
     * boolean)
     */
    @Override
    public void refresh(IElementParameter param, boolean checkErrorsWhenViewRefreshed) {
        CCombo combo = (CCombo) hashCurControls.get(EParameterName.ENCODING_TYPE.getName());
        if (combo == null) {
            return;
        }
        IElementParameter encodingTypeParameter = elem.getElementParameter(EParameterName.ENCODING_TYPE.getName());
        Object value = encodingTypeParameter.getValue();

        if (value instanceof String) {
            String strValue = ""; //$NON-NLS-1$
            int nbInList = 0, nbMax = encodingTypeParameter.getListItemsValue().length;
            String name = (String) value;
            while (strValue.equals(new String("")) && nbInList < nbMax) { //$NON-NLS-1$
                if (name.equals(encodingTypeParameter.getListItemsValue()[nbInList])) {
                    strValue = encodingTypeParameter.getListItemsDisplayName()[nbInList];
                }
                nbInList++;
            }
            String[] paramItems = encodingTypeParameter.getListItemsDisplayName();
            String[] comboItems = combo.getItems();
            if (!paramItems.equals(comboItems)) {
                combo.setItems(paramItems);
            }
            combo.setText(strValue);
        }

        encodingTypeParameter = elem.getElementParameter(EParameterName.REPOSITORY_ENCODING_TYPE.getName());
        if (hashCurControls.get(encodingTypeParameter.getName()) instanceof Text) {
            Text labelText = (Text) hashCurControls.get(encodingTypeParameter.getName());
            if (labelText == null) {
                return;
            }
            Object value1 = encodingTypeParameter.getValue();
            boolean valueChanged = false;
            if (value1 == null) {
                labelText.setText(""); //$NON-NLS-1$
            } else {
                if (!value1.equals(labelText.getText())) {
                    labelText.setText((String) value1);
                    valueChanged = true;
                }
            }

            if (checkErrorsWhenViewRefreshed || valueChanged) {
                checkErrorsForPropertiesOnly(labelText);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
    }

    /**
     * 
     */
    SelectionListener selectionChangeListener = new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent e) {
            // do nothing.
        }

        public void widgetSelected(SelectionEvent e) {
            selectionEvent = e;
            Command cmd = createComboCommand((CCombo) selectionEvent.getSource());
            if (cmd != null) {
                getCommandStack().execute(cmd);
            }
        }
    };
}
