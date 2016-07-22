package com.skplanet.trunk.carowner.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by oro on 15. 2. 17..
 */
public class LLog {

    private static final String TAG = LLog.class.getSimpleName();

    private static final int TIME_LIMITE = 3 * Constants.TIME_MILLIS_1MIN; // 3 min
    private static final String DEBUG_PACKAGE_NAME = "com.skplanet.proximity.debug";

    private static boolean lastLogOutFlag;
    private static long lastTime;
    private static PackageManager mPackageManager;

    private static boolean isApplicationExists(String packageName) {
        try {
            mPackageManager.getPackageInfo(packageName, PackageManager.GET_GIDS);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static void initialize(Context context) {
        mPackageManager = context.getApplicationContext().getPackageManager();
    }

    public static boolean isLogOut() {
        if (System.currentTimeMillis() - lastTime < TIME_LIMITE) {
            return lastLogOutFlag;
        }

        lastTime = System.currentTimeMillis();
        lastLogOutFlag = isApplicationExists(DEBUG_PACKAGE_NAME);

        return lastLogOutFlag;
    }

    public static void v(String tag, String log) {
        if (!isLogOut()) { return; }
        Log.v(tag, log);
    }

    public static void d(String tag, String log) {
        if (!isLogOut()) { return; }
        Log.d(tag, log);
    }

    public static void i(String tag, String log) {
        if (!isLogOut()) { return; }
        Log.i(tag, log);
    }

    public static void w(String tag, String log) {
        if (!isLogOut()) { return; }
        Log.w(tag, log);
    }

    public static void e(String tag, String log) {
        if (!isLogOut()) { return; }
        Log.e(tag, log);
    }

    public static void v(String tag, String log, Throwable throwable) {
        if (!isLogOut()) { return; }
        Log.v(tag, log, throwable);
    }

    public static void d(String tag, String log, Throwable throwable) {
        if (!isLogOut()) { return; }
        Log.d(tag, log, throwable);
    }

    public static void i(String tag, String log, Throwable throwable) {
        if (!isLogOut()) { return; }
        Log.i(tag, log, throwable);
    }

    public static void w(String tag, String log, Throwable throwable) {
        if (!isLogOut()) { return; }
        Log.w(tag, log, throwable);
    }

    public static void e(String tag, String log, Throwable throwable) {
        if (!isLogOut()) { return; }
        Log.e(tag, log, throwable);
    }

    private static String getTag(String tag) {
        return TAG + ":" + tag;
    }
}

