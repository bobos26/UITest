package com.skplanet.trunk.carowner.common;

/**
 * Created by a1000990 on 16. 3. 3..
 */
public class Constants {

    public static final String SERVER_HOST = "https://pxsensalp.syrup.co.kr";
    /** SharedPreference **/
    public static final String PREF_NAME = "trunk_pref";
    public static final String PREF_TEST_KEY = "test";
    /** Time **/
    public static final int TIME_MILLIS_1SEC = 1000;
    public static final int TIME_MILLIS_1MIN = 1 * 60 * 1000;
    public static final int TIME_MILLIS_1HOUR = 1 * 60 * 60 * 1000;
    public static final int TIME_MILLIS_DAY = 24 * 60 * 60 * 1000;
    public static final int TIME_SEC_1MIN = 60;
    public static final int TIME_SEC_1HOUR = 1 * 60 * 60;
    /** Default value **/
    public static final int ERR_INT_UNDEF = 0;
    public static final String ERR_STR_UNDEF = "";
    public static final String ERR_JSONOBJ_UNDEF = "{}";
    public static final String ERR_JSONARR_UNDEF = "[]";
    /** GCM **/
    public static final String ACTION_GCM_TOKEN_REFRESH = "android.intent.action.GCM_TOKEN_REFRESH";
    /** RAKE **/
    public static final String RAKE_TOKEN = "80b01492a2d9550328096c4cb363a5dff39822e";

    /** DON'T REMOVE **/
    public static final String B = new String(new byte[] {0x25, 0x54, 0x44});
    public static final String O = new String(new byte[] {0x6e, 0x7b, 0x6e});
    public static final String A = new String(new byte[] {0x56, 0x6e, 0x56, 0x2a});
}

