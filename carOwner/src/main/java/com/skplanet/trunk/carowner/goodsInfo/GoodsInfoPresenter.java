package com.skplanet.trunk.carowner.goodsInfo;

import android.content.Context;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;

import com.activeandroid.content.ContentProvider;
import com.skplanet.trunk.carowner.common.AbstractPresenter;
import com.skplanet.trunk.carowner.common.Constants;
import com.skplanet.trunk.carowner.common.MainThreadImpl;
import com.skplanet.trunk.carowner.model.Goods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import hugo.weaving.DebugLog;

/**
 * Created by a1000990 on 16. 4. 6..
 */
public class GoodsInfoPresenter extends AbstractPresenter {

    JSONObject mSiDoObj;
    IGoodsInfoView mView;

    public GoodsInfoPresenter(MainThreadImpl mainThread, IGoodsInfoView iGoodsInfoView){
        super(mainThread);

        mView = iGoodsInfoView;
        try {
            mSiDoObj = new JSONObject(Constants.SIGU);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @DebugLog
    public CursorLoader getCursorLoader(Context context, String location, String tonType) {
        return new CursorLoader(context, ContentProvider.createUri(Goods.class, null),
                    null, // projection
                    location != null && tonType != null ? "wideLiftArea = ? AND ton = ?" : null, // selection
                    location != null && tonType != null ? new String[] {location, tonType} : null, // selectionArgs
                    "time ASC");// sortOrder
    }

    public String[] getSi() {
        String[] siArray = new String[mSiDoObj.length()];
        Iterator<String> it = mSiDoObj.keys();
        for(int i=0; it.hasNext(); i++) {
            siArray[i] = it.next();
        }
        // TODO callback을 무조건 쓸필요가 있나? return값으로 커버가 가능한다.
        return siArray;
    }

    public interface IGoodsInfoView {

    }
}
