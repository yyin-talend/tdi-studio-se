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

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.dnd.LocalDataTransfer;
import org.talend.commons.ui.swt.dnd.LocalDraggedData;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.Element;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.i18n.Messages;

/**
 * amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: Schema2XMLDragAndDropHandler.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class Schema2XMLDragAndDropHandler {

    private Schema2XMLLinker linker;

    private DragSource dragSource;

    protected int dropDefaultOperation = DND.DROP_LINK;

    private DropTarget loopDropTarget;

    /**
     * amaumont TreeToTableDragAndDropHandler constructor comment.
     * 
     * @param linker
     */
    public Schema2XMLDragAndDropHandler(Schema2XMLLinker linker) {
        this.linker = linker;
        init();
    }

    /**
     * amaumont Comment method "init".
     */
    private void init() {
        createDragSource();
        createDropTarget();
    }

    /**
     * 
     * amaumont Comment method "createDragSource".
     * 
     * @param sourceListener
     */
    private void createDragSource() {
        if (dragSource != null) {
            dragSource.dispose();
        }

        dragSource = new DragSource(linker.getSource(), DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY
                | DND.DROP_LINK);
        dragSource.setTransfer(new Transfer[] { LocalDataTransfer.getInstance() });

        DragSourceListener sourceListener = new TreeDragSourceListener();
        dragSource.addDragListener(sourceListener);
    }

    /**
     * 
     * create DropTarget.
     */
    private void createDropTarget() {

        if (loopDropTarget != null) {
            loopDropTarget.dispose();
        }
        loopDropTarget = new DropTarget(linker.getTarget(), DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY
                | DND.DROP_LINK);
        loopDropTarget.setTransfer(new Transfer[] { LocalDataTransfer.getInstance() });
        DropTargetListener targetListener = new TableDropTargetListener();
        loopDropTarget.addDropListener(targetListener);

    }

    /**
     * 
     * amaumont XmlToSchemaDragAndDropHandler class global comment. Detailled comment <br/>
     * 
     * $Id: Schema2XMLDragAndDropHandler.java,v 1.1 2007/06/12 07:20:38 gke Exp $
     * 
     */
    class TreeDragSourceListener implements TransferDragSourceListener {

        public void dragFinished(DragSourceEvent event) {
        }

        public void dragSetData(DragSourceEvent event) {
        }

        public void dragStart(DragSourceEvent event) {
            TableItem[] items = linker.getSource().getSelection();
            if (items.length == 0) {
                event.doit = false;
            } else {
                LocalDraggedData draggedData = new LocalDraggedData();
                for (TableItem treeItem : items) {
                    draggedData.add(treeItem.getData());
                }
                LocalDataTransfer.getInstance().setLocalDraggedData(draggedData);
            }
        }

        public Transfer getTransfer() {
            return LocalDataTransfer.getInstance();
        }
    };

    /**
     * 
     * amaumont XmlToSchemaDragAndDropHandler class global comment. Detailled comment <br/>
     * 
     * $Id: Schema2XMLDragAndDropHandler.java,v 1.1 2007/06/12 07:20:38 gke Exp $
     * 
     */
    public class TableDropTargetListener implements TransferDropTargetListener {

        public void dragEnter(DropTargetEvent event) {
        }

        public void dragLeave(DropTargetEvent event) {
        }

        public void dragOperationChanged(DropTargetEvent event) {
        }

        public void dropAccept(DropTargetEvent event) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.util.TransferDropTargetListener#getTransfer()
         */
        public Transfer getTransfer() {
            return LocalDataTransfer.getInstance();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.util.TransferDropTargetListener#isEnabled(org.eclipse.swt.dnd.DropTargetEvent)
         */
        public boolean isEnabled(DropTargetEvent event) {
            return false;
        }

        public void dragOver(DropTargetEvent event) {
            // System.out.println("\n>>drop");
            DropTarget dropTarget = (DropTarget) event.getSource();
            Item targetItem = (Item) event.item;
            if (targetItem == null) {
                event.detail = DND.DROP_NONE;
                return;
            }

            FOXTreeNode targetNode = (FOXTreeNode) (targetItem.getData());
            LocalDraggedData draggedData = LocalDataTransfer.getInstance().getDraggedData();
            List<Object> dragdedData = draggedData.getTransferableEntryList();
            if (dragdedData.size() == 1 && isDropRelatedColumn(event)) {
                if (targetNode instanceof Element) {
                    Element element = (Element) targetNode;
                    if (!element.getElementChildren().isEmpty() || element.getParent() == null) {
                        event.detail = DND.DROP_NONE;
                        return;
                    }
                } else {
                    FOXTreeNode parent = targetNode.getParent();
                    if (parent == null) {
                        event.detail = DND.DROP_NONE;
                        return;
                    }
                }
            }
            event.detail = DND.DROP_LINK;
        }

        private boolean isDropRelatedColumn(DropTargetEvent event) {
            DropTarget dropTarget = (DropTarget) event.getSource();
            Display display = event.display;
            Control control = dropTarget.getControl();
            TreeItem item = (TreeItem) event.item;
            Rectangle rec = display.map(control, null, item.getBounds(1));
            if ((event.x >= rec.x) && (event.y >= rec.y) && ((event.x - rec.x) <= rec.width)
                    && ((event.y - rec.y) <= rec.height)) {
                return true;
            }
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
         */
        public void drop(DropTargetEvent event) {
            // System.out.println("\n>>drop");
            DropTarget dropTarget = (DropTarget) event.getSource();
            Item targetItem = (Item) event.item;
            if (targetItem == null) {
                return;
            }

            Control control = dropTarget.getControl();
            LocalDraggedData draggedData = LocalDataTransfer.getInstance().getDraggedData();

            List<Object> dragdedData = draggedData.getTransferableEntryList();
            FOXTreeNode targetNode = (FOXTreeNode) (targetItem.getData());

            if (dragdedData.size() == 1 && isDropRelatedColumn(event)) {
                if (!targetNode.hasChildren()) {
                    IMetadataColumn metaColumn = (IMetadataColumn) dragdedData.get(0);
                    targetNode.setColumn(metaColumn);
                    linker.getXMLViewer().refresh(targetNode);
                    linker.getXMLViewer().expandAll();

                    Display display = linker.getSource().getDisplay();
                    Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
                    linker.getSource().getShell().setCursor(cursor);

                    linker.valuedChanged(targetItem);

                    linker.getSource().getShell().setCursor(null);
                }
            } else if (dragdedData.size() > 0) {
                DragAndDrogDialog dialog = new DragAndDrogDialog(null);
                dialog.open();
                if (dialog.getReturnCode() == IDialogConstants.CANCEL_ID) {
                    return;
                }
                // add by xzhang
                if (dialog.getSelectValue().equals(DragAndDrogDialog.CREATE_AS_TEXT)) {
                    if (targetNode.hasChildren()) {
                        MessageDialog.openConfirm(control.getShell(), Messages.getString("CreateElementAction.0"), //$NON-NLS-1$
                                "\"" + targetNode.getLabel() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
                                        + Messages.getString("Schema2XMLDragAndDropHandler.HasChildrenWarning")); //$NON-NLS-1$
                        return;

                    } else if (targetNode.getParent() == null) {
                        MessageDialog.openConfirm(control.getShell(), Messages.getString("CreateElementAction.0"), //$NON-NLS-1$
                                "\"" + targetNode.getLabel() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
                                        + Messages.getString("Schema2XMLDragAndDropHandler.IsRootWarning")); //$NON-NLS-1$
                        return;
                    }
                    IMetadataColumn metaColumn = (IMetadataColumn) dragdedData.get(0);
                    targetNode.setColumn(metaColumn);
                } else if (dialog.getSelectValue().equals(DragAndDrogDialog.CREATE_AS_SUBELEMENT)) {

                    if (targetNode.getColumn() != null) {
                        if (!MessageDialog.openConfirm(control.getShell(), Messages.getString("CreateElementAction.0"), //$NON-NLS-1$
                                Messages.getString("CreateElementAction.1") //$NON-NLS-1$
                                        + targetNode.getLabel() + "\"?")) { //$NON-NLS-1$
                            return;
                        }
                        targetNode.setColumn(null);
                    }
                    for (Object obj : dragdedData) {
                        IMetadataColumn metaColumn = (IMetadataColumn) obj;
                        boolean isContain = false;
                        for (FOXTreeNode node : ((Element) targetNode).getElementChildren()) {
                            if (node.getLabel().equals(metaColumn.getLabel())) {
                                node.setColumn(metaColumn);
                                isContain = true;
                            }
                        }
                        if (!isContain) {
                            FOXTreeNode child = new Element(metaColumn.getLabel());
                            child.setColumn(metaColumn);
                            targetNode.addChild(child);
                        }
                    }
                } else if (dialog.getSelectValue().equals(DragAndDrogDialog.CREATE_AS_ATTRIBUTE)) {
                    if (!(targetNode instanceof Element)) {
                        MessageDialog.openConfirm(control.getShell(), Messages.getString("CreateElementAction.0"), //$NON-NLS-1$
                                "\"" + targetNode.getLabel() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
                                        + Messages.getString("Schema2XMLDragAndDropHandler.IsNotElementWarning")); //$NON-NLS-1$
                        return;
                    }
                    for (Object obj : dragdedData) {
                        IMetadataColumn metaColumn = (IMetadataColumn) obj;
                        boolean isContain = false;
                        for (FOXTreeNode node : ((Element) targetNode).getAttributeChildren()) {
                            if (node.getLabel().equals(metaColumn.getLabel())) {
                                node.setColumn(metaColumn);
                                isContain = true;
                            }
                        }
                        if (!isContain) {
                            FOXTreeNode child = new Attribute(metaColumn.getLabel());
                            child.setColumn(metaColumn);
                            targetNode.addChild(child);
                        }
                    }
                }

                linker.getXMLViewer().refresh(targetNode);
                linker.getXMLViewer().expandAll();

                Display display = linker.getSource().getDisplay();
                Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
                linker.getSource().getShell().setCursor(cursor);

                linker.valuedChanged(targetItem);

                linker.getSource().getShell().setCursor(null);
            }
            linker.updateLinksStyleAndControlsSelection(control);
        }
    }
}
