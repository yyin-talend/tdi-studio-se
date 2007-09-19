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
package org.talend.spagic.engines.client.ui.preferences;

import org.apache.log4j.Logger;
import org.eclipse.gmf.runtime.common.ui.preferences.CheckBoxFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.model.properties.SpagoBiServer;
import org.talend.spagic.engines.client.Activator;
import org.talend.spagic.engines.client.i18n.Messages;

/**
 * Preference for the SpagoBiServer values.
 * 
 * $Id: SpagoBiPreferencePage.java 2738 2007-04-26 13:12:27Z cantoine $
 * 
 */
public class SpagicPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    protected static Logger log = Logger.getLogger(SpagicPreferencePage.class);

    protected CheckBoxFieldEditor spagoBiCheckButton;

    protected SpagicServerEditor editor;

    private Composite parent;

    /**
     * SpagoBiPreferencePage.
     * 
     * $Id: SpagoBiPreferencePage.java 2738 2007-04-26 13:12:27Z cantoine $
     * 
     */

    public SpagicPreferencePage() {
        super(FLAT);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    /*
     * @Override protected IPreferenceStore doGetPreferenceStore() { // SpagoPreferenceStore preferenceStore = new
     * SpagoPreferenceStore(ProxyRepositoryFactory.getInstance()); // try { // preferenceStore.load(); // } catch
     * (PersistenceException e) { // String detailError = e.getMessage(); // new ErrorDialogWidthDetailArea(new Shell(),
     * RepositoryPlugin.PLUGIN_ID, Messages // .getString("CommonWizard.persistenceException"), detailError);
     * //$NON-NLS-1$ // log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError);
     * //$NON-NLS-1$ // //$NON-NLS-2$ // } // return preferenceStore; return
     * Activator.getDefault().getPreferenceStore(); }
     */

    @Override
    protected void createFieldEditors() {
        parent = getFieldEditorParent();

        spagoBiCheckButton = new CheckBoxFieldEditor(SpagicPreferenceInitializer.SPAGIC_STATUS, Messages
                .getString("SpagicPreferencePage.spagicBiCheckButton"), parent);
        editor = new SpagicServerEditor(SpagoBiServer.SPAGOBI_SERVER, Messages
                .getString("SpagicPreferencePage.technicalStatusLabel"), parent); //$NON-NLS-1$
        addField(editor);

        updateEnableStateFromPreferences();

        SelectionListener listener = new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                IPreferenceStore preferenceStore = getPreferenceStore();
                preferenceStore.setValue(SpagicPreferenceInitializer.SPAGIC_STATUS, ((Button) e.getSource()).getSelection());
                updateEnableStateFromDisplay();
            }
        };
        spagoBiCheckButton.getCheckbox().addSelectionListener(listener);
    }

    private void updateEnableStateFromDisplay() {
        boolean spago = spagoBiCheckButton.getBooleanValue();
        editor.setEnabled(spago, parent);
    }

    private void updateEnableStateFromPreferences() {
        IPreferenceStore preferenceStore = getPreferenceStore();
        boolean spago = preferenceStore.getBoolean(SpagicPreferenceInitializer.SPAGIC_STATUS);
        spagoBiCheckButton.getCheckbox().setSelection(spago);
        editor.setEnabled(spago, parent);
    }

    @Override
    protected void initialize() {
        try {
            super.initialize();
        } catch (RuntimeException e) {
            e.printStackTrace();
            setErrorMessage(e.getMessage());
            log.error(e);
        }
    }

    @Override
    protected void checkState() {
        if (getErrorMessage() == null) {
            super.checkState();
        } else {
            setValid(false);
        }
    }

    public void init(IWorkbench workbench) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        super.performDefaults();
        updateEnableStateFromDisplay();
    }

}
