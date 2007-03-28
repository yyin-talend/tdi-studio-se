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

package org.talend.repository.registeruser.proxy;

/**
 * DOC mhirt  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public interface IRegisterUser extends javax.xml.rpc.Service {

    /**
     * DOC mhirt Comment method "getRegisterUserPortAddress".
     * @return
     */
    public java.lang.String getRegisterUserPortAddress();

    /**
     * DOC mhirt Comment method "getRegisterUserPort".
     * @return
     * @throws javax.xml.rpc.ServiceException
     */
    public org.talend.repository.registeruser.proxy.IRegisterUserPortType getRegisterUserPort()
            throws javax.xml.rpc.ServiceException;

    /**
     * DOC mhirt Comment method "getRegisterUserPort".
     * @param portAddress
     * @return
     * @throws javax.xml.rpc.ServiceException
     */
    public org.talend.repository.registeruser.proxy.IRegisterUserPortType getRegisterUserPort(java.net.URL portAddress)
            throws javax.xml.rpc.ServiceException;
}
