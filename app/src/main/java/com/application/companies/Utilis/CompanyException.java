package com.application.companies.Utilis;

import java.util.ArrayList;
import java.util.List;

public class CompanyException extends RuntimeException{
	
	protected List<ErrorInfo> errorInfoList = new ArrayList<ErrorInfo>();


	public CompanyException() {

	}

	public ErrorInfo addInfo(ErrorInfo info){
		this.errorInfoList.add(info);
		return info;
	}

	public ErrorInfo addInfo(){
		ErrorInfo info = new ErrorInfo();
		this.errorInfoList.add(info);
		return info;
	}

	public List<ErrorInfo> getErrorInfoList() {
		return errorInfoList;
	}
}
