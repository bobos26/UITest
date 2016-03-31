package project4.skplanet.com.carowner.common;

/**
 * Created by a1000990 on 16. 2. 23..
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by a1000990 on 16. 2. 19..
 */
public class Volleyer {
    private static Volleyer mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private Volleyer(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(getCacheSize(mCtx));

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }

                    // Returns a cache size equal to approximately three screens worth of images.
                    public int getCacheSize(Context ctx) {
                        final DisplayMetrics displayMetrics = ctx.getResources().
                                getDisplayMetrics();
                        final int screenWidth = displayMetrics.widthPixels;
                        final int screenHeight = displayMetrics.heightPixels;
                        // 4 bytes per pixel
                        final int screenBytes = screenWidth * screenHeight * 4;

                        return screenBytes * 3;
                    }
                });
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

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}

