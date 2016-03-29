package project4.skplanet.com.carowner;

import android.os.Build;
import android.widget.TextView;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import project4.skplanet.com.carowner.Main.MainActivity;

import static org.junit.Assert.*;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class ExampleUnitTest {
    private MainActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testUi() throws Exception {
        TextView distanceText = (TextView) activity.findViewById(R.id.text_distance);
        assertNotNull("TextView could not be found", distanceText);
        assertTrue("TextView contains incorrect text",
                "Hello world!".equals(distanceText.getText().toString()));
    }
}