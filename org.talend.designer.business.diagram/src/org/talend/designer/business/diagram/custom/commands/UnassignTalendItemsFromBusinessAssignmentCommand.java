package org.talend.designer.business.diagram.custom.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.talend.designer.business.diagram.custom.util.RepositoryHelper;
import org.talend.designer.business.model.business.BusinessAssignment;

public class UnassignTalendItemsFromBusinessAssignmentCommand extends AbstractTransactionalCommand {

    private List<BusinessAssignment> businessAssignments = new ArrayList<BusinessAssignment>();
    private boolean alsoDeleteBusinessAssignment;
    
    public UnassignTalendItemsFromBusinessAssignmentCommand(TransactionalEditingDomain domain, boolean alsoDeleteBusinessAssignment) {
        super(domain, null, null);
        this.alsoDeleteBusinessAssignment = alsoDeleteBusinessAssignment;
    }

    @Override
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        RepositoryHelper repositoryHelper = new RepositoryHelper();
        
        for (BusinessAssignment businessAssignment : businessAssignments) {
            repositoryHelper.unassignTalendItemsFromBusinessAssignment(businessAssignment);
            if (alsoDeleteBusinessAssignment) {
                businessAssignment.getBusinessItem().getAssignments().remove(businessAssignment);
            }
        }
        
        return CommandResult.newOKCommandResult();
    }

    public void addBusinessAssignment(BusinessAssignment businessAssignment) {
        businessAssignments.add(businessAssignment);        
    }
}
