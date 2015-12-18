package vydik.jjbytes.com.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

/**
 * Created by user on 12/18/2015.
 */
public class TermsAndCondition extends ActionBarActivity {
    WebView TermsCondition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_condition);

        TermsCondition = (WebView) findViewById(R.id.terms_condition);
        TermsCondition.getSettings().setJavaScriptEnabled(true);
        String pdf = "http://www.vydik.com/User_Terms_and_Conditions_Booking%20time.pdf";
        TermsCondition.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
    }
}
