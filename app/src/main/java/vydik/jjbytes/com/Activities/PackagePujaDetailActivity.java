package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 11/2/2015.
 */
public class PackagePujaDetailActivity extends ActionBarActivity {
    TextView PurohithName,PurohithUnivercity,PurohithSect,PurohithGuruName,PurohithLocation,PujaPrice,PurohitRating,AdvanceAmount,BalanceAmount;
    ArrayListConstants arrayListConstants;
    Button BookPurohit;
    BookPujaActivity bookPujaActivity;
    Constants constants;
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
        PurohitRating = (TextView) findViewById(R.id.rating);
        AdvanceAmount = (TextView) findViewById(R.id.advance_amount);
        BalanceAmount = (TextView) findViewById(R.id.balance_amount);

        BookPurohit = (Button) findViewById(R.id.book_purohit);

        if(BookPujaActivity.package_type.equals("1")){

        }
        PujaPrice.setText(constants.package_price);
        PurohitRating.setText(constants.package_expertise);
        PurohithName.setText(constants.SPujaName);
        PurohithUnivercity.setText(constants.package_university);
        PurohithSect.setText(constants.package_sect);
        PurohithGuruName.setText("");
        PurohithLocation.setText(constants.package_location);
        AdvanceAmount.setText(constants.AdvanceAmount);
        BalanceAmount.setText(constants.BalanceAmount);

        BookPurohit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackagePujaDetailActivity.this,CheckoutActivityAddress.class);
                intent.putExtra("type","with");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        arrayListConstants.PurohithFirstName.clear();
        arrayListConstants.PurohithExpertLevel.clear();
        arrayListConstants.PurohithLocation.clear();
        arrayListConstants.PurohithPrice.clear();
        arrayListConstants.PurohithPhoto.clear();
        bookPujaActivity.PoojaName.clear();
        bookPujaActivity.PoojaTypeId.clear();
        bookPujaActivity.PoojaId.clear();
        bookPujaActivity.LocationName.clear();
        bookPujaActivity.LangugesName.clear();
        bookPujaActivity.PurohithSect.clear();
        /*Intent intent = new Intent(PackagePujaDetailActivity.this,BookPujaActivity.class);
        intent.putExtra("type",BookPujaActivity.package_type);
        startActivity(intent);
        finish();*/
    }
}
