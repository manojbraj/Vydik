package vydik.jjbytes.com.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by manoj on 10/17/2015.
 */
public class UserRegistrationFormOne extends ActionBarActivity implements OnItemSelectedListener{

    EditText UserFName,UserLName,UserEmail,UserPassword,UserConfirmPassword,UserContactNumber,UserAddress;
    Spinner UserState,UserCity,UserLocality;
    TextView UserPinCode;
    CheckBox Male,Female;
    Button UserNext;
    ArrayAdapter<String> dataAdapterLocality;
    ArrayAdapter<String> dataAdapterPinCode;
    Constants constants;
    /*http entities*/
    Object content;
    HttpClient client = new DefaultHttpClient();
    HttpResponse response;

    int PositionValue;
    String StateR = "0",CityR = "0",LocalityR = "0",PinR = "0",LanguageR = "0";
    public static String GenderValue = "null";
    /*otp popup xml reff*/
    EditText OTPEdit;
    Button VerifyOTP,Cancel;
    public static String InputType;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration_form_one);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            InputType = extras.getString("type");
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();

        UserFName = (EditText) findViewById(R.id.first_name);
        UserLName = (EditText) findViewById(R.id.last_name);
        UserEmail = (EditText) findViewById(R.id.email);
        UserPassword = (EditText) findViewById(R.id.password);
        UserConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        UserContactNumber = (EditText) findViewById(R.id.mobile_number);
        UserAddress = (EditText) findViewById(R.id.user_address);

        UserState = (Spinner) findViewById(R.id.user_state);
        UserCity = (Spinner) findViewById(R.id.user_city);

        UserLocality = (Spinner) findViewById(R.id.user_Locality);
        UserLocality.setOnItemSelectedListener(this);

        UserPinCode = (TextView) findViewById(R.id.user_PinCode);

        UserNext = (Button) findViewById(R.id.next_one);
        Male = (CheckBox) findViewById(R.id.male);
        Female = (CheckBox) findViewById(R.id.female);

        if(InputType.equals("facebook")){
            UserFName.setText(LoginActivity.FacebookUserFName);
            UserLName.setText(LoginActivity.FAcebookUserLname);
            UserEmail.setText(LoginActivity.email);
        }else if(InputType.equals("google")){

        }else{

        }
       /* Male.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                GenderValue = "Male";
                Male.setBackground(getDrawable(R.drawable.male_selected));
                Female.setBackground(getDrawable(R.drawable.female));
            }
        });

        Female.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                GenderValue = "Female";
                Female.setBackground(getDrawable(R.drawable.female_selected));
                Male.setBackground(getDrawable(R.drawable.male));
            }
        });*/

        ArrayListConstants.LocalityName.add("Locality");
        ArrayListConstants.PinCode.add("Pin");

        dataAdapterLocality = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayListConstants.LocalityName);
        //dataAdapterPinCode = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayListConstants.PinCode);

        UserLocality.setAdapter(dataAdapterLocality);
        //UserPinCode.setAdapter(dataAdapterPinCode);

        new getLocalityName().execute();

        UserNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(new EditText[]{UserFName, UserLName, UserEmail, UserPassword, UserConfirmPassword, UserContactNumber, UserAddress})) {
                    if (isValidEmail(UserEmail.getEditableText().toString())) {
                        if (UserPassword.getText().toString().length() >= 6) {
                            if (UserPassword.getText().toString()
                                    .equals(UserConfirmPassword.getText().toString())) {
                                if (UserContactNumber.getText().toString().length() == 10) {
                                    if(UserState.getSelectedItem().toString().trim().equals("State")) {
                                        StateR = "1";
                                        CheckSpinnerMethod();
                                    }else{
                                        if(UserCity.getSelectedItem().toString().trim().equals("City")) {
                                            CityR = "2";
                                            CheckSpinnerMethod();
                                        }else{
                                            if(UserLocality.getSelectedItem().toString().trim().equals("Locality")){
                                                LocalityR = "3";
                                                CheckSpinnerMethod();
                                            }else{
                                                /*if(UserPinCode.getSelectedItem().toString().trim().equals("Pin")) {
                                                    PinR = "4";
                                                    CheckSpinnerMethod();
                                                }else{*/
                                                    CheckSpinnerMethod();
                                                //}
                                            }
                                        }
                                    }
                                }else{
                                    Toast.makeText(UserRegistrationFormOne.this, "Please make sure the entered mobile number is valid", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(UserRegistrationFormOne.this, "Please make sure your password matches", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(UserRegistrationFormOne.this, "Please make sure your password is greater than 6 characters", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(UserRegistrationFormOne.this, "Please enter a valid email id like vydik@domain.com", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(UserRegistrationFormOne.this, "Please Make sure u have filled all mandatory fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void CheckSpinnerMethod() {
        if(StateR.equals("1")){
            StateR = "0";
            Toast.makeText(UserRegistrationFormOne.this,"Please select State",Toast.LENGTH_LONG).show();
        }else if(CityR.equals("2")){
            CityR = "0";
            Toast.makeText(UserRegistrationFormOne.this,"Please select City",Toast.LENGTH_LONG).show();
        }else if(LocalityR.equals("3")){
            LocalityR = "0";
            Toast.makeText(UserRegistrationFormOne.this,"Please select Locality",Toast.LENGTH_LONG).show();
        }else if(PinR.equals("4")){
            PinR = "0";
            Toast.makeText(UserRegistrationFormOne.this,"Please select PinCode",Toast.LENGTH_LONG).show();
        }else if(Male.isChecked() == false && Female.isChecked() == false){
            Toast.makeText(UserRegistrationFormOne.this,"Please select your gender",Toast.LENGTH_LONG).show();
        }else if(Male.isChecked() && Female.isChecked()){
            Toast.makeText(UserRegistrationFormOne.this,"Please select any one gender",Toast.LENGTH_LONG).show();
        } else{

            if(Male.isChecked()){
                Male.setChecked(true);
                Female.setChecked(false);
                GenderValue = "Male";
            }

            if(Female.isChecked()){
                Female.setChecked(true);
                Male.setChecked(false);
                GenderValue = "Female";
            }

            constants.GetState = UserState.getSelectedItem().toString();
            constants.GetCity = UserCity.getSelectedItem().toString();
            //constants.GetPinCode = UserPinCode.getSelectedItem().toString();
            constants.UserFName = UserFName.getText().toString();
            constants.UserLName = UserLName.getText().toString();
            constants.UserEmail = UserEmail.getText().toString();
            constants.UserPassword = UserPassword.getText().toString();
            constants.UserContact = UserContactNumber.getText().toString();
            constants.UserAddress = UserAddress.getText().toString();

            //CallOtpVerifyPopup();
            /*once otp is configured open callotpverfication*/
            Intent intent = new Intent(UserRegistrationFormOne.this,UserRegistrationFinalSignUp.class);
            startActivity(intent);
            finish();
        }
    }
/*otp verify popup*/

    private void CallOtpVerifyPopup() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.otp_verification_popup, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD.setCancelable(false);
            OTPEdit = (EditText) PromtView.findViewById(R.id.otp_edit);
            VerifyOTP = (Button) PromtView.findViewById(R.id.otp_verify);
            Cancel = (Button) PromtView.findViewById(R.id.cancel_otp);

        VerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(new EditText[]{OTPEdit})){
                    constants.OTPString = OTPEdit.getText().toString();
                    if(constants.OTPString.equals("1234")){
                        /*if otp matches call next class*/
                        alertD.dismiss();
                        Intent intent = new Intent(UserRegistrationFormOne.this,UserRegistrationFinalSignUp.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(UserRegistrationFormOne.this,"Please enter a valid otp",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(UserRegistrationFormOne.this,"Please enter the otp to verify",Toast.LENGTH_LONG).show();
                }
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        alertD.setView(PromtView);
        alertD.show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.user_Locality:
                PositionValue = position;
                for (int i = 1; i < ArrayListConstants.LocalityName.size(); i++) {
                    if (i == PositionValue) {
                        pos = i;
                        constants.GetLocality = ArrayListConstants.LacalityId.get(i - 1);

                        for(int j=1;j<ArrayListConstants.PinCode.size();j++){
                            if(j == pos){
                                String pin = ArrayListConstants.PinCode.get(j);
                                constants.GetPinCode = pin;
                                UserPinCode.setText(pin);
                            }
                        }

                    }
                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*get locality and pin*/
    private class getLocalityName extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(UserRegistrationFormOne.this, constants.satecityProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet(constants.getLocalityAndPin);
            content = null;
            try{
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                JSONObject object1 = new JSONObject(content.toString());
                JSONArray array = object1.getJSONArray("location");
                for(int i=0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.has("loc_name"))
                    {
                        if(object.getString("loc_name")!= null)
                        {
                            ArrayListConstants.LocalityName.add(object.getString("loc_name").toString());
                        }else{
                            ArrayListConstants.LocalityName.add("not specifies");
                        }
                    }else{
                        ArrayListConstants.LocalityName.add("not specifies");
                    }

                    if(object.has("loc_id"))
                    {
                        if(object.getString("loc_id")!= null)
                        {
                            ArrayListConstants.LacalityId.add(object.getString("loc_id").toString());
                        }else{
                            ArrayListConstants.LacalityId.add("not specified");
                        }
                    }else{
                        ArrayListConstants.LacalityId.add("not specified");
                    }

                    if(object.has("pincode"))
                    {
                        if(object.getString("pincode")!= null)
                        {
                            ArrayListConstants.PinCode.add(object.getString("pincode").toString());
                        }else{
                            ArrayListConstants.PinCode.add("not specified");
                        }
                    }else{
                        ArrayListConstants.PinCode.add("not specified");
                    }
                }
            }
            catch (IOException e){

            }
            catch (JSONException e){

            }
            catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Utilities.cancelProgressDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ArrayListConstants.LocalityName.clear();
        ArrayListConstants.PinCode.clear();

        if(InputType.equals("facebook")){
            /*FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();*/
            Intent intent = new Intent(UserRegistrationFormOne.this,LoginActivity.class);
            intent.putExtra("login_type", "user");
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(UserRegistrationFormOne.this, LoginActivity.class);
            intent.putExtra("login_type", "user");
            startActivity(intent);
            finish();
        }
    }
}
