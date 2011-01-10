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
package org.talend.designer.components;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.core.model.components.IComponentsFactory;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public class LocalComponentsProvider extends AbstractComponentsProvider {

    /**
     * DOC guanglong.du LocalComponentsProvider constructor comment.
     */
    public LocalComponentsProvider() {

    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.core.model.components.AbstractComponentsProvider#getExternalComponentsLocation()
     */
    protected File getExternalComponentsLocation() {
        URL url = FileLocator.find(ComponentsLocalProviderPlugin.getDefault().getBundle(), new Path("components"), null); //$NON-NLS-1$
        URL fileUrl;
        try {
            fileUrl = FileLocator.toFileURL(url);
            return new File(fileUrl.getPath());
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    @Override
    public void preComponentsLoad() throws IOException {
        File installationFolder = getInstallationFolder();

        FilesUtils.createFoldersIfNotExists(installationFolder.getAbsolutePath(), false);

    }

    @Override
    public String getComponentsLocation() {
        return new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER).toString();
    }

    @Override
    public File getInstallationFolder() throws IOException {
        Bundle b = Platform.getBundle(IComponentsFactory.COMPONENTS_LOCATION);

        File installationFolder = null;
        IPath nullPath = new Path(""); //$NON-NLS-1$
        URL url = FileLocator.find(b, nullPath, null);
        URL fileUrl = FileLocator.toFileURL(url);
        File bundleFolder = new File(fileUrl.getPath());

        IPath path = new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER);

        installationFolder = new File(bundleFolder, path.toOSString());

        return installationFolder;
    }
}
