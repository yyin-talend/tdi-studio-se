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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class ComponentSettingsView extends ViewPart {

    public static final String ID = "org.talend.designer.core.ui.views.properties.ComponentSettingsView";

    private Element elem = null;

    private DynamicComposite dc = null;

    private Composite parentComposite;

    /**
     * DOC nrousseau ComponentSettings constructor comment.
     */
    public ComponentSettingsView() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        parentComposite = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        if (dc != null) {
            dc.refresh();
        }
    }

    public void setElement(Element elem) {
        this.elem = elem;
        if (dc == null || !dc.getElement().equals(elem)) {
            if (dc != null) {
                dc.dispose();
            }
            dc = new DynamicComposite(parentComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS, EComponentCategory.PROPERTY,
                    elem);
            dc.refresh();
        }
    }
}
