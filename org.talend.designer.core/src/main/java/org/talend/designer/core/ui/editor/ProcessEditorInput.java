// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.projectsetting.ProjectSettingManager;
import org.talend.repository.editor.JobEditorInput;

/**
 * smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProcessEditorInput extends JobEditorInput {

    public ProcessEditorInput(Item item, boolean load) throws PersistenceException {
        super(item, load);
    }

    public ProcessEditorInput(Item item, boolean load, Boolean readonly) throws PersistenceException {
        super(item, load, readonly);
    }

    protected void saveProcessBefore() {
        // use project setting true
        ProjectSettingManager.defaultUseProjectSetting(getLoadedProcess());
    }

    protected Process createProcess() {
        return new Process(getItem().getProperty());
    }

    public Process getLoadedProcess() {
        return (Process) loadedProcess;
    }

}
