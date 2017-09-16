package com.jrmartinez.reigndesigntest.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by junior on 9/15/17.
 */

public class NetworkConnection {

    public static boolean isConnected(Context ctx){
        ConnectivityManager conMgr =  (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return netInfo != null;
    }
}
