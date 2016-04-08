package com.skplanet.trunk.carowner.common;

public abstract class AbstractPresenter {
    protected MainThreadImpl mMainThread;

    public AbstractPresenter(MainThreadImpl mainThread) {
        mMainThread = mainThread;
    }
}
