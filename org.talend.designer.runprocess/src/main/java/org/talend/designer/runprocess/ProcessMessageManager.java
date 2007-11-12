// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.runprocess;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

import org.apache.commons.collections.buffer.BoundedFifoBuffer;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * 
 */
public class ProcessMessageManager implements IProcessMessageManager {

    /** Change property listeners. */
    private transient PropertyChangeSupport pcsDelegate;

    /** Messages associated to the process. */
    private BoundedFifoBuffer messages;

    public static final String PROP_MESSAGE_ADD = "RunProcessContext.Message.Added"; //$NON-NLS-1$

    public static final String PROP_DEBUG_MESSAGE_ADD = "RunProcessContext.DebugMessage.Added";

    public static final String PROP_MESSAGE_CLEAR = "RunProcessContext.Message.Cleared"; //$NON-NLS-1$

    public static final int LIMIT_MESSAGES = 500;

    /**
     * DOC amaumont ProcessMessageManager constructor comment.
     */
    public ProcessMessageManager() {
        super();

        messages = new BoundedFifoBuffer(LIMIT_MESSAGES);
        pcsDelegate = new PropertyChangeSupport(this);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }

        pcsDelegate.addPropertyChangeListener(l);
    }

    public void firePropertyChange(String property, Object oldValue, Object newValue) {
        if (pcsDelegate.hasListeners(property)) {
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
        }
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (l != null) {
            pcsDelegate.removePropertyChangeListener(l);
        }
    }

    public void addMessage(IProcessMessage message) {
        synchronized (messages) {
            if (messages.isFull()) {
                messages.remove();
            }
            messages.add(message);
            firePropertyChange(PROP_MESSAGE_ADD, null, message);
        }

    }

    public void clearMessages() {
        synchronized (messages) {
            messages.clear();
            firePropertyChange(PROP_MESSAGE_CLEAR, messages, null);
        }

    }

    public Collection<IProcessMessage> getMessages() {
        return (Collection<IProcessMessage>) messages;
    }

    public boolean isLastMessageEndWithCR() {
        int i = messages.size() - 1;
        if (i >= 0) {
            IProcessMessage processMessage = (IProcessMessage) messages.toArray()[i];
            return processMessage.getContent().endsWith("\n");
        } else {
            return false;
        }
    }

    public void addDebugResultToConsole(IProcessMessage debugResultMessage) {
        synchronized (messages) {
            if (messages.isFull()) {
                messages.remove();
            }

            firePropertyChange(PROP_DEBUG_MESSAGE_ADD, null, debugResultMessage);
        }

    }

}
