// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.abstractmap.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.talend.core.model.context.UpdateContextVariablesHelper;
import org.talend.core.model.process.IExternalData.ExternalDataType;
import org.talend.core.model.process.node.IExternalMapEntry;
import org.talend.core.model.process.node.IExternalMapTable;

/**
 * cli class global comment. Detailled comment
 */
public class MapDataDelegateHelper {

    private List<? extends IExternalMapTable> inputTables;

    private List<? extends IExternalMapTable> outputTables;

    private List<? extends IExternalMapTable> varsTables;

    public MapDataDelegateHelper(List<? extends IExternalMapTable> inputTables, List<? extends IExternalMapTable> outputTables,
            List<? extends IExternalMapTable> varsTables) {
        super();
        this.inputTables = inputTables;
        this.outputTables = outputTables;
        this.varsTables = varsTables;
    }

    protected List<? extends IExternalMapTable> getInputTables() {
        return this.inputTables;
    }

    protected List<? extends IExternalMapTable> getOutputTables() {
        return this.outputTables;
    }

    protected List<? extends IExternalMapTable> getVarsTables() {
        return this.varsTables;
    }

    public Map<IExternalMapTable, List<IExternalMapEntry>> getExpressionColumns(String expression,
            ExternalDataType... types) {

        Map<IExternalMapTable, List<IExternalMapEntry>> tableMap = new HashMap<IExternalMapTable, List<IExternalMapEntry>>();

        if (types != null && types.length > 0) {

            for (ExternalDataType type : types) {
                List<? extends IExternalMapTable> emTables = null;

                switch (type) {
                case INPUT:
                    emTables = getInputTables();
                    break;
                case OUTPUT:
                    emTables = getOutputTables();
                    break;
                case VAR:
                    emTables = getVarsTables();
                    break;
                default:
                }

                if (emTables != null) {
                    for (IExternalMapTable table : emTables) {
                        for (IExternalMapEntry entry : table.getTableEntries()) {
                            //
                            if (entry.getExpression() != null) {
                                PatternCompiler compiler = new Perl5Compiler();
                                try {
                                    Pattern pattern = compiler
                                            .compile("\\b(" + UpdateContextVariablesHelper.replaceSpecialChar(expression) + ")(\\b|\\_)"); //$NON-NLS-1$ //$NON-NLS-2$
                                    PatternMatcher matcher = new Perl5Matcher();
                                    ((Perl5Matcher) matcher).setMultiline(true);
                                    if (matcher.contains(entry.getExpression(), pattern)) {
                                        List<IExternalMapEntry> entryList = tableMap.get(table);
                                        if (entryList == null) {
                                            entryList = new ArrayList<IExternalMapEntry>();
                                            tableMap.put(table, entryList);
                                        }
                                        entryList.add(entry);
                                    }
                                } catch (MalformedPatternException e) {
                                    //
                                }
                            }
                        }
                    }
                }

            }
        }
        return tableMap;
    }
}
