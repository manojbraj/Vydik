package vydik.jjbytes.com;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vydik.jjbytes.com.Activities.R;
import vydik.jjbytes.com.Adapters.PurohitProfilePujaListAdapter;
import vydik.jjbytes.com.Adapters.PurohitRecycAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.GetPurohitLanguages;
import vydik.jjbytes.com.Models.GetPurohitPujaList;
import vydik.jjbytes.com.Models.PurohitLoginGet;
import vydik.jjbytes.com.constants.ArrayListConstants;

/**
 * Created by user on 12/2/2015.
 */
public class TestRclass extends ActionBarActivity {
    Toolbar toolbar;
    TextView ProfileView,PujaListView,TName,TPhone,TEmail,TDOB,TEducation,TUniversity,TState,TAddress,TCity,TZipCode,TGuruName,TLocality,TLanguage;
    View PFView,PJView;
    LinearLayout layout_one,layout_two;
    MainDatabase database;
    ArrayList<PurohitLoginGet> PurohitInfoOne = new ArrayList<PurohitLoginGet>();
    public ArrayList<GetPurohitPujaList> PurohitInfoTwo = new ArrayList<GetPurohitPujaList>();
    ArrayList<GetPurohitLanguages> PurohitInfoThree = new ArrayList<GetPurohitLanguages>();
    String FirstName,LastName,Email,DOB,Phone,Education,Univercity,State,Address,City,ZipCode,GuruName,Location,
            SPujaName,SPriceWithSamagri,SPriceWithoutPackage,SExpertLevel,Languages;
    ListView PujaList;
    ArrayListConstants arrayListConstants;
    ArrayList<String> LangArray = new ArrayList<String>();
    PurohitProfilePujaListAdapter adapter;
    CollapsingToolbarLayout actionBar;
    RecyclerView resList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purohit_profile_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        actionBar.setTitle("Purohit Profile");
        setSupportActionBar(toolbar);

        database = new MainDatabase(this);
        database = database.open();
        PurohitInfoOne = database.getPurohitBasicData();
        PurohitInfoTwo = database.getPujaList();
        PurohitInfoThree = database.PurohitLanguages();

        if(PurohitInfoOne.size() == 0){
            Toast.makeText(TestRclass.this, "Failed to retrieve data", Toast.LENGTH_LONG).show();
        }else{
            for(int i=0;i<PurohitInfoOne.size();i++){
                FirstName = PurohitInfoOne.get(i).getfName();
                LastName = PurohitInfoOne.get(i).getlName();
                Email = PurohitInfoOne.get(i).getEmail();
                DOB = PurohitInfoOne.get(i).getDOB();
                Phone = PurohitInfoOne.get(i).getPhone();
                Education = PurohitInfoOne.get(i).getEducation();
                Univercity = PurohitInfoOne.get(i).getUniversity();
                State =PurohitInfoOne.get(i).getState();
                Address = PurohitInfoOne.get(i).getAddress();
                City = PurohitInfoOne.get(i).getCity();
                ZipCode = PurohitInfoOne.get(i).getZipCode();
                GuruName = PurohitInfoOne.get(i).getGuruName();
                Location = PurohitInfoOne.get(i).getLocality();
            }
        }

        if(PurohitInfoTwo.size() == 0){
            Toast.makeText(TestRclass.this, "Failed to retrieve data", Toast.LENGTH_LONG).show();
        }else {
            for(int p=0;p<PurohitInfoTwo.size();p++){
                SPujaName = PurohitInfoTwo.get(p).getPujaName();
                SPriceWithSamagri = PurohitInfoTwo.get(p).getPujaWithPackage();
                SPriceWithoutPackage = PurohitInfoTwo.get(p).getPujaWithoutPackage();
                SExpertLevel = PurohitInfoTwo.get(p).getExpertLevel();

                arrayListConstants.PujaNameSubscribed.add(SPujaName);
                arrayListConstants.PujaWithSamagriSubscribed.add(SPriceWithSamagri);
                arrayListConstants.PujaWithoutSamagriSubscribed.add(SPriceWithoutPackage);
                arrayListConstants.PujaExpertLevel.add(SExpertLevel);
            }
        }

        if(PurohitInfoThree.size() == 0){

        }else {
            for(int j=0;j<PurohitInfoThree.size();j++){
                String Languages1 = PurohitInfoThree.get(j).getLanguages();
                Languages += Languages1 + "  ";
            }
        }

        ProfileView = (TextView) findViewById(R.id.profile_view_nested);
        PujaListView = (TextView) findViewById(R.id.puja_list_nested);
        TName = (TextView) findViewById(R.id.pur_name);
        TPhone = (TextView) findViewById(R.id.pur_phone);
        TEmail = (TextView) findViewById(R.id.pur_email);
        TDOB = (TextView) findViewById(R.id.pur_dob);
        TEducation = (TextView) findViewById(R.id.pur_education);
        TUniversity = (TextView) findViewById(R.id.pur_university);
        TState = (TextView) findViewById(R.id.pur_state);
        TAddress = (TextView) findViewById(R.id.pur_address);
        TCity = (TextView) findViewById(R.id.pur_city);
        TZipCode = (TextView) findViewById(R.id.pur_zip);
        TGuruName = (TextView)findViewById(R.id.pur_guruname);
        TLocality = (TextView) findViewById(R.id.pur_locality);
        TLanguage = (TextView) findViewById(R.id.pur_languages);

        PujaList = (ListView) findViewById(R.id.list);
        resList = (RecyclerView) findViewById(R.id.quiz_list);
        //PujaList.setScrollContainer(false);

        TName.setText(FirstName+" "+LastName);
        TPhone.setText(Phone);
        TEmail.setText(Email);
        TDOB.setText(DOB);
        TAddress.setText(Address);
        TLocality.setText(Location);
        TCity.setText(City);
        TState.setText(State);
        TZipCode.setText(ZipCode);
        TEducation.setText(Education);
        TUniversity.setText(Univercity);
        TGuruName.setText(GuruName);
        TLanguage.setText(Languages);

        PFView = (View) findViewById(R.id.prof_view);
        PJView = (View) findViewById(R.id.puja_view);

        layout_one = (LinearLayout) findViewById(R.id.layout_nested_profile);
        layout_two = (LinearLayout) findViewById(R.id.layout_nested_puja);

        LoadPujaList(PurohitInfoTwo);

        ProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PFView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                PJView.setBackgroundColor(getResources().getColor(R.color.gray));
                layout_one.setVisibility(View.VISIBLE);
                layout_two.setVisibility(View.GONE);
            }
        });

        PujaListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PJView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                PFView.setBackgroundColor(getResources().getColor(R.color.gray));
                layout_two.setVisibility(View.VISIBLE);
                resList.setVisibility(View.VISIBLE);
                PujaList.setVisibility(View.GONE);
                layout_one.setVisibility(View.GONE);
            }
        });
    }

    private void LoadPujaList(ArrayList<GetPurohitPujaList> purohitInfoTwo) {
        resList.setLayoutManager(new LinearLayoutManager(this));
        //resList.setAdapter(new PurohitRecycAdapter(new TestRclass().PurohitInfoTwo()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        arrayListConstants.PujaNameSubscribed.clear();
        arrayListConstants.PujaWithSamagriSubscribed.clear();
        arrayListConstants.PujaWithoutSamagriSubscribed.clear();
        arrayListConstants.PujaExpertLevel.clear();
    }
}
