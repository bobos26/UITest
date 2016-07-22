package com.skplanet.trunk.carowner.SecondPage;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.skplanet.trunk.carowner.R;

import java.security.InvalidParameterException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterGoodsFragment extends Fragment implements View.OnClickListener {

	// 상차지
	@Bind(R.id.startingPointWideAreasSpinner)
	Spinner startingPointWideAreasSpinner;
	@Bind(R.id.startingPointCitiesAndTownsSpinner)
	Spinner startingPointCitiesAndTownsSpinner;
	@Bind(R.id.startingPointDetailAddressEditText)
	EditText startingPointDetailAddressEditText;
	@Bind(R.id.wayToLoadSpinner)
	Spinner wayToLoadSpinner;

	// 하차지
	@Bind(R.id.destinationPointAddressEditText)
	EditText destinationPointAddressEditText;
	@Bind(R.id.wayToUnloadSpinnerDepth1Spinner)
	Spinner wayToUnloadSpinnerDepth1Spinner;
	@Bind(R.id.wayToUnloadSpinnerDepth2Spinner)
	Spinner wayToUnloadSpinnerDepth2Spinner;

	@Bind(R.id.capacitySpinner)
	Spinner capacitySpinner;
	@Bind(R.id.carTypeSpinner)
	Spinner carTypeSpinner;
	@Bind(R.id.feeEditText)
	EditText feeEditText;
	@Bind(R.id.requestCarCountSpinner)
	Spinner requestCarCountSpinner;
	@Bind(R.id.goodsDetailEditText)
	EditText goodsDetailEditText;

	@Bind(R.id.sendSelectedItemsToServerButton)
	Button sendSelectedItemsToServerButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register_goods, container, false);
		ButterKnife.bind(this, view);

		initView();
		return view;
	}

	private void initView() {
		final String[] optionWideAreas = getResources().getStringArray(R.array.wide_area);
		ArrayAdapter<String> wideAreasAdapter = new ArrayAdapter<>(getContext(),
				android.R.layout.simple_spinner_dropdown_item, optionWideAreas);
		startingPointWideAreasSpinner.setAdapter(wideAreasAdapter);
		startingPointWideAreasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selectedItem = optionWideAreas[position];
				setOptionForCitiesSpinner(selectedItem);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		ArrayAdapter<String> wayToLoadAdapter = new ArrayAdapter<>(getContext(),
				android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.way_to_load));
		wayToLoadSpinner.setAdapter(wayToLoadAdapter);

		ArrayAdapter<String> wayToUnloadAdapter1 = new ArrayAdapter<>(getContext(),
				android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.way_to_unload_depth_1));
		wayToUnloadSpinnerDepth1Spinner.setAdapter(wayToUnloadAdapter1);

		ArrayAdapter<String> wayToUnloadAdapter2 = new ArrayAdapter<>(getContext(),
				android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.way_to_unload_depth_2));
		wayToUnloadSpinnerDepth2Spinner.setAdapter(wayToUnloadAdapter2);

		ArrayAdapter<String> numberAdapter = new ArrayAdapter<>(getContext(),
				android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.number_1_to_5));
		capacitySpinner.setAdapter(numberAdapter);
		requestCarCountSpinner.setAdapter(numberAdapter);

		ArrayAdapter<String> carTypeAdapter = new ArrayAdapter<>(getContext(),
				android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.car_type));
		carTypeSpinner.setAdapter(carTypeAdapter);

		sendSelectedItemsToServerButton.setOnClickListener(this);
	}

	private void setOptionForCitiesSpinner(String selectedItem) {
		String[] optionItems = new String[0];
		switch (selectedItem) {
		case "서울":
			optionItems = getResources().getStringArray(R.array.cities_and_towns_seoul);
			break;
		case "경기도":
			optionItems = getResources().getStringArray(R.array.cities_and_towns_kyungi);
			break;
		default:
			break;
		}

		final String[] selectedItems = optionItems;
		ArrayAdapter<String> optionCityAdapter = new ArrayAdapter<>(getContext(),
				android.R.layout.simple_spinner_dropdown_item, selectedItems);
		startingPointCitiesAndTownsSpinner.setAdapter(optionCityAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendSelectedItemsToServerButton:
			saveSelectedItem();
			break;
		default:
			break;
		}
	}

	private void saveSelectedItem() {

		String FIELD_STARTING_WIDE_AREA = (String) startingPointWideAreasSpinner.getSelectedItem();
		String FIELD_STARTING_CITY = (String) startingPointCitiesAndTownsSpinner.getSelectedItem();
		String FIELD_STARTING_DETAIL_ADDRESS = startingPointDetailAddressEditText.getText().toString();
		String FIELD_WAY_TO_LOAD = (String) wayToLoadSpinner.getSelectedItem();

		String FIELD_DESTINATION_ADDRESS = destinationPointAddressEditText.getText().toString();
		String FIELD_WAY_TO_UNLOAD = new StringBuffer(wayToUnloadSpinnerDepth1Spinner.getSelectedItem().toString())
				.append("-").append(wayToUnloadSpinnerDepth2Spinner.getSelectedItem().toString()).toString();

		String FIELD_CAR_CAPACITY = (String) capacitySpinner.getSelectedItem();
		String FIELD_CAR_TYPE = (String) carTypeSpinner.getSelectedItem();
		String FIELD_FEE = feeEditText.getText().toString();
		String FIELD_CAR_COUNT = (String) requestCarCountSpinner.getSelectedItem();
		String FIELD_GOODS_DETAILS = goodsDetailEditText.getText().toString();

		try {
			validationCheck(FIELD_STARTING_WIDE_AREA);
			validationCheck(FIELD_STARTING_CITY);
			validationCheck(FIELD_STARTING_DETAIL_ADDRESS);
			validationCheck(FIELD_WAY_TO_LOAD);
			validationCheck(FIELD_DESTINATION_ADDRESS);
			validationCheck(FIELD_WAY_TO_UNLOAD);
			validationCheck(FIELD_CAR_CAPACITY);
			validationCheck(FIELD_CAR_TYPE);
			validationCheck(FIELD_FEE);
			validationCheck(FIELD_CAR_COUNT);
			validationCheck(FIELD_GOODS_DETAILS);

			// Preference write using ActiveAndroid
			RegisterGoodsData registerGoodsData = new RegisterGoodsData();
			registerGoodsData.startingWideArea = FIELD_STARTING_WIDE_AREA;
			registerGoodsData.startingCity = FIELD_STARTING_CITY;
			registerGoodsData.startingDetailAddress = FIELD_STARTING_DETAIL_ADDRESS;
			registerGoodsData.wayToLoad = FIELD_WAY_TO_LOAD;
			registerGoodsData.destinationAddress = FIELD_DESTINATION_ADDRESS;
			registerGoodsData.wayToUnload = FIELD_WAY_TO_UNLOAD;
			registerGoodsData.carCapacity = FIELD_CAR_CAPACITY;
			registerGoodsData.carType = FIELD_CAR_TYPE;
			registerGoodsData.fee = FIELD_FEE;
			registerGoodsData.carCount = FIELD_CAR_COUNT;
			registerGoodsData.goodsDetails = FIELD_GOODS_DETAILS;
			registerGoodsData.registerTime = new Date().toString();
			registerGoodsData.save();

			Snackbar.make(getView(), "등록이 완료되었습니다.", Snackbar.LENGTH_LONG).show();

		} catch (InvalidParameterException e) {
			Snackbar.make(getView(), "빠진 항목 없이 모두 작성해주세요.", Snackbar.LENGTH_INDEFINITE).show();
		} catch (Throwable t) {
			Snackbar.make(getView(), "에러가 발생했습니다. 개발자에게 문의해주세요.", Snackbar.LENGTH_INDEFINITE).show();
		}
	}

	private void validationCheck(String selectedItem) throws InvalidParameterException {
		if (selectedItem.isEmpty()) {
			throw new InvalidParameterException();
		}
	}
}
