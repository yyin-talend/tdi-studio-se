package org.talend.codegen.flowvariables.runtime;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Unit-tests for {@link MainDataProcessor} class
 */
public class MainDataProcessorTest {

    /**
     * Checks {@link MainDataProcessor#processData(Object)} returns input Object without any changes
     */
    @Test
    public void testProcessData() {
        Object expectedObject = new Object();
        MainDataProcessor mainDataProcessor = new MainDataProcessor();
        Object actualObject = mainDataProcessor.processData(expectedObject);
        assertEquals(expectedObject, actualObject);
    }

    /**
     * Checks {@link MainDataProcessor#processDataIterable(Object)} returns input {@link Iterable} without any changes
     */
    @Test
    public void testProcessDataIterable() {
        Iterable<Object> expectedIterable = new ArrayList<Object>();
        MainDataProcessor mainDataProcessor = new MainDataProcessor();
        Iterable<Object> actualIterable = mainDataProcessor.processDataIterable(expectedIterable);
        assertEquals(expectedIterable, actualIterable);
    }
}
