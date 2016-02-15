package com.application.companies.DbHelper;

import java.util.ArrayList;

import com.application.companies.model.Company;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public class CompaniesDbHelper {

	/** The name of the database file on the file system */  
	private static final String DATABASE_NAME = "CompaniesDB";     
	/** The version of the database that this class understands. */
	private static final int DATABASE_VERSION = 1; 

	private static final String TABLE_COMPANIES = "companies";

	private static final String companyId_Fld = "companyId";
	private static final String companyName_Fld = "companyName";
	private static final String companyOwner_Fld = "companyOwner";
	private static final String companyStartDate_Fld = "companyStartDate";
	private static final String companyDescription_Fld = "companyDescription";
	private static final String companyDepartments_Fld = "companyDepartments";

	private static String CREATE_COMPANIES_TABLE = "CREATE TABLE " + TABLE_COMPANIES + "("  
			+ companyId_Fld + " INTEGER PRIMARY KEY," + companyName_Fld + " TEXT,"  
			+ companyOwner_Fld + " TEXT," + companyStartDate_Fld + " TEXT," + companyDescription_Fld + " TEXT,"
			+ companyDepartments_Fld + " TEXT" +")"; 

	private final Context context;  
	private OpenHelper openHelper = null;  

	public CompaniesDbHelper(Context context) {
		this.context = context;
		this.openHelper = new OpenHelper(this.context);
	} 

	public SQLiteDatabase getWritableDb() {
		return this.openHelper.getWritableDatabase();
	}

	public SQLiteDatabase getReadableDb() {       
		return this.openHelper.getReadableDatabase();  
	}

	public void closeDB(){   
		this.openHelper.close();
	}

	private class OpenHelper extends SQLiteOpenHelper {
		private Context context;
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			final String sql = null;
			db.beginTransaction();
			try {
				// Create tables and test data
				db.execSQL(CREATE_COMPANIES_TABLE);
				db.setTransactionSuccessful();
			} 
			catch (SQLException e) {
				Log.e("Error creating tables and debug data", e.toString());
				throw e;
			} 
			finally {
				db.endTransaction();
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			db.beginTransaction();
			try {
				db.execSQL("DROP TABLE IF EXISTS " + CREATE_COMPANIES_TABLE);
				db.setTransactionSuccessful();
			} 
			catch (SQLException e) {
				Log.e("Error upgrading tables and debug data", e.toString());
				throw e;
			} 
			finally {
				db.endTransaction();
			}

			onCreate(db);
		}
	}

	public void executeSQL(String sql, Object[] bindArgs) {
		SQLiteDatabase db = this.getWritableDb();
		db.execSQL(sql, bindArgs);
		db.close();
	}

	// add companies to table
	public void addCompaniesData(Company company){

		String fields = String.format("%s, %s, %s, %s, %s, %s", companyId_Fld, companyName_Fld, companyOwner_Fld, companyStartDate_Fld, companyDescription_Fld, companyDepartments_Fld);	
		String sql = "INSERT INTO " +  TABLE_COMPANIES +" (" + fields + ")" + " VALUES (?, ?, ?, ?, ?, ?)";
		Object[] bindArgs1 = new Object[] { 
				company.getCompanyID(),
				company.getCompanyName(),
				company.getCompanyOwner(),
				company.getCompanyStartDate(),
				company.getCompanyDescription(),
				company.getCompanyDepartments()

		};
		try{
			executeSQL(sql, bindArgs1);
		} 
		catch (SQLException e) {
			Log.e("Error saving Attendees to table ", e.toString());
		}
	}

	//get company list info
	public ArrayList<Company> getCompanyListInfo() {

		ArrayList<Company> companyList = null;
		String sql = CompanyCursor.QUERY_ALL;
		SQLiteDatabase db = getReadableDb();
		CompanyCursor companyCursor = (CompanyCursor) db.rawQueryWithFactory(new CompanyCursor.Factory(),sql,null,null);
		Company company = null;
		companyCursor.moveToFirst();
		if(companyCursor.moveToFirst()) { 
			companyList = new ArrayList<Company>();
			do{
				company = new Company();
				company.setCompanyID(companyCursor.getCompanyId());
				company.setCompanyName(companyCursor.getCompanyName());
				company.setCompanyOwner(companyCursor.getCompanyOwner());
				company.setCompanyStartDate(companyCursor.getCompanyStartDate());
				company.setCompanyDescription(companyCursor.getCompanyDescription());
				company.setCompanyDepartments(companyCursor.getCompanyDepartments());
				companyList.add(company);
			}while(companyCursor.moveToNext());

		}
		else
			Log.d("DbHelper::getAttendees","Attendee ARE not Found ,,Attendee Table empty at the moment");

		companyCursor.close();
		db.close();

		return companyList;
	}

	//get company list info by filter item
	public ArrayList<Company> getCompanyListInfoByFilterItem(String string) {

		ArrayList<Company> companyList = null;
		String sql = CompanyCursor.QUERY_ALL + " WHERE " + companyDepartments_Fld + " LIKE '%" + string.trim()  + "%'";
		SQLiteDatabase db = getReadableDb();
		CompanyCursor companyCursor = (CompanyCursor) db.rawQueryWithFactory(new CompanyCursor.Factory(),sql,null,null);
		Company company = null;
		companyCursor.moveToFirst();
		if(companyCursor.moveToFirst()) { 
			companyList = new ArrayList<Company>();
			do{
				company = new Company();
				company.setCompanyID(companyCursor.getCompanyId());
				company.setCompanyName(companyCursor.getCompanyName());
				company.setCompanyOwner(companyCursor.getCompanyOwner());
				company.setCompanyStartDate(companyCursor.getCompanyStartDate());
				company.setCompanyDescription(companyCursor.getCompanyDescription());
				company.setCompanyDepartments(companyCursor.getCompanyDepartments());
				companyList.add(company);
			}while(companyCursor.moveToNext());

		}
		else
			Log.d("DbHelper::getAttendees","Attendee ARE not Found ,,Attendee Table empty at the moment");

		companyCursor.close();
		db.close();

		return companyList;
	}

	//company  cursor 
	public static class CompanyCursor extends SQLiteCursor{
		private static final String QUERY_ALL =	"SELECT * FROM " + TABLE_COMPANIES; 

		private CompanyCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
				String editTable, SQLiteQuery query) {
			super(db, driver, editTable, query);
		}
		private static class Factory implements SQLiteDatabase.CursorFactory{
			public Cursor newCursor(SQLiteDatabase db,
					SQLiteCursorDriver driver, String editTable,
					SQLiteQuery query) {
				return new CompanyCursor(db, driver, editTable, query);
			}
		}

		public int getCompanyId()
		{return getInt(getColumnIndexOrThrow(companyId_Fld));}
		public String getCompanyName()
		{return getString(getColumnIndexOrThrow(companyName_Fld));}
		public String getCompanyOwner()
		{return getString(getColumnIndexOrThrow(companyOwner_Fld));}
		public String getCompanyStartDate()
		{return getString(getColumnIndexOrThrow(companyStartDate_Fld));}
		public String getCompanyDescription()
		{return getString(getColumnIndexOrThrow(companyDescription_Fld));}
		public String getCompanyDepartments()
		{return getString(getColumnIndexOrThrow(companyDepartments_Fld));}
	}	

}
