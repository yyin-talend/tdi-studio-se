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
package org.talend.repository.ui.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PurposeStatusSection extends AbstractSection {

    private Text purposeText;

    private StatusHelper statusHelper = new StatusHelper(getRepositoryFactory());

    private CCombo statusText;

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        FormData data;

        purposeText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(70, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        purposeText.setLayoutData(data);
        addFocusListener(purposeText);

        CLabel purposeLabel = getWidgetFactory().createCLabel(composite, "Purpose");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(purposeText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(purposeText, 0, SWT.CENTER);
        purposeLabel.setLayoutData(data);

        statusText = getWidgetFactory().createCCombo(composite, SWT.READ_ONLY | SWT.BORDER); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(purposeText, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        statusText.setLayoutData(data);
        addFocusListener(statusText);

        // addComboFieldListeners(statusText, new ModifyListener() {
        // public void modifyText(ModifyEvent e) {
        // if (enableListener)
        // needSave = true;
        // }}, true);

        CLabel statusLabel = getWidgetFactory().createCLabel(composite, "Status");
        data = new FormData();
        data.left = new FormAttachment(purposeText, ITabbedPropertyConstants.HSPACE * 3);
        data.right = new FormAttachment(statusText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(statusText, 0, SWT.CENTER);
        statusLabel.setLayoutData(data);
    }

    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        try {
            Property property = getObject().getProperty();
            List<Status> status = property == null ? new ArrayList<Status>() : statusHelper.getStatusList(property);
            statusText.setItems(toArray(status));
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    public static String[] toArray(List<Status> status) {
        String[] res = new String[status.size()];
        int i = 0;
        for (Status s : status) {
            res[i++] = s.getLabel();
        }
        return res;
    }

    @Override
    public void refresh() {
        purposeText.setText(getPurpose() != null ? getPurpose().toString() : "");
        // enableListener = false;
        statusText.setText(getStatus() != null ? getStatus().toString() : "");
        // enableListener = true;
    }

    protected String getPurpose() {
        return getObject().getPurpose();
    }

    protected String getStatus() {
        String statusCode = getObject().getStatusCode();
        if (statusCode == null || "".equals(statusCode)) {
            return "";
        }
        return statusHelper.getStatusLabel(statusCode);
    }

    @Override
    protected void beforeSave() {
        String text = purposeText.getText();
        if (!text.equals(getObject().getPurpose())) {
            getObject().setPurpose(text);
        }

        text = statusHelper.getStatusCode(statusText.getText());
        if (!text.equals(getObject().getStatusCode())) {
            getObject().setStatusCode(text);
        }
    }

    @Override
    protected void enableControl(boolean enable) {
        purposeText.setEditable(enable);
        statusText.setEditable(enable);
        statusText.setEnabled(enable);
        // nameText.setEditable(enable);
    }

    @Override
    protected void showControl(boolean visible) {
        purposeText.getParent().setVisible(visible);
    }

}
