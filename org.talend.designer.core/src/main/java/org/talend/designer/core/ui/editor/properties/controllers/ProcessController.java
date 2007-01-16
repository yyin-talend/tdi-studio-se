// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.runprocess.ProcessorUtilities;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ProcessController extends AbstractElementPropertySectionController {

    public ProcessController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    @Override
    public Command createCommand() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Control createControl(final Composite subComposite, final IElementParameter param, final int numInRow,
            final int nbInRow, final int top, final Control lastControl) {
        Button btn;

        final DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, new IControlCreator() {

            public Control createControl(Composite parent, int style) {
                return getWidgetFactory().createButton(parent, param.getDisplayName(), SWT.None);
            }

        });

        Control cLayout = dField.getLayoutControl();
        cLayout.setBackground(subComposite.getBackground());
        btn = (Button) dField.getControl();

        FormData data = new FormData();
        data.top = new FormAttachment(0, top);
        if (lastControl != null) {
            data.left = new FormAttachment(lastControl, 0);
        } else {
            data.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
        }
        cLayout.setLayoutData(data);
        hashCurControls.put(param.getName(), btn);
        btn.setEnabled(!param.isReadOnly());
        btn.addSelectionListener(listenerSelection);

        Point initialSize = btn.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return cLayout;
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        // TODO Auto-generated method stub

    }

    private String getCurrentProcessName() {
        String selectedProcess = null;

        for (int i = 0; (i < elem.getElementParameters().size()) && (selectedProcess == null); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
                selectedProcess = (String) param.getValue();
            }
        }
        return selectedProcess.replace("'", "");
    }

    private String getCurrentContextName() {
        String selectedContext = null;

        for (int i = 0; (i < elem.getElementParameters().size()) && (selectedContext == null); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.PROCESS_TYPE_CONTEXT.getName())) {
                selectedContext = (String) param.getValue();
            }
        }
        return selectedContext.replace("'", "");
    }

    SelectionListener listenerSelection = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            ProcessorUtilities.generateCode(getCurrentProcessName(), getCurrentContextName());
        }
    };

}
