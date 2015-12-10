package vydik.jjbytes.com.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by manoj on 10/17/2015.
 */
public class UserRegistrationFinalSignUp extends ActionBarActivity{
    /*from gallery*/
    private static int RESULT_LOAD_IMAGE = 1;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Uri fileUri;

    String picturePath   = "no path";
    EditText FriendReffName,FriendReffNumber,PurohithReffName,PurohithReffNumber;
    TextView DateOfBirth, Anniversary,ImageSelected;
    Spinner ReferenceType;
    Button FinalSignUp,UploadUserPick;
    Constants constants;
    String UserSuccess;
    MainDatabase database;

    LinearLayout FriendReffLayout,PurohithReffLayout,reffnumfriend,reffnumpurohit;
    LinearLayout Camera,Gallery;
    /*date picker constants*/
    private int ALmonthOfYear, ALdayOfMonth,ALyear;
    String strdate;

    /*spinner v string*/
    String ReferenceR = "0";

    int resAge;

    /*http entity*/
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new MainDatabase(this);
        database = database.open();

        setContentView(R.layout.user_registration_form_two);

        FriendReffName = (EditText) findViewById(R.id.reff_friend_name);
        FriendReffNumber = (EditText) findViewById(R.id.reff_friend_number);
        PurohithReffName = (EditText) findViewById(R.id.reff_purohith_name);
        PurohithReffNumber = (EditText) findViewById(R.id.reff_purohith_number);

        DateOfBirth = (TextView) findViewById(R.id.date_of_birth);
        Anniversary = (TextView) findViewById(R.id.Anniversary);
        ImageSelected = (TextView) findViewById(R.id.image_selected);

        ReferenceType = (Spinner) findViewById(R.id.reference_spinner);

        FinalSignUp = (Button) findViewById(R.id.user_sign_up);
        UploadUserPick = (Button) findViewById(R.id.choose_file_id);

        FriendReffLayout = (LinearLayout) findViewById(R.id.ref_friend_name_layout);
        reffnumfriend = (LinearLayout) findViewById(R.id.ref_friend_number_layout);

        PurohithReffLayout = (LinearLayout) findViewById(R.id.reff_purohith_name_layout);
        reffnumpurohit = (LinearLayout) findViewById(R.id.ref_friend_number_layout);

        DateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerMethod();
            }
        });

        Anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnniversaryDatePickerMethod();
            }
        });

        UploadUserPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCameraGalleryPopup();
            }
        });

        FinalSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(constants.UserDOB != null){
                       /* if(constants.ImagePath != null) {*/
                            if (ReferenceType.getSelectedItem().toString().trim().equals("Reference")) {
                                ReferenceR = "1";
                                CheckSpinnerMethod();
                            } else {
                                CheckSpinnerMethod();
                            }
                       /* }else{
                            Toast.makeText(UserRegistrationFinalSignUp.this, "Please upload your profile picture", Toast.LENGTH_LONG).show();
                        }*/
                }else{
                    Toast.makeText(UserRegistrationFinalSignUp.this,"Please select your date of birth",Toast.LENGTH_LONG).show();
                }
            }
        });
        ReferenceType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ReferenceType.getSelectedItem().toString().trim().equals("Friend")) {
                    constants.ReferredFrom = "Friend";
                    PurohithReffLayout.setVisibility(View.GONE);
                    reffnumpurohit.setVisibility(View.GONE);
                    FriendReffLayout.setVisibility(View.VISIBLE);
                    reffnumfriend.setVisibility(View.VISIBLE);
                } else if (ReferenceType.getSelectedItem().toString().trim().equals("Purohith")) {
                    constants.ReferredFrom = "Purohith";
                    FriendReffLayout.setVisibility(View.GONE);
                    reffnumfriend.setVisibility(View.GONE);
                    PurohithReffLayout.setVisibility(View.VISIBLE);
                    reffnumpurohit.setVisibility(View.VISIBLE);
                } else if(ReferenceType.getSelectedItem().toString().trim().equals("Advertisement")){
                    constants.ReferredFrom = "Advertisement";
                    FriendReffLayout.setVisibility(View.GONE);
                    reffnumfriend.setVisibility(View.GONE);
                    PurohithReffLayout.setVisibility(View.GONE);
                    reffnumpurohit.setVisibility(View.GONE);
                }else if(ReferenceType.getSelectedItem().toString().trim().equals("Website")){
                    constants.ReferredFrom = "Website";
                    FriendReffLayout.setVisibility(View.GONE);
                    reffnumfriend.setVisibility(View.GONE);
                    PurohithReffLayout.setVisibility(View.GONE);
                    reffnumpurohit.setVisibility(View.GONE);
                }else if(ReferenceType.getSelectedItem().toString().trim().equals("Other")){
                    constants.ReferredFrom = "Other";
                    FriendReffLayout.setVisibility(View.GONE);
                    reffnumfriend.setVisibility(View.GONE);
                    PurohithReffLayout.setVisibility(View.GONE);
                    reffnumpurohit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    private void launchUploadActivity(boolean b) {
        constants.ImagePath = fileUri.getPath();
        picturePath = constants.ImagePath;
        System.out.println("camera path 1:" + picturePath + " camera path 2: "+ constants.ImagePath);
        GetImageName();
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + Constants.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }else {
            return null;
        }

        return mediaFile;
    }

    private void CallCameraGalleryPopup() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.camera_gallery_popup, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Camera = (LinearLayout) PromtView.findViewById(R.id.camera_functionality);
        Gallery = (LinearLayout) PromtView.findViewById(R.id.gallery_functionality);

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*start camera implementation hear*/
                alertD.dismiss();
                captureImage();
            }
        });

        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }

    /*on gallery image get*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            constants.ImagePath = picturePath;
            System.out.println("galery path"+ picturePath + "added path :" + constants.ImagePath);
            cursor.close();
            GetImageName();
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void GetImageName() {
        Intent i = getIntent();
        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);
        if (picturePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
    }
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            //Profile.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
            String path = picturePath;
            String[] a = path.split("/");
            String imageTitle = a[(a.length-1)];
            ImageSelected.setText(imageTitle);
            ImageSelected.setVisibility(View.VISIBLE);
            UploadUserPick.setVisibility(View.GONE);
        } else {

        }
    }

    /*validate reference edit box and submit the final registration details*/
    private void CheckSpinnerMethod() {
        if(ReferenceR.equals("1")){
            ReferenceR = "0";
            Toast.makeText(UserRegistrationFinalSignUp.this,"Please Select Your Reference type",Toast.LENGTH_LONG).show();
        }else if(resAge < 18){
            Toast.makeText(UserRegistrationFinalSignUp.this,"Please Select Your Age",Toast.LENGTH_LONG).show();
        }
        else{
            if(ReferenceType.getSelectedItem().toString().trim().equals("Friend")){
                if (validate(new EditText[]{FriendReffName,FriendReffNumber})){
                    constants.URefFName = FriendReffName.getText().toString();
                    constants.URefNNumber = FriendReffNumber.getText().toString();
                    new FinalRegistrationSubbmit().execute();
                }else{
                    Toast.makeText(UserRegistrationFinalSignUp.this,"Please Make sure you have entered referal details",Toast.LENGTH_LONG).show();
                }
            }else if(ReferenceType.getSelectedItem().toString().trim().equals("Purohith")){
                if (validate(new EditText[]{PurohithReffName,PurohithReffNumber})){
                    constants.URefPName = PurohithReffName.getText().toString();
                    constants.URefPNumber = PurohithReffNumber.getText().toString();
                    new FinalRegistrationSubbmit().execute();
                }else{
                    Toast.makeText(UserRegistrationFinalSignUp.this,"Please Make sure you have entered referal details",Toast.LENGTH_LONG).show();
                }
            }else{
                new FinalRegistrationSubbmit().execute();
            }
        }
    }

    /*edit text box validator*/
    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }

    /*anniversary date picker*/
    private void AnniversaryDatePickerMethod() {
        final Calendar c = Calendar.getInstance();
        ALyear = c.get(Calendar.YEAR);
        ALmonthOfYear = c.get(Calendar.MONTH);
        ALdayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ALyear=year;
                        ALmonthOfYear=monthOfYear;
                        ALdayOfMonth=dayOfMonth;
                        strdate = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        constants.UserAnneversery = strdate;
                        Anniversary.setText(strdate);
                    }
                }, ALyear, ALmonthOfYear, ALdayOfMonth);
        dpd.show();
    }

