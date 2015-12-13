package vydik.jjbytes.com.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Extras.ConnectionDetector;
import vydik.jjbytes.com.Fragments.NavigationDrawerFragment;
import vydik.jjbytes.com.Interfaces.ApplicationConstants;
import vydik.jjbytes.com.Interfaces.NavigationDrawerCallbacks;
import vydik.jjbytes.com.Models.GetUserLoginData;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 10/2/2015.
 */
public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks,View.OnClickListener {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    String longitude = "12.9667";
    String latitude = "77.5667";
    Location location;
    String officealSunrise,officealSunset,FinalSunrise,FinalSunSet;
    TextView dateofDay,monthofYear,daySunrise,daySunset;
    private int ALmonthOfYear, ALdayOfMonth,ALyear;
    /*tiles click text*/
    TextView BookPuja,Bhajans,PujaAtTemple;
    CheckBox PujaWP,PujaWWP;
    Button PujaAccept,PujaCancel;
    private GoogleApiClient mGoogleApiClient;
    MainDatabase database;

    ArrayList<GetUserLoginData> getUserLoginData;
    private ArrayList<String> UserId = new ArrayList<String>();

    /*for slider*/
    public int currentimageindex=0;
    Timer timer;
    TimerTask task;
    ImageView slidingimage;

    /*for gcm*/
    Context applicationContext;
    String PhoneNumber="7873885045",regId = "";
    //gcm variable
    GoogleCloudMessaging gcmObj;
    public static final String REG_ID = "regId";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    RequestParams params = new RequestParams();

    private int[] IMAGE_IDS = {
            R.drawable.slider_banner_one, R.drawable.slider_banner_two, R.drawable.slider_banner_three,
            R.drawable.main_screen_banner
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        database = new MainDatabase(this);
        database = database.open();
        getUserLoginData = database.getUserLoginDetails();
        if(getUserLoginData.size() == 0){

        }else {
            for (int i = 0; i < getUserLoginData.size(); i++) {
                UserId.add(getUserLoginData.get(i).getUserid());
                Constants.UserIdData = UserId.get(0).toString();
            }
        }

        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {

                AnimateandSlideShow();

            }
        };

        int delay = 50; // delay for 50meli sec sec.

        int period = 8000; // repeat every 4 sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);

            }

        }, delay, period);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dateofDay = (TextView) findViewById(R.id.date);
        monthofYear = (TextView) findViewById(R.id.month);
        daySunrise = (TextView) findViewById(R.id.sunrise);
        daySunset = (TextView) findViewById(R.id.sunset);

        BookPuja = (TextView) findViewById(R.id.book_puja_tile);
        BookPuja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallPackageChoosePopup();
            }
        });

        Bhajans = (TextView) findViewById(R.id.listen_bhajans);
        Bhajans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BhajansActivity.class);
                startActivity(intent);
            }
        });

        PujaAtTemple = (TextView) findViewById(R.id.puja_temple);
        PujaAtTemple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Thank you for your interest in the service Book a Puja @Temple. We are launching this service soon...",Toast.LENGTH_LONG).show();
            }
        });

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        Calendar c = Calendar.getInstance();
        ALyear = c.get(Calendar.YEAR);
        ALmonthOfYear = c.get(Calendar.MONTH);
        ALdayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        String month = getMonthForInt(c);
        String dayOfM = Integer.toString(ALdayOfMonth);

        dateofDay.setText(dayOfM);
        monthofYear.setText(month);

        location = new Location(longitude,latitude);
        SunriseSunsetCalculator calculate = new SunriseSunsetCalculator(location, "Asia/Kollata");

        officealSunrise = calculate.getOfficialSunriseForDate(Calendar.getInstance());
        officealSunset = calculate.getOfficialSunsetForDate(Calendar.getInstance());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        Date date = null;
        Date sunset = null;
        try {
            date = sdf.parse(officealSunrise);
            sunset = sdf.parse(officealSunset);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 330);

        Calendar sunsetCal = Calendar.getInstance();
        sunsetCal.setTime(sunset);
        sunsetCal.add(Calendar.MINUTE, 330);

        FinalSunrise = sdf.format(calendar.getTime());
        FinalSunSet = sdf.format(sunsetCal.getTime());

        daySunrise.setText("Sunrise : "+FinalSunrise+" AM");
        daySunset.setText("Sunset : " + FinalSunSet + " PM");
        System.out.println("data from splash: "+SplashScreenActivity.GCMLoginType);
        if (checkPlayServices()) {
            if(SplashScreenActivity.GCMLoginType.equals("new")){
                registerInBackground(PhoneNumber);
            }else{

            }
        }
    }

    //GCM CODE START
    private void registerInBackground(final String PhoneNumber) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    regId = gcmObj
                            .register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;
                    System.out.println("Registration ID :"+msg);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    storeRegIdinSharedPref(MainActivity.this, regId, PhoneNumber);
                } else {

                }
            }
        }.execute(null, null, null);
    }

    private void storeRegIdinSharedPref(Context context, String regId,
                                        String emailID) {
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putString(PhoneNumber, PhoneNumber);
        editor.commit();
        storeRegIdinServer();
    }
