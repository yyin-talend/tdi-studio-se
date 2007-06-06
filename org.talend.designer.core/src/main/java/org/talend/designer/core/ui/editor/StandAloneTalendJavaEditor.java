// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.designer.core.DesignerPlugin;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Stand alone Perl editor.<br/>
 * 
 */
public class StandAloneTalendJavaEditor extends JavaEditor {

    private RepositoryEditorInput rEditorInput;

    /**
     * DOC smallet Comment method "getRepositoryFactory".
     */
    private IProxyRepositoryFactory getRepositoryFactory() {
        return DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
    }

    @Override
    public boolean isSaveAsAllowed() {
        return getRepositoryFactory().getStatus(item).isEditable();
    }

    @Override
    public boolean isEditable() {
        return !rEditorInput.isReadOnly() && getRepositoryFactory().getStatus(item).isEditable();
    }

    public void doSetInput(IEditorInput input) throws CoreException {
        super.doSetInput(input);
        // Lock the process :
        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
        try {
            rEditorInput = (RepositoryEditorInput) input;
            item = (RoutineItem) rEditorInput.getItem();
            item.getProperty().eAdapters().add(dirtyListener);
            if (!rEditorInput.isReadOnly()) {
                repFactory.lock(item);
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (BusinessException e) {
            // Nothing to do
        }
        
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
        ILabelProvider labelProvider = (ILabelProvider) viewPart.getViewer().getLabelProvider();
        setTitleImage(labelProvider.getImage(item.getProperty()));
        setPartName(labelProvider.getText(item.getProperty()));
    }

    @Override
    public void dispose() {
        super.dispose();
        // Unlock the process :
        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
        try {
            item.getProperty().eAdapters().remove(dirtyListener);
            Property property = repFactory.reload(item.getProperty());
            item = (RoutineItem) property.getItem();
            repFactory.unlock(item);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
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
            ByteArray byteArray = item.getContent();
            byteArray.setInnerContentFromFile(((RepositoryEditorInput) getEditorInput()).getFile());
            IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
            IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
            repFactory.save(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        propertyIsDirty = false;
        adapters.add(dirtyListener);
        firePropertyChange(IEditorPart.PROP_DIRTY);
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.JavaEditor#getCorrespondingElement(org.eclipse.jdt.core.IJavaElement)
     */
    @Override
    protected IJavaElement getCorrespondingElement(IJavaElement element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.JavaEditor#getElementAt(int)
     */
    @Override
    protected IJavaElement getElementAt(int offset) {
        return null;
    }

}
