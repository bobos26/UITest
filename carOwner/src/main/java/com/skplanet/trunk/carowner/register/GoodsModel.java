package com.skplanet.trunk.carowner.register;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by a1000990 on 16. 3. 31..
 */
public class GoodsModel implements IGoodsModel {
    @Override
    public void insert(Goods goods) {
        goods.save();
    }

    @Override
    public void insertAll(List<Goods> goodsList) {
        ActiveAndroid.beginTransaction();
        try {
            for (Goods goods : goodsList) {
                goods.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    @Override
    public void update(Goods goods) {
        // need to implements
    }

    @Override
    public Goods getById(int id) {
        // need to implements
        return null;
    }

    @Override
    public List<Goods> selectAll() {
        try {
            return new Select()
                    .from(Goods.class)
                    .orderBy("major ASC")
                    .execute();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(int id) {
        // need to implements
    }

    @Override
    public void deleteAll() {
        try {
            new Delete().from(Goods.class).execute();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cursor fetchCursor() {
        String resultRecords = new Select().from(Goods.class).toSql();
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }
}
