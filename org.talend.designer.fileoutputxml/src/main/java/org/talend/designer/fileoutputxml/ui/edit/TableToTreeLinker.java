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
package org.talend.designer.fileoutputxml.ui.edit;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.drawing.background.IBackgroundRefresher;
import org.talend.commons.ui.swt.drawing.background.IBgDrawableComposite;
import org.talend.commons.ui.swt.drawing.link.BezierHorizontalLink;
import org.talend.commons.ui.swt.drawing.link.IDrawableLink;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.commons.ui.swt.drawing.link.IStyleLink;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.LinksManager;
import org.talend.commons.ui.swt.drawing.link.StyleLink;
import org.talend.commons.ui.swt.linking.BgDrawableComposite;
import org.talend.commons.ui.swt.linking.IControlsLinker;
import org.talend.commons.ui.swt.linking.LinkableTable;
import org.talend.commons.ui.swt.linking.LinkableTree;
import org.talend.commons.ui.utils.TableUtils;
import org.talend.commons.ui.utils.TreeUtils;
import org.talend.commons.ui.ws.WindowSystem;

/**
 * bqian class global comment. Detailled comment <br/>
 * 
 * $Id: TableToTreeLinker.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 * @param <D1> the data item of extremety 1
 * @param <D2> the data item of extremety 2
 */
public class TableToTreeLinker<D1, D2> extends BgDrawableComposite implements IBgDrawableComposite, IControlsLinker {

    protected LinksManager<Item, D1, Tree, D2> linksManager = new LinksManager<Item, D1, Tree, D2>();

    private IStyleLink defaultStyleLink;

    private IStyleLink selectedStyleLink;

    private IStyleLink unselectedStyleLink;

    private Comparator<LinkDescriptor<Item, D1, Tree, D2>> drawingLinksComparator;

    private IBackgroundRefresher backgroundRefresher;

    private Display display;

    private Table source;

    private Tree target;

