package vydik.jjbytes.com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vydik.jjbytes.com.Activities.R;

/**
 * Created by user on 12/2/2015.
 */
public class PurohitRecycAdapter extends RecyclerView.Adapter<PurohitRecycAdapter.CustomViewHolder>  {

    private ArrayList<String> PujaName = new ArrayList<String>();
    private ArrayList<String> P1 = new ArrayList<String>();
    private ArrayList<String> P2 = new ArrayList<String>();
    private ArrayList<String> P3 = new ArrayList<String>();
    private Context mContext;
    Activity activity;

    public PurohitRecycAdapter(ArrayList<String> purohitInfoTwo, ArrayList<String> pujaWithSamagriSubscribed, ArrayList<String> pujaWithoutSamagriSubscribed, ArrayList<String> pujaExpertLevel, Context testRclass) {
        PujaName = purohitInfoTwo;
        P1 = pujaWithSamagriSubscribed;
        P2 = pujaWithoutSamagriSubscribed;
        P3 = pujaExpertLevel;
        mContext = testRclass;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView poojaName,pujawithsamagri,pujawithoutsamagri,expertLevel,pujaType;
        public CustomViewHolder(View itemView) {

            super(itemView);
            poojaName = (TextView) itemView.findViewById(R.id.puja_name);
            pujawithsamagri = (TextView) itemView.findViewById(R.id.price_with_samagri);
            pujawithoutsamagri = (TextView) itemView.findViewById(R.id.price_without_samagri);
            expertLevel = (TextView) itemView.findViewById(R.id.expert);
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("size received"+PujaName.size());
        return PujaName.size();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.poojaName.setText(PujaName.get(position).toString());
        holder.pujawithsamagri.setText(P1.get(position));
        holder.pujawithoutsamagri.setText(P2.get(position));
        holder.expertLevel.setText(P3.get(position));
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purohit_puja_list_design, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }




}
