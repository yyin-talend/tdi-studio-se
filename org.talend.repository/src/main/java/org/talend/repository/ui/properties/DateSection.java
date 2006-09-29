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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DateSection extends AbstractSection {

    private Text creationText;

    private Text modificationText;

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat();

    @Override
    protected void enableControl(boolean enable) {
//        creationText.setEditable(enable);
//        modificationText.setEditable(enable);
    }
    @Override
    protected void showControl(boolean visible) {
        creationText.getParent().setVisible(visible);
    }


    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        FormData data;

        creationText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(50, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        creationText.setLayoutData(data);
        creationText.setEditable(false);

        CLabel purposeLabel = getWidgetFactory().createCLabel(composite, "Creation");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(creationText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(creationText, 0, SWT.CENTER);
        purposeLabel.setLayoutData(data);

        modificationText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(creationText, STANDARD_LABEL_WIDTH + 15);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        modificationText.setLayoutData(data);
        modificationText.setEditable(false);

        CLabel statusLabel = getWidgetFactory().createCLabel(composite, "Modification");
        data = new FormData();
        data.left = new FormAttachment(creationText, ITabbedPropertyConstants.HSPACE * 3);
        data.right = new FormAttachment(modificationText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(modificationText, 0, SWT.CENTER);
        statusLabel.setLayoutData(data);
    }

    @Override
    public void refresh() {
        creationText.setText(getCreationDate() != null ? FORMATTER.format(getCreationDate()) : "");
        modificationText.setText(getModificationDate() != null ? FORMATTER.format(getModificationDate()) : "");
    }

    protected Date getCreationDate() {
        return getObject().getCreationDate();
    }

    protected Date getModificationDate() {
        return getObject().getModificationDate();
    }

    protected void beforeSave() {
    }
}
