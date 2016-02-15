package com.application.companies.Utilis;

import java.util.HashMap;
import java.util.Map;

public class ErrorInfo {
	public static final int ERROR_TYPE_INTERNAL = 1;
	public static final int ERROR_TYPE_EXTERNAL = 2;
	public static final int ERROR_TYPE_NETWORK_NOT_AVAILABLE = 3;

	public static final int SEVERITY_ERROR = 1;
	
	protected Throwable cause                = null;
	protected String    errorId              = null;
	protected String    contextId            = null;

	protected int       errorType            = -1;
	protected int       severity             = -1;

	protected String    userErrorDescription = null;
	protected String    errorDescription     = null;
	protected String    errorCorrection      = null;

	protected Map<String, Object> parameters = new HashMap<String, Object>();

	public void setCause(Throwable _cause) {this.cause = _cause;}
	public Throwable getCause() {return this.cause;}

	public void setErrorId(String Id){ this.errorId = Id;}
	public String getErrorId(){ return this.errorId;}
	
	public void setContextId(String Id){ this.contextId = Id;}
	public String getContextId(){ return this.contextId;}
	
	public void setErrorType(int errType){this.errorType = errType;}
	public int getErrorType(){return this.errorType;}

	public void setSeverity(int _serverity){this.severity = _serverity;}
	public int getSeverity(){ return this.severity;}

	public void setUserErrorDescription(String desc){ this.userErrorDescription = desc;}
	public String getUserErrorDescription(){ return this.userErrorDescription;}

	public void setErrorDescription(String desc){ this.errorDescription = desc;}
	public String getErrorDescription(){ return this.errorDescription;}
	
	public void setErrorCorrection(String correction){this.errorCorrection = correction;}
	public String getErrorCorrection(){return this.errorCorrection;}
}
