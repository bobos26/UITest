package project4.skplanet.com.carowner.model;

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
    @Column(name="major", uniqueGroups = "group1", onUniqueConflict = Column.ConflictAction.REPLACE)
    public int major;
    @Column(name="minor", uniqueGroups = "group1", onUniqueConflict = Column.ConflictAction.REPLACE)
    public int minor;
    @Column(name="mid", index=true)
    public String mid;
    @Column(name="nextUrl")
    public String nextUrl;

    public BLERegion() {
        super();
    }

    public BLERegion(int major, int minor) {
        super();

        this.major = major;
        this.minor = minor;
    }

    public BLERegion(int major, int minor, String mid, String nextUrl) {
        super();

        this.major = major;
        this.minor = minor;
        this.mid = mid;
        this.nextUrl = nextUrl;
    }

    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(BLERegion.class).getTableName();
        String resultRecords = new Select().from(BLERegion.class).toSql();
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }

    public static BLERegion fromCursor(Cursor cursor) {
        int major = cursor.getInt(cursor.getColumnIndexOrThrow("major"));
        int minor = cursor.getInt(cursor.getColumnIndexOrThrow("minor"));
        String mid = cursor.getString(cursor.getColumnIndexOrThrow("mid"));
        return new BLERegion(major, minor, mid, null);
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
        String set;

        // update시 nextUrl=null이면 "null" String으로 저장된다.
        if (nextUrl == null) {
            set = "nextUrl = NULL";
        } else {
            set = String.format("nextUrl = \"%s\"", nextUrl);
        }
        try {
            new Update(BLERegion.class)
                    .set(set)
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("major: " + major);
        sb.append(" minor: " + minor);
        sb.append(" mid: " + mid);

        return sb.toString();
    }
}
