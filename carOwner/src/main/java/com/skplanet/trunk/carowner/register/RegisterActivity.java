package com.skplanet.trunk.carowner.register;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.skplanet.trunk.carowner.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class RegisterActivity extends AppCompatActivity implements RegisterPresenter.IRegisterView, AdapterView.OnItemSelectedListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Bind(R.id.wide_lift_area_spinner)
    Spinner mWideAreaSpn;
    @Bind(R.id.local_lift_area_spinner)
    Spinner mLocalAreaSpn;
    @Bind(R.id.lift_area_edit)
    EditText mLiftAreaEdit;
    @Bind(R.id.lift_method_spinner)
    Spinner mLiftMethodSpn;
    @Bind(R.id.land_area_edit)
    EditText mLandAreaEdit;
    @Bind(R.id.land_method_spinner)
    Spinner mLandMethodSpn;
    @Bind(R.id.ton_spinner)
    Spinner mToneSpn;
    @Bind(R.id.car_type_spinner)
    Spinner mCarTypeSpn;
    @Bind(R.id.fee_edit)
    EditText mFeeEdit;
    @Bind(R.id.car_count_spinner)
    Spinner mCarCountSpn;
    @Bind(R.id.goods_register_phone_edit)
    EditText mGoodsRegisterPhoneEdit;
    @Bind(R.id.goods_owner_phone_edit)
    EditText mGoodsOwnerPhoneEdit;

    String[] mWideLiftAreas, mLocalLiftAreas, mLiftMethods, mLandMethods;
    String[] mToneTypes, mCarTypes, mCarCounts;

    RegisterPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter = new RegisterPresenter(this, new GoodsModel());

        final Resources resources = getResources();
        mWideLiftAreas = resources.getStringArray(R.array.wide_lift_area);
        mLocalLiftAreas = resources.getStringArray(R.array.local_lift_area);
        mLiftMethods = resources.getStringArray(R.array.lift_method);
        mLandMethods = resources.getStringArray(R.array.land_method);
        mToneTypes = resources.getStringArray(R.array.ton_type);
        mCarTypes = resources.getStringArray(R.array.car_type);
        mCarCounts = resources.getStringArray(R.array.car_count);

        mWideAreaSpn.setOnItemSelectedListener(this);
        mLocalAreaSpn.setOnItemSelectedListener(this);
        mLiftMethodSpn.setOnItemSelectedListener(this);
        mLandMethodSpn.setOnItemSelectedListener(this);
    }

    @DebugLog
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.wide_lift_area_spinner:
                // TODO  mWideLiftSpn.getSelectedItemPosition()을 쓰면 되니, 여기서 String을 구할필요가 없다
                break;
            case R.id.local_lift_area_spinner:
                break;
            case R.id.lift_method_spinner:
                break;
            case R.id.land_method_spinner:
                break;
            case R.id.ton_spinner:
                break;
            case R.id.car_type_spinner:
                break;
            case R.id.car_count_spinner:
                break;
        }
    }

    @DebugLog
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @DebugLog
    public void onSaveClicked(View view) {
        String wideLiftArea = mWideLiftAreas[mWideAreaSpn.getSelectedItemPosition()];
        String localLiftArea = mLocalLiftAreas[mLocalAreaSpn.getSelectedItemPosition()];
        String liftArea = mLiftAreaEdit.getText().toString();
        String liftMethod = mLiftMethods[mLiftMethodSpn.getSelectedItemPosition()];
        String landArea = mLandAreaEdit.getText().toString();
        String landMethod = mLandMethods[mLandMethodSpn.getSelectedItemPosition()];
        String ton = mToneTypes[mToneSpn.getSelectedItemPosition()];
        String carType = mCarTypes[mCarTypeSpn.getSelectedItemPosition()];
        int fee = 0;
        try {
            fee = Integer.parseInt(mFeeEdit.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int carCount = Integer.parseInt(mCarCounts[mCarCountSpn.getSelectedItemPosition()]);
        String goodsRegisterPhone = mGoodsRegisterPhoneEdit.getText().toString();
        String goodsOwnerPhone = mGoodsOwnerPhoneEdit.getText().toString();

        mPresenter.addGoods(wideLiftArea, localLiftArea, liftArea, liftMethod, landArea, landMethod,
                ton, carType, fee, carCount, goodsRegisterPhone, goodsOwnerPhone);
    }

    @DebugLog
    @Override
    public void updated() {

    }
}
