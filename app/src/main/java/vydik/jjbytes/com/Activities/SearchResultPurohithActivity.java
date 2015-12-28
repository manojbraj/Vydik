package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vydik.jjbytes.com.Adapters.SearchResultAdapter;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 10/20/2015.
 */
public class  SearchResultPurohithActivity extends ActionBarActivity {
    TextView SearchPujaName,ServerErrorMessage;
    ListView SearchPurohithdetails;
    ArrayListConstants arrayListConstants;
    BookPujaActivity bookPujaActivity;
    Constants constants;
    SearchResultAdapter searchResultAdapter;
    public static String PurohithId,PurohithLocation,PujaPrice,PurohitExpertLevel;
    public static String PurohithName;
    Object content;
    HttpClient client = new DefaultHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pujari_search_result_activity);

        SearchPujaName = (TextView) findViewById(R.id.search_puja_name);
        SearchPujaName.setText(constants.SPujaName);
        SearchPurohithdetails = (ListView) findViewById(R.id.purohith_list);
        ServerErrorMessage = (TextView) findViewById(R.id.error_message);

        if(constants.SearchMessage.equals("null")){

        }else {
            ServerErrorMessage.setText(constants.SearchMessage);
        }
        LoadListView(arrayListConstants.PurohithFirstName,arrayListConstants.PurohithExpertLevel,arrayListConstants.PurohithLocation,
                arrayListConstants.PurohithPrice,arrayListConstants.PurohithPhoto);
    }

    private void LoadListView(ArrayList<String> purohithFirstName, ArrayList<String> purohithExpertLevel,
                              ArrayList<String> purohithLocation, ArrayList<String> purohithPrice, ArrayList<String> purohithPhoto) {
        searchResultAdapter = new SearchResultAdapter(SearchResultPurohithActivity.this,purohithFirstName,purohithExpertLevel,purohithLocation,purohithPrice,purohithPhoto);
        SearchPurohithdetails.setAdapter(searchResultAdapter);
        SearchPurohithdetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                constants.purohith_id = arrayListConstants.PurohithId.get(position);
                PurohithId = arrayListConstants.PurohithId.get(position);
                PurohithLocation = arrayListConstants.PurohithLocation.get(position);
                PujaPrice = arrayListConstants.PurohithPrice.get(position);
                PurohitExpertLevel = arrayListConstants.PurohithExpertLevel.get(position);
                String fName = arrayListConstants.PurohithFirstName.get(position);
                String lName = arrayListConstants.PurohithLastName.get(position);
                constants.AdvanceAmount = "Rs."+arrayListConstants.AdvanceAmountList.get(position)+"/-";
                System.out.println("amount advance :"+constants.AdvanceAmount);
                if(constants.AdvanceAmount.equals("Rs.000/-")){
                    constants.AdvanceAmount = arrayListConstants.PurohithPrice.get(position);
                    constants.PayementGatewayAmount = arrayListConstants.PaymentErrorAdvance.get(position);
                    System.out.println("payment 2"+constants.PayementGatewayAmount);
                }else {
                    constants.AdvanceAmount = "Rs."+arrayListConstants.AdvanceAmountList.get(position)+"/-";
                    constants.PayementGatewayAmount = arrayListConstants.AdvanceAmountList.get(position);
                    System.out.println("payment 1"+constants.PayementGatewayAmount);
                }
                constants.BalanceAmount = "Rs."+arrayListConstants.BalanceAmountList.get(position)+"/-";

                PurohithName = fName;
                constants.package_Name = PurohithName;
                new PurohithDetail().execute();
            }
        });
    }

    private class PurohithDetail extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(SearchResultPurohithActivity.this, constants.PurohithDetails, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet(constants.PurohithDetailUrl+PurohithId);
            content = null;
            try{
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                System.out.println("pert details : "+content);
                JSONArray array = new JSONArray(content.toString());
                for(int i=0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.has("education")){
                        if(object.getString("education")!= null){
                            constants.Education = object.getString("education").toString();
                        }else {
                            constants.Education = " ";
                        }
                    }else{
                        constants.Education = " ";
                    }
                    if(object.has("sect")){
                        if(object.getString("sect")!= null){
                            constants.Sect = object.getString("sect").toString();
                        }else{
                            constants.Sect = " ";
                        }
                    }else{
                        constants.Sect = " ";
                    }

                    if(object.has("university")){
                        if(object.getString("university")!= null){
                            constants.Univercity = object.getString("university").toString();
                        }else{
                            constants.Univercity = " ";
                        }
                    }else{
                        constants.Univercity = " ";
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
            Intent intent = new Intent(SearchResultPurohithActivity.this,PurohithDetailsActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        arrayListConstants.PurohithFirstName.clear();
        arrayListConstants.PurohithExpertLevel.clear();
        arrayListConstants.PurohithLocation.clear();
        arrayListConstants.PurohithPrice.clear();
        arrayListConstants.PurohithPhoto.clear();
        arrayListConstants.AdvanceAmountList.clear();
        arrayListConstants.BalanceAmountList.clear();
        arrayListConstants.PaymentErrorAdvance.clear();
        BookPujaActivity.package_type = "1";
        BookPujaActivity.newdateupdated ="";
        constants.BalanceAmount="000";
        /*bookPujaActivity.PoojaName.clear();
        bookPujaActivity.PoojaTypeId.clear();
        bookPujaActivity.PoojaId.clear();
        bookPujaActivity.LocationName.clear();
        bookPujaActivity.LangugesName.clear();
        bookPujaActivity.PurohithSect.clear();*/
        Intent intent = new Intent(SearchResultPurohithActivity.this, BookPujaActivity.class);
        intent.putExtra("type",BookPujaActivity.package_type);
        startActivity(intent);
        finish();
    }
}