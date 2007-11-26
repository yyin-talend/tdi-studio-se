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
package org.talend.repository.ui.wizards.snippet;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.properties.SnippetItem;
import org.talend.core.model.snippets.SnippetManager;
import org.talend.core.ui.snippet.VariableItemEditor;

/**
 * Wizard page collecting informations to create a new IDocumentation. <br/>
 * 
 * $Id: DocumentationPage.java 1935 2007-02-09 05:58:57 +0000 (ven., 09 févr. 2007) bqian $
 * 
 */
public class SnippetPage extends WizardPage {

    SnippetManager snippetManager;

    SnippetItem snippetItem;

    boolean readOnly;

    private VariableItemEditor snippetEditor;

    protected SnippetPage(String pageName, SnippetItem snippetItem, SnippetManager snippetManager, boolean readOnly) {
        super(pageName);
        this.snippetManager = snippetManager;
        this.readOnly = readOnly;
        this.snippetItem = snippetItem;
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());

        snippetEditor = new VariableItemEditor();
        snippetEditor.setItem(snippetItem);
        snippetEditor.createContents(composite);
        setControl(composite);
    }

    public void updateItem() {
        snippetEditor.updateItem();
    }

}
