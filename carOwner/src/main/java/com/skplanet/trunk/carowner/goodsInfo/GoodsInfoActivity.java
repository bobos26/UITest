package com.skplanet.trunk.carowner.goodsInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.skplanet.trunk.carowner.R;
import com.skplanet.trunk.carowner.model.Goods;
import com.skplanet.trunk.carowner.model.GoodsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class GoodsInfoActivity extends AppCompatActivity implements GoodsModel.ICallback {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    GoodsModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);

        mModel = new GoodsModel(this);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_goods_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.actionInsertDB:
//                ActiveAndroid.beginTransaction();
//                for (int i = 0; i < 2; i++) {
                    Goods goods = new Goods();
                    goods.wideLiftArea = "서울시";
                    goods.localLiftArea = "서면";
                    goods.liftArea = "";
                    goods.landArea = "서울시 천호" + new Random().nextInt() + "동";
                    goods.ton = "1톤미만";
                    goods.fee = 1000000;
                    goods.time = Long.MAX_VALUE - System.currentTimeMillis();
                    goods.save();
//                }
//                ActiveAndroid.setTransactionSuccessful();
//                ActiveAndroid.endTransaction();
                break;
            case R.id.actionRemoveDB:
                mModel.deleteAll();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void insertAllCompleted() {
        Toast.makeText(this, R.string.toast_delete_all_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteAllCompleted() {

    }
}
