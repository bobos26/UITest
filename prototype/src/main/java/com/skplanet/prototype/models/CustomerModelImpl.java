package com.skplanet.prototype.models;

import hugo.weaving.DebugLog;

/**
 * Created by 1001955 on 3/25/16.
 */
public class CustomerModelImpl implements CustomerModel {

    @DebugLog
    @Override
    public void save(String firstName, String lastName) {}

    @DebugLog
    @Override
    public boolean load(int id) {
        return true;
    }

    @DebugLog
    @Override
    public int getId() {
        return 0;
    }

    @DebugLog
    @Override
    public String getFirstName() {
        return null;
    }

    @DebugLog
    @Override
    public String getLastName() {
        return null;
    }
}