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
package org.talend.designer.rowgenerator.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.data.container.Content;
import org.talend.commons.utils.data.container.ContentList;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.rowgenerator.RowGeneratorPlugin;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * class global comment. Detailled comment <br/>
 * 
 * $Id: FunctionManager.java,v 1.13 2007/01/31 05:20:51 pub Exp $
 * 
 */
public class FunctionManager {

    private List<TalendType> talendTypes = null;

    private static FunctionManager instance = new FunctionManager();

    /**
     * qzhang Comment method "getFunctionByName".
     * 
     * @param name is TalendType name.
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public List<Function> getFunctionByName(String name) {
        List<Function> funtions = null;

        for (TalendType talendType : talendTypes) {
            if (talendType.getName().equals(name)) {
                funtions = talendType.getFunctions();
            }
        }

        if (funtions == null) {
            funtions = new ArrayList<Function>();
        }
        return funtions;
    }

    public RepositoryContext getRepositoryContext() {
        Context ctx = CorePlugin.getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    public FunctionManager() {
        List<File> files = new ArrayList<File>();
        // List<URL> list = RowGeneratorPlugin.getDefault().getPerlModuleService().getBuiltInRoutines();
        IProxyRepositoryFactory factory = RowGeneratorPlugin.getDefault().getProxyRepositoryFactory();
        // TODO find a better way to find routine files

        try {

            RootContainer<String, IRepositoryObject> routineContainer = factory.getRoutine();
            ContentList<String, IRepositoryObject> routineAbsoluteMembers = routineContainer.getAbsoluteMembers();

            for (Content<String, IRepositoryObject> object : routineAbsoluteMembers.values()) {
                IRepositoryObject routine = (IRepositoryObject) object.getContent();
                URI uri = CommonPlugin.asLocalURI(routine.getProperty().getItem().eResource().getURI());
                String filePath = uri.devicePath().replaceAll("%20", " "); // to fix URI bug
                filePath = filePath.replace(".properties", ".item");

                files.add(new File(filePath));

                // String completePath = ERepositoryObjectType.getFolderName(ERepositoryObjectType.ROUTINES)
                // + IPath.SEPARATOR + factory.getRoutine().getPath().toString();
                // Resource resource = test.getItemResource(routine.getProperty().getItem());
                // Platform.r
                // IFolder folder = ResourceUtils.getFolder(fsProject, completePath, false);
                // System.out.println(folder);

            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        //        
        // for (int i = 0; i < list.size(); i++) {
        // URL url = list.get(i);
        // try {
        // url = FileLocator.toFileURL(url);
        // File file = new File(url.getFile());
        // files.add(file);
        // } catch (Exception e) {
        // ExceptionHandler.process(e);
        // }
        // }

        FunctionParser parser = new FunctionParser(files.toArray(new File[files.size()]));
        parser.parse();
        talendTypes = parser.getList();

    }

    public static FunctionManager getInstance() {
        return instance;
    }

}
