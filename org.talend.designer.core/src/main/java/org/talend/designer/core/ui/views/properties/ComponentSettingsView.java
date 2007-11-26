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
package org.talend.designer.core.ui.views.properties;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.properties.tab.HorizontalTabFactory;
import org.talend.core.properties.tab.TalendPropertyTabDescriptor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.connections.MainConnectionComposite;

/**
 * nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class ComponentSettingsView extends ViewPart {

    public static final String ID = "org.talend.designer.core.ui.views.properties.ComponentSettingsView";

    private HorizontalTabFactory tabFactory = null;

    private TalendPropertyTabDescriptor currentSelectedTab;

    private Element elem;

    /**
     * nrousseau ComponentSettings constructor comment.
     */
    public ComponentSettingsView() {
        tabFactory = new HorizontalTabFactory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        tabFactory.initComposite(parent);
        tabFactory.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                TalendPropertyTabDescriptor descriptor = (TalendPropertyTabDescriptor) selection.getFirstElement();

                if (currentSelectedTab != null
                        && currentSelectedTab.getComposite() != null
                        && (!currentSelectedTab.getElement().equals(descriptor.getElement()) || currentSelectedTab.getCategory() != descriptor
                                .getCategory())) {
                    currentSelectedTab.getComposite().setVisible(false);
                    // currentSelectedTab.getComposite().getChildren()
                    for (Control curControl : currentSelectedTab.getComposite().getChildren()) {
                        curControl.dispose();
                    }
                }

                Composite parent = descriptor.getComposite();

                if (elem == null || !elem.equals(descriptor.getElement())
                        || currentSelectedTab.getCategory() != descriptor.getCategory()) {
                    elem = descriptor.getElement();
                    parent = tabFactory.createTabComposite();
                    createDynamicComposite(parent, descriptor.getElement(), descriptor.getCategory());
                    parent.getParent().layout();
                    parent.setVisible(true);
                }
                descriptor.setComposite(parent);
                currentSelectedTab = descriptor;
            }

        });
    }

    /**
     * yzhang Comment method "createDynamicComposite".
     * 
     * @param parent
     * @param element
     * @param category
     */
    private void createDynamicComposite(Composite parent, Element element, EComponentCategory category) {
        DynamicComposite dc = null;

        if (element instanceof Node) {
            dc = new DynamicComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS, category, element);
        } else {
            dc = new MainConnectionComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS, category, element);
        }
        dc.refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {

    }

    public void setElement(Element elem) {
        if (currentSelectedTab != null && currentSelectedTab.getElement().equals(elem)) {
            return;
        }

        EComponentCategory[] categories = getCategories(elem);
        final List<TalendPropertyTabDescriptor> descriptors = new ArrayList<TalendPropertyTabDescriptor>();
        for (EComponentCategory category : categories) {
            TalendPropertyTabDescriptor d = new TalendPropertyTabDescriptor(category);
            d.setElement(elem);
            descriptors.add(d);
        }

        tabFactory.setInput(descriptors);
        setPropertiesViewerTitle(elem);
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

    /**
     * yzhang Comment method "setPropertiesViewerTitle".
     * 
     * @param elem
     */
    private void setPropertiesViewerTitle(Element elem) {
        String label = null;
        Image image = null;
        if (elem instanceof Node) {
            label = ((Node) elem).getLabel();
            image = new Image(Display.getDefault(), ((Node) elem).getComponent().getIcon24().getImageData());
        } else if (elem instanceof Connection) {
            label = ((Connection) elem).getElementName();
            image = ImageProvider.getImage(EImage.RIGHT_ICON);
        }
        tabFactory.setTitle(label, image);
    }

    /**
     * yzhang Comment method "getCategories".
     * 
     * @param elem
     * @return
     */
    private EComponentCategory[] getCategories(Element elem) {
        if (elem instanceof Connection) {
            return EElementType.CONNECTION.getCategories();
        } else if (elem instanceof Node) {
            return EElementType.NODE.getCategories();
        }
        return null;
    }

}
