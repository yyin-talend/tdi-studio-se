package org.talend.marketo.response;

import java.util.List;

import org.talend.marketo.type.MarketoError;
import org.talend.marketo.type.SyncStatus;

public class ResultSync extends RequestResult {

    private List<SyncStatus> result;

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

    public List<SyncStatus> getResult() {
        return result;
    }

    public void setResult(List<SyncStatus> result) {
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
        return "ResultSyncLeads [requestId=" + requestId + ", success="
                + success + ", errors=" + errors + ", result=" + result + "]";
    }

}
