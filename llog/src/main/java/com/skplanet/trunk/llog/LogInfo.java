package com.skplanet.trunk.llog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Delete;

/**
 * Created by a1000990 on 2017. 2. 3..
 */

@Table(name="logInfo", id= BaseColumns._ID)
public class LogInfo extends Model {

    public static final String JSON_INSERTED_DATE = "insertedTime";
    public static final String JSON_LOG_TEXT = "log";

    @Column(name= JSON_INSERTED_DATE)
    public String insertedDate;

    @Column(name= JSON_LOG_TEXT)
    public String log;

    public static void deleteAll() {
        try {
            new Delete().from(LogInfo.class).execute();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public static CursorLoader getCursorLoader(Context context) {
        return new CursorLoader(context, ContentProvider.createUri(LogInfo.class, null),
                    null, // projection
                    null, // selection
                    null, // selectionArg
                    null);// sortOrder
    }

    public static LogInfo fromCursor(Cursor cursor) {
        String insertedDate = cursor.getString(cursor.getColumnIndex(JSON_INSERTED_DATE));
        String log = cursor.getString(cursor.getColumnIndex(JSON_LOG_TEXT));

        LogInfo logInfo = new LogInfo();
        logInfo.insertedDate = insertedDate;
        logInfo.log = log;

        return logInfo;
    }
}