/*send gcm to server*/
    private void storeRegIdinServer() {
        params.put("gcm_client_id", regId);
        params.put("gcm_email_id",PhoneNumber);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String Loginsatus="0";
        client.get("http://vydik.com/android_book_pur_noti.php?gcm_id="+regId+"&phone_no="+PhoneNumber+"&type="+SplashScreenActivity.LoginType+"&log="+Loginsatus+"",

                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                    // When the response returned by REST has Http
                    // response code '200'

                    //  @Override
                    public void onSuccess(String response) {

                    }
                    //  @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(applicationContext,
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(applicationContext,
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    applicationContext,
                                    "Unexpected Error occcured! [Most common Error: Device might "
                                            + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        MainActivity.this,
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {


        }
        return true;
    }
    /**
     * Helper method to start the animation on the splash screen
     */
    private void AnimateandSlideShow() {


        slidingimage = (ImageView)findViewById(R.id.slider_layout);
        slidingimage.setImageResource(IMAGE_IDS[currentimageindex % IMAGE_IDS.length]);

        currentimageindex++;
        /*remove below commented code for animation*/
        //Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        //slidingimage.startAnimation(rotateimage);
    }

    private void CallPackageChoosePopup() {
        final LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.book_puja_popup, null);
        final AlertDialog alertD = new AlertDialog.Builder(MainActivity.this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.XaxisDialog;
        alertD.setCancelable(false);
        PujaWP = (CheckBox) PromtView.findViewById(R.id.with_package);
        PujaWWP = (CheckBox) PromtView.findViewById(R.id.without_package);

        PujaAccept = (Button) PromtView.findViewById(R.id.puja_submit);
        PujaCancel = (Button) PromtView.findViewById(R.id.puja_cancel);

        PujaAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PujaWP.isChecked() && PujaWWP.isChecked()){
                    Toast.makeText(MainActivity.this,"Please select any one type package",Toast.LENGTH_LONG).show();
                }else if(PujaWP.isChecked() == false && PujaWWP.isChecked() == false){
                    Toast.makeText(MainActivity.this,"Please any one type of puja package and proceed",Toast.LENGTH_LONG).show();
                }else if(PujaWP.isChecked()){
                    if(ConnectionDetector.isConnectingToInternet(getApplicationContext()))
                    {
                        alertD.dismiss();
                        Intent intent = new Intent(MainActivity.this,BookPujaActivity.class);
                        intent.putExtra("type","1");
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
                    }

                }else{
                    if(ConnectionDetector.isConnectingToInternet(getApplicationContext()))
                    {
                        alertD.dismiss();
                        Intent intent = new Intent(MainActivity.this,BookPujaActivity.class);
                        intent.putExtra("type","2");
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        PujaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }

    public String getMonthForInt(Object p0) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (ALmonthOfYear >= 0 && ALmonthOfYear <= 11 ) {
            month = months[ALmonthOfYear];
        }
        return month;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if(position == 0){

        }else if(position == 1){
            Intent intent = new Intent(MainActivity.this,InboxActivity.class);
            startActivity(intent);
        }else if(position == 2){
            CallPackageChoosePopup();
        }else if(position == 3){
            Intent intent = new Intent(MainActivity.this,BhajansActivity.class);
            startActivity(intent);
        }else if(position == 4){

        }else if(position == 5){

        }else if(position == 6){
            Intent intent = new Intent(MainActivity.this,UserBookingList.class);
            startActivity(intent);
        }else if(position == 7){
            Intent intent = new Intent(MainActivity.this,UserProfileActivity.class);
            startActivity(intent);
            //finish();
        } else if(position == 8){
            /*log out functionality for facebook g+ and normal sign up or sign in(purohith or user)*/
            if(SplashScreenActivity.LoginType.equals("facebook")){
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                database.deleteReplace();
                database.deleteUserData();
                SplashScreenActivity.LoginType.equals("non");
                LoginActivity.type_for_login.equals("");
                Intent intent = new Intent(MainActivity.this,SplashScreenActivity.class);
                startActivity(intent);
                finish();
            }else if(SplashScreenActivity.LoginType.equals("google")){
                database.deleteReplace();
                database.deleteUserData();
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }
                SplashScreenActivity.LoginType.equals("non");
                LoginActivity.type_for_login.equals("");
                Intent intent = new Intent(MainActivity.this,SplashScreenActivity.class);
                startActivity(intent);
                finish();
            }else{
                database.deleteReplace();
                database.deleteUserData();
                SplashScreenActivity.LoginType.equals("non");
                LoginActivity.type_for_login.equals("");
                Intent intent = new Intent(MainActivity.this,SplashScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }else{

        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}