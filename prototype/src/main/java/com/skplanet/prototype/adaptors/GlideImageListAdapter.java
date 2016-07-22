package com.skplanet.prototype.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skplanet.prototype.R;
import com.skplanet.prototype.serverAPIs.DataModel.ImageData;

import java.util.List;

/**
 * Created by 1001955 on 3/8/16.
 */
public class GlideImageListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<ImageData> items;
	private ImageView myImageView;

	public GlideImageListAdapter(Context context, List<ImageData> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int location) {
		return items.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_glide_image_row, null);
		}

		if (myImageView == null) {
			myImageView = (ImageView) convertView.findViewById(R.id.image_view);
		}

		TextView targetId = (TextView) convertView.findViewById(R.id.target_id);
		TextView major = (TextView) convertView.findViewById(R.id.major);
		TextView minor = (TextView) convertView.findViewById(R.id.minor);

		// getting movie data for the row
		ImageData imageData = items.get(position);
		targetId.setText(imageData.getBuid());
		major.setText("Major: " + String.valueOf(imageData.getMajor()));
		minor.setText("Minor: " + String.valueOf(imageData.getMinor()));

		// thumbnail image
		Glide.with(context).load(imageData.getThumbnailUrl())
				// .crossFade()
				// .override(200, 200)
				// .thumbnail(0.1f)
				// .placeholder(R.drawable.ic_menu_gallery)
				// .error(R.drawable.ic_menu_share)
				.into(myImageView);

		return convertView;
	}

}
