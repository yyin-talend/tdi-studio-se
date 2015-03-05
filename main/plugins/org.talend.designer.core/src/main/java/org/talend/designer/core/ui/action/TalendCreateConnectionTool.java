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
package org.talend.designer.core.ui.action;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.requests.CreationFactory;

/**
 * DOC Talend class global comment. Detailled comment
 */
public class TalendCreateConnectionTool extends TalendConnectionCreationTool implements DragTracker {

    public TalendCreateConnectionTool(CreationFactory factory) {
        super(factory, false);
        setUnloadWhenFinished(true);
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleButtonUp(int)
    // */
    // @Override
    // protected boolean handleButtonUp(int button) {
    // // if (button == 3) {
    // // super.handleButtonDown(1);
    // // }
    // return handleCreateConnection();
    // }

}
