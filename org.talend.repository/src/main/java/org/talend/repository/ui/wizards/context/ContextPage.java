// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.repository.ui.wizards.context;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.IContextManager;

/**
 * Wizard page collecting informations to create a new IDocumentation. <br/>
 * 
 * $Id: DocumentationPage.java 1935 2007-02-09 05:58:57 +0000 (ven., 09 févr. 2007) bqian $
 * 
 */
public class ContextPage extends WizardPage {
    
    IContextManager contextManager;
    boolean readOnly;

    protected ContextPage(String pageName, IContextManager contextManager, boolean readOnly) {
        super(pageName);
        this.contextManager = contextManager;
        this.readOnly = readOnly;
    }

    public void createControl(Composite parent) {
        this.setControl(new ContextForm(parent, SWT.NONE, null, contextManager, readOnly));
    }

}
