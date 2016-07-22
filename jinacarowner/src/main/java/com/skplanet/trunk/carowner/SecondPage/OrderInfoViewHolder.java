package com.skplanet.trunk.carowner.SecondPage;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.skplanet.trunk.carowner.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 1001955 on 4/7/16.
 */
public class OrderInfoViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.startingPointTextView)
	TextView startingPointTextView;
	@Bind(R.id.registeredTimeTextView)
	TextView registeredTimeTextView;

	@Bind(R.id.destinationPointTextView)
	TextView destinationPointTextView;
	@Bind(R.id.feeTextView)
	TextView feeTextView;

	@Bind(R.id.capacityTextView)
	TextView capacityTextView;
	@Bind(R.id.carTypeTextView)
	TextView carTypeTextView;
	@Bind(R.id.goodsDetailTextView)
	TextView goodsDetailTextView;

	@Bind(R.id.cardView)
	CardView cardView;

	OrderInfoViewHolder(View convertView) {
		super(convertView);

		ButterKnife.bind(this, convertView);
	}
}
