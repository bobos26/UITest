package com.skplanet.trunk.carowner.common;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.widget.EditText;

/**
 * Created by a1000990 on 16. 4. 5..
 */
public class PhoneNumberTextWatcher extends PhoneNumberFormattingTextWatcher {
    EditText mEditText;
    String strAmount;

    public PhoneNumberTextWatcher(EditText editText) {
        mEditText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        super.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        // TODO 한국식으로 010-4086-0843에 맞게 작동하도록 customizing이 필요하다.
    }

    @Override
    public void afterTextChanged(Editable s) {
        super.afterTextChanged(s);
    }
}
