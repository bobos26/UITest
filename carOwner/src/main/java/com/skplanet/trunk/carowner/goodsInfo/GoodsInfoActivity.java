package com.skplanet.trunk.carowner.goodsInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.skplanet.trunk.carowner.R;
import com.skplanet.trunk.carowner.model.Goods;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class GoodsInfoActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);

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
                ActiveAndroid.beginTransaction();
                for (int i = 0; i < 2; i++) {
                    Goods goods = new Goods();
                    goods.wideLiftArea = "부산시";
                    goods.localLiftArea = "서면";
                    goods.liftArea = "";
                    goods.landArea = "서울시 천호" + i + "동";
                    goods.ton = "1톤미만";
                    goods.fee = 1000000;
                    goods.save();
                }
                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
