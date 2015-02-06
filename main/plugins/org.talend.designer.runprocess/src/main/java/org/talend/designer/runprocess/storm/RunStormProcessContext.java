// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.storm;

import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.RunProcessContext;

/**
 * 
 */
public class RunStormProcessContext extends RunProcessContext {

    /**
     * DOC marvin RunStormProcessContext constructor comment.
     * 
     * @param process
     */
    public RunStormProcessContext(IProcess2 process) {
        super(process);
    }

    @Override
    protected IProcessor getProcessor(IProcess process, Property property) {
        return new StormJavaProcessor(process, property, true);
    }

}
