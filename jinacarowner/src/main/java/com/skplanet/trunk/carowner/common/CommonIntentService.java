package com.skplanet.trunk.carowner.common;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.skplanet.trunk.carowner.R;

import java.io.IOException;

public class CommonIntentService extends IntentService {

    private static final String TAG = CommonIntentService.class.getSimpleName();

    public CommonIntentService() {
        super(TAG);
    }

    /**
     * GCM을 위한 Instance ID의 토큰을 생성하여 가져온다.
     * @param intent
     */
    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        LLog.i(TAG, "onHandleIntent()... action: " + action);

        switch (action) {
            case Constants.ACTION_GCM_TOKEN_REFRESH:
                // GCM을 위한 Instance ID를 가져온다.
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = null;
                try {
                    synchronized (TAG) {
                        // GCM 앱을 등록하고 획득한 설정파일인 google-services.json을 기반으로 SenderID를 자동으로 가져온다.
                        String default_senderId = getString(R.string.gcm_defaultSenderId);
                        // GCM 기본 scope는 "GCM"이다.
                        String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
                        // Instance ID에 해당하는 토큰을 생성하여 가져온다.
                        token = instanceID.getToken(default_senderId, scope, null);
                        LLog.i(TAG, "GCM Registration Token: " + token);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}