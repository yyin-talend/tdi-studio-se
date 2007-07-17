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
package org.talend.designer.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.BlockCode;
import org.talend.core.model.process.HashConfiguration;
import org.talend.core.model.process.HashableColumn;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IHashConfiguration;
import org.talend.core.model.process.IHashableColumn;
import org.talend.core.model.process.IHashableInputConnections;
import org.talend.core.model.process.IMatchingMode;
import org.talend.core.model.process.Problem;
import org.talend.core.model.temp.ECodePart;
import org.talend.designer.abstractmap.AbstractMapComponent;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.components.commons.AdvancedLookup.MATCHING_MODE;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.language.generation.GenerationManager;
import org.talend.designer.mapper.language.generation.GenerationManagerFactory;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;
import org.talend.designer.mapper.utils.DataMapExpressionParser;
import org.talend.designer.mapper.utils.problems.ProblemsAnalyser;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MapperComponent extends AbstractMapComponent implements IHashableInputConnections {

    private MapperMain mapperMain;

    private List<IMetadataTable> metadataListOut;

    private ExternalMapperData externalData;

    private GenerationManager generationManager;

    /**
     * DOC amaumont MapperComponent constructor comment.
     */
    public MapperComponent() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.AbstractExternalNode#initialize()
     */
    public void initialize() {
        super.initialize();
        initMapperMain(false);
        mapperMain.loadInitialParamters();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IExternalComponent#getPersistentData()
     */
    public IExternalData getExternalData() {
        if (this.externalData == null) {
            this.externalData = new ExternalMapperData();
        }
        return this.externalData;
    }

    private void initMapperMain(boolean forceNew) {
        if (forceNew || !forceNew && mapperMain == null) {
            mapperMain = new MapperMain(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IExternalComponent#open(org.eclipse.swt.widgets.Display)
     */
    public int open(final Display display) {
        // TimeMeasure.start("Total open");
        // TimeMeasure.display = false;
        initMapperMain(true);
        mapperMain.createModelFromExternalData(getIODataComponents(), getMetadataList(), externalData, true);
        Shell shell = mapperMain.createUI(display);
        // TimeMeasure.display = true;
        // TimeMeasure.end("Total open");
        try {
            refreshMapperConnectorData();
            mapperMain.getMapperManager().setOriginalExternalData(externalData.clone());
        } catch (CloneNotSupportedException e1) {
            ExceptionHandler.process(e1);
        }
        while (!shell.isDisposed()) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            } catch (Throwable e) {
                if (MapperMain.isStandAloneMode()) {
                    e.printStackTrace();
                } else {
                    ExceptionHandler.process(e);
                }
            }
        }
        if (MapperMain.isStandAloneMode()) {
            display.dispose();
        }
        return mapperMain.getMapperDialogResponse();
    }

    /**
     * DOC amaumont Comment method "refreshMapperConnectorData".
     */
    public void refreshMapperConnectorData() {
        super.refreshMapperConnectorData();
        mapperMain.loadModelFromInternalData();
        metadataListOut = mapperMain.getMetadataListOut();
        externalData = mapperMain.buildExternalData();
        // System.out.println("refreshMapperConnectorData");
        sortOutputsConnectionsLikeVisualOrder();
    }

    /**
     * Sort outgoingConnections for code generation as visible output zone of tMap.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void sortOutputsConnectionsLikeVisualOrder() {
        List<IConnection> outgoingConnections = (List<IConnection>) getOutgoingConnections();
        Map<String, IConnection> connectionNameToOutgoingConnection = new HashMap<String, IConnection>();
        for (IConnection connection : outgoingConnections) {
            connectionNameToOutgoingConnection.put(connection.getName(), connection);
        }

        List<ExternalMapperTable> outputTables = externalData.getOutputTables();
        List<IConnection> tmpList = new ArrayList<IConnection>(outgoingConnections);
        outgoingConnections.clear();

        int lstSize = outputTables.size();
        for (int i = 0; i < lstSize; i++) {
            ExternalMapperTable table = outputTables.get(i);
            String tableName = table.getName();

            IConnection connection = connectionNameToOutgoingConnection.get(tableName);
            if (connection != null) {
                outgoingConnections.add(connection);
            }
        }
        // add connections without metadata
        for (IConnection connection : tmpList) {
            if (!outgoingConnections.contains(connection)) {
                outgoingConnections.add(connection);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IExternalComponent#open()
     */
    public int open(final Composite parent) {
        initMapperMain(true);
        mapperMain.createModelFromExternalData(getIODataComponents(), getMetadataList(), externalData, true);
        mapperMain.createUI(parent);
        return mapperMain.getMapperDialogResponse();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IExternalComponent#setPersistentData(java.lang.Object)
     */
    public void setExternalData(IExternalData externalData) {
        this.externalData = (ExternalMapperData) externalData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getGeneratedCode()
     */
    public String getGeneratedCode() {
        try {
            ICodeGeneratorService service = Activator.getDefault().getCodeGeneratorService();

            return service.createCodeGenerator().generateComponentCode(this, ECodePart.MAIN);
        } catch (SystemException e) {
            ExceptionHandler.process(e);
        }
        return ""; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getMetadataList()
     */
    public List<IMetadataTable> getMetadataList() {
        return this.metadataListOut;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setMetadataList(java.util.List)
     */
    public void setMetadataList(List<IMetadataTable> metadataTablesOut) {
        this.metadataListOut = metadataTablesOut;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.AbstractExternalNode#setExternalXmlData(java.io.InputStream)
     */
    public void loadDataIn(InputStream in, Reader stringReader) throws IOException, ClassNotFoundException {
        // if (in.available() > 0) {
        // ObjectInputStream oin = null;
        // try {
        // oin = new ObjectInputStream(in);
        // externalData = (ExternalMapperData) oin.readObject();
        //
        // } finally {
        // if (oin != null) {
        // oin.close();
        // }
        // }
        // }

        if (stringReader != null) {
            Unmarshaller unmarshaller = new Unmarshaller(ExternalMapperData.class);
            try {
                externalData = (ExternalMapperData) unmarshaller.unmarshal(stringReader);
            } catch (MarshalException e) {
                ExceptionHandler.process(e);
            } catch (ValidationException e) {
                ExceptionHandler.process(e);
            } finally {
                if (stringReader != null) {
                    stringReader.close();
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#loadDataOut(java.io.OutputStream, java.io.Writer)
     */
    public void loadDataOut(final OutputStream out, Writer writer) throws IOException {
        // System.out.println("loadDataOut");

        initMapperMain(false);

        mapperMain.createModelFromExternalData(getIncomingConnections(), getOutgoingConnections(), externalData,
                getMetadataList(), false);
        ExternalMapperData data = mapperMain.buildExternalData();
        if (mapperMain != null && data != null) {

            try {
                Marshaller marshaller = new Marshaller(writer);
                marshaller.marshal(externalData);
            } catch (MarshalException e) {
                ExceptionHandler.process(e);
            } catch (ValidationException e) {
                ExceptionHandler.process(e);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }

            // ObjectOutputStream objectOut = null;
            // try {
            // objectOut = new ObjectOutputStream(out);
            // objectOut.writeObject(data);
            // } catch (IOException e) {
            // ExceptionHandler.process(e);
            // } finally {
            // if (objectOut != null) {
            // objectOut.close();
            // }
            // }
        }
    }

    public void renameInputConnection(String oldConnectionName, String newConnectionName) {
        if (oldConnectionName == null || newConnectionName == null) {
            throw new NullPointerException();
        }
        if (externalData != null) {
            List<ExternalMapperTable> inputTables = externalData.getInputTables();
            for (ExternalMapperTable table : inputTables) {
                if (table.getName().equals(oldConnectionName)) {
                    table.setName(newConnectionName);
                    TableEntryLocation oldLocation = new TableEntryLocation(oldConnectionName, null);
                    TableEntryLocation newLocation = new TableEntryLocation(newConnectionName, null);
                    replaceLocationsInAllExpressions(oldLocation, newLocation, true);
                    break;
                }
            }
        }
    }

    public void renameOutputConnection(String oldName, String newName) {
        if (oldName == null || newName == null) {
            throw new NullPointerException();
        }
        if (externalData != null) {
            List<ExternalMapperTable> outputTables = externalData.getOutputTables();
            for (ExternalMapperTable table : outputTables) {
                if (table.getName().equals(oldName)) {
                    table.setName(newName);
                    break;
                }
            }
        }
    }

    protected void renameMetadataColumnName(String conectionName, String oldColumnName, String newColumnName) {
        if (conectionName == null || oldColumnName == null || newColumnName == null) {
            throw new NullPointerException();
        }
        if (externalData != null) {
            // rename metadata column name
            List<ExternalMapperTable> tables = new ArrayList<ExternalMapperTable>(externalData.getInputTables());
            tables.addAll(externalData.getOutputTables());
            ExternalMapperTable tableFound = null;
            for (ExternalMapperTable table : tables) {
                if (table.getName().equals(conectionName)) {
                    List<ExternalMapperTableEntry> metadataTableEntries = table.getMetadataTableEntries();
                    for (ExternalMapperTableEntry entry : metadataTableEntries) {
                        if (entry.getName().equals(oldColumnName)) {
                            entry.setName(newColumnName);
                            tableFound = table;
                            break;
                        }
                    }
                    break;
                }
            }

            // it is necessary to update expressions only if renamed column come from input table
            if (tableFound != null && externalData.getInputTables().indexOf(tableFound) != -1) {
                TableEntryLocation oldLocation = new TableEntryLocation(conectionName, oldColumnName);
                TableEntryLocation newLocation = new TableEntryLocation(conectionName, newColumnName);
                replaceLocationsInAllExpressions(oldLocation, newLocation, false);
            }

        }
    }

    /**
     * DOC amaumont Comment method "replaceLocations".
     * 
     * @param oldLocation
     * @param newLocation
     * @param tableRenamed TODO
     * @param newTableName
     * @param newColumnName
     */
    private void replaceLocationsInAllExpressions(TableEntryLocation oldLocation, TableEntryLocation newLocation,
            boolean tableRenamed) {
        // replace old location by new location for all expressions in mapper
        List<ExternalMapperTable> tables = new ArrayList<ExternalMapperTable>(externalData.getInputTables());
        tables.addAll(new ArrayList<ExternalMapperTable>(externalData.getVarsTables()));
        tables.addAll(new ArrayList<ExternalMapperTable>(externalData.getOutputTables()));
        DataMapExpressionParser dataMapExpressionParser = new DataMapExpressionParser(LanguageProvider
                .getCurrentLanguage());
        // loop on all tables
        for (ExternalMapperTable table : tables) {
            List<ExternalMapperTableEntry> metadataTableEntries = table.getMetadataTableEntries();

            String expressionFilter = replaceLocation(oldLocation, newLocation, table.getExpressionFilter(),
                    dataMapExpressionParser, tableRenamed);
            if (expressionFilter != null) {
                table.setExpressionFilter(expressionFilter);
            }

            if (metadataTableEntries != null) {
                // loop on all entries of current table
                for (ExternalMapperTableEntry entry : metadataTableEntries) {
                    String expression = replaceLocation(oldLocation, newLocation, entry.getExpression(),
                            dataMapExpressionParser, tableRenamed);
                    if (expression != null) {
                        entry.setExpression(expression);
                    }
                } // for (ExternalMapperTableEntry entry : metadataTableEntries) {
            }
            if (table.getConstraintTableEntries() != null) {
                for (ExternalMapperTableEntry entry : table.getConstraintTableEntries()) {
                    String expression = replaceLocation(oldLocation, newLocation, entry.getExpression(),
                            dataMapExpressionParser, tableRenamed);
                    if (expression != null) {
                        entry.setExpression(expression);
                    }
                }
            }
        } // for (ExternalMapperTable table : tables) {
    }

    public String replaceLocation(TableEntryLocation oldLocation, TableEntryLocation newLocation,
            String currentExpression, DataMapExpressionParser dataMapExpressionParser, boolean tableRenamed) {
        if (currentExpression == null || currentExpression.trim().length() == 0) {
            return null;
        }
        TableEntryLocation[] tableEntryLocations = dataMapExpressionParser.parseTableEntryLocations(currentExpression);
        // loop on all locations of current expression
        for (int i = 0; i < tableEntryLocations.length; i++) {
            TableEntryLocation currentLocation = tableEntryLocations[i];
            if (tableRenamed && oldLocation.tableName.equals(currentLocation.tableName)) {
                oldLocation.columnName = currentLocation.columnName;
                newLocation.columnName = currentLocation.columnName;
            }
            if (currentLocation.equals(oldLocation)) {
                currentExpression = dataMapExpressionParser.replaceLocation(currentExpression, currentLocation,
                        newLocation);
            }
        } // for (int i = 0; i < tableEntryLocations.length; i++) {
        return currentExpression;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.AbstractExternalNode#getProblems()
     */
    @Override
    public List<Problem> getProblems() {
        initMapperMain(false);
        ProblemsAnalyser problemsAnalyser = new ProblemsAnalyser(mapperMain.getMapperManager());
        return problemsAnalyser.checkProblems(externalData);
    }

    /**
     * Getter for mapperMain.
     * 
     * @return the mapperMain
     */
    public MapperMain getMapperMain() {
        return this.mapperMain;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getComponentDocumentation(java.lang.String, java.lang.String)
     */
    public IComponentDocumentation getComponentDocumentation(String componentLabel, String tempFolderPath) {
        MapperComponentDocumentation componentDocumentation = new MapperComponentDocumentation();
        componentDocumentation.setComponentName(componentLabel);
        componentDocumentation.setTempFolderPath(tempFolderPath);
        componentDocumentation.setExternalData(this.externalData);
        componentDocumentation.setPreviewPicPath(HTMLDocUtils.getPreviewPicPath(this));

        return componentDocumentation;
    }

    public GenerationManager getGenerationManager() {
        if (this.generationManager == null) {
            this.generationManager = GenerationManagerFactory.getInstance().getGenerationManager(
                    LanguageProvider.getCurrentLanguage());
        }
        return this.generationManager;
    }

    @Override
    public List<BlockCode> getBlocksCodeToClose() {
        return getGenerationManager().getBlocksCodeToClose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IHashableInputConnections#getHashConfiguration(java.lang.String)
     */
    public IHashConfiguration getHashConfiguration(String connectionName) {

        IHashConfiguration hashConfigurationForMapper = null;
        ExternalMapperData externalData = (ExternalMapperData) getExternalData();
        List<ExternalMapperTable> inputTables = externalData.getInputTables();
        List<IHashableColumn> hashableColumns = new ArrayList<IHashableColumn>();
        for (ExternalMapperTable inputTable : inputTables) {
            if (inputTable.getName().equals(connectionName)) {
                List<ExternalMapperTableEntry> metadataTableEntries = inputTable.getMetadataTableEntries();
                int metadataTableEntriesListSize = metadataTableEntries.size();
                for (int i = 0; i < metadataTableEntriesListSize; i++) {
                    ExternalMapperTableEntry entry = metadataTableEntries.get(i);
                    if (entry.getExpression() != null && !entry.getExpression().trim().equals("")) {
                        hashableColumns.add(new HashableColumn(entry.getName(), i));
                    }
                }

                IMatchingMode matchingMode = MATCHING_MODE.parse(inputTable.getMatchingMode());
                if (matchingMode == null) {
                    matchingMode = MATCHING_MODE.UNIQUE_MATCH;
                }
                hashConfigurationForMapper = new HashConfiguration(hashableColumns, matchingMode);
                break;
            }
        }

        return hashConfigurationForMapper;
    }

}
