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
package org.talend.repository.preview;

import org.eclipse.core.runtime.CoreException;
import org.talend.core.utils.XmlArray;

/**
 * Generates the preview of a file delimited input.
 * 
 * $Id$
 * 
 */
public interface IPreview {

    /**
     * Generates the preview of a file delimited input.
     * 
     * @param description Description a the input file.
     * @return The content of the preview, null in case of errors.
     * @throws CoreException
     * @throws ProcessorException
     */
    XmlArray preview(ProcessDescription description, String type) throws CoreException;
}
