package org.talend.repository.view.di;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.repository.navigator.TalendRepositoryRoot;
import org.talend.repository.view.di.viewer.content.ContentAdapterFactory;

/**
 * The activator class controls the plug-in life cycle register adapters
 */
public class DIRepositoryViewPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.repository.view.di"; //$NON-NLS-1$

    // The shared instance
    private static DIRepositoryViewPlugin plugin;

    /**
     * The constructor
     */
    public DIRepositoryViewPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        registerAllAdapters();
    }

    /**
     * Register all adatpters caus using the extension points does not activate the adapters automatically.
     * 
     * DOC sgandon Comment method "registerAllAdapters".
     */
    private void registerAllAdapters() {
        Platform.getAdapterManager().registerAdapters(new ContentAdapterFactory(), TalendRepositoryRoot.class);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static DIRepositoryViewPlugin getDefault() {
        return plugin;
    }

}
