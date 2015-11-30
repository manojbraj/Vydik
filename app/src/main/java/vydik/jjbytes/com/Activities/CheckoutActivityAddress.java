package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.GetUserLoginData;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 11/3/2015.
 */
public class CheckoutActivityAddress extends ActionBarActivity {
    TextInputLayout FirstName,PhoneNumber,UserAddress,UserCity,UserLocation;
    EditText FName,LName,Phone,Address,City,Locality;
    CheckBox TermsCondition;
    Button Confirm;
    ArrayListConstants arrayListConstants;
    String InputType;

    Constants constants;
    BookPujaActivity bookPujaActivity;
    MainDatabase database;

    ArrayList<GetUserLoginData> getUserLoginData;
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> UserFirstName = new ArrayList<String>();
    private ArrayList<String> UserLastName= new ArrayList<String>();
    private ArrayList<String> UserEmail = new ArrayList<String>();
    private ArrayList<String> UserMobile = new ArrayList<String>();
    private ArrayList<String> UserLocality = new ArrayList<String>();
    private ArrayList<String> UserState = new ArrayList<String>();
    private ArrayList<String> UserAAddress = new ArrayList<String>();
    private ArrayList<String> UserId = new ArrayList<String>();
    private ArrayList<String> UserImage = new ArrayList<String>();

    String UFName,ULName,UEmail,UMobile,ULocality,UState,UAddress,UId,UImage,UCity;

