package com.skplanet.prototype.tmap;

import android.content.Context;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import hugo.weaving.DebugLog;

/**
 * Created by 1001955 on 3/29/16.
 */
public class TmapModel implements ITmapModel {

	private TMapView mTmapView;
	private Context context;

	public TmapModel(Context context) {
		this.context = context;
		initTmapView(context);
	}

	/**
	 * T map 지도 사용 시
	 */
	@DebugLog
	private void initTmapView(Context context) {

		mTmapView = new TMapView(context);
		// 라이브러리 사용을 위해 등록된 키를 설정한다.
		mTmapView.setSKPMapApiKey(TmapUtils.APP_KEY);

		// 언어를 선택하고, 미 설정 시 사용자의 기본언어로 설정합니다. 기본언어는 한국어입니다.
		// tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
		// 지도 축척 레벨을 설정합니다. 지도레벨은 7~19 레벨까지 설정이 가능합니다.
		mTmapView.setZoomLevel(13);
		// 지도 타입을 선택합니다: TMapView.MAPTYPE_STANDARD | TMapView.MAPTYPE_TRAFFIC
		mTmapView.setMapType(TMapView.MAPTYPE_STANDARD);
		// 단말의 방향에 따라 움직이는 나침반모드로 설정합니다.
		// tmapview.setCompassMode(true);
		// 화면중심을 단말의 현재위치로 이동시켜주는 트래킹 모드로 설정합니다.
		// tmapview.setTrackingMode(false);
		// 지도의 중심좌표를 이동합니다
		// tmapview.setCenterPoint(126.985022, 37.566474);
		// BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_menu_send);
		// Bitmap bitmap = drawable.getBitmap();
		// 현재위치로 표시될 아이콘을 설정합니다.
		// tmapview.setIcon(bitmap);
		// 현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.
		// mMapView.setIconVisibility(true);

	}

	@DebugLog
	@Override
	public TMapView setTmapPoint(TMapPoint point) {
		initTmapView(context);
		mTmapView.setCenterPoint(point.getLongitude(), point.getLatitude());
		setMarkerCircle(context, mTmapView, point.getLongitude(), point.getLatitude());

		return mTmapView;
	}

	@Override
	public TMapView setTmapPolyLine(TMapPolyLine tMapPolyLine, TMapPoint centerPoint) {
		initTmapView(context);
		mTmapView.setCenterPoint(centerPoint.getLongitude(), centerPoint.getLatitude());
		mTmapView.addTMapPath(tMapPolyLine);
		return mTmapView;
	}

	@DebugLog
	@Override
	public void calculateDistance(TMapPoint start, TMapPoint end, TMapData.FindPathDataListenerCallback findPathDataListenerCallback,
								  TMapData.FindTimeMachineCarPathListenerCallback findTimeMachineCarPathListenerCallback)
			throws IOException, ParserConfigurationException, SAXException {
		TMapData tMapData = new TMapData();

		// 출발, 목적지 값으로 경로탐색을 요청합니다.
		tMapData.findPathData(start, end, findPathDataListenerCallback);

		// 출발 목적지 값으로 소요 시간, 거리를 요청합니다.
		HashMap<String, String> pathInfo = new HashMap<>();
		pathInfo.put("rStName", "상차지");
		pathInfo.put("rStlat", String.valueOf(start.getLatitude()));
		pathInfo.put("rStlon", String.valueOf(start.getLongitude()));
		pathInfo.put("rGoName", "하차지");
		pathInfo.put("rGolat", String.valueOf(end.getLatitude()));
		pathInfo.put("rGolon", String.valueOf(end.getLongitude()));
		pathInfo.put("type", "arrival");
		Date currentTime = new Date();
		tMapData.findTimeMachineCarPath(pathInfo, currentTime, null, findTimeMachineCarPathListenerCallback);
	}

	/**
	 * circle 마커표시
	 *
	 * @param longitude
	 *            경도
	 * @param latitude
	 *            위도
	 */
	@DebugLog
	protected void setMarkerCircle(Context context, TMapView tMapView, double longitude, double latitude) {
		TMapMarkerItem circleItem = tMapView.getMarkerItemFromID("CircleMarker");
		if (null == circleItem)
			circleItem = new TMapMarkerItem();

		circleItem.longitude = longitude;
		circleItem.latitude = latitude;
		circleItem.setVisible(circleItem.VISIBLE);
		circleItem.setCanShowCallout(false);

		tMapView.addMarkerItem("CircleMarker", circleItem);
	}
}
