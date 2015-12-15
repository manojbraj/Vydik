package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payUMoney.sdk.SdkConstants;

import vydik.jjbytes.com.constants.Constants;

/**
 * Created by user on 12/11/2015.
 */
public class PaymentSuccess extends ActionBarActivity{
    String status;
    LinearLayout SuccessLayout,FailureLayout;
    TextView Point_one,Point_two,Point_three,Point_four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_success_activity);

        SuccessLayout = (LinearLayout) findViewById(R.id.success_layout);
        FailureLayout = (LinearLayout) findViewById(R.id.failed_transaction);

        Point_one = (TextView) findViewById(R.id.point_one);
        Point_two = (TextView) findViewById(R.id.point_two);
        Point_three = (TextView) findViewById(R.id.point_three);
        Point_four = (TextView) findViewById(R.id.point_four);

        status = getIntent().getStringExtra(SdkConstants.RESULT);
        SuccessLayout.setVisibility(View.VISIBLE);
        FailureLayout.setVisibility(View.GONE);
        if(status.equals("success")) {
            if(BookPujaActivity.package_type.equals("1")){
                Point_one.setVisibility(View.VISIBLE);
                Point_two.setVisibility(View.VISIBLE);
                Point_three.setVisibility(View.VISIBLE);
                if(Constants.BalanceAmount.equals("000")){
                    Point_four.setVisibility(View.GONE);
                }else {
                    Point_four.setVisibility(View.VISIBLE);
                }
            }else {
                Point_one.setVisibility(View.VISIBLE);
                Point_two.setVisibility(View.GONE);
                Point_three.setVisibility(View.GONE);
                if(Constants.BalanceAmount.equals("000")){
                    Point_four.setVisibility(View.GONE);
                }else {
                    Point_four.setVisibility(View.VISIBLE);
                }
            }
        }else {
            FailureLayout.setVisibility(View.VISIBLE);
            SuccessLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PaymentSuccess.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
