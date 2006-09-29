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
package org.talend.designer.core.ui.editor.properties;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.EMetadataType;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.designer.core.model.context.ContextParameter;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.wizards.ContextParameterWizard;

/**
 * Extract context parameter from the GUI. <br/>
 * 
 * $Id$
 * 
 */
public final class ContextParameterExtractor {

    /**
     * Constructs a new ContextParameterExtractor.
     */
    private ContextParameterExtractor() {
    }

    /**
     * Install a context parameter extracter on a text field associated with a given process.
     * 
     * @param text Component on wich extractor is installed.
     * @param process Process on wich context parameter is added.
     */
    public static void installOn(final Control text, final Process process) {
        text.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.F5) {
                    IContextParameter parameter = buildParameterFrom(text, process.getContextManager());
                    ContextParameterWizard prmWizard = new ContextParameterWizard(process.getContextManager(),
                            parameter);
                    WizardDialog dlg = new WizardDialog(text.getShell(), prmWizard);
                    if (dlg.open() == WizardDialog.OK) {
                        String replaceCode = ContextParameterUtils.getScriptCode(parameter,
                                ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                                        .getProject().getLanguage());
                        if (text instanceof Text) {
                            if (((Text) text).getSelectionCount() == 0) {
                                ((Text) text).setText(replaceCode);
                            } else {
                                ((Text) text).insert(replaceCode);

                            }
                        } else {
                            if (text instanceof StyledText) {
                                if (((StyledText) text).getSelectionCount() == 0) {
                                    ((StyledText) text).setText(replaceCode);
                                } else {
                                    ((StyledText) text).insert(replaceCode);

                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private static IContextParameter buildParameterFrom(final Control text, final IContextManager manager) {
        String nameProposal = "";
        if (text instanceof Text) {
            nameProposal = ((Text) text).getSelectionText();
            if (nameProposal.length() == 0) {
                nameProposal = ((Text) text).getText();
            }
        } else {
            if (text instanceof StyledText) {
                nameProposal = ((StyledText) text).getSelectionText();
                if (nameProposal.length() == 0) {
                    nameProposal = ((StyledText) text).getText();
                }
            }
        }

        IContextParameter parameter = new ContextParameter();
        if (manager.checkValidParameterName(nameProposal)) {
            parameter.setName(nameProposal);
        } else {
            parameter.setName("");
        }
        parameter.setType(EMetadataType.STRING);
        parameter.setPrompt(nameProposal + "?");
        parameter.setValue(nameProposal);
        return parameter;
    }
}
