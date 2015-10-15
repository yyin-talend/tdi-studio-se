package org.talend.component.ui.wizard.view.tester;

import org.talend.component.ui.wizard.util.GenericWizardServiceFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.tester.AbstractNodeTester;

/**
 * 
 * created by ycbai on 2015年9月16日 Detailled comment
 *
 */
public class GenericConnectionTester extends AbstractNodeTester {

    private static final String IS_GENERIC_CONNECTION = "isGenericConnection"; //$NON-NLS-1$

    @Override
    protected Boolean testProperty(Object receiver, String property, Object[] args, Object expectedValue) {
        if (receiver instanceof RepositoryNode) {
            RepositoryNode repositoryNode = (RepositoryNode) receiver;
            if (IS_GENERIC_CONNECTION.equals(property)) {
                return isGenericConnection(repositoryNode);
            }
        }
        return null;
    }

    public boolean isGenericConnection(RepositoryNode repositoryNode) {
        ERepositoryObjectType contentType = getNodeContentType(repositoryNode);
        return GenericWizardServiceFactory.getGenericWizardService().isGenericType(contentType);
    }

}
