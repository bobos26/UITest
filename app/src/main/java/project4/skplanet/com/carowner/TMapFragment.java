package project4.skplanet.com.carowner;

/**
 * Created by a1000990 on 16. 3. 15..
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapInfo;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import hugo.weaving.DebugLog;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class TMapFragment extends Fragment {
    public static final String FRAGMENT_TAG = "tmapfragment_tag";
    public static final String ARG_PLANET_NUMBER = "planet_number";

    private static final String TAG_DISTANCE = "tmap:totalDistance";
    private static final String TAG_TIME = "tmap:totalTime";
    TMapView tmapview;

    public TMapFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tmap_fragment, container, false);
        int i = getArguments().getInt(ARG_PLANET_NUMBER);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        tmapview = (TMapView) getActivity().findViewById(R.id.tmapview);
        tmapview.setSKPMapApiKey("e4bfc3be-3f9a-3bd5-98c4-4043d8b214f4");
        tmapview.setSKPMapBizappId(null);
        tmapview.setLanguage(tmapview.LANGUAGE_KOREAN);
        tmapview.setIconVisibility(true);
        tmapview.setZoomLevel(10);
        tmapview.setMapType(tmapview.MAPTYPE_STANDARD);
//        tmapview.setCompassMode(true);
        tmapview.setTrackingMode(true);
    }

    @DebugLog
    public void drawLine(TMapPoint targetPoint) {

        final TMapPoint mCurrentPoint = randomTMapPoint();
        ArrayList<TMapPoint> tmapPoints = new ArrayList<>();
        tmapPoints.add(mCurrentPoint);
        tmapPoints.add(targetPoint);
        final TMapInfo tmapInfo = tmapview.getDisplayTMapInfo(tmapPoints);

        new TMapData().findPathData(mCurrentPoint, targetPoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyline) {
                tmapview.addTMapPath(polyline);
            }
        });
        new TMapData().findPathDataAllType(TMapData.TMapPathType.CAR_PATH, mCurrentPoint, targetPoint,
                new TMapData.FindPathDataAllListenerCallback() {
                    @DebugLog
                    @Override
                    public void onFindPathDataAll(final Document doc) {
                        final int distance = getNodeDistance(doc, TAG_DISTANCE);
                        final int sec = getNodeDistance(doc, TAG_TIME);
                        final TMapPoint centerPoint = tmapInfo.getTMapPoint();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tmapview.setZoomLevel(tmapInfo.getTMapZoomLevel());
                                tmapview.setCenterPoint(centerPoint.getLongitude(), centerPoint.getLatitude(), true);
//                                    distanceText.setText("이동거리: " + distance / 1000 + " KM");
//                                    timeText.setText("이동시간: " + DateUtils.formatElapsedTime(sec));
                            }
                        });
                    }
                }
        );
    }

    private int getNodeDistance(Document doc, String tag) {
        int time = 0;

        if (doc == null)
            return time;

        try {
            NodeList totalTime = doc.getElementsByTagName(tag);
            if (totalTime == null || totalTime.getLength() == 0)
                return time;

            Element element = (Element) totalTime.item(0);
            if (element == null)
                return time;

            if (element.getFirstChild() != null
                    && !TextUtils.isEmpty(element.getFirstChild().getNodeValue())) {
                time = Integer.parseInt(element.getFirstChild().getNodeValue());
            }
        } catch (NumberFormatException e) {
//            if (DEBUG) Qlog.e(TAG, e);
        } catch (DOMException e) {
//            if (DEBUG) Qlog.e(TAG, e);
        }

        return time;
    }

    public TMapPoint randomTMapPoint() {
        double latitude = Math.random() * (37.575113 - 37.483086) + 37.483086;
        double longitude = Math.random() * (127.027359 - 126.878357) + 126.878357;

        latitude = Math.min(37.575113, latitude);
        latitude = Math.max(37.483086, latitude);

        longitude = Math.min(127.027359, longitude);
        longitude = Math.max(126.878357, longitude);

        TMapPoint point = new TMapPoint(latitude, longitude);

        return point;
    }
}
