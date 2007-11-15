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
package org.talend.repository.plugin.integration;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.ui.DisableLanguageActions;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ui.login.LoginDialog;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public class SwitchProjectAction extends Action {

    private static final boolean PLUGIN_MODEL = true;

    private static final String COMMAND_ID = "org.talend.repository.integration.bootTalend";

    private static final String CATEGORY_ID = "org.talend.repository.integration";

    public SwitchProjectAction() {

    }

    @Override
    public void run() {

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        boolean logged = false;
        LoginDialog loginDialog = new LoginDialog(shell);
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);
        logged = loginDialog.open() == LoginDialog.OK;
        if (logged) {

            // addCommand();
            new DisableLanguageActions().earlyStartup();

            new BindingActions().bind();

            IMigrationToolService toolService = CorePlugin.getDefault().getMigrationToolService();
            toolService.executeMigration(PLUGIN_MODEL);

            IRunProcessService runService = CorePlugin.getDefault().getRunProcessService();
            runService.deleteAllJobs(PLUGIN_MODEL);

            CorePlugin.getDefault().getCodeGeneratorService().generationInit();
            ComponentUtilities.isComponentPaletteNeedRefresh = true;
            CorePlugin.getDefault().getDesignerCoreService().refreshDesignerPalette();

        }

    }

    // private void addCommand() {
    //
    // ICommandService cmdService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
    // Category category = cmdService.getCategory(CATEGORY_ID);
    // if (!category.isDefined()) {
    // category.define("Talend", "");
    // }
    // Command command = cmdService.getCommand(COMMAND_ID);
    // if (!command.isDefined()) {
    // command.define("Boot Talend", "", category);
    // }
    // command.setHandler(new LaunchTalendHandler());
    //
    // IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
    // LaunchTalendHandler handler = new LaunchTalendHandler();
    // service.activateHandler(COMMAND_ID, handler);
    // }
}
