package com.skplanet.trunk.carowner.common;

import android.os.Handler;
import android.os.Looper;

/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 * <p/>
 * Created by dmilicic on 7/29/15.
 */
public class MainThreadImpl {

    private static MainThreadImpl sMainThread;

    private Handler mHandler;

    private MainThreadImpl() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static MainThreadImpl getInstance() {
        if (sMainThread == null) {
            sMainThread = new MainThreadImpl();
        }

        return sMainThread;
    }
}
