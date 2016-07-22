package com.skplanet.trunk.carowner.MainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.skplanet.trunk.carowner.R;
import com.skplanet.trunk.carowner.SecondPage.SecondActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    // Square Button
	@Bind(R.id.showGoodsInfoButton)
	public Button showCarInfoButton;
	@Bind(R.id.showOrderInfoButton)
	public Button showOrderInfoButton;
	@Bind(R.id.registerGoodsButton)
	public Button registerCarButton;
	@Bind(R.id.registerByVoiceButton)
	public Button registerByVoiceButton;
	@Bind(R.id.registerEmptyCarButton)
	public Button registerEmptyCarButton;
	@Bind(R.id.goHomePageButton)
	public Button goHomePageButton;

    // Circle Button
	@Bind(R.id.showNoticeButton)
	public Button showNoticeButton;
	@Bind(R.id.settingButton)
	public Button settingButton;
	@Bind(R.id.showFeeInfoButton)
	public Button showFeeInfoButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		ButterKnife.bind(this, view);

		showCarInfoButton.setOnClickListener(this);
		showOrderInfoButton.setOnClickListener(this);
		registerCarButton.setOnClickListener(this);
		registerByVoiceButton.setOnClickListener(this);
		registerEmptyCarButton.setOnClickListener(this);
		goHomePageButton.setOnClickListener(this);

		showNoticeButton.setOnClickListener(this);
		settingButton.setOnClickListener(this);
		showFeeInfoButton.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.showGoodsInfoButton:
			showNewPage(SecondActivity.SHOW_GOODS_INFO_LIST_ACTION);
			break;
		case R.id.showOrderInfoButton:
			showNewPage(SecondActivity.SHOW_ORDERS_INFO_LIST_ACTION);
			break;
		case R.id.registerGoodsButton:
			showNewPage(SecondActivity.SHOW_REGISTER_GOODS_ACTION);
			break;
		case R.id.registerByVoiceButton:
			showNotPrepareMessage(v);
			break;
		case R.id.registerEmptyCarButton:
			showNotPrepareMessage(v);
			break;
		case R.id.goHomePageButton:
			showNotPrepareMessage(v);
			break;
		case R.id.showNoticeButton:
			showNotPrepareMessage(v);
			break;
		case R.id.settingButton:
			showNotPrepareMessage(v);
			break;
		case R.id.showFeeInfoButton:
			showNotPrepareMessage(v);
			break;
		}
	}

	public void showNewPage(String action) {
		Intent intent = new Intent(getContext(), SecondActivity.class);
		intent.setAction(action);
		startActivity(intent);
	}

	public void showNotPrepareMessage(View view) {
		Snackbar.make(view, "Not prepare yet", Snackbar.LENGTH_SHORT).show();
	}
}
