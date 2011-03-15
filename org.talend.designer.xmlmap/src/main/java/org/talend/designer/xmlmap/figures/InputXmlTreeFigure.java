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
package org.talend.designer.xmlmap.figures;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.figures.layout.TreeLayout;
import org.talend.designer.xmlmap.figures.treesettings.FilterContainer;
import org.talend.designer.xmlmap.figures.treesettings.InputTreeSettingContainer;
import org.talend.designer.xmlmap.figures.treetools.TreeToolBarContainer;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.FontInfo;
import org.talend.designer.xmlmap.ui.resource.FontProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class InputXmlTreeFigure extends GenericFigure {

    protected Figure columnContainer;

    protected InputXmlTreeEditPart xmlTreePart;

    protected InputXmlTree xmlTree;

    protected Figure tableColumnstitle;

    private Figure header;

    private TreeToolBarContainer imageButtonsFigure;

    private InputTreeSettingContainer settingContainer;

    private FilterContainer filterFigure;

    public InputXmlTreeFigure(InputXmlTreeEditPart xmlTreePart) {
        this.xmlTreePart = xmlTreePart;
        this.xmlTree = (InputXmlTree) xmlTreePart.getModel();
        createContents();
        addListeners();

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
        tableName.setText((xmlTree.isLookup() ? "lookup : " : "main :") + xmlTree.getName());
        tableName.setFont(FontProviderMapper.getFont(FontInfo.FONT_SYSTEM_BOLD));
        tableName.setLabelAlignment(PositionConstants.LEFT);
        tableName.setBorder(new MarginBorder(5, 10, 5, -1));

        header.add(tableName);
        imageButtonsFigure = new TreeToolBarContainer(xmlTreePart);

        header.setOpaque(true);
        header.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
        this.add(header);

        header.add(imageButtonsFigure);
        if (xmlTree.isLookup()) {
            settingContainer = new InputTreeSettingContainer(xmlTree);
            this.add(settingContainer);
            filterFigure = new FilterContainer(xmlTree, (Composite) xmlTreePart.getViewer().getControl());
            this.add(filterFigure);

        }

        tableColumnstitle = new ColumnTitleFigure();
        this.add(tableColumnstitle);

        ScrollPane scroll = new ScrollPane();
        scroll.setVerticalScrollBarVisibility(ScrollPane.NEVER);

        columnContainer = new Figure();
        scroll.getViewport().setContents(columnContainer);

        ToolbarLayout layout = new ToolbarLayout();
        layout.setStretchMinorAxis(true);
        layout.setVertical(true);
        columnContainer.setLayoutManager(layout);
        columnContainer.setOpaque(true);
        columnContainer.setBackgroundColor(ColorConstants.white);
        // this.add(columnContainer);
        this.add(scroll);
    }

    @Override
    public void setBounds(Rectangle rect) {
        super.setBounds(rect);
    }

    public IFigure getColumnContainer() {
        return this.columnContainer;
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
        return this.xmlTree;
    }

    private void addListeners() {
        this.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent me) {
                if (InputXmlTreeFigure.this.containsPoint(me.x, me.y)) {
                    header.setBackgroundColor(ColorConstants.yellow);
                    xmlTreePart.updateChildrenConnections(xmlTreePart.getChildren(), xmlTreePart);
                    if (xmlTreePart.getParent() instanceof XmlMapDataEditPart) {
                        List children = ((XmlMapDataEditPart) xmlTreePart.getParent()).getChildren();
                        for (Object obj : children) {
                            if (obj == xmlTreePart) {
                                continue;
                            }
                            if (obj instanceof InputXmlTreeEditPart) {
                                InputXmlTreeEditPart otherTreePart = (InputXmlTreeEditPart) obj;
                                ((InputXmlTreeFigure) otherTreePart.getFigure()).resetHeaderColor();
                                otherTreePart.updateChildrenConnections(otherTreePart.getChildren(), xmlTreePart);
                            }
                        }
                    }

                }
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseDoubleClicked(MouseEvent me) {
            }

        });
    }

    public void resetHeaderColor() {
        header.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.ZONE_BACKGROUND_COLOR));
    }

    class ColumnTitleFigure extends Figure {

        public ColumnTitleFigure() {
            if (xmlTree.isLookup()) {
                Label expression = new Label();
                expression.setText("Exp.key");
                expression.setBorder(new MarginBorder(3, 10, 3, -1));
                expression.setLabelAlignment(PositionConstants.LEFT);
                this.add(expression);
            }

            Label column1 = new Label();
            column1.setText("Column");
            column1.setBorder(new MarginBorder(3, 10, 3, -1));
            column1.setLabelAlignment(PositionConstants.LEFT);
            this.add(column1);
            this.setLayoutManager(new EqualWidthLayout());

            setBackgroundColor(ColorConstants.menuBackground);
            setOpaque(true);

        }

    }
}
