package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.payUMoney.sdk.SdkConstants;

/**
 * Created by user on 12/11/2015.
 */
public class PaymentSuccess extends ActionBarActivity{
    String status;
    LinearLayout SuccessLayout,FailureLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_success_activity);

        SuccessLayout = (LinearLayout) findViewById(R.id.success_layout);
        FailureLayout = (LinearLayout) findViewById(R.id.failed_transaction);
        status = getIntent().getStringExtra(SdkConstants.RESULT);
        if(status.equals("success")) {
            SuccessLayout.setVisibility(View.VISIBLE);
            FailureLayout.setVisibility(View.GONE);
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
