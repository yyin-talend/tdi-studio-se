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
package org.talend.designer.mapper.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.generation.GenerationManagerFactory;
import org.talend.designer.mapper.language.generation.PerlGenerationManager;
import org.talend.designer.mapper.language.perl.PerlLanguage;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DataMapExpressionParserTest {

    /**
     * Test method for
     * {@link org.talend.designer.mapper.utils.DataMapExpressionParser#parseTableEntryLocations(java.lang.String)}.
     */
    @Test
    public void testParse() {
        ILanguage language = new PerlLanguage();
        DataMapExpressionParser expressionParser = new DataMapExpressionParser(language);
        PerlGenerationManager gen = (PerlGenerationManager) GenerationManagerFactory.getInstance()
                .getGenerationManager(language);
        TableEntryLocation[] stringCouples = expressionParser.parseTableEntryLocations("abc * "
                + gen.getTableColumnVariable("table1", "col1") + " + " + gen.getTableColumnVariable("ta_ble2", "co_l2")
                + " - " + gen.getTableColumnVariable("$table1", "col2"));
        assertEquals(stringCouples[0].tableName, "table1");
        assertEquals(stringCouples[0].columnName, "col1");

        assertEquals(stringCouples[1].tableName, "ta_ble2");
        assertEquals(stringCouples[1].columnName, "co_l2");

        assertEquals(stringCouples[2].tableName, "table1");
        assertEquals(stringCouples[2].columnName, "col2");

    }

    // @Test
    // public void retrieveCurrentSource() {
    // String expression = "abc * $table1{col1} + $ta_ble2{co_l2} - $table1{col2}";
    // StringMetadataCouple metadataCouple = DataMapExpressionParser.retrieveCurrentSource(expression, 7); // "abc *
    // $^table1{col1} + $ta_ble2{co_l2} - $table1{col2}";
    // assertEquals(metadataCouple.tableName, "table1");
    // assertEquals(metadataCouple.columnName, "col1");
    //        
    // metadataCouple = DataMapExpressionParser.retrieveCurrentSource(expression, 7); // "abc * $^table1{col1} +
    // $ta_ble2{co_l2} - $table1{col2}";
    // assertEquals(metadataCouple.tableName, "ta_ble2");
    // assertEquals(metadataCouple.columnName, "co_l2");
    //        
    // metadataCouple = DataMapExpressionParser.retrieveCurrentSource(expression, 7); // "abc * $^table1{col1} +
    // $ta_ble2{co_l2} - $table1{col2}";
    // assertEquals(metadataCouple.tableName, "table1");
    // assertEquals(metadataCouple.columnName, "col2");
    //        
    // }

    /**
     * Test method for
     * {@link org.talend.designer.mapper.utils.DataMapExpressionParser#addTablePrefixToColumnName(String, TableEntryLocation[])}.
     */
    @Test
    public void testaddTablePrefixToColumnNameString() {
        ILanguage language = new PerlLanguage();
        DataMapExpressionParser expressionParser = new DataMapExpressionParser(language);
        PerlGenerationManager gen = (PerlGenerationManager) GenerationManagerFactory.getInstance()
                .getGenerationManager(language);

        TableEntryLocation[] locations = new TableEntryLocation[] { new TableEntryLocation("page", "content"),
                new TableEntryLocation("book", "id_book"), };

        String result = expressionParser.addTablePrefixToColumnName("UNIQUE_COMPONENT_NAME", "uc "
                + gen.getTableColumnVariable("page", "content") + " + " + gen.getTableColumnVariable("book", "id_book")
                + " - 2 * " + language.getPrefixTable() + " book " + language.getSuffixTable()
                + language.getPrefixField() + "  id_book " + language.getSuffixField(), locations, true);
        assertEquals("uc " + gen.getTableColumnVariable("page", "page__content") + " + "
                + gen.getTableColumnVariable("book", "book__id_book") + " - 2 * "
                + gen.getTableColumnVariable("book", "book__id_book"), result);

        locations = new TableEntryLocation[] { new TableEntryLocation("book", "id_book"), };

        // result = expressionParser.addTablePrefixToColumnName("uc $page{content} + $book{id_book} - 2 * $ book {
        // id_book }", locations);
        // assertEquals("uc $page{content} + $book{book__id_book} - 2 * $book{book__id_book}", result);

    }

    /**
     * Test method for
     * {@link org.talend.designer.mapper.utils.DataMapExpressionParser#testreplaceLocationString(String, 
     * TableEntryLocation, TableEntryLocation)}.
     */
    @Test
    public void testreplaceLocationString() {
        ILanguage language = new PerlLanguage();
        DataMapExpressionParser expressionParser = new DataMapExpressionParser(language);
        // GenerationManager gen = new GenerationManager(language);

        String result = expressionParser.replaceLocation(" $ book [ id_book ] ", new TableEntryLocation("book",
                "id_book"), new TableEntryLocation("book", "id_book_changed"));
        System.out.println(result);
        assertEquals(" $ book [ id_book_changed ] ", result);

        // String result = expressionParser.replaceLocation("uc "
        // + "$page[content] + $book[id_book] - 2 * $ book [ id_book ]", new TableEntryLocation("book", "id_book"),
        // new TableEntryLocation("book", "id_book_changed"));
        // assertEquals("uc $page[content] + $book[id_book_changed] - 2 * $book[id_book_changed]", result);
        //
        // result = expressionParser.replaceLocation("uc $page[content] + $book[id_book] - 2 * $ book [ id_book ]",
        // new TableEntryLocation("page", "content"),
        // new TableEntryLocation("page", "content_changed"));
        // assertEquals("uc $page[content_changed] + $book[id_book] - 2 * $book[id_book]", result);

    }

}
