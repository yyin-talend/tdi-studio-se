// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.model.process;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.update.UpdateResult;
import org.talend.designer.core.IReplaceNodeInProcess;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC qzhang class global comment. Detailled comment
 */
public abstract class AbstractProcessProvider implements IReplaceNodeInProcess {

    public static final String EXTENSION_ID = "org.talend.designer.core.process_provider"; //$NON-NLS-1$

    public static final String ATTR_CLASS = "class"; //$NON-NLS-1$

    public static final String ATTR_PID = "pluginId"; //$NON-NLS-1$

    private static boolean canCreateNode = true;

    private static Process componentProcess = null;

    // public abstract List<Node> buildReplaceNodesInDataProcess(Node node, Map<INode, INode> buildCheckMap,
    // DataProcess currDataProcess);

    public static AbstractProcessProvider findProcessProviderFromPID(String pid) {
        IConfigurationElement[] elems = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
        for (IConfigurationElement elem : elems) {
            String attribute = elem.getAttribute(ATTR_PID);
            if (attribute.equals(pid)) {
                try {
                    AbstractProcessProvider createExecutableExtension = (AbstractProcessProvider) elem
                            .createExecutableExtension(ATTR_CLASS);
                    return createExecutableExtension;
                } catch (CoreException ex) {
                    // ex.printStackTrace();
                    ExceptionHandler.process(ex);
                }
            }
        }
        return null;
    }

    public static List<AbstractProcessProvider> findAllProcessProviders() {
        List<AbstractProcessProvider> processProviders = new ArrayList<AbstractProcessProvider>();
        IConfigurationElement[] elems = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
        for (IConfigurationElement elem : elems) {
            try {
                AbstractProcessProvider createExecutableExtension = (AbstractProcessProvider) elem
                        .createExecutableExtension(ATTR_CLASS);
                processProviders.add(createExecutableExtension);
            } catch (CoreException ex) {
                // ex.printStackTrace();
                ExceptionHandler.process(ex);
            }
        }
        return processProviders;
    }

    /**
     * DOC qzhang Comment method "isExtensionProcessForJoblet".
     * 
     * @param process
     * @return
     */
    public static boolean isExtensionProcessForJoblet(IProcess process) {
        AbstractProcessProvider findProcessProvider = findProcessProviderFromPID(IComponent.JOBLET_PID);
        if (findProcessProvider != null) {
            return findProcessProvider.isExtensionProcess(process);
        }
        return false;
    }

    /**
     * DOC qzhang Comment method "openNewProcessEditor".
     */
    public void openNewProcessEditor(Node node) {
        // do nothing.
    }

    public void loadComponentsFromExtensionPoint() {
        // do nothing.
    }

    public abstract Process buildNewGraphicProcess(Item node);

    /**
     * DOC qzhang Comment method "loadComponentsFromProviders".
     * 
     * @return
     */
    public static void loadComponentsFromProviders() {

        for (AbstractProcessProvider processProvider : findAllProcessProviders()) {
            processProvider.loadComponentsFromExtensionPoint();
        }
    }

    /**
     * DOC qzhang Comment method "canDeleteNode".
     * 
     * @param no
     * 
     * @return
     */
    public boolean canDeleteNode(Node no) {
        return true;
    }

    public boolean canCopyNode(Node no) {
        return true;
    }

    public boolean canCutNode(Node no) {
        return true;
    }

    // public abstract void updateJobletContext(Node node);

    /**
     * DOC qzhang Comment method "updateJobletContext".
     * 
     * @param nodes
     */
    public abstract List<String> updateProcessContexts(Process process);

    public abstract List<String> updateProcessContextsWithoutUI(Process process);

    /**
     * 
     * ggu Comment method "checkJobletNodeSchema".
     * 
     * 
     */
    public abstract List<UpdateResult> checkJobletNodeSchema(Process process);

    public abstract boolean hasJobletComponent(Process process);

    public abstract ImageDescriptor getIcons(IProcess process);

    public abstract void setIcons(IProcess process, ImageDescriptor image);

    /**
     * DOC qzhang Comment method "canCreate".
     * 
     * @param node
     */
    public boolean canCreateNode(Node node) {
        return true;
    }

    public abstract List<PaletteEntry> addJobletEntry();

    public boolean isExtensionComponent(Node node) {
        return false;
    }

    /**
     * DOC qli Comment method "isExtensionProcess".
     * 
     * @param process
     * 
     * @return
     */
    public boolean isJobletInputOrOutputComponent(Node node) {
        return false;
    }

    /**
     * DOC qli Comment method "isExtensionProcess".
     * 
     * @param process
     * 
     * @return
     */
    public boolean isExtensionProcess(IProcess process) {
        return false;
    }

    public abstract IProcess getProcessFromJobletProcessItem(JobletProcessItem jobletProcessItem);

    /**
     * 
     * cLi Comment method "getJobletItem".
     * 
     * bug 6158
     */
    public abstract Item getJobletItem(INode node);
	// for feature 13361
    public abstract Item getJobletItem(INode node, String version);

    public boolean containNodeInMemoryNotProcess() {
        return this.canCreateNode;
    }

    public void setCanCreateNode(boolean flag) {
        this.canCreateNode = flag;
    }

    public static Process getComponentProcess() {
        return componentProcess;
    }

    public static void setComponentProcess(Process componentProcess) {
        AbstractProcessProvider.componentProcess = componentProcess;
    }

}
