package org.talend.salesforceBulk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.csvreader.CsvReader;
import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BatchStateEnum;
import com.sforce.async.CSVReader;
import com.sforce.async.ContentType;
import com.sforce.async.JobInfo;
import com.sforce.async.JobStateEnum;
import com.sforce.async.OperationEnum;
import com.sforce.async.RestConnection;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SalesforceBulkAPI {

    private final String FILE_ENCODING = "UTF-8";

    private String username;

    private String password;

    private String sObjectType;

    private OperationEnum operation;

    private String externalIdFieldName;

    private ContentType contentType;

    private String bulkFileName;

    private int maxBytesPerBatch;

    private int maxRowsPerBatch;

    private List<BatchInfo> batchInfoList;

    private void setBulkOperation(String sObjectType, String operationStr, String externalIdFieldName, String contentTypeStr,
            String bulkFileName, int maxBytes, int maxRows) {
        this.sObjectType = sObjectType;
        if ("insert".equals(operationStr)) {
            operation = OperationEnum.insert;
        } else if ("update".equals(operationStr)) {
            operation = OperationEnum.update;
        } else if ("upsert".equals(operationStr)) {
            operation = OperationEnum.upsert;
        }

        this.externalIdFieldName = externalIdFieldName;

        if ("csv".equals(contentTypeStr)) {
            contentType = ContentType.CSV;
        } else if ("xml".equals(contentTypeStr)) {
            contentType = ContentType.XML;
        }
        this.bulkFileName = bulkFileName;

        int sforceMaxBytes = 10 * 1024 * 1024;
        int sforceMaxRows = 10000;
        maxBytesPerBatch = (maxBytes > sforceMaxBytes) ? sforceMaxBytes : maxBytes;
        maxRowsPerBatch = (maxRows > sforceMaxRows) ? sforceMaxRows : maxRows;
    }

    private RestConnection connection;

    public RestConnection getConnection() {
        return connection;
    }

    public void login(RestConnection connection) {
        this.connection = connection;
    }

    public void login(String endpoint, String username, String password, String apiVersion) throws ConnectionException,
            AsyncApiException {
        this.connection = getRestConnection(endpoint, username, password, apiVersion);
    }

    private JobInfo job;

    private CsvReader baseFileReader;

    private List<String> baseFileHeader;

    private int baseFileHeaderSize;

    public void executeBulk(String sObjectType, String operationStr, String externalIdFieldName, String contentTypeStr,
            String bulkFileName, int maxBytes, int maxRows) throws AsyncApiException, ConnectionException, IOException {
        setBulkOperation(sObjectType, operationStr, externalIdFieldName, contentTypeStr, bulkFileName, maxBytes, maxRows);
        job = createJob();
        batchInfoList = createBatchesFromCSVFile();
        closeJob();
        awaitCompletion();
        prepareLog();
    }

    private void prepareLog() throws IOException {
        baseFileReader = new CsvReader(new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(
                bulkFileName), FILE_ENCODING)), ',');
        if (baseFileReader.readRecord())
            baseFileHeader = Arrays.asList(baseFileReader.getValues());
        baseFileHeaderSize = baseFileHeader.size();
    }

    private boolean useProxy = false;

    private String proxyHost;

    private int proxyPort;

    private String proxyUsername;

    private String proxyPassword;

    public void setProxy(boolean useProxy, String host, int port, String username, String password) {
        this.proxyHost = host;
        this.proxyPort = port;
        this.proxyUsername = username;
        this.proxyPassword = password;
        this.useProxy = useProxy;
    }

    private void setProxyToConnection(ConnectorConfig conn) {
        if (useProxy) {
            conn.setProxy(proxyHost, proxyPort);
            if (proxyUsername != null && !"".equals(proxyUsername)) {
                conn.setProxyUsername(proxyUsername);
            }
            if (proxyPassword != null && !"".equals(proxyPassword)) {
                conn.setProxyPassword(proxyPassword);
            }
        }
    }

    private RestConnection getRestConnection(String endpoint, String username, String password, String apiVersion)
            throws ConnectionException, AsyncApiException {
        ConnectorConfig partnerConfig = new ConnectorConfig();
        partnerConfig.setUsername(username);
        partnerConfig.setPassword(password);
        partnerConfig.setAuthEndpoint(endpoint);
        setProxyToConnection(partnerConfig);
        // Creating the connection automatically handles login and stores
        // the session in partnerConfig
        new PartnerConnection(partnerConfig);
        // When PartnerConnection is instantiated, a login is implicitly
        // executed and, if successful,
        // a valid session is stored in the ConnectorConfig instance.
        // Use this key to initialize a RestConnection:
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(partnerConfig.getSessionId());
        // The endpoint for the Bulk API service is the same as for the normal
        // SOAP uri until the /Soap/ part. From here it's '/async/versionNumber'
        String soapEndpoint = partnerConfig.getServiceEndpoint();
        String restEndpoint = soapEndpoint.substring(0, soapEndpoint.indexOf("Soap/")) + "async/" + apiVersion;
        config.setRestEndpoint(restEndpoint);
        setProxyToConnection(config);
        // This should only be false when doing debugging.
        config.setCompression(true);
        // Set this to true to see HTTP requests and responses on stdout
        config.setTraceMessage(false);
        RestConnection connection = new RestConnection(config);
        return connection;
    }

    private JobInfo createJob() throws AsyncApiException {
        JobInfo job = new JobInfo();

        job.setObject(sObjectType);
        job.setOperation(operation);
        if (OperationEnum.upsert.equals(operation)) {
            job.setExternalIdFieldName(externalIdFieldName);
        }
        job.setContentType(contentType);
        job = connection.createJob(job);
        // System.out.println(job);
        return job;
    }

    private List<BatchInfo> createBatchesFromCSVFile() throws IOException, AsyncApiException {
        List<BatchInfo> batchInfos = new ArrayList<BatchInfo>();
        BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(bulkFileName), FILE_ENCODING));
        // read the CSV header row
        byte[] headerBytes = (rdr.readLine() + "\n").getBytes("UTF-8");
        int headerBytesLength = headerBytes.length;
        File tmpFile = File.createTempFile("sforceBulkAPI", ".csv");
        // Split the CSV file into multiple batches
        try {
            FileOutputStream tmpOut = new FileOutputStream(tmpFile);
            int currentBytes = 0;
            int currentLines = 0;
            String nextLine;
            while ((nextLine = rdr.readLine()) != null) {
                byte[] bytes = (nextLine + "\n").getBytes("UTF-8");

                // Create a new batch when our batch size limit is reached
                if (currentBytes + bytes.length > maxBytesPerBatch || currentLines > maxRowsPerBatch) {
                    createBatch(tmpOut, tmpFile, batchInfos);
                    currentBytes = 0;
                    currentLines = 0;
                }
                if (currentBytes == 0) {
                    tmpOut = new FileOutputStream(tmpFile);
                    tmpOut.write(headerBytes);
                    currentBytes = headerBytesLength;
                    currentLines = 1;
                }
                tmpOut.write(bytes);
                currentBytes += bytes.length;
                currentLines++;
            }
            // Finished processing all rows
            // Create a final batch for any remaining data

            if (currentLines > 1) {
                createBatch(tmpOut, tmpFile, batchInfos);
            }
        } finally {
            tmpFile.delete();
        }
        return batchInfos;
    }

    private void createBatch(FileOutputStream tmpOut, File tmpFile, List<BatchInfo> batchInfos) throws IOException,
            AsyncApiException {
        tmpOut.flush();
        tmpOut.close();
        FileInputStream tmpInputStream = new FileInputStream(tmpFile);
        try {
            BatchInfo batchInfo = connection.createBatchFromStream(job, tmpInputStream);
            // System.out.println(batchInfo);
            batchInfos.add(batchInfo);
        } finally {
            tmpInputStream.close();
        }
    }

    private void closeJob() throws AsyncApiException {
        JobInfo closeJob = new JobInfo();
        closeJob.setId(job.getId());
        closeJob.setState(JobStateEnum.Closed);
        connection.updateJob(closeJob);
    }

    private void awaitCompletion() throws AsyncApiException {
        long sleepTime = 0L;
        Set<String> incomplete = new HashSet<String>();
        for (BatchInfo bi : batchInfoList) {
            incomplete.add(bi.getId());
        }
        while (!incomplete.isEmpty()) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
            // System.out.println("Awaiting results..." + incomplete.size());
            sleepTime = 100L;
            BatchInfo[] statusList = connection.getBatchInfoList(job.getId()).getBatchInfo();
            for (BatchInfo b : statusList) {
                if (b.getState() == BatchStateEnum.Completed || b.getState() == BatchStateEnum.Failed) {
                    incomplete.remove(b.getId());
                }
            }
        }
    }

    private Map<String, String> getBaseFileRow() throws IOException {
        Map<String, String> dataInfo = new HashMap<String, String>();
        if (baseFileReader.readRecord()) {
            List<String> row = Arrays.asList(baseFileReader.getValues());
            for (int i = 0; i < row.size(); i++) {
                dataInfo.put(baseFileHeader.get(i), row.get(i));
            }
        }
        return dataInfo;
    }

    public List<Map<String, String>> getBatchLog(int batchNum) throws AsyncApiException, IOException {
        // batchInfoList was populated when batches were created and submitted
        List<Map<String, String>> resultInfoList = new ArrayList<Map<String, String>>();
        Map<String, String> resultInfo;
        BatchInfo b = batchInfoList.get(batchNum);
        CSVReader rdr = new CSVReader(connection.getBatchResultStream(job.getId(), b.getId()));

        List<String> resultHeader = rdr.nextRecord();
        int resultCols = resultHeader.size();
        List<String> row;
        while ((row = rdr.nextRecord()) != null) {
            resultInfo = new HashMap<String, String>();
            resultInfo.putAll(getBaseFileRow());
            for (int i = 0; i < resultCols; i++) {
                resultInfo.put(resultHeader.get(i), row.get(i));
            }
            resultInfoList.add(resultInfo);
            // boolean success = Boolean.valueOf(resultInfo.get("Success"));
            // boolean created = Boolean.valueOf(resultInfo.get("Created"));
            // String id = resultInfo.get("Id");
            // String error = resultInfo.get("Error");
            // if (success && created) {
            // System.out.println("Created row with id " + id);
            // } else if (!success) {
            // System.out.println("Failed with error: " + error);
            // }
        }

        return resultInfoList;
    }

    public int getBatchCount() {
        return batchInfoList.size();
    }
}
