// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.composite.ThreeCompositesSashForm;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ComponentSetting;
import org.talend.designer.components.preference.labelformat.TalendPaletteLabelProvider;
import org.talend.designer.components.preference.labelformat.TalendPaletteTreeProvider;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class PaletteSettingsDialog extends Dialog {

    private static final String DIALOG_TITLE = "Palette Settings";

    private TreeViewer hiddenViewer, displayViewer;

    private Project project;

    private Button leftButton;

    private Button rightButton;

    private ThreeCompositesSashForm compositesSachForm;

    private Map<String, Boolean> statusBackup = new HashMap<String, Boolean>();

    /**
     * DOC qwei PaletteSettingsDialog constructor comment.
     * 
     * @param parentShell
     */

    protected PaletteSettingsDialog(Shell parentShell, Project pro) {
        super(parentShell);
        this.project = pro;
        // IComponentsService service = (IComponentsService)
        // GlobalServiceRegister.getDefault().getService(IComponentsService.class);
        // List<IComponent> components = service.getComponentsFactory().getComponents();
        // for (IComponent component : components) {
        // System.out.println(component.isVisible() + " " + component.getName() + " " + component.getFamily());
        // }
        //
        // System.out.println("----------------------");
        List<ComponentSetting> c = getComponentsFromProject();
        for (ComponentSetting componentSetting : c) {
            statusBackup.put(componentSetting.getName(), !componentSetting.isHidden());
            System.out.println(!componentSetting.isHidden() + " " + componentSetting.getName());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(DIALOG_TITLE);
        newShell.setSize(800, 600);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        compositesSachForm = new ThreeCompositesSashForm(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginBottom = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        addTreeViewer(compositesSachForm);
        // addFileds(composite);
        return composite;
    }

    private PaletteRoot getViewerInput() {
        IComponentsFactory components = ComponentsFactoryProvider.getInstance();
        PaletteRoot paletteRoot = CorePlugin.getDefault().getDesignerCoreService().getAllNodeStructure(components);
        return paletteRoot;
    }

    /**
     * qwei Comment method "addViewer".
     * 
     * @param parent
     */
    private void addTreeViewer(ThreeCompositesSashForm parent) {

        PaletteRoot input = getViewerInput();

        hiddenViewer = new TreeViewer(parent.getLeftComposite());
        hiddenViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        hiddenViewer.setContentProvider(new TalendPaletteTreeProvider());
        hiddenViewer.setLabelProvider(new TalendPaletteLabelProvider());
        hiddenViewer.addFilter(getFilterForComponent(false));
        hiddenViewer.setInput(input);
        hiddenViewer.expandToLevel(2);
        hiddenViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                rightButton.setEnabled(!event.getSelection().isEmpty());
            }
        });
        createButtons(parent.getMidComposite());
        displayViewer = new TreeViewer(parent.getRightComposite());
        displayViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        displayViewer.setContentProvider(new TalendPaletteTreeProvider());
        displayViewer.setLabelProvider(new TalendPaletteLabelProvider());
        displayViewer.addFilter(getFilterForComponent(true));
        displayViewer.setInput(input);
        displayViewer.expandToLevel(2);
        displayViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                leftButton.setEnabled(!event.getSelection().isEmpty());
            }
        });
    }

    /**
     * DOC qwei Comment method "getFilterForHiddenComponent".
     * 
     * @return
     */
    private ViewerFilter getFilterForComponent(final boolean isVisible) {
        ViewerFilter filter = new ViewerFilter() {

            public boolean select(Viewer viewer, Object parentElement, Object element) {
                // PaletteComponentFactory

                PaletteEntry entry = (PaletteEntry) element;
                if (entry instanceof PaletteContainer) {
                    return isFolderVisible((PaletteContainer) entry, isVisible);
                    // return true;
                }

                if (isVisible) {
                    return isComponentVisible(entry.getLabel());
                } else {
                    return !isComponentVisible(entry.getLabel());
                }

                // if (entry instanceof PaletteContainer) {
                // } else if (entry instanceof PaletteSeparator) {
                // } else {
                // }
                // return true;

            }
        };
        return filter;
    }

    /**
     * Check if this folder needs to be showed. *
     * 
     * @param entry
     * @param isVisible
     * @return
     */
    protected boolean isFolderVisible(PaletteContainer container, boolean isVisible) {
        for (Iterator iterator = container.getChildren().iterator(); iterator.hasNext();) {
            PaletteEntry entry = (PaletteEntry) iterator.next();
            if (entry instanceof PaletteContainer) {
                return isFolderVisible((PaletteContainer) entry, isVisible);
            } else {
                if (isVisible) {
                    if (isComponentVisible(entry.getLabel())) {
                        return true;
                    }
                } else {
                    if (!isComponentVisible(entry.getLabel())) {
                        return true;
                    }
                }
                continue;

            }
        }

        // // right viewer
        // if (isVisible) {
        // for (Iterator iterator = container.getChildren().iterator(); iterator.hasNext();) {
        // PaletteEntry entry = (PaletteEntry) iterator.next();
        // if (entry instanceof PaletteContainer) {
        // return isFolderVisible((PaletteContainer) entry, isVisible);
        // } else {
        // if (isComponentVisible(entry.getLabel())) {
        // return true;
        // }
        // continue;
        // }
        // }
        // } else {
        // // left viewer
        // for (Iterator iterator = container.getChildren().iterator(); iterator.hasNext();) {
        // PaletteEntry entry = (PaletteEntry) iterator.next();
        // if (entry instanceof PaletteContainer) {
        // return isFolderVisible((PaletteContainer) entry, isVisible);
        // } else {
        // if (!isComponentVisible(entry.getLabel())) {
        // return true;
        // }
        // continue;
        // }
        // }
        // }
        return false;
    }

    private void refreshViewer() {
        hiddenViewer.refresh();
        displayViewer.refresh();
    }

    private void createButtons(Composite parent) {
        Label label1 = new Label(compositesSachForm.getMidComposite(), SWT.NONE);
        GridDataFactory.swtDefaults().hint(42, 18).applyTo(label1);
        Composite buttonComposite = new Composite(compositesSachForm.getMidComposite(), SWT.BORDER);
        Label label2 = new Label(compositesSachForm.getMidComposite(), SWT.NONE);
        GridDataFactory.swtDefaults().hint(42, 0).applyTo(label2);

        GridLayout gridLayout = new GridLayout(1, true);
        buttonComposite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        // gridData.verticalAlignment = GridData.CENTER;
        buttonComposite.setLayoutData(gridData);

        Composite buttonComposite2 = new Composite(buttonComposite, SWT.NONE);

        gridLayout = new GridLayout(1, true);
        gridLayout.marginBottom = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginWidth = 0;
        buttonComposite2.setLayout(gridLayout);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.verticalAlignment = GridData.CENTER;
        buttonComposite2.setLayoutData(gridData);
        rightButton = new Button(buttonComposite2, SWT.NONE);
        rightButton.setImage(ImageProvider.getImage(EImage.RIGHT_ICON));
        rightButton.setToolTipText(""); //$NON-NLS-1$
        GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(rightButton);
        rightButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setComponentVisible(hiddenViewer.getSelection(), true);
                getButton(IDialogConstants.OK_ID).setEnabled(true);

            }
        });
        leftButton = new Button(buttonComposite2, SWT.NONE);
        leftButton.setImage(ImageProvider.getImage(EImage.LEFT_ICON));
        leftButton.setToolTipText(""); //$NON-NLS-1$
        gridData = new GridData();
        gridData.verticalAlignment = GridData.CENTER;
        leftButton.setLayoutData(gridData);
        leftButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setComponentVisible(displayViewer.getSelection(), false);
                getButton(IDialogConstants.OK_ID).setEnabled(true);
            }
        });

        rightButton.setEnabled(false);
        leftButton.setEnabled(false);

    }

    /**
     * set the selected component as visible or not
     * 
     * @param selection
     * @param b
     */
    protected void setComponentVisible(ISelection selection, boolean visible) {
        IStructuredSelection sel = (IStructuredSelection) selection;
        Set<String> names = new HashSet<String>();

        for (Iterator iterator = sel.iterator(); iterator.hasNext();) {
            PaletteEntry entry = (PaletteEntry) iterator.next();
            retreiveAllEntry(names, entry);
        }

        for (String string : names) {
            setComponentVisible(string, visible);
        }

        refreshViewer();
    }

    private void retreiveAllEntry(Set<String> list, PaletteEntry entry) {
        if (entry instanceof PaletteContainer) {
            PaletteContainer container = (PaletteContainer) entry;
            for (Iterator iterator = container.getChildren().iterator(); iterator.hasNext();) {
                PaletteEntry en = (PaletteEntry) iterator.next();
                retreiveAllEntry(list, en);
            }
        } else {
            list.add(entry.getLabel());
        }

    }

    @SuppressWarnings("unchecked")
    public List<ComponentSetting> getComponentsFromProject() {
        ComponentSetting cs;

        List<ComponentSetting> components = (List<ComponentSetting>) project.getEmfProject().getComponentsSettings();
        return components;
    }

    public boolean isComponentVisible(String name) {
        List<ComponentSetting> components = getComponentsFromProject();
        for (ComponentSetting componentSetting : components) {
            if (componentSetting.getName().equals(name)) {
                return !componentSetting.isHidden();
            }
        }

        return true;
    }

    private void setComponentVisible(String name, boolean visible) {
        List<ComponentSetting> components = getComponentsFromProject();
        for (ComponentSetting componentSetting : components) {
            if (componentSetting.getName().equals(name)) {
                componentSetting.setHidden(!visible);
            }
        }
    }

    /**
     * qwei Comment method "getViewer".
     * 
     * @return
     */
    public TreeViewer getViewer() {
        return this.hiddenViewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        super.okPressed();

        IProxyRepositoryFactory prf = CorePlugin.getDefault().getProxyRepositoryFactory();
        try {
            prf.saveProject(project);
            ComponentUtilities.updatePalette();
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
     */
    @Override
    protected void cancelPressed() {
        super.cancelPressed();
        for (Iterator iterator = statusBackup.keySet().iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            setComponentVisible(name, statusBackup.get(name));
        }
    }
}
