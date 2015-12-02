package vydik.jjbytes.com.Activities;

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
    PurohitRecycAdapter rAdapter;
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

        resList = (RecyclerView) findViewById(R.id.quiz_list);

        PFView = (View) findViewById(R.id.prof_view);
        PJView = (View) findViewById(R.id.puja_view);

        LoadPujaList(arrayListConstants.PujaNameSubscribed,arrayListConstants.PujaWithSamagriSubscribed,arrayListConstants.PujaWithoutSamagriSubscribed,
                arrayListConstants.PujaExpertLevel);
    }

    private void LoadPujaList(ArrayList<String> purohitInfoTwo, ArrayList<String> pujaWithSamagriSubscribed, ArrayList<String> pujaWithoutSamagriSubscribed,
                              ArrayList<String> pujaExpertLevel) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(TestRclass.this);
        resList.setLayoutManager(layoutManager);
        rAdapter = new PurohitRecycAdapter(purohitInfoTwo,pujaWithSamagriSubscribed,pujaWithoutSamagriSubscribed,pujaExpertLevel,TestRclass.this);
        resList.setAdapter(rAdapter);
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