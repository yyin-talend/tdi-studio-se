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
package org.talend.repository.ui.swt.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.repository.RepositoryPlugin;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractForm extends Composite {

    /**
     * Common Forms Settings.
     */
    protected static final String PID = RepositoryPlugin.PLUGIN_ID;

    protected static final int VERTICAL_SPACING_FORM = 0;

    protected static final int WIDTH_BUTTON_PIXEL = 100;

    protected static final int HEIGHT_BUTTON_PIXEL = 30;

    /**
     * Main Forms Settings.
     */
    private boolean readOnly = false;

    private boolean isInWizard = true;

    /**
     * Use to manage the status (error, warning, info messages).
     */
    private int statusLevel = IStatus.OK;

    private String status;

    protected Label statusLabel;

    protected ICheckListener listener;

    /**
     * Use to validate the unicity of label use to the metadata.
     */
    protected List<String> existingNames;

    /**
     * DOC ocarbone AbstractForm constructor comment.
     * 
     * @param parent
     * @param style
     */
    protected AbstractForm(Composite parent, int style) {
        super(parent, style);
        this.existingNames = Collections.EMPTY_LIST;
    }

    /**
     * DOC ocarbone AbstractForm constructor comment.
     * 
     * @param parent
     * @param style
     * @param existingNames
     */
    protected AbstractForm(Composite parent, int style, String[] existingNames) {
        super(parent, style);
        this.existingNames = existingNames == null ? Collections.EMPTY_LIST : Arrays.asList(existingNames);
    }

    /**
     * DOC tguiu AbstractDelimitedFileStepForm class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    public static interface ICheckListener extends EventListener {

        void checkPerformed(AbstractForm source);
    }

    /**
     * checks control values DOC ocarbone Comment method "checkFieldsValue".
     */
    protected abstract boolean checkFieldsValue();

    /**
     * DOC ocarbone Comment method "setupForm".
     */
    protected void setupForm() {
        addComponents();
        // statusLabel is only use to SWT form / not use to Wizard
        if (!isInWizard) {
            statusLabel = new Label(this, SWT.LEFT);
            statusLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
            statusLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
        }
        addFields();
        initialize();
        addUtilsButtonListeners();
        addFieldsListeners();
    }

    /**
     * adds addComponents to the form DOC ocarbone Comment method "addComponents". add a swt Form width a statusLabel
     * who is a default element of wizard. create an instance of GridLayout and addField()
     */
    protected void addComponents() {
        // Main Layout
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = VERTICAL_SPACING_FORM;
        this.setLayout(gridLayout);
    }

    /**
     * init the UI and the values.
     * 
     * @param String
     */
    protected abstract void initialize();

    /**
     * adds listeners to controls the utils buttons disposed on the form DOC ocarbone Comment method
     * "addUtilsButtonControls".
     */
    protected abstract void addUtilsButtonListeners();

    /**
     * adds controls to parent composite DOC ocarbone Comment method "addFields".
     * 
     * @param form
     */
    protected abstract void addFields();

    /**
     * adds listeners to controls DOC ocarbone Comment method "addControls".
     */
    protected abstract void addFieldsListeners();

    /**
     * Getter for readOnly. DOC ocarbone Comment method.
     * 
     * @return the readOnly
     */
    protected boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * Sets readOnly, adapt the Form to Read Only Mode or edition and execute checkFieldsValue. DOC ocarbone Comment
     * method setReadOnly.
     * 
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        // adapt all the fields to enabled
        adaptFormToReadOnly();
        if (!readOnly) {
            // adapt the field to the context
            checkFieldsValue();
        }
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    protected abstract void adaptFormToReadOnly();

    /**
     * Sets the listener.
     * 
     * @param listener the listener to set
     */
    public void setListener(ICheckListener listener) {
        this.listener = listener;
    }

    /**
     * update Status of the Wizard OR of the label Status.
     * 
     * @param String
     */
    protected void updateStatus(final int status, final String string) {
        this.status = string;
        if (!isInWizard) {
            if (string != null) {
                statusLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
                statusLabel.setText(string);
            } else {
                statusLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
                statusLabel.setText("");
            }
        }
        this.statusLevel = status;
        if (listener != null) {
            listener.checkPerformed(this);
        }
    }

    /**
     * Getter for statusOnError.
     * 
     * @return the statusOnError
     */
    public boolean isStatusOnError() {
        return this.statusLevel == IStatus.ERROR;
    }
    /**
     * Getter for statusOnValid.
     * 
     * @return the statusOnValid
     */
    public boolean isStatusOnValid() {
        return this.statusLevel == IStatus.OK;
    }
    
    public String getStatus() {
        return status;
    }

    public int getStatusLevel() {
        return statusLevel;
    }

    protected boolean isNameAllowed(String name) {
        return existingNames.contains(name);
    }

    /**
     * Getter for isInWizard.
     * 
     * @return the isInWizard
     */
    protected boolean isInWizard() {
        return this.isInWizard;
    }

    /**
     * Sets the isInWizard.
     * 
     * @param isInWizard the isInWizard to set
     */
    protected void setInWizard(boolean isInWizard) {
        this.isInWizard = isInWizard;
    }

}
