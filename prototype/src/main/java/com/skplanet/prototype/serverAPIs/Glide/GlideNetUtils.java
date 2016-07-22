package com.skplanet.prototype.serverAPIs.Glide;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.skplanet.prototype.R;
import com.skplanet.prototype.activities.MainActivity;
import com.skplanet.prototype.serverAPIs.DataModel.ImageData;
import com.skplanet.prototype.serverAPIs.Volley.Volleyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GlideNetUtils {
    private static final String TAG = GlideNetUtils.class.getSimpleName();

    public static final String SENSING_SERVER_HOST = "https://pxsensalp.syrup.co.kr";
    public static final String PREF_NAME = "setting.pref";

    private static volatile GlideNetUtils mInstance = null;
    private static final Object InstanceLock = new Object();
    private Context context;

    public static GlideNetUtils getInstance(Context appContext) {
        if (appContext != null) {
            appContext = appContext.getApplicationContext();

            if (mInstance == null) {
                synchronized (InstanceLock) {
                    if (mInstance == null) {
                        mInstance = new GlideNetUtils(appContext);
                    }
                }
            }
            return mInstance;
        } else {
            throw new NullPointerException(appContext.getString(R.string.internal_error_message_context_is_null));
        }
    }

    private GlideNetUtils(Context context) {
        this.context = context;
    }

    public void initialize() {
        String URL_INIT = SENSING_SERVER_HOST + "/v1/initialize";

        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("duid", "b5dcdfb4248b9618");
            jsonRequest.put("appId", "lIDZrVIKxHyLwPJc");
            jsonRequest.put("initType", "init");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL_INIT, jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse()..." + response);
                            String requestToken = response.getString("requestToken");
                            Log.e(TAG, "requestToken: " + requestToken);

                            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("requestToken", requestToken);
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestToken(context);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse()... " + error.getLocalizedMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // the GET parameters:
                Map<String, String> params = new HashMap<>();
                // Content-Type은 Request.getBodyContentType()으로 정의한다.
                //params.put("Content-Type", "application/json");
                params.put("Accept", "application/json;charset=utf-8");
                params.put("Connection", "keep-alive");
                params.put("x-skp-c3po-sdk-package", "com.skplanet.common.sdk");
                params.put("x-skp-c3po-sdk-version", "1.0.0");
                params.put("x-skp-c3po-device-os", "adr");
                params.put("x-skp-c3po-device-os-version", "21");
                params.put("x-skp-c3po-device-model", "SKP-TEST");
                params.put("x-skp-c3po-device-capabilities", "1111111");
                params.put("x-skp-c3po-network-provider", "SKT");
                params.put("x-skp-c3po-googleplay-version", "7099430");
                params.put("x-skp-c3po-personal-agree", "7");
                params.put("x-skp-c3po-location-agree", "7");
                params.put("x-skp-c3po-battery", "100");
                params.put("x-skp-c3po-timezone", "GMT+09:00");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        Volleyer.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public void requestToken(final Context context) {
        String URL_REQUST_TOKEN = SENSING_SERVER_HOST + "/v1/auth/token";

        final SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);
        final SharedPreferences.Editor editor = pref.edit();
        String requstToken = pref.getString("requestToken", "");

        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("requestToken", requstToken);
            jsonRequest.put("appId", "lIDZrVIKxHyLwPJc");
            jsonRequest.put("appSecretKey", "KqlaXnsJxuprJhQwXQFWALVYAuQUvJWz");
            jsonRequest.put("grantType", "init");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL_REQUST_TOKEN, jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse()..." + response);
                            String accessToken = response.getString("accessToken");
                            editor.putString("accessToken", accessToken);
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        nearest(context, null);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse()... " + error.getLocalizedMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // the GET parameters:
                Map<String, String> params = new HashMap<>();
                // Content-Type은 Request.getBodyContentType()으로 정의한다.
                //params.put("Content-Type", "application/json");
                params.put("Accept", "application/json;charset=utf-8");
                params.put("Connection", "keep-alive");
                params.put("x-skp-c3po-sdk-package", "com.skplanet.common.sdk");
                params.put("x-skp-c3po-sdk-version", "1.0.0");
                params.put("x-skp-c3po-device-os", "adr");
                params.put("x-skp-c3po-device-os-version", "21");
                params.put("x-skp-c3po-device-model", "SKP-TEST");
                params.put("x-skp-c3po-device-capabilities", "1111111");
                params.put("x-skp-c3po-network-provider", "SKT");
                params.put("x-skp-c3po-googleplay-version", "7099430");
                params.put("x-skp-c3po-personal-agree", "7");
                params.put("x-skp-c3po-location-agree", "7");
                params.put("x-skp-c3po-battery", "100");
                params.put("x-skp-c3po-timezone", "GMT+09:00");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        Volleyer.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public void nearest(final Context context, String url) {
        int major = 0, minor = 264;
        String URL = null;
        if (TextUtils.isEmpty(url)) {
            URL = SENSING_SERVER_HOST + "/v2/data/nearest?tid=11111111111111&returnData=BLE&techType=BLE" +
                    "&idType=0288DCBAC7B2436CBED44E9256E67F89&techId=0_264&radius=500&limit=1000";
        } else {
            URL = SENSING_SERVER_HOST + url;
        }
        final SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);
        final String accessToken = pref.getString("accessToken", "");
        Log.e(TAG, "accessToken = " + accessToken);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ActiveAndroid.beginTransaction();

                        try {
                            int major = 0, minor = 0;
                            String targetId = null;

                            JSONObject proximities = response.getJSONObject("proximities");
                            JSONArray bleDatas = proximities.getJSONArray("bleData");
                            Log.i("JINA", "bleDatas.length(): " + bleDatas.length());
                            for (int i = 0; i < bleDatas.length(); i++) {
                                JSONObject bleData = bleDatas.getJSONObject(i);
                                JSONObject sourceObj = bleData.getJSONObject("source");
                                major = sourceObj.getInt("major");
                                minor = sourceObj.getInt("minor");
                                JSONObject baseTarget = bleData.getJSONObject("baseTarget");
                                targetId = baseTarget.getString("targetId");
                                ImageData data = new ImageData(targetId, MainActivity.KEI_IMAGE_URL, major, minor);
                                MainActivity.imageDataList.add(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            MainActivity.notifyGlideImageListAdaptor();
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // the GET parameters:
                Map<String, String> params = new HashMap<>();
                // Content-Type은 Request.getBodyContentType()으로 정의한다.
                //params.put("Content-Type", "application/json");
                params.put("Accept", "application/json;charset=utf-8");
                params.put("Connection", "keep-alive");
                params.put("x-skp-c3po-access-token", accessToken);
                params.put("x-skp-c3po-sdk-package", "com.skplanet.common.sdk");
                params.put("x-skp-c3po-sdk-version", "1.0.0");
                params.put("x-skp-c3po-device-os", "adr");
                params.put("x-skp-c3po-device-os-version", "21");
                params.put("x-skp-c3po-device-model", "SKP-TEST");
                params.put("x-skp-c3po-device-capabilities", "1111111");
                params.put("x-skp-c3po-network-provider", "SKT");
                params.put("x-skp-c3po-googleplay-version", "7099430");
                params.put("x-skp-c3po-personal-agree", "7");
                params.put("x-skp-c3po-location-agree", "7");
                params.put("x-skp-c3po-battery", "100");
                params.put("x-skp-c3po-timezone", "GMT+09:00");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        Volleyer.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
