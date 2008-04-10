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
package org.talend.designer.core.ui.views.jobsettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IProcess;
import org.talend.core.properties.tab.HorizontalTabFactory;
import org.talend.core.properties.tab.TalendPropertyTabDescriptor;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;
import org.talend.designer.core.ui.views.properties.IJobSettingsView;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;
import org.talend.designer.core.ui.views.statsandlogs.StatsAndLogsComposite;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobSettingsView extends ViewPart implements IJobSettingsView {

    public static final String VIEW_NAME = Messages.getString("JobSettingsView.JobSettings"); //$NON-NLS-1$

    public static final String VIEW_NAME_JOBLET = "Joblet Settings";

    private HorizontalTabFactory tabFactory = null;

    private TalendPropertyTabDescriptor currentSelectedTab;

    private Element element;

    private boolean cleaned;

    private boolean selectedPrimary;

    public JobSettingsView() {
        tabFactory = new HorizontalTabFactory();
    }

    @Override
    public void createPartControl(Composite parent) {
        // tabFactory = new HorizontalTabFactory();
        tabFactory.initComposite(parent);
        tabFactory.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                TalendPropertyTabDescriptor descriptor = (TalendPropertyTabDescriptor) selection.getFirstElement();

                if (descriptor == null) {
                    return;
                }

                if (currentSelectedTab != null) {
                    if ((!currentSelectedTab.getElement().equals(descriptor.getElement()) || currentSelectedTab.getCategory() != descriptor
                            .getCategory())) {
                        for (Control curControl : tabFactory.getTabComposite().getChildren()) {
                            curControl.dispose();
                        }
                    }
                }

                if (element == null || !element.equals(descriptor.getElement()) || currentSelectedTab == null
                        || currentSelectedTab.getCategory() != descriptor.getCategory() || selectedPrimary) {
                    element = descriptor.getElement();
                    currentSelectedTab = descriptor;

                    createTabComposite(tabFactory.getTabComposite(), descriptor.getElement(), descriptor.getCategory());

                    selectedPrimary = false;
                }
            }
        });

    }

    private void createTabComposite(Composite parent, Element element, EComponentCategory category) {
        final int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS;
        IDynamicProperty dynamicComposite = null;

        if (EComponentCategory.EXTRA.equals(category)) {
            dynamicComposite = new MultipleThreadDynamicComposite(parent, style, category, element, true);

        } else if (EComponentCategory.STATSANDLOGS.equals(category)) {
            dynamicComposite = new StatsAndLogsComposite(parent, style, category, element);

        } else if (EComponentCategory.CONTEXT.equals(category)) {
            // TODO
            // dynamicComposite = new ContextDynamicComposite(parent, style, category, element);

        }
        if (dynamicComposite != null) {
            dynamicComposite.refresh();
        }
    }

    /**
     * 
     * DOC ggu Comment method "setElement".
     * 
     * @param element
     */

    private void setElement(Element element, final String title) {

        if (element != null && element instanceof Process) {
            Process process = (Process) element;
            if (currentSelectedTab != null && currentSelectedTab.getElement().equals(process) && !cleaned) {
                return;
            }

            EComponentCategory[] categories = getCategories(process);
            final List<TalendPropertyTabDescriptor> descriptors = new ArrayList<TalendPropertyTabDescriptor>();
            for (EComponentCategory category : categories) {
                TalendPropertyTabDescriptor d = new TalendPropertyTabDescriptor(category);
                d.setElement(process);
                descriptors.add(d);
            }

            tabFactory.setInput(descriptors);
            setPartName(title);
            cleaned = false;
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
        } else {
            cleanDisplay();
        }

    }

    /**
     * 
     * DOC ggu Comment method "setPartName".
     * 
     * set title
     */
    public void setPartName(String title) {
        String viewName = VIEW_NAME;
        if (element instanceof IProcess && AbstractProcessProvider.isExtensionProcessForJoblet((IProcess) element)) {
            viewName = VIEW_NAME_JOBLET;
        }
        if (title == null) {
            title = ""; //$NON-NLS-1$
        }
        if (!title.equals("")) { //$NON-NLS-1$
            viewName = viewName + "(" + title + ")"; //$NON-NLS-1$ //$NON-NLS-2$            
            super.setTitleToolTip(title);
        }
        if (tabFactory != null) {
            Image image = ImageProvider.getImage(ECoreImage.PROCESS_ICON);
            if (this.element != null && this.element instanceof IProcess) {
                if (((IProcess) this.element).disableRunJobView()) { // ?? joblet
                    image = ImageProvider.getImage(ECoreImage.JOBLET_ICON);
                }
            }
            tabFactory.setTitle(title, image);
        }
        super.setPartName(viewName);
    }

    /**
     * set the category.
     */
    private EComponentCategory[] getCategories(Process process) {
        List<EComponentCategory> category = new ArrayList<EComponentCategory>();

        category.add(EComponentCategory.EXTRA);
        boolean isJoblet = AbstractProcessProvider.isExtensionProcessForJoblet(process);
        if (!isJoblet) {
            category.add(EComponentCategory.STATSANDLOGS);
        }
        // category.add(EComponentCategory.CONTEXT);

        return category.toArray(new EComponentCategory[0]);
    }

    public Process getElement() {
        return (Process) element;
    }

    public boolean isCleaned() {
        return this.cleaned;
    }

    public void cleanDisplay() {
        setPartName(null);
        tabFactory.setInput(null);
        tabFactory.setTitle(null, null);
        if (tabFactory.getTabComposite() != null) {
            for (Control curControl : tabFactory.getTabComposite().getChildren()) {
                curControl.dispose();
            }
        }
        this.currentSelectedTab = null;
        this.element = null;
        this.cleaned = true;
        this.selectedPrimary = true;
    }

    public void refresh() {
        refresh(false);
    }

    public void refresh(boolean force) {
        if (force) {
            cleanDisplay();
        }
        final IEditorPart activeEditor = getSite().getPage().getActiveEditor();
        if (activeEditor != null && activeEditor instanceof AbstractMultiPageTalendEditor) {
            AbstractTalendEditor talendEditor = ((AbstractMultiPageTalendEditor) activeEditor).getTalendEditor();
            IProcess process = talendEditor.getProcess();
            if (process != null && process instanceof Element) {
                this.selectedPrimary = true;
                this.cleaned = force;
                this.element = (Element) process;
                setElement(element, activeEditor.getTitle());
                return;
            }
        }
        cleanDisplay();

    }

    @Override
    public void setFocus() {
        if (selectedPrimary) {
            if (getViewSite() != null) {
                getViewSite().getShell().setFocus();
            }
        } else {
            if (tabFactory.getTabComposite() != null) {
                tabFactory.getTabComposite().setFocus();
            }
        }
    }

}