    /**
     * DOC amaumont TreeToTableLinker constructor comment.
     * 
     * @param source
     * @param table
     */
    public TableToTreeLinker(Composite commonParent) {
        super(commonParent);
    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param sourceTable
     * @param targetTree
     * @param backgroundRefresher
     */
    public void init(Table sourceTable, Tree targetTree, IBackgroundRefresher backgroundRefresher) {
        this.display = sourceTable.getDisplay();
        this.backgroundRefresher = backgroundRefresher;

        LinkableTree linkableTree = new LinkableTree(this, backgroundRefresher, targetTree, this);
        targetTree.removeSelectionListener(linkableTree.getSelectionListener());

        new LinkableTable(this, backgroundRefresher, sourceTable);
        this.target = targetTree;
        this.source = sourceTable;
    }

    protected IStyleLink getDefaultStyleLink() {
        if (defaultStyleLink == null) {
            StyleLink styleLink = new StyleLink();
            styleLink.setDrawableLink(new BezierHorizontalLink(styleLink));
            styleLink.setForegroundColor(display.getSystemColor(SWT.COLOR_BLACK));
            styleLink.setLineWidth(2);
            this.defaultStyleLink = styleLink;
        }
        return this.defaultStyleLink;
    }

    /**
     * DOC amaumont Comment method "createLinkSorter".
     */
    protected void createLinksComparators() {
        this.drawingLinksComparator = getDrawingLinksComparator();
    }

    /**
     * Define a comparator to draw links.
     */
    protected Comparator<LinkDescriptor<Item, D1, Tree, D2>> getDrawingLinksComparator() {
        if (this.drawingLinksComparator == null) {
            this.drawingLinksComparator = new Comparator<LinkDescriptor<Item, D1, Tree, D2>>() {

                public int compare(LinkDescriptor<Item, D1, Tree, D2> link1, LinkDescriptor<Item, D1, Tree, D2> link2) {
                    IStyleLink link1StyleLink = link1.getStyleLink();
                    IStyleLink link2StyleLink = link2.getStyleLink();
                    if (link1StyleLink == link2StyleLink) {
                        return 0;
                    }
                    if (link1StyleLink == getUnselectedStyleLink()) {
                        return -1;
                    }
                    return 1;
                }

            };
        }
        return this.drawingLinksComparator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.BackgroundRefresher#drawBackground(org.eclipse.swt.graphics.GC)
     */
    @Override
    public void drawBackground(GC gc) {

        List<LinkDescriptor<Item, D1, Tree, D2>> links = linksManager.getLinks();
        int lstSize = links.size();

        int xStartBezierLink = findXRightStartBezierLink(source.getItems(), 0);

        Point sourceToCommonPoint = display.map(source, getBgDrawableComposite(), new Point(0, 0));

        int treeItemHeight = source.getItemHeight();

        Rectangle tableBounds = source.getBounds();

        if (WindowSystem.isGTK()) {
            gc.setAdvanced(false);
        } else {
            gc.fillRectangle(sourceToCommonPoint.x, sourceToCommonPoint.y, tableBounds.width - source.getBorderWidth(),
                    tableBounds.height - source.getBorderWidth());
            if (backgroundRefresher.isAntialiasAllowed()) {
                gc.setAntialias(SWT.ON);
            } else {
                gc.setAntialias(SWT.OFF);
            }
        }

        for (int i = 0; i < lstSize; i++) {
            LinkDescriptor<Item, D1, Tree, D2> link = links.get(i);

            Tree tree = link.getExtremity2().getGraphicalObject();
            Point tableToCommonPoint = display.map(tree, getBgDrawableComposite(), new Point(0, 0));

            IDrawableLink drawableLink = link.getStyleLink().getDrawableLink();
            if (drawableLink == null) {
                drawableLink = getDefaultStyleLink().getDrawableLink();
            }
            drawableLink.getStyle().apply(gc);

            IExtremityLink<Item, D1> extremity1 = link.getExtremity1();
            IExtremityLink<Tree, D2> extremity2 = link.getExtremity2();

            TableItem tableItem = TableUtils.getTableItem(source, (Object) extremity1.getDataItem());
            TableItem firstExpandedAscTableItem = tableItem;

            Rectangle tableItemBounds = firstExpandedAscTableItem.getBounds();

            int yStraight = sourceToCommonPoint.y + treeItemHeight / 2 + tableItemBounds.y;
            Point pointStartStraight = new Point(sourceToCommonPoint.x + tableItemBounds.x + tableItemBounds.width,
                    yStraight);
            Point pointEndStraight = new Point(sourceToCommonPoint.x + xStartBezierLink, yStraight);

            TreeItem treeItem = TreeUtils.getTreeItem(tree, (Object) extremity2.getDataItem());
            Rectangle treeItemBounds = treeItem.getBounds();
            Rectangle treeBounds = tree.getBounds();

            int pointY = treeItemBounds.y + tree.getItemHeight() / 2 + tree.getBorderWidth();
            if (tree.getHeaderVisible()) {
                pointY += tree.getHeaderHeight();
            }

            // TreeItem[] selects = tree.getSelection();
            // Boolean isSelected = false;
            // for (int k = 0; k < selects.length; k++) {
            // if (selects[k].equals(treeItem)) {
            // isSelected = true;
            // break;
            // }
            // }
            Point pointEndCentralCurve = null;
            // if (isSelected) {
            pointEndCentralCurve = backgroundRefresher.convertPointToCommonParentOrigin(new Point(treeBounds.x - 10,
                    pointY), tree);

            // } else {
            // pointEndCentralCurve = backgroundRefresher.convertPointToCommonParentOrigin(new Point(
            // treeItemBounds.x - 10, pointY), tree);
            // }

            boolean lineStyleDot = false;

            Point point = display.map(source, getBgDrawableComposite(), new Point(0, 0));

            if (yStraight < point.y || yStraight > point.y + tableBounds.height) {
                lineStyleDot = true;
            }

            if (pointEndCentralCurve.y < tableToCommonPoint.y) {
                pointEndCentralCurve.y = tableToCommonPoint.y;
                lineStyleDot = true;
            }

            if (pointEndCentralCurve.y > tableToCommonPoint.y + treeBounds.height) {
                pointEndCentralCurve.y = tableToCommonPoint.y + treeBounds.height - 2 * tree.getBorderWidth();
                lineStyleDot = true;
            }

            if (firstExpandedAscTableItem == tableItem && !lineStyleDot) {
                gc.setLineStyle(SWT.LINE_SOLID);
            } else {
                gc.setLineStyle(SWT.LINE_DOT);
            }

            Point offset = getOffset();

            if (WindowSystem.isGTK()) {
                pointStartStraight.x += -15;
            }

            gc.drawLine(pointStartStraight.x + offset.x, pointStartStraight.y + offset.y,
                    pointEndStraight.x + offset.x, pointEndStraight.y + offset.y);

            pointEndStraight.x += offset.x;
            pointEndStraight.y += offset.y;

            pointEndCentralCurve.x += offset.x;
            pointEndCentralCurve.y += offset.y;

            drawableLink.setPoint1(pointEndStraight);
            drawableLink.setPoint2(pointEndCentralCurve);

            drawableLink.draw(gc);
        }

    }

    /**
     * amaumont Comment method "findMaxWidth".
     * 
     * @param items
     * @param maxWidth
     * @return
     */
    private int findXRightStartBezierLink(TableItem[] items, int maxWidth) {
        for (int i = 0; i < items.length; i++) {
            TableItem item = items[i];
            if (!item.isDisposed()) {
                Rectangle bounds = item.getBounds();
                maxWidth = Math.max(maxWidth, bounds.x + bounds.width);
            }
        }
        return maxWidth;
    }

    public void updateLinksStyleAndControlsSelection(Control currentControl) {

        boolean isSource = false;
        if (currentControl == this.getSource()) {
            isSource = true;
        } else if (currentControl == this.getTarget()) {
            isSource = false;
        } else {
            throw new IllegalArgumentException("This type of currentControl is unsupported"); //$NON-NLS-1$
        }

        HashSet selectedItems = new HashSet();

        if (isSource) {
            getTarget().deselectAll();

            TreeItem[] selection = getTarget().getSelection();
            for (int i = 0; i < selection.length; i++) {
                TreeItem tableItem = selection[i];
                selectedItems.add(tableItem.getData());
            }

        } else {
            getSource().deselectAll();

            TableItem[] selection = getSource().getSelection();
            for (int i = 0; i < selection.length; i++) {
                TableItem treeItem = selection[i];
                selectedItems.add(treeItem.getData());
            }

        }

        List<LinkDescriptor<Item, D1, Tree, D2>> links = linksManager.getLinks();
        for (LinkDescriptor<Item, D1, Tree, D2> link : links) {
            IStyleLink styleLink = null;
            IExtremityLink extremity = null;
            if (isSource) {
                extremity = link.getExtremity2();
            } else {
                extremity = link.getExtremity1();
            }
            boolean currentItemIsSelected = selectedItems.contains(extremity.getDataItem());
            if (currentItemIsSelected) {
                styleLink = selectedStyleLink;
            } else {
                styleLink = unselectedStyleLink;
            }
            if (styleLink == null) {
                styleLink = getDefaultStyleLink();
            }
            link.setStyleLink(styleLink);

        }
        linksManager.sortLinks(getDrawingLinksComparator());
        backgroundRefresher.refreshBackground();
    }

    /**
     * Getter for selectedStyleLink.
     * 
     * @return the selectedStyleLink
     */
    public IStyleLink getSelectedStyleLink() {
        return this.selectedStyleLink;
    }

    /**
     * Sets the selectedStyleLink.
     * 
     * @param selectedStyleLink the selectedStyleLink to set
     */
    public void setSelectedStyleLink(IStyleLink selectedStyleLink) {
        this.selectedStyleLink = selectedStyleLink;
    }

    /**
     * Getter for unselectedStyleLink.
     * 
     * @return the unselectedStyleLink
     */
    public IStyleLink getUnselectedStyleLink() {
        return this.unselectedStyleLink;
    }

    /**
     * Sets the unselectedStyleLink.
     * 
     * @param unselectedStyleLink the unselectedStyleLink to set
     */
    public void setUnselectedStyleLink(IStyleLink unselectedStyleLink) {
        this.unselectedStyleLink = unselectedStyleLink;
    }

    /**
     * Getter for backgroundRefresher.
     * 
     * @return the backgroundRefresher
     */
    public IBackgroundRefresher getBackgroundRefresher() {
        return this.backgroundRefresher;
    }

    /**
     * Sets the backgroundRefresher.
     * 
     * @param backgroundRefresher the backgroundRefresher to set
     */
    public void setBackgroundRefresher(IBackgroundRefresher backgroundRefresher) {
        this.backgroundRefresher = backgroundRefresher;
    }

    /**
     * Getter for tables.
     * 
     * @return the tables
     */
    public Tree getTarget() {
        return this.target;
    }

    /**
     * Getter for tree.
     * 
     * @return the tree
     */
    public Table getSource() {
        return this.source;
    }

    /**
     * Getter for linksManager.
     * 
     * @return the linksManager
     */
    protected LinksManager<Item, D1, Tree, D2> getLinksManager() {
        return this.linksManager;
    }
}
