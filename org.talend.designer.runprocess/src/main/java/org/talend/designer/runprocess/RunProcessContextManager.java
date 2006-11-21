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
package org.talend.designer.runprocess;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talend.core.model.process.IProcess;

/**
 * Manage all RunProcess contexts. <br/>
 * 
 * $Id$
 * 
 */
public class RunProcessContextManager {

    public static final String PROP_ACTIVE = "RunProcessContextManager.Active"; //$NON-NLS-1$

    // FIXME Mettre cette donn�e en fenetre de pr�f�rences.
    private static final int DEBUG_PORT_BASE = 3334;

    private static final int DEBUG_PORT_RANGE = 30;
    
    // FIXME Mettre cette donn�e en fenetre de pr�f�rences.
    private static final int TRACE_PORT_BASE = 4334;

    private static final int TRACE_PORT_RANGE = 30;
    
    private static final int WATCH_PORT_RANGE = 30;

    /** Change property listeners. */
    private transient PropertyChangeSupport pcsDelegate;

    /** Contexts. */
    private List<RunProcessContext> contexts;

    /** Active context. */
    private RunProcessContext activeContext;

    /** Allocated ports. */
    private Map<RunProcessContext, Integer> portsByContext;

    /**
     * Constructs a new RunProcessContextManager.
     */
    public RunProcessContextManager() {
        super();

        contexts = new ArrayList<RunProcessContext>();
        portsByContext = new HashMap<RunProcessContext, Integer>();

        pcsDelegate = new PropertyChangeSupport(this);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }

        pcsDelegate.addPropertyChangeListener(l);
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        if (pcsDelegate.hasListeners(property)) {
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
        }
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (l != null) {
            pcsDelegate.removePropertyChangeListener(l);
        }
    }

    /**
     * Getter for activeContext.
     * 
     * @return the activeContext
     */
    public RunProcessContext getActiveContext() {
        return this.activeContext;
    }

    /**
     * Sets the activeProcess.
     * 
     * @param activeContext the activeContext to set
     */
    public void setActiveProcess(IProcess activeProcess) {
        RunProcessContext tempActiveContext = findContext(activeProcess);
        if (tempActiveContext == null && activeProcess != null) {
            tempActiveContext = new RunProcessContext(activeProcess);
        }
        if (!contexts.contains(tempActiveContext) && tempActiveContext != null) {
            contexts.add(tempActiveContext);
        }

        RunProcessContext oldContext = this.activeContext;
        if (tempActiveContext != oldContext) {
            this.activeContext = tempActiveContext;
            firePropertyChange(PROP_ACTIVE, oldContext, activeContext);
        }
    }

    public void removeProcess(IProcess process) {
        RunProcessContext context = findContext(process);
        if (context != null) {
            context.kill();
            contexts.remove(context);

            portsByContext.remove(context);
        }
        if (context == activeContext || contexts.isEmpty()) {
            setActiveProcess(null);
        }
    }

    private RunProcessContext findContext(IProcess process) {
        RunProcessContext context = null;
        for (Iterator<RunProcessContext> i = contexts.iterator(); context == null && i.hasNext();) {
            RunProcessContext ctx = i.next();
            if (ctx.getProcess() == process) {
                context = ctx;
            }
        }
        return context;
    }

    /**
     * Get a free TCP port to grab statistics on a process.
     * 
     * @param context Process monitored.
     * @return A free TCP port, -1 if none is available.
     */
    public int getPortForStatistics(RunProcessContext context) {
        int port = -1;
        for (int i = 0; port == -1 && i < DEBUG_PORT_RANGE; i++) {
            int p = DEBUG_PORT_BASE + i;
            boolean alreadyUsed = false;
            for (Iterator<Integer> j = portsByContext.values().iterator(); !alreadyUsed && j.hasNext();) {
                alreadyUsed = j.next().intValue() == p;
            }
            if (!alreadyUsed) {
                port = p;
            }
        }
        return port;
    }
    
    /**
     * Get a free TCP port to grab trace on a process.
     * 
     * @param context Process monitored.
     * @return A free TCP port, -1 if none is available.
     */
    public int getPortForTraces(RunProcessContext context) {
        int port = -1;
        for (int i = 0; port == -1 && i < TRACE_PORT_RANGE; i++) {
            int p = TRACE_PORT_BASE + i;
            boolean alreadyUsed = false;
            for (Iterator<Integer> j = portsByContext.values().iterator(); !alreadyUsed && j.hasNext();) {
                alreadyUsed = j.next().intValue() == p;
            }
            if (!alreadyUsed) {
                port = p;
            }
        }
        return port;
    }
    
    
    /**
     * Get a free TCP port to grab watch on a process.
     * 
     * @param context Process monitored.
     * @return A free TCP port, -1 if none is available.
     */
    public int getPortForWatch(RunProcessContext context) {
        int port = -1;
        for (int i = 0; port == -1 && i < WATCH_PORT_RANGE; i++) {
            int p = WATCH_PORT_RANGE + i;
            boolean alreadyUsed = false;
            for (Iterator<Integer> j = portsByContext.values().iterator(); !alreadyUsed && j.hasNext();) {
                alreadyUsed = j.next().intValue() == p;
            }
            if (!alreadyUsed) {
                port = p;
            }
        }
        return port;
    }
}
