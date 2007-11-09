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
package org.talend.repository.ui.actions.metadata.database.wizard;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public class ImportDBTableForm extends AbstractForm {

    private LabelledFileField importFile;

    /**
     * ggu ImportDBTableForm constructor comment.
     * 
     * @param parent
     * @param style
     * @param existingNames
     */
    public ImportDBTableForm(Composite parent) {
        super(parent, SWT.NONE);

        setupForm();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        // nothing to do

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        int width = getSize().x;
        GridLayout layout2;

        Group group = Form.createGroup(this, 1, "Import", 50);

        Composite paraSetting = Form.startNewDimensionnedGridLayout(group, 3, width, 50);
        // layout2 = (GridLayout) paraSetting.getLayout();
        // layout2.marginHeight = 0;
        // layout2.marginTop = 0;
        // layout2.marginBottom = 0;
        // layout2.marginLeft = 0;
        // layout2.marginRight = 0;
        // layout2.marginWidth = 0;

        Composite tmpSetting = Form.startNewGridLayout(paraSetting, 3);
        GridData layoutData = (GridData) tmpSetting.getLayoutData();
        layoutData.horizontalSpan = 3;
        importFile = new LabelledFileField(tmpSetting, "Import File:", new String[] { "*.csv", "*.*" });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        importFile.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String str = importFile.getText().trim();
                if (str == "") {
                    updateStatus(IStatus.ERROR, Messages.getString("ImportDBTableWizard.Description")); //$NON-NLS-1$
                    return;
                }
                File file = new File(str);
                if (!file.exists()) {
                    updateStatus(IStatus.ERROR, "File not found.");
                    return;
                }
                updateStatus(IStatus.OK, Messages.getString("ImportDBTableWizard.Description")); //$NON-NLS-1$
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        // nothing to do
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        return true;
    }

    public File getImportFile() {
        File file = new File(importFile.getText().trim());
        return file.exists() ? file : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {

    }

}
