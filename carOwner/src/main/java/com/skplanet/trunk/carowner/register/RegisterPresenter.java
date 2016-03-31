package com.skplanet.trunk.carowner.register;

/**
 * Created by a1000990 on 16. 3. 30..
 */
public class RegisterPresenter {
    private IRegisterView mView;
    private IGoodsModel mModel;

    public RegisterPresenter(IRegisterView iRegisterView, IGoodsModel iGoodsModel) {
        mView = iRegisterView;
        mModel = iGoodsModel;
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

    public interface IRegisterView {
        void updated();
    }
}
