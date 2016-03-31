package com.skplanet.trunk.carowner.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.skplanet.trunk.carowner.common.Constants;
import com.skplanet.trunk.carowner.common.CommonIntentService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, CommonIntentService.class);
        intent.setAction(Constants.ACTION_GCM_TOKEN_REFRESH);
        startService(intent);
    }
}