    /*http entity*/
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity_one);

        database = new MainDatabase(this);
        database = database.open();
        getUserLoginData = database.getUserLoginDetails();

        if(getUserLoginData.size() == 0){

        }else {
            for(int i=0;i<getUserLoginData.size();i++){
                id.add(getUserLoginData.get(i).getId());
                UserFirstName.add(getUserLoginData.get(i).getFname());
                UserLastName.add(getUserLoginData.get(i).getLname());
                UserEmail.add(getUserLoginData.get(i).getEmail());
                UserMobile.add(getUserLoginData.get(i).getMobile());
                UserLocality.add(getUserLoginData.get(i).getLocality());
                UserState.add(getUserLoginData.get(i).getState());
                UserAAddress.add(getUserLoginData.get(i).getAddress());
                UserId.add(getUserLoginData.get(i).getUserid());
                UserImage.add(getUserLoginData.get(i).getImage());

                UFName = UserFirstName.get(0).toString();
                ULName = UserLastName.get(0).toString();
                UEmail = UserEmail.get(0).toString();
                UMobile = UserMobile.get(0).toString();
                ULocality = UserLocality.get(0).toString();
                UState = UserState.get(0).toString();
                UAddress = UserAAddress.get(0).toString();
                UId = UserId.get(0).toString();
                UImage = UserImage.get(0).toString();
            }
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            InputType = extras.getString("type");
        }

        FirstName = (TextInputLayout) findViewById(R.id.fNameLayout);
        FirstName.setErrorEnabled(true);
        FName = (EditText) findViewById(R.id.fName);
        FName.setError("Required");
        if(UFName!= null){
            FName.setText(UFName);
        }

        LName = (EditText) findViewById(R.id.lName);
        if(ULName!= null){
            LName.setText(ULName);
        }

        PhoneNumber = (TextInputLayout) findViewById(R.id.PhoneLayout);
        PhoneNumber.setErrorEnabled(true);
        PhoneNumber.setError("Please Enter your Phone Number");
        Phone = (EditText) findViewById(R.id.PhoneNumber);
        Phone.setError("Required");
        if(UMobile!= null){
            Phone.setText(UMobile);
        }

        UserAddress = (TextInputLayout) findViewById(R.id.address_layout);
        UserAddress.setErrorEnabled(true);
        Address = (EditText) findViewById(R.id.edit_address);
        Address.setError("Required");
        if(UAddress!= null){
            Address.setText(UAddress);
        }

        UserCity = (TextInputLayout) findViewById(R.id.city_layout);
        UserCity.setErrorEnabled(true);
        City = (EditText) findViewById(R.id.edit_city);
        City.setError("Required");

        UserLocation = (TextInputLayout) findViewById(R.id.locality_layout);
        UserLocation.setErrorEnabled(true);
        Locality = (EditText) findViewById(R.id.edit_locality);
        Locality.setError("Required");
        if(ULocality != null){
            Locality.setText(ULocality);
        }

        TermsCondition = (CheckBox) findViewById(R.id.terms_condition_check);

        Confirm = (Button) findViewById(R.id.continue_button);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(new EditText[]{FName,Phone,Address,City,Locality})){
                    if (Phone.getText().toString().length() == 10) {
                        if (TermsCondition.isChecked() == true) {

                            arrayListConstants.PurohithFirstName.clear();
                            arrayListConstants.PurohithExpertLevel.clear();
                            arrayListConstants.PurohithLocation.clear();
                            arrayListConstants.PurohithPrice.clear();
                            arrayListConstants.PurohithPhoto.clear();
                            bookPujaActivity.PoojaName.clear();
                            bookPujaActivity.PoojaTypeId.clear();
                            bookPujaActivity.PoojaId.clear();
                            bookPujaActivity.LocationName.clear();
                            bookPujaActivity.LangugesName.clear();
                            bookPujaActivity.PurohithSect.clear();

                            ULocality = Locality.getText().toString();
                            UCity = City.getText().toString();
                            UAddress = Address.getText().toString();
                            /*background process code*/
                            new SubbmitBookingDetails().execute();

                        } else {
                            Toast.makeText(CheckoutActivityAddress.this, "Please accept the terms and condition", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(CheckoutActivityAddress.this, "Please make sure the entered 10 digit valid mobile number", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(CheckoutActivityAddress.this, "Please Make sure u have filled all mandatory fields", Toast.LENGTH_LONG).show();
                }
            }
        });
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

    private class SubbmitBookingDetails extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(CheckoutActivityAddress.this, constants.BookingPujaProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(constants.SendBookingDetails);
            try{
                multipartEntity.addPart(constants.Booking1, new StringBody(constants.SearchPujaId));
                multipartEntity.addPart(constants.Booking2,new StringBody(constants.purohith_id));
                multipartEntity.addPart(constants.Booking3,new StringBody(UId));
                multipartEntity.addPart(constants.Booking4,new StringBody(constants.SDate));
                multipartEntity.addPart(constants.Booking5,new StringBody(constants.SearchPriceBooking));
                multipartEntity.addPart(constants.Booking6,new StringBody(ULocality));
                multipartEntity.addPart(constants.Booking7,new StringBody(BookPujaActivity.package_type));
                multipartEntity.addPart(constants.Booking8,new StringBody(constants.package_sect));
                multipartEntity.addPart(constants.Booking9,new StringBody("India"));
                multipartEntity.addPart(constants.Booking10,new StringBody(UState));
                multipartEntity.addPart(constants.Booking11,new StringBody(UCity));
                multipartEntity.addPart(constants.Booking12,new StringBody(UAddress));
                multipartEntity.addPart(constants.Booking13,new StringBody(constants.package_Name));
                multipartEntity.addPart(constants.Booking14,new StringBody(UFName+" "+ULName));
            }catch (Exception e){

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
            String Success =" ";
            try {
                JSONObject object = new JSONObject(s);
                if(object.has("success")){
                    if(object.getString("success")!= null){
                      Success = object.getString("success").toString();
                    }else{
                        Success = "failed";
                    }
                }else{
                    Success = "failed";
                }

                if(Success.equals("Sucessfully")){
                    /*insert to local db the booked puja if success*/
                    database.InserBookingDetails(UId,constants.SPujaName,constants.package_Name,constants.SDate,constants.SearchPriceBooking,UAddress,"0");
                    Toast.makeText(CheckoutActivityAddress.this,"Puja booking successful the vydik team will confirm the availability of purohit in next 48hrs",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CheckoutActivityAddress.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(CheckoutActivityAddress.this,"Failed to book puja please try after some time",Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(InputType.equals("with")) {
            Intent intent = new Intent(CheckoutActivityAddress.this, PackagePujaDetailActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(CheckoutActivityAddress.this, PurohithDetailsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}