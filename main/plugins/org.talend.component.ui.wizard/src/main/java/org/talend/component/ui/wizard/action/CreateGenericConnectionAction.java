package org.talend.component.ui.wizard.action;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.component.ui.wizard.i18n.Messages;
import org.talend.component.ui.wizard.ui.GenericConnWizard;
import org.talend.component.ui.wizard.util.GenericWizardServiceFactory;
import org.talend.component.ui.wizard.view.tester.GenericConnectionTester;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * created by ycbai on 2015年9月14日 Detailled comment
 *
 */
public class CreateGenericConnectionAction extends AbstractCreateAction {

    private static final int DEFAULT_WIZARD_WIDTH = 700;

    private static final int DEFAULT_WIZARD_HEIGHT = 400;

    private boolean creation = true;

    private GenericConnectionTester connectionTester;

    public CreateGenericConnectionAction() {
        super();
        this.setText(getCreateLabel());
        this.setToolTipText(getEditLabel());
        this.setImageDescriptor(ImageDescriptor.createFromImage(getNodeImage()));
        connectionTester = new GenericConnectionTester();
    }

    @Override
    protected void doRun() {
        if (repositoryNode == null) {
            repositoryNode = getCurrentRepositoryNode();
        }
        IWizard wizard = new GenericConnWizard(PlatformUI.getWorkbench(), creation, repositoryNode, getExistingNames());
        WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        if (Platform.getOS().equals(Platform.OS_LINUX)) {
            wizardDialog.setPageSize(getWizardWidth(), getWizardHeight() + 80);
        }
        wizardDialog.create();
        wizardDialog.open();
    }

    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == null) {
            return;
        }

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (connectionTester.isGenericConnection(node)) {
            if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)
                    || (node.getObject() != null && factory.getStatus(node.getObject()) == ERepositoryStatus.DELETED)) {
                setEnabled(false);
                return;
            }
            this.setText(getCreateLabel());
            collectChildNames(node);
            creation = true;
            setEnabled(true);
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
            if (node.getObject() != null && node.getObject().getProperty().getItem().getState().isDeleted()) {
                setEnabled(false);
                return;
            }
            break;
        case SYSTEM_FOLDER:
            if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                setEnabled(false);
                return;
            }
            this.setText(getCreateLabel());
            collectChildNames(node);
            creation = true;
            break;
        case REPOSITORY_ELEMENT:
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(getEditLabel());
                collectSiblingNames(node);
            } else {
                this.setText(getOpenLabel());
            }
            collectSiblingNames(node);
            creation = false;
            break;
        default:
            return;
        }

        setEnabled(true);
    }

    protected int getWizardWidth() {
        return DEFAULT_WIZARD_WIDTH;
    }

    protected int getWizardHeight() {
        return DEFAULT_WIZARD_HEIGHT;
    }

    protected String getCreateLabel() {
        return Messages.getString("CreateGenericConnectionAction", getNodeLabel()); //$NON-NLS-1$
    }

    protected String getEditLabel() {
        return Messages.getString("CreateGenericConnectionAction", getNodeLabel()); //$NON-NLS-1$
    }

    protected String getOpenLabel() {
        return Messages.getString("CreateGenericConnectionAction", getNodeLabel()); //$NON-NLS-1$
    }

    protected String getNodeLabel() {
        return repositoryNode.getContentType().getLabel();
    }

    protected Image getNodeImage() {
        return GenericWizardServiceFactory.getGenericWizardService().getNodeImage(repositoryNode.getContentType().getType());
    }

    @Override
    public Class getClassForDoubleClick() {
        return SalesforceSchemaConnectionItem.class;
    }

}
