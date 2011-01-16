// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.parts.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ExpressionCellEditorLocator implements CellEditorLocator {

    private IFigure figure;

    public ExpressionCellEditorLocator(IFigure figure) {
        this.figure = figure;
    }

    public void relocate(CellEditor celleditor) {
        Composite text = (Composite) celleditor.getControl();
        Rectangle copy = figure.getBounds().getCopy();
        figure.translateToAbsolute(copy);

        text.setBounds(copy.x, copy.y, copy.width, copy.height);

    }

}
