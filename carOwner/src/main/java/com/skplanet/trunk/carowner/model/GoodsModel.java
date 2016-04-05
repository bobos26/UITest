package com.skplanet.trunk.carowner.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by a1000990 on 16. 3. 31..
 */
public class GoodsModel implements IGoodsModel {
    ICallback mICallback;

    public GoodsModel(ICallback ICallback) {
        mICallback = ICallback;
    }

    @Override
    public void insert(Goods goods) {
        goods.save();
    }

    @Override
    public void insertAll(List<Goods> goodsList) {
        AsyncTask<List<Goods>, Integer, Boolean>  asyncTask = new AsyncTask<List<Goods>, Integer, Boolean>() {

            @Override
            protected Boolean doInBackground(List<Goods>... params) {
                boolean success = false;
                ActiveAndroid.beginTransaction();
                try {
                    for (Goods goods : params[0]) {
                        goods.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                    success = true;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ActiveAndroid.endTransaction();
                }

                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                if (success && mICallback != null) {
                    mICallback.insertAllCompleted();
                }
            }

        };
        asyncTask.execute(goodsList);
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
        AsyncTask<Object, Object, Boolean> asyncTask = new AsyncTask<Object, Object, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                boolean success = false;
                try {
                    new Delete().from(Goods.class).execute();
                    success = true;
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }

                return success;
            }
            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);

                if (success && mICallback != null) {
                    mICallback.deleteAllCompleted();
                }
            }
        };
        asyncTask.execute();
    }

    @Override
    public Cursor fetchCursor() {
        String resultRecords = new Select().from(Goods.class).toSql();
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }

    static public Goods fromCursor(Cursor cursor) {
        String wideLiftArea = cursor.getString(cursor.getColumnIndexOrThrow("wideLiftArea"));
        String localLiftArea = cursor.getString(cursor.getColumnIndexOrThrow("localLiftArea"));
        String liftArea = cursor.getString(cursor.getColumnIndexOrThrow("liftArea"));
        String liftMethod = cursor.getString(cursor.getColumnIndexOrThrow("liftMethod"));
        String landArea = cursor.getString(cursor.getColumnIndexOrThrow("landArea"));
        String landMethod = cursor.getString(cursor.getColumnIndexOrThrow("landMethod"));
        String ton = cursor.getString(cursor.getColumnIndexOrThrow("ton"));
        String carType = cursor.getString(cursor.getColumnIndexOrThrow("carType"));
        int fee = cursor.getInt(cursor.getColumnIndexOrThrow("fee"));
        int carCount = cursor.getInt(cursor.getColumnIndexOrThrow("carCount"));
        String goodsRegisterPhone = cursor.getString(cursor.getColumnIndexOrThrow("goodsRegisterPhone"));
        String goodsOwnerPhone = cursor.getString(cursor.getColumnIndexOrThrow("goodsOwnerPhone"));

        Goods goods = new Goods(wideLiftArea, localLiftArea, liftArea, liftMethod,
                landArea, landMethod, ton, carType, fee, carCount, goodsRegisterPhone, goodsOwnerPhone);
        return goods;
    }

    public interface ICallback {
        void insertAllCompleted();
        void deleteAllCompleted();
    }
}
