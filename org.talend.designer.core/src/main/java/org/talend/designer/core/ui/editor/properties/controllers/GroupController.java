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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;

/**
 * yzhang class global comment. Detailled comment
 */
public class GroupController extends AbstractElementPropertySectionController {

    /**
     * yzhang GroupController constructor comment.
     * 
     * @param dp
     */
    public GroupController(IDynamicProperty dp) {
        super(dp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#createControl(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter, int, int, int, org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(Composite subComposite, final IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {

        final DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, new IControlCreator() {

            public Control createControl(Composite parent, int style) {
                return getWidgetFactory().createGroup(parent, param.getGroupDisplayName());
            }

        });

        Control groupLayout = dField.getLayoutControl();
        groupLayout.setBackground(subComposite.getBackground());

        FormData data = new FormData();
        data.top = new FormAttachment(0, top);
        if (lastControl != null) {
            data.left = new FormAttachment(lastControl, 0);
        } else {
            data.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
        }
        groupLayout.setLayoutData(data);

        Group group = (Group) dField.getControl();
        group.setLayout(new FormLayout());

        hashCurControls.put(param.getGroup(), group);

        return group;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#estimateRowSize(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter)
     */
    @Override
    public int estimateRowSize(Composite subComposite, final IElementParameter param) {

        final DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, new IControlCreator() {

            public Control createControl(Composite parent, int style) {
                return getWidgetFactory().createGroup(parent, param.getDisplayName());
            }

        });
        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        dField.getLayoutControl().dispose();

        return initialSize.y + ITabbedPropertyConstants.VSPACE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#refresh(org.talend.core.model.process.IElementParameter,
     * boolean)
     */
    @Override
    public void refresh(IElementParameter param, boolean check) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }

}
