package com.skplanet.prototype.event;

import com.skplanet.prototype.serverAPIs.DataModel.ImageData;

import java.util.ArrayList;

/**
 * 이벤트 인터페이스
 */
public interface NetUtilsListener {
    /**
     * 성공시엔 아이템 목록을 반환
     * @param list
     */
    void onResult(ArrayList<ImageData> list);

    /**
     * 실패시엔 HTTP 응답코드를 반환
     * @param code 응답코드
     */
    void onFault(int code);
}
