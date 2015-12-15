package vydik.jjbytes.com.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.GetUserLoginData;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 11/3/2015.
 */
public class CheckoutActivityAddress extends ActionBarActivity {
    TextInputLayout FirstName,PhoneNumber,UserAddress,UserCity,UserLocation,UserEmailPayment;
    EditText FName,LName,Phone,Address,City,Locality,Email;
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

    public static String UFName,ULName,UEmail,UMobile,ULocality,UState,UAddress,UId,UImage,UCity;

    /*http entity*/
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

    /*payment gate way hash map don't alter*/
    HashMap<String, String> params = new HashMap<>();
    String Amount = "1";
    String TransactionId = "0000";

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

        UserEmailPayment = (TextInputLayout) findViewById(R.id.EmailLayout);
        UserEmailPayment.setErrorEnabled(true);
        Email = (EditText) findViewById(R.id.Email);
        Email.setError("Required");

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
                if (validate(new EditText[]{FName,Phone,Email,Address,City,Locality})){
                    if (Phone.getText().toString().length() == 10) {
                        if (TermsCondition.isChecked() == true) {

                            /*arrayListConstants.PurohithFirstName.clear();
                            arrayListConstants.PurohithExpertLevel.clear();
                            arrayListConstants.PurohithLocation.clear();
                            arrayListConstants.PurohithPrice.clear();
                            arrayListConstants.PurohithPhoto.clear();*/
                            /*bookPujaActivity.PoojaName.clear();
                            bookPujaActivity.PoojaTypeId.clear();
                            bookPujaActivity.PoojaId.clear();
                            bookPujaActivity.LocationName.clear();
                            bookPujaActivity.LangugesName.clear();
                            bookPujaActivity.PurohithSect.clear();*/

                            ULocality = Locality.getText().toString();
                            UCity = City.getText().toString();
                            UAddress = Address.getText().toString();
                            UFName = FName.getText().toString();
                            ULName = LName.getText().toString();
                            UMobile = Phone.getText().toString();
                            UEmail = Email.getText().toString();
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
            System.out.println("output of bookings : " + constants.SearchPujaId);
            System.out.println("output of bookings : " + constants.purohith_id);
            System.out.println("output of bookings : " + UId);
            System.out.println("output of bookings : " + BookPujaActivity.newdateupdated);
            System.out.println("output of bookings : " + constants.SearchPriceBooking);
            System.out.println("output of bookings : " + ULocality);
            System.out.println("output of bookings : " + BookPujaActivity.package_type);
            System.out.println("output of bookings : " + "Karnataka");
            System.out.println("output of bookings : " + UCity);
            System.out.println("output of bookings : " + UAddress);
            System.out.println("output of bookings : " + constants.package_Name);
            System.out.println("output of bookings : " + UFName+" "+ULName);
            System.out.println("output of bookings : " + constants.package_sect);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(constants.SendBookingDetails);
            try{
                multipartEntity.addPart(constants.Booking1, new StringBody(constants.SearchPujaId));
                multipartEntity.addPart(constants.Booking2,new StringBody(constants.purohith_id));
                multipartEntity.addPart(constants.Booking3,new StringBody(UId));
                multipartEntity.addPart(constants.Booking4,new StringBody(BookPujaActivity.newdateupdated));
                multipartEntity.addPart(constants.Booking5,new StringBody(constants.SearchPriceBooking));
                multipartEntity.addPart(constants.Booking6,new StringBody(ULocality));
                multipartEntity.addPart(constants.Booking7,new StringBody(BookPujaActivity.package_type));
                multipartEntity.addPart(constants.Booking9,new StringBody("India"));
                multipartEntity.addPart(constants.Booking10,new StringBody("Karnataka"));
                multipartEntity.addPart(constants.Booking11,new StringBody(UCity));
                multipartEntity.addPart(constants.Booking12,new StringBody(UAddress));
                multipartEntity.addPart(constants.Booking13,new StringBody(constants.package_Name));
                multipartEntity.addPart(constants.Booking14,new StringBody(UFName+" "+ULName));
                multipartEntity.addPart(constants.Booking8,new StringBody(constants.package_sect));
                multipartEntity.addPart("user_phone",new StringBody(UMobile));
                multipartEntity.addPart("e_mail",new StringBody(UEmail));
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
                JSONArray array = new JSONArray(s);
                for(int r=0;r<array.length();r++){
                    JSONObject object = array.getJSONObject(r);
                    if(object.has("success")){
                        if(object.getString("success")!= null){
                            Success = object.getString("success").toString();
                        }else{
                            Success = "failed";
                        }
                    }else{
                        Success = "failed";
                    }

                    if(object.has("rand_value")){
                        if(object.getString("rand_value")!= null){
                            TransactionId = object.getString("rand_value").toString();
                        }else {
                            TransactionId = "00000";
                        }
                    }else {
                        TransactionId = "00000";
                    }

                }
                if(Success.equals("Sucessfully")){
                    /*insert to local db the booked puja if success*/
                    database.InserBookingDetails(UId, constants.SPujaName, constants.package_Name, constants.SDate, constants.SearchPriceBooking, UAddress, "0");

                }else{
                    Toast.makeText(CheckoutActivityAddress.this,"Failed to book puja please try after some time",Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){

            }
            if(Success.equals("Sucessfully")) {
                ProceedToPayment();
            }
        }
    }

    private void ProceedToPayment() {
        System.out.println("hashSequence"+TransactionId);
        System.out.println("hashSequence"+Amount);
        System.out.println("hashSequence"+FName.getText().toString());
        System.out.println("hashSequence"+Email.getText().toString());
        if (Amount.equals("") || (Double.parseDouble(Amount) == 0.0)) {
            Toast.makeText(getApplicationContext(), "no amount specified", Toast.LENGTH_LONG).show();
        }else if (Double.parseDouble(Amount) > 1000000.00) {
            Toast.makeText(CheckoutActivityAddress.this, "Amount exceeding the limit : 1000000.00 ", Toast.LENGTH_LONG).show();
        }else {
            if (SdkSession.getInstance(CheckoutActivityAddress.this) == null) {
                SdkSession.startPaymentProcess(CheckoutActivityAddress.this, params);
            } else {
                SdkSession.createNewInstance(CheckoutActivityAddress.this);
            }
            String hashSequence = "heauku" + "|" + TransactionId + "|" + constants.PayementGatewayAmount + "|" + "purohit_booking" + "|" + FName.getText().toString() + "|"
                    + Email.getText().toString() + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "d13Ety2U";
            System.out.println("hashSequence" + hashSequence);
            params.put("key", "heauku");
            params.put("MerchantId", "5327426");
            String hash = hashCal(hashSequence);
            Log.i("hash", hash);
            params.put("TxnId", TransactionId);
            params.put("SURL", "https://mobiletest.payumoney.com/mobileapp/payumoney/success.php");
            params.put("FURL", "https://mobiletest.payumoney.com/mobileapp/payumoney/failure.php");
            params.put("ProductInfo", "purohit_booking");
            params.put("firstName", FName.getText().toString());
            params.put("Email", Email.getText().toString());
            params.put("Phone", Phone.getText().toString());
            params.put("Amount", constants.PayementGatewayAmount);
            params.put("hash", hash);
            params.put("udf1", "");
            params.put("udf2", "");
            params.put("udf3", "");
            params.put("udf4", "");
            params.put("udf5", "");
            SdkSession.startPaymentProcess(CheckoutActivityAddress.this, params);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SdkSession.PAYMENT_SUCCESS) {
            if (resultCode == RESULT_OK) {
                Log.i("app_activity", "success");
                Log.i("paymentID", data.getStringExtra("paymentId"));
                Intent intent = new Intent(CheckoutActivityAddress.this, PaymentSuccess.class);
                intent.putExtra(SdkConstants.RESULT, "success");
                intent.putExtra(SdkConstants.PAYMENT_ID, data.getStringExtra("paymentId"));
                startActivity(intent);
                finish();
            }

            if (resultCode == RESULT_CANCELED) {
                Log.i("app_activity", "failure");
                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        Intent intent = new Intent(CheckoutActivityAddress.this, PaymentSuccess.class);
                        intent.putExtra(SdkConstants.RESULT, "failure");
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
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