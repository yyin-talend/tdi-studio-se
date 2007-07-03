package org.talend.expressionbuilder.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.TalendType;

public class CategoryManager {

    public java.util.List<TalendType> getInputCategory() {
        FunctionManager functionManager = new FunctionManager();

        java.util.List<TalendType> talendTypes = functionManager.getTalendTypes();
        TalendType allTypes = new TalendType();
        allTypes.setCategory("*All");
        TalendType userDefined = new TalendType();
        userDefined.setCategory("*User Defined");

        for (Iterator<TalendType> iter = talendTypes.iterator(); iter.hasNext();) {
            allTypes.addFunctions(iter.next().getFunctions());
        }

        java.util.List<TalendType> input = new ArrayList<TalendType>();
        input.add(allTypes);
        input.add(userDefined);
        input.addAll(talendTypes);

        return input;

    }
}
