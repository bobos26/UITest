package project4.skplanet.com.carowner.model;

import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by a1000990 on 16. 3. 8..
 */
@Table(name="SettingData", id= BaseColumns._ID)
public class SettingData extends SettingBase {

    public static final String FIELD_ACCESS_TOKEN = "access_token";

    @Column(name=FIELD_ACCESS_TOKEN)
    private String access_token;

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
