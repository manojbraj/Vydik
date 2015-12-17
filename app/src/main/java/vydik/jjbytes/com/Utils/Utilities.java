package vydik.jjbytes.com.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;

import vydik.jjbytes.com.Activities.R;

/**
 * Created by manoj on 10/10/2015.
 */
public class Utilities {
    public static Boolean TRANSACTION_HEADER_OPEN = false;
    private static ProgressDialog mProgressDialog = null;

    @SuppressLint("NewApi")
    public static void displayProgressDialog(Context context,String message, Boolean backButtonCancelable){
        if(mProgressDialog == null && context != null){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(backButtonCancelable);
        }
    }

    public static void cancelProgressDialog(){
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }
}
