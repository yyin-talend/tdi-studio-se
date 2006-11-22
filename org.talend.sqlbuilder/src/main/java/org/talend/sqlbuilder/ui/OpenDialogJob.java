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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
	public OpenDialogJob(ConnectionParameters connectionParameters) {
		super("Open SQLBuilder Dialog");
		this.connectionParameters = connectionParameters;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
            	connectionParameters.setRepositoryNodeBuiltIn(
                		manager.getRepositoryNodeByBuildIn(null, connectionParameters));
        // everything ended ok..
		return Status.OK_STATUS;
	}

}
