package com.skplanet.prototype.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import com.skplanet.prototype.R;

/**
 * Created by 1001955 on 3/10/16.
 */
public class SendSmsActivity extends Activity {

    EditText smsReceiver, smsContent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_sms);

        smsReceiver = (EditText) findViewById(R.id.sms_receiver_edit_text);
        smsContent = (EditText) findViewById(R.id.sms_content_edit_text);
    }

    public void sendSmsButton(View view) {
        String smsNum = smsReceiver.getText().toString();
        String smsText = smsContent.getText().toString();

        if (smsNum.length()>0 && smsText.length()>0){
            sendSMS(view, smsNum, smsText);
        }else{
            Snackbar.make(view, "모두 입력해 주세요", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void sendSMS(final View view, String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Snackbar.make(view, "전송 완료", Snackbar.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Snackbar.make(view, "전송 실패", Snackbar.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Snackbar.make(view, "서비스 지역이 아닙니다", Snackbar.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Snackbar.make(view, "무선(Radio)가 꺼져있습니다", Snackbar.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Snackbar.make(view, "PDU Null", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Snackbar.make(view, "SMS 도착 완료", Snackbar.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Snackbar.make(view, "SMS 도착 실패", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }
}
