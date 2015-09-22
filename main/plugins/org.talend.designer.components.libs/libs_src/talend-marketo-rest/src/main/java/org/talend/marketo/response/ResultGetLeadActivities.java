package org.talend.marketo.response;

import java.util.List;

import org.talend.marketo.type.LeadActivityRecord;
import org.talend.marketo.type.MarketoError;

public class ResultGetLeadActivities extends RequestResult {
	
	private String nextPageToken;
	private List<LeadActivityRecord> result;

	public String getRequestId() {
		return requestId;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public List<LeadActivityRecord> getResult() {
		return result;
	}

	public void setResult(List<LeadActivityRecord> result) {
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
