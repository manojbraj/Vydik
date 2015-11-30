package vydik.jjbytes.com.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import vydik.jjbytes.com.Activities.MainActivity;
import vydik.jjbytes.com.Activities.R;
import vydik.jjbytes.com.Activities.UserBookingList;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 11/16/2015.
 */
public class UserPurohitBookingAdapter extends BaseAdapter {
    Activity activity;
    private ArrayList<String> PujaName = new ArrayList<String>();
    private ArrayList<String> PurohitName = new ArrayList<String>();
    private ArrayList<String> BookingDate = new ArrayList<String>();
    private ArrayList<String> Price = new ArrayList<String>();
    private ArrayList<String> Address = new ArrayList<String>();
    int listSize = 0;
    private static LayoutInflater inflater = null;
    Constants constants;
    /*for reschedule*/
    String ReschedulePositionPujaName,ReschedulePositionPurohitName,RescheduleDate = null,RescheduleReason,RSID;
    TextView RSPujaName,RSPurohitName,RSDate,RSCalender;
    EditText RSReason;
    TextInputLayout Reason;
    Button RSSubmit,RSCancel;
    /*end*/

    /*for cancel purohit*/
    String CancelPujaName,CancelPusrohitName,CancelReason,CPID;
    TextView CPPujaName,CPPurohitName;
    EditText CPReason;
    TextInputLayout CReason;
    Button CPSubmit,CPCancel;
    /*end*/

    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    MainDatabase database;

