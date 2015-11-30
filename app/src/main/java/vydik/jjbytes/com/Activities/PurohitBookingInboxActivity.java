package vydik.jjbytes.com.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vydik.jjbytes.com.Adapters.PurohithBookingInboxAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.PurohitBookingInboxGetDB;

/**
 * Created by Manoj on 11/6/2015.
 */
public class PurohitBookingInboxActivity extends ActionBarActivity{
    MainDatabase database;
    ListView bookingList;
    ArrayList<PurohitBookingInboxGetDB> purohitBookingInboxGetDB;
    private ArrayList<String> ID = new ArrayList<String>();
    private ArrayList<String> UserPhoneNumber = new ArrayList<String>();
    private ArrayList<String> PujaName = new ArrayList<String>();
    private ArrayList<String> UserLocation = new ArrayList<String>();
    private ArrayList<String> UserAddress = new ArrayList<String>();
    private ArrayList<String> PujaDate = new ArrayList<String>();
    private ArrayList<String> UserName = new ArrayList<String>();
    private ArrayList<String> Date = new ArrayList<String>();
    private ArrayList<String> PurohitId = new ArrayList<String>();
    private ArrayList<String> UserId = new ArrayList<String>();
    PurohithBookingInboxAdapter purohithBookingInboxAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_activity);

        database = new MainDatabase(this);
        database = database.open();
        purohitBookingInboxGetDB = database.getPurohitBookingDetails();

        bookingList = (ListView) findViewById(R.id.list);

        if(purohitBookingInboxGetDB.size() == 0){
            Toast.makeText(PurohitBookingInboxActivity.this, "your inbox is empty", Toast.LENGTH_LONG).show();
        }else{
            Collections.reverse(purohitBookingInboxGetDB);
            for(int i=0;i<purohitBookingInboxGetDB.size();i++){
                ID.add(purohitBookingInboxGetDB.get(i).getId());
                UserPhoneNumber.add(purohitBookingInboxGetDB.get(i).getPhone_number());
                PujaName.add(purohitBookingInboxGetDB.get(i).getPuja_name());
                UserLocation.add(purohitBookingInboxGetDB.get(i).getUser_location());
                UserAddress.add(purohitBookingInboxGetDB.get(i).getUser_address());
                PujaDate.add(purohitBookingInboxGetDB.get(i).getPuja_date());
                UserName.add(purohitBookingInboxGetDB.get(i).getUser_name());
                Date.add(purohitBookingInboxGetDB.get(i).getDate());
                PurohitId.add(purohitBookingInboxGetDB.get(i).getPurohit_it());
                UserId.add(purohitBookingInboxGetDB.get(i).getUser_id());
            }
            LoadMessageList(UserPhoneNumber,PujaName,UserLocation,UserAddress,PujaDate,UserName,Date);
        }
    }

    private void LoadMessageList(ArrayList<String> userPhoneNumber, ArrayList<String> pujaName, ArrayList<String> userLocation,
                                 ArrayList<String> userAddress, ArrayList<String> pujaDate, ArrayList<String> userName,
                                 ArrayList<String> date) {
        purohithBookingInboxAdapter = new PurohithBookingInboxAdapter(PurohitBookingInboxActivity.this,userPhoneNumber,pujaName,
                userLocation,userAddress,pujaDate,userName,date);
        bookingList.setAdapter(purohithBookingInboxAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}