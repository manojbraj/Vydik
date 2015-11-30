package vydik.jjbytes.com.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Manoj on 11/23/2015.
 */
public class PurohitProfileView extends AppCompatActivity {
    Toolbar toolbar;
    TextView ProfileView,PujaListView;
    View PFView,PJView;
    LinearLayout layout_one,layout_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purohit_profile_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ProfileView = (TextView) findViewById(R.id.profile_view_nested);
        PujaListView = (TextView) findViewById(R.id.puja_list_nested);

        PFView = (View) findViewById(R.id.prof_view);
        PJView = (View) findViewById(R.id.puja_view);

        layout_one = (LinearLayout) findViewById(R.id.layout_nested_profile);
        layout_two = (LinearLayout) findViewById(R.id.layout_nested_puja);

        ProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PFView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                PJView.setBackgroundColor(getResources().getColor(R.color.gray));
                layout_one.setVisibility(View.VISIBLE);
                layout_two.setVisibility(View.GONE);
            }
        });

        PujaListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PJView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                PFView.setBackgroundColor(getResources().getColor(R.color.gray));
                layout_two.setVisibility(View.VISIBLE);
                layout_one.setVisibility(View.GONE);
            }
        });
    }
}