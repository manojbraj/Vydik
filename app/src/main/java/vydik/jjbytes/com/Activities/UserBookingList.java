package vydik.jjbytes.com.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vydik.jjbytes.com.Adapters.UserPurohitBookingAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.UserPurohitBookingGet;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 11/16/2015.
 */
public class UserBookingList extends ActionBarActivity{
    ListView bookingList;
    MainDatabase database;
    ArrayList<UserPurohitBookingGet> userPurohitBookingGet;
    public static ArrayList<String> PujaId = new ArrayList<String>();
    ArrayList<String> PujaName = new ArrayList<String>();
    ArrayList<String> PurohitName = new ArrayList<String>();
    ArrayList<String> BookingDate = new ArrayList<String>();
    ArrayList<String> Price = new ArrayList<String>();
    ArrayList<String> Address = new ArrayList<String>();
    public static ArrayList<String> FlagValue = new ArrayList<String>();
    Constants constants;

    UserPurohitBookingAdapter userPurohitBookingAdapter;
    Toolbar toolbar;
    TextView label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_booking_activity);

        database = new MainDatabase(this);
        database = database.open();
        userPurohitBookingGet = database.getUserPurohitBooking(constants.UserIdData);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        label = (TextView) toolbar.findViewById(R.id.tool_bar_name);
        label.setText("Booking Details");

        bookingList = (ListView) findViewById(R.id.list);

        if(userPurohitBookingGet.size() == 0){
            Toast.makeText(UserBookingList.this,"You havn't booked a Purohit yet",Toast.LENGTH_LONG).show();
        }else {
            for(int i=0;i<userPurohitBookingGet.size();i++){
                PujaId.add(userPurohitBookingGet.get(i).getId());
                PujaName.add(userPurohitBookingGet.get(i).getPujaname());
                PurohitName.add(userPurohitBookingGet.get(i).getPurohitname());
                BookingDate.add(userPurohitBookingGet.get(i).getData());
                Price.add(userPurohitBookingGet.get(i).getPrice());
                Address.add(userPurohitBookingGet.get(i).getAddress());
                FlagValue.add(userPurohitBookingGet.get(i).getFlagvalue());
            }
            loadListView(PujaName,PurohitName,BookingDate,Price,Address,FlagValue);
        }
    }

    private void loadListView(ArrayList<String> pujaName, ArrayList<String> purohitName, ArrayList<String> bookingDate,
                              ArrayList<String> price, ArrayList<String> address, ArrayList<String> flagValue) {
        userPurohitBookingAdapter = new UserPurohitBookingAdapter(UserBookingList.this,pujaName,purohitName,bookingDate,price,address,flagValue);
        bookingList.setAdapter(userPurohitBookingAdapter);
    }
}
