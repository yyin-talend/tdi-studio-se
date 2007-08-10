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
package org.talend.expressionbuilder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.TalendType;
import org.talend.expressionbuilder.i18n.Messages;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: CategoryManager.java 上午10:06:09 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class CategoryManager {

    public java.util.List<Category> getInputCategory() {
        FunctionManager functionManager = new FunctionManager();

        java.util.List<TalendType> talendTypes = functionManager.getTalendTypes();

        List<Category> categories = convertTypesToCategories(talendTypes);

        Category allCategories = new Category();
        allCategories.setName(Messages.getString("CategoryManager.all")); //$NON-NLS-1$
        Category userDefined = new Category();
        userDefined.setName(Messages.getString("CategoryManager.user.defined")); //$NON-NLS-1$

        for (Iterator<Category> iter = categories.iterator(); iter.hasNext();) {
            final List<Function> functions = iter.next().getFunctions();
            allCategories.addFunctions(functions);
            for (Function function : functions) {
                if (function.isUserDefined()) {
                    userDefined.addFunctions(function);
                }
            }
        }

        List<Category> input = new ArrayList<Category>();
        input.add(allCategories);
        input.add(userDefined);
        input.addAll(categories);

        return input;

    }

    /**
     * Converts the structure of talendTypes to Categories.
     * 
     * @param talendTypes
     * @return
     */
    private List<Category> convertTypesToCategories(List<TalendType> talendTypes) {
        List<Category> categories = new ArrayList<Category>();

        Map<String, List<Function>> map = new HashMap<String, List<Function>>();

        for (Iterator<TalendType> iter = talendTypes.iterator(); iter.hasNext();) {
            TalendType type = iter.next();
            List functions = type.getFunctions();
            for (int i = 0; i < functions.size(); i++) {
                Function func = (Function) functions.get(i);
                List<Function> funcs = map.get(func.getCategory());
                if (funcs == null) {
                    funcs = new ArrayList<Function>();
                    map.put(func.getCategory(), funcs);
                }
                funcs.add(func);
            }
        }

        for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
            String categoryName = iter.next();
            Category category = new Category();
            category.setName(categoryName);
            category.setFunctions(map.get(categoryName));

            categories.add(category);
        }

        return categories;
    }
}
