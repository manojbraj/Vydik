package vydik.jjbytes.com.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payUMoney.sdk.SdkConstants;

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

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by user on 12/11/2015.
 */
public class PaymentSuccess extends ActionBarActivity{
    String status;
    LinearLayout SuccessLayout,FailureLayout;
    TextView Point_one,Point_two,Point_three,Point_four;
    Constants constants;
    /*http entity*/
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    String TransactionId = "0000";
    MainDatabase database;
    String RunBackground = "failed";
    Button Submit;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_success_activity);

        database = new MainDatabase(this);
        database = database.open();

        SuccessLayout = (LinearLayout) findViewById(R.id.success_layout);
        FailureLayout = (LinearLayout) findViewById(R.id.failed_transaction);

        Point_one = (TextView) findViewById(R.id.point_one);
        Point_two = (TextView) findViewById(R.id.point_two);
        Point_three = (TextView) findViewById(R.id.point_three);
        Point_four = (TextView) findViewById(R.id.point_four);
        Submit = (Button) findViewById(R.id.Submit_button);

        status = getIntent().getStringExtra(SdkConstants.RESULT);
        SuccessLayout.setVisibility(View.VISIBLE);
        FailureLayout.setVisibility(View.GONE);
        if(status.equals("success")) {
            RunBackground = "complete";
            if(BookPujaActivity.package_type.equals("1")){
                Point_one.setVisibility(View.VISIBLE);
                Point_two.setVisibility(View.VISIBLE);
                Point_three.setVisibility(View.VISIBLE);
                if(Constants.BalanceAmount.equals("000")){
                    Point_four.setVisibility(View.GONE);
                }else {
                    Point_four.setVisibility(View.VISIBLE);
                }
            }else {
                Point_one.setVisibility(View.VISIBLE);
                Point_two.setVisibility(View.GONE);
                Point_three.setVisibility(View.GONE);
                if(Constants.BalanceAmount.equals("000")){
                    Point_four.setVisibility(View.GONE);
                }else {
                    Point_four.setVisibility(View.VISIBLE);
                }
            }
        }else {
            FailureLayout.setVisibility(View.VISIBLE);
            SuccessLayout.setVisibility(View.GONE);
        }



        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RunBackground.equals("complete")){
                    new SubbmitBookingDetails().execute();
                }else {
                    Intent intent = new Intent(PaymentSuccess.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private class SubbmitBookingDetails extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PaymentSuccess.this);
            pDialog.setMessage("Submitting booking details please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            //Utilities.displayProgressDialog(PaymentSuccess.this, constants.BookingPujaProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(constants.SendBookingDetails);
            try{
                multipartEntity.addPart(constants.Booking1, new StringBody(constants.SearchPujaId));
                multipartEntity.addPart(constants.Booking2,new StringBody(constants.purohith_id));
                multipartEntity.addPart(constants.Booking3,new StringBody(CheckoutActivityAddress.UId));
                multipartEntity.addPart(constants.Booking4,new StringBody(BookPujaActivity.newdateupdated));
                multipartEntity.addPart(constants.Booking5,new StringBody(constants.SearchPriceBooking));
                multipartEntity.addPart(constants.Booking6,new StringBody(CheckoutActivityAddress.ULocality));
                multipartEntity.addPart(constants.Booking7,new StringBody(BookPujaActivity.package_type));
                multipartEntity.addPart(constants.Booking9,new StringBody("India"));
                multipartEntity.addPart(constants.Booking10,new StringBody("Karnataka"));
                multipartEntity.addPart(constants.Booking11,new StringBody(CheckoutActivityAddress.UCity));
                multipartEntity.addPart(constants.Booking12,new StringBody(CheckoutActivityAddress.UAddress));
                multipartEntity.addPart(constants.Booking13,new StringBody(constants.package_Name));
                multipartEntity.addPart(constants.Booking14,new StringBody(CheckoutActivityAddress.UFName+" "+CheckoutActivityAddress.ULName));
                multipartEntity.addPart(constants.Booking8,new StringBody(constants.package_sect));
                multipartEntity.addPart("user_phone",new StringBody(CheckoutActivityAddress.UMobile));
                multipartEntity.addPart("e_mail",new StringBody(CheckoutActivityAddress.UEmail));
                multipartEntity.addPart("puja_adv_price",new StringBody(constants.PayementGatewayAmount));
                multipartEntity.addPart("booking_status",new StringBody("1"));
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
            pDialog.dismiss();
            //Utilities.cancelProgressDialog();
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
                    database.InserBookingDetails(CheckoutActivityAddress.UId, constants.SPujaName, constants.package_Name, constants.SDate, constants.SearchPriceBooking, CheckoutActivityAddress.UAddress, "0");
                    Toast.makeText(PaymentSuccess.this, "Thank you for booking puja from vydik.com", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PaymentSuccess.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(PaymentSuccess.this, "Failed to book puja please try after some time", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){

            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(PaymentSuccess.this, "please press ok button thank you..", Toast.LENGTH_LONG).show();
    }
}
