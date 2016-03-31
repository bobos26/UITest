package com.skplanet.trunk.carowner.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by a1000990 on 16. 2. 25..
 */
@Table(name="nearest", id = BaseColumns._ID)
public class BLERegion extends Model {
    // major, minor 조합은 unique group을 걸어놔서, 0-2664같은 조합이 add되면, REPLACE한다.
    @Column(name="major", uniqueGroups = "group1", onUniqueConflict = Column.ConflictAction.REPLACE)
    public int major;
    @Column(name="minor", uniqueGroups = "group1", onUniqueConflict = Column.ConflictAction.REPLACE)
    public int minor;
    @Column(name="mid", index=true)
    public String mid;

    public BLERegion() {
        super();
    }

    public BLERegion(int major, int minor, String mid) {
        super();

        this.major = major;
        this.minor = minor;
        this.mid = mid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("major: " + major);
        sb.append(" minor: " + minor);
        sb.append(" mid: " + mid);

        return sb.toString();
    }

    public static Cursor fetchResultCursor() {
        String resultRecords = new Select().from(BLERegion.class).toSql();
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }

    public static BLERegion fromCursor(Cursor cursor) {
        int major = cursor.getInt(cursor.getColumnIndexOrThrow("major"));
        int minor = cursor.getInt(cursor.getColumnIndexOrThrow("minor"));
        String mid = cursor.getString(cursor.getColumnIndexOrThrow("mid"));
        return new BLERegion(major, minor, mid);
    }

    public static void delete(int major, int minor) {
        try {
            new Delete().from(BLERegion.class).where("major = ? AND minor = ?", major, minor).execute();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll() {
        try {
            new Delete().from(BLERegion.class).execute();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void update(String nextUrl) {
        try {
            new Update(BLERegion.class)
                    .set(String.format("nextUrl = \"%s\"", nextUrl))
                    .where("major = ? AND minor = ?", major, minor)
                    .execute();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public static List<BLERegion> selectAll() {
        try {
            return new Select()
                    .from(BLERegion.class)
                    .orderBy("major ASC")
                    .execute();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
