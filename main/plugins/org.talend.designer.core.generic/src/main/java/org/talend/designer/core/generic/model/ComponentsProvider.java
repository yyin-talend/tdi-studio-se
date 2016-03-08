// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.generic.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.update.UpdateResult;
import org.talend.designer.core.generic.palette.ComponentsPaletteFactory;
import org.talend.designer.core.generic.utils.ComponentsUtils;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.ProjectManager;

/**
 * created by hcyi on Sep 11, 2015 Detailled comment
 *
 */
// TUP-4152
public class ComponentsProvider extends AbstractProcessProvider {

    private static Logger log = Logger.getLogger(ComponentsProvider.class);

    @Override
    public void loadComponentsFromExtensionPoint() {
        if (ProjectManager.getInstance().getCurrentProject() != null) {
            ComponentsUtils.loadComponents(ComponentsUtils.getComponentService());
        }
    }

    @Override
    public void rebuildGraphicProcessFromNode(INode node, List<INode> graphicalNodeList) {
    }

    @Override
    public boolean isNeedForceRebuild(IProcess2 process) {
        return false;
    }

    @Override
    public void beforeRunJobInGUI(IProcess2 process) {
    }

    @Override
    public Process buildNewGraphicProcess(Item node) {
        return null;
    }

    @Override
    public Process buildNewGraphicProcess(Item node, boolean needScreenshot) {
        return null;
    }

    @Override
    public List<String> updateProcessContexts(IProcess process) {
        return null;
    }

    @Override
    public List<String> updateProcessContextsWithoutUI(IProcess process) {
        return null;
    }

    @Override
    public List<UpdateResult> checkJobletNodeSchema(IProcess process) {
        return null;
    }

    @Override
    public boolean hasJobletComponent(IProcess process) {
        return false;
    }

    @Override
    public ImageDescriptor getIcons(IProcess2 process) {
        return null;
    }

    @Override
    public void setIcons(IProcess process, ImageDescriptor image) {
    }

    @Override
    public List<PaletteEntry> addJobletEntry() {
        return ComponentsPaletteFactory.createPalette();
    }

    @Override
    public IProcess getProcessFromJobletProcessItem(JobletProcessItem jobletProcessItem) {
        return null;
    }

    @Override
    public Item getJobletItem(INode node) {
        return null;
    }

    @Override
    public Item getJobletItem(INode node, String version) {
        return null;
    }
}
