package vydik.jjbytes.com.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;

import vydik.jjbytes.com.Extras.ConnectionDetector;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.ConfigFile;
import vydik.jjbytes.com.constants.Constants;


/**
 * Created by Manoj on 10/18/2015.
 */
public class BookPujaActivity extends ActionBarActivity implements OnItemSelectedListener{
    private static final String SWITCH_TAB = null;
    public static String package_type = "1",strdate,newdateupdated;
    LinearLayout WithPackage,WithoutPackage;
    Button SubmitSearch;
    Spinner Location,Languages,Sect;
    String SLocation = "0",SLanguage = "0",SSect = "0";
    AutoCompleteTextView PujaName;
    TextView Date;
    Constants constants;
    ArrayListConstants arrayListConstants;
    String StatusOfPuja="false";
    Object content;
    HttpClient client = new DefaultHttpClient();
    CheckBox CheckWithPackage,CheckWithoutPackage;

    /*pooja array list*/
    public static ArrayList<String> PoojaName = new ArrayList<String>();
    public static ArrayList<String> PoojaTypeId = new ArrayList<String>();
    public static ArrayList<String> PoojaId = new ArrayList<String>();
    public static ArrayList<String> LocationName = new ArrayList<String>();
    public static ArrayList<String> LangugesName = new ArrayList<String>();
    public static ArrayList<String> PurohithSect = new ArrayList<String>();
    public static ArrayList<String> PurohithSectId = new ArrayList<String>();

    ArrayAdapter<String> dataAdapterPuja;
    ArrayAdapter<String> dataAdapterLocation;
    ArrayAdapter<String> dataAdapterLanguages;
    ArrayAdapter<String> dataAdapterSect;

    Toolbar toolbar;
    int PositionValue;
    private int ALmonthOfYear, ALdayOfMonth,ALyear;
    String FirstName,LastName;
    Calendar c;
    RadioButton WithPackageRadio,WithoutPackageRadio;

