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
package org.talend.designer.runprocess;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

/**
 * Read streams of a process. <br/>
 * 
 * $Id$
 * 
 */
public final class ProcessStreamTrashReader {

    /**
     * Constructs a new ProcessStreamTrashReader.
     */
    private ProcessStreamTrashReader() {
        super();
    }

    public static void readAndForget(Process process) {
        try {
            boolean stopped = false;
            while (!stopped) {
                InputStream is = process.getInputStream();
                DataInputStream din = new java.io.DataInputStream(is);
                StringBuffer sb = new StringBuffer();
                try{
                String line = null;
                    while((line=din.readLine()) != null){
                    System.out.println("getInputStream "+line);
                    }
                }catch(Exception ex){
                ex.getMessage();
                }finally{
                    try{
                    is.close();
                    }catch(Exception ex){}
                }
                
                
//                int len = is.available();
//                if (len > 0) {
//                    byte[] data = new byte[len];
//                    is.read(data);
//                }
                
                
                
                is = process.getErrorStream();
                din = new java.io.DataInputStream(is);
                sb = new StringBuffer();
                try{
                String line = null;
                    while((line=din.readLine()) != null){
                    System.out.println("getErrorStream "+line);
                    }
                }catch(Exception ex){
                ex.getMessage();
                }finally{
                    try{
                    is.close();
                    }catch(Exception ex){}
                }
            
//                len = is.available();
//                if (len > 0) {
//                    byte[] data = new byte[len];
//                    is.read(data);
//                }

                try {
                    process.exitValue();
                    stopped = true;
                } catch (IllegalThreadStateException itse) {
                    // Do nothing
                }
            }
        } catch (Exception ioe) {//IO
            // Do nothing
        }
    }
}
