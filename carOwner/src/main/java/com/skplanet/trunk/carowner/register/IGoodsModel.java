package com.skplanet.trunk.carowner.register;

import android.database.Cursor;

import java.util.List;

/**
 * Created by a1000990 on 16. 3. 31..
 */
public interface IGoodsModel {
    void insert(Goods goods);
    void insertAll(List<Goods> goodsList);
    void update(Goods goods);
    Goods getById(int id);
    List<Goods> selectAll();
    void delete(int id);
    void deleteAll();
    Cursor fetchCursor();
}
