// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
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
package org.talend.designer.core.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.epic.perleditor.editors.PerlEditor;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.RoutineItem;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

/**
 * Stand alone Perl editor <br/>
 * 
 */
public class StandAloneTalendPerlEditor extends PerlEditor {

    public void doSetInput(IEditorInput input) throws CoreException {
        super.doSetInput(input);
        // Lock the process :
        IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
        try {
            RepositoryEditorInput rEditorInput = (RepositoryEditorInput) input;
            item = (RoutineItem) rEditorInput.getItem();
            item.getProperty().eAdapters().add(dirtyListener);
            repFactory.lock(item);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // Unlock the process :
        IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
        try {
            item.getProperty().eAdapters().remove(dirtyListener);
            repFactory.unlock(item);
            repFactory.reload(item.getProperty());

        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(RepositoryView.VIEW_ID);
        viewPart.refresh();
        // viewPart1.refresh();
    }

    @Override
    public boolean isDirty() {
        return propertyIsDirty || super.isDirty();
    }

    protected void editorSaved() {

    }

    public void doSave(final IProgressMonitor monitor) {
        EList adapters = item.getProperty().eAdapters();
        adapters.remove(dirtyListener);
        super.doSave(monitor);

        try {
            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            byteArray.setInnerContentFromFile(((RepositoryEditorInput) getEditorInput()).getFile());
            item.setContent(byteArray);
            ProxyRepositoryFactory.getInstance().save(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        propertyIsDirty = false;
        adapters.add(dirtyListener);
        firePropertyChange(IEditorPart.PROP_DIRTY);
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(RepositoryView.VIEW_ID);
        viewPart.refresh();
    }

    private RoutineItem item;

    private boolean propertyIsDirty;

    private AdapterImpl dirtyListener = new AdapterImpl() {

        public void notifyChanged(Notification notification) {
            if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
                propertyIsDirty = true;
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        }
    };

}
