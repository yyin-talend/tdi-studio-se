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
package org.talend.designer.runprocess.shadow;

import static org.talend.designer.runprocess.shadow.ShadowProcess.EShadowProcessType.FILE_DELIMITED;
import static org.talend.designer.runprocess.shadow.ShadowProcess.EShadowProcessType.FILE_POSITIONAL;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.model.process.IProcess;
import org.talend.core.utils.XmlArray;
import org.talend.designer.runprocess.ProcessStreamTrashReader;
import org.talend.designer.runprocess.Processor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.perl.PerlUtils;
import org.talend.repository.preview.IProcessDescription;
import org.xml.sax.SAXException;

/**
 * Launch a Process in shadow mode. <br/>
 * 
 * $Id$
 * 
 * @param <T>
 */
public class ShadowProcess<T extends IProcessDescription> {

    /**
     * Available Shadow Process Types.
     * 
     * $Id$
     * 
     */
    public static enum EShadowProcessType {
        FILE_DELIMITED,
        FILE_POSITIONAL,
        FILE_CSV,
        FILE_REGEXP;

        private EShadowProcessType() {

        }
    }

    private static final String XML_EXT = "xml";

    private T description;

    private IPath inPath;

    private IPath outPath;
    
    private EShadowProcessType type;

    /**
     * Constructs a new ShadowProcess.
     */
    public ShadowProcess(T description, EShadowProcessType type) {
        super();

        this.description = description;
        this.inPath = new Path(description.getFilepath());
        this.type = type;
        outPath = buildTempXmlFilename(inPath);
    }

    private IProcess buildProcess() {
        IProcess ps = null;
        FileOutputXmlNode outNode = new FileOutputXmlNode("'" + outPath.toOSString() + "'", description.getEncoding());
        switch (type) {
        case FILE_DELIMITED:
            FileInputDelimitedNode inDelimitedNode = new FileInputDelimitedNode("'" + inPath.toOSString() + "'", description
                    .getRowSeparator(), description.getFieldSeparator(), description.getLimitRows(), description
                    .getHeaderRow(), description.getFooterRow());
            ps = new FileinToXmlProcess<FileInputDelimitedNode>(inDelimitedNode, outNode);
            break;
        case FILE_POSITIONAL:
            FileInputPositionalNode inPositionalNode = new FileInputPositionalNode("'" + inPath.toOSString() + "'",
                    description.getRowSeparator(), description.getPattern(), description.getHeaderRow(), description
                            .getFooterRow(), description.getLimitRows());            
            ps = new FileinToXmlProcess<FileInputPositionalNode>(inPositionalNode, outNode);
            break;
        case FILE_CSV:
            FileInputCSVNode inCSVNode = new FileInputCSVNode("'" + inPath.toOSString() + "'", description
                    .getRowSeparator(), description.getFieldSeparator(), description.getLimitRows(), description
                    .getHeaderRow(), description.getFooterRow(), description.getEscapeCharacter(), description.getTextEnclosure());
            ps = new FileinToXmlProcess<FileInputCSVNode>(inCSVNode, outNode);
            break;
        case FILE_REGEXP:
            FileInputRegExpNode inRegExpNode = new FileInputRegExpNode("'" + inPath.toOSString() + "'", description
                    .getRowSeparator(), description.getPattern(), description.getLimitRows(), description
                    .getHeaderRow(), description.getFooterRow());
            ps = new FileinToXmlProcess<FileInputRegExpNode>(inRegExpNode, outNode);
            break;
        default:
            break;
        }
        return ps;
    }

    private static IPath buildTempXmlFilename(IPath inPath) {
        String filename = inPath.lastSegment();
        filename = filename.substring(0, filename.length() - inPath.getFileExtension().length());
        filename += XML_EXT;
        IPath tempPath;
        try {
            tempPath = PerlUtils.getProject().getLocation();
            tempPath = tempPath.append(filename);
        } catch (CoreException e) {
            tempPath = new Path(filename);
        }

        return tempPath;
    }

    public XmlArray run() throws ProcessorException {
        IProcess process = buildProcess();
        Processor processor = new Processor(process);
        try {
            // Delete previous Perl generated file
            File previousFile = outPath.toFile();
            if (previousFile.exists()) {
                previousFile.delete();
            }

            Process ps = processor.run(process.getContextManager().getDefaultContext(), Processor.NO_STATISTICS, Processor.NO_TRACES);
            ProcessStreamTrashReader.readAndForget(ps);

            if (!outPath.toFile().exists()) {
                throw new ProcessorException("Output not generated.");
            }
            FileInputStream fis = new FileInputStream(outPath.toFile());
            try {
                XmlArray array = XmlArray.createFrom(fis);
                return array;
            } finally {
                fis.close();
            }
        } catch (IOException ioe) {
            throw new ProcessorException(ioe);
        } catch (ParserConfigurationException pce) {
            throw new ProcessorException(pce);
        } catch (SAXException se) {
            throw new ProcessorException(se);
        }
    }

}
