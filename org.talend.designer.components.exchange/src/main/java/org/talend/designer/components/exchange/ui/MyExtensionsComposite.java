// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.exchange.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.core.model.process.EComponentCategory;
import org.talend.designer.components.exchange.ExchangePlugin;
import org.talend.designer.components.exchange.i18n.Messages;
import org.talend.designer.components.exchange.model.Category;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.model.ExchangeFactory;
import org.talend.designer.components.exchange.model.VersionRevision;
import org.talend.designer.components.exchange.ui.actions.DeleteExtensionAction;
import org.talend.designer.components.exchange.ui.actions.InsertionExtensionAction;
import org.talend.designer.components.exchange.ui.actions.ModifyExtensionAction;
import org.talend.designer.components.exchange.ui.actions.UploadRevisionAction;
import org.talend.designer.components.exchange.util.ExchangeUtils;
import org.talend.designer.components.exchange.util.ExchangeWebService;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class MyExtensionsComposite extends ExchangeComposite {

    private ISelection selection;

    private IWizardPage wizardPage = null;

    private Table itemTable;

    private static final String[] FILE_EXPORT_MASK = { "*.zip;*.tar;*.tar.gz", "*.*" }; //$NON-NLS-1$ //$NON-NLS-2$

    final StackLayout stackLayout;

    private Composite listMyExtensonsComp, formComp, addToolBarComp;

    private Button browseUploadFileBtn, addNewExtensonBtn;

    private Text selectUploadFileText;

    private String previouslyBrowsedArchive = ""; //$NON-NLS-1$

    private boolean enableControl;

    private List<VersionRevision> fVersionRevisions = new ArrayList<VersionRevision>();

    private List<Category> fCategorys = new ArrayList<Category>();

    private String listVersionCompatibles = "";

    private Map<Integer, Image> imageMaps = new HashMap<Integer, Image>();

    /**
     * DOC hcyi MyExtensionsComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public MyExtensionsComposite(Composite parent, int styles, TabbedPropertySheetWidgetFactory factory,
            List<ComponentExtension> fExtensions) {
        super(parent, styles, factory);
        FormLayout layout = new FormLayout();
        setLayout(layout);
        FormData thisFormData = new FormData();
        thisFormData.left = new FormAttachment(0, 0);
        thisFormData.right = new FormAttachment(100, 0);
        thisFormData.top = new FormAttachment(0, 0);
        thisFormData.bottom = new FormAttachment(100, 0);
        setLayoutData(thisFormData);

        //
        addToolBarComp = widgetFactory.createFlatFormComposite(this);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        addToolBarComp.setLayoutData(data);
        FormLayout addNewExtensonCompLayout = new FormLayout();
        addToolBarComp.setLayout(addNewExtensonCompLayout);

        addNewExtensonBtn = widgetFactory.createButton(addToolBarComp,
                Messages.getString("MyExtensionsComposite.AddNewExtensionTitle"), SWT.CENTER);
        data = new FormData();
        data.left = new FormAttachment(80, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        addNewExtensonBtn.setLayoutData(data);

        //
        listMyExtensonsComp = widgetFactory.createFlatFormComposite(this);
        FormData compositeData = new FormData();
        compositeData.left = new FormAttachment(0, 0);
        compositeData.right = new FormAttachment(100, 0);
        compositeData.top = new FormAttachment(addToolBarComp, 0);
        compositeData.bottom = new FormAttachment(100, 0);
        listMyExtensonsComp.setLayoutData(compositeData);
        FormLayout nsonCompLayout = new FormLayout();
        listMyExtensonsComp.setLayout(nsonCompLayout);

        stackLayout = new StackLayout();
        listMyExtensonsComp.setLayout(stackLayout);

        itemTable = new Table(listMyExtensonsComp, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout tableLayout = new TableLayout();
        itemTable.setLayout(tableLayout);
        itemTable.setHeaderVisible(true);
        itemTable.setLinesVisible(true);

        //
        TableColumn itemColumn = new TableColumn(itemTable, SWT.CENTER);
        itemColumn.setText(Messages.getString("MyExtensionsComposite.ExtensionName")); //$NON-NLS-1$
        itemColumn.setWidth(250);

        TableColumn versionColumn = new TableColumn(itemTable, SWT.CENTER);
        versionColumn.setText(Messages.getString("MyExtensionsComposite.Version")); //$NON-NLS-1$
        versionColumn.setWidth(220);

        TableColumn dateColumn = new TableColumn(itemTable, SWT.CENTER);
        dateColumn.setText(Messages.getString("MyExtensionsComposite.UploadDate")); //$NON-NLS-1$
        dateColumn.setWidth(220);

        Object layoutData = listMyExtensonsComp.getLayoutData();
        if (layoutData instanceof GridData) {
            GridData gridData = (GridData) layoutData;
            gridData.grabExcessVerticalSpace = true;
            gridData.verticalAlignment = SWT.FILL;
        }

        FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        formData.top = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.bottom = new FormAttachment(100);
        itemTable.setLayoutData(formData);

        // event
        addNewExtensonBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                //
                formComp = widgetFactory.createFlatFormComposite(listMyExtensonsComp);
                FormData data = new FormData();
                data.left = new FormAttachment(0, 0);
                data.right = new FormAttachment(100, 0);
                data.top = new FormAttachment(0, 0);
                data.bottom = new FormAttachment(100, 0);
                formComp.setLayoutData(data);
                formComp.setLayout(new FormLayout());
                createUpLoadFormComposite();
                stackLayout.topControl = formComp;
                listMyExtensonsComp.layout();
                addNewExtensonBtn.setEnabled(false);
            }
        });
        itemTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] itemList = itemTable.getItems();
                for (int i = 0; i < itemList.length; i++) {
                    if (itemTable.isSelected(i)) {
                        addOperateMenu(itemList[i]);
                        break;
                    } else {
                        itemTable.setMenu(null);
                        addNewExtensonBtn.setEnabled(true);
                    }
                }
            }
        });

        stackLayout.topControl = itemTable;
        listMyExtensonsComp.layout();
        //
        refresh();
        if (fExtensions != null && !fExtensions.isEmpty()) {
            for (ComponentExtension ext : fExtensions) {
                fContributedExtensions.put(ext.getIdExtension(), ext);
            }
        }
        refreshTableItems();

    }

    private void refreshTableItems() {
        addItemElements(fContributedExtensions);
        getVersionRevisionsAndCategorys();
    }

    private void addItemElements(Map<String, ComponentExtension> elements) {
        if (elements == null || elements.isEmpty()) {
            return;
        }
        itemTable.setRedraw(false);

        Iterator ite = elements.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, ComponentExtension> entry = (Entry<String, ComponentExtension>) ite.next();
            ComponentExtension object = entry.getValue();

            final TableItem tableItem = new TableItem(itemTable, SWT.NONE);
            tableItem.setData(object);

            // Extension Name
            tableItem.setText(0, object.getLabel());
            tableItem.setText(1, object.getLastVersionAvailable());
            tableItem.setText(2, formatter.format(object.getPublicationDate()));
        }
        itemTable.setRedraw(true);
    }

    public void getFormComposite(int operateStatus, ComponentExtension componentExtension) {
        //
        if (operateStatus == 0) {
            createUpdateFormComposite(operateStatus, componentExtension);
        } else if (operateStatus == 1) {
            createModifyFormComposite(operateStatus, componentExtension);
        }
    }

    /**
     * 
     * DOC hcyi Comment method "createUpLoadComposite".
     */
    public void createUpLoadFormComposite() {

        //
        ScrolledComposite formScroll = new ScrolledComposite(formComp, SWT.V_SCROLL | SWT.H_SCROLL);
        formScroll.setExpandHorizontal(true);
        formScroll.setExpandVertical(true);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        formScroll.setLayoutData(data);

        Composite composite = new Composite(formScroll, SWT.NONE);
        formScroll.setContent(composite);
        formScroll.setLayout(new FormLayout());
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        composite.setLayoutData(data);
        composite.setLayout(new FormLayout());

        Button returnBtn = widgetFactory.createButton(composite, Messages.getString("MyExtensionsComposite.Form.Return"),
                SWT.CENTER);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(10, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        returnBtn.setLayoutData(data);

        // Extension Title
        final Text extensionTitleText = widgetFactory.createText(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(50, 0);
        data.top = new FormAttachment(returnBtn, ITabbedPropertyConstants.VSPACE);
        extensionTitleText.setLayoutData(data);

        CLabel extensionTitle = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.ExtensionTitle")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(extensionTitleText, 0, SWT.CENTER);
        extensionTitle.setLayoutData(data);

        final Text versionText = widgetFactory.createText(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(extensionTitleText, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(extensionTitle, ITabbedPropertyConstants.VSPACE, SWT.CENTER);
        versionText.setLayoutData(data);

        CLabel initialVersion = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.InitialVersion")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(extensionTitleText, ITabbedPropertyConstants.HSPACE - 3);
        data.right = new FormAttachment(85, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionText, 0, SWT.CENTER);
        initialVersion.setLayoutData(data);

        // Compatibility
        Group compatibilityGroup = widgetFactory.createGroup(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(extensionTitleText, 0);
        compatibilityGroup.setLayoutData(data);
        FormLayout compatibilityGroupLayout = new FormLayout();
        compatibilityGroup.setLayout(compatibilityGroupLayout);

        final Button allVersionsButton = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AllVersions"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(extensionTitle, ITabbedPropertyConstants.VSPACE);
        allVersionsButton.setLayoutData(data);

        final Button versionOlderBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.Version"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(17, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 30);
        data.top = new FormAttachment(allVersionsButton, ITabbedPropertyConstants.VSPACE);
        versionOlderBtn.setLayoutData(data);

        final Text versionOlderText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOlderBtn, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOlderBtn, 0, SWT.TOP);
        versionOlderText.setLayoutData(data);
        versionOlderText.setEnabled(false);

        CLabel andOlderLabel = widgetFactory.createCLabel(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AndOlder")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOlderText, 0);
        data.right = new FormAttachment(50, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOlderText, 0, SWT.TOP);
        andOlderLabel.setLayoutData(data);

        final Button versionExceptBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AllVersionsExcept"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(andOlderLabel, 0);
        data.right = new FormAttachment(66, -ITabbedPropertyConstants.HSPACE + 20);
        data.top = new FormAttachment(andOlderLabel, 0, SWT.TOP);
        versionExceptBtn.setLayoutData(data);

        final Text versionExceptText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionExceptBtn, 0);
        data.right = new FormAttachment(90, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionExceptBtn, 0, SWT.TOP);
        versionExceptText.setLayoutData(data);
        versionExceptText.setEnabled(false);

        //
        final Button versionNewerBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.Version"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(17, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 30);
        data.top = new FormAttachment(versionOlderBtn, ITabbedPropertyConstants.VSPACE + 5);
        versionNewerBtn.setLayoutData(data);

        final Text versionNewerText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionNewerBtn, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionNewerBtn, 0, SWT.TOP);
        versionNewerText.setLayoutData(data);
        versionNewerText.setEnabled(false);

        CLabel andNewerLabel = widgetFactory.createCLabel(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AndNewer")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionNewerText, 0);
        data.right = new FormAttachment(50, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionNewerText, 0, SWT.TOP);
        andNewerLabel.setLayoutData(data);

        final Button versionOnlyTheseBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.OnlyTheseVersions"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(andNewerLabel, 0);
        data.right = new FormAttachment(66, -ITabbedPropertyConstants.HSPACE + 20);
        data.top = new FormAttachment(andNewerLabel, 0, SWT.TOP);
        versionOnlyTheseBtn.setLayoutData(data);

        final Text versionOnlyTheseText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOnlyTheseBtn, 0);
        data.right = new FormAttachment(90, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOnlyTheseBtn, 0, SWT.TOP);
        versionOnlyTheseText.setLayoutData(data);
        versionOnlyTheseText.setEnabled(false);

        CLabel compatibility = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.Compatibility")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(compatibilityGroup, 0, SWT.TOP);
        compatibility.setLayoutData(data);

        // Description
        final Text descriptionText = widgetFactory.createText(composite, "", SWT.MULTI | SWT.V_SCROLL); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(compatibilityGroup, ITabbedPropertyConstants.VSPACE);
        data.height = 4 * descriptionText.getLineHeight();
        descriptionText.setLayoutData(data);

        CLabel descriptionLabel = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.Description")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(descriptionText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(descriptionText, 0, SWT.TOP);
        descriptionLabel.setLayoutData(data);

        // // Visibility
        //        Group visibilityGroup = widgetFactory.createGroup(composite, ""); //$NON-NLS-1$
        // data = new FormData();
        // data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        // data.right = new FormAttachment(100, 0);
        // data.top = new FormAttachment(descriptionText, 0);
        // visibilityGroup.setLayoutData(data);
        // FormLayout visibilityGroupLayout = new FormLayout();
        // visibilityGroup.setLayout(visibilityGroupLayout);
        //
        // Button visibilityPublicBtn = widgetFactory.createButton(visibilityGroup,
        // Messages.getString("MyExtensionsComposite.Form.Visibility.Public"), SWT.RADIO);
        // data = new FormData();
        // data.left = new FormAttachment(0, 0);
        // data.right = new FormAttachment(100, 0);
        // data.top = new FormAttachment(0, 0);
        // visibilityPublicBtn.setLayoutData(data);
        //
        // Button visibilityPrivateBtn = widgetFactory.createButton(visibilityGroup,
        // Messages.getString("MyExtensionsComposite.Form.Visibility.Private"), SWT.RADIO);
        // data = new FormData();
        // data.left = new FormAttachment(0, 0);
        // data.right = new FormAttachment(100, 0);
        // data.top = new FormAttachment(visibilityPublicBtn, ITabbedPropertyConstants.VSPACE + 5);
        // visibilityPrivateBtn.setLayoutData(data);
        //
        //        CLabel visibility = widgetFactory.createCLabel(composite, Messages.getString("MyExtensionsComposite.Form.Visibility")); //$NON-NLS-1$
        // data = new FormData();
        // data.left = new FormAttachment(0, 0);
        // data.right = new FormAttachment(visibilityGroup, -ITabbedPropertyConstants.HSPACE);
        // data.top = new FormAttachment(visibilityGroup, 0, SWT.TOP);
        // visibility.setLayoutData(data);

        // Upload
        Group uploadGroup = widgetFactory.createGroup(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(descriptionText, 0);
        uploadGroup.setLayoutData(data);
        FormLayout uploadGroupLayout = new FormLayout();
        uploadGroup.setLayout(uploadGroupLayout);

        Button uploadSelectItemBtn = widgetFactory.createButton(uploadGroup,
                Messages.getString("MyExtensionsComposite.Form.Upload.SelectAnItem"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        uploadSelectItemBtn.setLayoutData(data);

        Button uploadFileBtn = widgetFactory.createButton(uploadGroup,
                Messages.getString("MyExtensionsComposite.Form.Upload.File"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(15, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 20);
        data.top = new FormAttachment(uploadSelectItemBtn, ITabbedPropertyConstants.VSPACE);
        uploadFileBtn.setLayoutData(data);

        selectUploadFileText = widgetFactory.createText(uploadGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(uploadFileBtn, 0);
        data.right = new FormAttachment(50, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(uploadFileBtn, 0, SWT.TOP);
        selectUploadFileText.setLayoutData(data);

        browseUploadFileBtn = widgetFactory.createButton(uploadGroup,
                Messages.getString("MyExtensionsComposite.Form.Upload.Browse"), SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(selectUploadFileText, 0);
        data.right = new FormAttachment(60, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(selectUploadFileText, 0, SWT.TOP);
        browseUploadFileBtn.setLayoutData(data);
        browseUploadFileBtn.setEnabled(false);

        CLabel uploadLabel = widgetFactory.createCLabel(composite, Messages.getString("MyExtensionsComposite.Form.Upload")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(uploadGroup, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(uploadGroup, 0, SWT.TOP);
        uploadLabel.setLayoutData(data);

        final Button opateBtn = widgetFactory.createButton(composite,
                Messages.getString("MyExtensionsComposite.Form.UploadExtension"), SWT.CENTER);
        data = new FormData();
        data.left = new FormAttachment(80, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(uploadGroup, ITabbedPropertyConstants.VSPACE);
        opateBtn.setLayoutData(data);

        formScroll.setMinSize(formComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        // event
        returnBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                stackLayout.topControl = itemTable;
                listMyExtensonsComp.layout();
                addNewExtensonBtn.setEnabled(true);
            }
        });

        uploadSelectItemBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                browseUploadFileBtn.setEnabled(false);
            }

        });

        uploadFileBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                browseUploadFileBtn.setEnabled(true);
            }

        });

        //
        browseUploadFileBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleLocationFileButtonPressed();
            }

        });

        allVersionsButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(true);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionOlderBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(true);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(true);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionNewerBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(true);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(true);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionExceptBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(true);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(true);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionOnlyTheseBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(true);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(true);
            }
        });

        opateBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ComponentExtension extension = ExchangeFactory.eINSTANCE.createComponentExtension();
                String extensionTitleTextStr = extensionTitleText.getText();
                extension.setLabel(extensionTitleTextStr != null ? extensionTitleTextStr : "");
                String versionTextStr = versionText.getText();
                extension.setLastVersionAvailable(versionTextStr != null ? versionTextStr : "");
                if (allVersionsButton.getSelection()) {
                    listVersionCompatibles = filterVersionRevisionsToString("all", null);
                } else if (versionOlderBtn.getSelection()) {
                    listVersionCompatibles = filterVersionRevisionsToString("older", versionOlderText.getText());
                } else if (versionNewerBtn.getSelection()) {
                    listVersionCompatibles = filterVersionRevisionsToString("newer", versionNewerText.getText());
                } else if (versionExceptBtn.getSelection()) {
                    listVersionCompatibles = filterVersionRevisionsToString("except", versionExceptText.getText());
                } else if (versionOnlyTheseBtn.getSelection()) {
                    listVersionCompatibles = filterVersionRevisionsToString("only", versionOnlyTheseText.getText());
                }
                extension.setListVersionCompatibles(listVersionCompatibles);
                String descriptionStr = descriptionText.getText();
                extension.setDescription(descriptionStr != null ? descriptionStr : "");
                String fileNameTextStr = selectUploadFileText.getText();
                extension.setFilename(fileNameTextStr != null ? fileNameTextStr : "");
                setSelectedExtension(extension);
                InsertionExtensionAction uploadAction = new InsertionExtensionAction();
                if (uploadAction != null) {
                    uploadAction.run();
                }
                opateBtn.setEnabled(false);
            }

        });

    }

    /**
     * 
     * DOC hcyi Comment method "createUpdateFormComposite".
     */
    public void createUpdateFormComposite(int operateStatus, final ComponentExtension componentExtension) {

        //
        if (operateStatus == 2) {
            enableControl = true;
        }
        ScrolledComposite formScroll = new ScrolledComposite(formComp, SWT.V_SCROLL | SWT.H_SCROLL);
        formScroll.setExpandHorizontal(true);
        formScroll.setExpandVertical(true);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        formScroll.setLayoutData(data);

        Composite composite = new Composite(formScroll, SWT.NONE);
        formScroll.setContent(composite);
        formScroll.setLayout(new FormLayout());
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        composite.setLayoutData(data);
        composite.setLayout(new FormLayout());

        Button returnBtn = widgetFactory.createButton(composite, Messages.getString("MyExtensionsComposite.Form.Return"),
                SWT.CENTER);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(10, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        returnBtn.setLayoutData(data);

        // version Text
        final Text versionText = widgetFactory.createText(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(50, 0);
        data.top = new FormAttachment(returnBtn, ITabbedPropertyConstants.VSPACE);
        versionText.setLayoutData(data);
        String version = componentExtension.getLastVersionAvailable();
        versionText.setText(version != null ? version : ""); //$NON-NLS-1$

        CLabel initialVersion = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.InitialVersion")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionText, 0, SWT.CENTER);
        initialVersion.setLayoutData(data);

        // Compatibility
        Group compatibilityGroup = widgetFactory.createGroup(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(versionText, ITabbedPropertyConstants.VSPACE);
        compatibilityGroup.setLayoutData(data);
        FormLayout compatibilityGroupLayout = new FormLayout();
        compatibilityGroup.setLayout(compatibilityGroupLayout);

        final Button allVersionsButton = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AllVersions"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        allVersionsButton.setLayoutData(data);

        final Button versionOlderBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.Version"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(17, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 30);
        data.top = new FormAttachment(allVersionsButton, ITabbedPropertyConstants.VSPACE + 5);
        versionOlderBtn.setLayoutData(data);

        final Text versionOlderText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOlderBtn, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOlderBtn, 0, SWT.TOP);
        versionOlderText.setLayoutData(data);
        versionOlderText.setEnabled(false);

        CLabel andOlderLabel = widgetFactory.createCLabel(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AndOlder")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOlderText, 0);
        data.right = new FormAttachment(50, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOlderText, 0, SWT.TOP);
        andOlderLabel.setLayoutData(data);

        final Button versionExceptBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AllVersionsExcept"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(andOlderLabel, 0);
        data.right = new FormAttachment(66, -ITabbedPropertyConstants.HSPACE + 20);
        data.top = new FormAttachment(andOlderLabel, 0, SWT.TOP);
        versionExceptBtn.setLayoutData(data);

        final Text versionExceptText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionExceptBtn, 0);
        data.right = new FormAttachment(90, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionExceptBtn, 0, SWT.TOP);
        versionExceptText.setLayoutData(data);
        versionExceptText.setEnabled(false);

        //
        final Button versionNewerBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.Version"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(17, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 30);
        data.top = new FormAttachment(versionOlderBtn, ITabbedPropertyConstants.VSPACE + 5);
        versionNewerBtn.setLayoutData(data);

        final Text versionNewerText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionNewerBtn, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionNewerBtn, 0, SWT.TOP);
        versionNewerText.setLayoutData(data);
        versionNewerText.setEnabled(false);

        CLabel andNewerLabel = widgetFactory.createCLabel(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AndNewer")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionNewerText, 0);
        data.right = new FormAttachment(50, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionNewerText, 0, SWT.TOP);
        andNewerLabel.setLayoutData(data);

        final Button versionOnlyTheseBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.OnlyTheseVersions"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(andNewerLabel, 0);
        data.right = new FormAttachment(66, -ITabbedPropertyConstants.HSPACE + 20);
        data.top = new FormAttachment(andNewerLabel, 0, SWT.TOP);
        versionOnlyTheseBtn.setLayoutData(data);

        final Text versionOnlyTheseText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOnlyTheseBtn, 0);
        data.right = new FormAttachment(90, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOnlyTheseBtn, 0, SWT.TOP);
        versionOnlyTheseText.setLayoutData(data);
        versionOnlyTheseText.setEnabled(false);

        CLabel compatibility = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.Compatibility")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(compatibilityGroup, 0, SWT.TOP);
        compatibility.setLayoutData(data);

        // Upload
        Group uploadGroup = widgetFactory.createGroup(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(compatibilityGroup, ITabbedPropertyConstants.VSPACE);
        uploadGroup.setLayoutData(data);
        FormLayout uploadGroupLayout = new FormLayout();
        uploadGroup.setLayout(uploadGroupLayout);

        Button uploadSelectItemBtn = widgetFactory.createButton(uploadGroup,
                Messages.getString("MyExtensionsComposite.Form.Upload.SelectAnItem"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        uploadSelectItemBtn.setLayoutData(data);

        Button uploadFileBtn = widgetFactory.createButton(uploadGroup,
                Messages.getString("MyExtensionsComposite.Form.Upload.File"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(15, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 20);
        data.top = new FormAttachment(uploadSelectItemBtn, ITabbedPropertyConstants.VSPACE + 5);
        uploadFileBtn.setLayoutData(data);

        selectUploadFileText = widgetFactory.createText(uploadGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(uploadFileBtn, 0);
        data.right = new FormAttachment(50, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(uploadFileBtn, 0, SWT.TOP);
        selectUploadFileText.setLayoutData(data);

        browseUploadFileBtn = widgetFactory.createButton(uploadGroup,
                Messages.getString("MyExtensionsComposite.Form.Upload.Browse"), SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(selectUploadFileText, 0);
        data.right = new FormAttachment(60, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(selectUploadFileText, 0, SWT.TOP);
        browseUploadFileBtn.setLayoutData(data);
        browseUploadFileBtn.setEnabled(false);

        CLabel uploadLabel = widgetFactory.createCLabel(composite, Messages.getString("MyExtensionsComposite.Form.Upload")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(uploadGroup, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(uploadGroup, 0, SWT.TOP);
        uploadLabel.setLayoutData(data);

        final Button opateBtn = widgetFactory.createButton(composite,
                Messages.getString("MyExtensionsComposite.Form.UpdateExtension"), SWT.CENTER);
        data = new FormData();
        data.left = new FormAttachment(80, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(uploadGroup, ITabbedPropertyConstants.VSPACE);
        opateBtn.setLayoutData(data);

        formScroll.setMinSize(formComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        // event
        returnBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                stackLayout.topControl = itemTable;
                listMyExtensonsComp.layout();
                addNewExtensonBtn.setEnabled(true);
            }
        });

        uploadSelectItemBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                browseUploadFileBtn.setEnabled(false);
            }

        });

        uploadFileBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                browseUploadFileBtn.setEnabled(true);
            }

        });

        //
        browseUploadFileBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleLocationFileButtonPressed();
            }

        });

        allVersionsButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(true);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionOlderBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(true);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(true);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionNewerBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(true);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(true);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionExceptBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(true);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(true);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionOnlyTheseBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(true);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(true);
            }
        });

        opateBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ComponentExtension extension = null;
                if (componentExtension != null) {
                    extension = componentExtension;
                    String versionTextStr = versionText.getText();
                    extension.setLastVersionAvailable(versionTextStr != null ? versionTextStr : "");
                    if (allVersionsButton.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("all", null);
                    } else if (versionOlderBtn.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("older", versionOlderText.getText());
                    } else if (versionNewerBtn.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("newer", versionNewerText.getText());
                    } else if (versionExceptBtn.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("except", versionExceptText.getText());
                    } else if (versionOnlyTheseBtn.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("only", versionOnlyTheseText.getText());
                    }

                    extension.setListVersionCompatibles(listVersionCompatibles);
                    String fileNameTextStr = selectUploadFileText.getText();
                    extension.setFilename(fileNameTextStr != null ? fileNameTextStr : "");
                    setSelectedExtension(extension);
                    UploadRevisionAction uploadAction = new UploadRevisionAction();
                    if (uploadAction != null) {
                        uploadAction.run();
                    }
                }
                opateBtn.setEnabled(false);
            }

        });

    }

    /**
     * 
     * DOC hcyi Comment method "createModifyFormComposite".
     */
    public void createModifyFormComposite(int operateStatus, final ComponentExtension componentExtension) {

        //
        if (operateStatus == 2) {
            enableControl = true;
        }
        ScrolledComposite formScroll = new ScrolledComposite(formComp, SWT.V_SCROLL | SWT.H_SCROLL);
        formScroll.setExpandHorizontal(true);
        formScroll.setExpandVertical(true);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        formScroll.setLayoutData(data);

        Composite composite = new Composite(formScroll, SWT.NONE);
        formScroll.setContent(composite);
        formScroll.setLayout(new FormLayout());
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        composite.setLayoutData(data);
        composite.setLayout(new FormLayout());

        Button returnBtn = widgetFactory.createButton(composite, Messages.getString("MyExtensionsComposite.Form.Return"),
                SWT.CENTER);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(10, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        returnBtn.setLayoutData(data);

        // Extension Title
        Text extensionTitleText = widgetFactory.createText(composite, ""); //$NON-NLS-1$
        extensionTitleText.setEnabled(false);
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(50, 0);
        data.top = new FormAttachment(returnBtn, ITabbedPropertyConstants.VSPACE);
        extensionTitleText.setLayoutData(data);
        String title = componentExtension.getLabel();
        extensionTitleText.setText(title != null ? title : ""); //$NON-NLS-1$
        extensionTitleText.setEnabled(enableControl);

        CLabel extensionTitle = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.ExtensionTitle")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(extensionTitleText, 0, SWT.CENTER);
        extensionTitle.setLayoutData(data);
        extensionTitle.setEnabled(enableControl);

        // Compatibility
        Group compatibilityGroup = widgetFactory.createGroup(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(extensionTitleText, ITabbedPropertyConstants.VSPACE);
        compatibilityGroup.setLayoutData(data);
        FormLayout compatibilityGroupLayout = new FormLayout();
        compatibilityGroup.setLayout(compatibilityGroupLayout);

        final Button allVersionsButton = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AllVersions"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        allVersionsButton.setLayoutData(data);

        final Button versionOlderBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.Version"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(17, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 30);
        data.top = new FormAttachment(allVersionsButton, ITabbedPropertyConstants.VSPACE + 5);
        versionOlderBtn.setLayoutData(data);

        final Text versionOlderText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOlderBtn, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOlderBtn, 0, SWT.TOP);
        versionOlderText.setLayoutData(data);
        versionOlderText.setEnabled(false);

        CLabel andOlderLabel = widgetFactory.createCLabel(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AndOlder")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOlderText, 0);
        data.right = new FormAttachment(50, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOlderText, 0, SWT.TOP);
        andOlderLabel.setLayoutData(data);

        final Button versionExceptBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AllVersionsExcept"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(andOlderLabel, 0);
        data.right = new FormAttachment(66, -ITabbedPropertyConstants.HSPACE + 20);
        data.top = new FormAttachment(andOlderLabel, 0, SWT.TOP);
        versionExceptBtn.setLayoutData(data);

        final Text versionExceptText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionExceptBtn, 0);
        data.right = new FormAttachment(90, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionExceptBtn, 0, SWT.TOP);
        versionExceptText.setLayoutData(data);
        versionExceptText.setEnabled(false);

        //
        final Button versionNewerBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.Version"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(17, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 30);
        data.top = new FormAttachment(versionOlderBtn, ITabbedPropertyConstants.VSPACE + 5);
        versionNewerBtn.setLayoutData(data);

        final Text versionNewerText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionNewerBtn, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionNewerBtn, 0, SWT.TOP);
        versionNewerText.setLayoutData(data);
        versionNewerText.setEnabled(false);

        CLabel andNewerLabel = widgetFactory.createCLabel(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.AndNewer")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionNewerText, 0);
        data.right = new FormAttachment(50, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionNewerText, 0, SWT.TOP);
        andNewerLabel.setLayoutData(data);

        final Button versionOnlyTheseBtn = widgetFactory.createButton(compatibilityGroup,
                Messages.getString("MyExtensionsComposite.Form.Compatibility.OnlyTheseVersions"), SWT.RADIO);
        data = new FormData();
        data.left = new FormAttachment(andNewerLabel, 0);
        data.right = new FormAttachment(66, -ITabbedPropertyConstants.HSPACE + 20);
        data.top = new FormAttachment(andNewerLabel, 0, SWT.TOP);
        versionOnlyTheseBtn.setLayoutData(data);

        final Text versionOnlyTheseText = widgetFactory.createText(compatibilityGroup, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(versionOnlyTheseBtn, 0);
        data.right = new FormAttachment(90, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(versionOnlyTheseBtn, 0, SWT.TOP);
        versionOnlyTheseText.setLayoutData(data);
        versionOnlyTheseText.setEnabled(false);

        CLabel compatibility = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.Compatibility")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(30, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(compatibilityGroup, 0, SWT.TOP);
        compatibility.setLayoutData(data);

        // Description
        final Text descriptionText = widgetFactory.createText(composite, "", SWT.MULTI | SWT.V_SCROLL); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(compatibilityGroup, ITabbedPropertyConstants.VSPACE);
        data.height = 4 * descriptionText.getLineHeight();
        descriptionText.setLayoutData(data);

        String description = componentExtension.getDescription();
        descriptionText.setText(description != null ? description : ""); //$NON-NLS-1$

        CLabel descriptionLabel = widgetFactory.createCLabel(composite,
                Messages.getString("MyExtensionsComposite.Form.Description")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(descriptionText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(descriptionText, 0, SWT.TOP);
        descriptionLabel.setLayoutData(data);

        // // Visibility
        //        Group visibilityGroup = widgetFactory.createGroup(composite, ""); //$NON-NLS-1$
        // data = new FormData();
        // data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        // data.right = new FormAttachment(100, 0);
        // data.top = new FormAttachment(descriptionText, 0);
        // visibilityGroup.setLayoutData(data);
        // FormLayout visibilityGroupLayout = new FormLayout();
        // visibilityGroup.setLayout(visibilityGroupLayout);
        //
        // Button visibilityPublicBtn = widgetFactory.createButton(visibilityGroup,
        // Messages.getString("MyExtensionsComposite.Form.Visibility.Public"), SWT.RADIO);
        // data = new FormData();
        // data.left = new FormAttachment(0, 0);
        // data.right = new FormAttachment(30, 0);
        // data.top = new FormAttachment(0, 0);
        // visibilityPublicBtn.setLayoutData(data);
        //
        // Button visibilityPrivateBtn = widgetFactory.createButton(visibilityGroup,
        // Messages.getString("MyExtensionsComposite.Form.Visibility.Private"), SWT.RADIO);
        // data = new FormData();
        // data.left = new FormAttachment(0, 0);
        // data.right = new FormAttachment(30, 0);
        // data.top = new FormAttachment(visibilityPublicBtn, ITabbedPropertyConstants.VSPACE + 5);
        // visibilityPrivateBtn.setLayoutData(data);
        //
        //        CLabel visibility = widgetFactory.createCLabel(composite, Messages.getString("MyExtensionsComposite.Form.Visibility")); //$NON-NLS-1$
        // data = new FormData();
        // data.left = new FormAttachment(0, 0);
        // data.right = new FormAttachment(visibilityGroup, -ITabbedPropertyConstants.HSPACE);
        // data.top = new FormAttachment(visibilityGroup, 0, SWT.TOP);
        // visibility.setLayoutData(data);

        final Button opateBtn = widgetFactory.createButton(composite,
                Messages.getString("MyExtensionsComposite.Form.ModifyExtension"), SWT.CENTER);
        data = new FormData();
        data.left = new FormAttachment(80, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(descriptionText, ITabbedPropertyConstants.VSPACE);
        opateBtn.setLayoutData(data);

        formScroll.setMinSize(formComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        // event
        returnBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                stackLayout.topControl = itemTable;
                listMyExtensonsComp.layout();
                addNewExtensonBtn.setEnabled(true);
            }
        });

        allVersionsButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(true);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionOlderBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(true);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(true);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionNewerBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(true);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(true);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionExceptBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(true);
                versionOnlyTheseBtn.setSelection(false);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(true);
                versionOnlyTheseText.setEnabled(false);
            }
        });

        versionOnlyTheseBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                allVersionsButton.setSelection(false);
                versionOlderBtn.setSelection(false);
                versionNewerBtn.setSelection(false);
                versionExceptBtn.setSelection(false);
                versionOnlyTheseBtn.setSelection(true);
                versionOlderText.setEnabled(false);
                versionNewerText.setEnabled(false);
                versionExceptText.setEnabled(false);
                versionOnlyTheseText.setEnabled(true);
            }
        });

        opateBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ComponentExtension extension = null;
                if (componentExtension != null) {
                    extension = componentExtension;
                    String descriptionStr = descriptionText.getText();
                    extension.setDescription(descriptionStr != null ? descriptionStr : "");
                    if (allVersionsButton.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("all", null);
                    } else if (versionOlderBtn.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("older", versionOlderText.getText());
                    } else if (versionNewerBtn.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("newer", versionNewerText.getText());
                    } else if (versionExceptBtn.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("except", versionExceptText.getText());
                    } else if (versionOnlyTheseBtn.getSelection()) {
                        listVersionCompatibles = filterVersionRevisionsToString("only", versionOnlyTheseText.getText());
                    }

                    extension.setListVersionCompatibles(listVersionCompatibles);
                    setSelectedExtension(extension);
                    ModifyExtensionAction modifyAction = new ModifyExtensionAction();
                    if (modifyAction != null) {
                        modifyAction.run();
                    }
                }
                opateBtn.setEnabled(false);
            }

        });

    }

    private void handleLocationFileButtonPressed() {
        FileDialog dialog = new FileDialog(selectUploadFileText.getShell(), SWT.SAVE);
        dialog.setFilterExtensions(FILE_EXPORT_MASK);
        dialog.setText(Messages.getString("MyExtensionsComposite.Form.Upload.Browse.SelectFile"));

        String fileName = selectUploadFileText.getText().trim();
        if (fileName.length() == 0) {
            fileName = previouslyBrowsedArchive;
        }

        if (fileName.length() == 0) {
            dialog.setFilterPath(IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getLocation().toOSString());
        } else {
            File path = new File(fileName);
            if (path.exists()) {
                dialog.setFilterPath(new Path(fileName).toOSString());
            }
        }

        String selectedArchive = dialog.open();
        if (selectedArchive != null) {
            previouslyBrowsedArchive = selectedArchive;
            selectUploadFileText.setText(previouslyBrowsedArchive);
        }

    }

    private String[] getStatusItemCombos() {
        String[] res = new String[3];
        res[0] = Messages.getString("MyExtensionsComposite.UpLoadNewVersionOperateStatus");
        res[1] = Messages.getString("MyExtensionsComposite.ModifyOperateStatus");
        res[2] = Messages.getString("MyExtensionsComposite.DeleteOperateStatus");
        return res;
    }

    private void getVersionRevisionsAndCategorys() {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                fVersionRevisions.clear();
                fVersionRevisions = ExchangeWebService.searchVersionRevisionJSONArray(ExchangeUtils.TYPEEXTENSION);
                fCategorys.clear();
                fCategorys = ExchangeWebService.searchCategoryExtensionJSONArray(ExchangeUtils.TYPEEXTENSION);
            }
        });
    }

    private String filterVersionRevisionsToString(String status, String version) {
        StringBuffer sb = new StringBuffer();
        if (status.equals("all")) {
            for (VersionRevision ver : fVersionRevisions) {
                sb.append(ver.getVersionId());
                sb.append(",");
            }
            return sb.substring(0, sb.length() - 1);
        } else if (status.equals("older")) {
            for (VersionRevision ver : fVersionRevisions) {
                if (ver.getVersionName().equalsIgnoreCase(version)) {
                    break;
                }
                sb.append(ver.getVersionId());
                sb.append(",");
            }
            return sb.substring(0, sb.length() - 1);
        } else if (status.equals("newer")) {
            boolean newerb = false;
            for (VersionRevision ver : fVersionRevisions) {
                if (ver.getVersionName().equalsIgnoreCase(version)) {
                    newerb = true;
                }
                if (newerb) {
                    sb.append(ver.getVersionId());
                    sb.append(",");
                }

            }
            return sb.substring(0, sb.length() - 1);
        } else if (status.equals("except")) {
            for (VersionRevision ver : fVersionRevisions) {
                if (!ver.getVersionName().equalsIgnoreCase(version)) {
                    sb.append(ver.getVersionId());
                    sb.append(",");
                }
            }
            return sb.substring(0, sb.length() - 1);
        } else if (status.equals("only")) {
            for (VersionRevision ver : fVersionRevisions) {
                if (ver.getVersionName().equalsIgnoreCase(version)) {
                    sb.append(ver.getVersionId());
                    break;
                }
            }
            return sb.toString();
        }

        return "";
    }

    private void removeItemElement(ComponentExtension object) {
        itemTable.setRedraw(false);
        TableItem[] items = itemTable.getItems();
        for (TableItem item : items) {
            if (item.getData() != null && item.getData() instanceof ComponentExtension) {
                ComponentExtension itemObject = (ComponentExtension) item.getData();
                if (itemObject != null && itemObject.getIdExtension().equals(object.getIdExtension())) {
                    removeTableItem(item);
                }
            }
        }
        itemTable.setRedraw(true);
    }

    private void removeTableItem(TableItem item) {
        if (item == null) {
            return;
        }
        item.dispose();
    }

    /**
     * 
     * DOC hcyi Comment method "addOperateMenu".
     */
    private void addOperateMenu(final TableItem item) {
        if (item != null) {
            setSelectedExtension((ComponentExtension) item.getData());
        }
        //
        FormLayout layout = new FormLayout();
        setLayout(layout);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        setLayoutData(data);
        formComp = widgetFactory.createFlatFormComposite(listMyExtensonsComp);
        formComp.setLayoutData(data);
        formComp.setLayout(new FormLayout());
        addNewExtensonBtn.setEnabled(false);
        //
        initImages();
        final String[] res = getStatusItemCombos();
        Menu menu = new Menu(itemTable.getShell(), SWT.POP_UP);
        MenuItem uploadMenu = new MenuItem(menu, SWT.PUSH);
        uploadMenu.setText(res[0]);
        uploadMenu.setImage(imageMaps.get(1));
        uploadMenu.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {

                getFormComposite(0, getSelectedExtension());
                stackLayout.topControl = formComp;
                listMyExtensonsComp.layout();

            }
        });
        MenuItem modifyMenu = new MenuItem(menu, SWT.PUSH);
        modifyMenu.setText(res[1]);
        modifyMenu.setImage(imageMaps.get(2));
        modifyMenu.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                getFormComposite(1, getSelectedExtension());
                stackLayout.topControl = formComp;
                listMyExtensonsComp.layout();
            }
        });
        MenuItem deleteMenu = new MenuItem(menu, SWT.PUSH);
        deleteMenu.setText(res[2]);
        deleteMenu.setImage(imageMaps.get(3));
        deleteMenu.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                final DeleteExtensionAction delAction = new DeleteExtensionAction();
                if (delAction != null) {
                    delAction.run();
                }
                addNewExtensonBtn.setEnabled(true);
            }
        });

        itemTable.setMenu(menu);
    }

    private void initImages() {
        imageMaps.put(1, ExchangePlugin.getImageDescriptor("icons/upload.gif").createImage());
        imageMaps.put(2, ExchangePlugin.getImageDescriptor("icons/modify.gif").createImage());
        imageMaps.put(3, ExchangePlugin.getImageDescriptor("icons/delete.gif").createImage());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getSection()
     */
    public EComponentCategory getSection() {
        return EComponentCategory.MYEXTENSIONS;
    }

    public ISelection getSelection() {
        return this.selection;
    }

    public IWizardPage getParentWizard() {
        return this.wizardPage;
    }

    public void setParentWizard(IWizardPage parentWizard) {
        this.wizardPage = parentWizard;
    }

}
