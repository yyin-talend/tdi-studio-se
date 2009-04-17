// ============================================================================
//
// Copyright (C) 2006-2008 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.business.diagram.custom.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.talend.designer.business.diagram.custom.util.RepositoryHelper;
import org.talend.designer.business.model.business.ActionBusinessItem;
import org.talend.designer.business.model.business.ActorBusinessItem;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.designer.business.model.business.BusinessFactory;
import org.talend.designer.business.model.business.BusinessItem;
import org.talend.designer.business.model.business.BusinessProcess;
import org.talend.designer.business.model.business.DataBusinessItem;
import org.talend.designer.business.model.business.DatabaseBusinessItem;
import org.talend.designer.business.model.business.DecisionBusinessItem;
import org.talend.designer.business.model.business.DocumentBusinessItem;
import org.talend.designer.business.model.business.EllipseBusinessItem;
import org.talend.designer.business.model.business.GearBusinessItem;
import org.talend.designer.business.model.business.InputBusinessItem;
import org.talend.designer.business.model.business.ListBusinessItem;
import org.talend.designer.business.model.business.Repository;
import org.talend.designer.business.model.business.TalendItem;
import org.talend.designer.business.model.business.TerminalBusinessItem;
import org.talend.designer.business.model.business.util.BusinessSwitch;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * wchen class global comment. Detailled comment
 */
public class GmfPastCommand extends AbstractTransactionalCommand {

    List<BusinessItem> businessItems;

    DiagramEditPart part;

    BusinessProcess process;

    Map itemIds;

    boolean addNew;

    /**
     * wchen GmfPastCommand constructor comment.
     * 
     * @param domain
     * @param label
     * @param affectedFiles
     */
    public GmfPastCommand(BusinessProcess process, List<BusinessItem> businessItems, DiagramEditPart part, Map itemIds,
            boolean addNew) {
        super(TransactionUtil.getEditingDomain(process), null, null);
        this.businessItems = businessItems;
        this.part = part;
        this.process = process;
        this.addNew = addNew;
        this.itemIds = itemIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doExecuteWithResult(org.eclipse
     * .core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
     */
    @Override
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        List pChildren = ((Diagram) part.getModel()).getPersistedChildren();
        int i = 1;
        for (BusinessItem businessItem : this.businessItems) {

            BusinessItem newItem = (BusinessItem) getEObject(businessItem);
            if (!addNew) {
                newItem.setBusinessProcess(businessItem.getBusinessProcess());
            }
            newItem.setHAlignment(businessItem.getVAlignment());
            newItem.setName(businessItem.getName());
            newItem.setVAlignment(businessItem.getVAlignment());

            List assignments = new ArrayList();
            boolean execute = false;
            int n = 0;
            for (Object object : businessItem.getAssignments()) {

                BusinessAssignment older = (BusinessAssignment) object;
                BusinessAssignment newAssignment = (BusinessAssignment) getEObject(older);
                newAssignment.setBusinessItem(newItem);
                newAssignment.setComment(older.getComment());
                if (!addNew) {
                    newAssignment.setTalendItem(older.getTalendItem());
                } else {
                    TalendItem item = older.getTalendItem();
                    String talendId = null;
                    if (item != null) {
                        talendId = item.getId();
                    } else {
                        List idList = (List) itemIds.get(businessItem);
                        if (n < idList.size()) {
                            talendId = (String) idList.get(n);
                        }
                        n++;
                    }
                    RepositoryNode node = null;
                    if (talendId.split(" - ").length == 1) { //$NON-NLS-1$
                        node = RepositoryNodeUtilities.getRepositoryNode(talendId);
                    } else {
                        node = RepositoryNodeUtilities.getMetadataTableFromConnection(talendId);
                        if (node == null) {
                            node = RepositoryNodeUtilities.getQueryFromConnection(talendId);
                        }
                    }
                    RepositoryHelper repositoryHelper = new RepositoryHelper();
                    Repository repository = process.getLocalRepositoryCopy();

                    if (repository == null) {
                        repository = repositoryHelper.createLocalRepositoryCopy(process);
                    }
                    TalendItem talendItem = repositoryHelper.createTalendItem(repository, node);
                    newAssignment.setTalendItem(talendItem);
                }

                assignments.add(newAssignment);
            }

            if (assignments.size() > 0) {
                newItem.getAssignments().addAll(assignments);
            }

            Node node = (Node) pChildren.get(pChildren.size() - i);
            // if (node.getElement() instanceof BusinessProcess) {
            node.setElement(newItem);
            i++;
            // }

            if (addNew) {
                this.process.getBusinessItems().add(newItem);
            }
        }

        return CommandResult.newOKCommandResult();
    }

    private EObject getEObject(EObject object) {
        return (EObject) new BusinessSwitch() {

            @Override
            public Object caseActionBusinessItem(ActionBusinessItem object) {
                return BusinessFactory.eINSTANCE.createActionBusinessItem();
            }

            @Override
            public Object caseActorBusinessItem(ActorBusinessItem object) {
                return BusinessFactory.eINSTANCE.createActorBusinessItem();
            }

            @Override
            public Object caseDatabaseBusinessItem(DatabaseBusinessItem object) {
                return BusinessFactory.eINSTANCE.createDatabaseBusinessItem();
            }

            @Override
            public Object caseDecisionBusinessItem(DecisionBusinessItem object) {
                return BusinessFactory.eINSTANCE.createDecisionBusinessItem();
            }

            @Override
            public Object caseDocumentBusinessItem(DocumentBusinessItem object) {
                return BusinessFactory.eINSTANCE.createDocumentBusinessItem();
            }

            @Override
            public Object caseEllipseBusinessItem(EllipseBusinessItem object) {
                return BusinessFactory.eINSTANCE.createEllipseBusinessItem();
            }

            @Override
            public Object caseGearBusinessItem(GearBusinessItem object) {
                return BusinessFactory.eINSTANCE.createGearBusinessItem();
            }

            @Override
            public Object caseInputBusinessItem(InputBusinessItem object) {
                return BusinessFactory.eINSTANCE.createInputBusinessItem();
            }

            @Override
            public Object caseListBusinessItem(ListBusinessItem object) {
                return BusinessFactory.eINSTANCE.createListBusinessItem();
            }

            @Override
            public Object caseTerminalBusinessItem(TerminalBusinessItem object) {
                return BusinessFactory.eINSTANCE.createTerminalBusinessItem();
            }

            @Override
            public Object caseDataBusinessItem(DataBusinessItem object) {
                return BusinessFactory.eINSTANCE.createDataBusinessItem();
            }

            @Override
            public Object caseBusinessAssignment(BusinessAssignment object) {
                return BusinessFactory.eINSTANCE.createBusinessAssignment();
            }

            // @Override
            // public Object caseTalendItem(TalendItem object) {
            // return BusinessFactory.eINSTANCE.create();
            // }

        }.doSwitch(object);
    }

}
