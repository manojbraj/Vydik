package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
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
import java.util.ArrayList;

import vydik.jjbytes.com.Adapters.AnustanaListAdapter;
import vydik.jjbytes.com.Adapters.HomaListAdapter;
import vydik.jjbytes.com.Adapters.OthersPoojaListAdapter;
import vydik.jjbytes.com.Adapters.PoojaListAdapter;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by manoj on 10/10/2015.
 */
public class RegistrationPoojaActivity extends ActionBarActivity {

    private ListView ls1;
    private ListView ls2;
    private ListView ls3;
    private ListView ls4;

    private TabHost myTabHost;
    Constants constants;

    Object content;
    HttpClient client = new DefaultHttpClient();
    /*pooja array list*/
    public static ArrayList<String> PoojaName = new ArrayList<String>();
    public static ArrayList<String> PoojaTypeId = new ArrayList<String>();
    public static ArrayList<String> PoojaId = new ArrayList<String>();
    String ModelPooja,ModelId;
    public static String SelectedPosition;

    /*homa array list*/
    private ArrayList<String> HomaName = new ArrayList<String>();
    private ArrayList<String> HomaTypeId = new ArrayList<String>();
    private ArrayList<String> HomaId = new ArrayList<String>();

    /*anustana array list*/
    private ArrayList<String> AnustanaName = new ArrayList<String>();
    private ArrayList<String> AnustanaTypeId = new ArrayList<String>();
    private ArrayList<String> AnustanaId = new ArrayList<String>();

    /*others array list*/
    private ArrayList<String> OthersName = new ArrayList<String>();
    private ArrayList<String> OthersTypeId = new ArrayList<String>();
    private ArrayList<String> OtherId = new ArrayList<String>();

    PoojaListAdapter poojaListAdapter;
    HomaListAdapter homaListAdapter;
    AnustanaListAdapter anustanaListAdapter;
    OthersPoojaListAdapter othersPoojaListAdapter;
    TabHost.TabSpec ts1,ts2,ts3,ts4;

    public static ListView pujaList,homaList,anustanaList,otherList;
    Button PujaButton,HomaButton,AnustanaButton,OtherButton;
    View pujaView,homaView,anustanaView,otherView;
    LinearLayout pujaLayout,homaLayout,anustanaLayout,otherLayout;

    int pujaCount = 0,homaCount = 0,anustanaCount = 0,othersCount = 0;

