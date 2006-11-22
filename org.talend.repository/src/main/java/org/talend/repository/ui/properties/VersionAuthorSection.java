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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.properties.User;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class VersionAuthorSection extends AbstractSection {

    private Text versionText;

    private Text authorText;

    private Button btnDown;

    private Button btnUp;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls(org.eclipse.swt.widgets.Composite,
     * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        FormData data;

        authorText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
        authorText.setEditable(false);
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(70, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        authorText.setLayoutData(data);

        CLabel authorLabel = getWidgetFactory().createCLabel(composite, "Author");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(authorText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(authorText, 0, SWT.CENTER);
        authorLabel.setLayoutData(data);

        btnDown = getWidgetFactory().createButton(composite, "m", SWT.PUSH);
        data = new FormData();
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        btnDown.setLayoutData(data);
        btnDown.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                versionMinorUp();
            }
        });

        btnUp = getWidgetFactory().createButton(composite, "M", SWT.PUSH);
        data = new FormData();
        data.right = new FormAttachment(btnDown, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        btnUp.setLayoutData(data);
        btnUp.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                versionMajorUp();
            }
        });

        versionText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
        versionText.setEditable(false);
        data = new FormData();
        data.left = new FormAttachment(authorText, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(btnUp, -2);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        versionText.setLayoutData(data);

        CLabel versionLabel = getWidgetFactory().createCLabel(composite, "Version");
        data = new FormData();
        data.left = new FormAttachment(authorText, ITabbedPropertyConstants.HSPACE * 3);
        data.right = new FormAttachment(versionText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionText, 0, SWT.CENTER);
        versionLabel.setLayoutData(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
     */
    @Override
    public void refresh() {
        authorText.setText(getAuthor() != null ? getAuthor().getLogin() : "");
        versionText.setText(getVersion() == null ? "" : getVersion());
    }

    private void versionMajorUp() {
        String newVersion = VersionUtils.upMajor(versionText.getText());
        versionText.setText(newVersion);
        beforeSave();
    }

    private void versionMinorUp() {
        String newVersion = VersionUtils.upMinor(versionText.getText());
        versionText.setText(newVersion);
        beforeSave();
    }

    protected User getAuthor() {
        return getObject().getAuthor();
    }

    protected String getVersion() {
        return getObject().getVersion();
    }

    @Override
    protected void beforeSave() {
        String version = versionText.getText();
        String version2 = getObject().getVersion();
        if (version != null && version2 != null) {
            if (VersionUtils.compareTo(version, version2) != 0) {
                getObject().setVersion(version);
            }
        }
    }

    @Override
    protected void enableControl(boolean enable) {
        btnDown.setEnabled(enable);
        btnUp.setEnabled(enable);
    }

    @Override
    protected void showControl(boolean visible) {
        authorText.getParent().setVisible(visible);
    }

}
