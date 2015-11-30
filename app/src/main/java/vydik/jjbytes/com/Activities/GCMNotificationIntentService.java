package vydik.jjbytes.com.Activities;;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import vydik.jjbytes.com.Adapters.UserInboxAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Interfaces.ApplicationConstants;
import vydik.jjbytes.com.constants.Constants;

import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GCMNotificationIntentService extends IntentService {
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;
	int msg_count=0;
	String Message,Day,Month,Hover,Minutes,AM_PM,DateValue,Type,Specification;
	MainDatabase database;
	UserInboxAdapter userInboxAdapter;
	Intent resultIntent;
	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}
	Constants constants;

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification(""+extras.get(ApplicationConstants.MSG_KEY));
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		System.out.println(msg);
		database = new MainDatabase(this);
		database = database.open();

		Calendar calendar = Calendar.getInstance();
		int date = calendar.get(Calendar.DATE);
		int month = calendar.get(Calendar.MONTH);
		int hover = calendar.get(Calendar.HOUR);
		int minutes = calendar.get(Calendar.MINUTE);
		int am_pm = calendar.get(Calendar.AM_PM);

/*set month based on Calender month return value*/
		if(month == 0){
			Month = "Jan";
		}else if(month == 1){
			Month = "Feb";
		}else if(month == 2){
			Month = "Mar";
		}else if(month == 3){
			Month = "Apr";
		}else if(month == 4){
			Month = "May";
		}else if(month == 5){
			Month = "Jun";
		}else if(month == 6){
			Month = "Jul";
		}else if(month == 7){
			Month = "Oug";
		}else if(month == 8){
			Month = "Sep";
		}else if(month == 9){
			Month = "Oct";
		}else if(month == 10){
			Month = "Nov";
		}else{
			Month = "Dec";
		}

/*get am pm from calender am pm*/
		if(am_pm == 1){
			AM_PM = "PM";
		}else{
			AM_PM = "AM";
		}

/*formating hour*/
		if(hover == 0){
			hover = 12;
		}
