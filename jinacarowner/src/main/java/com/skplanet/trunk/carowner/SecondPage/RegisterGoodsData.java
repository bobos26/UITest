package com.skplanet.trunk.carowner.SecondPage;

import android.database.sqlite.SQLiteException;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "RegisterGoodsData")
public class RegisterGoodsData extends Model {

	public static final String FIELD_STARTING_WIDE_AREA = "starting_wide_area";
	public static final String FIELD_STARTING_CITY = "starting_city";
	public static final String FIELD_STARTING_DETAIL_ADDRESS = "starting_detail_address";
	public static final String FIELD_WAY_TO_LOAD = "way_to_load";

	public static final String FIELD_DESTINATION_ADDRESS = "destination_address";
	public static final String FIELD_WAY_TO_UNLOAD = "way_to_unload";

	public static final String FIELD_CAR_CAPACITY = "car_capacity";
	public static final String FIELD_CAR_TYPE = "car_type";
	public static final String FIELD_FEE = "fee";
	public static final String FIELD_CAR_COUNT = "car_count";
	public static final String FIELD_GOODS_DETAILS = "goods_details";
	public static final String FIELD_REGISTER_TIME = "register_time";

	@Column(name = FIELD_STARTING_WIDE_AREA)
	public String startingWideArea;

	@Column(name = FIELD_STARTING_CITY)
	public String startingCity;

	@Column(name = FIELD_STARTING_DETAIL_ADDRESS)
	public String startingDetailAddress;

	@Column(name = FIELD_WAY_TO_LOAD)
	public String wayToLoad;

	@Column(name = FIELD_DESTINATION_ADDRESS)
	public String destinationAddress;

	@Column(name = FIELD_WAY_TO_UNLOAD)
	public String wayToUnload;

	@Column(name = FIELD_CAR_CAPACITY)
	public String carCapacity;

	@Column(name = FIELD_CAR_TYPE)
	public String carType;

	@Column(name = FIELD_FEE)
	public String fee;

	@Column(name = FIELD_CAR_COUNT)
	public String carCount;

	@Column(name = FIELD_GOODS_DETAILS)
	public String goodsDetails;

	@Column(name = FIELD_REGISTER_TIME)
	public String registerTime;

	public RegisterGoodsData() {
		super();
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("startingWideArea: " + startingWideArea);
		sb.append(" startingCity: " + startingCity);
		sb.append(" startingDetailAddress: " + startingDetailAddress);
		sb.append(" wayToLoad: " + wayToLoad);
		sb.append(" destinationAddress: " + destinationAddress);
		sb.append(" wayToUnload: " + wayToUnload);
		sb.append(" carCapacity: " + carCapacity);
		sb.append(" carType: " + carType);
		sb.append(" fee: " + fee);
		sb.append(" carCount: " + carCount);
		sb.append(" goodsDetails: " + goodsDetails);
		sb.append(" registerTime: " + registerTime);

		return sb.toString();
	}

	public static List<RegisterGoodsData> selectAll() {
		try {
			return new Select()
					.from(RegisterGoodsData.class)
					.execute();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return null;
	}

}
