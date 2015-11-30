package vydik.jjbytes.com.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import vydik.jjbytes.com.Extras.ConnectionDetector;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by manoj on 10/15/2015.
 */
public class RegistrationFormThree extends ActionBarActivity implements OnItemSelectedListener {
    Button SubmitBankDetails;
    ImageView RCN,RCP,RCE,RPN,RPP,RPE;
    EditText ifscCode,accountNumber,RefePN,RefePP,RefePE,RefeCN,RefeCP,RefeCE;
    Spinner BankName,Schemes,branchName;
    String BankR = "0",SchemesR = "0" ;
    Constants constants;

    ArrayAdapter<String> ABName;
    ArrayAdapter<String> ABBranch;

    /*http entities*/
    Object content;
    HttpClient client = new DefaultHttpClient();
    HttpResponse response;

    int PositionValue;
    String BackStatus = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form_three);

        SubmitBankDetails = (Button) findViewById(R.id.pujari_reference);

        RCN = (ImageView) findViewById(R.id.rcn_image);
        RCP = (ImageView) findViewById(R.id.rcp_image);
        RCE = (ImageView) findViewById(R.id.rce_image);
        RPN = (ImageView) findViewById(R.id.rpn_image);
        RPP = (ImageView) findViewById(R.id.rpp_image);
        RPE = (ImageView) findViewById(R.id.rpe_image);

        ifscCode = (EditText) findViewById(R.id.ifsc_code);
        accountNumber = (EditText) findViewById(R.id.acount_number);
        RefePN = (EditText) findViewById(R.id.ref_p_name);
        RefePP = (EditText) findViewById(R.id.ref_p_number);
        RefePE = (EditText) findViewById(R.id.ref_p_email);
        RefeCN = (EditText) findViewById(R.id.ref_c_name);
        RefeCP = (EditText) findViewById(R.id.ref_c_number);
        RefeCE = (EditText) findViewById(R.id.ref_c_email);

        BankName = (Spinner) findViewById(R.id.bank_spinner);
        BankName.setOnItemSelectedListener(this);

        Schemes = (Spinner) findViewById(R.id.schemes_spinner);

        branchName = (Spinner) findViewById(R.id.branch_name);
        branchName.setOnItemSelectedListener(this);

        ArrayListConstants.BankNames.add("Bank");
        ArrayListConstants.BankBranch.add("Select Branch Name");

        ABName = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayListConstants.BankNames);
        ABBranch = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayListConstants.BankBranch);

        BankName.setAdapter(ABName);
        branchName.setAdapter(ABBranch);

        new getBankName().execute();

        SubmitBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(new EditText[]{ifscCode, accountNumber})) {
                    if (BankName.getSelectedItem().toString().trim().equals("Bank")) {
                        BankR = "1";
                        CheckSpinnerMethod();
                    } else {
                        if (Schemes.getSelectedItem().toString().trim().equals("Schemes")) {
                            SchemesR = "2";
                            CheckSpinnerMethod();
                        } else {
                            if (branchName.getSelectedItem().toString().trim().equals("Select Branch Name")) {
                                SchemesR = "3";
                                CheckSpinnerMethod();
                            } else {
                                CheckSpinnerMethod();
                            }
                        }
                    }
                } else {
                    Toast.makeText(RegistrationFormThree.this, "Please Make sure you have entered your Account details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void CheckSpinnerMethod() {
        if(BankR.equals("1")){
            BankR = "0";
            Toast.makeText(RegistrationFormThree.this, "Please Select your Bank name", Toast.LENGTH_LONG).show();
        }else if(SchemesR.equals("2")){
            SchemesR = "0";
            Toast.makeText(RegistrationFormThree.this, "Please Select your Schemes", Toast.LENGTH_LONG).show();
        }else{
            constants.BankName = BankName.getSelectedItem().toString();
            constants.SchemeName = Schemes.getSelectedItem().toString();
            constants.BankIFSC = ifscCode.getText().toString();
            constants.BankAcNum = accountNumber.getText().toString();

            if(ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
                Intent intent = new Intent(RegistrationFormThree.this, RegistrationPoojaActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.branch_name:
                PositionValue = position;
                for(int i =1;i<ArrayListConstants.BankBranch.size();i++){
                    if(i == PositionValue){
                        constants.BankBranch = ArrayListConstants.BankBranchId.get(i-1);
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*background process*/

    /*bank name and branch name with id*/
    private class getBankName extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(RegistrationFormThree.this, constants.getingBankDetails, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet(constants.getBankNameAndBranchName);
            content = null;
            try{
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                JSONObject object1 = new JSONObject(content.toString());
                JSONArray array = object1.getJSONArray("bank");
                for(int i=0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.has("Bank_name"))
                    {
                        if(object.getString("Bank_name")!= null)
                        {
                            ArrayListConstants.BankNames.add(object.getString("Bank_name").toString());
                        }else{
                            ArrayListConstants.BankNames.add("not specifies");
                        }
                    }else{
                        ArrayListConstants.BankNames.add("not specifies");
                    }

                    if(object.has("branch_name")){
                        if(object.getString("branch_name")!= null){
                            ArrayListConstants.BankBranch.add(object.getString("branch_name").toString());
                        }else{
                            ArrayListConstants.BankBranch.add("not specifies");
                        }
                    }else{
                        ArrayListConstants.BankBranch.add("not specifies");
                    }

                    if(object.has("bank_id")){
                        if(object.getString("bank_id")!= null){
                            ArrayListConstants.BankBranchId.add(object.getString("bank_id").toString());
                        }else{
                            ArrayListConstants.BankBranchId.add("not specifies");
                        }
                    }else{
                        ArrayListConstants.BankBranchId.add("not specifies");
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
            Utilities.cancelProgressDialog();
        }
    }

    @Override
    public void onBackPressed() {
        if(BackStatus.equals("0")){
            CallBackPopup();
        }else {
            super.onBackPressed();
            Intent intent = new Intent(RegistrationFormThree.this, LoginActivity.class);
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
