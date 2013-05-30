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
package org.talend.designer.codegen;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.ComponentCompilations;
import org.talend.core.model.process.Element;
import org.talend.designer.codegen.model.CodeGeneratorEmittersPoolFactory;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.ui.views.properties.IComponentSettingsView;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RefreshTemplatesAction extends Action {

    public RefreshTemplatesAction() {
        super();
        this.setActionDefinitionId("refreshTemplates"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        // TDI-25866:In case select a component and sctrl+shift+f3,need clean its componentSetting view
        IComponentSettingsView viewer = (IComponentSettingsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().findView(IComponentSettingsView.ID);

        Element oldComponent = null;

        if (viewer != null) {
            oldComponent = viewer.getElement();
            viewer.cleanDisplay();
        }
        ComponentCompilations.deleteMarkers();
        ComponentsFactoryProvider.getInstance().resetCache();
        CorePlugin.getDefault().getLibrariesService().syncLibraries();
        CodeGeneratorEmittersPoolFactory.initialize();
        // achen modify to record ctrl+shift+f3 is pressed to fix bug 0006107
        IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                IDesignerCoreService.class);
        designerCoreService.getLastGeneratedJobsDateMap().clear();

        if (oldComponent != null) {
            viewer.setElement(oldComponent);
        }
    }

}
