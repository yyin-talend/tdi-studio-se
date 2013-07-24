// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures.treetools.zone;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.dialogs.SearchPattern;
import org.talend.designer.gefabstractmap.figures.table.entity.TableEntityFigure;
import org.talend.designer.gefabstractmap.figures.var.VarEntityFigure;
import org.talend.designer.gefabstractmap.part.TableEntityPart;
import org.talend.designer.gefabstractmap.resource.EntryState;
import org.talend.designer.xmlmap.figures.InputXmlTreeFigure;
import org.talend.designer.xmlmap.figures.OutputXmlTreeFigure;
import org.talend.designer.xmlmap.figures.treeNode.XmlmapTreeNodeFigure;
import org.talend.designer.xmlmap.figures.treesettings.XmlMapFilterContainer;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class SearchZoneMapper {

    private MapperManager mapperManager;

    private boolean isHightlightAll = false;

    public SearchZoneMapper(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }

    public void search(Map<Integer, Figure> searchMaps, String searchValue) {
        if (searchValue.equals("") || searchValue == null) {
            return;
        }
        // SearchPattern
        SearchPattern matcher = new SearchPattern();
        matcher.setPattern(searchValue);

        List<InputXmlTree> inputTrees = mapperManager.getExternalData().getInputTrees();
        List<VarTable> varTables = mapperManager.getExternalData().getVarTables();
        List<OutputXmlTree> outputTrees = mapperManager.getExternalData().getOutputTrees();

        int index = -1;

        // for the Lookup InputTables
        for (InputXmlTree inputXmlTree : inputTrees) {
            // ExpressionFilter
            if (inputXmlTree.getExpressionFilter() != null && matcher.matches(inputXmlTree.getExpressionFilter())) {
                EList<Adapter> adapter = inputXmlTree.eAdapters();
                if (adapter.size() > 0) {
                    if (adapter.get(0) instanceof InputXmlTreeEditPart) {
                        InputXmlTreeEditPart inputXmlTreeEditPart = (InputXmlTreeEditPart) adapter.get(0);
                        if (inputXmlTreeEditPart != null && inputXmlTreeEditPart.getFigure() != null
                                && inputXmlTreeEditPart.getFigure() instanceof InputXmlTreeFigure) {
                            InputXmlTreeFigure inputXmlTreeFigure = (InputXmlTreeFigure) inputXmlTreeEditPart.getFigure();
                            index++;
                            searchMaps.put(index, inputXmlTreeFigure.getFilterContainer());
                        }
                    }
                }
            }
            // TreeNode
            for (TreeNode node : inputXmlTree.getNodes()) {
                if (node.getExpression() != null && matcher.matches(node.getExpression())) {
                    EList<Adapter> adapter = node.eAdapters();
                    if (adapter.size() > 0) {
                        if (adapter.get(0) instanceof TableEntityPart) {
                            TableEntityPart tableEntityPart = (TableEntityPart) adapter.get(0);
                            if (tableEntityPart != null && tableEntityPart.getFigure() != null
                                    && tableEntityPart.getFigure() instanceof TableEntityFigure) {
                                TableEntityFigure nodeFigure = (TableEntityFigure) tableEntityPart.getFigure();
                                index++;
                                searchMaps.put(index, nodeFigure);
                            }
                        }
                    }
                }
            }
        }

        // for the VarsTables
        for (VarTable varTable : varTables) {
            for (VarNode node : varTable.getNodes()) {
                if (node.getExpression() != null && matcher.matches(node.getExpression())) {
                    EList<Adapter> adapter = node.eAdapters();
                    if (adapter.size() > 0) {
                        if (adapter.get(0) instanceof TableEntityPart) {
                            TableEntityPart tableEntityPart = (TableEntityPart) adapter.get(0);
                            if (tableEntityPart != null && tableEntityPart.getFigure() != null
                                    && tableEntityPart.getFigure() instanceof TableEntityFigure) {
                                TableEntityFigure nodeFigure = (TableEntityFigure) tableEntityPart.getFigure();
                                index++;
                                searchMaps.put(index, nodeFigure);
                            }
                        }
                    }
                }
            }
        }

        // for the OutputTables
        for (OutputXmlTree outputXmlTree : outputTrees) {
            // ExpressionFilter
            if (outputXmlTree.getExpressionFilter() != null && matcher.matches(outputXmlTree.getExpressionFilter())) {
                EList<Adapter> adapter = outputXmlTree.eAdapters();
                if (adapter.size() > 0) {
                    if (adapter.get(0) instanceof OutputXmlTreeEditPart) {
                        OutputXmlTreeEditPart outputXmlTreeEditPart = (OutputXmlTreeEditPart) adapter.get(0);
                        if (outputXmlTreeEditPart != null && outputXmlTreeEditPart.getFigure() != null
                                && outputXmlTreeEditPart.getFigure() instanceof OutputXmlTreeFigure) {
                            OutputXmlTreeFigure outputXmlTreeFigure = (OutputXmlTreeFigure) outputXmlTreeEditPart.getFigure();
                            index++;
                            searchMaps.put(index, outputXmlTreeFigure.getFilterContainer());
                        }
                    }
                }
            }
            // OutputTreeNode
            for (OutputTreeNode node : outputXmlTree.getNodes()) {
                if (node.getExpression() != null && matcher.matches(node.getExpression())) {
                    EList<Adapter> adapter = node.eAdapters();
                    if (adapter.size() > 0) {
                        if (adapter.get(0) instanceof TableEntityPart) {
                            TableEntityPart tableEntityPart = (TableEntityPart) adapter.get(0);
                            if (tableEntityPart != null && tableEntityPart.getFigure() != null
                                    && tableEntityPart.getFigure() instanceof TableEntityFigure) {
                                TableEntityFigure nodeFigure = (TableEntityFigure) tableEntityPart.getFigure();
                                index++;
                                searchMaps.put(index, nodeFigure);
                            }
                        }
                    }
                }
            }
        }
    }

    public Integer selectHightlight(Map<Integer, Figure> searchMaps, Integer selectKey, String option) {
        if (searchMaps.containsKey(selectKey)) {
            if (option.equals("next") && selectKey + 1 < searchMaps.size()) {
                if (isHightlightAll) {
                    setEntryState(mapperManager, EntryState.HIGHLIGHTALL, searchMaps.get(selectKey));
                    setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(selectKey + 1));
                } else {
                    setEntryState(mapperManager, EntryState.NONE, searchMaps.get(selectKey));
                    setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(selectKey + 1));
                }
                // move scrollBarZone at selected TableItem
                moveScrollBarZoneAtSelectedTableItem(searchMaps.get(selectKey + 1));
                return selectKey + 1;
            } else if (option.equals("previous") && selectKey > 0) {
                if (isHightlightAll) {
                    setEntryState(mapperManager, EntryState.HIGHLIGHTALL, searchMaps.get(selectKey));
                    setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(selectKey - 1));
                } else {
                    setEntryState(mapperManager, EntryState.NONE, searchMaps.get(selectKey));
                    setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(selectKey - 1));
                }
                // move scrollBarZone at selected TableItem
                moveScrollBarZoneAtSelectedTableItem(searchMaps.get(selectKey - 1));
                return selectKey - 1;
            } else if (option.equals("first")) {
                setEntryState(mapperManager, EntryState.SEARCH_HIGHLIGHT, searchMaps.get(0));
                // move scrollBarZone at selected TableItem
                moveScrollBarZoneAtSelectedTableItem(searchMaps.get(0));
                return 0;
            }
        }
        return selectKey;
    }

    public void hightlightAll(Map<Integer, Figure> searchMaps, boolean isHightlight) {
        Iterator iter = searchMaps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (isHightlight) {
                setEntryState(mapperManager, EntryState.HIGHLIGHTALL, (Figure) entry.getValue());
            } else {
                setEntryState(mapperManager, EntryState.NONE, (Figure) entry.getValue());
            }
        }
    }

    public void setEntryState(MapperManager mapperManager, EntryState entryState, Figure entry) {
        if (entry != null) {
            if (entry instanceof XmlmapTreeNodeFigure) {
                XmlmapTreeNodeFigure xmlMapTreeNodeFigure = (XmlmapTreeNodeFigure) entry;
                if (xmlMapTreeNodeFigure != null && xmlMapTreeNodeFigure.getExpressionFigure() != null) {
                    xmlMapTreeNodeFigure.getExpressionFigure().setOpaque(true);
                    xmlMapTreeNodeFigure.getExpressionFigure().setBackgroundColor(entryState.getColor());
                }
            } else if (entry instanceof VarEntityFigure) {
                VarEntityFigure varEntityFigure = (VarEntityFigure) entry;
                if (varEntityFigure != null && varEntityFigure.getExpression() != null) {
                    varEntityFigure.getExpression().setOpaque(true);
                    varEntityFigure.getExpression().setBackgroundColor(entryState.getColor());
                }
            } else if (entry instanceof XmlMapFilterContainer) {
                XmlMapFilterContainer filterText = (XmlMapFilterContainer) entry;
                if (filterText != null && filterText.getTextArea() != null) {
                    filterText.getTextArea().setOpaque(true);
                    filterText.getTextArea().setBackgroundColor(entryState.getColor());
                }
            }
        }
    }

    public void moveScrollBarZoneAtSelectedTableItem(Figure entry) {
        if (entry != null) {
            Rectangle bounds = entry.getBounds();
            int selection = bounds.y - 30;
            ScrolledComposite scrollComposite = null;
            if (scrollComposite != null) {
                setPositionOfVerticalScrollBarZone(scrollComposite, selection);
            }
        }
    }

    private void setPositionOfVerticalScrollBarZone(ScrolledComposite scrollComposite, int scrollBarSelection) {
        ScrollBar verticalBar = scrollComposite.getVerticalBar();
        verticalBar.setSelection(scrollBarSelection);
        scrollComposite.setOrigin(0, scrollBarSelection);
    }

    public Integer getSelectedKeyAtSelectedTableItem(Map<Integer, Figure> searchMaps) {
        Integer selectKey = 0;
        IFigure figure = mapperManager.getSelectedFigure();
        if (figure != null && searchMaps.containsValue(figure)) {
            Iterator iter = searchMaps.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (entry.getValue() != null && entry.getValue() instanceof IFigure) {
                    IFigure figureTemp = (IFigure) entry.getValue();
                    if (figure.equals(figureTemp)) {
                        mapperManager.setSelectedFigure(null);
                        return (Integer) entry.getKey();
                    }
                }
            }
        }

        return selectKey;
    }

    public boolean isHightlightAll() {
        return this.isHightlightAll;
    }

    public void setHightlightAll(boolean isHightlightAll) {
        this.isHightlightAll = isHightlightAll;
    }
}
