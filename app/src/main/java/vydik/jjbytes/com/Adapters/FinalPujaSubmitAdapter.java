package vydik.jjbytes.com.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import vydik.jjbytes.com.Activities.FinalRegistrationSubmit;
import vydik.jjbytes.com.Activities.R;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 10/14/2015.
 */
public class FinalPujaSubmitAdapter extends BaseAdapter {
    Activity activity;
    private ArrayList<String> PoojaName = new ArrayList<String>();
    private ArrayList<String> PoojaWithSamagri = new ArrayList<String>();
    private ArrayList<String> PoojaWithoutSamagri = new ArrayList<String>();
    private ArrayList<String> PoojaExpertLevel = new ArrayList<String>();
    private ArrayList<String> PoojaType = new ArrayList<String>();

    int ListSize = 0;
    private static LayoutInflater inflater = null;
    public static ViewHolder holder;
    Button AddPuja,Cancel;
    EditText withSamagri,withoutSamagri;
    Spinner ExpertLevel;

    public FinalPujaSubmitAdapter(Activity a, ArrayList<String> pujaNameSubscribed,
                                  ArrayList<String> pujaWithSamagriSubscribed, ArrayList<String> pujaWithoutSamagriSubscribed,
                                  ArrayList<String> pujaExpertLevel, ArrayList<String> pujaType) {
        activity = a;
        PoojaName = pujaNameSubscribed;
        PoojaWithSamagri = pujaWithSamagriSubscribed;
        PoojaWithoutSamagri = pujaWithoutSamagriSubscribed;
        PoojaExpertLevel = pujaExpertLevel;
        PoojaType = pujaType;
        ListSize = pujaNameSubscribed.size();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListSize;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public TextView poojaName,pujawithsamagri,pujawithoutsamagri,expertLevel,pujaType;
        public LinearLayout UpdatePuja;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null){
            vi = inflater.inflate(R.layout.final_puja_list_items,null);
            holder = new ViewHolder();
            holder.poojaName = (TextView) vi.findViewById(R.id.puja_name);
            holder.pujawithsamagri = (TextView) vi.findViewById(R.id.price_with_samagri);
            holder.pujawithoutsamagri = (TextView) vi.findViewById(R.id.price_without_samagri);
            holder.expertLevel = (TextView) vi.findViewById(R.id.expert);
            holder.pujaType = (TextView) vi.findViewById(R.id.puja_type);

            holder.UpdatePuja = (LinearLayout) vi.findViewById(R.id.update_puja_list);
            holder.UpdatePuja.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View PromtView = inflater.inflate(R.layout.add_pooja_details_popup, null);
                    final AlertDialog alertD = new AlertDialog.Builder(activity).create();
                    alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    alertD.setCancelable(false);
                    withSamagri = (EditText) PromtView.findViewById(R.id.with_samagri);
                    withoutSamagri = (EditText) PromtView.findViewById(R.id.without_samagri);
                    AddPuja = (Button) PromtView.findViewById(R.id.add_puja);
                    Cancel = (Button) PromtView.findViewById(R.id.cancel);
                    ExpertLevel = (Spinner) PromtView.findViewById(R.id.expert_level);
                    AddPuja.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*validation of edit text and spinner*/
                            if (validate(new EditText[] { withSamagri, withoutSamagri })) {
                                String WithSamagri = withSamagri.getEditableText().toString();
                                String WithoutSamagri = withoutSamagri.getEditableText().toString();
                                String Experties = ExpertLevel.getSelectedItem().toString();
                                for(int i=0;i< ArrayListConstants.PujaNameSubscribed.size();i++){
                                    if(position == i){
                                        ArrayListConstants.PujaWithSamagriSubscribed.set(i,WithSamagri);
                                        ArrayListConstants.PujaWithoutSamagriSubscribed.set(i,WithoutSamagri);
                                        ArrayListConstants.PujaExpertLevel.set(i,Experties);
                                        notifyDataSetChanged();
                                    }
                                }
                                alertD.dismiss();
                            }else {
                                withSamagri.setHintTextColor(activity.getResources().getColor(R.color.red));
                                withSamagri.setHint("With Samagri");
                                withoutSamagri.setHintTextColor(activity.getResources().getColor(R.color.red));
                                withoutSamagri.setHint("Without Samagri");
                                Vibrator vibrator = (Vibrator) activity.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(500);
                            }
                        }
                    });

                    Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertD.dismiss();
                        }
                    });
                    alertD.setView(PromtView);
                    alertD.show();
                }
            });
            vi.setTag(holder);
        }else{
            holder = (ViewHolder) vi.getTag();
        }if(PoojaName.size() > position){
            holder.poojaName.setText(PoojaName.get(position));
        }else{
            holder.poojaName.setVisibility(View.GONE);
        }if(PoojaWithSamagri.size() > position){
            holder.pujawithsamagri.setText(PoojaWithSamagri.get(position));
        }else{
            holder.pujawithsamagri.setVisibility(View.GONE);
        }if(PoojaWithoutSamagri.size() > position){
            holder.pujawithoutsamagri.setText(PoojaWithoutSamagri.get(position));
        }else{
            holder.pujawithoutsamagri.setVisibility(View.GONE);
        }if(PoojaExpertLevel.size() > position){
            holder.expertLevel.setText(PoojaExpertLevel.get(position));
        }else{
            holder.expertLevel.setVisibility(View.GONE);
        }if(PoojaType.size() > position){
            holder.pujaType.setText(PoojaType.get(position));
        }else{
            holder.pujaType.setVisibility(View.GONE);
        }
        return vi;
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
