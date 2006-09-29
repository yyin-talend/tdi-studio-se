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
package org.talend.designer.core.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;

/**
 * Wizard for the creation of a new IContextParameter. <br/>
 * 
 * $Id$
 * 
 */
public class ContextParameterWizard extends Wizard {

    /** Manager for all contexts in the project. */
    private IContextManager contextManager;

    /** Edited parameter. */
    private IContextParameter parameter;

    private ContextParameterPage ctxPrmPage;

    /**
     * Constructs a new ContextParameterWizard.
     */
    public ContextParameterWizard(IContextManager contextManager, IContextParameter parameter) {
        super();

        this.contextManager = contextManager;
        this.parameter = parameter;

        setWindowTitle(Messages.getString("ContextParameterWizard.title")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(DesignerPlugin.getImageDescriptor("icons/ctxprm_wiz.png")); //$NON-NLS-1$
        setNeedsProgressMonitor(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        ctxPrmPage = new ContextParameterPage(contextManager);
        ctxPrmPage.setParameter(parameter, contextManager.getListContext());
        addPage(ctxPrmPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        for (IContext context : contextManager.getListContext()) {
            context.getContextParameterList().add(parameter);
        }
        contextManager.fireContextsChangedEvent();
        return true;
    }

}
