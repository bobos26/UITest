package com.skplanet.trunk.carowner.common;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by a1000990 on 16. 4. 5..
 */
public class CurrencyTextWatcher implements TextWatcher{
    EditText mEditText;
    String strAmount;

    public CurrencyTextWatcher(EditText editText) {
        mEditText = editText;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(strAmount)) { // StackOverflow 방지
            strAmount = makeStringComma(s.toString().replace(",", ""));
            mEditText.setText(strAmount);
            Editable e = mEditText.getText();
            Selection.setSelection(e, strAmount.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    protected String makeStringComma(String str) {    // 천단위 콤마 처리
        if (str.length() == 0)
            return "";
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }
}
