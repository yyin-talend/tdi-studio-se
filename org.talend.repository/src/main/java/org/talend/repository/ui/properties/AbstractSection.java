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
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.LocalLockHelper;
import org.talend.repository.model.ProxyRepositoryFactory;
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

    private RepositoryNode repositoryNode;

    private FocusListener listener = new FocusListener() {

        public void focusLost(FocusEvent e) {
            performSave();
        }

        public void focusGained(FocusEvent e) {
            manageLock();
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
                if (object.getId().equals(id)) {
                    enableControl(enable);
                }
            }
        }

        @Override
        public void lockRemoved(String id) {
            performRefresh();
            handleEvent(id, false);
        }
    };

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
        for (AbstractSection section : REGISTERED_SECTIONS) {
            section.refresh();
        }
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
        boolean enableControl = ProxyRepositoryFactory.getInstance().getStatus(repositoryObject).isEditable();
        enableControls(enableControl);
    }

    protected final IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    /**
     * DOC tguiu Comment method "enableControls".
     * 
     * @param locked
     */
    private static void showControls(boolean visible) {
        for (AbstractSection section : REGISTERED_SECTIONS) {
            section.showControl(visible);
        }
    }

    private static void enableControls(boolean enable) {
        for (AbstractSection section : REGISTERED_SECTIONS) {
            section.enableControl(enable);
        }
    }

    /**
     * DOC tguiu Comment method "enableControl".
     * 
     * @param b
     */
    protected abstract void showControl(boolean visible);

    protected abstract void enableControl(boolean enable);

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
    protected ProxyRepositoryFactory getRepositoryFactory() {
        return ProxyRepositoryFactory.getInstance();
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
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getCreationDate()
         */
        public Date getCreationDate() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getDescription()
         */
        public String getDescription() {
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getId()
         */
        public String getId() {
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getLabel()
         */
        public String getLabel() {
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getModificationDate()
         */
        public Date getModificationDate() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getProperty()
         */
        public Property getProperty() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getPurpose()
         */
        public String getPurpose() {
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getStatusCode()
         */
        public String getStatusCode() {
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getType()
         */
        public ERepositoryObjectType getType() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#getVersion()
         */
        public String getVersion() {
            return "";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setAuthor(org.talend.core.model.general.User)
         */
        public void setAuthor(User author) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setCreationDate(java.util.Date)
         */
        public void setCreationDate(Date value) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setDescription(java.lang.String)
         */
        public void setDescription(String value) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setId(int)
         */
        public void setId(String id) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setLabel(java.lang.String)
         */
        public void setLabel(String label) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setModificationDate(java.util.Date)
         */
        public void setModificationDate(Date value) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setPurpose(java.lang.String)
         */
        public void setPurpose(String value) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setStatusCode(java.lang.String)
         */
        public void setStatusCode(String statusCode) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.repository.IRepositoryObject#setVersion(org.talend.core.model.general.Version)
         */
        public void setVersion(String version) {
        }

    }
}
