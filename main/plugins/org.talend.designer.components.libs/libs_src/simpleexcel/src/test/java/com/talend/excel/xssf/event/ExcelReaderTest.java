package com.talend.excel.xssf.event;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ExcelReaderTest {

    @ParameterizedTest
    @CsvSource(
            value = {
                    "dd-MM-yyyy@200201,11-05-2020",
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'@200201,2020-05-11T06:15:35.000Z",
                    "yyyy@200201,2020"
            },
            delimiter = '@'
    )
    public void checkSetParseDateFormat(String datePattern, String expected) throws IOException {

        int readLines = 0;
        List<String> row = Collections.emptyList();

        try (InputStream stream = getClass().getResourceAsStream("datetest.xlsx")) {
            Assertions.assertNotNull(stream, "Can not find a resource file");

            ExcelReader reader = new ExcelReader();
            reader.addDateFormat(1, new SimpleDateFormat(datePattern));
            reader.addSheetName(".*", true);
            reader.parse(stream, "UTF-8", null);

            while (reader.hasNext()) {
                readLines++;
                row = reader.next();
            }
        }

        Assertions.assertEquals(1, readLines);
        Assertions.assertFalse(row.isEmpty());
        Assertions.assertEquals(expected, String.join(",", row));
    }

    @Test
    public void testNumberFormat() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("input.xlsx")) {
            Assertions.assertNotNull(stream, "Can not find a resource file");

            List<List<String>> expected = Arrays.asList(
                    Arrays.asList("1", "200001.0021"),
                    Arrays.asList("2", "100002.00001002"),
                    Arrays.asList("3", "5000000000"),
                    Arrays.asList("4", "-2000"),
                    Arrays.asList("5", "43765"),
                    Arrays.asList("6", "12.0000000032"),
                    Arrays.asList("7", "-0.11215154400011")
            );
            List<List<String>> result = new ArrayList<>();

            ExcelReader reader = new ExcelReader();
            reader.addDateFormat(1, new SimpleDateFormat("yyyy-MM-dd"));
            reader.addSheetName(".*", true);
            reader.parse(stream, "UTF-8", null);

            while (reader.hasNext()) {
                List<String> row = reader.next();
                result.add(row);
            }

            Assertions.assertEquals(expected, result);
        }
    }
}
