package com.skplanet.trunk.llog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainReceiver extends BroadcastReceiver {
    public static final String ACTION_LOG = "com.skplanet.trunk.llog.action.LLOG";
    public static final String EXTRA_LOG = "extra_log";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);

    public MainReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction() != null ? intent.getAction() : "";
        switch (action) {
            case ACTION_LOG:
                LogInfo logInfo = new LogInfo();
                logInfo.insertedDate = dateFormat.format(new Date());
                logInfo.log = intent.getStringExtra(EXTRA_LOG);
                logInfo.save();
                break;
            default:
                break;
        }

    }
}
