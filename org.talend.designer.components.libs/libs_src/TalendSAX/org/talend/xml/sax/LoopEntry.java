// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.xml.sax;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the result class.
 * 
 * $Id: LoopEntry.java,v 1.1 2008/03/21 07:20:39 xzhang Exp $
 * 
 */
public class LoopEntry {

    // loop full path
    private String loop;

    // node full paths
    private List<String> paths = new ArrayList<String>();

    // node original path
    private List<String> originalPaths = new ArrayList<String>();

    // result of read, notice the last element of row is a count of sub loop row.
    private List<String[]> rows = new ArrayList<String[]>();;

    // sub loop entry, make this class as a linkedlist
    private LoopEntry subLoop;

    public LoopEntry(String loop) {
        this.loop = loop;
    }

    /**
     * This use in init LoopEntry object. DOC s Comment method "addPath".
     * 
     * @param path
     * @param originalPath
     */
    public void addPath(String path, String originalPath) {
        paths.add(path);
        originalPaths.add(originalPath);
    }

    /**
     * Getter for loop.
     * 
     * @return the loop
     */
    public String getLoop() {
        return this.loop;
    }

    /**
     * Getter for subLoop.
     * 
     * @return the subLoop
     */
    public LoopEntry getSubLoop() {
        return this.subLoop;
    }

    /**
     * Sets the subLoop.
     * 
     * @param subLoop the subLoop to set
     */
    public void setSubLoop(LoopEntry subLoop) {
        this.subLoop = subLoop;
    }

    /**
     * Getter for paths.
     * 
     * @return the paths
     */
    public List<String> getPaths() {
        return this.paths;
    }

    /**
     * Getter for originalPaths.
     * 
     * @return the originalPaths
     */
    public List<String> getOriginalPaths() {
        return this.originalPaths;
    }

    /**
     * Getter for rows.
     * 
     * @return the rows
     */
    public List<String[]> getRows() {
        return this.rows;
    }
}
