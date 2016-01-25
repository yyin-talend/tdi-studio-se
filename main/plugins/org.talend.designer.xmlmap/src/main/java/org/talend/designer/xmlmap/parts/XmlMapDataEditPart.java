// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.parts;

import java.util.ArrayList;
import java.util.List;

import org.talend.designer.gefabstractmap.figures.layout.ZoneContentLayout;
import org.talend.designer.gefabstractmap.figures.treetools.zone.InputZoneToolBar;
import org.talend.designer.gefabstractmap.figures.treetools.zone.OutputZoneToolBar;
import org.talend.designer.gefabstractmap.part.MapperRootEditPart;
import org.talend.designer.xmlmap.figures.layout.XmlMapTreeContainerLayout;
import org.talend.designer.xmlmap.figures.treetools.zone.XmlMapInputZoneToolBar;
import org.talend.designer.xmlmap.figures.treetools.zone.XmlMapOutputZoneToolBar;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapDataEditPart extends MapperRootEditPart {

    @Override
    public List getModelChildren() {
        List children = new ArrayList();
        children.addAll(((XmlMapData) getModel()).getInputTrees());
        children.addAll(((XmlMapData) getModel()).getOutputTrees());
        children.addAll(((XmlMapData) getModel()).getVarTables());
        return children;
    }

    @Override
    protected XmlMapDataManager createRootModelManager() {
        return new XmlMapDataManager((XmlMapData) getModel(), this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.newabstractmap.part.MapperRootEditPart#createOutputZoneToolBar()
     */
    @Override
    protected OutputZoneToolBar createOutputZoneToolBar() {
        return new XmlMapOutputZoneToolBar((XmlMapDataManager) getRootModelManager());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.newabstractmap.part.MapperRootEditPart#createInputZoneToolBar()
     */
    @Override
    protected InputZoneToolBar createInputZoneToolBar() {
        return new XmlMapInputZoneToolBar((XmlMapDataManager) getRootModelManager());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.newabstractmap.part.MapperRootEditPart#createTreeContainerLayout()
     */
    @Override
    protected ZoneContentLayout createZoneContentLayout() {
        return new XmlMapTreeContainerLayout();
    }

    // @Override
    // public XmlMapDataManager getRootModelManager() {
    // return (XmlMapDataManager) super.getRootModelManager();
    // }

}
