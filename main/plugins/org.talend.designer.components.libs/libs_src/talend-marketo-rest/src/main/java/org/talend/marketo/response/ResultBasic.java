package org.talend.marketo.response;

import java.util.List;
import java.util.Map;

import org.talend.marketo.type.MarketoError;

public class ResultBasic extends RequestResult {

    private String nextPageToken;
    private List<Map<String, String>> result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Map<String, String>> getResult() {
        return result;
    }

    public void setResult(List<Map<String, String>> result) {
        this.result = result;
    }

    public List<MarketoError> getErrors() {
        return errors;
    }

    public void setErrors(List<MarketoError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ResultBasic [requestId=" + requestId + ", success=" + success
                + ", errors=" + errors + ", nextPageToken=" + nextPageToken
                + ", result=" + result + "]";
    }

}
