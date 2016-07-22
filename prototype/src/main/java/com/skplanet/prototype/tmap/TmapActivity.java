package com.skplanet.prototype.tmap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.skp.Tmap.TMapView;
import com.skplanet.prototype.R;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 1001955 on 3/29/16.
 */
public class TmapActivity extends Activity implements View.OnClickListener, ITmapView {

	private TmapPresenter mTmapPresenter;

	@Bind(R.id.getStartPointMapButton)
	public Button mGetStartPointMapButton;

	@Bind(R.id.getEndPointMapButton)
	public Button mGetEndPointMapButton;

	@Bind(R.id.getRouteButton)
	public Button mGetDistanceButton;

	@Bind(R.id.start_lat_edittext)
	public EditText mStartLatitude;

	@Bind(R.id.start_long_edittext)
	public EditText mStartLongitude;

	@Bind(R.id.end_lat_edittext)
	public EditText mEndLatitude;

	@Bind(R.id.end_long_edittext)
	public EditText mEndLongitude;

	@Bind(R.id.tmap_frame_layout)
	public FrameLayout tmapFrameLayout;

	@Bind(R.id.distanceTextView)
	public TextView distanceTextView;

	@Bind(R.id.travelTimeTextView)
	public TextView travelTimeTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tmap);
		ButterKnife.bind(this);

		mTmapPresenter = new TmapPresenter(getApplicationContext(), this);
		mGetStartPointMapButton.setOnClickListener(this);
		mGetDistanceButton.setOnClickListener(this);
		mGetEndPointMapButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.getStartPointMapButton:
				mTmapPresenter.setMap(new Double(String.valueOf(mStartLongitude.getText())),
						new Double(String.valueOf(mStartLatitude.getText())));
				break;
			case R.id.getEndPointMapButton:
				mTmapPresenter.setMap(new Double(String.valueOf(mEndLongitude.getText())),
						new Double(String.valueOf(mEndLatitude.getText())));
				break;
			case R.id.getRouteButton:
				try {
					mTmapPresenter.getRoute(new Double(String.valueOf(mStartLatitude.getText())),
							new Double(String.valueOf(mStartLongitude.getText())),
							new Double(String.valueOf(mEndLatitude.getText())),
							new Double(String.valueOf(mEndLongitude.getText())));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}

	}

	@Override
	public void setMap(TMapView map) {
		tmapFrameLayout.removeAllViews();
		tmapFrameLayout.addView(map);
	}

	@Override
	public void setDistance(String distance) {
		distanceTextView.setText(distance);
	}

	@Override
	public void setTravelTime(String travelTime) {
		travelTimeTextView.setText(travelTime);
	}

	// @Override
	// public void onFindPathData(TMapPolyLine tMapPolyLine) {
	// distanceTextView.setText(String.valueOf(tMapPolyLine.getRoute()));
	// }
}
