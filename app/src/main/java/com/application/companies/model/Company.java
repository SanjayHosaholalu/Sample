package com.application.companies.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Company implements Parcelable{

	// ------------------------- data members ------------------------------------
	private int companyID; 
	private String companyName;
	private String companyOwner;
	private String companyStartDate;
	private String companyDescription;
	private String companyDepartments;

	public Company(){

		companyID = 0;
		companyName  = "";
		companyOwner = "";
		companyStartDate = "";
		companyDescription = "";
		companyDepartments = "";

	}

	// Getter setter methods +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	public int getCompanyID() {
		return companyID;
	}
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}


	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyOwner() {
		return companyOwner;
	}
	public void setCompanyOwner(String companyOwner) {
		this.companyOwner = companyOwner;
	}

	public String getCompanyStartDate() {
		return companyStartDate;
	}
	public void setCompanyStartDate(String companyStartDate) {
		this.companyStartDate = companyStartDate;
	}

	public String getCompanyDescription() {
		return companyDescription;
	}
	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}

	public String getCompanyDepartments() {
		return companyDepartments;
	}
	public void setCompanyDepartments(String companyDepartments) {
		this.companyDepartments = companyDepartments;
	}

	// get list of companies from the json
	public static ArrayList<Company> getCompaniesArrayList(String jsonStr){

		ArrayList<Company> companiesList = null;

		try {
			JSONArray companiesJSOnArray = new JSONArray(jsonStr);

			if(companiesJSOnArray != null && companiesJSOnArray.length() > 0){

				companiesList = new ArrayList<Company>();

				for(int i = 0; i<companiesJSOnArray.length(); i++){

					Company company = new Company();

					JSONObject companyObj = companiesJSOnArray.getJSONObject(i);

					if(companyObj != null && companyObj.length() > 0){

						company.setCompanyID(companyObj.getInt("companyID"));
						company.setCompanyName(companyObj.getString("comapnyName"));
						company.setCompanyOwner(companyObj.getString("companyOwner"));
						company.setCompanyStartDate(companyObj.getString("companyStartDate"));
						company.setCompanyDescription(companyObj.getString("companyDescription"));
						company.setCompanyDepartments(companyObj.getString("companyDepartments"));

						companiesList.add(company);
					}
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return companiesList;

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(companyID);
		dest.writeString(companyName);
		dest.writeString(companyOwner);
		dest.writeString(companyStartDate);
		dest.writeString(companyDescription);
		dest.writeString(companyDepartments);
	}

	public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>() {
		public Company createFromParcel(Parcel in) {
			return new Company(in);
		}

		public Company[] newArray(int size) {
			return new Company[size];
		}
	};

	private Company(Parcel in) {

		companyID = in.readInt();
		companyName = in.readString();
		companyOwner = in.readString();
		companyStartDate = in.readString();
		companyDescription = in.readString();
		companyDepartments = in.readString();
	}

}
