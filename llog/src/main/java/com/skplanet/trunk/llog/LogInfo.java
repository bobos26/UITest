package com.skplanet.trunk.llog;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by a1000990 on 2017. 2. 3..
 */

@Table(name="logInfo", id= BaseColumns._ID)
public class LogInfo extends Model {

    public static final String INSERTED_DATE = "insertedTime";
    public static final String LOG_TEXT = "log";

    @Column(name=INSERTED_DATE)
    public String insertedDate;

    @Column(name=LOG_TEXT)
    public String log;
}
