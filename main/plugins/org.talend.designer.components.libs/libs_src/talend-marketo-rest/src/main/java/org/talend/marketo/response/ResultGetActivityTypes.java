package org.talend.marketo.response;

import java.util.List;

import org.talend.marketo.type.ActivityType;
import org.talend.marketo.type.MarketoError;

public class ResultGetActivityTypes extends RequestResult {

	private String requestId;
	private boolean success;
	private List<ActivityType> result;
	private List<MarketoError> errors;

	public String getRequestId() {
		return requestId;
	}

	public boolean isSuccess() {
		return success;
	}

	public List<ActivityType> getResult() {
		return result;
	}

	public void setResult(List<ActivityType> result) {
		this.result = result;
	}

	public List<MarketoError> getErrors() {
		return errors;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setErrors(List<MarketoError> errors) {
		this.errors = errors;
	}

}
