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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.talend.designer.xmlmap.figures.treetools.TreeToolBarContainer;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;

/**
 * DOC talend class global comment. Detailled comment
 */
public class AbstractInputTreeFigure extends GenericFigure {

    protected Figure header;

    protected TreeToolBarContainer imageButtonsFigure;

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
        }
    }
}
