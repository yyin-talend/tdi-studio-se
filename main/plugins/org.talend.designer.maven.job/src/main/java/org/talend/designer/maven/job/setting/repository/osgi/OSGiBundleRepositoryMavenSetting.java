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
package org.talend.designer.maven.job.setting.repository.osgi;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.preference.IPreferenceNode;
import org.talend.designer.maven.job.setting.repository.osgi.node.OSGiBundlePomRepositorySettingNode;
import org.talend.designer.maven.job.setting.repository.osgi.node.OSGiBundleRepositorySettingNode;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.ui.setting.repository.RepositoryMavenSetting;
import org.talend.designer.maven.ui.setting.repository.node.RepositoryPreferenceNode;
import org.talend.designer.maven.ui.utils.DesignerMavenUiHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class OSGiBundleRepositoryMavenSetting extends RepositoryMavenSetting {

    @Override
    public void createMavenScriptsChildren(RepositoryPreferenceNode parentNode, RepositoryNode node) {
        OSGiBundleRepositorySettingNode osgiBundleNode = new OSGiBundleRepositorySettingNode(parentNode.getId(), node);
        parentNode.add(osgiBundleNode);

        IFolder nodeFolder = DesignerMavenUiHelper.getNodeFolder(node);

        List<IPreferenceNode> osgiBundleChildrenNodes = createOSGiBundleChildrenNodes(nodeFolder, node, osgiBundleNode.getId(),
                true);
        for (IPreferenceNode n : osgiBundleChildrenNodes) {
            osgiBundleNode.add(n);
        }
    }

    public static List<IPreferenceNode> createOSGiBundleChildrenNodes(IFolder nodeFolder, RepositoryNode node, String parentId,
            boolean checkExist) {
        List<IPreferenceNode> childrenNodes = new ArrayList<IPreferenceNode>();

        String osgiBundleName = PomUtil.getPomFileName(TalendMavenConstants.OSGI_BUNDLE_NAME);
        // if have existed the pom and assembly
        if (!checkExist || DesignerMavenUiHelper.existMavenSetting(nodeFolder, osgiBundleName)) {
            IFile pomTemplateFile = nodeFolder.getFile(osgiBundleName);

            String pomId = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(parentId, pomTemplateFile);

            OSGiBundlePomRepositorySettingNode pomNode = new OSGiBundlePomRepositorySettingNode(pomId, pomTemplateFile);

            childrenNodes.add(pomNode);
        }
        return childrenNodes;
    }

}
