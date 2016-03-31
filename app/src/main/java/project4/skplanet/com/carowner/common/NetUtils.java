package project4.skplanet.com.carowner.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hugo.weaving.DebugLog;
import project4.skplanet.com.carowner.model.BLERegion;
import project4.skplanet.com.carowner.model.JsonObjectRequestTrunk;
import project4.skplanet.com.carowner.model.SettingData;

/**
 * Created by a1000990 on 16. 2. 23..
 */
public class NetUtils {
    private static final String TAG = NetUtils.class.getSimpleName();

    public static final String SENSING_SERVER_HOST = "https://pxsensalp.syrup.co.kr";
    public static final String PREF_NAME = "setting.pref";

    @DebugLog
    public static void initialize(final Context context) {
        String URL_INIT = SENSING_SERVER_HOST + "/v1/initialize";

        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("duid", "b5dcdfb4248b9618");
            jsonRequest.put("appId", "lIDZrVIKxHyLwPJc");
            jsonRequest.put("initType", "init");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequestTrunk
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
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse()... " + error.getLocalizedMessage());
                    }
                });
        Volleyer.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    @DebugLog
    public static void requestToken (final Context context) {
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequestTrunk
                (Request.Method.POST, URL_REQUST_TOKEN, jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse()..." + response);
                            String accessToken = response.getString("accessToken");
                            SettingData settingData = SettingData.getSettingData();
                            settingData.setValue(SettingData.FIELD_ACCESS_TOKEN, accessToken);
                            settingData.save();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse()... " + error.getLocalizedMessage());
                    }
                });
        Volleyer.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    @DebugLog
    public static void nearest(Context context, String url) {
        String URL = null;
        if (TextUtils.isEmpty(url)) {
            URL = SENSING_SERVER_HOST + "/v2/data/nearest?tid=11111111111111&returnData=BLE&techType=BLE" +
                    "&idType=0288DCBAC7B2436CBED44E9256E67F89&techId=0_264&radius=500&limit=100";
        } else {
            URL = SENSING_SERVER_HOST + url;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequestTrunk
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @DebugLog
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "nearest.onResponse() ..." + response);
                        ActiveAndroid.beginTransaction();
                        try {
                            int major = 0, minor = 0;
                            String mid = null;

                            JSONObject proximities = response.getJSONObject("proximities");
                            JSONArray bleDatas = proximities.getJSONArray("bleData");
                            for (int i=0; i < bleDatas.length(); i++) {
                                JSONObject bleData = bleDatas.getJSONObject(i);
                                JSONObject sourceObj = bleData.getJSONObject("source");
                                major = sourceObj.getInt("major");
                                minor = sourceObj.getInt("minor");
                                JSONObject baseTarget = bleData.getJSONObject("baseTarget");
                                mid = baseTarget.getString("targetId");
                                BLERegion region = new BLERegion(major, minor, mid, null);
                                region.save();
                            }
                            if (response.has("nextFetchUrl")) {
                                String nextUrl = response.getString("nextFetchUrl");
                                new BLERegion(major, minor).update(nextUrl);
                            }
                            ActiveAndroid.setTransactionSuccessful();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            ActiveAndroid.endTransaction();
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volleyer.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
