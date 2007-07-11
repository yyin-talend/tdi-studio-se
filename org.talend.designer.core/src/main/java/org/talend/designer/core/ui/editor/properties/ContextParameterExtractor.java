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
package org.talend.designer.core.ui.editor.properties;

import java.util.List;

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
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
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
    public static void installOn(final Control text, final Process process, final String parameterName,
            final Element elem) {
        text.addKeyListener(new KeyAdapter() {

            @SuppressWarnings("unchecked")
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.F5) {
                    IContextParameter parameter = buildParameterFrom(text, process.getContextManager(), parameterName);
                    ContextParameterWizard prmWizard = new ContextParameterWizard(process.getContextManager(),
                            parameter);
                    WizardDialog dlg = new WizardDialog(text.getShell(), prmWizard);
                    if (dlg.open() == WizardDialog.OK) {
                        String replaceCode = ContextParameterUtils.getScriptCode(parameter,
                                ((RepositoryContext) CorePlugin.getContext()
                                        .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject().getLanguage());
                        if (text instanceof Text) {
                            if (((Text) text).getSelectionCount() == 0) {
                                ((Text) text).setText(replaceCode);
                            } else {
                                ((Text) text).insert(replaceCode);

                            }
                            saveContext(parameterName, elem, ((Text) text).getText());
                        } else {
                            if (text instanceof StyledText) {
                                if (((StyledText) text).getSelectionCount() == 0) {
                                    ((StyledText) text).setText(replaceCode);
                                } else {
                                    ((StyledText) text).insert(replaceCode);

                                }
                                saveContext(parameterName, elem, ((StyledText) text).getText());
                            }
                        }
                    }
                }
            }

        });
    }

    /**
     * qzhang Comment method "saveContext".
     * 
     * @param parameterName
     * @param elem
     * @param replaceCode
     */
    public static void saveContext(final String parameterName, final Element elem, String replaceCode) {
        PropertyChangeCommand cmd = new PropertyChangeCommand(elem, parameterName, replaceCode);
        cmd.execute();
        
        // note that no undo will be available
    }

    private static IContextParameter buildParameterFrom(final Control text, final IContextManager manager,
            final String parameterName) {
        String nameProposal = ""; //$NON-NLS-1$
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
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            if (nameProposal.startsWith("\"") && nameProposal.endsWith("\"") && (nameProposal.length() > 1)) {
                nameProposal = nameProposal.substring(1, nameProposal.length() - 1);
            }
        }

        String value = nameProposal;

        nameProposal = nameProposal.replace(" ", "_");

        IContextParameter parameter = new JobContextParameter();
        if (manager.checkValidParameterName(parameterName)) {
            parameter.setName(parameterName);
        } else if (manager.checkValidParameterName(nameProposal)) {
            parameter.setName(nameProposal);
        } else {
            parameter.setName(""); //$NON-NLS-1$
        }
        ECodeLanguage curLanguage = LanguageManager.getCurrentLanguage();
        if (curLanguage == ECodeLanguage.PERL) {
            parameter.setType(MetadataTalendType.getDefaultTalendType());
        } else {
            parameter.setType(JavaTypesManager.getDefaultJavaType().getId());
        }
        parameter.setPrompt(parameterName + "?"); //$NON-NLS-1$
        parameter.setComment("");
        parameter.setValue(value);
        return parameter;
    }
}
