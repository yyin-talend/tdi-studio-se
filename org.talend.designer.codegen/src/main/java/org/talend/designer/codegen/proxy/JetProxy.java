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
package org.talend.designer.codegen.proxy;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.codegen.jet.JETEmitter;
import org.eclipse.emf.codegen.jet.JETException;
import org.talend.designer.codegen.config.JetBean;
import org.talend.designer.codegen.model.CodeGeneratorEmittersPoolFactory;

/**
 * This class allows the Code Generation with for a fully initiated JetBean.
 * 
 * $Id$
 * 
 */
public class JetProxy {

    private JetBean jetBean = null;

    private IProgressMonitor monitor = null;

    public JetProxy(JetBean jetBean) {
        this.jetBean = jetBean;
        monitor = new NullProgressMonitor();
    }

    /**
     * Get the initalized Emitter for this Bean and use it to generate code.
     * 
     * @param monitor
     * @return
     * @throws CoreException
     * @throws JETException
     */
    public String generate() throws CoreException, JETException {
        JETEmitter emitter = CodeGeneratorEmittersPoolFactory.getEmitterPool().get(jetBean);
        IProgressMonitor sub = new SubProgressMonitor(monitor, 1);
        String result = "";
        if (emitter != null) {
            result = emitter.generate(sub, new Object[] { jetBean.getArgument() });
            sub.worked(1);
        }
        return result;
    }
}
