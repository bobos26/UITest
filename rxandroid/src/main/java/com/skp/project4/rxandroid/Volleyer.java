package com.skp.project4.rxandroid;

/**
 * Created by a1000990 on 16. 2. 23..
 */

import android.content.Context;
import android.os.Build;
import android.webkit.CookieSyncManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class Volleyer {
    private static Volleyer mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mCtx;

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
            // (session유지) HttpUrlConnection은 내부적으로 CookieManager를 쓴다.
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            // 401 error 에 대해 하위버전 단말에서 IOException이 발생하는 버그가 있어서 이를 처리하고자 OkHttpStack을 사용한다.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            // 하위버전 단말에서 CookieManager.setCookie호출시 SIGSEGV가 발생한다. CookieSyncManager을 create하면 해결
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.createInstance(mCtx);
            }
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelAll(final String url) {
        getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                if (request.getUrl().contains(url)) {
                    return true;
                }
                return false;
            }
        });
    }
}

