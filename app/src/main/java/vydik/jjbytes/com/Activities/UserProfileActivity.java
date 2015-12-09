package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Models.GetUserLoginData;

/**
 * Created by Manoj on 11/24/2015.
 */
public class UserProfileActivity extends AppCompatActivity{

    MainDatabase database;

    ArrayList<GetUserLoginData> getUserLoginData;
    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> UserFirstName = new ArrayList<String>();
    private ArrayList<String> UserLastName= new ArrayList<String>();
    private ArrayList<String> UserEmail = new ArrayList<String>();
    private ArrayList<String> UserMobile = new ArrayList<String>();
    private ArrayList<String> UserLocality = new ArrayList<String>();
    private ArrayList<String> UserState = new ArrayList<String>();
    private ArrayList<String> UserAAddress = new ArrayList<String>();
    private ArrayList<String> UserId = new ArrayList<String>();
    private ArrayList<String> UserImage = new ArrayList<String>();

    public static String UFName,ULName,UEmail,UMobile,ULocality,UState,UAddress,UId,UImage;

    TextView UserName,Email,Phone,Locality,Address;
    Toolbar toolbar;
    LinearLayout BackPress;
    FloatingActionButton EditProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);

        toolbar = (Toolbar) findViewById(R.id.tab_layout);
        BackPress = (LinearLayout) findViewById(R.id.back_press);
        BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        database = new MainDatabase(this);
        database = database.open();
        getUserLoginData = database.getUserLoginDetails();

        if(getUserLoginData.size() == 0){

        }else {
            for(int i=0;i<getUserLoginData.size();i++){
                id.add(getUserLoginData.get(i).getId());
                UserFirstName.add(getUserLoginData.get(i).getFname());
                UserLastName.add(getUserLoginData.get(i).getLname());
                UserEmail.add(getUserLoginData.get(i).getEmail());
                UserMobile.add(getUserLoginData.get(i).getMobile());
                UserLocality.add(getUserLoginData.get(i).getLocality());
                UserState.add(getUserLoginData.get(i).getState());
                UserAAddress.add(getUserLoginData.get(i).getAddress());
                UserId.add(getUserLoginData.get(i).getUserid());
                UserImage.add(getUserLoginData.get(i).getImage());

                UFName = UserFirstName.get(0).toString();
                ULName = UserLastName.get(0).toString();
                UEmail = UserEmail.get(0).toString();
                UMobile = UserMobile.get(0).toString();
                ULocality = UserLocality.get(0).toString();
                UState = UserState.get(0).toString();
                UAddress = UserAAddress.get(0).toString();
                UId = UserId.get(0).toString();
                UImage = UserImage.get(0).toString();
            }
        }

        UserName = (TextView) findViewById(R.id.user_name);
        Email = (TextView) findViewById(R.id.user_email);
        Phone = (TextView) findViewById(R.id.mobile_number);
        Locality = (TextView) findViewById(R.id.user_locality);
        Address = (TextView) findViewById(R.id.user_address);
        EditProfile = (FloatingActionButton) findViewById(R.id.fab);

        UserName.setText(UFName + " "+ ULName);
        Email.setText(UEmail);
        Phone.setText(UMobile);
        Locality.setText(ULocality);
        Address.setText(UAddress);

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,UserProfileUpdate.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfileActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
