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
package org.talend.repository.ui.actions;

import org.eclipse.jface.action.Action;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Action used to refresh a repository view.<br/>
 * 
 * $Id$
 * 
 */
public class RefreshAction extends Action {

    private IRepositoryView view;

    public RefreshAction(IRepositoryView view) {
        super();
        this.view = view;

        this.setText(Messages.getString("RefreshAction.text")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("RefreshAction.toolTipText")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.REFRESH_ICON));
        this.setActionDefinitionId("refresh");
    }

    public void run() {
        System.out.println("Refresh");
        view.refresh();
    }
}
