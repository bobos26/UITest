package com.skplanet.trunk.carowner;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.skplanet.trunk.carowner.common.LLog;

/**
 * Created by a1000990 on 16. 3. 3..
 */
public class MainApplication extends Application {
    static {
        com.android.volley.VolleyLog.DEBUG = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // workaround for
        // http://code.google.com/p/android/issues/detail?id=20915
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
        }

        ActiveAndroid.initialize(this);
        LLog.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        ActiveAndroid.dispose();
    }
}
