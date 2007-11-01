// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.codegen.config;

import org.talend.core.model.temp.ECodePart;

/**
 * Shared Object to use as Template Argument in Code Generation.
 * 
 * $Id$
 * 
 */
public class CodeGeneratorArgument {

    private Object argument;

    private boolean statistics;

    private boolean trace;

    private boolean checkingSyntax = false;

    String interpreterPath;

    String libPath;

    String runtimeFilePath;

    String currentProjectName;

    String jobName;

    String contextName;

    private ECodePart codePart;

    String incomingName;

    private boolean isRunInMultiThread;

    private int pauseTime;

    /**
     * Constructor.
     */
    public CodeGeneratorArgument() {
    }

    /**
     * Getter for codePart.
     * 
     * @return the codePart
     */
    public ECodePart getCodePart() {
        return this.codePart;
    }

    /**
     * Sets the codePart.
     * 
     * @param codePart the codePart to set
     */
    public void setCodePart(ECodePart codePart) {
        this.codePart = codePart;
    }

    /**
     * Getter for node.
     * 
     * @return the node
     */
    public Object getArgument() {
        return this.argument;
    }

    /**
     * Sets the node.
     * 
     * @param node the node to set
     */
    public void setNode(Object node) {
        this.argument = node;
    }

    /**
     * Getter for statistics.
     * 
     * @return the statistics
     */
    public boolean isStatistics() {
        return this.statistics;
    }

    /**
     * Sets the statistics.
     * 
     * @param statistics the statistics to set
     */
    public void setStatistics(boolean statistics) {
        this.statistics = statistics;
    }

    /**
     * Getter for trace.
     * 
     * @return the trace
     */
    public boolean isTrace() {
        return this.trace;
    }

    /**
     * Sets the trace.
     * 
     * @param trace the trace to set
     */
    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public String getCurrentProjectName() {
        return this.currentProjectName;
    }

    public void setCurrentProjectName(String currentProjectName) {
        this.currentProjectName = currentProjectName;
    }

    public String getInterpreterPath() {
        return this.interpreterPath;
    }

    public void setInterpreterPath(String interpreterPath) {
        this.interpreterPath = interpreterPath;
    }

    public String getLibPath() {
        return this.libPath;
    }

    public void setLibPath(String libPath) {
        this.libPath = libPath;
    }

    public String getRuntimeFilePath() {
        return this.runtimeFilePath;
    }

    public void setRuntimeFilePath(String projectContext) {
        this.runtimeFilePath = projectContext;
    }

    public String getContextName() {
        return this.contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public boolean isCheckingSyntax() {
        return checkingSyntax;
    }

    public void setCheckingSyntax(boolean checkingSyntax) {
        this.checkingSyntax = checkingSyntax;
    }

    public void setIncomingName(String incomingName) {
        this.incomingName = incomingName;
    }

    public String getIncomingName() {
        return incomingName;
    }

    public boolean getIsRunInMultiThread() {
        return this.isRunInMultiThread;
    }

    public void setIsRunInMultiThread(boolean isRunInMultiThread) {
        this.isRunInMultiThread = isRunInMultiThread;
    }

    /**
     * Getter for pauseTime.
     * 
     * @return the pauseTime
     */
    public int getPauseTime() {
        return this.pauseTime;
    }

    /**
     * Sets the pauseTime.
     * 
     * @param pauseTime the pauseTime to set
     */
    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }
}
