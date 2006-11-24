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
package org.talend.sqlbuilder.ui.progress;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 *
 */
public class OpenSQLBuilderDialogProgress implements IRunnableWithProgress {

	private SQLBuilderRepositoryNodeManager manager;
    
	private ConnectionParameters connectionParameters;

	private RepositoryNode repositoryNode;

	public OpenSQLBuilderDialogProgress(ConnectionParameters connectionParameters, 
			SQLBuilderRepositoryNodeManager manager, Composite composite) {
    	this.connectionParameters = connectionParameters;
    	this.manager = manager;
    }
	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.setTaskName(Messages.getString("OpenDialogJob.Waitdatabase"));
        monitor.beginTask(Messages.getString("OpenDialogJob.Waitdatabase"), IProgressMonitor.UNKNOWN);
        try {
        	repositoryNode = manager.getRepositoryNodeByBuildIn(null, connectionParameters);
            connectionParameters.setRepositoryNodeBuiltIn(repositoryNode);
            while (true) {
                
                if (monitor.isCanceled()) {                    
                    
                    if (repositoryNode != null) {
                    	repositoryNode = null;
                    }
                    connectionParameters.setConnectionComment(null);
                    break;
                }
                if (connectionParameters.getRepositoryNodeBuiltIn() != null) {
                	break;
                }
                
                Thread.sleep(100);
            }
            
            // check for cancellation by user
            if (monitor.isCanceled()) {
                monitor.done();
                throw new InterruptedException("Open SqlBuilderDialog cancelled.");
            }
            monitor.done();
            
            
        } catch (Throwable e) {
        	SqlBuilderPlugin.log("Open Dialog Job In Built-In Mode: Failure!", e);
        } finally {
            monitor.done();
        }
    }

	/**
     * Close any RepositoryNode.
     */
    public void cleanUp() {
            if (connectionParameters.getRepositoryNodeBuiltIn() != null) {
            	connectionParameters.setRepositoryNodeBuiltIn(null);
            }

    }
}
