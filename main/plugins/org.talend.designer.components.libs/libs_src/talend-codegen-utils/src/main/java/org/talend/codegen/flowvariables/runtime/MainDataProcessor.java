package org.talend.codegen.flowvariables.runtime;

/**
 * Processes data retrieved from wrapped component runtime classes as Main data.
 * It returns Main record data without any processing and changes.
 */
class MainDataProcessor extends DataProcessor {

    /**
     * Processes incoming <code>data</code> as Main data, thus returns it without any changes
     * 
     * @param data data object retrieved from component runtime classes
     * @return data without any changes
     */
    public Object processData(Object data) {
        return data;
    }

    /**
     * Processes incoming <code>dataIterable</code> as Main data {@link Iterable}, thus returns it without 
     * any changes
     * 
     * @param dataIterable data {@link Iterable} object retrieved from component runtime classes
     * @return data {@link Iterable} without any changes
     */
    public Iterable<Object> processDataIterable(Iterable<Object> dataIterable) {
        return dataIterable;
    }
}
