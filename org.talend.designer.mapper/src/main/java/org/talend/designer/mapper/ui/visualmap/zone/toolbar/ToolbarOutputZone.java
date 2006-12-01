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
package org.talend.designer.mapper.ui.visualmap.zone.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.core.ui.images.EImage;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ToolbarOutputZone extends ToolbarZone {

    private ToolItem addOutputItem;

    private ToolItem removeOutputItem;

    public static final String MINIMIZE_TOOLTIP = "Minimize all output tables";

    public static final String RESTORE_TOOLTIP = "Restore all output tables";

    /**
     * DOC amaumont MatadataToolbarEditor constructor comment.
     * 
     * @param parent
     * @param style
     * @param manager
     * @param metadatEditorView
     */
    public ToolbarOutputZone(Composite parent, int style, MapperManager manager) {
        super(parent, style, manager);
        createComponents();
        addListeners();
    }

    /**
     * DOC amaumont Comment method "createComponents".
     */
    private void createComponents() {

        addCommonsComponents();

        new ToolItem(getToolBarActions(), SWT.SEPARATOR);

        addOutputItem = new ToolItem(getToolBarActions(), SWT.PUSH);
        addOutputItem.setToolTipText("Add output table");
        addOutputItem.setImage(org.talend.core.ui.images.ImageProvider.getImage(org.talend.core.ui.images.ImageProvider.getImageDesc(EImage.ADD_ICON)));

        removeOutputItem = new ToolItem(getToolBarActions(), SWT.PUSH);
        removeOutputItem.setEnabled(false);
        removeOutputItem.setImage(org.talend.core.ui.images.ImageProvider.getImage(org.talend.core.ui.images.ImageProvider
                .getImageDesc(EImage.MINUS_ICON)));
        removeOutputItem.setToolTipText("Remove selected output table");

    }

    /**
     * DOC amaumont Comment method "addListeners".
     */
    private void addListeners() {
        // final UIManager uiManager = getMapperManager().getUiManager();
        addOutputItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                getMapperManager().addOutput();
            }

        });

        removeOutputItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                getMapperManager().removeSelectedOutput();
            }

        });

    }

    public String getMinimizeTooltipText() {
        return MINIMIZE_TOOLTIP;
    }

    public String getRestoreTooltipText() {
        return RESTORE_TOOLTIP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.zone.ToolbarZone#getZone()
     */
    @Override
    public Zone getZone() {
        return Zone.OUTPUTS;
    }

    /**
     * DOC amaumont Comment method "setEnabledRemoveTableButton".
     * 
     * @param b
     */
    public void setEnabledRemoveTableButton(boolean enabled) {
        removeOutputItem.setEnabled(enabled);
    }

}
