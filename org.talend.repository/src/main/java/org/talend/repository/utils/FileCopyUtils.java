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
package org.talend.repository.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.talend.commons.exception.ExceptionHandler;

/**
 * This class is used for copying file from one place to the other.
 * 
 * $Id: CopyFileUtils.java 2007-3-9,下午07:28:36 ftang $
 * 
 */
public class FileCopyUtils {

    /**
     * This methos is used for coping file from one place to the other.
     * 
     * @param srcFilePath
     * @param destFilePath
     * @throws Exception
     */
    public static void copy(String srcFilePath, String destFilePath) {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            byte[] bytearray = new byte[512];
            int len = 0;
            input = new FileInputStream(srcFilePath);
            output = new FileOutputStream(destFilePath);
            while ((len = input.read(bytearray)) != -1) {
                output.write(bytearray, 0, len);
            }

        } catch (Exception fe) {
            ExceptionHandler.process(fe);
        }

        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }
}
