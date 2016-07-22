package com.skplanet.prototype.tmap;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by 1001955 on 3/29/16.
 */
public interface ITmapModel {

	TMapView setTmapPoint(TMapPoint point);

	void calculateDistance(TMapPoint start, TMapPoint end, TMapData.FindPathDataListenerCallback findPathDataListenerCallback,
						   TMapData.FindTimeMachineCarPathListenerCallback findTimeMachineCarPathListenerCallback)
			throws IOException, ParserConfigurationException, SAXException;

	TMapView setTmapPolyLine(TMapPolyLine tMapPolyLine, TMapPoint centerPoint);
}
