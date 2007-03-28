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
public interface IRegisterUserPortType extends java.rmi.Remote {

    /**
     * DOC mhirt Comment method "registerUser".
     * @param email
     * @param country
     * @param designerversion
     * @return
     * @throws java.rmi.RemoteException
     */
    public boolean registerUser(java.lang.String email, java.lang.String country, java.lang.String designerversion)
            throws java.rmi.RemoteException;

    /**
     * DOC mhirt Comment method "registerUserWithProductName".
     * @param email
     * @param country
     * @param designerversion
     * @param productname
     * @return
     * @throws java.rmi.RemoteException
     */
    public boolean registerUserWithProductName(java.lang.String email, java.lang.String country,
            java.lang.String designerversion, java.lang.String productname) throws java.rmi.RemoteException;
}
