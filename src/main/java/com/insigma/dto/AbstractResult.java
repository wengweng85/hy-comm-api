package com.insigma.dto;

import java.io.Serializable;

public abstract class AbstractResult implements Serializable {
	protected boolean bizSuccess;
	protected int errorCode;
	protected String message;

	public boolean isBizSuccess() {
		return this.bizSuccess;
	}

	public void setBizSuccess(boolean bizSuccess) {
		this.bizSuccess = bizSuccess;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
