package com.example.apod;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String NO_INTERNET = "no_internet";
    public static final String INTERNET = "internet";

    public String isInternetActive = NO_INTERNET;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);

        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                isInternetActive = INTERNET;
            }
        }
    }


    /* ----------------------------------------------------------------------- */
    /* Проверяем, что интернет у нас есть ------------------------------------ */
    /* ----------------------------------------------------------------------- */
    public String IsInternetActive(final Context context, final Intent intent) {
        int currentInternetStatus = NetworkUtil.getConnectivityStatus(context);
        if (currentInternetStatus == 0) {
            isInternetActive = NO_INTERNET;
        } else {
            isInternetActive = INTERNET;
        }
        return isInternetActive;
    }
    /* ----------------------------------------------------------------------- */
}