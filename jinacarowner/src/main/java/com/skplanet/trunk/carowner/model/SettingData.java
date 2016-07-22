package com.skplanet.trunk.carowner.model;

import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by a1000990 on 16. 3. 8..
 */
@Table(name="SettingData", id= BaseColumns._ID)
public class SettingData extends SettingBase {

    public static final String FIELD_APP_FIRST_YN = "app_first_state";
    public static final String FIELD_CHECKIN_FIRST_YN = "checkin_state";

    @Column(name=FIELD_APP_FIRST_YN)
    private String app_first_state;

    @Column(name=FIELD_CHECKIN_FIRST_YN)
    private String checkin_state;

    public static SettingData getSettingData() {
        return getInstance(SettingData.class);
    }

    @Override
    protected String onEncrypt(String rawData) {
        return rawData;
    }

    @Override
    protected String onDecrypt(String encData) {
        return encData;
    }
}
