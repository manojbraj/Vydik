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
    TextView PurohithName,PurohithUnivercity,PurohithSect,PurohithGuruName,PurohithLocation,PujaPrice,Rating,AdvanceAmount,BalanceAmount,AdvanceText,BalanceText;
    SearchResultPurohithActivity searchResultPurohithActivity;
    Constants constants;
    Button BookPurohit;
    TextView point_two,point_three,point_four;

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
        AdvanceAmount = (TextView) findViewById(R.id.advance_amount);
        BalanceAmount = (TextView) findViewById(R.id.balance_amount);
        AdvanceText = (TextView) findViewById(R.id.advance_text);
        BalanceText = (TextView) findViewById(R.id.balance_text);

        point_two = (TextView) findViewById(R.id.point_two);
        point_three = (TextView) findViewById(R.id.point_three);
        point_four = (TextView) findViewById(R.id.point_four);

        if(BookPujaActivity.package_type.equals("1")){
            point_two.setVisibility(View.VISIBLE);
            point_three.setVisibility(View.VISIBLE);
            if(Constants.BalanceAmount.equals("000")){
                point_four.setVisibility(View.GONE);
            }else {
                point_four.setVisibility(View.VISIBLE);
            }
        } else {
            point_two.setVisibility(View.GONE);
            point_three.setVisibility(View.GONE);
            if(Constants.BalanceAmount.equals("Rs.000/-")){
                point_four.setVisibility(View.GONE);
                BalanceText.setVisibility(View.GONE);
                BalanceAmount.setVisibility(View.GONE);
                AdvanceText.setText("Puja Price");
            }else {
                point_four.setVisibility(View.VISIBLE);
            }
        }


        BookPurohit = (Button) findViewById(R.id.book_purohit);

        Rating.setText(searchResultPurohithActivity.PurohitExpertLevel);
        PujaPrice.setText(searchResultPurohithActivity.PujaPrice);
        PurohithName.setText(searchResultPurohithActivity.PurohithName);
        PurohithUnivercity.setText(constants.Univercity);
        PurohithSect.setText(constants.Sect);
        PurohithGuruName.setText("");
        PurohithLocation.setText(searchResultPurohithActivity.PurohithLocation);

        if(constants.AdvanceAmount.equals("000")){
            AdvanceAmount.setText(constants.package_price);
            //constants.PayementGatewayAmount = constants.PaymentErrorAdvance;
        }else {
            AdvanceAmount.setText(constants.AdvanceAmount);
        }

        BalanceAmount.setText(constants.BalanceAmount);

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
