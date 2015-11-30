package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 10/31/2015.
 */
public class PurohithDetailsActivity extends ActionBarActivity{
    TextView PurohithName,PurohithUnivercity,PurohithSect,PurohithGuruName,PurohithLocation,PujaPrice,Rating;
    SearchResultPurohithActivity searchResultPurohithActivity;
    Constants constants;
    Button BookPurohit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purohith_detail_layout);

        PurohithName = (TextView) findViewById(R.id.purohith_name);
        PurohithUnivercity = (TextView) findViewById(R.id.purohith_univercity);
        PurohithSect = (TextView) findViewById(R.id.purohith_sect);
        PurohithGuruName = (TextView) findViewById(R.id.purohith_guru_name);
        PurohithLocation = (TextView) findViewById(R.id.purohith_location);
        PujaPrice = (TextView) findViewById(R.id.price);
        Rating = (TextView) findViewById(R.id.rating);

        BookPurohit = (Button) findViewById(R.id.book_purohit);

        Rating.setText(searchResultPurohithActivity.PurohitExpertLevel);
        PujaPrice.setText(searchResultPurohithActivity.PujaPrice);
        PurohithName.setText(searchResultPurohithActivity.PurohithName);
        PurohithUnivercity.setText(constants.Univercity);
        PurohithSect.setText(constants.Sect);
        PurohithGuruName.setText("");
        PurohithLocation.setText(searchResultPurohithActivity.PurohithLocation);

        BookPurohit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurohithDetailsActivity.this, CheckoutActivityAddress.class);
                intent.putExtra("type","without");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PurohithDetailsActivity.this, SearchResultPurohithActivity.class);
        startActivity(intent);
        finish();
    }
}
