package com.skplanet.prototype.tmap;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import hugo.weaving.DebugLog;

/**
 * Created by 1001955 on 3/29/16.
 */
public class TmapPresenter {

	private static final String TMAP_TAG_NAME_TOTAL_DISTANCE = "tmap:totalDistance";
	private static final String TMAP_TAG_NAME_TOTAL_TIME = "tmap:totalTime";

	private ITmapView mTmapView;
	private TmapModel mTmapModel;
	private Context context;

	@DebugLog
	public TmapPresenter(Context context, ITmapView view) {
		this.context = context;
		mTmapView = view;
		mTmapModel = new TmapModel(context);
	}

	@DebugLog
	public void setMap(double longitude, double latitude) {
		TMapPoint point = new TMapPoint(latitude, longitude);
		mTmapView.setMap(mTmapModel.setTmapPoint(point));
	}

	@DebugLog
	public void getRoute(double startLatitude, double startLongitude, double endLatitude, double endLongitude)
			throws ParserConfigurationException, SAXException, IOException {

		TMapPoint start = new TMapPoint(startLatitude, startLongitude);
		TMapPoint end = new TMapPoint(endLatitude, endLongitude);
		final TMapPoint center = start;

		TMapData.FindPathDataListenerCallback findPathDataListenerCallback = new TMapData.FindPathDataListenerCallback() {

			@Override
			public void onFindPathData(final TMapPolyLine tMapPolyLine) {
				new Handler(context.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						mTmapView.setMap(mTmapModel.setTmapPolyLine(tMapPolyLine, center));
					}
				});
			}
		};

		TMapData.FindTimeMachineCarPathListenerCallback findTimeMachineCarPathListenerCallback = new TMapData.FindTimeMachineCarPathListenerCallback() {
			@Override
			public void onFindTimeMachineCarPath(final Document document) {

				new Handler(context.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						mTmapView.setDistance(
								"Distance (m): " + getValueFromDocument(TMAP_TAG_NAME_TOTAL_DISTANCE, document));
						mTmapView.setTravelTime(
								"Travel time (sec): " + getValueFromDocument(TMAP_TAG_NAME_TOTAL_TIME, document));
					}
				});
			}
		};

		mTmapModel.calculateDistance(start, end, findPathDataListenerCallback, findTimeMachineCarPathListenerCallback);
	}

	private String getValueFromDocument(String tagName, Document doc) {
		String result = "";

		if (doc == null)
			return result;

		NodeList totalResult = doc.getElementsByTagName(tagName);
		if (totalResult == null || totalResult.getLength() == 0)
			return result;

		Element element = (Element) totalResult.item(0);
		if (element == null)
			return result;

		if (element.getFirstChild() != null && !TextUtils.isEmpty(element.getFirstChild().getNodeValue())) {
			result = element.getFirstChild().getNodeValue();
		}

		return result;
	}

}
