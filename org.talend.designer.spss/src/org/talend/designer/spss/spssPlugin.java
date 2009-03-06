package org.talend.designer.spss;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;


public class spssPlugin  extends Plugin {

    public static final String PLUGIN_ID = "org.talend.designer.spss";
    private static spssPlugin plugin;

    public spssPlugin() {
        plugin = this;
    }
    
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
    
    public static spssPlugin getDefault() {
        return plugin;
    }
    
    public ICodeGeneratorService getCodeGeneratorService() {
        IService service = GlobalServiceRegister.getDefault().getService(ICodeGeneratorService.class);
        return (ICodeGeneratorService) service;
    }

    public IRunProcessService getRunProcessService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        return (IRunProcessService) service;
    }

    public IRepositoryService getRepositoryService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        return (IRepositoryService) service;
    }

    public IProxyRepositoryFactory getProxyRepositoryFactory() {
        IRepositoryService service = getRepositoryService();
        return service.getProxyRepositoryFactory();
    }

    public static void log(Exception e) {
    	ExceptionHandler.process(e);
    }
}
