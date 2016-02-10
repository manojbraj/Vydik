package vydik.jjbytes.com.Activities;

import android.app.AlertDialog;
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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.Constants;

/**
 * Created by user on 12/9/2015.
 */
public class UserProfileUpdate extends ActionBarActivity {

    TextInputLayout FirstName,PhoneNumber,UserAddress,UserEmail,UserLocation;
    EditText FName,LName,Phone,Address,Email,Locality;
    Button Confirm;
    LinearLayout UpdateProfilePick;
    LinearLayout Camera,Gallery;
    Constants constants;
    /*from gallery*/
    private static int RESULT_LOAD_IMAGE = 1;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Uri fileUri;

    String picturePath   = "no path",ImageUpdateNumber = null,ImageUploadResponse ="no image",ImagePathResponse;

    String UpdateFname,UpdateLname,UpdatePhone,UpdateEmail,UpdateAddress,UpdateLocality;

    MainDatabase database;

    /*http entity*/
    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_update);

        FirstName = (TextInputLayout) findViewById(R.id.fNameLayout);
        PhoneNumber = (TextInputLayout) findViewById(R.id.PhoneLayout);
        UserAddress = (TextInputLayout) findViewById(R.id.address_layout);
        UserEmail = (TextInputLayout) findViewById(R.id.email_layout);
        UserLocation = (TextInputLayout) findViewById(R.id.locality_layout);

        FName = (EditText) findViewById(R.id.fName);
        LName = (EditText) findViewById(R.id.lName);
        Phone = (EditText) findViewById(R.id.PhoneNumber);
        Address = (EditText) findViewById(R.id.edit_address);
        Email = (EditText) findViewById(R.id.edit_email);
        Locality = (EditText) findViewById(R.id.edit_locality);

        UpdateProfilePick = (LinearLayout) findViewById(R.id.update_profile_pick);

        if(UserProfileActivity.UFName!= null){
            FName.setText(UserProfileActivity.UFName);
        }

        if(UserProfileActivity.ULName!= null){
            LName.setText(UserProfileActivity.ULName);
        }

        if(UserProfileActivity.UEmail!= null){
            Email.setText(UserProfileActivity.UEmail);
        }

        if(UserProfileActivity.UMobile!= null){
            Phone.setText(UserProfileActivity.UMobile);
            ImageUpdateNumber = Phone.getText().toString();
            System.out.println("phone : " + ImageUpdateNumber);
        }

        if(UserProfileActivity.ULocality!= null){
            Locality.setText(UserProfileActivity.ULocality);
        }

        if(UserProfileActivity.UAddress!= null){
            Address.setText(UserProfileActivity.UAddress);
        }

        UpdateProfilePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCameraGalleryPopup();
            }
        });
        Confirm = (Button) findViewById(R.id.continue_button);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FName.getText().toString().equals(UserProfileActivity.UFName) && LName.getText().toString().equals(UserProfileActivity.ULName) && Email.getText().toString().equals(UserProfileActivity.UEmail) && Phone.getText().toString().equals(UserProfileActivity.UMobile) && Address.getText().toString().equals(UserProfileActivity.UAddress) && Locality.getText().toString().equals(UserProfileActivity.ULocality)){
                    Toast.makeText(getApplicationContext(),"You are trying to update the previous profile without changes...",Toast.LENGTH_LONG).show();
                }else {
                    if (validate(new EditText[]{FName,LName,Email,Phone,Locality,Address})){
                        UpdateFname = FName.getText().toString();
                        UpdateLname = LName.getText().toString();
                        UpdatePhone = Phone.getText().toString();
                        UpdateEmail = Email.getText().toString();
                        UpdateAddress = Address.getText().toString();
                        UpdateLocality = Locality.getText().toString();
                        new CallUpdateUserProfile().execute();
                    }else {
                        Toast.makeText(getApplicationContext(),"Please make sure you have entered all fields...",Toast.LENGTH_LONG).show();
                    }
                }
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

    /*camera and galery functionality start fro m hear*/
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
            System.out.println("galery path" + picturePath + "added path :" + constants.ImagePath);
            cursor.close();
            //GetImageName();
            if(picturePath.equals("no path")){

            }else {
                new UploadImageFile().execute();
            }
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
        } else {

        }
    }

    private void launchUploadActivity(boolean b) {
        constants.ImagePath = fileUri.getPath();
        picturePath = constants.ImagePath;
        System.out.println("camera path 1:" + picturePath + " camera path 2: " + constants.ImagePath);
        //GetImageName();
        if(picturePath.equals("no path")){

        }else {
            new UploadImageFile().execute();
        }
    }

    /*ends hear camers and gallery*/
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

    /*final user registartion submit backgrounf=d process*/
    class CallUpdateUserProfile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(UserProfileUpdate.this, Constants.UserProfileUpdateProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Constants.UserProfileUpdateURL);
            try{
                //System.out.println(UpdateFname+"  "+UpdateLname +"  "+UpdatePhone + " "+UpdateEmail+ " "+UpdateAddress+" "+UpdateLocality);
                multipartEntity.addPart(Constants.UpdateFName,new StringBody(UpdateFname));
                multipartEntity.addPart(Constants.UpdateLName,new StringBody(UpdateLname));
                multipartEntity.addPart(Constants.UpdateMobileNo,new StringBody(UpdatePhone));
                multipartEntity.addPart(Constants.UpdateEmail,new StringBody(UpdateEmail));
                multipartEntity.addPart(Constants.UpdateAddress,new StringBody(UpdateAddress));
                multipartEntity.addPart(Constants.UpdateLocality,new StringBody(UpdateLocality));
            }catch (Exception e){

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
            String respo = null;
            try{
                JSONObject object = new JSONObject(s);
                if(object.has("sucess")){
                    if(object.getString("sucess")!= null){
                        respo = object.getString("sucess").toString();
                    }
                }

                if(respo.equals("Profile Updated")){
                    database = new MainDatabase(UserProfileUpdate.this);
                    database = database.open();
                    database.UpdateUserProfile(UpdatePhone,UpdateFname,UpdateLname,UpdateEmail,UpdatePhone,UpdateLocality,UpdateAddress);
                    database.close();
                    Toast.makeText(getApplicationContext(),"profile update successful",Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }catch (Exception E){

            }
        }
    }
    class UploadImageFile extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utilities.displayProgressDialog(UserProfileUpdate.this, Constants.UserProfileUpdateProgress, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Constants.UserProfilePickChangeURL);
            try{
                File sourcefile = new File(picturePath);
                FileBody fileBody = new FileBody(sourcefile);
                multipartEntity.addPart(constants.UK19, fileBody);
                multipartEntity.addPart("mobno",new StringBody(ImageUpdateNumber));
            }catch (Exception e){

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
            System.out.println("HttpResponse :" + s);
            try {
                JSONObject object = new JSONObject(s);
                if(object.has("sucess")){
                    if(object.getString("sucess")!= null){
                        ImageUploadResponse = object.getString("sucess").toString();
                    }else {

                    }
                }else {

                }

                if(object.has("image")){
                    if(object.getString("image")!= null){
                        ImagePathResponse = "http://vydik.com/"+object.getString("image").toString();
                    }else {

                    }
                }else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("HttpResponse :" + ImagePathResponse);
            if(ImageUploadResponse.equals("Success")){
                database = new MainDatabase(UserProfileUpdate.this);
                database = database.open();
                database.UpdateUserProfileImage(ImageUpdateNumber, ImagePathResponse);
                database.close();
                Toast.makeText(getApplicationContext(),"Profile pic updated successfully...",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"Failed to upload the pic please try after some time...",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfileUpdate.this,UserProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
