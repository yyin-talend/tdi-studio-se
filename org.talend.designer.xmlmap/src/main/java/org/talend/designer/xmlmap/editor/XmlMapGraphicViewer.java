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
package org.talend.designer.xmlmap.editor;

import java.util.Collection;

import org.eclipse.draw2d.ExclusionSearch;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;

/**
 * DOC XYuser class global comment. Detailled comment
 */
public class XmlMapGraphicViewer extends GraphicalViewerImpl {

    private MapperManager mapperManager;

    public IFigure findFigureAt(int x, int y, Collection exclude, final Conditional condition) {
        class ConditionalTreeSearch extends ExclusionSearch {

            ConditionalTreeSearch(Collection coll) {
                super(coll);
            }

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

    public MapperManager getMapperManager() {
        return mapperManager;
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }

}
