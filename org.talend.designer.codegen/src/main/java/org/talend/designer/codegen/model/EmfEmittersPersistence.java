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
package org.talend.designer.codegen.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.exception.BusinessException;
import org.talend.designer.codegen.config.LightJetBean;
import org.talend.designer.codegen.i18n.Messages;
import org.talend.designer.codegen.persistence.DocumentRoot;
import org.talend.designer.codegen.persistence.EmittersPoolFactory;
import org.talend.designer.codegen.persistence.EmittersPoolPackage;
import org.talend.designer.codegen.persistence.PoolType;
import org.talend.designer.codegen.persistence.impl.EmittersPoolFactoryImpl;
import org.talend.designer.codegen.persistence.util.EmittersPoolResourceImpl;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class EmfEmittersPersistence {

    private File persistantFile = null;

    private static Logger log = Logger.getLogger(EmfEmittersPersistence.class);

    /**
     * DOC mhirt EmfEmittersPersistance constructor comment.
     * 
     * @param persistantFile
     */
    public EmfEmittersPersistence(File persistantFile) {
        this.persistantFile = persistantFile;
    }

    /**
     * DOC mhirt Comment method "saveEmittersPool".
     * 
     * @param pool
     * @throws BusinessException
     */
    public void saveEmittersPool(List<LightJetBean> pool) throws BusinessException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = null;

        try {
            objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(pool);
            saveEmfPoolFactory(persistantFile, out.toByteArray());
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * DOC mhirt Comment method "loadEmittersPool".
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public List<LightJetBean> loadEmittersPool() {
        List<LightJetBean> emittersToReturn = new ArrayList<LightJetBean>();
        byte[] poolAsBytes = null;
        try {
            poolAsBytes = loadEmfPoolFactory(persistantFile);
        } catch (IOException e) {
            log.info(Messages.getString("EmfEmittersPersistence.CodeGen.DataMissing")); //$NON-NLS-1$
            // Nothing to do if persistent file not found
            return emittersToReturn;
        }

        if (poolAsBytes != null) {
            ByteArrayInputStream stream = new ByteArrayInputStream(poolAsBytes);
            if (stream.available() > 0) {
                try {
                    ObjectInputStream oin = new ObjectInputStream(stream);
                    emittersToReturn = (List<LightJetBean>) oin.readObject();
                    oin.close();
                    return emittersToReturn;
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return new ArrayList<LightJetBean>();
    }

    /**
     * DOC mhirt Comment method "loadEmfPoolFactory".
     * 
     * @param file
     * @return
     * @throws IOException
     */
    private byte[] loadEmfPoolFactory(final File file) throws IOException {
        URI uri = URI.createFileURI(file.getPath());
        EmittersPoolPackage.eINSTANCE.getNsURI();

        Resource res = new EmittersPoolResourceImpl(uri);

        res.load(new FileInputStream(file), null);
        DocumentRoot xmlDoc = (DocumentRoot) res.getContents().get(0);
        PoolType pool = (PoolType) xmlDoc.eContents().get(0);
        return pool.getPersistentPool();
    }

    /**
     * DOC mhirt Comment method "saveEmfPoolFactory".
     * 
     * @param file
     * @param currentPool
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private void saveEmfPoolFactory(File file, byte[] currentPool) {
        URI uri = URI.createFileURI(file.getPath());
        EmittersPoolPackage.eINSTANCE.getNsURI();
        Resource res = new EmittersPoolResourceImpl(uri);

        EmittersPoolFactory fileFact = EmittersPoolFactoryImpl.init();
        DocumentRoot xmlDoc;
        xmlDoc = fileFact.createDocumentRoot();

        PoolType pool = fileFact.createPoolType();

        pool.setPersistentPool(currentPool);
        xmlDoc.setPool(pool);
        try {
            res.getContents().add(xmlDoc);
            res.save(null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
