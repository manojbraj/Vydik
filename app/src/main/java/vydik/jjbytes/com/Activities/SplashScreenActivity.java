package vydik.jjbytes.com.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.GetUserLoginData;
import vydik.jjbytes.com.Models.LoginGetDB;
import vydik.jjbytes.com.constants.Constants;
import vydik.jjbytes.com.view.ContentView;
import vydik.jjbytes.com.view.SplashView;
import vydik.jjbytes.com.view.SplashView.ISplashListener;

public class SplashScreenActivity extends ActionBarActivity {

    private static final String TAG = "SplashScreenActivity";
    private static final boolean DO_XML = false;

    private ViewGroup mMainView;
    private SplashView mSplashView;
    private View mContentView;
    private Handler mHandler = new Handler();
    CheckBox user_login,purohit_login;
    Button continue_button;
    AlertDialog alertDialog;
    MainDatabase database;
    ArrayList<LoginGetDB> loginDetails;
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> image = new ArrayList<String>();
    private ArrayList<String> type = new ArrayList<String>();

    public static String ID,UserName,ImageUrl,LoginType = "non",GCMLoginType;
    String roId = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new MainDatabase(this);
        database = database.open();
        loginDetails = database.getMyLogin();

        // change the DO_XML variable to switch between code and xml
        if(DO_XML){
            // inflate the view from XML and then get a reference to it
            setContentView(R.layout.splash_screen);
            mMainView = (ViewGroup) findViewById(R.id.main_view);
            mSplashView = (SplashView) findViewById(R.id.splash_view);
        } else {
            // create the main view
            mMainView = new FrameLayout(getApplicationContext());

            // create the splash view
            mSplashView = new SplashView(getApplicationContext());
            mSplashView.setRemoveFromParentOnEnd(true); // remove the SplashView from MainView once animation is completed
            mSplashView.setSplashBackgroundColor(getResources().getColor(R.color.splash_bg)); // the background color of the view
            mSplashView.setRotationRadius(getResources().getDimensionPixelOffset(R.dimen.splash_rotation_radius)); // radius of the big circle that the little circles will rotate on
            mSplashView.setCircleRadius(getResources().getDimensionPixelSize(R.dimen.splash_circle_radius)); // radius of each circle
            mSplashView.setRotationDuration(getResources().getInteger(R.integer.splash_rotation_duration)); // time for one rotation to be completed by the small circles
            mSplashView.setSplashDuration(getResources().getInteger(R.integer.splash_duration)); // total time taken for the circles to merge together and disappear
            mSplashView.setCircleColors(getResources().getIntArray(R.array.splash_circle_colors)); // the colors of each circle in order

            // add splash view to the parent view
            mMainView.addView(mSplashView);
            setContentView(mMainView);
        }

        // pretend like we are loading data
        startLoadingData();
    }

    private void startLoadingData(){
        // finish "loading data" in a random time between 1 and 3 seconds
        Random random = new Random();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoadingDataEnded();
            }
        }, 1000 + random.nextInt(2000));
    }

    private void onLoadingDataEnded(){
        Context context = getApplicationContext();
        // now that our data is loaded we can initialize the content view
        mContentView = new ContentView(context);
        // add the content view to the background
        mMainView.addView(mContentView, 0);

        // start the splash animation
        mSplashView.splashAndDisappear(new ISplashListener(){
            @Override
            public void onStart(){
                // log the animation start event
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash started");
                }
            }

            @Override
            public void onUpdate(float completionFraction){
                // log animation update events
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash at " + String.format("%.2f", (completionFraction * 100)) + "%");
                }
            }

            @Override
            public void onEnd(){
                // log the animation end event
                if(BuildConfig.DEBUG){

                }

                if(loginDetails.size() == 0){
                    GCMLoginType = "new";
                    //CallPopup();
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.putExtra("login_type", "user");
                    startActivity(intent);
                    finish();
                }
                else{
                    for(int i=0;i<loginDetails.size();i++){
                        id.add(loginDetails.get(i).getId());
                        name.add(loginDetails.get(i).getEmail());
                        image.add(loginDetails.get(i).getMobile());
                        type.add(loginDetails.get(i).getType());

                        ID = id.get(0).toString();
                        UserName = name.get(0).toString();
                        ImageUrl = image.get(0).toString();
                        LoginType = type.get(0).toString();
                    }
                    if(LoginType.equals("purohit")){
                        GCMLoginType = "purohit";
                        Intent intent = new Intent(SplashScreenActivity.this, PurohithMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        GCMLoginType = "user";
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                // free the view so that it turns into garbage
                mSplashView = null;

            }
        });
    }
/*login popup*/
    private void CallPopup() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.login_popup, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD.setCancelable(false);
        user_login = (CheckBox) PromtView.findViewById(R.id.user_login);
        purohit_login = (CheckBox) PromtView.findViewById(R.id.purohit_login);
        continue_button = (Button) PromtView.findViewById(R.id.continue_button);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_login.isChecked() && purohit_login.isChecked()) {
                    Toast.makeText(SplashScreenActivity.this, "Please select any one mode off login", Toast.LENGTH_LONG).show();
                } else if (user_login.isChecked()) {
                    alertD.dismiss();
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.putExtra("login_type", "user");
                    startActivity(intent);
                    finish();
                } else if (user_login.isChecked() == false && purohit_login.isChecked() == false) {
                    Toast.makeText(SplashScreenActivity.this, "Please select a login type and proceed", Toast.LENGTH_LONG).show();
                } else {
                    alertD.dismiss();
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.putExtra("login_type", "purohit");
                    startActivity(intent);
                    finish();
                }
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
