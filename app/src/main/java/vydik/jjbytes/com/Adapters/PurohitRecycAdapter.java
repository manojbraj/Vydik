package vydik.jjbytes.com.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vydik.jjbytes.com.Activities.R;

/**
 * Created by user on 12/2/2015.
 */
public class PurohitRecycAdapter extends RecyclerView.Adapter<PurohitRecycAdapter.CustomViewHolder>  {

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purohit_puja_list_design, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
}
