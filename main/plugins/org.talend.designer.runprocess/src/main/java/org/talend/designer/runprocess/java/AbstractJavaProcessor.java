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
package org.talend.designer.runprocess.java;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.IProcess;
import org.talend.designer.core.runprocess.Processor;

/**
 * Created by Marvin Wang on Mar 22, 2013.
 */
public abstract class AbstractJavaProcessor extends Processor implements IJavaProcessor {

    /**
     * DOC marvin AbstractJavaProcessor constructor comment.
     * 
     * @param process
     */
    public AbstractJavaProcessor(IProcess process) {
        super(process);
    }

    /**
     * <pre>
     * Makes all command segments up, like ahead commands, jvm commands, cp commands, main-class command, and others.
     * This method should be invoked by {@link #getCommandLine()}. The following is about some methods invoked by this
     * method. 
     * <li>{@link #extractAheadCommandSegments()} to extract ahead command segments like "cd `dirname $0`\n".
     * <li>{@link #extractJavaCommandSegments()} to extract java command segments like "java -Xms256M ". 
     * <li>{@link #extractCPCommandSegments()} to extract cp command segments like "cp ../a.jar:../b.jar..". 
     * <li>{@link #extractMainClassSegments()} to extract the main-class command segment. 
     * <li>{@link #extractArgumentSegments()} to extract other arguments for commands.
     * </pre>
     * 
     * Added by Marvin Wang on Mar 22, 2013.
     * 
     * @return
     */
    protected List<String> makeUpCommandSegments() {
        List<String> commands = new ArrayList<String>();
        commands.addAll(extractAheadCommandSegments());
        commands.addAll(extractJavaCommandSegments());
        commands.addAll(extractCPCommandSegments());
        commands.add(extractMainClassSegments() == null ? "" : extractMainClassSegments()); //$NON-NLS-1$
        commands.addAll(extractArgumentSegments());
        return commands;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.java.IJavaProcessor#extractAheadCommandSegments()
     */
    @Override
    public List<String> extractAheadCommandSegments() {
        return new ArrayList<String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.java.IJavaProcessor#extractJavaCommandSegments()
     */
    @Override
    public List<String> extractJavaCommandSegments() {
        return new ArrayList<String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.java.IJavaProcessor#extractCPCommandSegments()
     */
    @Override
    public List<String> extractCPCommandSegments() {
        return new ArrayList<String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.java.IJavaProcessor#extractMainClassSegments()
     */
    @Override
    public String extractMainClassSegments() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.java.IJavaProcessor#extractArgumentSegments()
     */
    @Override
    public List<String> extractArgumentSegments() {
        return new ArrayList<String>();
    }

}