/*conver dat hover minutes to string*/
		Day = String.valueOf(date);
		Hover = String.valueOf(hover);
		Minutes = String.valueOf(minutes);
		DateValue = Day+" "+Month+" "+Hover+":"+Minutes+" "+AM_PM;

		try {
			JSONObject object = new JSONObject(msg.toString());

			if(object.has("message")){
				if(object.getString("message")!= null){
					Message = object.getString("message").toString();
				}else{
					Message = "failed to retrieve message";
				}
			}else{
				Message = "failed to retrieve message";
			}

			if(object.has("type")){
				if(object.getString("type")!= null){
					Type = object.getString("type").toString();
				}else{
					Type = "not given";
				}
			}else{
				Type = "not given";
			}

			if(object.has("specification")){
				if(object.getString("specification")!= null){
					Specification = object.getString("specification").toString();
				}else{
					Specification = "not given";
				}
			}else{
				Specification = "not given";
			}

			JSONArray array = object.getJSONArray("details");
			for(int i=0;i<array.length();i++){
				JSONObject object1 = array.getJSONObject(i);
				if(object1.has("mobile_no")){
					if(object1.getString("mobile_no")!= null){
						constants.BUser_Phone = object1.getString("mobile_no").toString();
					}else{
						constants.BUser_Phone = "0000";
					}
				}else{
					constants.BUser_Phone = "0000";
				}

				if(object1.has("puja_name")){
					if(object1.getString("puja_name")!= null){
						constants.BPuja_Name = object1.getString("puja_name").toString();
					}else{
						constants.BPuja_Name = "not specified";
					}
				}else{
					constants.BPuja_Name = "not specified";
				}

				if(object1.has("loc_name")){
					if(object1.getString("loc_name")!= null){
						constants.BUser_Location = object1.getString("loc_name").toString();
					}else{
						constants.BUser_Location = "not specified";
					}
				}else{
					constants.BUser_Location = "not specified";
				}

				if(object1.has("Address")){
					if(object1.getString("Address")!= null){
						constants.BUser_Address = object1.getString("Address").toString();
					}else{
						constants.BUser_Address = "not specified";
					}
				}else{
					constants.BUser_Address = "not specified";
				}

				if(object1.has("puja_date")){
					if(object1.getString("puja_date")!= null){
						constants.BPuja_Date = object1.getString("puja_date").toString();
					}else{
						constants.BPuja_Date = "not specified";
					}
				}else{
					constants.BPuja_Date = "not specified";
				}

				if(object1.has("first_name")){
					if(object1.getString("first_name")!= null){
						constants.BUserF_Name = object1.getString("first_name").toString();
					}else{
						constants.BUserF_Name = "not specified";
					}
				}else{
					constants.BUserF_Name = "not specified";
				}

				if(object1.has("last_name")){
					if(object1.getString("last_name")!= null){
						constants.BUserL_Name = object1.getString("last_name").toString();
					}else {
						constants.BUserL_Name = "not specified";
					}
				}else {
					constants.BUserL_Name = "not specified";
				}

				if(object1.has("purohit_id")){
					if(object1.getString("purohit_id")!= null){
						constants.BPurohit_ID = object1.getString("purohit_id").toString();
					}else{
						constants.BPurohit_ID = "000";
					}
				}else {
					constants.BPurohit_ID = "000";
				}

				if(object1.has("user_id")){
					if (object1.getString("user_id")!= null){
						constants.BUser_ID = object1.getString("user_id").toString();
					}else{
						constants.BUser_ID = "000";
					}
				}else{
					constants.BUser_ID = "000";
				}

				constants.BUser_Name = constants.BUserF_Name +" "+constants.BUserL_Name;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}

/*launch activity based on type and specification received*/
		if(Type.equals("purohit")){
			if(Specification.equals("puja booked")){
				/*insert purohith booking notification to db*/
				database.InsertPurohitBookingInbox(constants.BUser_Phone,constants.BPuja_Name,constants.BUser_Location,constants.BUser_Address,
						constants.BPuja_Date,constants.BUser_Name,DateValue,constants.BPurohit_ID,constants.BUser_ID);

				resultIntent = new Intent(this, PurohitBookingInboxActivity.class);
			}else{
				/*insert purohith alert notification to db*/
				database.InsertPurohitInbox(Message,DateValue);

				resultIntent = new Intent(this, PurohitInboxActivity.class);
			}
		}else {
			/*insert user notification to db*/
			database.InsertUserInbox(Message,DateValue);

			resultIntent = new Intent(this, InboxActivity.class);
		}

		resultIntent.putExtra("msg", Message);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_ONE_SHOT);
		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("Vydik")
				.setContentText("test")
				.setSmallIcon(R.drawable.android_logo);
		//Set pending intent
		mNotifyBuilder.setContentIntent(resultPendingIntent);

		// Set Vibrate, Sound and Light
		int defaults = 0;
		defaults = defaults | Notification.DEFAULT_LIGHTS;
		defaults = defaults | Notification.DEFAULT_VIBRATE;
		defaults = defaults | Notification.DEFAULT_SOUND;

		mNotifyBuilder.setDefaults(defaults);
		// Set the content for Notification
		mNotifyBuilder.setContentText(Message);
		// Set autocancel
		mNotifyBuilder.setAutoCancel(true);
		// Post a notification
		mNotificationManager.notify(notifyID, mNotifyBuilder.build());

		try
		{
			SharedPreferences sharedpreferences = getSharedPreferences("NEW_MESSAGE", MODE_PRIVATE);
			Editor editor = sharedpreferences.edit();
			editor.putString("msg_count", ""+msg_count);
			editor.commit();
		}
		catch(Exception e)
		{

		}
		database.close();
	}
}