package vydik.jjbytes.com.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import vydik.jjbytes.com.Adapters.UserInboxAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.UserInboxGetDB;

/**
 * Created by Manoj on 11/4/2015.
 */
public class InboxActivity extends ActionBarActivity{
    ListView messages;
    MainDatabase database;
    ArrayList<UserInboxGetDB> userInboxGetDB;
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> Message = new ArrayList<String>();
    private ArrayList<String> Date = new ArrayList<String>();
    UserInboxAdapter userInboxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new MainDatabase(this);
        database = database.open();
        userInboxGetDB = database.getUserInbox();
        setContentView(R.layout.inbox_activity);

        messages = (ListView) findViewById(R.id.list);

        if(userInboxGetDB.size() == 0){
            Toast.makeText(InboxActivity.this,"your inbox is empty",Toast.LENGTH_LONG).show();
        }else{
            Collections.reverse(userInboxGetDB);
            for(int i=0;i<userInboxGetDB.size();i++){
                id.add(userInboxGetDB.get(i).getId());
                Message.add(userInboxGetDB.get(i).getMessage());
                Date.add(userInboxGetDB.get(i).getDate());
            }
            loadMessageList(Message,Date);
        }
    }

    private void loadMessageList(ArrayList<String> message, ArrayList<String> date) {
        userInboxAdapter = new UserInboxAdapter(InboxActivity.this,message,date);
        messages.setAdapter(userInboxAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(GCMNotificationIntentService.notifyID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}