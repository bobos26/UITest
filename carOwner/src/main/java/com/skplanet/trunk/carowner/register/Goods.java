package com.skplanet.trunk.carowner.register;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by a1000990 on 16. 3. 30..
 */
@Table(name="goods")
public class Goods extends Model {

    @Column(name="wideLiftArea") // TODO default value가 필요한가?
    public String wideLiftArea; // 광역

    @Column(name="localLiftArea")
    public String localLiftArea; // 시군구

    @Column(name="liftArea")
    public String liftArea; // 상차지

    @Column(name="liftMethod")
    public String liftMethod; // 상차방법

    @Column(name="landArea")
    public String landArea; // 하차지

    @Column(name="landMethod")
    public String landMethod; // 하차방법

    @Column(name="ton")
    public String ton; // 톤

    @Column(name="carType")
    public String carType; // 차종

    @Column(name="fee")
    public int fee; // 요금

    @Column(name="carCount")
    public int carCount; // 차 대수

    @Column(name="goodsRegisterPhone")
    public String goodsRegisterPhone; // 원천화주

    @Column(name="goodsOwnerPhone")
    public String goodsOwnerPhone; // 하차지전화

    public Goods() {
        super();
    }

    public Goods(String wideLiftArea, String localLiftArea, String liftArea, String liftMethod, String landArea, String landMethod,
                 String ton, String carType, int fee, int carCount, String goodsRegisterPhone, String goodsOwnerPhone) {
        super();
        this.wideLiftArea = wideLiftArea;
        this.localLiftArea = localLiftArea;
        this.liftArea = liftArea;
        this.liftMethod = liftMethod;
        this.landArea = landArea;
        this.landMethod = landMethod;
        this.ton = ton;
        this.carType = carType;
        this.fee = fee;
        this.carCount = carCount;
        this.goodsRegisterPhone = goodsRegisterPhone;
        this.goodsOwnerPhone = goodsOwnerPhone;

    }

}
