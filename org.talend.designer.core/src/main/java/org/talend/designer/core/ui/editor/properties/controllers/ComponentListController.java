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
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class ComponentListController extends AbstractElementPropertySectionController {

    public ComponentListController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    private Command createCommand(SelectionEvent selectionEvent) {
        Set<String> elementsName;
        Control ctrl;

        elementsName = hashCurControls.keySet();
        for (String name : elementsName) {
            Object o = hashCurControls.get(name);
            if (o instanceof Control) {
                ctrl = (Control) o;
                if (ctrl == null) {
                    hashCurControls.remove(name);
                    return null;
                }

                if (ctrl.equals(selectionEvent.getSource()) && ctrl instanceof CCombo) {
                    boolean isDisposed = ((CCombo) ctrl).isDisposed();
                    if (!isDisposed && (!elem.getPropertyValue(name).equals(((CCombo) ctrl).getText()))) {

                        String value = new String(""); //$NON-NLS-1$
                        for (int i = 0; i < elem.getElementParameters().size(); i++) {
                            IElementParameter param = elem.getElementParameters().get(i);
                            if (param.getName().equals(name)) {
                                for (int j = 0; j < param.getListItemsValue().length; j++) {
                                    if (((CCombo) ctrl).getText().equals(param.getListItemsDisplayName()[j])) {
                                        value = (String) param.getListItemsValue()[j];
                                    }
                                }
                            }
                        }
                        return new PropertyChangeCommand(elem, name, value);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Control createControl(Composite subComposite, IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {
        param.setDisplayName(EParameterName.COMPONENT_LIST.getDisplayName());
        IControlCreator cbCtrl = new IControlCreator() {

            public Control createControl(final Composite parent, final int style) {
                CCombo cb = new CCombo(parent, style);
                return cb;
            }
        };
        DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, cbCtrl);
        if (param.isRequired()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_REQUIRED);
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.TOP, false);
        }

        Control cLayout = dField.getLayoutControl();
        CCombo combo = (CCombo) dField.getControl();
        FormData data;
        combo.setEditable(false);
        cLayout.setBackground(subComposite.getBackground());
        combo.setEnabled(!param.isReadOnly());
        combo.addSelectionListener(listenerSelection);
        if (elem instanceof Node) {
            combo.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());
        }

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
        // *********************
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
            } else {
                data.left = new FormAttachment(0, currentLabelWidth);
            }

        } else {
            data.left = new FormAttachment(labelLabel, 0, SWT.RIGHT);
        }
        data.top = new FormAttachment(0, top);
        cLayout.setLayoutData(data);
        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);

        // **********************
        hashCurControls.put(param.getName(), combo);
        updateComponentList(elem, param);

        dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return cLayout;
    }

    public static void updateComponentList(Element elem, IElementParameter param) {
        if (elem instanceof Node) {
            List<INode> nodeList = (List<INode>) ((Node) elem).getProcess().getNodesOfType(param.getFilter());

            List<String> componentUniqueNames = new ArrayList<String>();
            for (INode node : nodeList) {
                String uniqueName = node.getUniqueName();
                String displayName = (String) node.getElementParameter("LABEL").getValue();
                if (displayName.indexOf("__UNIQUE_NAME__") != -1) {
                    uniqueName = displayName.replaceAll("__UNIQUE_NAME__", uniqueName);
                } else {
                    uniqueName = uniqueName + " - " + displayName;
                }
                componentUniqueNames.add(uniqueName);
            }

            String[] componentNameList = (String[]) componentUniqueNames.toArray(new String[0]);
            String[] componentValueList = (String[]) componentUniqueNames.toArray(new String[0]);

            param.setListItemsDisplayName(componentNameList);
            param.setListItemsValue(componentValueList);
        }
    }

    SelectionListener listenerSelection = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            Command cmd = createCommand(event);
            if (cmd != null) {
                getCommandStack().execute(cmd);
            }
        }
    };

    @Override
    public void refresh(IElementParameter param, boolean check) {
        updateComponentList(elem, param);

        String[] curComponentNameList = param.getListItemsDisplayName();

        String[] curComponentValueList = (String[]) param.getListItemsValue();

        Object value = param.getValue();
        boolean listContainValue = false;
        int numValue = 0;
        for (int i = 0; i < curComponentValueList.length && !listContainValue; i++) {
            if (curComponentValueList[i].equals(value)) {
                listContainValue = true;
                numValue = i;
            }
        }

        CCombo combo = (CCombo) hashCurControls.get(param.getName());

        combo.setItems(curComponentNameList);
        if (!listContainValue) {
            if (curComponentNameList.length > 0) {
                elem.setPropertyValue(param.getName(), curComponentNameList[0]);
                combo.setText(curComponentNameList[0]);
            }
        } else {
            combo.setText(curComponentNameList[numValue]);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub

    }

}
