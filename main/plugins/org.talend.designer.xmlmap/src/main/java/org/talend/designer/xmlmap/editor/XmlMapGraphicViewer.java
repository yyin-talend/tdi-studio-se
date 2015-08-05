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
package org.talend.designer.xmlmap.editor;

import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.ExclusionSearch;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.talend.designer.xmlmap.figures.manager.FiguresManager;
import org.talend.designer.xmlmap.parts.AbstractInOutTreeEditPart;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.VarTableEditPart;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC XYuser class global comment. Detailled comment
 */
public class XmlMapGraphicViewer extends GraphicalViewerImpl {

    private FiguresManager listener;

    public XmlMapGraphicViewer() {
        listener = new FiguresManager(this);
        this.addSelectionChangedListener(listener);
    }

    private MapperManager mapperManager;

    public IFigure findFigureAt(int x, int y, Collection exclude, final Conditional condition) {
        class ConditionalTreeSearch extends ExclusionSearch {

            ConditionalTreeSearch(Collection coll) {
                super(coll);
            }

            @Override
            public boolean accept(IFigure figure) {
                EditPart editpart = null;
                while (editpart == null && figure != null) {
                    editpart = (EditPart) getVisualPartMap().get(figure);
                    figure = figure.getParent();
                }
                return editpart != null && (condition == null || condition.evaluate(editpart));
            }
        }
        return getLightweightSystem().getRootFigure().findFigureAt(x, y, new ConditionalTreeSearch(exclude));
    }

    @Override
    public void select(EditPart editpart) {
        if (editpart instanceof VarTableEditPart) {
            return;
        }

        if (editpart instanceof XmlMapDataEditPart) {
            return;
        }

        // set false to dispatch mouse release event ??? need or not mouseListeners is executed before selection change
        // setRouteEventsToEditDomain(false);

        if ((getSelectedEditParts().size() == 1) && (getSelectedEditParts().get(0) == editpart)) {
            return;
        }

        primDeselectAll(editpart);
        appendSelection(editpart); // fireSelectionChanged() is called here
    }

    /*
     * if partToAppend is inputTree , don't clean all outputTree in selection list , leave the first one by default . if
     * partToAppend is outputTree , do the same for inputTree selections
     */
    private void primDeselectAll(EditPart eidtPart) {
        EditPart partToAppend = eidtPart;
        List list = primGetSelectedEditParts();

        if (partToAppend instanceof TreeNodeEditPart) {
            partToAppend = XmlMapUtil.findTreePart((TreeNodeEditPart) partToAppend);
        }

        EditPart part;
        AbstractInOutTreeEditPart abstractTreePart = null;
        for (int i = 0; i < list.size(); i++) {
            part = (EditPart) list.get(i);
            if (partToAppend instanceof InputXmlTreeEditPart) {
                if (abstractTreePart == null && part instanceof OutputXmlTreeEditPart) {
                    abstractTreePart = (OutputXmlTreeEditPart) part;
                    continue;
                }
            } else if (partToAppend instanceof OutputXmlTreeEditPart) {
                if (abstractTreePart == null && part instanceof InputXmlTreeEditPart) {
                    abstractTreePart = (InputXmlTreeEditPart) part;
                    continue;
                }
            }
            part.setSelected(EditPart.SELECTED_NONE);
        }
        list.clear();
        if (abstractTreePart != null) {
            list.add(abstractTreePart);
        }
    }

    public MapperManager getMapperManager() {
        return mapperManager;
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
        if (!selectionListeners.contains(mapperManager)) {
            mapperManager.setGraphicalViewer(this);
            addSelectionChangedListener(mapperManager);
        }
    }

    public FiguresManager getFiguresManager() {
        return this.listener;
    }

    // @Override
    // public void appendSelection(EditPart editpart) {
    // if (editpart instanceof TreeNodeEditPart) {
    // if (isParentSelected((TreeNodeEditPart) editpart)) {
    // return;
    // }
    // List selectedChildren = new ArrayList();
    // getSelectedChild(selectedChildren, (TreeNodeEditPart) editpart);
    // if (!selectedChildren.isEmpty()) {
    // return;
    // }
    // }
    // super.appendSelection(editpart);
    // }
    //
    // /**
    // *
    // * DOC check if parent node is already selected
    // *
    // * @return
    // */
    // private boolean isParentSelected(TreeNodeEditPart editpart) {
    // EditPart parent = editpart.getParent();
    // boolean selected = getSelectedEditParts().contains(parent);
    // if (selected) {
    // return true;
    // }
    // if (!(parent instanceof AbstractInOutTreeEditPart)) {
    // return selected || isParentSelected((TreeNodeEditPart) parent);
    // }
    // return false;
    // }
    //
    // private void getSelectedChild(List selectedChild, TreeNodeEditPart editPart) {
    // if (!editPart.getChildren().isEmpty()) {
    // for (Object child : editPart.getChildren()) {
    // if (child instanceof TreeNodeEditPart) {
    // TreeNodeEditPart childPart = (TreeNodeEditPart) child;
    // if (getSelectedEditParts().contains(childPart)) {
    // selectedChild.add(childPart);
    // }
    // getSelectedChild(selectedChild, childPart);
    // }
    // }
    // }
    // }

}
