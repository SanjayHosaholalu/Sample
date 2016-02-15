package com.application.companies.DataMgrs;

import java.util.ArrayList;

import com.application.companies.DbHelper.CompaniesDbHelper;
import com.application.companies.Webservice.CompanyDataWS;
import com.application.companies.model.Company;

import android.content.Context;

public class CompaniesDataMgr {

	public ArrayList<Company> getCompanyDataList(Context context){

		ArrayList<Company> companyList = null;

		CompaniesDbHelper companiesDbHelper = new CompaniesDbHelper(context);

		companyList = companiesDbHelper.getCompanyListInfo();

		if(companyList == null){
			CompanyDataWS companyDataWS = new CompanyDataWS();
			companyList = companyDataWS.getCompanyListFromWS();
			if(companyList != null && companyList.size() > 0){
				for(Company company: companyList){
					companiesDbHelper.addCompaniesData(company);
				}
			}
		}
		return companyList;
	}
	
	// get company list by filter item
	public ArrayList<Company> getCompanyListByFilter(Context context, String str){
		
		ArrayList<Company> companyList = null;
		
		CompaniesDbHelper companiesDbHelper = new CompaniesDbHelper(context);
		
		companyList = companiesDbHelper.getCompanyListInfoByFilterItem(str);
		
		return companyList;
	}

}
