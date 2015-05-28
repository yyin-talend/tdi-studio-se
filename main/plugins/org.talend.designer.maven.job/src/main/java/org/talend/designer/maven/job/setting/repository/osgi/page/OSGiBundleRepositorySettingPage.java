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
package org.talend.designer.maven.job.setting.repository.osgi.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.runtime.projectsetting.IProjectSettingContainer;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.maven.job.MavenJobPlugin;
import org.talend.designer.maven.job.i18n.Messages;
import org.talend.designer.maven.job.setting.repository.osgi.OSGiBundleRepositoryMavenSetting;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.ui.setting.repository.page.FolderMavenSettingPage;
import org.talend.designer.maven.ui.utils.DesignerMavenUiHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("rawtypes")
public class OSGiBundleRepositorySettingPage extends FolderMavenSettingPage {

    protected static final String ID_MAVEN_OSGI_BUNDLE_PROJECT_SETTING = "projectsetting.OSGiBundle"; //$NON-NLS-1$

    private final String templateFileName;

    public OSGiBundleRepositorySettingPage(RepositoryNode node) {
        super(node);
        this.templateFileName = PomUtil.getPomFileName(TalendMavenConstants.OSGI_BUNDLE_NAME);
    }

    @Override
    protected boolean checkMavenScriptsExisted(IFolder nodeFolder) {
        return DesignerMavenUiHelper.existMavenSetting(nodeFolder, this.templateFileName);
    }

    protected String createMessages(boolean created) {
        StringBuffer messages = new StringBuffer(200);
        // existed
        if (created) {
            String pomLinkStr = buildLink(templateFileName);
            messages.append(Messages.getString("OSGiBundleRepositorySettingPage_ExistedMavenSettingMessage",//$NON-NLS-1$
                    pomLinkStr));
        } else {
            messages.append(Messages.getString("OSGiBundleRepositorySettingPage_CreatingMavenSettingMessage", //$NON-NLS-1$
                    TalendMavenConstants.POM_FILE_NAME, TalendMavenConstants.ASSEMBLY_FILE_NAME));
            messages.append('\n');
            messages.append('\n');

            messages.append(Messages.getString("RepositorySettingPage_CreatingMavenSettingNote"));//$NON-NLS-1$
            messages.append(' ');
            String mvnProjectSettingLinkStr = "<a href=\"" + ID_MAVEN_OSGI_BUNDLE_PROJECT_SETTING + "\">Default</a>";//$NON-NLS-1$ //$NON-NLS-2$
            messages.append(Messages.getString("RepositorySettingPage_CreatingMavenSettingNoteMessage", //$NON-NLS-1$
                    mvnProjectSettingLinkStr));
        }
        messages.append('\n');

        return messages.toString();
    }

    protected void processLinkId(SelectionEvent e) {
        String id = e.text;
        if (ID_MAVEN_OSGI_BUNDLE_PROJECT_SETTING.equals(id)) {
            openProjectSettingDialog(id);
        }
        processLinkIdForFileNames(id, templateFileName);
        super.processLinkId(e);
    }

    protected void createMavenFiles() {
        processFiles(new RepositoryWorkUnit(createBtn.getText()) {

            @Override
            protected void run() throws LoginException, PersistenceException {

                ProjectPreferenceManager projectPreferenceManager = MavenJobPlugin.getDefault().getProjectPreferenceManager();

                final IFolder nodeFolder = DesignerMavenUiHelper.getNodeFolder(getNode());
                final IFile pomFile = nodeFolder.getFile(templateFileName);
                File pomF = pomFile.getLocation().toFile();

                boolean ok = true;
                try {
                    String pomContent = projectPreferenceManager
                            .getValue(IProjectSettingPreferenceConstants.TEMPLATE_OSGI_BUNDLE_POM);
                    if (pomContent == null) { // use project setting always
                        // pomContent =
                        // MavenTemplateManager.getTemplateContent(MavenTemplateConstants.POM_JOB_TEMPLATE_FILE_NAME);
                    }
                    if (pomContent != null && pomContent.length() > 0) {
                        writeContent(pomF, pomContent);
                    } else {
                        ok = false;
                    }
                } catch (Exception e) {
                    ok = false;
                    ExceptionHandler.process(e);
                }

                if (ok) {
                    // update the tree view to add the new nodes
                    final Shell shell = OSGiBundleRepositorySettingPage.this.getShell();
                    shell.getDisplay().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            // have created,no need check again.
                            List<IPreferenceNode> autonomousJobChildrenNodes = OSGiBundleRepositoryMavenSetting
                                    .createOSGiBundleChildNode(nodeFolder, getNode(), getPrefNodeId(), false);

                            IPreferencePageContainer container = getContainer();
                            if (container instanceof IProjectSettingContainer) {
                                ((IProjectSettingContainer) container).addChildrenPreferenceNodes(getPrefNodeId(),
                                        autonomousJobChildrenNodes);
                            }
                        }
                    });

                } else { // if failed, clean the created files.
                    if (pomF.exists()) {
                        pomF.delete();
                    }

                    //
                    showErrorDialog(nodeFolder.getProjectRelativePath().toString());
                }

                // refresh
                try {
                    nodeFolder.refreshLocal(IResource.DEPTH_ONE, null);
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }

        });

    }

    protected void deleteMavenFiles() {
        processFiles(new RepositoryWorkUnit(deleteBtn.getText()) {

            @Override
            protected void run() throws LoginException, PersistenceException {
                //
                try {
                    final IFolder nodeFolder = DesignerMavenUiHelper.getNodeFolder(getNode());

                    final IFile pomFile = nodeFolder.getFile(templateFileName);

                    pomFile.delete(true, null);

                    // update the tree view to add the new nodes
                    final Shell shell = OSGiBundleRepositorySettingPage.this.getShell();
                    shell.getDisplay().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            List<String> childIds = new ArrayList<String>();

                            // pom and assembly
                            String pomId = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(getPrefNodeId(), pomFile);
                            childIds.add(pomId);

                            IPreferencePageContainer container = getContainer();
                            if (container instanceof IProjectSettingContainer) {
                                ((IProjectSettingContainer) container).removeChildrenPreferenceNodes(getPrefNodeId(), childIds);
                            }
                        }
                    });

                    nodeFolder.refreshLocal(IResource.DEPTH_ONE, null);
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }

        });
    }
}
