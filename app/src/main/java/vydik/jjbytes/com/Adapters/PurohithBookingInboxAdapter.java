package vydik.jjbytes.com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.LinkAddress;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vydik.jjbytes.com.Activities.PurohitBookingInboxActivity;
import vydik.jjbytes.com.Activities.R;

/**
 * Created by Manoj on 11/7/2015.
 */
public class PurohithBookingInboxAdapter extends BaseAdapter {
    Activity activity;

    private ArrayList<String> Pnumber = new ArrayList<String>();
    private ArrayList<String> PujaName = new ArrayList<String>();
    private ArrayList<String> UserLocation = new ArrayList<String>();
    private ArrayList<String> UserAddress = new ArrayList<String>();
    private ArrayList<String> PujaDate = new ArrayList<String>();
    private ArrayList<String> UserName = new ArrayList<String>();
    private ArrayList<String> Date = new ArrayList<String>();

    int listSize = 0;
    private static LayoutInflater inflater = null;

    public PurohithBookingInboxAdapter(Activity purohitBookingInboxActivity, ArrayList<String> userPhoneNumber,
                                       ArrayList<String> pujaName, ArrayList<String> userLocation, ArrayList<String> userAddress,
                                       ArrayList<String> pujaDate, ArrayList<String> userName, ArrayList<String> date) {
        activity = purohitBookingInboxActivity;
        Pnumber = userPhoneNumber;
        PujaName = pujaName;
        UserLocation = userLocation;
        UserAddress = userAddress;
        PujaDate = pujaDate;
        UserName = userName;
        Date = date;
        listSize = pujaName.size();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listSize;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.booking_inbox_list_design, null);
        LinearLayout PhoneUser = (LinearLayout) vi.findViewById(R.id.call_user);
        LinearLayout RejectPuja = (LinearLayout) vi.findViewById(R.id.reject_action);
        LinearLayout AcceptPuja = (LinearLayout) vi.findViewById(R.id.accept_action);

        TextView userName = (TextView) vi.findViewById(R.id.user_name);
        TextView pujaName = (TextView) vi.findViewById(R.id.puja_name);
        TextView pujaDate = (TextView) vi.findViewById(R.id.puja_date);
        TextView userAddress = (TextView) vi.findViewById(R.id.user_address);
        TextView userLocation = (TextView) vi.findViewById(R.id.user_location);
        TextView messageDate = (TextView) vi.findViewById(R.id.received_datetime);

        RejectPuja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"rejected puja",Toast.LENGTH_LONG).show();
            }
        });

        AcceptPuja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"accepted puja",Toast.LENGTH_LONG).show();
            }
        });

        PhoneUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<Pnumber.size();i++){
                    if(position == i){
                        String mobile_number=Pnumber.get(i);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + mobile_number));
                        activity.startActivity(callIntent);
                    }
                }
            }
        });

        if(PujaName.size() > position){
            pujaName.setText(PujaName.get(position));
        }else{
            pujaName.setVisibility(View.GONE);
        }
        if(PujaDate.size() > position){
            pujaDate.setText(PujaDate.get(position));
        }else{
            pujaDate.setVisibility(View.GONE);
        }
        if(UserAddress.size() > position){
            userAddress.setText(UserAddress.get(position));
        }else{
            userAddress.setVisibility(View.GONE);
        }
        if(UserLocation.size() > position){
            userLocation.setText(UserLocation.get(position));
        }else{
            userLocation.setVisibility(View.GONE);
        }
        if(UserName.size() > position){
            userName.setText(UserName.get(position));
        }else{
            userName.setVisibility(View.GONE);
        }
        if(Date.size() > position){
            messageDate.setText(Date.get(position));
        }else{
            messageDate.setVisibility(View.GONE);
        }
        return vi;
    }
}
