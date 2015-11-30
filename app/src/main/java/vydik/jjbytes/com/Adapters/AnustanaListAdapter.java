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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vydik.jjbytes.com.Activities.R;
import vydik.jjbytes.com.Models.PoojaModel;
import vydik.jjbytes.com.constants.ArrayListConstants;

/**
 * Created by manoj on 10/12/2015.
 */
public class AnustanaListAdapter extends BaseAdapter {

    private ArrayList<String> AnustanaName = new ArrayList<String>();
    private List<String> AnustanaTypeId = new ArrayList<String>();
    private List<String> AnustanaId = new ArrayList<String>();

    int ListSize = 0;
    private static LayoutInflater inflater = null;

    private Activity activity;
    public static ViewHolder holder;
    int i;
    Button AddPuja,Cancel;
    EditText withSamagri,withoutSamagri;
    Spinner ExpertLevel;
    public AnustanaListAdapter(Activity a, ArrayList<String> poojaName,List<String> poojaTypeId,List<String> poojaId){
        activity = a;
        AnustanaName = poojaName;
        AnustanaTypeId = poojaTypeId;
        AnustanaId = poojaId;
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
        public TextView poojaName;
        public LinearLayout AddLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null){
            vi = inflater.inflate(R.layout.pooja_list_items,null);
            holder = new ViewHolder();
            holder.poojaName = (TextView) vi.findViewById(R.id.pooja_name);
            holder.AddLayout = (LinearLayout) vi.findViewById(R.id.add_puja_layout);
            holder.AddLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<AnustanaName.size();i++){
                        if(position == i) {
                            final String PujaNamePosition = AnustanaName.get(i);
                            final String PujaTypeIdPosition = AnustanaTypeId.get(i);
                            boolean returnValue = CheckArrayListDuplicate(PujaNamePosition);
                            if (returnValue == true) {
                                Toast.makeText(activity, "You cannot add the same homa more than once", Toast.LENGTH_LONG).show();
                            } else {
                                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View PromtView = inflater.inflate(R.layout.add_pooja_details_popup, null);
                                final AlertDialog alertD = new AlertDialog.Builder(activity).create();
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
                                        if (validate(new EditText[]{withSamagri, withoutSamagri})) {
                                            String WithSamagri = withSamagri.getEditableText().toString();
                                            String WithoutSamagri = withoutSamagri.getEditableText().toString();
                                            String Experties = ExpertLevel.getSelectedItem().toString();
                                            ArrayListConstants.PujaNameSubscribed.add(PujaNamePosition);
                                            ArrayListConstants.PujaWithSamagriSubscribed.add(WithSamagri);
                                            ArrayListConstants.PujaWithoutSamagriSubscribed.add(WithoutSamagri);
                                            ArrayListConstants.PujaExpertLevel.add(Experties);
                                            if (PujaTypeIdPosition.equals("3")) {
                                                ArrayListConstants.PujaType.add("Anusthana");
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
                            }
                        }
                    }
                }
            });
            vi.setTag(holder);
        }else{
            holder = (ViewHolder) vi.getTag();
        }if(AnustanaName.size() > position){
            holder.poojaName.setText(AnustanaName.get(position));
        }else{
            holder.poojaName.setVisibility(View.GONE);
        }if(AnustanaTypeId.size() > position){
            String type_id = AnustanaTypeId.get(i);
        }if(AnustanaId.size() > position){
            String id = AnustanaId.get(i);
        }
        return vi;
    }

    private boolean CheckArrayListDuplicate(String pujaNamePosition) {
        boolean rType = false;
        int listSize = ArrayListConstants.PujaNameSubscribed.size();
        if(listSize == 0){
            rType = false;
        }else {

            for (String data : ArrayListConstants.PujaNameSubscribed) {
                if (data.equals(pujaNamePosition)) {
                    rType = true;
                    break;
                } else {
                    rType = false;
                }
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
