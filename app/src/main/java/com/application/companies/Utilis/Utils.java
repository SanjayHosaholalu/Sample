package com.application.companies.Utilis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class Utils {

	public static boolean isNetworkAvailable(Context context, boolean prompt) {
		boolean isNwAvailable = false;
		isNwAvailable = isNetworkAvailable(context);
		if(prompt && !isNwAvailable)
			Toast.makeText(context, "Network not available\nPlease try again later", Toast.LENGTH_LONG).show();

		return isNwAvailable;
	}

	public static boolean isNetworkAvailable(Context context) {
		boolean isNwAvailable = false;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && 
				info.isAvailable() &&
				info.isConnected()) {
			isNwAvailable = true;
		}
		return isNwAvailable;
	}	 
}
