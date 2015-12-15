package vydik.jjbytes.com.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by user on 12/15/2015.
 */
public class PanchangActivity extends ActionBarActivity {

    TextView SignLord,Sign,NakshatraOne,NakshatraTwo,NakshatraLord,NakshatraCharan,Day,Tithi,Yog,Karan,SunRise,SunSet,SelectedDate;
    Button DateSelector,SubmitDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panchang_activity);

        SignLord = (TextView) findViewById(R.id.sign_lord);
        Sign = (TextView) findViewById(R.id.sign);
        NakshatraOne = (TextView) findViewById(R.id.nakshatra);
        NakshatraTwo = (TextView) findViewById(R.id.nakshatra_second);
        NakshatraLord = (TextView) findViewById(R.id.naksahtra_lord);
        NakshatraCharan = (TextView) findViewById(R.id.nakshatra_charan);
        Day = (TextView) findViewById(R.id.day);
        Tithi = (TextView) findViewById(R.id.tithi);
        Yog = (TextView) findViewById(R.id.yog);
        Karan = (TextView) findViewById(R.id.karan);
        SunRise = (TextView) findViewById(R.id.sunrise);
        SunSet = (TextView) findViewById(R.id.sunset);
        SelectedDate = (TextView) findViewById(R.id.date_selected);

        DateSelector = (Button) findViewById(R.id.date_selector);
        SubmitDate = (Button) findViewById(R.id.submit_date);


    }
}