/*dob date picker*/
    private void DatePickerMethod() {
        final Calendar c = Calendar.getInstance();
        ALyear = c.get(Calendar.YEAR);
        ALmonthOfYear = c.get(Calendar.MONTH);
        ALdayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ALyear=year;
                        ALmonthOfYear=monthOfYear;
                        ALdayOfMonth=dayOfMonth;
                        strdate = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        constants.UserDOB = strdate;
                        int y = year;
                        int check = c.get(Calendar.YEAR);
                        resAge = check - y;
                        if(resAge < 18 ){
                            Toast.makeText(UserRegistrationFinalSignUp.this,"Your age Should not be less than 18 years",Toast.LENGTH_LONG).show();
                        }else {
                            DateOfBirth.setText(strdate);
                        }
                    }
                }, ALyear, ALmonthOfYear, ALdayOfMonth);
        dpd.show();
    }

    /*final user registartion submit backgrounf=d process*/
class FinalRegistrationSubbmit extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Utilities.displayProgressDialog(UserRegistrationFinalSignUp.this, constants.UserRegistrationProgress, false);
    }

    @Override
    protected String doInBackground(String... params) {
        System.out.println("file path:" + constants.ImagePath);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(constants.SubmitUserRegistration);
        try{
            multipartEntity.addPart(constants.UK1,new StringBody(constants.submit));
            multipartEntity.addPart(constants.UK2,new StringBody(constants.UserFName));
            multipartEntity.addPart(constants.UK3,new StringBody(constants.UserLName));
            multipartEntity.addPart(constants.UK4,new StringBody(constants.UserEmail));
            multipartEntity.addPart(constants.UK5,new StringBody(constants.UserPassword));
            multipartEntity.addPart(constants.UK6,new StringBody(constants.GetCity));
            multipartEntity.addPart(constants.UK7,new StringBody(constants.GetState));
            multipartEntity.addPart(constants.UK8,new StringBody(constants.GetLocality));
            multipartEntity.addPart(constants.UK9,new StringBody(constants.GetPinCode));
            multipartEntity.addPart(constants.UK10,new StringBody(constants.UserDOB));
            multipartEntity.addPart(constants.UK11,new StringBody(constants.UserAnneversery));
            multipartEntity.addPart(constants.UK12,new StringBody(constants.ReferredFrom));
            multipartEntity.addPart(constants.UK13,new StringBody(constants.UserContact));
            multipartEntity.addPart(constants.UK14,new StringBody(constants.UserAddress));
            multipartEntity.addPart(constants.UK15,new StringBody(constants.URefFName));
            multipartEntity.addPart(constants.UK16,new StringBody(constants.URefNNumber));
            multipartEntity.addPart(constants.UK17,new StringBody(constants.URefPName));
            multipartEntity.addPart(constants.UK18,new StringBody(constants.URefPNumber));
            multipartEntity.addPart("gend", new StringBody(UserRegistrationFormOne.GenderValue));

            if(picturePath.equals("no path")){
                multipartEntity.addPart(constants.UK19, new StringBody("no image"));
            }else {
                File sourcefile = new File(picturePath);
                FileBody fileBody = new FileBody(sourcefile);
                multipartEntity.addPart(constants.UK19, fileBody);
            }
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try{
            httpPost.setEntity(multipartEntity);
            try{
                HttpResponse httpResponse = httpClient.execute(httpPost);
                InputStream inputStream = httpResponse.getEntity()
                        .getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                    stringBuilder.append(bufferedStrChunk);
                }
                return stringBuilder.toString();

            }catch (ClientProtocolException cpe) {
                System.out.println("First Exception caz of HttpResponese :"
                        + cpe);
                cpe.printStackTrace();
            }catch (IOException ioe) {
                System.out.println("Second Exception caz of HttpResponse :"
                        + ioe);
                ioe.printStackTrace();
            }catch (Exception e){

            }
        }
        catch (IndexOutOfBoundsException e) {

        }
        catch (Exception e){

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Utilities.cancelProgressDialog();
        System.out.println("server message" + s);
        try{
            JSONArray array = new JSONArray(s);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                if (jsonObject.has("First_name")) {
                    if (jsonObject.getString("First_name") != null) {
                        constants.UL1 = jsonObject.getString("First_name").toString();
                        System.out.println("success message : " + constants.UL1);
                    } else {

                    }
                } else {

                }

                if (jsonObject.has("last_name")) {
                    if (jsonObject.getString("last_name") != null) {
                        constants.UL2 = jsonObject.getString("last_name").toString();
                        System.out.println("success message : " + constants.UL2);
                    } else {

                    }
                } else {

                }

                if (jsonObject.has("email_id")) {
                    if (jsonObject.getString("email_id") != null) {
                        constants.UL3 = jsonObject.getString("email_id").toString();
                        System.out.println("success message : " + constants.UL3);
                    } else {

                    }
                } else {

                }

                if (jsonObject.has("mobile_no")) {
                    if (jsonObject.getString("mobile_no") != null) {
                        constants.UL4 = jsonObject.getString("mobile_no").toString();
                        System.out.println("success message : " + constants.UL4);
                    } else {

                    }
                } else {

                }

                if (jsonObject.has("locality")) {
                    if (jsonObject.getString("locality") != null) {
                        constants.UL5 = jsonObject.getString("locality").toString();
                        System.out.println("success message : " + constants.UL5);
                    } else {

                    }
                } else {

                }

                if (jsonObject.has("state")) {
                    if (jsonObject.getString("state") != null) {
                        constants.UL6 = jsonObject.getString("state").toString();
                        System.out.println("success message : " + constants.UL6);
                    } else {

                    }
                } else {

                }

                if (jsonObject.has("address")) {
                    if (jsonObject.getString("address") != null) {
                        constants.UL7 = jsonObject.getString("address").toString();
                        System.out.println("success message : " + constants.UL7);
                    } else {

                    }
                } else {

                }

                if (jsonObject.has("user_id")) {
                    if (jsonObject.getString("user_id") != null) {
                        constants.UL8 = jsonObject.getString("user_id").toString();
                        System.out.println("success message : " + constants.UL8);
                    } else {

                    }
                } else {

                }

                if (jsonObject.has("success")) {
                    if (jsonObject.getString("success") != null) {
                        constants.UL9 = jsonObject.getString("success").toString();
                        UserSuccess = jsonObject.getString("success").toString();
                        System.out.println("success message : " + UserSuccess);
                    } else {

                    }
                } else {

                }

                if(jsonObject.has("profile_picture")){
                    if(jsonObject.getString("profile_picture")!= null){
                        constants.UL10 = jsonObject.getString("profile_picture").toString();
                    }else{

                    }
                }else{

                }
            }
           /* JSONObject object = new JSONObject(s);
            String result = object.getString("success").toString();*/
            String Image = "add image hear";
            if(UserSuccess.equals("Sucessfully")){
                if(UserRegistrationFormOne.InputType.equals("facebook")){
                    database.insertLogin(LoginActivity.email,LoginActivity.FacebookImage,"facebook");
                    database.InserUserLoginData(constants.UL1, constants.UL2, constants.UL3, constants.UL4, constants.UL5, constants.UL6,
                            constants.UL7, constants.UL8, constants.UL10);
                    Intent intent = new Intent(UserRegistrationFinalSignUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(UserRegistrationFormOne.InputType.equals("google")){
                    database.insertLogin(LoginActivity.email,LoginActivity.personPhotoUrl,"google");
                    database.InserUserLoginData(constants.UL1, constants.UL2, constants.UL3, constants.UL4, constants.UL5, constants.UL6,
                            constants.UL7, constants.UL8, constants.UL10);
                    Intent intent = new Intent(UserRegistrationFinalSignUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    database.insertLogin(constants.UserFName, Image, "user");
                    database.InserUserLoginData(constants.UL1, constants.UL2, constants.UL3, constants.UL4, constants.UL5, constants.UL6,
                            constants.UL7, constants.UL8, constants.UL10);
                    Intent intent = new Intent(UserRegistrationFinalSignUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else{
                Toast.makeText(UserRegistrationFinalSignUp.this,"Something went wrong please try after some time",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){

        }
    }
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}