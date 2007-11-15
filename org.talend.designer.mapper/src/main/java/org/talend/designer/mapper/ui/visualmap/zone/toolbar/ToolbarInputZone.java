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
package org.talend.designer.mapper.ui.visualmap.zone.toolbar;

import org.eclipse.swt.widgets.Composite;
import org.talend.designer.mapper.i18n.Messages;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ToolbarInputZone extends ToolbarZone {

    public static final String MINIMIZE_TOOLTIP = Messages.getString("ToolbarInputZone.minimizeTooltip"); //$NON-NLS-1$

    public static final String RESTORE_TOOLTIP = Messages.getString("ToolbarInputZone.restoreTooltip"); //$NON-NLS-1$

    private static final String MOVE_UP_TOOLTIP = Messages.getString("ToolbarInputZone.moveupTooltip"); //$NON-NLS-1$

    private static final String MOVE_DOWN_TOOLTIP = Messages.getString("ToolbarInputZone.movedownTooltip"); //$NON-NLS-1$

    /**
     * DOC amaumont MatadataToolbarEditor constructor comment.
     * 
     * @param parent
     * @param style
     * @param manager
     * @param metadatEditorView
     */
    public ToolbarInputZone(Composite parent, int style, MapperManager manager) {
        super(parent, style, manager);
        addCommonsComponents();
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
        return Zone.INPUTS;
    }

    public String getMoveUpTooltipText() {
        return MOVE_UP_TOOLTIP;
    }

    public String getMoveDownTooltipText() {
        return MOVE_DOWN_TOOLTIP;
    }

}
