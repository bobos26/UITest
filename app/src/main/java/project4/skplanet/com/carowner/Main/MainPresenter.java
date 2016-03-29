package project4.skplanet.com.carowner.Main;

import project4.skplanet.com.carowner.model.BLERegion;
import project4.skplanet.com.carowner.model.IBLEModel;

/**
 * Created by a1000990 on 16. 3. 28..
 */
public class MainPresenter {
    IMainView mMainView;
    IBLEModel mBLEModel;

    public MainPresenter(IMainView mainView, IBLEModel bleRegion) {
        this.mMainView = mainView;
        this.mBLEModel = bleRegion;
    }

    public void addBLERegion(int major, int minor, String mid) {
        BLERegion bleRegion = new BLERegion(major, minor, mid, null);
        bleRegion.save();
        mMainView.dataReloaded();
    }

    public interface IMainView {
        void dataReloaded();
    }
}
