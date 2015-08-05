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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ScrollPane;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.figures.layout.TreeLayout;
import org.talend.designer.xmlmap.figures.table.TableTree;
import org.talend.designer.xmlmap.figures.treesettings.AbstractTreeSettingContainer;
import org.talend.designer.xmlmap.figures.treesettings.FilterContainer;
import org.talend.designer.xmlmap.figures.treetools.TreeToolBarContainer;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.parts.AbstractInOutTreeEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.FontInfo;
import org.talend.designer.xmlmap.ui.resource.FontProviderMapper;

/**
 * DOC talend class global comment. Detailled comment
 */
public abstract class AbstractInOutTreeFigure extends GenericFigure {

    private TableTree tableTree;

    protected Figure header;

    protected TreeToolBarContainer imageButtonsFigure;

    protected FilterContainer filterFigure;

    protected AbstractTreeSettingContainer settingContainer;

    protected Figure columnContainer;

    protected AbstractInOutTreeEditPart xmlTreePart;

    protected AbstractInOutTree xmlTree;

    public AbstractInOutTreeFigure(AbstractInOutTreeEditPart xmlTreePart) {
        this.xmlTreePart = xmlTreePart;
        this.xmlTree = (AbstractInOutTree) xmlTreePart.getModel();
    }

    public void highLightHeader(boolean highight) {
        if (header == null || imageButtonsFigure == null) {
            return;
        }
        if (highight) {
            header.setBackgroundColor(ColorConstants.yellow);
            imageButtonsFigure.updateButtonsColor(ColorConstants.yellow);
        } else {
            header.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
            imageButtonsFigure.updateButtonsColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
            if (settingContainer != null) {
                settingContainer.deselectTreeSettingRows();
            }
        }
    }

    public FilterContainer getFilterContainer() {
        return this.filterFigure;
    }

    protected void createContents() {
        setLayoutManager(new TreeLayout(xmlTree));
        this.setBorder(new LineBorder(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_BORDER)));

        header = new Figure();
        header.setOpaque(true);
        header.setBackgroundColor(ColorConstants.tooltipBackground);
        header.setBorder(new RowBorder());
        header.setLayoutManager(new EqualWidthLayout());
        Label tableName = new Label();
        // tableName.setBorder(new LineBorder(ColorConstants.black));
        tableName.setText(getTreeDisplayName());
        tableName.setFont(FontProviderMapper.getFont(FontInfo.FONT_SYSTEM_BOLD));
        tableName.setLabelAlignment(PositionConstants.LEFT);
        tableName.setBorder(new MarginBorder(5, 10, 5, -1));

        header.add(tableName);
        imageButtonsFigure = new TreeToolBarContainer(xmlTreePart);

        header.setOpaque(true);
        header.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
        this.add(header);

        header.add(imageButtonsFigure);

        createTreeSettings(this);

        tableTree = new TableTree(xmlTreePart);

        ScrollPane scroll = new ScrollPane();
        scroll.setVerticalScrollBarVisibility(ScrollPane.NEVER);
        scroll.getViewport().setContents(tableTree);
        scroll.getViewport().setContentsTracksWidth(true);

        this.add(scroll);
    }

    protected abstract String getTreeDisplayName();

    protected abstract void createTreeSettings(Figure parent);

    public IFigure getColumnContainer() {
        return tableTree.getTableItemContainer();
    }

    public TableTree getTableTree() {
        return this.tableTree;
    }
}
