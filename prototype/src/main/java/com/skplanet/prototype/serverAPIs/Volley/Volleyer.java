package com.skplanet.prototype.serverAPIs.Volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.skplanet.prototype.R;

/**
 * Created by 1001955 on 3/8/16.
 */
public class Volleyer {

    private RequestQueue queue;

    private static volatile Volleyer mInstance = null;
    private static Context mContext;

    public static synchronized Volleyer getInstance(Context context) {
        if (context != null) {
            context = context.getApplicationContext();

            if (mInstance == null) {
                if (mInstance == null) {
                    mInstance = new Volleyer(context);
                }
            }
            return mInstance;
        } else {
            throw new NullPointerException(context.getString(R.string.internal_error_message_context_is_null));
        }
    }

    private Volleyer(Context context) {
        mContext = context;

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(mContext);

    }

    public void addToRequestQueue(JsonObjectRequest jsonObjectRequest) {
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
