package vydik.jjbytes.com.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Extras.ConnectionDetector;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ConfigFile;
import vydik.jjbytes.com.constants.Constants;
import vydik.jjbytes.com.view.AnimationThread;

/**
 * Created by manoj' on 10/2/2015.
 */
public class LoginActivity extends FragmentActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "LoginActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    EditText username,password,UserPhoneNumber,OTPVERIFY,FPassword,FConformPassword;
    Button normalLoginButton,SignUpButton,CreateUser,SubmitForgotPassword,CancelForgotPassword,SkipLogin;
    TextView ForgotPassword;
    public static String type_for_login = "non";

    MainDatabase database;
    Object content;
    HttpClient client = new DefaultHttpClient();
    String ForgotPasswordUserNumber,ReceivedOTP,EditOtp,EditPassword,EditConfirmPassword;

    /*facebook variable inetialization*/
    CallbackManager callbackManager;
    public LoginButton loginButton;
    public static String uid = "",FacebookUserFName = " ",FAcebookUserLname = " ",personName = " ",personPhotoUrl = " ";
    String api_key;
    String access_token = "";
    public static String email = "",name = "",f_image = "",FacebookImage ="";
    String EditBoxMobile,EditBoxPassword;
    ConfigFile config;
    public long startTime;
    private AnimationThread animationThread;
    public int width, height;
    DisplayMetrics metrics;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    Constants constants;
    String ErrorServer;
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "vydik.jjbytes.com.Activities",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //appLogin();
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("login error", "facebook login canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                showAlert();
                Log.e("login error", "facebook login failed error");
            }

            private void showAlert() {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Unable To Login")
                        .setMessage("Try After Some Time")
                        .setPositiveButton("ok", null)
                        .show();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type_for_login = extras.getString("login_type");
        }

        if(type_for_login.equals("purohit")){
            constants.MyType = "Purohit";
        }else{
            constants.MyType = "USER";
        }
        database = new MainDatabase(this);
        database = database.open();

        setContentView(R.layout.login_activity);

        animationThread = new AnimationThread(this);
        animationThread.setRunning(true);

        CreateUser = (Button) findViewById(R.id.create_user);
        CreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type_for_login.equals("purohit")) {
                    Intent intent = new Intent(LoginActivity.this, RegistrationFormOne.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(LoginActivity.this,UserRegistrationFormOne.class);
                    intent.putExtra("type", "normalregistration");
                    startActivity(intent);
                    finish();
                }
            }
        });
        //get display metrics
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        //get object
        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        //loginButton.setReadPermissions(Arrays.asList("name"));
        //loginButton.setBackgroundResource(R.drawable.facebook_icon);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setAlpha(0);
        appLogin(true);

        //Track Access Tokens
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // App code
                appLogin(false);

            }
        };

        //Track Current Profile
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
            }
        };

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);

        username = (EditText) findViewById(R.id.username);
        username.setHintTextColor(getResources().getColor(R.color.white));
        password = (EditText) findViewById(R.id.password);
        password.setHintTextColor(getResources().getColor(R.color.white));
        normalLoginButton = (Button) findViewById(R.id.login_button);
        SkipLogin = (Button) findViewById(R.id.login_skip);
        SignUpButton = (Button) findViewById(R.id.create_user);
        ForgotPassword = (TextView) findViewById(R.id.forgot_password);

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForgotPassword();
            }
        });
        normalLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              LoginValidationMethod();
            }
        });

        SkipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashScreenActivity.GCMLoginType = "user";
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(type_for_login.equals("purohit")){
            btnSignIn.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
        }else{
            btnSignIn.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
        }

        // Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    private void CallForgotPassword() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.forgot_password_popup_one, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD.setCancelable(false);

        UserPhoneNumber = (EditText) PromtView.findViewById(R.id.phone_number);
        SubmitForgotPassword = (Button) PromtView.findViewById(R.id.submit_phonenumber);
        CancelForgotPassword = (Button) PromtView.findViewById(R.id.cancel_fp);
        SubmitForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(new EditText[] { UserPhoneNumber})) {
                    if (UserPhoneNumber.getText().toString().length() == 10) {
                        ForgotPasswordUserNumber = UserPhoneNumber.getText().toString();
                        alertD.cancel();
                        new CallOtpGenerateURL().execute();
                    }else{
                        Toast.makeText(LoginActivity.this, "Please make sure the entered mobile number is valid", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"please enter your number to proceed...",Toast.LENGTH_LONG).show();
                }
            }
        });
        CancelForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.cancel();
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }

    /*otp background*/
    private class CallOtpGenerateURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(LoginActivity.this, "Requesting OTP Please wait...", false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpget = new HttpGet(constants.RequestOTPUrl+ForgotPasswordUserNumber);
            content = null;
            try{
                HttpResponse response;
                response = client.execute(httpget);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                System.out.println("output otp "+content.toString());

                JSONObject object = new JSONObject(content.toString());
                if(object.has("otp")){
                    if(object.getString("otp")!= null){
                        ReceivedOTP = object.getString("otp").toString();
                    }
                }
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Utilities.cancelProgressDialog();
            CallOtpVeriyPopup();
        }
    }

    private void CallOtpVeriyPopup(){
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.forgot_password_popup_two, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD.setCancelable(false);
        OTPVERIFY = (EditText) PromtView.findViewById(R.id.otp_number);
        FPassword = (EditText) PromtView.findViewById(R.id.password);
        FConformPassword = (EditText) PromtView.findViewById(R.id.confirm_password);
        SubmitForgotPassword = (Button) PromtView.findViewById(R.id.submit_phonenumber);
        CancelForgotPassword = (Button) PromtView.findViewById(R.id.cancel_fp);
        SubmitForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(new EditText[] { OTPVERIFY,FPassword,FConformPassword})) {
                    EditOtp = OTPVERIFY.getText().toString();
                    if(ReceivedOTP.equals(EditOtp)) {
                        if (FPassword.getText().toString().length() >= 6) {
                            if (FPassword.getText().toString()
                                    .equals(FConformPassword.getText().toString())) {
                                    EditPassword = FPassword.getText().toString();
                                    EditConfirmPassword = FConformPassword.getText().toString();
                                    alertD.cancel();
                                new CallSubmitFasswordPassword().execute();
                            } else {
                                Toast.makeText(LoginActivity.this, "Please make sure your password matches", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Please make sure your password is greater than 6 characters", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "OTP not matching, Please enter a valid otp sent to your number...", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"please enter the above mandatory fiends to continue...",Toast.LENGTH_LONG).show();
                }
            }
        });

        CancelForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.cancel();
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }

    /*send forgot password data to server*/
    private class CallSubmitFasswordPassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(LoginActivity.this, "Please wait this may take few minutes...", false);
        }
        @Override
        protected String doInBackground(String... params) {
            HttpGet httpget = new HttpGet(constants.UpdateForgotPassword+ForgotPasswordUserNumber+"&otp="+EditOtp+"&pwd="+EditPassword);
            content = null;
            try{
                HttpResponse response;
                response = client.execute(httpget);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                System.out.println("output otp "+content.toString());

                JSONObject object = new JSONObject(content.toString());
                if(object.has("sucess")){
                    if(object.getString("sucess")!= null){
                        ReceivedOTP = object.getString("sucess").toString();
                    }
                }
            }catch (Exception e){

            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Utilities.cancelProgressDialog();
            if(ReceivedOTP.equals("sucess")){
                Toast.makeText(LoginActivity.this, "Password change successful...", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(LoginActivity.this, "Failed to change password...", Toast.LENGTH_LONG).show();
            }
        }
    }
    /*g+ sign up start*/
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
        }
    }
    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // Signin button clicked
                signInWithGplus();
                break;
            case R.id.btn_sign_out:
                // Signout button clicked
                signOutFromGplus();
                break;
            case R.id.btn_revoke_access:
                // Revoke access button clicked
                revokeGplusAccess();
                break;
        }
    }

    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                personName = currentPerson.getDisplayName();
                personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                /*Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);*/

                String Type = "google";
                //database.insertLogin(personName, personPhotoUrl, Type);
                String logType = "user",type = "google";
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }

                CallMainMethod(logType,type);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallMainMethod(String logType,String type) {
        if(logType.equals("user")){
            if(type.equals("normal")){
                if(constants.LoginFrom.equals("PackagePuja")){
                    constants.LoginFrom = "new one";
                    Intent intent = new Intent(this,CheckoutActivityAddress.class);
                    intent.putExtra("type","with");
                    startActivity(intent);
                    finish();
                }else if(constants.LoginFrom.equals("WithoutPackagePuja")){
                    constants.LoginFrom = "new one";
                    Intent intent = new Intent(this, CheckoutActivityAddress.class);
                    intent.putExtra("type","without");
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else {
                Intent intent = new Intent(this, UserRegistrationFormOne.class);
                intent.putExtra("type", type);
                startActivity(intent);
                finish();
            }
            /**/
        }else{
            Intent intent = new Intent(LoginActivity.this, PurohithMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    /**
     * Sign-out from google
     * */
    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    /**
     * Revoking access from google
     * */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }
/*g+ sign up end*/

/*normal user login with validation start from hear*/

    public void LoginValidationMethod(){
        if (validate(new EditText[] { username, password })) {
            if (username.getEditableText().toString().length() == 10) {
                    String user_email = username.getEditableText().toString();
                    String user_Password = password.getEditableText()
                            .toString();
                    if(ConnectionDetector.isConnectingToInternet(getApplicationContext()))
                    {
                        connectwithhttpget(user_email, user_Password);
                    }else{
                        Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
                    }
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Please make sure the entered mobile number is valid.",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            username.setHintTextColor(getResources().getColor(R.color.red));
            username.setHint("Enter Phone Number");
            password.setHintTextColor(getResources().getColor(R.color.red));
            password.setHint("Enter Password");
            Vibrator v = (Vibrator) LoginActivity.this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
    }

    private void connectwithhttpget(final String user_email, final String user_password) {
        EditBoxMobile = user_email;
        EditBoxPassword = user_password;
        new SubmitRegistrationForm().execute();
    }
    /*normal login background process*/
    class SubmitRegistrationForm extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(type_for_login.equals("purohit")){
                Utilities.displayProgressDialog(LoginActivity.this, constants.LoginPurohitProgress, false);
            }else {
                Utilities.displayProgressDialog(LoginActivity.this, constants.LoginProgress, false);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = null;
            /*if statement for login*/
            if(type_for_login.equals("user")){
                httpPost = new HttpPost(constants.LoginUrl);
                try{
                    multipartEntity.addPart(constants.Ls,new StringBody(constants.LType));
                    multipartEntity.addPart(constants.Lm,new StringBody(EditBoxMobile));
                    multipartEntity.addPart(constants.Lp,new StringBody(EditBoxPassword));
                }catch (Exception e){

                }
            }else{
                httpPost = new HttpPost(constants.PurohitLogin);
                try{
                    multipartEntity.addPart(constants.PurSubmit,new StringBody(constants.PurSubmit));
                    multipartEntity.addPart(constants.PurPhone,new StringBody(EditBoxMobile));
                    multipartEntity.addPart(constants.PurPass,new StringBody(EditBoxPassword));
                }catch (Exception e){

                }
            }
            try{
                httpPost.setEntity(multipartEntity);
                try{
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    InputStream inputStream = httpResponse.getEntity()
                            .getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);
                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }
                    return stringBuilder.toString();

                }catch (ClientProtocolException cpe) {
                    System.out.println("First Exception caz of HttpResponese :"
                            + cpe);
                    cpe.printStackTrace();
                }catch (IOException ioe) {
                    System.out.println("Second Exception caz of HttpResponse :"
                            + ioe);
                    ioe.printStackTrace();
                }catch (Exception e){

                }
            }
            catch (IndexOutOfBoundsException e) {

            }
            catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Utilities.cancelProgressDialog();
            System.out.println("login responce from server"+s);
            String FailureMobileResult = "",FailurePasswordResult = "",SuccessResult = "",UserSuccess = "";
            try{
                if(type_for_login.equals("purohit")) {
                    JSONObject object = new JSONObject(s);
                    if (object.has("incorect_phone")) {
                        if (object.getString("incorect_phone") != null) {
                            FailureMobileResult = object.getString("incorect_phone").toString();
                        } else {

                        }
                    } else {

                    }

                    if (object.has("fails")) {
                        if (object.getString("fails") != null) {
                            FailurePasswordResult = object.getString("fails").toString();
                        } else {

                        }
                    } else {

                    }

                    if (object.has("sucess")) {
                        if (object.getString("sucess") != null) {
                            SuccessResult = object.getString("sucess").toString();
                        } else {

                        }
                    } else {

                    }
                }else {
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        if (jsonObject.has("First_name")) {
                            if (jsonObject.getString("First_name") != null) {
                                constants.UL1 = jsonObject.getString("First_name").toString();
                                System.out.println("success message : " + constants.UL1);
                            } else {

                            }
                        } else {

                        }

                        if (jsonObject.has("last_name")) {
                            if (jsonObject.getString("last_name") != null) {
                                constants.UL2 = jsonObject.getString("last_name").toString();
                                System.out.println("success message : " + constants.UL2);
                            } else {

                            }
                        } else {

                        }

                        if (jsonObject.has("email_id")) {
                            if (jsonObject.getString("email_id") != null) {
                                constants.UL3 = jsonObject.getString("email_id").toString();
                                System.out.println("success message : " + constants.UL3);
                            } else {

                            }
                        } else {

                        }

                        if (jsonObject.has("mobile_no")) {
                            if (jsonObject.getString("mobile_no") != null) {
                                constants.UL4 = jsonObject.getString("mobile_no").toString();
                                System.out.println("success message : " + constants.UL4);
                            } else {

                            }
                        } else {

                        }

                        if (jsonObject.has("locality")) {
                            if (jsonObject.getString("locality") != null) {
                                constants.UL5 = jsonObject.getString("locality").toString();
                                System.out.println("success message : " + constants.UL5);
                            } else {

                            }
                        } else {

                        }

                        if (jsonObject.has("state")) {
                            if (jsonObject.getString("state") != null) {
                                constants.UL6 = jsonObject.getString("state").toString();
                                System.out.println("success message : " + constants.UL6);
                            } else {

                            }
                        } else {

                        }

                        if (jsonObject.has("address")) {
                            if (jsonObject.getString("address") != null) {
                                constants.UL7 = jsonObject.getString("address").toString();
                                System.out.println("success message : " + constants.UL7);
                            } else {

                            }
                        } else {

                        }

                        if (jsonObject.has("user_id")) {
                            if (jsonObject.getString("user_id") != null) {
                                constants.UL8 = jsonObject.getString("user_id").toString();
                                System.out.println("success message : " + constants.UL8);
                            } else {

                            }
                        } else {

                        }

                        if (jsonObject.has("success")) {
                            if (jsonObject.getString("success") != null) {
                                constants.UL9 = jsonObject.getString("success").toString();
                                UserSuccess = jsonObject.getString("success").toString();
                                System.out.println("success message : " + UserSuccess);
                            } else {

                            }
                        } else {

                        }

                        if(jsonObject.has("profile_picture")){
                            if(jsonObject.getString("profile_picture")!= null){
                                constants.UL10 = "http://vydik.com/"+jsonObject.getString("profile_picture").toString();
                            }else{

                            }
                        }else{

                        }

                        if(jsonObject.has("msg")){
                            if(jsonObject.getString("msg")!= null){
                                ErrorServer = jsonObject.getString("msg").toString();
                            }else {
                                ErrorServer = "success";
                            }
                        }else {
                            ErrorServer = "success";
                        }
                    }
                }
            }catch (Exception e){

            }

/*mage a top level if for type check purohit or user*/
            if(type_for_login.equals("user")){
                if(UserSuccess.equals("Sucessfully")){
                    database.insertLogin("", "", "user");
                    database.InserUserLoginData(constants.UL1,constants.UL2,constants.UL3,constants.UL4,constants.UL5,constants.UL6,
                            constants.UL7,constants.UL8,constants.UL10);
                    String logType = "user",type = "normal";
                    CallMainMethod(logType,type);
                }else if(ErrorServer.equals("Incorrect User Or Password")){
                    Toast.makeText(LoginActivity.this, "Incorrect User ID Or Password", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "User doesn't exist please register...", Toast.LENGTH_LONG).show();
                }
            }else {
                if (FailureMobileResult.equals("Enter correct Phone no")) {
                    Toast.makeText(LoginActivity.this, FailureMobileResult, Toast.LENGTH_LONG).show();
                } else if (FailurePasswordResult.equals("Enter Correct Password")) {
                    Toast.makeText(LoginActivity.this, FailurePasswordResult, Toast.LENGTH_LONG).show();
                } else if (SuccessResult.equals("Sucessfully Login")) {
                    database.insertLogin("", "", "purohit");
                    String logType = "purohit",type = "purohit";
                    CallMainMethod(logType,type);
                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong please try after some time", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

/*edit text box validator*/
    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }
/*email validator*/

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    /*fb start*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

        boolean retry = true;
        animationThread.setRunning(false);

        while (retry)
        {
            try
            {
                animationThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                // TODO: handle exception
            }
        }
    }

    private void updateUI() {

        Profile profile = Profile.getCurrentProfile();
        AccessToken  token = AccessToken.getCurrentAccessToken();

        if (profile != null) {
            uid = profile.getId();
            FacebookUserFName = profile.getFirstName();
            FAcebookUserLname  = profile.getLastName();
        }

        if (token != null) {
            uid = token.getUserId();
            access_token = token.getToken();
        }

    }


    private void appLogin(boolean ani)
    {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {

                                    // Application code
                                    Log.v("MainActivity", response.toString());

                                    try {
                                        if (object != null)
                                            email = object.getString("email");

                                    } catch (JSONException e) {

                                    }

                                    if (email != "") {
                                        updateUI();
                                        System.out.println("user name" + name);
                                        loginProcess(email, uid, access_token);
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }, 2000);
        }
        else if(ani)
        {
            Calendar calendar = Calendar.getInstance();
            startTime = calendar.getTimeInMillis();
            //start animation
            animationThread.start();
        }
    }

    private void loginProcess(String email, String uid, String access_token) {
        String Type = "facebook";
        URL imageURL;
        FacebookImage = "https://graph.facebook.com/" + uid + "/picture?type=large";
        try {
            imageURL = new URL("https://graph.facebook.com/" + uid + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //database.insertLogin(email,FacebookImage,Type);
        String logType = "user",type = "facebook";
        CallMainMethod(logType,type);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}