package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.GetUserLoginData;
import vydik.jjbytes.com.constants.ArrayListConstants;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 11/2/2015.
 */
public class PackagePujaDetailActivity extends ActionBarActivity {
    TextView PurohithName,PurohithUnivercity,PurohithSect,PurohithGuruName,PurohithLocation,PujaPrice,PurohitRating,AdvanceAmount
            ,BalanceAmount,AdvanceText,BalanceText,PujaDescription,ServerErrorMessage;
    ArrayListConstants arrayListConstants;
    Button BookPurohit;
    BookPujaActivity bookPujaActivity;
    Constants constants;
    TextView point_two,point_three,point_four;
    LinearLayout LayoutPurohitDetail;
    MainDatabase database;
    ArrayList<GetUserLoginData> getUserLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purohith_detail_layout);

        database = new MainDatabase(this);
        database = database.open();
        getUserLoginData = database.getUserLoginDetails();

        PurohithName = (TextView) findViewById(R.id.purohith_name);
        PurohithUnivercity = (TextView) findViewById(R.id.purohith_univercity);
        PurohithSect = (TextView) findViewById(R.id.purohith_sect);
        PurohithGuruName = (TextView) findViewById(R.id.languages);
        PurohithLocation = (TextView) findViewById(R.id.purohith_location);
        PujaPrice = (TextView) findViewById(R.id.price);
        PurohitRating = (TextView) findViewById(R.id.rating);
        AdvanceAmount = (TextView) findViewById(R.id.advance_amount);
        BalanceAmount = (TextView) findViewById(R.id.balance_amount);
        point_two = (TextView) findViewById(R.id.point_two);
        point_three = (TextView) findViewById(R.id.point_three);
        point_four = (TextView) findViewById(R.id.point_four);
        AdvanceText = (TextView) findViewById(R.id.advance_text);
        BalanceText = (TextView) findViewById(R.id.balance_text);
        PujaDescription = (TextView) findViewById(R.id.puja_details);
        ServerErrorMessage = (TextView) findViewById(R.id.error_message);

        if(constants.SearchMessage.equals("null")){

        }else {
            ServerErrorMessage.setText(constants.SearchMessage);
        }
        LayoutPurohitDetail = (LinearLayout) findViewById(R.id.layout_three);

        LayoutPurohitDetail.setVisibility(View.GONE);

        if(BookPujaActivity.package_type.equals("1")){
            point_two.setVisibility(View.VISIBLE);
            point_three.setVisibility(View.VISIBLE);
            System.out.println("balance" + Constants.BalanceAmount);
            if(Constants.BalanceAmount.equals("000")){
                point_four.setVisibility(View.GONE);
                BalanceText.setVisibility(View.GONE);
                BalanceAmount.setVisibility(View.GONE);
                AdvanceText.setText("Puja Price");
            }else {
                point_four.setVisibility(View.VISIBLE);
            }
        } else {
            point_two.setVisibility(View.GONE);
            point_three.setVisibility(View.GONE);
            if(Constants.BalanceAmount.equals("000")){
                point_four.setVisibility(View.GONE);
            }else {
                point_four.setVisibility(View.VISIBLE);
            }
        }

        BookPurohit = (Button) findViewById(R.id.book_purohit);

        PujaPrice.setText(constants.package_price);
        PurohitRating.setText(constants.package_expertise);
        PurohithName.setText(constants.SPujaName);
        PurohithUnivercity.setText(constants.package_university);
        PujaDescription.setText(constants.PujaDescription);
        PurohithGuruName.setText(constants.Languages);
        //PurohithLocation.setText(constants.package_location);
        if(constants.AdvanceAmount.equals("000")){
            AdvanceAmount.setText(constants.package_price);
            constants.PayementGatewayAmount = constants.PaymentErrorAdvance;
        }else {
            AdvanceAmount.setText(constants.AdvanceAmount);
        }
        BalanceAmount.setText(constants.BalanceAmount);

        BookPurohit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getUserLoginData.size() == 0){
                    SplashScreenActivity.GCMLoginType = "new";
                    constants.LoginFrom = "PackagePuja";
                    Intent intent = new Intent(PackagePujaDetailActivity.this, LoginActivity.class);
                    intent.putExtra("login_type", "user");
                    intent.putExtra("activity","PujaActivity");
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(PackagePujaDetailActivity.this,CheckoutActivityAddress.class);
                    intent.putExtra("type","with");
                    startActivity(intent);
                    finish();
                }
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
        BookPujaActivity.package_type = "1";
        BookPujaActivity.newdateupdated ="";
        constants.SearchMessage = "null";
        constants.BalanceAmount="000";
       /* bookPujaActivity.PoojaName.clear();
        bookPujaActivity.PoojaTypeId.clear();
        bookPujaActivity.PoojaId.clear();
        bookPujaActivity.LocationName.clear();
        bookPujaActivity.LangugesName.clear();
        bookPujaActivity.PurohithSect.clear();*/
        Intent intent = new Intent(PackagePujaDetailActivity.this,BookPujaActivity.class);
        intent.putExtra("type",BookPujaActivity.package_type);
        startActivity(intent);
        finish();
    }
}