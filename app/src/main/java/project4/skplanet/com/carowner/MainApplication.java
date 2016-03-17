package project4.skplanet.com.carowner;

import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;

/**
 * Created by a1000990 on 16. 2. 25..
 */
public class MainApplication extends MultiDexApplication {
    static {
        com.android.volley.VolleyLog.DEBUG = true;
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

        // Volley는 Application Context여야 효율적이다.
        //Volleyer.initialize(this);
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        ActiveAndroid.dispose();
    }
}