    /*http entity*/
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_puja_activity);
       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            package_type = extras.getString("type");
        }*/

        /*toolbar = (Toolbar) findViewById(R.id.tab_layout);
        toolbar.setNavigationIcon(R.drawable.back_white_new);
        setSupportActionBar(toolbar);*/

        /*PoojaName.clear();
        PoojaTypeId.clear();
        PoojaId.clear();
        LocationName.clear();
        LangugesName.clear();
        PurohithSect.clear();*/

        constants.SPujaName = null;
        newdateupdated = null;

        WithPackage = (LinearLayout) findViewById(R.id.with_package_layout);
        WithoutPackage = (LinearLayout) findViewById(R.id.without_package_layout);
        CheckWithPackage = (CheckBox) findViewById(R.id.with_check_package);
        CheckWithoutPackage = (CheckBox) findViewById(R.id.without_check_package);

        if(CheckWithPackage.isChecked()){
            WithoutPackage.setVisibility(View.GONE);
            WithPackage.setVisibility(View.VISIBLE);
        }
        CheckWithPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckWithPackage.isChecked()){
                    package_type = "1";
                    CheckWithPackage.setChecked(true);
                    CheckWithoutPackage.setEnabled(true);
                    CheckWithoutPackage.setChecked(false);
                    WithoutPackage.setVisibility(View.GONE);
                    WithPackage.setVisibility(View.VISIBLE);
                }
            }
        });

        CheckWithoutPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckWithoutPackage.isChecked()){
                    package_type = "2";
                    CheckWithoutPackage.setChecked(true);
                    CheckWithPackage.setEnabled(true);
                    CheckWithPackage.setChecked(false);
                    WithPackage.setVisibility(View.GONE);
                    WithoutPackage.setVisibility(View.VISIBLE);
                }
            }
        });
        /*if(package_type.equals("1")){
            WithPackage.setVisibility(View.VISIBLE);
        }else{
            WithoutPackage.setVisibility(View.VISIBLE);
        }*/

        SubmitSearch = (Button) findViewById(R.id.submit_search);
        Location = (Spinner) findViewById(R.id.location_list);
        Languages = (Spinner) findViewById(R.id.languages);
        Date = (TextView) findViewById(R.id.date_of_puja);

        Sect = (Spinner) findViewById(R.id.sect);
        Sect.setOnItemSelectedListener(this);

        PujaName = (AutoCompleteTextView) findViewById(R.id.puja_list);
        dataAdapterPuja = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, PoojaName);
        PujaName.setAdapter(dataAdapterPuja);
        PujaName.setThreshold(1);
        PujaName.setTextColor(Color.BLACK);

        /*add first element to array list*/
        //PoojaName.add("Puja");
        LocationName.add("Location");
        LangugesName.add("Languages");
        PurohithSect.add("Sect");

        /*create a data adapter and pars the array list value*/
        //dataAdapterPuja = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, PoojaName);
        dataAdapterLocation = new ArrayAdapter<String>(this,R.layout.spinner_custom_layout,LocationName);
        dataAdapterLanguages = new ArrayAdapter<String>(this,R.layout.spinner_custom_layout,LangugesName);
        dataAdapterSect = new ArrayAdapter<String>(this,R.layout.spinner_custom_layout,PurohithSect);

        /*send the values to drop down*/
        //PujaName.setAdapter(dataAdapterPuja);
        Location.setAdapter(dataAdapterLocation);
        Languages.setAdapter(dataAdapterLanguages);
        Sect.setAdapter(dataAdapterSect);

        /*call for background process to get all puja list*/
        if(ConfigFile.BackgroundProcessCount.equals("0")){
            ConfigFile.BackgroundProcessCount = "1";
            new GetAllPoojaList().execute();
            new getLocalityName().execute();
            new getLanguages().execute();
            new getAllSect().execute();
        }
        /*select listener for add item*/
        PujaName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constants.SPujaName = parent.getItemAtPosition(position).toString();
                for(int i=0;i<PoojaName.size();i++){
                    String pname = PoojaName.get(i).toString();
                    if(constants.SPujaName.equals(pname)){
                        constants.SearchPujaId = PoojaId.get(i).toString();
                        break;
                    }
                }
            }
        });

        /*date picker*/
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerMethod();
            }
        });
        /*button click field validation*/
        SubmitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(constants.SPujaName != null){
                   /* if(Location.getSelectedItem().toString().trim().equals("Location")){
                        SLocation = "1";
                        CheckSpinnerMethod();
                    }else {*/
                        if(newdateupdated != null){
                            CheckSpinnerMethod();
                        }else{
                            Toast.makeText(BookPujaActivity.this,"Please select your date to perform puja",Toast.LENGTH_LONG).show();
                        }
                    //}
                }else{
                    Toast.makeText(BookPujaActivity.this,"Please select your puja",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void DatePickerMethod() {
        c = Calendar.getInstance();
        ALyear = c.get(Calendar.YEAR);
        ALmonthOfYear = c.get(Calendar.MONTH);
        ALdayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ALyear=year;
                        ALmonthOfYear=monthOfYear;
                        ALdayOfMonth=dayOfMonth;
                        strdate = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        newdateupdated = ((monthOfYear + 1) + "/" + dayOfMonth + "/" +year);
                        constants.SDate = strdate;
                        Date.setText(strdate);
                    }
                }, ALyear, ALmonthOfYear, ALdayOfMonth);
        dpd.show();
    }

    /*spinner validation*/
    private void CheckSpinnerMethod() {
        /*if(SLocation.equals("1")){
            SLocation = "0";
            Toast.makeText(BookPujaActivity.this,"Please select your location",Toast.LENGTH_LONG).show();
        }else{*/
            if(ConnectionDetector.isConnectingToInternet(getApplicationContext()))
            {
                /*constants.SLocationName = Location.getSelectedItem().toString();
                constants.SLanguageName = Languages.getSelectedItem().toString();*/
                new SubmitPujaActivity().execute();
            }else {
                Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
            }

        //}
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        final Bundle bundle = new Bundle();
        final Intent intent = new Intent(this,MainActivity.class);
        bundle.putString(SWITCH_TAB, String.valueOf(MainActivity.class)); // Both constants are defined in your code
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.sect:
                PositionValue = position;
                for(int i =1;i<PurohithSect.size();i++){
                    if(i==PositionValue){
                        constants.SSectId = PurohithSectId.get(i-1);
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GetAllPoojaList extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(BookPujaActivity.this, constants.getPoojaListProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet(constants.getPoojaList);
            try{
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                JSONObject top_level_object = new JSONObject(content.toString());
                JSONObject next_level_object = top_level_object.getJSONObject("Puja_lists");
                /*decode json for puja list*/
                JSONArray top_level_pooja_array = next_level_object.getJSONArray("pujas");
                for(int i=0;i<top_level_pooja_array.length();i++){
                    JSONObject object = top_level_pooja_array.getJSONObject(i);
                    if(object.has("puja_name")){
                        if(object.getString("puja_name")!= null){
                            PoojaName.add(object.getString("puja_name").toString());
                        }else{
                            PoojaName.add("not given");
                        }
                    }else{
                        PoojaName.add("not given");
                    }

                    if(object.has("puja_type_id")){
                        if(object.getString("puja_type_id")!= null){
                            PoojaTypeId.add(object.getString("puja_type_id").toString());
                        }else{
                            PoojaTypeId.add("000");
                        }
                    }else{
                        PoojaTypeId.add("000");
                    }

                    if(object.has("puja_id")){
                        if(object.getString("puja_id")!= null){
                            PoojaId.add(object.getString("puja_id").toString());
                        }else{
                            PoojaId.add("000");
                        }
                    }else{
                        PoojaId.add("000");
                    }
                }

                /*decode json for homa*/

                JSONArray top_level_homa_array = next_level_object.getJSONArray("homa");
                for(int j=0;j<top_level_homa_array.length();j++){
                    JSONObject object = top_level_homa_array.getJSONObject(j);
                    if(object.has("puja_name")){
                        if(object.getString("puja_name")!= null){
                            PoojaName.add(object.getString("puja_name").toString());
                        }else{
                            PoojaName.add("not given");
                        }
                    }else{
                        PoojaName.add("not given");
                    }

                    if(object.has("puja_type_id")){
                        if(object.getString("puja_type_id")!= null){
                            PoojaTypeId.add(object.getString("puja_type_id").toString());
                        }else{
                            PoojaTypeId.add("000");
                        }
                    }else{
                        PoojaTypeId.add("000");
                    }

                    if(object.has("puja_id")){
                        if(object.getString("puja_id")!= null){
                           PoojaId.add(object.getString("puja_id").toString());
                        }else{
                            PoojaId.add("000");
                        }
                    }else{
                        PoojaId.add("000");
                    }
                }

                /*decode json for anustana*/

                JSONArray top_level_anusthana_array = next_level_object.getJSONArray("anusthan");
                for(int a=0;a<top_level_anusthana_array.length();a++){
                    JSONObject object = top_level_anusthana_array.getJSONObject(a);

                    if(object.has("puja_name")){
                        if(object.getString("puja_name")!= null){
                            PoojaName.add(object.getString("puja_name").toString());
                        }else{
                            PoojaName.add("not given");
                        }
                    }else{
                        PoojaName.add("not given");
                    }

                    if(object.has("puja_type_id")){
                        if(object.getString("puja_type_id")!= null){
                            PoojaTypeId.add(object.getString("puja_type_id").toString());
                        }else{
                            PoojaTypeId.add("000");
                        }
                    }else{
                        PoojaTypeId.add("000");
                    }

                    if(object.has("puja_id")){
                        if(object.getString("puja_id")!= null){
                            PoojaId.add(object.getString("puja_id").toString());
                        }else{
                            PoojaId.add("000");
                        }
                    }else{
                        PoojaId.add("000");
                    }
                }

                /*decode json for others*/
                JSONArray top_level_other_array = next_level_object.getJSONArray("others");
                for(int o=0;o<top_level_other_array.length();o++){
                    JSONObject object = top_level_other_array.getJSONObject(o);
                    if(object.has("puja_name")){
                        if(object.getString("puja_name")!= null){
                           PoojaName.add(object.getString("puja_name").toString());
                        }else{
                            PoojaName.add("not given");
                        }
                    }else{
                        PoojaName.add("not given");
                    }

                    if(object.has("puja_type_id")){
                        if(object.getString("puja_type_id")!= null){
                            PoojaTypeId.add(object.getString("puja_type_id").toString());
                        }else{
                            PoojaTypeId.add("000");
                        }
                    }else{
                        PoojaTypeId.add("000");
                    }

                    if(object.has("puja_id")){
                        if(object.getString("puja_id")!= null){
                            PoojaId.add(object.getString("puja_id").toString());
                        }else{
                            PoojaId.add("000");
                        }
                    }else{
                        PoojaId.add("000");
                    }
                }
            }
            catch (JSONException e){

            }
            catch (IOException e){

            }
            catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("array list value"+PoojaName);
        }
    }

    /*get locality and pin*/
    private class getLocalityName extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(BookPujaActivity.this, constants.satecityProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet(constants.getLocalityAndPin);
            content = null;
            try{
                HttpResponse response;
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
                            LocationName.add(object.getString("loc_name").toString());
                        }else{
                            LocationName.add("not specifies");
                        }
                    }else{
                        LocationName.add("not specifies");
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
        }
    }

    /*get languages*/
    private class getLanguages extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet(constants.getLanguages);
            content = null;
            try{
                HttpResponse response;
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                JSONObject object1 = new JSONObject(content.toString());
                JSONArray array = object1.getJSONArray("language");
                for(int i=0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.has("name_language"))
                    {
                        if(object.getString("name_language")!= null)
                        {
                            LangugesName.add(object.getString("name_language").toString());
                        }else{
                            LangugesName.add("not given");
                        }
                    }else{
                        LangugesName.add("not given");
                    }

                    if(object.has("language_id")){
                        if(object.getString("language_id")!= null){
                            ArrayListConstants.LanguageId.add(object.getString("language_id").toString());
                        }else{
                            ArrayListConstants.LanguageId.add("not given");
                        }
                    }else{
                        ArrayListConstants.LanguageId.add("not given");
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
        }
    }

    /*get all sect with id to send*/
    private class getAllSect extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet(constants.PurohithSect);
            content = null;
            try{
                HttpResponse response;
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                JSONObject object= new JSONObject(content.toString());
                JSONArray array = object.getJSONArray("sect");
                for(int i=0;i<array.length();i++){
                    JSONObject object1 = array.getJSONObject(i);
                    if(object1.has("sect_name")){
                        if(object1.getString("sect_name")!= null){
                            PurohithSect.add(object1.getString("sect_name").toString());
                        }else{
                            PurohithSect.add("not given");
                        }
                    }else{
                        PurohithSect.add("not given");
                    }

                    if(object1.has("sect_id")){
                        if(object1.getString("sect_id")!= null){
                            PurohithSectId.add(object1.getString("sect_id").toString());
                        }else{
                            PurohithSectId.add("000");
                        }
                    }else{
                        PurohithSectId.add("000");
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
        }
    }

    private class SubmitPujaActivity extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(BookPujaActivity.this, constants.SearchProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("date value :"+newdateupdated);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(constants.SendSearchParameters);
            try{

                multipartEntity.addPart(constants.searchSubmit,new StringBody(constants.submit));
                multipartEntity.addPart(constants.searchPackage,new StringBody(package_type));
                multipartEntity.addPart(constants.searchPujaId,new StringBody(constants.SPujaName));
                multipartEntity.addPart(constants.searchDate,new StringBody(newdateupdated));
                multipartEntity.addPart(constants.searchSectId,new StringBody(constants.SSectId));
                multipartEntity.addPart(constants.searchLanguage,new StringBody(constants.SLanguageName));
                multipartEntity.addPart(constants.searchLocation,new StringBody(constants.SLocationName));

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
            System.out.println("out put of search" + s);
            try {
               /* if (package_type.equals("3")) {
                    JSONObject object = new JSONObject(s);
                    if (object.has("purohit_id")) {
                        if (object.getString("purohit_id") != null) {
                            arrayListConstants.PurohithId.add(object.getString("purohit_id").toString());
                            constants.purohith_id = object.getString("purohit_id").toString();
                        } else {
                            arrayListConstants.PurohithId.add("");
                        }
                    } else {
                        arrayListConstants.PurohithId.add("");
                    }

                    if (object.has("location")) {
                        if (object.getString("location") != null) {
                            arrayListConstants.PurohithLocation.add(object.getString("location").toString());
                            constants.package_location = object.getString("location").toString();
                        } else {
                            arrayListConstants.PurohithLocation.add("no location");
                        }
                    } else {
                        arrayListConstants.PurohithLocation.add("no location");
                    }

                    if (object.has("first_name")) {
                        if (object.getString("first_name") != null) {
                            arrayListConstants.PurohithFirstName.add(object.getString("first_name").toString());
                            FirstName = object.getString("first_name").toString();
                        } else {
                            arrayListConstants.PurohithFirstName.add("no first name");
                        }
                    } else {
                        arrayListConstants.PurohithFirstName.add("no first name");
                    }

                    if (object.has("last_name")) {
                        if (object.getString("last_name") != null) {
                            arrayListConstants.PurohithLastName.add(object.getString("last_name").toString());
                            LastName = object.getString("last_name").toString();
                        } else {
                            arrayListConstants.PurohithLastName.add("np last name");
                        }
                    } else {
                        arrayListConstants.PurohithLastName.add("np last name");
                    }

                    if (object.has("sect")) {
                        if (object.getString("sect") != null) {
                            arrayListConstants.PurohithSectId.add(object.getString("sect").toString());
                            constants.package_sect = object.getString("sect").toString();
                        } else {
                            arrayListConstants.PurohithSectId.add("no sect id");
                        }
                    } else {
                        arrayListConstants.PurohithSectId.add("no sect id");
                    }

                    if (object.has("expert_level")) {
                        if (object.getString("expert_level") != null) {
                            arrayListConstants.PurohithExpertLevel.add(object.getString("expert_level").toString());
                            constants.package_expertise = object.getString("expert_level").toString();
                        } else {
                            arrayListConstants.PurohithExpertLevel.add("no expert level");
                        }
                    } else {
                        arrayListConstants.PurohithExpertLevel.add("no expert level");
                    }

                    if (object.has("schems")) {
                        if (object.getString("schems") != null) {
                            arrayListConstants.PurohithSchem.add(object.getString("schems").toString());
                        } else {
                            arrayListConstants.PurohithSchem.add("no schems");
                        }
                    } else {
                        arrayListConstants.PurohithSchem.add("no schems");
                    }

                    if (object.has("photo_uploaded")) {
                        if (object.getString("photo_uploaded") != null) {
                            arrayListConstants.PurohithPhoto.add(object.getString("photo_uploaded").toString());
                            constants.package_image = object.getString("photo_uploaded").toString();
                        } else {
                            arrayListConstants.PurohithPhoto.add("");
                        }
                    } else {
                        arrayListConstants.PurohithPhoto.add("");
                    }

                    if (object.has("zone")) {
                        if (object.getString("zone") != null) {
                            arrayListConstants.PurohithZone.add(object.getString("zone").toString());
                        } else {
                            arrayListConstants.PurohithZone.add("no zone");
                        }
                    } else {
                        arrayListConstants.PurohithZone.add("no zone");
                    }

                    if (object.has("new_with_price")) {
                        if (object.getString("new_with_price") != null) {
                            int Price = object.getInt("new_with_price");
                            String ConvertedPrice = Integer.toString(Price);
                            constants.package_price = "Rs." + ConvertedPrice + "/-";

                            constants.SearchPriceBooking = ConvertedPrice;
                            arrayListConstants.PurohithPrice.add("Rs." + ConvertedPrice + "/-");
                        } else {

                        }
                    } else {

                    }
                } else {*/
                    JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    if(object.has("status")){
                        if(object.getString("status")!= null){
                            StatusOfPuja = object.getString("status").toString();
                            break;
                        }
                    }
                    if (object.has("purohit_id")) {
                        if (object.getString("purohit_id") != null) {
                            constants.purohith_id = object.getString("purohit_id").toString();
                            arrayListConstants.PurohithId.add(object.getString("purohit_id").toString());
                        } else {
                            arrayListConstants.PurohithId.add("no id");
                        }
                    } else {
                        arrayListConstants.PurohithId.add("no id");
                    }

                    if (object.has("location")) {
                        if (object.getString("location") != null) {
                            arrayListConstants.PurohithLocation.add(object.getString("location").toString());
                            constants.package_location = object.getString("location").toString();
                        } else {
                            arrayListConstants.PurohithLocation.add("no location");
                        }
                    } else {
                        arrayListConstants.PurohithLocation.add("no location");
                    }

                    if (object.has("first_name")) {
                        if (object.getString("first_name") != null) {
                            arrayListConstants.PurohithFirstName.add(object.getString("first_name").toString() + " " + object.getString("last_name").toString());
                            FirstName = object.getString("first_name").toString();
                        } else {
                            arrayListConstants.PurohithFirstName.add("no first name");
                        }
                    } else {
                        arrayListConstants.PurohithFirstName.add("no first name");
                    }

                    if (object.has("last_name")) {
                        if (object.getString("last_name") != null) {
                            arrayListConstants.PurohithLastName.add(object.getString("last_name").toString());
                            LastName = object.getString("last_name").toString();
                        } else {
                            arrayListConstants.PurohithLastName.add("np last name");
                        }
                    } else {
                        arrayListConstants.PurohithLastName.add("np last name");
                    }

                    if (object.has("sect")) {
                        if (object.getString("sect") != null) {
                            arrayListConstants.PurohithSectId.add(object.getString("sect").toString());
                            constants.package_sect = object.getString("sect").toString();
                        } else {
                            arrayListConstants.PurohithSectId.add("");
                        }
                    } else {
                        arrayListConstants.PurohithSectId.add("");
                    }

                    if (object.has("expert_level")) {
                        if (object.getString("expert_level") != null) {
                            arrayListConstants.PurohithExpertLevel.add(object.getString("expert_level").toString());
                            constants.package_expertise = object.getString("expert_level").toString();
                        } else {
                            arrayListConstants.PurohithExpertLevel.add("no expert level");
                        }
                    } else {
                        arrayListConstants.PurohithExpertLevel.add("no expert level");
                    }

                    if (object.has("schems")) {
                        if (object.getString("schems") != null) {
                            arrayListConstants.PurohithSchem.add(object.getString("schems").toString());
                        } else {
                            arrayListConstants.PurohithSchem.add("no schems");
                        }
                    } else {
                        arrayListConstants.PurohithSchem.add("no schems");
                    }

                    if (object.has("puja_photo")) {
                        if (object.getString("photo_uploaded") != null) {
                            arrayListConstants.PurohithPhoto.add("http://www.vydik.com/"+object.getString("puja_photo").toString());
                            constants.package_image = object.getString("puja_photo").toString();
                        } else {
                            arrayListConstants.PurohithPhoto.add("");
                        }
                    } else {
                        arrayListConstants.PurohithPhoto.add("");
                    }

                    if (object.has("zone")) {
                        if (object.getString("zone") != null) {
                            arrayListConstants.PurohithZone.add(object.getString("zone").toString());
                        } else {
                            arrayListConstants.PurohithZone.add("no zone");
                        }
                    } else {
                        arrayListConstants.PurohithZone.add("no zone");
                    }

                    if (object.has("with_out_price")) {
                        if (object.getString("with_out_price") != null) {
                            int Price = object.getInt("with_out_price");
                            String ConvertedPrice = Integer.toString(Price);
                            constants.SearchPriceBooking = ConvertedPrice;
                            constants.package_price = "Rs." + ConvertedPrice + "/-";
                            arrayListConstants.PaymentErrorAdvance .add(ConvertedPrice);
                            arrayListConstants.PurohithPrice.add("Rs." + ConvertedPrice + "/-");
                        } else {

                        }
                    } else {

                    }

                    if (object.has("new_with_price")) {
                        if (object.getString("new_with_price") != null) {
                            int Price = object.getInt("new_with_price");
                            String ConvertedPrice = Integer.toString(Price);
                            constants.SearchPriceBooking = ConvertedPrice;
                            constants.PaymentErrorAdvance = ConvertedPrice;
                            constants.package_price = "Rs." + ConvertedPrice + "/-";
                            arrayListConstants.PurohithPrice.add("Rs." + ConvertedPrice + "/-");
                        } else {

                        }
                    } else {

                    }

                    if(package_type.equals("1")){

                        if(object.has("with_price_advance")){
                            if(object.getString("with_price_advance")!= null){
                                constants.AdvanceAmount = "Rs."+object.getString("with_price_advance").toString()+"/-";
                                constants.PayementGatewayAmount = object.getString("with_price_advance").toString();
                            }else {
                                constants.AdvanceAmount = "000";
                            }
                        }else {
                            constants.AdvanceAmount = "000";
                        }

                        if(object.has("with_price_blance")){
                            if(object.getString("with_price_blance")!= null){
                                constants.BalanceAmount = "Rs."+object.getString("with_price_blance").toString()+"/-";
                            }
                        }
                    }else {
                            if(object.has("with_out_price_advance")){
                                if(object.getString("with_out_price_advance")!= null){
                                    arrayListConstants.AdvanceAmountList.add(object.getString("with_out_price_advance").toString());
                                    System.out.println("advance :"+arrayListConstants.AdvanceAmountList);
                                }else {
                                    arrayListConstants.AdvanceAmountList.add("000");
                                }
                            }else {
                                arrayListConstants.AdvanceAmountList.add("000");
                            }

                            if(object.has("with_out_price_blance")){
                                if(object.getString("with_out_price_blance")!= null){
                                    arrayListConstants.BalanceAmountList.add(object.getString("with_out_price_blance"));
                                }else {
                                    arrayListConstants.BalanceAmountList.add("000");
                                }
                            }else {
                                arrayListConstants.BalanceAmountList.add("000");
                            }
                    }
                }
            //}
            }
            catch (JSONException e){

            }
            catch (Exception e){

            }
            if(package_type.equals("1")) {
                if(StatusOfPuja.equals("success")){
                    constants.package_Name = FirstName+" "+LastName;
                    Intent intent = new Intent(BookPujaActivity.this,PackagePujaDetailActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(BookPujaActivity.this,"No purohit available for the selected details please try with new Thank you",Toast.LENGTH_LONG).show();
                }

            }else {
                if(StatusOfPuja.equals("success")){
                    constants.package_Name = FirstName+" "+LastName;
                    Intent intent = new Intent(BookPujaActivity.this, SearchResultPurohithActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(BookPujaActivity.this,"No purohit available for the selected details please try with new Thank you",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*PoojaName.clear();
        PoojaTypeId.clear();
        PoojaId.clear();
        LocationName.clear();
        LangugesName.clear();
        PurohithSect.clear();
        ConfigFile.BackgroundProcessCount = "0";*/
        Intent intent = new Intent(BookPujaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}