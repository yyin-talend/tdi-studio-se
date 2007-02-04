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
package org.talend.designer.rowgenerator.shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextListener;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;

/**
 *  qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: RowGenContextManager.java,v 1.1 2007/02/02 07:47:01 pub Exp $
 * 
 */
public class RowGenContextManager implements IContextManager, Cloneable {

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IContextManager#addContextListener(org.talend.core.model.process.IContextListener)
     */
    public void addContextListener(IContextListener listener) {
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IContextManager#fireContextsChangedEvent()
     */
    public void fireContextsChangedEvent() {
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IContextManager#getDefaultContext()
     */
    public IContext getDefaultContext() {
        return new EmptyContext();
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IContextManager#getListContext()
     */
    public List<IContext> getListContext() {
        return Arrays.asList(new IContext[] { getDefaultContext() });
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IContextManager#removeContextListener(org.talend.core.model.process.IContextListener)
     */
    public void removeContextListener(IContextListener listener) {
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IContextManager#setDefaultContext(org.talend.core.model.process.IContext)
     */
    public void setDefaultContext(IContext context) {
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IContextManager#setListContext(java.util.List)
     */
    public void setListContext(List<IContext> listContext) {
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IContextManager#checkValidContextName(java.lang.String)
     */
    public boolean checkValidParameterName(String contextName) {
        return false;
    }

    public IContext getContext(String name) {
        return null;
    }
    /**
     *  qzhang RowGenContextManager class global comment. Detailled comment
     * <br/>
     *
     * $Id: RowGenContextManager.java,v 1.1 2007/02/02 07:47:01 pub Exp $
     *
     */
    private class EmptyContext implements IContext, Cloneable {

        /*
         * (non-Java)
         * 
         * @see org.talend.core.model.process.IContext#getContextParameterList()
         */
        public List<IContextParameter> getContextParameterList() {
            return new ArrayList<IContextParameter>();
        }

        /*
         * (non-Java)
         * 
         * @see org.talend.core.model.process.IContext#getName()
         */
        public String getName() {
            return "Shadow"; //$NON-NLS-1$
        }

        /*
         * (non-Java)
         * 
         * @see org.talend.core.model.process.IContext#isConfirmationNeeded()
         */
        public boolean isConfirmationNeeded() {
            return false;
        }

        /*
         * (non-Java)
         * 
         * @see org.talend.core.model.process.IContext#setConfirmationNeeded(boolean)
         */
        public void setConfirmationNeeded(boolean confirmationNeeded) {
            // Read-only
        }

        /*
         * (non-Java)
         * 
         * @see org.talend.core.model.process.IContext#setContextParameterList(java.util.List)
         */
        public void setContextParameterList(List<IContextParameter> contextParameterList) {
            // Read-only
        }

        /*
         * (non-Java)
         * 
         * @see org.talend.core.model.process.IContext#setName(java.lang.String)
         */
        public void setName(String name) {
            // Read-only
        }

        @Override
        public IContext clone() {
            return this;
        }
    }
}
