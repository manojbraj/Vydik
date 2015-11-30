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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import java.util.Date;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.TileOverlay;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import vydik.jjbytes.com.constants.Constants;

/**
 * Created by Manoj on 10/13/2015.
 */
public class RegistrationFormOne extends ActionBarActivity {
    /*from gallery*/
    private static int RESULT_LOAD_IMAGE = 1;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private Uri fileUri;

    String picturePath;

    Button Next,Camera_gallery;
    EditText FirstName,LastName,EmailId,Password,ConfirmPassword,PhoneNumber,univercityName;
    Spinner EducationLevel;
    TextView Date,ImageSelected;
    private int ALmonthOfYear, ALdayOfMonth,ALyear;
    String strdate;
    Constants constants;
    LinearLayout Camera,Gallery;
    String BackStatus = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pujari_registration_form_one);

        Next = (Button) findViewById(R.id.next_one);
        Camera_gallery = (Button) findViewById(R.id.choose_file_id);

        FirstName = (EditText) findViewById(R.id.first_name);
        LastName = (EditText) findViewById(R.id.last_name);
        EmailId = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        ConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        PhoneNumber = (EditText) findViewById(R.id.mobile_number);
        univercityName = (EditText) findViewById(R.id.univercity_name);

        Date = (TextView) findViewById(R.id.date_of_birth);
        ImageSelected = (TextView) findViewById(R.id.image_selected);

        EducationLevel = (Spinner) findViewById(R.id.education_spinner);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(new EditText[]{FirstName, LastName, EmailId, Password, ConfirmPassword, PhoneNumber, univercityName})) {
                    if (isValidEmail(EmailId.getEditableText().toString())) {
                        if (Password.getText().toString().length() >= 6) {
                            if (Password.getText().toString()
                                    .equals(ConfirmPassword.getText().toString())) {
                                if (PhoneNumber.getText().toString().length() == 10) {
                                    if(constants.ImagePath != null) {
                                        constants.FirstNameValue = FirstName.getText().toString();
                                        constants.LastNameValue = LastName.getText().toString();
                                        constants.EmailValue = EmailId.getText().toString();
                                        constants.PasswordValue = Password.getText().toString();
                                        constants.ConfirmPasswordValue = ConfirmPassword.getText().toString();
                                        constants.PhoneNumberValue = PhoneNumber.getText().toString();
                                        constants.UnivercityNameValue = univercityName.getText().toString();
                                        constants.EducationValue = EducationLevel.getSelectedItem().toString();
                                        CallNextActivity();
                                    }else{
                                        Toast.makeText(RegistrationFormOne.this, "Please upload your id prough", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(RegistrationFormOne.this, "Please make sure the entered mobile number is valid", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(RegistrationFormOne.this, "Please make sure your password matches", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegistrationFormOne.this, "Please make sure your password is greater than 6 characters", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegistrationFormOne.this, "Please enter a valid email id like vydik@domain.com", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegistrationFormOne.this, "Please Make sure u have filled all mandatory fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerMethod();
            }
        });

        Camera_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCameraGalleryPopup();
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

    private void CallNextActivity() {
            Intent intent = new Intent(RegistrationFormOne.this,RegistrationFormTwo.class);
            startActivity(intent);
            finish();
    }

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
                        constants.DateOfBirthValue = strdate;
                        Date.setText(strdate);
                    }
                }, ALyear, ALmonthOfYear, ALdayOfMonth);
        dpd.show();
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
/*email validator*/

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
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
            Camera_gallery.setVisibility(View.GONE);
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        if(BackStatus.equals("0")){
            CallBackPopup();
        }else {
            super.onBackPressed();
            Intent intent = new Intent(RegistrationFormOne.this, LoginActivity.class);
            intent.putExtra("login_type", "purohit");
            startActivity(intent);
            finish();
        }
    }

    private void CallBackPopup() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.allert_box, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD.setCancelable(false);
        Button cancel = (Button) PromtView.findViewById(R.id.cancel_alert);
        Button finish = (Button) PromtView.findViewById(R.id.finish_activity);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
                BackStatus = "1";
                onBackPressed();
            }
        });
        alertD.setView(PromtView);
        alertD.show();
    }
}