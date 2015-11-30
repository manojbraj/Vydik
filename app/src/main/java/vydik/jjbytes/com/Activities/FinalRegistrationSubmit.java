package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import vydik.jjbytes.com.Adapters.FinalPujaSubmitAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 10/14/2015.
 */
public class FinalRegistrationSubmit extends ActionBarActivity{
    FinalPujaSubmitAdapter finalPujaSubmitAdapter;
    ListView pujaList;
    Button SubmitButton;
    Constants constants;
    MultipartEntity nameValuePairList = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    ArrayList<String> PujaSend = new ArrayList<String>();
    String puja = " ";
    String language = " ";
    String priceWithP = " ";
    String priceWithoutP = " ";
    String Expert = " ";
    MainDatabase database;
    /*response get string*/
    String FirstName,LastName,Email,DOB,Phone,Education,Univercity,State,Address,City,ZipCode,GuruName,Location,SPujaName,SPriceWithSamagri,SPriceWithoutPackage,
    SExpertLevel,Languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new MainDatabase(this);
        database = database.open();

        setContentView(R.layout.final_registration_submit);
        pujaList = (ListView) findViewById(R.id.list);
        SubmitButton = (Button) findViewById(R.id.final_submit_button);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SubmitRegistrationForm().execute();
            }
        });
        LoadListView(ArrayListConstants.PujaNameSubscribed, ArrayListConstants.PujaWithSamagriSubscribed, ArrayListConstants.PujaWithoutSamagriSubscribed,
                ArrayListConstants.PujaExpertLevel, ArrayListConstants.PujaType);

    }

    private void LoadListView(ArrayList<String> pujaNameSubscribed, ArrayList<String> pujaWithSamagriSubscribed,
                              ArrayList<String> pujaWithoutSamagriSubscribed, ArrayList<String> pujaExpertLevel,
                              ArrayList<String> pujaType) {
        finalPujaSubmitAdapter = new FinalPujaSubmitAdapter(FinalRegistrationSubmit.this,pujaNameSubscribed,pujaWithSamagriSubscribed,
                pujaWithoutSamagriSubscribed,pujaExpertLevel,pujaType);
        pujaList.setAdapter(finalPujaSubmitAdapter);
    }

    class SubmitRegistrationForm extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(FinalRegistrationSubmit.this, constants.regPurohithProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(constants.submitPujariRegistration);

            try {
                nameValuePairList.addPart(constants.k1,new StringBody(constants.submit));
                nameValuePairList.addPart(constants.k2,new StringBody(constants.FirstNameValue));
                nameValuePairList.addPart(constants.k3,new StringBody(constants.LastNameValue));
                nameValuePairList.addPart(constants.k4,new StringBody(constants.EmailValue));
                nameValuePairList.addPart(constants.k5,new StringBody(constants.PasswordValue));
                nameValuePairList.addPart(constants.k6,new StringBody(constants.DateOfBirthValue));
                nameValuePairList.addPart(constants.k7,new StringBody(constants.PhoneNumberValue));
                nameValuePairList.addPart(constants.k8,new StringBody(constants.EducationValue));
                nameValuePairList.addPart(constants.k9,new StringBody(constants.UnivercityNameValue));
                nameValuePairList.addPart(constants.k10,new StringBody(constants.GetAddress));
                nameValuePairList.addPart(constants.k11,new StringBody(constants.GetCity));
                nameValuePairList.addPart(constants.k12,new StringBody(constants.GetPinCode));
                nameValuePairList.addPart(constants.k13,new StringBody(constants.GetTravelCheck));
                nameValuePairList.addPart(constants.k14,new StringBody(constants.GetGuruName));
                nameValuePairList.addPart(constants.k15,new StringBody(constants.GetEmergencyCheck));
                nameValuePairList.addPart(constants.k16,new StringBody(constants.GetLocality));
                nameValuePairList.addPart(constants.k17,new StringBody(constants.BankName));
                nameValuePairList.addPart(constants.k18,new StringBody(constants.SchemeName));
                nameValuePairList.addPart(constants.k19,new StringBody(constants.BankBranch));
                nameValuePairList.addPart(constants.k20,new StringBody(constants.BankIFSC));
                nameValuePairList.addPart(constants.k21, new StringBody(constants.BankAcNum));

                for(String s : ArrayListConstants.PujaNameSubscribed){
                    puja += s + ",";
                }
                nameValuePairList.addPart(constants.k22,new StringBody(puja));

                for(String l : ArrayListConstants.LanguageSelected){
                    language += l + ",";
                }
                nameValuePairList.addPart(constants.k23,new StringBody(language));

                for(String pw : ArrayListConstants.PujaWithSamagriSubscribed){
                    priceWithP += pw + ",";
                }
                nameValuePairList.addPart(constants.k24,new StringBody(priceWithP));

                for(String pww : ArrayListConstants.PujaWithoutSamagriSubscribed){
                    priceWithoutP += pww + ",";
                }
                nameValuePairList.addPart(constants.k25,new StringBody(priceWithoutP));

                for(String pe : ArrayListConstants.PujaExpertLevel){
                    Expert += pe + ",";
                }
                nameValuePairList.addPart(constants.k26,new StringBody(Expert));

                nameValuePairList.addPart(constants.k27,new StringBody(constants.RefPName));
                nameValuePairList.addPart(constants.k28,new StringBody(constants.RefPNumber));
                nameValuePairList.addPart(constants.k29,new StringBody(constants.RefPEmail));
                nameValuePairList.addPart(constants.k30,new StringBody(constants.RefCName));
                nameValuePairList.addPart(constants.k31,new StringBody(constants.RefCNumber));
                nameValuePairList.addPart(constants.k32,new StringBody(constants.RefCEmail));

                File sourcefile = new File(constants.ImagePath);
                FileBody fileBody = new FileBody(sourcefile);
                nameValuePairList.addPart(constants.k33, fileBody);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try{
                httpPost.setEntity(nameValuePairList);
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
            try{
                JSONObject object = new JSONObject(s);
                String result = object.getString("success_msg").toString();
                /*decode purohit info*/
                if(object.has("firstname")){
                    if(object.getString("firstname")!= null){
                        FirstName = object.getString("firstname").toString();
                    }else{
                        FirstName = " ";
                    }
                }else{
                    FirstName = " ";
                }
                if(object.has("lastname")){
                    if(object.getString("lastname")!= null){
                        LastName = object.getString("lastname").toString();
                    }else{
                        LastName = " ";
                    }
                }else{
                    LastName = " ";
                }
                if(object.has("email")){
                    if(object.getString("email")!= null){
                        Email = object.getString("email").toString();
                    }else{
                        Email = " ";
                    }
                }else{
                    Email = " ";
                }
                if(object.has("dob")){
                    if(object.getString("dob")!= null){
                        DOB = object.getString("dob").toString();
                    }else{
                        DOB = " ";
                    }
                }else{
                    DOB = " ";
                }
                if(object.has("phone")){
                    if(object.getString("phone")!= null){
                        Phone = object.getString("phone").toString();
                    }else{
                        Phone = " ";
                    }
                }else{
                    Phone = " ";
                }
                if(object.has("education")){
                    if(object.getString("education")!= null){
                        Education = object.getString("education").toString();
                    }else {
                        Education = " ";
                    }
                }else {
                    Education = " ";
                }
                if(object.has("university")){
                    if(object.getString("university")!= null){
                        Univercity = object.getString("university").toString();
                    }else {
                        Univercity = " ";
                    }
                }else{
                    Univercity = " ";
                }
                if(object.has("state")){
                    if(object.getString("state")!= null){
                        State = object.getString("state").toString();
                    }else{
                        State = " ";
                    }
                }else{
                    State = " ";
                }
                if(object.has("Address")){
                    if(object.getString("Address")!= null){
                        Address = object.getString("Address").toString();
                    }else {
                        Address = " ";
                    }
                }else {
                    Address = " ";
                }
                if(object.has("city")){
                    if(object.getString("city")!= null){
                        City = object.getString("city").toString();
                    }else{
                        City = " ";
                    }
                }else{
                    City = " ";
                }
                if(object.has("zip_code")){
                    if(object.getString("zip_code")!= null){
                        ZipCode = object.getString("zip_code").toString();
                    }else {
                        ZipCode = " ";
                    }
                }else {
                    ZipCode = " ";
                }
                if(object.has("guruname")){
                    if(object.getString("guruname")!= null){
                        GuruName = object.getString("guruname").toString();
                    }else{
                        GuruName = " ";
                    }
                }else{
                    GuruName = " ";
                }
                if(object.has("location")){
                    if(object.getString("location")!= null){
                        Location = object.getString("location").toString();
                    }else{
                        Location = " ";
                    }
                }else{
                    Location = " ";
                }
                JSONArray pujaArray = object.getJSONArray("perfor_puja");
                for(int i=0;i<pujaArray.length();i++){
                    int len = pujaArray.length();
                    if(len == i){
                      break;
                    }else {
                        SPujaName = (String) pujaArray.get(i);
                        System.out.println("puja names retrieved" + SPujaName + " ArrayLength: " + pujaArray.length());
                    }
                }
                String Image = "add image hear";
                String Type = "purohit";
                if(result.equals("Successfully Registered")){
                    database.insertLogin(constants.FirstNameValue,Image,Type);
                    Intent intent = new Intent(FinalRegistrationSubmit.this,PurohithMainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(FinalRegistrationSubmit.this,"Something went wrong please try after some time",Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(FinalRegistrationSubmit.this,"Please do submit to complete your registration",Toast.LENGTH_LONG).show();
    }
}