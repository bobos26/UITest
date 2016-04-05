package com.skplanet.trunk.carowner.goodsRegister;

import com.skplanet.trunk.carowner.common.MainThreadImpl;
import com.skplanet.trunk.carowner.model.Goods;
import com.skplanet.trunk.carowner.model.GoodsModel;
import com.skplanet.trunk.carowner.model.IGoodsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by a1000990 on 16. 3. 30..
 */
public class RegisterPresenter implements GoodsModel.ICallback {
    // TODO 지역 시/구 정보를 서버에서 받아오자
    String SIGU = "{\"서울시\":[\"강동구\",\"송파구\",\"강남구\"],\"부산시\":[\"강서구\",\"부산진구\",\"서구\"],\"성남시\":[\"분당구\",\"중원구\"]}";

    private IRegisterView mView;
    private IGoodsModel mModel;
    protected MainThreadImpl mMainThread;

    private JSONObject mSiDoObj = new JSONObject();

    public RegisterPresenter(MainThreadImpl mainThread, IRegisterView iRegisterView) {
        mMainThread = mainThread;

        mView = iRegisterView;
        mModel = new GoodsModel(this);
        try {
            mSiDoObj = new JSONObject(SIGU);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addGoods(String wideLiftArea, String localLiftArea, String liftArea, String liftMethod,
                         String landArea, String landMethod, String ton, String carType, int fee, int carCount, String goodsRegisterPhone, String goodsOwnerPhone) {
        // DB에 등록한다.
        Goods goods = new Goods(wideLiftArea, localLiftArea, liftArea, liftMethod,
                landArea, landMethod, ton, carType, fee, carCount, goodsRegisterPhone, goodsOwnerPhone);
        mModel.insert(goods);

        mView.updated();
        // TODO 서버 API로 화물을 등록한다.
    }

    public void addAllGoods(List<Goods> goodsList) {
        mModel.insertAll(goodsList);
        // TODO insertAll이 완료된후 insertAllCompleted()가 호출된다.
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

    public void getGu(String si) {
        String[] guArray = new String[]{};
        try {
            JSONArray doObj = mSiDoObj.getJSONArray(si);
            guArray = new String[doObj.length()];
            for(int i=0; i < doObj.length(); i++) {
                guArray[i] = doObj.getString(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String[] guArrayFinal = guArray;
        // TODO View의 UI를 업데이트 하려면, 꼭 MainThread에서 callback을 호출해야 한다.
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mView.updateGu(guArrayFinal);
            }
        });
    }

    @Override
    public void insertAllCompleted() {
        mView.updated();
    }

    @Override
    public void deleteAllCompleted() {
        mView.updated();
    }

    public interface IRegisterView {
        void updated();
        void updateGu(String[] gu);
    }
}
