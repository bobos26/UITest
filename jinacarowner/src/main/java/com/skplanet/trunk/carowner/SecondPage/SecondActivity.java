package com.skplanet.trunk.carowner.SecondPage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.skplanet.trunk.carowner.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class SecondActivity extends AppCompatActivity {
	private static final String TAG = SecondActivity.class.getSimpleName();
	public static final String SHOW_REGISTER_GOODS_ACTION = "registerGoods";
	public static final String SHOW_GOODS_INFO_LIST_ACTION = "showGoodsInfoList";
	public static final String SHOW_ORDERS_INFO_LIST_ACTION = "showOrdersInfoFragment";

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		initUI();
		initFragment(getIntent().getAction());
	}

	private void initFragment(String action) {
		// 트랜잭션 시작
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		switch (action) {
			case SHOW_REGISTER_GOODS_ACTION:
				// 프레그먼트 생성과 추가
				RegisterGoodsFragment registerCarFragment = new RegisterGoodsFragment();
				fragmentTransaction.add(R.id.fragment, registerCarFragment, SHOW_REGISTER_GOODS_ACTION);
				fragmentTransaction.commit();
				break;
			case SHOW_GOODS_INFO_LIST_ACTION:
				ShowGoodsInfoFragment showGoodsInfoFragment = new ShowGoodsInfoFragment();
				fragmentTransaction.add(R.id.fragment, showGoodsInfoFragment, SHOW_GOODS_INFO_LIST_ACTION);
				fragmentTransaction.commit();
				break;
			case SHOW_ORDERS_INFO_LIST_ACTION:
				ShowOrdersInfoFragment showOrdersInfoFragment = new ShowOrdersInfoFragment();
				fragmentTransaction.add(R.id.fragment, showOrdersInfoFragment, SHOW_ORDERS_INFO_LIST_ACTION);
				fragmentTransaction.commit();
				break;
			default:
				break;
		}
	}

	@DebugLog
	private void initUI() {
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
	}
}
