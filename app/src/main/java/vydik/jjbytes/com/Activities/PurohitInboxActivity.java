package vydik.jjbytes.com.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import vydik.jjbytes.com.Adapters.PurohitInboxAdapter;
import vydik.jjbytes.com.Adapters.UserInboxAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.PurohitInboxGetDB;

/**
 * Created by Manoj on 11/6/2015.
 */
public class PurohitInboxActivity extends ActionBarActivity {
    MainDatabase database;
    ListView messages;
    ArrayList<PurohitInboxGetDB> purohitInboxGetDB;
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> Message = new ArrayList<String>();
    private ArrayList<String> Date = new ArrayList<String>();
    PurohitInboxAdapter purohitInboxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_activity);

        database = new MainDatabase(this);
        database = database.open();
        purohitInboxGetDB = database.getPurohitInbox();

        messages = (ListView) findViewById(R.id.list);

        if(purohitInboxGetDB.size() == 0){
            Toast.makeText(PurohitInboxActivity.this, "your inbox is empty", Toast.LENGTH_LONG).show();
        }else{
            Collections.reverse(purohitInboxGetDB);
            for(int i=0;i<purohitInboxGetDB.size();i++){
                id.add(purohitInboxGetDB.get(i).getId());
                Message.add(purohitInboxGetDB.get(i).getMessage());
                Date.add(purohitInboxGetDB.get(i).getDate());
            }
            loadMessageList(Message,Date);
        }
    }

    private void loadMessageList(ArrayList<String> message, ArrayList<String> date) {
        purohitInboxAdapter = new PurohitInboxAdapter(PurohitInboxActivity.this,message,date);
        messages.setAdapter(purohitInboxAdapter);
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
