package com.skplanet.prototype.models;

/**
 * Created by 1001955 on 3/24/16.
 */
public interface CustomerModel {

    void save(String firstName, String lastName);

    boolean load(int id);

    int getId();

    String getFirstName();

    String getLastName();
}
