package com.release.bibhu.planetappliances.Netwok;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;




 /**
 * @author Bibhu Prasad Jena
 *
 * Singleton class to check network status.
 */

public final class NetworkStatus {

	private static NetworkStatus instance = new NetworkStatus();

	private NetworkStatus() {
	}

    public static NetworkStatus getInstance() {
		return instance;
	}

	public boolean isConnected(Context con) {
		ConnectivityManager connectivityManager;
		boolean connected = false;
		try {
			connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			connected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();
			return connected;

		} catch (Exception e) {
		}
		return connected;
	}


	public boolean isWiFiConnected(Context context) {
		boolean haveConnectedWifi = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;

		}
		return haveConnectedWifi;
	}

	public boolean isMobileNetworkConnected(Context context) {
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;

		}
		return haveConnectedMobile;
	}
}
