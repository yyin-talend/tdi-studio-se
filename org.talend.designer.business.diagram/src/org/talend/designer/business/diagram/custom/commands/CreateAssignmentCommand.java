// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.business.diagram.custom.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.talend.designer.business.diagram.custom.util.RepositoryHelper;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.designer.business.model.business.BusinessFactory;
import org.talend.designer.business.model.business.BusinessItem;
import org.talend.designer.business.model.business.Repository;
import org.talend.designer.business.model.business.TalendItem;

public class CreateAssignmentCommand extends AbstractTransactionalCommand {

    private Object item;

    private BusinessItem businessItem;

    /**
     * DOC mhelleboid createAssignmentCommand constructor comment.
     * 
     * @param domain
     * @param label
     * @param affectedFiles
     */
    public CreateAssignmentCommand(TransactionalEditingDomain domain) {
        super(domain, "Create assignement", null);
    }

    /**
     * DOC mhelleboid Comment method "setTalendItem".
     * 
     * @param item
     */
    public void setItem(Object item) {
        this.item = item;
    }

    /**
     * DOC mhelleboid Comment method "setBusinessItem".
     * 
     * @param businessItem
     */
    public void setBusinessItem(BusinessItem businessItem) {
        this.businessItem = businessItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor,
     * org.eclipse.core.runtime.IAdaptable)
     */
    @Override
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        // PTODO MHE check if assignment already exist
        BusinessAssignment assignment = BusinessFactory.eINSTANCE.createBusinessAssignment();

        RepositoryHelper repositoryHelper = new RepositoryHelper();
        TalendItem talendItem = repositoryHelper.createTalendItem(item);
        // PTODO MHE check if talenditem already exists

        if (talendItem == null) {
            return CommandResult.newErrorCommandResult("Cannot Assign.");
        }

        Repository repository = businessItem.getBusinessProcess().getLocalRepositoryCopy();
        if (repository == null) {
            repository = repositoryHelper.createLocalRepositoryCopy(businessItem.getBusinessProcess());
        }

        talendItem.setRepository(repository);
        assignment.setBusinessItem(businessItem);
        assignment.setTalendItem(talendItem);

        return CommandResult.newOKCommandResult();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.operations.AbstractOperation#canExecute()
     */
    @Override
    public boolean canExecute() {
        // PTODO MHE
        // RepositoryHelper repositoryHelper = new RepositoryHelper();
        // TalendItem talendItem = repositoryHelper.createTalendItem(item);
        // if (talendItem == null)
        // {
        // return false;
        // }
        return true;
    }

}