    private int ALmonthOfYear, ALdayOfMonth,ALyear;
    String strdate;
    public UserPurohitBookingAdapter(Activity userBookingList, ArrayList<String> pujaName, ArrayList<String> purohitName,
                                     ArrayList<String> bookingDate, ArrayList<String> price, ArrayList<String> address, ArrayList<String> flagValue) {
        activity = userBookingList;
        PujaName = pujaName;
        PurohitName = purohitName;
        BookingDate = bookingDate;
        Price = price;
        Address = address;
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
            vi = inflater.inflate(R.layout.user_booking_list_item, null);
        TextView puja_name = (TextView) vi.findViewById(R.id.puja_name);
        TextView purohit_name = (TextView) vi.findViewById(R.id.purohit_name);
        TextView booking_date = (TextView) vi.findViewById(R.id.date_value);
        TextView price = (TextView) vi.findViewById(R.id.price_value);
        TextView address = (TextView) vi.findViewById(R.id.user_address);
        TextView cancel_purohit = (TextView) vi.findViewById(R.id.cancel_purohit);
        TextView reschedule_purohit = (TextView) vi.findViewById(R.id.reschedule_purohit);

        cancel_purohit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReschedulePositionPujaName = PujaName.get(position);
                ReschedulePositionPurohitName = PurohitName.get(position);
                RSID = UserBookingList.PujaId.get(position);
                CallCancellPopup();
            }
        });

        reschedule_purohit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReschedulePositionPujaName = PujaName.get(position);
                ReschedulePositionPurohitName = PurohitName.get(position);
                RSID = UserBookingList.PujaId.get(position);
                CallReschedulePopup();
            }
        });
        if(PujaName.size() > position){
            puja_name.setText(PujaName.get(position));
            for(int i=0;i<UserBookingList.FlagValue.size();i++){
                String value = UserBookingList.FlagValue.get(i);
                if(value.equals("0")){
                    cancel_purohit.setVisibility(View.VISIBLE);
                }else {
                    cancel_purohit.setVisibility(View.GONE);
                }
            }
        }else {
            puja_name.setVisibility(View.GONE);
        }
        if(PurohitName.size() > position){
            purohit_name.setText(PurohitName.get(position));
        }else {
            purohit_name.setVisibility(View.GONE);
        }
        if(BookingDate.size()>position){
            booking_date.setText(BookingDate.get(position));
        }else {
            booking_date.setVisibility(View.GONE);
        }
        if(Price.size()>position){
            price.setText(Price.get(position));
        }else {
            price.setVisibility(View.GONE);
        }
        if(Address.size()>position){
            address.setText(Address.get(position));
        } else {
            address.setVisibility(View.GONE);
        }
        return vi;
    }

    private void CallReschedulePopup() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View PromtView = inflater.inflate(R.layout.reshedule_puja_popup, null);
        final AlertDialog alertD = new AlertDialog.Builder(activity).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD.setCancelable(false);
            RSPujaName = (TextView) PromtView.findViewById(R.id.puja_name);
            RSPurohitName = (TextView) PromtView.findViewById(R.id.purohit_name);
            RSDate = (TextView) PromtView.findViewById(R.id.selected_date);
            RSCalender = (TextView) PromtView.findViewById(R.id.call_calender);
            Reason = (TextInputLayout) PromtView.findViewById(R.id.reason);
            Reason.setErrorEnabled(true);
            Reason.setError("Please Enter reason for Reschedule");
            RSReason = (EditText) PromtView.findViewById(R.id.edit_reason);
            RSSubmit = (Button) PromtView.findViewById(R.id.submit_reschedule);
            RSCancel = (Button) PromtView.findViewById(R.id.cancel_reschedule);

            RSPujaName.setText(ReschedulePositionPujaName);
            RSPurohitName.setText(ReschedulePositionPurohitName);

            RSCalender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerMethod();
                }
            });

            RSSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validate(new EditText[]{RSReason})){
                        if(RescheduleDate != null){
                            RescheduleReason = RSReason.getEditableText().toString();
                            alertD.cancel();
                            new SubmitRescheduleDetails().execute();
                        }else {
                            Toast.makeText(activity,"Please select a Reschedule date",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(activity,"Please enter a reason for Reschedule",Toast.LENGTH_LONG).show();
                    }
                }
            });

        RSCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RescheduleDate = null;
                alertD.cancel();
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }

    private void DatePickerMethod() {
        final Calendar c = Calendar.getInstance();
        ALyear = c.get(Calendar.YEAR);
        ALmonthOfYear = c.get(Calendar.MONTH);
        ALdayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ALyear=year;
                        ALmonthOfYear=monthOfYear;
                        ALdayOfMonth=dayOfMonth;
                        strdate = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        RescheduleDate = strdate;
                        RSDate.setText(strdate);
                    }
                }, ALyear, ALmonthOfYear, ALdayOfMonth);
        dpd.show();
    }

    private void CallCancellPopup() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View PromtView = inflater.inflate(R.layout.cancel_purohit_popup, null);
        final AlertDialog alertD = new AlertDialog.Builder(activity).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD.setCancelable(false);
            CPPujaName = (TextView)PromtView.findViewById(R.id.puja_name);
            CPPurohitName = (TextView)PromtView.findViewById(R.id.purohit_name);
            CPReason = (EditText) PromtView.findViewById(R.id.edit_reason);
            CPSubmit = (Button)PromtView.findViewById(R.id.submit_button);
            CPCancel = (Button)PromtView.findViewById(R.id.cancel_button);

        CPPujaName.setText(ReschedulePositionPujaName);
        CPPurohitName.setText(ReschedulePositionPurohitName);

        CPSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(new EditText[]{CPReason})){
                    alertD.cancel();
                    new SubbmitCancelRequest().execute();
                }else {
                    Toast.makeText(activity,"Please enter a reason for cancelation of puja",Toast.LENGTH_LONG).show();
                }
            }
        });

        CPCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.cancel();
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }

    private class SubmitRescheduleDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(activity, constants.RequestForReschedule, false);
        }

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            RescheduleDate = null;
            Utilities.cancelProgressDialog();
            database = new MainDatabase(activity);
            database = database.open();
            database.updateRescheduleDate(RSID, strdate);
            database.close();
            Toast.makeText(activity,"Reschedule puja Succesfull",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity,UserBookingList.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    private class SubbmitCancelRequest extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(activity, constants.RequestForCancelation, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(constants.CancelPujaRequestURL);
            try{
                multipartEntity.addPart("id",new StringBody("7"));

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
                    cpe.printStackTrace();
                }catch (IOException ioe) {
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
            RescheduleDate = null;
            Utilities.cancelProgressDialog();
            try {
                JSONObject object = new JSONObject(s);
                String message = object.getString("msg").toString();
                if(message.equals("Puja Canceled")){
                    database = new MainDatabase(activity);
                    database = database.open();
                    database.DeleteBooking(RSID);
                    database.close();
                    Toast.makeText(activity,"Puja cancellation successful",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity,UserBookingList.class);
                    activity.startActivity(intent);
                    activity.finish();
                }else{
                    Toast.makeText(activity,"Puja cancellation failed try after some time",Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){

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
}
