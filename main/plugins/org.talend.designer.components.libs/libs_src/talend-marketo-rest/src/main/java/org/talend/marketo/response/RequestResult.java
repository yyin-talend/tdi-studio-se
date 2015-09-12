package org.talend.marketo.response;

import java.util.List;

import org.talend.marketo.type.MarketoError;

public abstract class RequestResult {

    String requestId;
    boolean success;
    List<MarketoError> errors;

    public String getRequestId() {
        return requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<MarketoError> getErrors() {
        return errors;
    }

    public abstract List<?> getResult();

}
