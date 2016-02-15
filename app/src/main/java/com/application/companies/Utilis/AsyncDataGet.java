package com.application.companies.Utilis;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class AsyncDataGet extends AsyncTask<String, Void, CompanyException> {
	public  ProgressDialog progressDialog = null;	
	private Context context = null;
	private GetDataInterface getDataObj = null;
	private boolean executionSuccess = false;
	private CompanyException exceptions = null;
	private String prompt = "";

	public AsyncDataGet(Context _context, GetDataInterface _getDataObj, String _prompt){
		context = _context;
		getDataObj = _getDataObj;
		prompt = _prompt;
	}

	public CompanyException getExceptions() {  
		return exceptions;
	}
	
	public boolean isExecutionSuccess() {  
		return this.executionSuccess;
	}
	
	private void setExecutionSuccess(boolean _executionSuccess) {
		this.executionSuccess = _executionSuccess;
	}

	// can use UI thread here
	protected void onPreExecute() {
		// display progress dialog while data is being loaded       
		progressDialog = ProgressDialog.show(context, "Please wait...", prompt, true);
		progressDialog.getWindow().setGravity(Gravity.CENTER);
	}
	
	// can use UI thread here
	protected void onPostExecute(CompanyException ex) {
		Log.d("AsyncDataGet::onPostExecute", "entering...");   
		if (progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		if(isCancelled() && exceptions != null){
			progressDialog.dismiss();
			List<ErrorInfo> errorInfoList = exceptions.getErrorInfoList();
			for(ErrorInfo errInfo : errorInfoList){
				Log.e("AsyncDataGet::onPostExecute", errInfo.getErrorDescription());
				Toast t = Toast.makeText(context , errInfo.getErrorDescription(), Toast.LENGTH_LONG);
				t.show();
			}
		}
		else {
			Log.d("AsyncDataGet::onPostExecute", "Error info list is Null");
			progressDialog.dismiss();
			getDataObj.postExecute(executionSuccess);
		}
	}   

	// automatically done on worker thread (separate from UI thread)
	protected CompanyException doInBackground(String... params) {
		try{
			boolean status = getDataObj.execute();
			setExecutionSuccess(status);
			progressDialog.dismiss();
		}
		catch(CompanyException ex){
			setExecutionSuccess(false);
			exceptions = ex;
			if(exceptions != null){
				Log.e("AsyncDataGet::doInBackground", "Exception encountered. Cancelling execution");
				List<ErrorInfo> errorInfoList = exceptions.getErrorInfoList();
				for(ErrorInfo errInfo : errorInfoList){
					Log.e("AsyncDataGet::doInBackground", errInfo.getErrorDescription());
				}
			} 
			cancel(true);       
		}    
		catch(Exception ex){      
			//Toast.makeText(context, "Internet connection unavailable", Toast.LENGTH_SHORT).show();    
			setExecutionSuccess(false);
			CompanyException _ex = new CompanyException();
			ErrorInfo errInfo = ErrorInfoFactory.getGenericErrorInfo(ex, "AsyncDataGet");
			_ex.addInfo(errInfo);

			Log.e("AsyncDataGet::doInBackground", "Exception encountered. Cancelling execution");
			cancel(true);      
		}
		finally{   
			progressDialog.dismiss();
		}

		return exceptions;
	}

	@Override
	protected void onCancelled (){ 
		progressDialog.dismiss();
		if (progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		Log.d("AsyncDataGet::onCancelled", "entering...");
		getDataObj.postExecute(false);
	}
}

