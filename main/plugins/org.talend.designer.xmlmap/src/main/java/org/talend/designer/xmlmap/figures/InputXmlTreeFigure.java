// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.xmlmap.figures.treesettings.FilterContainer;
import org.talend.designer.xmlmap.figures.treesettings.InputTreeSettingContainer;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class InputXmlTreeFigure extends AbstractInOutTreeFigure {

    public InputXmlTreeFigure(InputXmlTreeEditPart xmlTreePart) {
        super(xmlTreePart);
        createContents();

    }

    protected String getTreeDisplayName() {
        return (getInputXmlTree().isLookup() ? "lookup : " : "main :") + getInputXmlTree().getName();
    }

    @Override
    public void setBounds(Rectangle rect) {
        super.setBounds(rect);
    }

    public void update(int type) {
        if (settingContainer != null) {
            settingContainer.update(type);
        }
        if (filterFigure != null) {
            filterFigure.update(type);
        }
        imageButtonsFigure.updateMinSizeImage();

    }

    public InputXmlTree getInputXmlTree() {
        return (InputXmlTree) xmlTree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.AbstractInOutTreeFigure#createTreeSettings(org.eclipse.draw2d.Figure)
     */
    @Override
    protected void createTreeSettings(Figure parent) {
        if (getInputXmlTree().isLookup()) {
            settingContainer = new InputTreeSettingContainer(getInputXmlTree());
            parent.add(settingContainer);
            filterFigure = new FilterContainer(xmlTreePart, (Composite) xmlTreePart.getViewer().getControl());
            parent.add(filterFigure);

        }
    }

}
