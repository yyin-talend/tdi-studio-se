package org.talend.marketo.response;

import java.util.List;

import org.talend.marketo.type.ListRecord;
import org.talend.marketo.type.MarketoError;

public class ResultGetStaticList extends RequestResult {

    private List<ListRecord> result;

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

    public List<ListRecord> getResult() {
        return result;
    }

    public void setResult(List<ListRecord> result) {
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
        return "ResultListOperation [requestId=" + requestId + ", success="
                + success + ", result=" + result + "]";
    }

}
