package org.talend.designer.business.model.business.diagram.edit.helpers;

import java.util.Iterator;

import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.talend.designer.business.diagram.custom.commands.UnassignTalendItemsFromBusinessAssignmentCommand;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.designer.business.model.business.BusinessItem;

/**
 * @generated
 */
public class BusinessBaseEditHelper extends AbstractEditHelper {

    /**
     * @generated
     */
    public static final String EDIT_POLICY_COMMAND = "edit policy command"; //$NON-NLS-1$

    /**
     * @generated
     */
    protected ICommand getInsteadCommand(IEditCommandRequest req) {
        ICommand epCommand = (ICommand) req.getParameter(EDIT_POLICY_COMMAND);
        req.setParameter(EDIT_POLICY_COMMAND, null);
        ICommand ehCommand = super.getInsteadCommand(req);
        if (epCommand == null) {
            return ehCommand;
        }
        if (ehCommand == null) {
            return epCommand;
        }
        CompositeCommand command = new CompositeCommand(null);
        command.add(epCommand);
        command.add(ehCommand);
        return command;
    }

    /**
     * @generated
     */
    protected ICommand getCreateCommand(CreateElementRequest req) {
        return null;
    }

    /**
     * @generated
     */
    protected ICommand getCreateRelationshipCommand(CreateRelationshipRequest req) {
        return null;
    }

    /**
     * @generated NOT
     */
    protected ICommand getDestroyElementCommand(DestroyElementRequest req) {
        if (req.getElementToDestroy() != null) {
            if (req.getElementToDestroy() instanceof BusinessItem) {
                BusinessItem businessItem = (BusinessItem) req.getElementToDestroy();
                if (businessItem.getAssignments().size() > 0) {
                    UnassignTalendItemsFromBusinessAssignmentCommand command = new UnassignTalendItemsFromBusinessAssignmentCommand(
                            req.getEditingDomain(), false);
                    for (Iterator iter = businessItem.getAssignments().iterator(); iter.hasNext();) {
                        BusinessAssignment businessAssignment = (BusinessAssignment) iter.next();
                        command.addBusinessAssignment(businessAssignment);
                    }
                    return command;
                }
            }
        }
        return null;
    }

    /**
     * @generated
     */
    protected ICommand getDestroyReferenceCommand(DestroyReferenceRequest req) {
        return null;
    }
}
