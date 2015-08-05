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
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.xmlmap.figures.treesettings.FilterContainer;
import org.talend.designer.xmlmap.figures.treesettings.OutputTreeSettingContainer;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputXmlTreeFigure extends AbstractInOutTreeFigure {

    public OutputXmlTreeFigure(OutputXmlTreeEditPart treePart) {
        super(treePart);
        createContents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.AbstractInOutTreeFigure#getTreeDisplayName()
     */
    @Override
    protected String getTreeDisplayName() {
        return xmlTree.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.AbstractInOutTreeFigure#createTreeSettings(org.eclipse.draw2d.Figure)
     */
    @Override
    protected void createTreeSettings(Figure parent) {
        settingContainer = new OutputTreeSettingContainer(getOutputXmlTree());
        parent.add(settingContainer);

        filterFigure = new FilterContainer(xmlTreePart, (Composite) xmlTreePart.getViewer().getControl());
        parent.add(filterFigure);

    }

    public void update(int type) {
        settingContainer.update(type);
        filterFigure.update(type);
        imageButtonsFigure.updateMinSizeImage();
        imageButtonsFigure.updateLoopFunctionButton();
    }

    private OutputXmlTree getOutputXmlTree() {
        return (OutputXmlTree) xmlTree;
    }
}
