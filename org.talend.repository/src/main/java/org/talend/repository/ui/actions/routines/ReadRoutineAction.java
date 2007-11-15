// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.actions.routines;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PartInitException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;

/**
 * Action that will edit routines.
 * 
 * $Id: EditRoutineAction.java 1863 2007-02-06 06:01:36Z yzhang $
 * 
 */
public class ReadRoutineAction extends AbstractRoutineAction {

    public ReadRoutineAction() {
        super();

        setText(Messages.getString("EditRoutineAction.text.readRoutine")); //$NON-NLS-1$
        setToolTipText(Messages.getString("EditRoutineAction.toolTipText.readRoutine")); //$NON-NLS-1$
        setImageDescriptor(ImageProvider.getImageDesc(EImage.READ_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (canWork) {
            RepositoryNode node = (RepositoryNode) selection.getFirstElement();
            canWork = node.getType() == ENodeType.REPOSITORY_ELEMENT
                    && node.getObject().getType() == ERepositoryObjectType.ROUTINES;
        }
        setEnabled(canWork);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        RepositoryNode node = (RepositoryNode) ((IStructuredSelection) getSelection()).getFirstElement();
        RoutineItem routineItem = (RoutineItem) node.getObject().getProperty().getItem();

        try {
            openRoutineEditor(routineItem, true);
        } catch (PartInitException e) {
            MessageBoxExceptionHandler.process(e);
        } catch (SystemException e) {
            MessageBoxExceptionHandler.process(e);
        }
    }

}
