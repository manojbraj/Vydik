package vydik.jjbytes.com.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import vydik.jjbytes.com.Activities.R;
import vydik.jjbytes.com.Activities.RegistrationPoojaActivity;
import vydik.jjbytes.com.constants.ArrayListConstants;

import static vydik.jjbytes.com.Activities.R.*;

/**
 * Created by manoj on 10/12/2015.
 */

public class PoojaListAdapter extends BaseAdapter {
    private ArrayList<String> PoojaName = new ArrayList<String>();
    private ArrayList<String> PoojaTypeId = new ArrayList<String>();
    private ArrayList<String> PoojaId = new ArrayList<String>();

    private ArrayList<Boolean> BooleanCheck = new ArrayList<Boolean>();
    int ListSize = 0;
    boolean Bool;
    private static LayoutInflater inflater = null;

    private Activity activity;
    RegistrationPoojaActivity registrationPoojaActivity;
    public static ViewHolder holder;
    int i;
    boolean isChecked = false;
    Button AddPuja,Cancel;
    EditText withSamagri,withoutSamagri;
    Spinner ExpertLevel;
    public PoojaListAdapter(Activity a, ArrayList<String> poojaName, ArrayList<String> poojaTypeId, ArrayList<String> poojaId){
        activity = a;
        PoojaName = poojaName;
        PoojaTypeId = poojaTypeId;
        PoojaId = poojaId;
        BooleanCheck.add(Bool);
        ListSize = poojaId.size();
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
        public TextView poojaName,add_text;
        public ImageView add_button;
        public LinearLayout AddLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null){
            vi = inflater.inflate(layout.pooja_list_items,null);
            holder = new ViewHolder();
            holder.poojaName = (TextView) vi.findViewById(R.id.pooja_name);
            holder.AddLayout = (LinearLayout) vi.findViewById(R.id.add_puja_layout);
            holder.add_button = (ImageView) vi.findViewById(R.id.add_button);
            holder.add_text = (TextView) vi.findViewById(id.add_text);
            holder.AddLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<PoojaName.size();i++){
                        final int k = i;
                        if(position == i){
                            final String PujaNamePosition = PoojaName.get(i);
                            final String PujaTypeIdPosition = PoojaTypeId.get(i);
                            int listSize = ArrayListConstants.PujaNameSubscribed.size();
                            if(listSize == 0){
                                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View PromtView = inflater.inflate(layout.add_pooja_details_popup, null);
                                final AlertDialog alertD = new AlertDialog.Builder(activity).create();
                                alertD.setCancelable(false);
                                withSamagri = (EditText) PromtView.findViewById(id.with_samagri);
                                withoutSamagri = (EditText) PromtView.findViewById(id.without_samagri);
                                AddPuja = (Button) PromtView.findViewById(id.add_puja);
                                Cancel = (Button) PromtView.findViewById(id.cancel);
                                ExpertLevel = (Spinner) PromtView.findViewById(id.expert_level);
                                AddPuja.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    /*validation of edit text and spinner*/
                                        if (validate(new EditText[]{withSamagri, withoutSamagri})) {
                                            String WithSamagri = withSamagri.getEditableText().toString();
                                            String WithoutSamagri = withoutSamagri.getEditableText().toString();
                                            String Experties = ExpertLevel.getSelectedItem().toString();
                                            boolean returnValue = CheckArrayListDuplicate(PujaNamePosition);
                                            ArrayListConstants.PujaNameSubscribed.add(PujaNamePosition);
                                            ArrayListConstants.PujaWithSamagriSubscribed.add(WithSamagri);
                                            ArrayListConstants.PujaWithoutSamagriSubscribed.add(WithoutSamagri);
                                            ArrayListConstants.PujaExpertLevel.add(Experties);
                                            if (PujaTypeIdPosition.equals("1")) {
                                                ArrayListConstants.PujaType.add("puja");
                                            }
                                            alertD.dismiss();
                                        } else {
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
                                break;
                            }else {
                                boolean returnValue = CheckArrayListDuplicate(PujaNamePosition);
                                if(returnValue == true){
                                    Toast.makeText(activity,"You cannot add the same puja more than once",Toast.LENGTH_LONG).show();
                                }else {
                                    LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View PromtView = inflater.inflate(layout.add_pooja_details_popup, null);
                                    final AlertDialog alertD = new AlertDialog.Builder(activity).create();
                                    alertD.setCancelable(false);
                                    withSamagri = (EditText) PromtView.findViewById(id.with_samagri);
                                    withoutSamagri = (EditText) PromtView.findViewById(id.without_samagri);
                                    AddPuja = (Button) PromtView.findViewById(id.add_puja);
                                    Cancel = (Button) PromtView.findViewById(id.cancel);
                                    ExpertLevel = (Spinner) PromtView.findViewById(id.expert_level);
                                    AddPuja.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                    /*validation of edit text and spinner*/
                                            if (validate(new EditText[]{withSamagri, withoutSamagri})) {
                                                String WithSamagri = withSamagri.getEditableText().toString();
                                                String WithoutSamagri = withoutSamagri.getEditableText().toString();
                                                String Experties = ExpertLevel.getSelectedItem().toString();
                                                boolean returnValue = CheckArrayListDuplicate(PujaNamePosition);
                                                ArrayListConstants.PujaNameSubscribed.add(PujaNamePosition);
                                                ArrayListConstants.PujaWithSamagriSubscribed.add(WithSamagri);
                                                ArrayListConstants.PujaWithoutSamagriSubscribed.add(WithoutSamagri);
                                                ArrayListConstants.PujaExpertLevel.add(Experties);
                                                if (PujaTypeIdPosition.equals("1")) {
                                                    ArrayListConstants.PujaType.add("puja");
                                                }
                                                alertD.dismiss();
                                            } else {
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
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        vi.setTag(holder);
        }else{
            holder = (ViewHolder) vi.getTag();
        }if(PoojaName.size() > position){
            holder.poojaName.setText(PoojaName.get(position));
        }else{
            holder.poojaName.setVisibility(View.GONE);
        }if(PoojaTypeId.size() > position){
            String type_id = PoojaTypeId.get(i);
        }if(PoojaId.size() > position){
            String id = PoojaId.get(i);
        }
        return vi;
    }

    private boolean CheckArrayListDuplicate(String pujaNamePosition) {
        boolean rType = false;
            for (String data : ArrayListConstants.PujaNameSubscribed) {
                if (data.equals(pujaNamePosition)) {
                    rType = true;
                    break;
                } else {
                    rType = false;
                }
            }
        return rType;
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
