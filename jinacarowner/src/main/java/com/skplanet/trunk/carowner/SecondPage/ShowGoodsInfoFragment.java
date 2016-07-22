package com.skplanet.trunk.carowner.SecondPage;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.skplanet.trunk.carowner.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowGoodsInfoFragment extends Fragment {

	@Bind(R.id.listview)
	ListView listView;
	ListViewAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_goods_info, container, false);
		ButterKnife.bind(this, view);

		initView();
		return view;
	}

	private void initView() {
		adapter = new ListViewAdapter();
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Snackbar.make(v, "onItemClick: " + id, Snackbar.LENGTH_SHORT).show();
			}
		});

		adapter.addAllItem(RegisterGoodsData.selectAll());
	}

	class ListViewAdapter extends BaseAdapter {

		private List<RegisterGoodsData> listViewItemList = new ArrayList<>();

		public ListViewAdapter() {}

		@Override
		public int getCount() {
			return listViewItemList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Context context = parent.getContext();

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.listview_item, parent, false);
			}

			TextView startingPointTextView = (TextView) convertView.findViewById(R.id.startingPointTextView);
			TextView registeredTimeTextView = (TextView) convertView.findViewById(R.id.registeredTimeTextView);

			TextView destinationPointTextView = (TextView) convertView.findViewById(R.id.destinationPointTextView);
			TextView feeTextView = (TextView) convertView.findViewById(R.id.feeTextView);

			TextView capacityTextView = (TextView) convertView.findViewById(R.id.capacityTextView);
			TextView carTypeTextView = (TextView) convertView.findViewById(R.id.carTypeTextView);
			TextView goodsDetailTextView = (TextView) convertView.findViewById(R.id.goodsDetailTextView);

			RegisterGoodsData listViewItem = listViewItemList.get(position);

			startingPointTextView.setText(
					new StringBuffer(listViewItem.startingWideArea).append(" ").append(listViewItem.startingCity));
			registeredTimeTextView.setText(listViewItem.registerTime);
			destinationPointTextView.setText(listViewItem.destinationAddress);
			feeTextView.setText(listViewItem.fee);
			capacityTextView.setText(String.format(context.getString(R.string.ton), listViewItem.carCapacity));
			carTypeTextView.setText(listViewItem.carType);
			goodsDetailTextView.setText(listViewItem.goodsDetails);

			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return listViewItemList.get(position);
		}

		public void addItem(RegisterGoodsData item) {
			listViewItemList.add(item);
		}

		public void addAllItem(List<RegisterGoodsData> listViewItemList) {
			this.listViewItemList = listViewItemList;
		}
	}

}
