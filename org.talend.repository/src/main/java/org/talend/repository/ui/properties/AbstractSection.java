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
package org.talend.repository.ui.properties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.User;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.LocalLockHelper;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.LocalLockHelper.Listerner;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractSection extends AbstractPropertySection {

    private static final List<AbstractSection> REGISTERED_SECTIONS = new ArrayList<AbstractSection>();

    private IRepositoryObject repositoryObject;

    private FocusListener listener = new FocusListener() {

        public void focusLost(FocusEvent e) {
            performSave();
        }

        public void focusGained(FocusEvent e) {
        }
    };

    private Listerner lockListerner = new Listerner() {

        @Override
        public void lockAdded(String id) {
            handleEvent(id, true);
        }

        /**
         * DOC tguiu Comment method "handleEvent".
         * 
         * @param id
         */
        private void handleEvent(String id, boolean enable) {
            IRepositoryObject object = getObject();
            if (object != null) {
                if (object.getId().equals(id))
                    enableControl(enable);
            }
        }

        @Override
        public void lockRemoved(String id) {
            performRefresh();
            handleEvent(id, false);
        }
    };

    private RepositoryNode repositoryNode;

    /**
     * DOC tguiu AbstractSection constructor comment.
     */
    public AbstractSection() {
        super();
        REGISTERED_SECTIONS.add(this);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls(org.eclipse.swt.widgets.Composite,
     * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        LocalLockHelper.addListener(lockListerner);
    }

    protected IRepositoryObject getObject() {
        return repositoryObject;
    }

    protected RepositoryNode getNode() {
        return repositoryNode;
    }

    protected ERepositoryObjectType getType() {
        return repositoryObject.getType();
    }

    /**
     * DOC tguiu Comment method "addFocusListener".
     * 
     * @param nameText2
     */
    protected void addFocusListener(Control control) {
        control.addFocusListener(listener);
    }

    protected void performSave() {
        for (AbstractSection section : REGISTERED_SECTIONS) {
            section.beforeSave();
        }
        // save();
        // performRefresh();
        // refreshRepositoryView();
    }

    protected static void performRefresh() {
        for (AbstractSection section : REGISTERED_SECTIONS)
            section.refresh();
    }

    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);

        Assert.isTrue(selection instanceof IStructuredSelection);
        Object input = ((IStructuredSelection) selection).getFirstElement();

        Assert.isTrue(input instanceof RepositoryNode);
        repositoryNode = (RepositoryNode) input;
        repositoryObject = repositoryNode.getObject();
        if (repositoryObject == null) {
            repositoryObject = new EmptyRepositoryObject();
            enableControls(false);
            showControls(false);
            return;
        }
        manageLock();
        ERepositoryObjectType type = repositoryObject.getType();
        showControls(type != ERepositoryObjectType.METADATA_CON_TABLE);
    }

    /**
     * DOC tguiu Comment method "manageLock".
     */
    protected void manageLock() {
        boolean enableControl = false;
//        try {

            User locker =null;// getRepositoryFactory().getLocker(getObject());
            if (locker != null) {
                RepositoryContext repositoryContext = ((RepositoryContext) CorePlugin.getContext().getProperty(
                        Context.REPOSITORY_CONTEXT_KEY));
                enableControl = locker.equals(repositoryContext.getUser());
            }
//        } catch (PersistenceException ea) {
//            ea.printStackTrace();
//        }
        enableControls(enableControl);
    }

    protected final IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    /**
     * DOC tguiu Comment method "lock".
     */
    protected void lock() {
        try {
            getRepositoryFactory().lock(getObject());
        } catch (PersistenceException e1) {
            e1.printStackTrace();
        }
        manageLock();
    }

    /**
     * DOC tguiu Comment method "enableControls".
     * 
     * @param locked
     */
    private static void showControls(boolean visible) {
        for (AbstractSection section : REGISTERED_SECTIONS)
            section.showControl(visible);
    }

    private static void enableControls(boolean locked) {
        for (AbstractSection section : REGISTERED_SECTIONS)
            section.enableControl(locked);
    }

    /**
     * DOC tguiu Comment method "enableControl".
     * 
     * @param b
     */
    protected abstract void showControl(boolean visible);

    protected abstract void enableControl(boolean locked);

    protected abstract void beforeSave();

    @Override
    public void dispose() {
        super.dispose();
        REGISTERED_SECTIONS.remove(this);
        LocalLockHelper.removeListener(lockListerner);
    }

    /**
     * DOC tguiu Comment method "getRepositoryFactory".
     * 
     * @return
     */
    protected IRepositoryFactory getRepositoryFactory() {
        return RepositoryFactoryProvider.getInstance();
    }

    /**
     * 
     * DOC tguiu AbstractSection class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    class EmptyRepositoryObject implements IRepositoryObject {

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getAuthor()
         */
        public User getAuthor() {
            // TODO Auto-generated method stub
            return new User("");
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getCreationDate()
         */
        public Date getCreationDate() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getDescription()
         */
        public String getDescription() {
            // TODO Auto-generated method stub
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getId()
         */
        public String getId() {
            // TODO Auto-generated method stub
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getLabel()
         */
        public String getLabel() {
            // TODO Auto-generated method stub
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getModificationDate()
         */
        public Date getModificationDate() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getProperty()
         */
        public Property getProperty() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getPurpose()
         */
        public String getPurpose() {
            // TODO Auto-generated method stub
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getStatusCode()
         */
        public String getStatusCode() {
            // TODO Auto-generated method stub
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getType()
         */
        public ERepositoryObjectType getType() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getVersion()
         */
        public String getVersion() {
            // TODO Auto-generated method stub
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setAuthor(org.talend.core.model.general.User)
         */
        public void setAuthor(User author) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setCreationDate(java.util.Date)
         */
        public void setCreationDate(Date value) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setDescription(java.lang.String)
         */
        public void setDescription(String value) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setId(int)
         */
        public void setId(String id) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setLabel(java.lang.String)
         */
        public void setLabel(String label) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setModificationDate(java.util.Date)
         */
        public void setModificationDate(Date value) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setPurpose(java.lang.String)
         */
        public void setPurpose(String value) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setStatusCode(java.lang.String)
         */
        public void setStatusCode(String statusCode) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setVersion(org.talend.core.model.general.Version)
         */
        public void setVersion(String version) {
            // TODO Auto-generated method stub

        }

    }
}