    Toolbar toolbar;
    ImageView subbmit_image;
    TextView submit_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pooja_list_frames);

        /*tool reference and calling tool bar view reference*/
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        subbmit_image = (ImageView) toolbar.findViewById(R.id.submit_button);
        submit_text = (TextView) toolbar.findViewById(R.id.submit_text);

        subbmit_image.setVisibility(View.VISIBLE);
        submit_text.setVisibility(View.VISIBLE);
        /*on click tool bar submit*/
        subbmit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = ArrayListConstants.PujaNameSubscribed.size();
                if(size == 0){
                    Toast.makeText(RegistrationPoojaActivity.this,"Please add atlease one puja and proceed",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(RegistrationPoojaActivity.this, FinalRegistrationSubmit.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        pujaList = (ListView) findViewById(R.id.puja_list);
        homaList = (ListView) findViewById(R.id.homa_list);
        anustanaList = (ListView) findViewById(R.id.anustana_list);
        otherList = (ListView) findViewById(R.id.others_list);

        PujaButton = (Button) findViewById(R.id.puja_button);
        HomaButton = (Button) findViewById(R.id.homa_button);
        AnustanaButton = (Button) findViewById(R.id.anustana_button);
        OtherButton = (Button) findViewById(R.id.other_button);

        pujaView = (View) findViewById(R.id.puja_view);
        homaView = (View) findViewById(R.id.homa_view);
        anustanaView = (View) findViewById(R.id.anustana_view);
        otherView = (View) findViewById(R.id.other_view);

        pujaLayout = (LinearLayout) findViewById(R.id.puja_list_layout);
        homaLayout = (LinearLayout) findViewById(R.id.homa_list_layout);
        anustanaLayout = (LinearLayout) findViewById(R.id.anustana_list_layout);
        otherLayout = (LinearLayout) findViewById(R.id.other_list_layout);

        PujaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pujaView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                homaView.setBackgroundColor(getResources().getColor(R.color.gray));
                anustanaView.setBackgroundColor(getResources().getColor(R.color.gray));
                otherView.setBackgroundColor(getResources().getColor(R.color.gray));
                pujaLayout.setVisibility(View.VISIBLE);
                homaLayout.setVisibility(View.GONE);
                anustanaLayout.setVisibility(View.GONE);
                otherLayout.setVisibility(View.GONE);
                if(pujaCount == 0){
                    pujaCount = 1;
                    LoadListViewPuja(PoojaName, PoojaTypeId, PoojaId);
                }else{

                }
            }
        });

        HomaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pujaView.setBackgroundColor(getResources().getColor(R.color.gray));
                homaView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                anustanaView.setBackgroundColor(getResources().getColor(R.color.gray));
                otherView.setBackgroundColor(getResources().getColor(R.color.gray));
                homaLayout.setVisibility(View.VISIBLE);
                pujaLayout.setVisibility(View.GONE);
                anustanaLayout.setVisibility(View.GONE);
                otherLayout.setVisibility(View.GONE);
                if(homaCount == 0){
                    homaCount = 1;
                    LoadListViewHoma(HomaName, HomaTypeId, HomaId);
                }
            }
        });

        AnustanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pujaView.setBackgroundColor(getResources().getColor(R.color.gray));
                homaView.setBackgroundColor(getResources().getColor(R.color.gray));
                anustanaView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                otherView.setBackgroundColor(getResources().getColor(R.color.gray));
                anustanaLayout.setVisibility(View.VISIBLE);
                pujaLayout.setVisibility(View.GONE);
                homaLayout.setVisibility(View.GONE);
                otherLayout.setVisibility(View.GONE);
                if(anustanaCount == 0){
                    anustanaCount = 1;
                    LoadListViewAnustana(AnustanaName,AnustanaTypeId,AnustanaId);
                }
            }
        });

        OtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pujaView.setBackgroundColor(getResources().getColor(R.color.gray));
                homaView.setBackgroundColor(getResources().getColor(R.color.gray));
                anustanaView.setBackgroundColor(getResources().getColor(R.color.gray));
                otherView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                otherLayout.setVisibility(View.VISIBLE);
                pujaLayout.setVisibility(View.GONE);
                homaLayout.setVisibility(View.GONE);
                anustanaLayout.setVisibility(View.GONE);
                if(othersCount == 0){
                    othersCount = 1;
                    LoadListViewOther(OthersName,OthersTypeId,OtherId);
                }
            }
        });
        new GetAllPoojaList().execute();
    }

    private class GetAllPoojaList extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(RegistrationPoojaActivity.this, constants.getPoojaListProgress, false);
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
                            ModelPooja = object.getString("puja_name").toString();
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
                            ModelId = object.getString("puja_id").toString();
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
                            HomaName.add(object.getString("puja_name").toString());
                        }else{
                            HomaName.add("not given");
                        }
                    }else{
                        HomaName.add("not given");
                    }

                    if(object.has("puja_type_id")){
                        if(object.getString("puja_type_id")!= null){
                            HomaTypeId.add(object.getString("puja_type_id").toString());
                        }else{
                            HomaTypeId.add("000");
                        }
                    }else{
                        HomaTypeId.add("000");
                    }

                    if(object.has("puja_id")){
                        if(object.getString("puja_id")!= null){
                            HomaId.add(object.getString("puja_id").toString());
                        }else{
                            HomaId.add("000");
                        }
                    }else{
                        HomaId.add("000");
                    }
                }

                /*decode json for anustana*/

                JSONArray top_level_anusthana_array = next_level_object.getJSONArray("anusthan");
                for(int a=0;a<top_level_anusthana_array.length();a++){
                    JSONObject object = top_level_anusthana_array.getJSONObject(a);

                    if(object.has("puja_name")){
                        if(object.getString("puja_name")!= null){
                            AnustanaName.add(object.getString("puja_name").toString());
                        }else{
                            AnustanaName.add("not given");
                        }
                    }else{
                        AnustanaName.add("not given");
                    }

                    if(object.has("puja_type_id")){
                        if(object.getString("puja_type_id")!= null){
                            AnustanaTypeId.add(object.getString("puja_type_id").toString());
                        }else{
                            AnustanaTypeId.add("000");
                        }
                    }else{
                        AnustanaTypeId.add("000");
                    }

                    if(object.has("puja_id")){
                        if(object.getString("puja_id")!= null){
                            AnustanaId.add(object.getString("puja_id").toString());
                        }else{
                            AnustanaId.add("000");
                        }
                    }else{
                        AnustanaId.add("000");
                    }
                }

                /*decode json for others*/
                JSONArray top_level_other_array = next_level_object.getJSONArray("others");
                for(int o=0;o<top_level_other_array.length();o++){
                    JSONObject object = top_level_other_array.getJSONObject(o);
                    if(object.has("puja_name")){
                        if(object.getString("puja_name")!= null){
                            OthersName.add(object.getString("puja_name").toString());
                        }else{
                            OthersName.add("not given");
                        }
                    }else{
                        OthersName.add("not given");
                    }

                    if(object.has("puja_type_id")){
                        if(object.getString("puja_type_id")!= null){
                            OthersTypeId.add(object.getString("puja_type_id").toString());
                        }else{
                            OthersTypeId.add("000");
                        }
                    }else{
                        OthersTypeId.add("000");
                    }

                    if(object.has("puja_id")){
                        if(object.getString("puja_id")!= null){
                            OtherId.add(object.getString("puja_id").toString());
                        }else{
                            OtherId.add("000");
                        }
                    }else{
                        OtherId.add("000");
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
            Utilities.cancelProgressDialog();
            if(pujaCount == 0) {
                pujaCount = 1;
                LoadListViewPuja(PoojaName, PoojaTypeId, PoojaId);
            }
        }
    }

    private void LoadListViewPuja(final ArrayList<String> poojaName, ArrayList<String> poojaTypeId, ArrayList<String> poojaId) {
        poojaListAdapter = new PoojaListAdapter(RegistrationPoojaActivity.this, poojaName, poojaTypeId, poojaId);
        pujaList.setAdapter(poojaListAdapter);
    }

    private void LoadListViewHoma(final ArrayList<String> poojaName, ArrayList<String> poojaTypeId, ArrayList<String> poojaId) {
        homaListAdapter = new HomaListAdapter(RegistrationPoojaActivity.this, poojaName, poojaTypeId, poojaId);
        homaList.setAdapter(homaListAdapter);
    }

    private void LoadListViewAnustana(final ArrayList<String> poojaName, ArrayList<String> poojaTypeId, ArrayList<String> poojaId) {
        anustanaListAdapter = new AnustanaListAdapter(RegistrationPoojaActivity.this, poojaName, poojaTypeId, poojaId);
        anustanaList.setAdapter(anustanaListAdapter);
    }

    private void LoadListViewOther(final ArrayList<String> poojaName, ArrayList<String> poojaTypeId, ArrayList<String> poojaId) {
        othersPoojaListAdapter = new OthersPoojaListAdapter(RegistrationPoojaActivity.this, poojaName, poojaTypeId, poojaId);
        otherList.setAdapter(othersPoojaListAdapter);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(RegistrationPoojaActivity.this,"Please select your puja and complete your registration",Toast.LENGTH_LONG).show();
    }
}