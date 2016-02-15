package com.application.companies.Webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.application.companies.model.Company;

import android.util.Log;

public class CompanyDataWS {

	private static final String URL = "https://api.myjson.com/bins/2ggcs";
	private static final int REGISTRATION_TIMEOUT = 3 * 1000;
	private static final int WAIT_TIMEOUT = 30 * 1000;

	private String content = null;

	private boolean error = false;

	public ArrayList<Company> getCompanyListFromWS(){

		ArrayList<Company> companiesList = null;

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpResponse response = null;
			HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
			ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

			//HttpPost httpPost = new HttpPost(URL);
			HttpGet httpGet = new HttpGet(URL);

			response = httpclient.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();

			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				content = out.toString();
			} else{
				//Closes the connection.
				Log.w("HTTP1:",statusLine.getReasonPhrase());
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		}catch (ClientProtocolException e) {
			Log.w("HTTP2:",e );
			content = e.getMessage();
			error = true;
		} catch (IOException e) {
			Log.w("HTTP3:",e );
			content = e.getMessage();
			error = true;
		}catch (Exception e) {
			Log.w("HTTP4:",e );
			content = e.getMessage();
			error = true;
		}

		if(!error){
			if(content != null){
				companiesList = Company.getCompaniesArrayList(content);
			}
		}

		return companiesList;

	}
}
