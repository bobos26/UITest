package com.skplanet.trunk.carowner.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by a1000990 on 16. 3. 8..
 */
public class Utils {

    public static final String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    public static final String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static final String getActiveNetwork(final Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net == null || !(net.isAvailable() && net.isConnected())) {
            return "NONE";
        }

        return net.getTypeName();
    }

    public static String getMdn(Context context) {
        String result = Constants.ERR_STR_UNDEF;

        if (context == null) {
            return result;
        }

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String msisdn = null;

        try {
            msisdn = tm.getLine1Number();
        } catch (SecurityException se) {
            // should check SecurityException before call getPhoneNumber
        }

        if (msisdn == null || msisdn.length() == 0) {
            return result;
        }

        try {
            String tmp = msisdn.substring(0, 2);
            String tmp1 = msisdn.substring(0, 3);
            if (tmp.equalsIgnoreCase("82")) {
                result = String.format("0%s", msisdn.substring(2, msisdn.length()));
            } else if (tmp1.equalsIgnoreCase("+82")) {
                result = String.format("0%s", msisdn.substring(3, msisdn.length()));
            } else {
                result = msisdn;
            }
        } catch (Exception e) {
        }

        return result;
    }
}
