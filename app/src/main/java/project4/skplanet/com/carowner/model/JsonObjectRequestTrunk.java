package project4.skplanet.com.carowner.model;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a1000990 on 16. 3. 9..
 */
public class JsonObjectRequestTrunk extends JsonObjectRequest {

    public JsonObjectRequestTrunk(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        SettingData settingData = SettingData.getSettingData();
        String accessToken = settingData.getValue(SettingData.FIELD_ACCESS_TOKEN);

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
}
