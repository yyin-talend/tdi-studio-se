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
package org.talend.designer.codegen;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Activator for Code Generator.
 * 
 * $Id$
 * 
 */
public class CodeGeneratorActivator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.designer.codegen";

    // The shared instance
    private static CodeGeneratorActivator plugin;

    /**
     * Default Constructor.
     */
    public CodeGeneratorActivator() {
        plugin = this;
    }

    public static CodeGeneratorActivator getDefault() {
        return plugin;
    }
}
