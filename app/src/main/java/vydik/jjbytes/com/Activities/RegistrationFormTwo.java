package vydik.jjbytes.com.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Manoj on 10/13/2015.
 */
public class RegistrationFormTwo extends ActionBarActivity implements OnItemSelectedListener{
    Button Next;
    EditText Address,GuruName;
    Spinner State,City,Locality,Pin,Language;
    CheckBox EmergencyService,Travelling;
    Constants constants;
    TextView SelectedLanguage,language2,language3,language4,language5;
    String StateR = "0",CityR = "0",LocalityR = "0",PinR = "0",LanguageR = "0",selectedLanguageArray;
    ArrayAdapter<String> dataAdapterState;
    ArrayAdapter<String> dataAdapterCity;
    ArrayAdapter<String> dataAdapterLocality;
    ArrayAdapter<String> dataAdapterPinCode;
    ArrayAdapter<String> dataAdapterLanguage;

    /*http entities*/
    Object content;
    HttpClient client = new DefaultHttpClient();
    HttpResponse response;

    int PositionValue;
    String LanguageSelected,lSelectedCount = "0";
    String BackStatus = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pujari_registration_form_two);
        Next = (Button) findViewById(R.id.next_two);

        Address = (EditText) findViewById(R.id.address);

        GuruName = (EditText) findViewById(R.id.guru_name);
        State = (Spinner) findViewById(R.id.state);
        City = (Spinner) findViewById(R.id.city);

        Locality = (Spinner) findViewById(R.id.Locality);
        Locality.setOnItemSelectedListener(this);

        Pin = (Spinner) findViewById(R.id.PinCode);
        Language = (Spinner) findViewById(R.id.language_spinner);
        Language.setOnItemSelectedListener(this);

        EmergencyService = (CheckBox) findViewById(R.id.emergency_service);
        Travelling = (CheckBox) findViewById(R.id.travell_service);

        SelectedLanguage = (TextView) findViewById(R.id.selected_languages);
        language2 = (TextView) findViewById(R.id.language_two);
        language3 = (TextView) findViewById(R.id.language_three);
        language4 = (TextView) findViewById(R.id.language_four);
        language5 = (TextView) findViewById(R.id.language_five);

        //ArrayListConstants.SateName.add("State");
        //ArrayListConstants.CityName.add("City");
        ArrayListConstants.LocalityName.add("Locality");
        ArrayListConstants.PinCode.add("Pin");
        ArrayListConstants.Languages.add("Language");

        //dataAdapterState = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ArrayListConstants.SateName);
        //dataAdapterCity = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayListConstants.CityName);
        dataAdapterLocality = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayListConstants.LocalityName);
        dataAdapterPinCode = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayListConstants.PinCode);
        dataAdapterLanguage = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayListConstants.Languages);

        //City.setAdapter(dataAdapterCity);
        Locality.setAdapter(dataAdapterLocality);
        Pin.setAdapter(dataAdapterPinCode);
        Language.setAdapter(dataAdapterLanguage);

        //new getCityNames().execute();
        new getLocalityName().execute();
        new getLanguages().execute();

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**/
                if (validate(new EditText[]{Address})){
                    if(State.getSelectedItem().toString().trim().equals("State")) {
                        StateR = "1";
                        CheckSpinnerMethod();
                    }else{
                        if(City.getSelectedItem().toString().trim().equals("City")) {
                            CityR = "2";
                            CheckSpinnerMethod();
                        }else{
                            if(Locality.getSelectedItem().toString().trim().equals("Locality")){
                                LocalityR = "3";
                                CheckSpinnerMethod();
                            }else{
                                if(Pin.getSelectedItem().toString().trim().equals("Pin")) {
                                    PinR = "4";
                                    CheckSpinnerMethod();
                                }else{
                                    if(Language.getSelectedItem().toString().trim().equals("Language")) {
                                        LanguageR = "5";
                                        CheckSpinnerMethod();
                                    }else{
                                        CheckSpinnerMethod();
                                    }
                                }
                            }
                        }
                    }
                }else{
                    Toast.makeText(RegistrationFormTwo.this,"Please Make sure you have entered Address and Lead Name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void CheckSpinnerMethod() {
        if(StateR.equals("1")){
            StateR = "0";
            Toast.makeText(RegistrationFormTwo.this,"Please Select State",Toast.LENGTH_LONG).show();
        }else if(CityR.equals("2")){
            CityR = "0";
            Toast.makeText(RegistrationFormTwo.this,"Please Select City",Toast.LENGTH_LONG).show();
        }else if(LocalityR.equals("3")){
            LocalityR = "0";
            Toast.makeText(RegistrationFormTwo.this,"Please Select Locality",Toast.LENGTH_LONG).show();
        }else if(PinR.equals("4")){
            PinR = "0";
            Toast.makeText(RegistrationFormTwo.this,"Please Select PinCode",Toast.LENGTH_LONG).show();
        }else if(LanguageR.equals("5")){
            LanguageR = "0";
            Toast.makeText(RegistrationFormTwo.this,"Please Select atleast one language",Toast.LENGTH_LONG).show();
        }else{
            constants.GetState = State.getSelectedItem().toString();
            constants.GetCity = City.getSelectedItem().toString();
            constants.GetPinCode = Pin.getSelectedItem().toString();
            constants.GetAddress = Address.getText().toString();
            constants.GetGuruName = GuruName.getText().toString();
            if(EmergencyService.isChecked() == false){
                constants.GetEmergencyCheck = "No";
            }else{
                constants.GetEmergencyCheck = "Yes";
            }

            if(Travelling.isChecked() == false){
                constants.GetTravelCheck = "No";
            }else{
                constants.GetTravelCheck = "Yes";
            }
            Intent intent = new Intent(RegistrationFormTwo.this,RegistrationFormThree.class);
            startActivity(intent);
            finish();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.Locality:
                PositionValue = position;
                for(int i =1;i<ArrayListConstants.LocalityName.size();i++){
                    if(i == PositionValue){
                        constants.GetLocality = ArrayListConstants.LocalityName.get(i-1);
                    }
                }
            break;

            case R.id.language_spinner:
                PositionValue = position;
                for(int j=1;j<ArrayListConstants.Languages.size();j++){
                    if(j == PositionValue){
                        constants.GetLanguage = ArrayListConstants.Languages.get(j-1);
                        LanguageSelected = ArrayListConstants.Languages.get(j);
                        ArrayListConstants.LanguageSelected.add(constants.GetLanguage);
                        for(String data : ArrayListConstants.LanguageSelected){
                            selectedLanguageArray += data + ",";
                        }
                        if(LanguageSelected.equals("Language")){
                            Toast.makeText(RegistrationFormTwo.this,"please select a language",Toast.LENGTH_LONG).show();
                        }else if(lSelectedCount.equals("0")){
                            lSelectedCount="1";
                            SelectedLanguage.setText(LanguageSelected);
                        }else if(lSelectedCount.equals("1")){
                            lSelectedCount = "2";
                            language2.setText(LanguageSelected);
                        }else if(lSelectedCount.equals("2")){
                            lSelectedCount = "3";
                            language3.setText(LanguageSelected);
                        }else if(lSelectedCount.equals("3")){
                            lSelectedCount = "4";
                            language4.setText(LanguageSelected);
                        }else if(lSelectedCount.equals("4")){
                            language5.setText(LanguageSelected);
                        }else{

                        }
                        break;
                    }
                }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /*background process*/

    /*get city*/
    private class getCityNames extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(RegistrationFormTwo.this, constants.satecityProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet(constants.getCityNames);
            content = null;
            try{
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                JSONObject object1 = new JSONObject(content.toString());
                JSONArray array = object1.getJSONArray("city");
                for(int i=0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.has("city_name"))
                    {
                        if(object.getString("city_name")!= null)
                        {
                            ArrayListConstants.CityName.add(object.getString("city_name").toString());
                        }else{
                            ArrayListConstants.CityName.add("not specifies");
                        }
                    }else{
                        ArrayListConstants.CityName.add("not specifies");
                    }
                }
            }catch (IOException e){

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

    /*get locality and pin*/
    private class getLocalityName extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(RegistrationFormTwo.this, constants.satecityProgress, false);
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
                            ArrayListConstants.Languages.add(object.getString("name_language").toString());
                        }else{
                            ArrayListConstants.Languages.add("not given");
                        }
                    }else{
                        ArrayListConstants.Languages.add("not given");
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
            Utilities.cancelProgressDialog();
        }
    }

    @Override
    public void onBackPressed() {
        if(BackStatus.equals("0")){
            CallBackPopup();
        }else {
            super.onBackPressed();
            Intent intent = new Intent(RegistrationFormTwo.this, LoginActivity.class);
            intent.putExtra("login_type", "purohit");
            startActivity(intent);
            finish();
        }
    }

    private void CallBackPopup() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.allert_box, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD.setCancelable(false);
        Button cancel = (Button) PromtView.findViewById(R.id.cancel_alert);
        Button finish = (Button) PromtView.findViewById(R.id.finish_activity);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
                BackStatus = "1";
                onBackPressed();
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }
}