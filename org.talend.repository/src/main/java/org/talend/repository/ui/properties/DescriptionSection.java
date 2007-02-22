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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.repository.i18n.Messages;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DescriptionSection extends AbstractSection {

    private static final int NB_LINES = 4;

    private Text descriptionText;

    @Override
    protected void enableControl(boolean enable) {
        descriptionText.setEnabled(enable);
    }

    @Override
    protected void showControl(boolean visible) {
        descriptionText.getParent().setVisible(visible);
    }

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        FormData data;

        descriptionText = getWidgetFactory().createText(composite, "", SWT.MULTI); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        data.height = NB_LINES * descriptionText.getLineHeight();
        descriptionText.setLayoutData(data);
        addFocusListener(descriptionText);

        CLabel labelLabel = getWidgetFactory().createCLabel(composite, Messages.getString("DescriptionSection.Label")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(descriptionText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(descriptionText, 0, SWT.TOP);
        labelLabel.setLayoutData(data);

        addFocusListenerToChildren(composite);
    }

    @Override
    public void refresh() {
        descriptionText.setText(getDescription() != null ? getDescription().toString() : ""); //$NON-NLS-1$
    }

    protected String getDescription() {
        return getObject().getDescription();
    }

    @Override
    protected void beforeSave() {
        String text = descriptionText.getText();
        if (!text.equals(getObject().getDescription())) {
            getObject().setDescription(text);
        }
    }
}
