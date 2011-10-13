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
package org.talend.designer.components.exchange.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.properties.tab.HorizontalTabFactory;
import org.talend.core.properties.tab.IDynamicProperty;
import org.talend.core.properties.tab.TalendPropertyTabDescriptor;
import org.talend.designer.components.exchange.ExchangePlugin;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.AvailableExtensionsComposite;
import org.talend.designer.components.exchange.ui.DownloadedExtensionsComposite;
import org.talend.designer.components.exchange.ui.MyExtensionsComposite;
import org.talend.designer.components.exchange.ui.actions.ShowContributedExtensionsAction;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class ExchangeView extends ViewPart {

    public static final String ID = ExchangePlugin.PLUGIN_ID + ".ui.views.ExchangeView"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(ExchangeView.class);

    private HorizontalTabFactory tabFactory = null;

    private TalendPropertyTabDescriptor currentSelectedTab;

    private boolean selectedPrimary = true;

    private IDynamicProperty dc = null;

    private AvailableExtensionsComposite availableExtensionsComposite;

    private DownloadedExtensionsComposite downloadedExtensionsComposite;

    private MyExtensionsComposite myExtensionsComposite;

    private List<ComponentExtension> fAvailableExtensions = new ArrayList<ComponentExtension>();

    private List<ComponentExtension> fContributedExtensions = new ArrayList<ComponentExtension>();

    private List<ComponentExtension> fInstalledExtensions = new ArrayList<ComponentExtension>();

    private Image imageView;

    public ExchangeView() {
        tabFactory = new HorizontalTabFactory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        tabFactory.initComposite(parent, false);
        tabFactory.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                TalendPropertyTabDescriptor descriptor = (TalendPropertyTabDescriptor) selection.getFirstElement();

                if (descriptor == null) {
                    return;
                }

                if (currentSelectedTab != null) {
                    if ((currentSelectedTab.getCategory() != descriptor.getCategory())) {
                        for (Control curControl : tabFactory.getTabComposite().getChildren()) {
                            curControl.dispose();
                        }
                    }
                }

                if (currentSelectedTab == null || currentSelectedTab.getCategory() != descriptor.getCategory() || selectedPrimary) {
                    currentSelectedTab = descriptor;
                    createDynamicTabComposite(tabFactory.getTabComposite(), null, descriptor.getCategory());
                    selectedPrimary = false;
                }
            }
        });
        setElement();
    }

    private IDynamicProperty createDynamicTabComposite(Composite parent, Object data, EComponentCategory category) {
        final int style = SWT.NO_FOCUS;
        if (EComponentCategory.AVAILABLEEXTENSIONS.equals(category)) {
            availableExtensionsComposite = new AvailableExtensionsComposite(parent, style, tabFactory.getWidgetFactory(),
                    fAvailableExtensions);
            dc = availableExtensionsComposite;
        } else if (EComponentCategory.DOWNLOADEDEXTENSIONS.equals(category)) {
            downloadedExtensionsComposite = new DownloadedExtensionsComposite(parent, style, tabFactory.getWidgetFactory(),
                    fInstalledExtensions);
            dc = downloadedExtensionsComposite;
        } else if (EComponentCategory.MYEXTENSIONS.equals(category)) {
            myExtensionsComposite = new MyExtensionsComposite(parent, style, tabFactory.getWidgetFactory(),
                    fContributedExtensions);
            dc = myExtensionsComposite;
        }
        if (dc != null) {
            dc.refresh();
        }
        currentSelectedTab.setPropertyComposite(dc);
        return dc;
    }

    private void setElement() {
        EComponentCategory[] categories = getCategories();
        final List<TalendPropertyTabDescriptor> descriptors = new ArrayList<TalendPropertyTabDescriptor>();
        for (EComponentCategory category : categories) {
            //
            if (EComponentCategory.MYEXTENSIONS.equals(category)) {
                ShowContributedExtensionsAction actiond = new ShowContributedExtensionsAction();
                if (actiond != null) {
                    actiond.run();
                }
            }
            TalendPropertyTabDescriptor d = new TalendPropertyTabDescriptor(category);
            descriptors.add(d);
        }

        tabFactory.setInput(descriptors);
        if (imageView == null) {
            imageView = ImageProvider.getImage(ECoreImage.EXCHNAGETAB);
        }
        tabFactory.setTitle("", imageView);
        tabFactory.setSelection(new IStructuredSelection() {

            public Object getFirstElement() {
                return null;
            }

            public Iterator iterator() {
                return null;
            }

            public int size() {
                return 0;
            }

            public Object[] toArray() {
                return null;
            }

            public List toList() {
                List<TalendPropertyTabDescriptor> d = new ArrayList<TalendPropertyTabDescriptor>();

                if (descriptors.size() > 0) {
                    if (currentSelectedTab != null) {
                        for (TalendPropertyTabDescriptor ds : descriptors) {
                            if (ds.getCategory() == currentSelectedTab.getCategory()) {
                                d.add(ds);
                                return d;
                            }
                        }
                    }
                    d.add(descriptors.get(0));
                }
                return d;
            }

            public boolean isEmpty() {
                return false;
            }

        });

    }

    private EComponentCategory[] getCategories() {
        List<EComponentCategory> category = new ArrayList<EComponentCategory>();
        category.add(EComponentCategory.AVAILABLEEXTENSIONS);
        category.add(EComponentCategory.DOWNLOADEDEXTENSIONS);
        category.add(EComponentCategory.MYEXTENSIONS);
        return category.toArray(new EComponentCategory[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
    }

    public ComponentExtension getSelectedExtension() {
        if (currentSelectedTab != null) {
            if (currentSelectedTab.getCategory().equals(EComponentCategory.AVAILABLEEXTENSIONS)) {
                return availableExtensionsComposite.getSelectedExtension();
            } else if (currentSelectedTab.getCategory().equals(EComponentCategory.DOWNLOADEDEXTENSIONS)) {
                return downloadedExtensionsComposite.getSelectedExtension();
            } else if (currentSelectedTab.getCategory().equals(EComponentCategory.MYEXTENSIONS)) {
                return myExtensionsComposite.getSelectedExtension();
            }
        }
        return null;

    }

    public void addDownloadedExtension(ComponentExtension extension) {
        availableExtensionsComposite.addDownloadedExtension(extension);
    }

    public void removeDownloadedExtension(ComponentExtension extension) {
        availableExtensionsComposite.removeDownloadedExtension(extension);
    }

    public void updateAvailableExtensions(List<ComponentExtension> extensions) {
        fAvailableExtensions.clear();
        if (extensions != null && !extensions.isEmpty()) {
            fAvailableExtensions = extensions;
            availableExtensionsComposite.updateAvailableExtensions(fAvailableExtensions);
        }
    }

    public void updateInstalledExtensions(List<ComponentExtension> extensions) {
        fInstalledExtensions.clear();
        if (extensions != null && !extensions.isEmpty()) {
            fInstalledExtensions = extensions;
        }
    }

    public void refresh() {

    }

    /**
     * Sets the fContributedExtensions.
     * 
     * @param fContributedExtensions the fContributedExtensions to set
     */
    public void setfContributedExtensions(List<ComponentExtension> fContributedExtensions) {
        this.fContributedExtensions = fContributedExtensions;
    }

    /**
     * Getter for myExtensionsComposite.
     * 
     * @return the myExtensionsComposite
     */
    public MyExtensionsComposite getMyExtensionsComposite() {
        return this.myExtensionsComposite;
    }

    public void returnAvailableExtensionsCompositeToFirstPage() {
        if (availableExtensionsComposite != null) {
            availableExtensionsComposite.returnToFirstPage();
        }
    }

    public void editAvailableExtensionReviews() {
        if (availableExtensionsComposite != null) {
            availableExtensionsComposite.editReviews();
        }
    }

}
