package com.skplanet.trunk.carowner;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 1001955 on 3/10/16.
 */
public class TTSActivity extends Activity implements TextToSpeech.OnInitListener {


    private TextToSpeech textToSpeech;

	@Bind(R.id.speak_out_button)
	public Button speakOutButton;

	@Bind(R.id.speech_contents_edit_text)
    public EditText speechContentEditText;

	@Bind(R.id.pitch_seekbar)
    public SeekBar pitchControl;

	@Bind(R.id.speech_rate_seekbar)
    public SeekBar speechRateControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tts);
		ButterKnife.bind(this);

        textToSpeech = new TextToSpeech(this, this);
    }

    /**
     * TTS 사용을 위한 {@link TextToSpeech.OnInitListener} 함수.
     *
     * @param status
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            setTtsProperties();
            int result = textToSpeech.setLanguage(Locale.KOREA);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Snackbar.make(speakOutButton, "이 언어는 지원하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                speakOutButton.setEnabled(true);
            }

        } else {
            Snackbar.make(speakOutButton, "TTS 초기화 실패.", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void speakOut(View v) {
        setTtsProperties();

        String text = speechContentEditText.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void setTtsProperties() {
        float speechRate = ((float)speechRateControl.getProgress())/10;
        if (speechRate < 0.5) {
            Snackbar.make(speakOutButton, "Speech rate는 0.5 이상이어야 합니다.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        textToSpeech.setSpeechRate(speechRate); //0.5 ~ 2.0

        float pitch =((float) pitchControl.getProgress())/10;
        textToSpeech.setPitch(pitch); //~1.0
    }

    @Override
    public void onDestroy() {
        // TextToSpeech 리소스 해제.
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
