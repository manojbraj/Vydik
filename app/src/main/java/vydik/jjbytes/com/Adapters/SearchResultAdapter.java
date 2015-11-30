package vydik.jjbytes.com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vydik.jjbytes.com.Activities.R;
import vydik.jjbytes.com.Utils.ImageLoader;

/**
 * Created by Manoj on 10/28/2015.
 */
public class SearchResultAdapter extends BaseAdapter {
    Activity activity;
    private ArrayList<String> pFName = new ArrayList<String>();
    private ArrayList<String> pExpert = new ArrayList<String>();
    private ArrayList<String> pLocation = new ArrayList<String>();
    private ArrayList<String> pPrice = new ArrayList<String>();
    private ArrayList<String> pPhoto = new ArrayList<String>();

    public ImageLoader imageLoader;
    int listsize = 0;
    private static LayoutInflater inflater = null;

    public SearchResultAdapter(Activity activity1, ArrayList<String> purohithFirstName, ArrayList<String> purohithExpertLevel,
                               ArrayList<String> purohithLocation, ArrayList<String> purohithPrice, ArrayList<String> purohithPhoto) {
        activity = activity1;
        pFName = purohithFirstName;
        pExpert = purohithExpertLevel;
        pLocation = purohithLocation;
        pPrice = purohithPrice;
        pPhoto = purohithPhoto;
        listsize = purohithFirstName.size();
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listsize;
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.puja_search_list_display, null);
        TextView FirstName = (TextView) vi.findViewById(R.id.pujari_name);
        TextView ExpertLevel = (TextView) vi.findViewById(R.id.experties_level);
        TextView location = (TextView) vi.findViewById(R.id.location_state_name);
        TextView Price = (TextView) vi.findViewById(R.id.price_of_puja);
        ImageView ProfilePick = (ImageView) vi.findViewById(R.id.pujari_image);
        if(pFName.size()>position){
            FirstName.setText(pFName.get(position));
        }else{
            FirstName.setVisibility(View.GONE);
        }
        if(pExpert.size()>position){
            ExpertLevel.setText(pExpert.get(position));
        }else{
            ExpertLevel.setVisibility(View.GONE);
        }
        if(pLocation.size()>position){
            location.setText(pLocation.get(position));
        }else{
            location.setVisibility(View.GONE);
        }
        if(pPrice.size()>position){
            Price.setText(pPrice.get(position));
        }else{
            Price.setVisibility(View.GONE);
        }
        if(pPhoto.size()>position){
            imageLoader.DisplayImage(pPhoto.get(position), ProfilePick);
        }else{

        }
        return vi;
    }
}