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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.Attribute;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.utils.CsvArray;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.ui.swt.preview.ShadowProcessPreview;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.swt.utils.IRefreshable;
import org.talend.repository.ui.utils.ShadowProcessHelper;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaStep3Form extends AbstractForm implements IRefreshable {

    private static Logger log = Logger.getLogger(LDAPSchemaStep3Form.class);

    private Group previewGroup;

    public static List<String> itemTableNameList;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview ldapSchemaPreview;

    /**
     * Another.
     */

    private UtilsButton cancelButton;

    private boolean readOnly;

    private ExtendedTableModel<String> attributeModel;

    private AbstractExtendedTableViewer<String> tableEditorView;

    SWTUIThreadProcessor processor = new PreviewProcessor();

    private ConnectionItem connectionItem;

    private Text filterText;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public LDAPSchemaStep3Form(Composite parent, ConnectionItem connectionItem) {
        super(parent, SWT.NONE, null);
        this.connectionItem = connectionItem;
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        // compositeFile Main Fields
        Composite mainComposite = Form.startNewGridLayout(this, 2);
        addGroupAttributes(mainComposite, 300, 115);
        addFilter(mainComposite, 300, 85);
        addGroupFileViewer(this, 700, 180);

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton,
                    Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        filterText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String filter = filterText.getText();
                if (filter != null && filter.length() > 0)
                    getConnection().setFilter(filterText.getText().trim());
            }
        });
    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    protected void addUtilsButtonListeners() {

        // Event PreviewButton
        previewButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {

                refreshPreview();
            }
        });

        if (cancelButton != null) {
            // Event CancelButton
            cancelButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
        }

        // Event checkBox action
        final Table table = tableEditorView.getTableViewerCreator().getTable();
        table.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem tableItem = (TableItem) e.item;
                    boolean promptNeeded = tableItem.getChecked();
                    if (promptNeeded) {
                        getConnection().getValue().add(tableItem.getText());
                    } else {
                        getConnection().getValue().remove(tableItem.getText());
                    }
                }
                checkFieldsValue();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        previewInformationLabel.setText(""); //$NON-NLS-1$
        updateStatus(IStatus.OK, null);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        String filter = getConnection().getFilter();
        if (filter != null) {
            this.filterText.setText(filter);
        } else {
            this.filterText.setText(ConnectionUIConstants.DEFAULT_FILTER);
        }

        checkFieldsValue();
    }

    /**
     * Administrator Comment method "getConnection".
     * 
     * @return
     */
    private LDAPSchemaConnection getConnection() {
        return (LDAPSchemaConnection) this.connectionItem.getConnection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.IRefreshable#refresh()
     */
    public void refresh() {
        refreshPreview();
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the preview event. <br/>
     * 
     * $Id: DelimitedFileStep2Form.java 4837 2007-07-27 05:40:31Z bqian $
     * 
     */
    class PreviewProcessor extends SWTUIThreadProcessor {

        CsvArray csvArray = null;

        ProcessDescription processDescription = null;

        public boolean preProcessStart() {
            previewButton.setText(Messages.getString("FileStep2.stop"));

            clearPreview();

            // if incomplete settings, , the process don't be executed
            if (!checkFieldsValue()) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$
                //$NON-NLS-2$
                return false;
            }

            previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$
            //$NON-NLS-2$
            processDescription = getProcessDescription();
            return true;
        }

        public void nonUIProcessInThread() {
            // get the CsvArray width an adapt ProcessDescription
            try {
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "LDAP_SCHEMA"); //$NON-NLS-1$

            } catch (CoreException e) {
                setException(e);
                log.error(Messages.getString("FileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        public void updateUIInThreadIfThreadIsCanceled() {
            if (!previewInformationLabel.isDisposed()) {
                previewInformationLabel.setText("");
            }
        }

        public void updateUIInThreadIfThreadIsNotCanceled() {
            if (previewInformationLabel.isDisposed()) {
                return;
            }
            if (getException() != null) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$
                //$NON-NLS-2$
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("FileStep2.previewFailure"), getException().getMessage()); //$NON-NLS-1$
                return;
            }

            if (csvArray == null) {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$
                //$NON-NLS-2$
            } else {
                previewInformationLabel.setText(" " + Messages.getString("FileStep2.previewIsDone")); //$NON-NLS-1$
                //$NON-NLS-2$

                // refresh TablePreview on this step
                ldapSchemaPreview.refreshTablePreview(csvArray, false, processDescription);
                previewInformationLabel.setText(""); //$NON-NLS-1$
            }
        }

        public void updateUIInThreadIfThreadFinally() {
            if (!previewButton.isDisposed()) {
                previewButton.setText(Messages.getString("FileStep2.refreshPreview"));
                previewButton.setEnabled(true);
            }
        }

        public void postProcessCancle() {
            previewButton.setEnabled(false);
        }
    }

    /**
     * 
     * addGroupAttributes.
     */
    private void addGroupAttributes(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        Group group = Form.createGroup(mainComposite, 1, "List attributes of LDAP Schema", height); //$NON-NLS-1$

        attributeModel = new ExtendedTableModel<String>();
        attributeModel.registerDataList(itemTableNameList);
        tableEditorView = new AbstractExtendedTableViewer<String>(attributeModel, group) {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions(org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
             */
            @Override
            protected void setTableViewerCreatorOptions(TableViewerCreator<String> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setFirstColumnMasked(false);
                newTableViewerCreator.setCheckboxInFirstColumn(true);
            }

            @Override
            protected void createColumns(TableViewerCreator<String> tableViewerCreator, Table table) {
                TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
                column.setBeanPropertyAccessors(new IBeanPropertyAccessors<String, String>() {

                    public String get(String bean) {
                        return bean.toString();
                    }

                    public void set(String bean, String value) {
                    }

                });
                column.setTitle(Messages.getString("LdifFileStep2Form.columnTtitle.attributes")); //$NON-NLS-1$
                column.setWeight(100);

            }

        };
    }

    /**
     * add Field to Group File Viewer.
     * 
     * @param parent
     * @param form
     * @param width
     * @param height
     */
    private void addGroupFileViewer(final Composite parent, final int width, int height) {
        // composite LDAP Schema Preview
        previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height); //$NON-NLS-1$
        Composite compositeLdifFilePreviewButton = Form.startNewDimensionnedGridLayout(previewGroup, 4, width,
                HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // LDAP Schema Preview Info
        previewButton = new Button(compositeLdifFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // simple space
        new Label(compositeLdifFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeLdifFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeLDAPSchemaPreview = Form.startNewDimensionnedGridLayout(previewGroup, 1, width, height);

        // LDAP Schema Preview
        ldapSchemaPreview = new ShadowProcessPreview(compositeLDAPSchemaPreview, null, width, height - 10);
        ldapSchemaPreview.newTablePreview();
    }

    /**
     * add field to Group Limit.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addFilter(final Composite mainComposite, final int width, final int height) {
        // Composite filter rows
        Group group = Form.createGroup(mainComposite, 1, "Filter", height);
        Composite compositeFilter = Form.startNewDimensionnedGridLayout(group, 3, width, height);

        // Information Text
        filterText = new Text(compositeFilter, SWT.MULTI | SWT.V_SCROLL);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        filterText.setLayoutData(gridData);
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        processor.execute();
    }

    /**
     * populateLDAPSchemaAttributes method to populate the Table of Attributes to read the ldap schema
     * 
     */
    protected void populateLDAPSchemaAttributes() {

        itemTableNameList = new ArrayList<String>();

        List<Attribute> attributeList = LDAPConnectionUtils.getAttributes((LDAPSchemaConnection) this.connectionItem
                .getConnection());
        for (Attribute attribute : attributeList) {
            String attributeId = attribute.getID();
            if (itemTableNameList.contains(attributeId) == false) {
                itemTableNameList.add(attributeId);
                // System.out.println(attributeId);
            }
        }
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        ldapSchemaPreview.clearTablePreview();
    }

    /**
     * create ProcessDescription and set it.
     * 
     * WARNING ::field FieldSeparator, RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * @param getConnection()
     * 
     * @return processDescription
     */
    private ProcessDescription getProcessDescription() {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(getConnection());
        return processDescription;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     * 
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (super.isVisible()) {

            try {
                populateLDAPSchemaAttributes();
            } catch (Exception e) {
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("LdifFileStep2.previewFailure"), e.getMessage()); //$NON-NLS-1$
                log.error(Messages.getString("LdifFileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
                updateStatus(IStatus.ERROR, Messages.getString("LdifFileStep2.previewFailure")); //$NON-NLS-1$
            }
            attributeModel.registerDataList(itemTableNameList);

            if (getConnection().getValue() != null && !getConnection().getValue().isEmpty()) {
                refreshPreview();
            }

            EList attributeValueList = getConnection().getValue();
            if (attributeValueList != null && !attributeValueList.isEmpty()) {
                refreshPreview();
                checkTheRightAttributes(attributeValueList);
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
        }
    }

    /**
     * checkTheRightAttributes.
     * 
     * @param getConnection().getValue() Checked Attribute Checked in EMF model
     */
    protected void checkTheRightAttributes(List<String> attribute) {

        TableItem[] tableItems = tableEditorView.getTableViewerCreator().getTable().getItems();
        for (int j = 0; j < tableItems.length; j++) {
            TableItem tableItem = tableItems[j];
            for (int i = 0; i < attribute.size(); i++) {
                String attributeName = attribute.get(i);
                if (attributeName != null && !("").equals(attributeName)) { //$NON-NLS-1$
                    if (tableItem.getText().equals(attributeName)) {
                        tableItem.setChecked(true);
                        break;
                    }
                }
            }
        }
    }
}
