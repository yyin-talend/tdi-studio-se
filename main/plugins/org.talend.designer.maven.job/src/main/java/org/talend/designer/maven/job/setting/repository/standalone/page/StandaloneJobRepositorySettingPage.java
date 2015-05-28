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
package org.talend.designer.maven.job.setting.repository.standalone.page;

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
import org.talend.designer.maven.job.setting.repository.standalone.StandaloneJobRepositoryMavenSetting;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.ui.setting.repository.page.FolderMavenSettingPage;
import org.talend.designer.maven.ui.utils.DesignerMavenUiHelper;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("rawtypes")
public class StandaloneJobRepositorySettingPage extends FolderMavenSettingPage {

    protected static final String ID_MAVEN_AUTONOMOUS_JOB_PROJECT_SETTING = "projectsetting.StandaloneJob"; //$NON-NLS-1$

    public StandaloneJobRepositorySettingPage(RepositoryNode node) {
        super(node);
    }

    @Override
    protected boolean checkMavenScriptsExisted(IFolder nodeFolder) {
        return DesignerMavenUiHelper.existMavenSetting(nodeFolder, TalendMavenConstants.POM_FILE_NAME,
                TalendMavenConstants.ASSEMBLY_FILE_NAME);
    }

    protected String createMessages(boolean created) {
        StringBuffer messages = new StringBuffer(200);
        // existed
        if (created) {
            String pomLinkStr = buildLink(TalendMavenConstants.POM_FILE_NAME);
            String assemblyLinkStr = buildLink(TalendMavenConstants.ASSEMBLY_FILE_NAME);
            messages.append(Messages.getString("StandaloneJobRepositorySettingPage_ExistedMavenSettingMessage",//$NON-NLS-1$
                    pomLinkStr, assemblyLinkStr));
        } else {
            messages.append(Messages.getString("StandaloneJobRepositorySettingPage_CreatingMavenSettingMessage", //$NON-NLS-1$
                    TalendMavenConstants.POM_FILE_NAME, TalendMavenConstants.ASSEMBLY_FILE_NAME));
            messages.append('\n');
            messages.append('\n');

            messages.append(Messages.getString("RepositorySettingPage_CreatingMavenSettingNote"));//$NON-NLS-1$
            messages.append(' ');
            String mvnProjectSettingLinkStr = "<a href=\"" + ID_MAVEN_AUTONOMOUS_JOB_PROJECT_SETTING + "\">Default</a>";//$NON-NLS-1$ //$NON-NLS-2$
            messages.append(Messages.getString("RepositorySettingPage_CreatingMavenSettingNoteMessage", //$NON-NLS-1$
                    mvnProjectSettingLinkStr));
        }
        messages.append('\n');

        return messages.toString();
    }

    protected void processLinkId(SelectionEvent e) {
        String id = e.text;
        if (ID_MAVEN_AUTONOMOUS_JOB_PROJECT_SETTING.equals(id)) {
            openProjectSettingDialog(id);
        }
        processLinkIdForFileNames(id, TalendMavenConstants.POM_FILE_NAME, TalendMavenConstants.ASSEMBLY_FILE_NAME);
        super.processLinkId(e);
    }

    protected void createMavenFiles() {
        processFiles(new RepositoryWorkUnit(createBtn.getText()) {

            @Override
            protected void run() throws LoginException, PersistenceException {

                ProjectPreferenceManager projectPreferenceManager = MavenJobPlugin.getDefault().getProjectPreferenceManager();

                final IFolder nodeFolder = DesignerMavenUiHelper.getNodeFolder(getNode());
                final IFile pomFile = nodeFolder.getFile(TalendMavenConstants.POM_FILE_NAME);
                final IFile assemblyFile = nodeFolder.getFile(TalendMavenConstants.ASSEMBLY_FILE_NAME);
                File pomF = pomFile.getLocation().toFile();
                File assemblyF = assemblyFile.getLocation().toFile();

                boolean ok = true;
                try {
                    String pomContent = projectPreferenceManager
                            .getValue(IProjectSettingPreferenceConstants.TEMPLATE_STANDALONE_JOB_POM);
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
                    try {
                        String assemblyContent = projectPreferenceManager
                                .getValue(IProjectSettingPreferenceConstants.TEMPLATE_STANDALONE_JOB_ASSEMBLY);
                        if (assemblyContent == null) { // use project setting always
                            // assemblyContent = MavenTemplateManager
                            // .getTemplateContent(MavenTemplateConstants.ASSEMBLY_JOB_TEMPLATE_FILE_NAME);
                        }
                        if (assemblyContent != null && assemblyContent.length() > 0) {
                            writeContent(assemblyF, assemblyContent);
                        } else {
                            ok = false;
                        }
                    } catch (Exception e) {
                        ok = false;
                        ExceptionHandler.process(e);
                    }
                }

                if (ok) {
                    // update the tree view to add the new nodes
                    final Shell shell = StandaloneJobRepositorySettingPage.this.getShell();
                    shell.getDisplay().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            // have created,no need check again.
                            List<IPreferenceNode> autonomousJobChildrenNodes = StandaloneJobRepositoryMavenSetting
                                    .createAutonomousJobChildNode(nodeFolder, getNode(), getPrefNodeId(), false);

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
                    if (assemblyF.exists()) {
                        assemblyF.delete();
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

                    final IFile pomFile = nodeFolder.getFile(TalendMavenConstants.POM_FILE_NAME);
                    final IFile assemblyFile = nodeFolder.getFile(TalendMavenConstants.ASSEMBLY_FILE_NAME);

                    pomFile.delete(true, null);
                    assemblyFile.delete(true, null);

                    // update the tree view to add the new nodes
                    final Shell shell = StandaloneJobRepositorySettingPage.this.getShell();
                    shell.getDisplay().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            List<String> childIds = new ArrayList<String>();

                            // pom and assembly
                            String pomId = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(getPrefNodeId(), pomFile);
                            String assemblyId = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(getPrefNodeId(),
                                    assemblyFile);
                            childIds.add(pomId);
                            childIds.add(assemblyId);

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
