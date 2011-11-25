package org.talend.designer.business.model.business.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * @generated
 */
public class BusinessItemRelationshipItemSemanticEditPolicy extends BusinessBaseItemSemanticEditPolicy {

    /**
     * @generated
     */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        return getMSLWrapper(new DestroyElementCommand(req));
    }
}
