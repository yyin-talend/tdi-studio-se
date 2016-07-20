package org.talend.designer.dbmap.utils;

import static org.junit.Assert.*;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.junit.Before;
import org.junit.Test;
import org.talend.designer.dbmap.language.mysql.MysqlLanguage;
import org.talend.designer.dbmap.model.tableentry.TableEntryLocation;

public class DataMapExpressionParserTest {

    private DataMapExpressionParser parser;
    
    @Before
    public void setUp() throws Exception {
        parser = new DataMapExpressionParser(new MysqlLanguage());
    }

    @Test
    public void testParseTableEntryLocations() {
        //test context and built-in expression
        verifyParseResult("context.schema.context.table.column");
        verifyParseResult("context.schema.table.column");
        verifyParseResult("schema.context.table.column");
        verifyParseResult("schema.table.column");
        verifyParseResult("table.column");
        // test expression with blanks
        verifyParseResult(" context .  schema. context . table.column");
        verifyParseResult(" schema . context .table .column");
        verifyParseResult("schema. table . column");
        verifyParseResult("table. column ");
    }

    private void verifyParseResult(String expression) {
        String expectColumnName = expression.substring(expression.lastIndexOf(".") + 1).trim();
        String expectTableName = expression.substring(0, expression.lastIndexOf("."));
        String[] fragments = expectTableName.split("\\.");
        expectTableName = "";
        for (int i = 0; i < fragments.length; i++) {
            String fragment = fragments[i].trim();
            if (i < fragments.length - 1) {
                fragment = fragment + ".";
            }
            expectTableName = expectTableName + fragment;
        }
        TableEntryLocation[] locations = parser.parseTableEntryLocations(expression);
        for (TableEntryLocation location : locations) {
            assertEquals(expectTableName, location.tableName);
            assertEquals(expectColumnName, location.columnName);
        }
    }

    public static void main(String[] args) throws Exception {

        Perl5Matcher matcher = new Perl5Matcher();
        Perl5Compiler compiler = new Perl5Compiler();
        // String PATTERN_STR = "\\s*(\\w+)\\s*(\\.\\s*(\\w+)\\s*)+"; // can't get correct group count.
        String PATTERN_STR = 
                  "(\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*)"
                + "|(\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*)"
                + "|(\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*)"
                + "|(\\s*(\\w+)\\s*\\.\\s*(\\w+)\\s*)";
        String expression = "context. schema.  context  .table.column";
        // String expression = "context.schema.table.column";
        // String expression = "schema.table.column";
        // String expression = "table.column";
        matcher.setMultiline(true);
        PatternMatcherInput patternMatcherInput = new PatternMatcherInput(expression);
        Pattern pattern = compiler.compile(PATTERN_STR);
        while (matcher.contains(patternMatcherInput, pattern)) {
            MatchResult matchResult = matcher.getMatch();
            System.out.println("group count:" + matchResult.groups());
            for (int i = 1; i <= matchResult.groups(); i++) {
                System.out.println("group[" + i + "] content:" + matchResult.group(i));
            }
        }
    }
}
