// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.maven;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.designer.maven.pom.PomUtil;
import org.talend.designer.runprocess.java.JavaProcessorUtilities;

public class MavenDeployUtil {

    public static void deployToLocalMaven(IProgressMonitor monitor, String[] jarsOrMvnURls) throws Exception {
        if (jarsOrMvnURls != null) {
            // unify
            Set<String> unifiedMvnUrls = new LinkedHashSet<String>();
            for (String name : jarsOrMvnURls) {
                if (PomUtil.isMvnUrl(name)) {
                    unifiedMvnUrls.add(name);
                } else {
                    unifiedMvnUrls.add(PomUtil.buildMvnUrlByJarName(name));
                }
            }
            Set<String> availableMvnUrls = PomUtil.availableArtifacts(monitor, unifiedMvnUrls.toArray(new String[0]));

            Set<String> unavailableMvnUrls = new LinkedHashSet<String>(unifiedMvnUrls);
            if (availableMvnUrls != null) {
                unavailableMvnUrls.removeAll(availableMvnUrls);
            }
            // 1, if have been existed in lib or bundles, install jar to local
            // TODO

            // 2, still not existed some.
            if (!unavailableMvnUrls.isEmpty() && !CommonsPlugin.isHeadless()) {
                // for studio, popup the install modules dialog to install it????
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
                    ILibraryManagerService libService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                            ILibraryManagerService.class);
                    File libDir = JavaProcessorUtilities.getJavaProjectLibFolder();
                    libService.retrieve(unavailableMvnUrls, libDir.getAbsolutePath());
                }
            }

        }
    }
}
