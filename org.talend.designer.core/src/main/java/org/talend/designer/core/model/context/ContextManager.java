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
package org.talend.designer.core.model.context;

import java.util.ArrayList;
import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextListener;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ContextManager implements IContextManager {

    private IContext defaultContext = new Context(IContext.DEFAULT);

    private List<IContext> listContext = new ArrayList<IContext>();

    private List<IContextListener> contextListenerList = new ArrayList<IContextListener>();

    private ECodeLanguage language;

    public ContextManager(ECodeLanguage language) {
        this.language = language;
        listContext.add(defaultContext);
    }

    public void addContextListener(IContextListener listener) {
        contextListenerList.add(listener);
    }

    public void removeContextListener(IContextListener listener) {
        contextListenerList.remove(listener);
    }

    public void fireContextsChangedEvent() {
        for (IContextListener contextListener : contextListenerList) {
            contextListener.contextsChanged();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IContextManager#getDefaultContext()
     */
    public IContext getDefaultContext() {
        return defaultContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IContextManager#setDefaultContext(org.talend.core.model.process.IContext)
     */
    public void setDefaultContext(IContext context) {
        defaultContext = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IContextManager#getListContext()
     */
    public List<IContext> getListContext() {
        return listContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IContextManager#getListContext(java.util.List)
     */
    public void setListContext(List<IContext> listContext) {
        this.listContext = listContext;
    }

    /**
     * Check if the given name will be unique in the process. If another link already exists with that name, false will
     * be returned.
     * 
     * @param uniqueName
     * @return true if the name is unique
     */
    public boolean checkValidParameterName(String contextName) {
        for (IContextParameter contextParameter : listContext.get(0).getContextParameterList()) {
            if (contextParameter.getName().equals(contextName)) {
                return false;
            }
        }

        Perl5Matcher matcher = new Perl5Matcher();
        Perl5Compiler compiler = new Perl5Compiler();
        Pattern pattern;

        switch (language) {
        case PERL:
            try {
                pattern = compiler.compile("^[A-Za-z_][A-Za-z0-9_]*$"); //$NON-NLS-1$
                if (!matcher.matches(contextName, pattern)) {
                    return false;
                }
            } catch (MalformedPatternException e) {
                throw new RuntimeException(e);
            }
        default:
        }

        return true;
    }

    public IContext getContext(String name) {
        for (int i = 0; i < listContext.size(); i++) {
            if (listContext.get(i).getName().equals(name)) {
                return listContext.get(i);
            }
        }
        return defaultContext;
    }
}
