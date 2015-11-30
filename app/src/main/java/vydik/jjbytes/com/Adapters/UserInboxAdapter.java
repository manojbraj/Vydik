package vydik.jjbytes.com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vydik.jjbytes.com.Activities.InboxActivity;
import vydik.jjbytes.com.Activities.R;

/**
 * Created by Manoj on 11/5/2015.
 */
public class UserInboxAdapter extends BaseAdapter {
    Activity activity;
    private ArrayList<String> Message = new ArrayList<String>();
    private ArrayList<String> Date = new ArrayList<String>();
    int listSize = 0;
    private static LayoutInflater inflater = null;

    public UserInboxAdapter(Activity inboxActivity, ArrayList<String> message, ArrayList<String> date) {
        activity = inboxActivity;
        Message = message;
        Date = date;
        listSize = message.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.inbox_list_design, null);
        TextView ServerMessage = (TextView) vi.findViewById(R.id.server_message);
        TextView ReceivedDate = (TextView) vi.findViewById(R.id.date_time);
        if(Message.size()>position){
            ServerMessage.setText(Message.get(position));
        }else{
            ServerMessage.setVisibility(View.GONE);
        }
        if(Date.size()>position){
            ReceivedDate.setText(Date.get(position));
        }else{
            ReceivedDate.setVisibility(View.GONE);
        }
        return vi;
    }
}
