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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.properties.tab.HorizontalTabFactory;
import org.talend.core.properties.tab.TalendPropertyTabDescriptor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.connections.MainConnectionComposite;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class ComponentSettingsView extends ViewPart {

    public static final String ID = "org.talend.designer.core.ui.views.properties.ComponentSettingsView";

    private Element elem = null;

    private HorizontalTabFactory tabFactory;

    private Map<EComponentCategory, Composite> tabContents;

    private Composite currentComposite;

    /**
     * nrousseau ComponentSettings constructor comment.
     */
    public ComponentSettingsView() {
        tabContents = new HashMap<EComponentCategory, Composite>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        tabFactory = new HorizontalTabFactory();
        tabFactory.initComposite(parent);
        tabFactory.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                TalendPropertyTabDescriptor descriptor = (TalendPropertyTabDescriptor) selection.getFirstElement();

                if (currentComposite == null) {
                    currentComposite = tabContents.get(descriptor.getCategory());
                    currentComposite.setVisible(true);
                } else {
                    if (!currentComposite.isDisposed()) {
                        currentComposite.setVisible(false);
                    }
                    currentComposite = tabContents.get(descriptor.getCategory());
                    currentComposite.setVisible(true);
                }

            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        // DynamicComposite dc = (DynamicComposite) currentComposite.getChildren()[0];
        // dc.refresh();
        // if (dc != null) {
        // dc.refresh();
        // }
    }

    public void removeCategory(List<EComponentCategory> categories) {

        for (EComponentCategory category : categories) {
            tabContents.get(category).dispose();
            tabContents.remove(category);
        }
    }

    public void setElement(Element elem) {
        this.elem = elem;

        List<EComponentCategory> requiredCategories = Arrays.asList(getCategories(elem));

        List<EComponentCategory> needRemoveCategories = new ArrayList<EComponentCategory>();
        for (EComponentCategory category : tabContents.keySet()) {
            if (!requiredCategories.contains(category)) {
                needRemoveCategories.add(category);
            }
        }
        removeCategory(needRemoveCategories);

        final List<TalendPropertyTabDescriptor> descriptors = new ArrayList<TalendPropertyTabDescriptor>();

        for (EComponentCategory category : requiredCategories) {
            Composite root = tabContents.get(category);
            if (root == null) {
                root = tabFactory.createTabComposite();
            }
            createComposite(root, elem, category);

            descriptors.add(new TalendPropertyTabDescriptor(category));
            tabContents.put(category, root);

        }
        if (elem instanceof Node) {
            String label = ((Node) elem).getLabel();
            Image image = new Image(Display.getDefault(), ((Node) elem).getComponent().getIcon24().getImageData());
            tabFactory.setTitle(label, image);
        } else if (elem instanceof Connection) {
            String label = ((Connection) elem).getConnectionLabel().getLabelText();
            // Image image = new Image(Display.getDefault(), ((Connection)
            // elem).getComponent().getIcon16().getImageData());
            tabFactory.setTitle(label, null);
        }
        tabFactory.setInput(descriptors);
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
                List<TalendPropertyTabDescriptor> selected = new ArrayList<TalendPropertyTabDescriptor>();
                if (descriptors.size() > 0) {
                    selected.add(descriptors.get(0));
                }
                return selected;
            }

            public boolean isEmpty() {
                return false;
            }

        });

    }

    private void createComposite(Composite parentComposite, Element elem, EComponentCategory category) {
        DynamicComposite dc = null;
        if (parentComposite.getChildren().length > 0) {
            dc = (DynamicComposite) parentComposite.getChildren()[0];
        }
        if (dc == null || !dc.getElement().equals(elem)) {
            if (dc != null) {
                dc.dispose();
            }
            if (elem instanceof Node) {
                dc = new DynamicComposite(parentComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS, category, elem);
            } else {
                dc = new MainConnectionComposite(parentComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS, category, elem);
            }
            dc.refresh();
        }
    }

    private EComponentCategory[] getCategories(Element elem) {
        if (elem instanceof Connection) {
            return EElementType.CONNECTION.getCategories();
        } else if (elem instanceof Node) {
            return EElementType.NODE.getCategories();
        }
        return null;
    }
}
