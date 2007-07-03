package org.talend.expressionbuilder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.TalendType;

public class CategoryManager {

    public java.util.List<Category> getInputCategory() {
        FunctionManager functionManager = new FunctionManager();

        java.util.List<TalendType> talendTypes = functionManager.getTalendTypes();

        List<Category> categories = convertTypesToCategories(talendTypes);

        Category allCategories = new Category();
        allCategories.setName("*All");
        Category userDefined = new Category();
        userDefined.setName("*User Defined");

        for (Iterator<Category> iter = categories.iterator(); iter.hasNext();) {
            allCategories.addFunctions(iter.next().getFunctions());
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
