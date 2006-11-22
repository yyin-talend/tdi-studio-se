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
package org.talend.sqlbuilder.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * Open SqlBuilderDialog Job.
 * @author phou
 *
 */
public class OpenDialogJob extends Job {
	private SQLBuilderRepositoryNodeManager manager = new SQLBuilderRepositoryNodeManager();
	private ConnectionParameters connectionParameters;
	private static String id = SqlBuilderPlugin.PLUGIN_ID; 
	private LoginProgress loginProgress;
	
	public OpenDialogJob(ConnectionParameters connectionParameters) {
		super("Open SQLBuilder Dialog");
		this.connectionParameters = connectionParameters;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		loginProgress = new LoginProgress(connectionParameters, manager);
		try {
			loginProgress.run(monitor);
		} catch (InterruptedException ie) {
            cleanUp();
            return new Status(IStatus.CANCEL, id, IStatus.CANCEL, "Progress.Connection.Cancelled", null);
		} catch (Exception e) {
			cleanUp();
            return new Status(IStatus.ERROR, id, IStatus.CANCEL, "Progress.Connection.Error", e);
		}
//		if (connectionParameters.getConnectionComment() != null) {
//			return new Status(IStatus.ERROR, id, IStatus.CANCEL, connectionParameters.getConnectionComment(), null);
//		}
		return new Status(IStatus.OK, id, IStatus.OK, "tested ok ", null);
	}

	/**
     * Close any RepositoryNode.
     */
    private void cleanUp() {

        RepositoryNode repositoryNode = connectionParameters.getRepositoryNodeBuiltIn();
            if (repositoryNode != null) {
            	connectionParameters.setRepositoryNodeBuiltIn(null);
            }

    }
}
/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 *
 */
class LoginProgress implements IRunnableWithProgress {

	private SQLBuilderRepositoryNodeManager manager;
	    
	private ConnectionParameters connectionParameters;

	private RepositoryNode repositoryNode;
    
    /**
     * DOC dev LoginProgress class global comment. Detailled comment
     * <br/>
     *
     * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
     *
     */
    private class InteractiveConnectionThread extends Thread {

        public void run() {

               try {
                   repositoryNode = manager.getRepositoryNodeByBuildIn(null, connectionParameters);
                   connectionParameters.setRepositoryNodeBuiltIn(repositoryNode);
                } catch (Throwable e) {
                	SqlBuilderPlugin.log("Get RepositoryNode From Built-In Mode: Failure: ", e);
                }
        }
    }
    public LoginProgress(ConnectionParameters connectionParameters, 
    		SQLBuilderRepositoryNodeManager manager) {
    	this.connectionParameters = connectionParameters;
    	this.manager = manager;
    }
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.setTaskName("Logging_to_database..._1");
        monitor.beginTask("Logging_to_database..._1", IProgressMonitor.UNKNOWN);
        try {
            InteractiveConnectionThread iThread = new InteractiveConnectionThread();
            iThread.start();
            
            while (true) {
                
                if (monitor.isCanceled()) {                    
                    
                    if (iThread.isAlive()) {
                        iThread.interrupt();
                    }
                    if (repositoryNode != null) {
                    	repositoryNode = null;
                    }
                    connectionParameters.setConnectionComment(null);
                    break;
                }
                if (repositoryNode != null && !iThread.isAlive()) {
                	break;
                }
                Thread.sleep(100);
            }
            
            // check for cancellation by user
            if (monitor.isCanceled()) {
                monitor.done();
                throw new InterruptedException("Connection cancelled.");
            }
            monitor.done();
        } catch (Throwable e) {
        	SqlBuilderPlugin.log("Open Dialog Job In Built-In Mode: Failure!", e);
        } finally {
            monitor.done();
        }
    }
}