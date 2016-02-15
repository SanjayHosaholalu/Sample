package com.application.companies.Utilis;

import java.io.IOException;

public class ErrorInfoFactory {
	public static final ErrorInfo getFileReadErrorInfo(IOException e, String filePath) {

		ErrorInfo info = new ErrorInfo();
		info.setCause(e);
		info.setErrorId("FileReadFound");
		info.setContextId("FileLoader");

		info.setErrorType(ErrorInfo.ERROR_TYPE_INTERNAL);
		info.setSeverity(ErrorInfo.SEVERITY_ERROR);

		info.setErrorDescription("Error processing file" + filePath);

		return info;
	}

	public static final ErrorInfo getNetworkNotAvailableErrorInfo(Throwable ex, String context) {

		ErrorInfo info = new ErrorInfo();
		info.setCause(ex);
		info.setErrorId("NetworkNotAvailable");
		info.setContextId(context);

		info.setErrorType(ErrorInfo.ERROR_TYPE_NETWORK_NOT_AVAILABLE);
		info.setSeverity(ErrorInfo.SEVERITY_ERROR);

		info.setErrorDescription("Network not available, cannot complete HTTP call");

		return info;
	}

	public static final ErrorInfo getGenericErrorInfo(Throwable ex, String context) {

		ErrorInfo info = new ErrorInfo();
		info.setCause(ex);
		info.setErrorId("Unknown exception");
		info.setContextId(context);

		info.setErrorType(ErrorInfo.ERROR_TYPE_INTERNAL);
		info.setSeverity(ErrorInfo.SEVERITY_ERROR);

		info.setErrorDescription(ex.toString());

		return info;
	}
	
}
