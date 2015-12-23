package vydik.jjbytes.com.Activities;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by user on 12/15/2015.
 */
public class PanchangActivity extends ActionBarActivity {

    TextView SignLord,Sign,NakshatraOne,NakshatraTwo,NakshatraLord,NakshatraCharan,Day,Tithi,Yog,Karan,SunRise,SunSet,SelectedDate,Ykala,Gkala,Rkala,Amurta;
    Button DateSelector,SubmitDate;
    private int ALmonthOfYear, ALdayOfMonth,ALyear,ALHour,ALMinutes;
    String Year,Month,DayCal,Hour,Minutes,SubmitDateMultipart;
    Constants constants;
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

    /*json decode entity*/
    String sign_lord,sign,Naksahtra,naksahtra_lord,nakshatra_charan,day,tithi,yog,karan,Yamaghanta,Gulikala,Rahukala,abhijit_murat;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panchang_activity);

        SignLord = (TextView) findViewById(R.id.sign_lord);
        Sign = (TextView) findViewById(R.id.sign);
        NakshatraOne = (TextView) findViewById(R.id.nakshatra);
        NakshatraTwo = (TextView) findViewById(R.id.nakshatra_second);
        NakshatraLord = (TextView) findViewById(R.id.naksahtra_lord);
        NakshatraCharan = (TextView) findViewById(R.id.nakshatra_charan);
        Day = (TextView) findViewById(R.id.day);
        Tithi = (TextView) findViewById(R.id.tithi);
        Yog = (TextView) findViewById(R.id.yog);
        Karan = (TextView) findViewById(R.id.karan);
        SunRise = (TextView) findViewById(R.id.sunrise);
        SunSet = (TextView) findViewById(R.id.sunset);
        SelectedDate = (TextView) findViewById(R.id.date_selected);
        Ykala = (TextView) findViewById(R.id.yemaganta_kala);
        Gkala = (TextView) findViewById(R.id.guli_kala);
        Rkala = (TextView) findViewById(R.id.rahu_kala);
        Amurta = (TextView) findViewById(R.id.abhijit_murat);

        DateSelector = (Button) findViewById(R.id.date_selector);
        SubmitDate = (Button) findViewById(R.id.submit_date);

        calendar = Calendar.getInstance();
        ALyear = calendar.get(Calendar.YEAR);
        ALmonthOfYear = calendar.get(Calendar.MONTH);
        ALdayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        ALHour = calendar.get(Calendar.HOUR_OF_DAY);
        ALMinutes = calendar.get(Calendar.MINUTE);

        Year = Integer.toString(ALyear);
        Month = Integer.toString(ALmonthOfYear+1);
        DayCal = Integer.toString(ALdayOfMonth);
        Hour = Integer.toString(ALHour);
        Minutes = Integer.toString(ALMinutes);

        SubmitDateMultipart = Year + "-" + Month + "-" + DayCal;

        SelectedDate.setText(SubmitDateMultipart);

        DateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerMethod();
            }
        });

        new GetPanchangForDay().execute();
    }

    private void DatePickerMethod() {
        calendar = Calendar.getInstance();
        ALyear = calendar.get(Calendar.YEAR);
        ALmonthOfYear = calendar.get(Calendar.MONTH);
        ALdayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ALyear=year;
                        ALmonthOfYear=monthOfYear;
                        ALdayOfMonth=dayOfMonth;
                        String strdate = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        SubmitDateMultipart = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        ALHour = calendar.get(Calendar.HOUR_OF_DAY);
                        ALMinutes = calendar.get(Calendar.MINUTE);
                        Hour = Integer.toString(ALHour);
                        Minutes = Integer.toString(ALMinutes);
                        constants.SDate = strdate;
                        SelectedDate.setText(strdate);
                        new GetPanchangForDay().execute();
                    }
                }, ALyear, ALmonthOfYear, ALdayOfMonth);
        dpd.show();
    }

    private class GetPanchangForDay extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(PanchangActivity.this, constants.GettingPanchang, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(constants.AccessPanchangForDay);
            System.out.println("out put : "+SubmitDateMultipart);
            try{
                multipartEntity.addPart(constants.PDate,new StringBody(SubmitDateMultipart));
                multipartEntity.addPart(constants.PTime,new StringBody(Hour));
                multipartEntity.addPart(constants.PMinutes,new StringBody(Minutes));
                multipartEntity.addPart(constants.PSubmit,new StringBody(constants.PSubmit));
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
                    System.out.println("First Exception caz of HttpResponese :"
                            + cpe);
                    cpe.printStackTrace();
                }catch (IOException ioe) {
                    System.out.println("Second Exception caz of HttpResponse :"
                            + ioe);
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
            Utilities.cancelProgressDialog();
            //System.out.println("out put : "+s);
            try {
                JSONObject object = new JSONObject(s);

                if(object.has("sign_lord")){
                    if(object.getString("sign_lord")!= null){
                        sign_lord = object.getString("sign_lord").toString();
                    }
                }

                if(object.has("sign")){
                    if(object.getString("sign")!= null){
                        sign = object.getString("sign").toString();
                    }
                }

                if(object.has("Naksahtra")){
                    if(object.getString("Naksahtra")!= null){
                        Naksahtra = object.getString("Naksahtra").toString();
                    }
                }

                if(object.has("naksahtra_lord")){
                    if(object.getString("naksahtra_lord")!= null){
                        naksahtra_lord = object.getString("naksahtra_lord").toString();
                    }
                }

                if(object.has("nakshatra_charan")){
                    if(object.getString("nakshatra_charan")!= null){
                        nakshatra_charan = object.getString("nakshatra_charan").toString();
                    }
                }

                if(object.has("day")){
                    if(object.getString("day")!= null){
                        day = object.getString("day").toString();
                    }
                }

                if(object.has("tithi")){
                    if(object.getString("tithi")!= null){
                        tithi = object.getString("tithi").toString();
                    }
                }

                if(object.has("yog")){
                    if(object.getString("yog")!= null){
                        yog = object.getString("yog").toString();
                    }
                }

                if(object.has("karan")){
                    if(object.getString("karan")!= null){
                        karan = object.getString("karan").toString();
                    }
                }

                JSONObject object1 = object.getJSONObject("Yamaghanta");
                if(object1.has("start")){
                    if(object1.getString("start")!= null){
                        Yamaghanta = "Start time :"+object1.getString("start").toString() + " End time :" +object1.getString("end").toString() ;
                    }
                }

                JSONObject object2 = object.getJSONObject("guliKaal");
                if(object2.has("start")){
                    if(object2.getString("start")!= null){
                        Gulikala = "Start time :"+object2.getString("start").toString() + " End time :" +object2.getString("end").toString();
                    }
                }

                JSONObject object3 = object.getJSONObject("rahukal");
                if(object3.has("start")){
                    if(object3.getString("start")!= null){
                        Rahukala = "Start time :"+object3.getString("start").toString() + " End time :" +object3.getString("end").toString();
                    }
                }

                JSONObject object4 = object.getJSONObject("abhijit_murat");
                if(object4.has("start")){
                    if(object4.getString("start")!= null){
                        abhijit_murat = "Start time :"+object4.getString("start").toString() + " End time :" +object4.getString("end").toString();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

                SignLord.setText(sign_lord);
                Sign.setText(sign);
                NakshatraOne.setText(Naksahtra);
                NakshatraLord.setText(naksahtra_lord);
                NakshatraCharan.setText(nakshatra_charan);
                Day.setText(day);
                Tithi.setText(tithi);
                Yog.setText(yog);
                Karan.setText(karan);
                SunRise.setText(MainActivity.FinalSunrise+" AM");
                SunSet.setText(MainActivity.FinalSunSet+" PM");
                Ykala.setText(Yamaghanta);
                Gkala.setText(Gulikala);
                Rkala.setText(Rahukala);
                Amurta.setText(abhijit_murat);
        }
    }
}