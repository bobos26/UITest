package com.skplanet.prototype.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.skplanet.prototype.R;
import com.skplanet.prototype.serverAPIs.DataModel.ImageData;
import com.skplanet.prototype.serverAPIs.Volley.AppController;

import java.util.List;

/**
 * Created by 1001955 on 3/8/16.
 */
public class VolleyImageListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ImageData> items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public VolleyImageListAdapter(Context context, List<ImageData> items) {
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

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_volley_image_row, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView targetId = (TextView) convertView.findViewById(R.id.target_id);
        TextView major = (TextView) convertView.findViewById(R.id.major);
        TextView minor = (TextView) convertView.findViewById(R.id.minor);

        // getting movie data for the row
        ImageData imageData = items.get(position);

        // thumbnail image
        thumbNail.setImageUrl(imageData.getThumbnailUrl(), imageLoader);

        // targetId
        targetId.setText(imageData.getBuid());

        // major
        major.setText("Major: " + String.valueOf(imageData.getMajor()));

        // minor
        minor.setText("Minor: " + String.valueOf(imageData.getMinor()));

        return convertView;
    }

}