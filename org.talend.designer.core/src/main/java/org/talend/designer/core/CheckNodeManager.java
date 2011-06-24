package org.talend.designer.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;

public class CheckNodeManager {

    private static final IExtensionPointLimiter CHECK_NODES = new ExtensionPointLimiterImpl(
            "org.talend.designer.core.check_nodes", "checknodes", 1, -1);

    public static List<ICheckNodesService> getCheckNodesService() {
        List<ICheckNodesService> checkNodeServices = new ArrayList<ICheckNodesService>();
        List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(CHECK_NODES);

        for (IConfigurationElement current : extension) {
            try {
                Object object = current.createExecutableExtension("class");
                if (object instanceof ICheckNodesService) {
                    checkNodeServices.add((ICheckNodesService) object);
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        return checkNodeServices;
    }
}
