package com.skplanet.prototype.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skplanet.prototype.R;
import com.skplanet.prototype.presenters.CustomerPresenter;
import com.skplanet.prototype.views.CustomerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Created by 1001955 on 3/24/16.
 */
public class CustomerActivity extends Activity implements View.OnClickListener, CustomerView {

    @Bind(R.id.first_name_edittext)
    public EditText mFirstNameEditText;

    @Bind(R.id.last_name_edittext)
    public EditText mLastNameEditText;

    @Bind(R.id.id_edittext)
    public EditText mIdEditText;

    @Bind(R.id.saveButton)
    public Button mSaveButton;

    @Bind(R.id.loadButton)
    public Button mLoadButton;

    private CustomerPresenter mCustomerPresenter;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer);
        ButterKnife.bind(this);

        mCustomerPresenter = new CustomerPresenter(this);
        mSaveButton.setOnClickListener(this);
        mLoadButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                mCustomerPresenter.saveCustomer(mFirstNameEditText.getText().toString(),
                        mLastNameEditText.getText().toString());
                break;
            case R.id.loadButton:
                mCustomerPresenter.loadCustomer(Integer.parseInt(mIdEditText.getText().toString()));
                break;
        }
    }

    @DebugLog
    @Override
    public void setLastName(String lastName) {

    }

    @DebugLog
    @Override
    public void setFirstName(String firstName) {

    }

    @DebugLog
    @Override
    public void setId(int id) {

    }
}
