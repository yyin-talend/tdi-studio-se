// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.drawing.background.BackgroundRefresher;
import org.talend.commons.ui.swt.drawing.link.BezierHorizontalLink;
import org.talend.commons.ui.swt.drawing.link.IDrawableLink;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.LinksManager;
import org.talend.commons.ui.swt.drawing.link.StyleLink;
import org.talend.commons.ui.swt.drawing.link.TableItemExtremityDescriptor;
import org.talend.commons.ui.swt.drawing.link.TreeItemExtremityDescriptor;
import org.talend.commons.utils.threading.AsynchronousThreading;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class TreeToTableLinker extends BackgroundRefresher {

    protected Tree tree;

    protected Table table;

    protected LinksManager<TreeItem, TableItem> linksManager = new LinksManager<TreeItem, TableItem>();

    /**
     * DOC amaumont TreeToTableLinker constructor comment.
     * 
     * @param tree
     * @param table
     */
    public TreeToTableLinker(Composite commonParent, Tree tree, Table table) {
        super(commonParent);
        this.tree = tree;
        this.table = table;
        init();
    }

    private void init() {

        ControlListener controlListener = new ControlListener() {

            public void controlMoved(ControlEvent e) {
                updateBackground();
            }

            public void controlResized(ControlEvent e) {
                updateBackground();
            }

        };

        configureScrollBars(table.getDisplay());

        table.addControlListener(controlListener);
        tree.addControlListener(controlListener);

        tree.addTreeListener(new TreeListener() {

            public void treeCollapsed(TreeEvent e) {
                updateBackroundAsynchronous();
            }

            public void treeExpanded(TreeEvent e) {
                updateBackroundAsynchronous();
            }

            /**
             * DOC amaumont Comment method "updateBackroundAsynchronous".
             */
            private void updateBackroundAsynchronous() {
                new AsynchronousThreading(50, false, tree.getDisplay(), new Runnable() {

                    /*
                     * (non-Javadoc)
                     * 
                     * @see java.lang.Runnable#run()
                     */
                    public void run() {
                        updateBackground();
                    }

                }).start();
            }

        });

    }

    private void configureScrollBars(final Display display) {
        ScrollBar vBarTable = table.getVerticalBar();
        ScrollBar vBarTree = tree.getVerticalBar();
        ScrollBar hBarTree = tree.getHorizontalBar();

        SelectionListener scrollListener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                updateBackground();
            }
        };
        vBarTable.addSelectionListener(scrollListener);
        vBarTree.addSelectionListener(scrollListener);
        hBarTree.addSelectionListener(scrollListener);
    }

    /**
     * Getter for table.
     * 
     * @return the table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Getter for tree.
     * 
     * @return the tree
     */
    public Tree getTree() {
        return this.tree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.BackgroundRefresher#drawBackground(org.eclipse.swt.graphics.GC)
     */
    @Override
    public void drawBackground(GC gc) {

        List<LinkDescriptor<TreeItem, TableItem>> links = linksManager.getLinks();
        int lstSize = links.size();

        int xStartBezierLink = findXRightStartBezierLink(tree.getItems(), 0);

        Display display = table.getDisplay();

        Point treeToCommonPoint = display.map(tree, commonParent, new Point(0, 0));
        // System.out.println("treeToCommonPoint=" + treeToCommonPoint);
        Point tableToCommonPoint = display.map(table, commonParent, new Point(0, 0));

        int treeItemHeight = tree.getItemHeight();

        for (int i = 0; i < lstSize; i++) {
            LinkDescriptor<TreeItem, TableItem> link = links.get(i);

            IDrawableLink drawableLink = link.getDrawableLink();

            drawableLink.getStyle().apply(gc);

            IExtremityLink<TreeItem> extremity1 = link.getExtremity1();
            IExtremityLink<TableItem> extremity2 = link.getExtremity2();

            TreeItem treeItem = extremity1.getAssociatedItem();

            TreeItem firstExpandedAscTreeItem = findFirstVisibleItemAscFrom(treeItem);
            if (isAntialiasAllowed()) {
                gc.setAntialias(SWT.ON);
            } else {
                gc.setAdvanced(false);
            }

            // System.out.println(point);

            Rectangle treeItemBounds = firstExpandedAscTreeItem.getBounds();

            int yStraight = treeToCommonPoint.y + treeItemHeight / 2 + treeItemBounds.y;
            Point pointStartStraight = new Point(treeToCommonPoint.x + treeItemBounds.x + treeItemBounds.width,
                    yStraight);
            Point pointEndStraight = new Point(treeToCommonPoint.x + xStartBezierLink, yStraight);

            Rectangle tableItemBounds = extremity2.getAssociatedItem().getBounds();
            Rectangle tableBounds = table.getBounds();

            Point pointEndCentralCurve = convertPointToCommonParentOrigin(new Point(tableItemBounds.x - 2,
                    tableItemBounds.y + table.getItemHeight() / 2 + table.getBorderWidth()), table);

            boolean lineStyleDot = false;
            
            if (pointEndCentralCurve.y < tableToCommonPoint.y) {
                pointEndCentralCurve.y = tableToCommonPoint.y;
                lineStyleDot = true;
            }

            if (pointEndCentralCurve.y > tableToCommonPoint.y + tableBounds.height) {
                pointEndCentralCurve.y = tableToCommonPoint.y + tableBounds.height - 2 * table.getBorderWidth();
                lineStyleDot = true;
            }

            if (firstExpandedAscTreeItem == treeItem && !lineStyleDot) {
                gc.setLineStyle(SWT.LINE_SOLID);
            } else {
                gc.setLineStyle(SWT.LINE_DOT);
            }

            gc.drawLine(pointStartStraight.x, pointStartStraight.y, pointEndStraight.x, pointEndStraight.y);
            
            drawableLink.setPoint1(pointEndStraight);
            drawableLink.setPoint2(pointEndCentralCurve);

            drawableLink.draw(gc);
        }

        // IDrawableLink drawableLink = styleLink.getDrawableLink();
        // System.out.println("##################");
        //
        // TreeColumn[] columns = tree.getColumns();
        // int sizeColumns = 0;
        // for (int i = 0; i < columns.length; i++) {
        // TreeColumn column = columns[i];
        // sizeColumns += column.getWidth();
        // }
        // System.out.println("sizeColumns=" + sizeColumns);
        //
        // TreeItem[] items = tree.getItems();
        // int maxX = 0;
        //
        // xStartBezierLink = findXRightStartBezierLink(items, xStartBezierLink);
        // System.out.println("maxWidth=" + xStartBezierLink);
        //
        // Point point2 = tree.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        // System.out.println("computeSize=" + point2);
        // System.out.println("ClientArea=" + tree.getClientArea());
        // System.out.println("Bounds=" + tree.getBounds());
        // System.out.println("Size=" + tree.getSize());
        // ScrollBar horizontalBar = tree.getHorizontalBar();
        // Point barSize = horizontalBar.getSize();
        // System.out.println("barSize=" + barSize);
        // int barSelection = horizontalBar.getSelection();
        // System.out.println("barSelection=" + barSelection);
        //
        // Composite parent = tree.getParent();
        // Point point3 = parent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        // System.out.println("parent computeSize=" + point3);
        // System.out.println("parent ClientArea=" + parent.getClientArea());
        // System.out.println("parent Bounds=" + parent.getBounds());
        // System.out.println("parent Size=" + parent.getSize());
        //
        // System.out.println("treeToCommonPoint=" + treeToCommonPoint);
        // System.out.println(point);

        // drawableLink.setPoint1(new Point(treeToCommonPoint.x + xStartBezierLink, 143));
        // drawableLink.setPoint2(tableToCommonPoint);
        // drawableLink.setPoint2(new Point(140, 243));
        // drawableLink.draw(gc);
    }

    /**
     * DOC amaumont Comment method "findFirstVisibleItemAscFrom".
     * 
     * @param treeItem
     */
    private TreeItem findFirstVisibleItemAscFrom(TreeItem treeItem) {
        TreeItem parentItem = treeItem.getParentItem();
        if (parentItem == null) {
            return treeItem;
        } else if (parentItem.getExpanded()) {
            TreeItem treeItemFound = getNextCollapseParent(parentItem);
            if (treeItemFound != null) {
                return findFirstVisibleItemAscFrom(treeItemFound);
            } else {
                return treeItem;
            }
        } else {
            return findFirstVisibleItemAscFrom(parentItem);
        }
    }

    /**
     * DOC amaumont Comment method "getNextCollapseParent".
     * 
     * @param parentItem
     */
    private TreeItem getNextCollapseParent(TreeItem treeItem) {
        TreeItem parentItem = treeItem.getParentItem();
        if (parentItem == null) {
            return null;
        } else if (!parentItem.getExpanded()) {
            return parentItem;
        } else {
            TreeItem treeItemFound = getNextCollapseParent(parentItem);
            if (treeItemFound != null) {
                return parentItem;
            } else {
                return null;
            }
        }

    }

    /**
     * DOC amaumont Comment method "findMaxWidth".
     * 
     * @param items
     * @param maxWidth
     * @return
     */
    private int findXRightStartBezierLink(TreeItem[] items, int maxWidth) {
        for (int i = 0; i < items.length; i++) {
            TreeItem item = items[i];
            Rectangle bounds = item.getBounds();
            maxWidth = Math.max(maxWidth, bounds.x + bounds.width);
            if (item.getExpanded()) {
            	maxWidth = findXRightStartBezierLink(item.getItems(), maxWidth);
            }
        }
        return maxWidth;
    }

}
