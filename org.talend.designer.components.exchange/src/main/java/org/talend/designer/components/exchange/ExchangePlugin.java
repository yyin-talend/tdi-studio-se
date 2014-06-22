package org.talend.designer.components.exchange;

import java.io.File;
import java.util.StringTokenizer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.commons.utils.VersionUtils;
import org.talend.designer.components.exchange.util.ExchangeUtils;

/**
 * The activator class controls the plug-in life cycle
 */
public class ExchangePlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.designer.components.exchange"; //$NON-NLS-1$

    // The shared instance
    private static ExchangePlugin plugin;

    private static final String LEVEL_SEPARATOR = "."; //$NON-NLS-1$

    /**
     * The constructor
     */
    public ExchangePlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        createNecessaryFolders();
    }

    private void createNecessaryFolders() {
        File file = ExchangeUtils.getComponentFolder("/"); //$NON-NLS-1$
        if (file != null) {
            File dowloadedFoder = new File(file, ExchangeUtils.FOLDER_DOWNLOADED);
            if (!dowloadedFoder.exists()) {
                dowloadedFoder.mkdir();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static ExchangePlugin getDefault() {
        return plugin;
    }

    public static String getStudioVersion() {
        StringBuffer sb = new StringBuffer();
        StringTokenizer stringTokenizer = new StringTokenizer(VersionUtils.getVersion(), LEVEL_SEPARATOR);
        try {
            sb.append(stringTokenizer.nextToken());
            sb.append(LEVEL_SEPARATOR);
            sb.append(stringTokenizer.nextToken());
            sb.append(LEVEL_SEPARATOR);
            sb.append(stringTokenizer.nextToken());
        } catch (NumberFormatException e) {
        }
        return sb.toString();
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

}
