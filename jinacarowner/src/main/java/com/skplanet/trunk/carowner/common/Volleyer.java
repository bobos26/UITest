package com.skplanet.trunk.carowner.common;

/**
 * Created by a1000990 on 16. 2. 23..
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by a1000990 on 16. 2. 19..
 */
public class Volleyer {
    private static Volleyer mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private Volleyer(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Volleyer getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Volleyer(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

