package com.skplanet.prototype.presenters;

import com.skplanet.prototype.models.CustomerModelImpl;
import com.skplanet.prototype.models.CustomerModel;
import com.skplanet.prototype.views.CustomerView;

import hugo.weaving.DebugLog;

/**
 * Created by 1001955 on 3/24/16.
 * MVP- ㅖ
 */
public class CustomerPresenter {

	private CustomerView mCustomerView;
	private CustomerModel mCustomerModel;

    @DebugLog
	public CustomerPresenter(CustomerView view) {
		mCustomerView = view;
        mCustomerModel = new CustomerModelImpl();
	}

    @DebugLog
    public void saveCustomer(String firstName, String lastName) {
        mCustomerModel.save(firstName, lastName);
    }

    @DebugLog
    public void loadCustomer(int id) {
        if (mCustomerModel.load(id)) {
            mCustomerView.setId(mCustomerModel.getId());
            mCustomerView.setFirstName(mCustomerModel.getFirstName());
            mCustomerView.setLastName(mCustomerModel.getLastName());
        }
    }
}
