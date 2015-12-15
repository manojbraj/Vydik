package vydik.jjbytes.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Stack;

import vydik.jjbytes.com.Models.GetPurohitLanguages;
import vydik.jjbytes.com.Models.GetPurohitPujaList;
import vydik.jjbytes.com.Models.GetUserLoginData;
import vydik.jjbytes.com.Models.LoginGetDB;
import vydik.jjbytes.com.Models.PurohitBookingInboxGetDB;
import vydik.jjbytes.com.Models.PurohitInboxGetDB;
import vydik.jjbytes.com.Models.PurohitLoginGet;
import vydik.jjbytes.com.Models.UserInboxGetDB;
import vydik.jjbytes.com.Models.UserPurohitBookingGet;

/**
 * Created by manoj on 10/3/2015.
 */
public class MainDatabase {
    private static final String DATABASE_NAME = "vydik";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_LOGIN_TABLE = "login";
    private static final String DATABASE_USER_INBOX_TABLE = "user_inbox";
    private static final String DATABASE_PUROHIT_INBOX_TABLE = "purohit_inbox";
    private static final String DATABSE_PUROHIT_PUJABOOKED_INBOX = "pujabooked_inbox";
    private static final String DATABASE_USER_TABLE = "user_data";
    private static final String DATABSE_USER_BOOKING_DETAILS = "user_bookings";
    private static final String DATABASE_PUROHIT_LOGIN_TABLE = "purohit_login";
    private static final String DATABASE_PUROHIT_PUJA_LOGIN_LIST = "purohit_puja_list";
    private static final String DATABASE_PUROHIT_Langagres = "purohit_languages";


    private static final String TABLE_LOGIN = "create table login (_id integer primary key autoincrement, "
            + "email varchar, mobile varchar, logintype varchar);";

    private static final String TABLE_USER_INBOX = "create table user_inbox (_id integer primary key autoincrement, "
            + "message varchar, date varchar);";

    private static final String TABLE_PUROHIT_INBOX = "create table purohit_inbox (_id integer primary key autoincrement, "
            + "message varchar, date varchar);";

    private static final String TABLE_PUROHIT_BOOKING_INBOX = "create table pujabooked_inbox (_id integer primary key autoincrement, "
            +"user_phone_number varchar, puja_name varchar, location varchar, address varchar, puja_date varchar, user_name varchar, "
            +"app_date varchar, purohit_id varchar, user_id varchar);";

    private static final String LOGIN_RESPONCE_USER = "create table user_data (_id integer primary key autoincrement, "
            + "first_name varchar, last_name varchar, email varchar, mobile varchar,locality varchar,state varchar,address varchar,"
            + "userid varchar,image varchar);";

    private static final String TABLE_USER_BOOKING_DETAILS = "create table user_bookings (_id integer primary key autoincrement, "
            + "user_id varchar,puja_name varchar,purohit_name varchar,date varchar,price varchar,address varchar,cancel_flag varchar);";

    private static final String TABLE_PUROHIT_LOGIN = "create table purohit_login (_id integer primary key autoincrement, "
            + "fName varchar, lName varchar, email varchar, dob varchar, phone varchar, education varchar, university varchar, state varchar, address varchar, "
            + "city varchar, zipcode varchar,guruname varchar, location varchar);";

    private static final String TABLE_PUROHIT_PUJA_LIST = "create table purohit_puja_list (_id integer primary key autoincrement, "
            +"PujaName varchar,PriceWithSamagri varchar,PriceWithoutSamagri varchar,ExpertLevel varchar);";

    private static final String TABLE_PUROHIT_LANGUAGE = "create table purohit_languages (_id integer primary key autoincrement,"
            +"Languages varchar);";

    private static final String TAG = "DatabaseClass";
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private final Context context;

    /*initialize getter setter methods*/
    ArrayList<LoginGetDB> loginlist = new ArrayList<LoginGetDB>();
    ArrayList<UserInboxGetDB> userInboxGetDBs = new ArrayList<UserInboxGetDB>();
    ArrayList<PurohitInboxGetDB> purohitInboxGetDBs = new ArrayList<PurohitInboxGetDB>();
    ArrayList<PurohitBookingInboxGetDB> purohitBookingInboxGetDBs = new ArrayList<PurohitBookingInboxGetDB>();
    ArrayList<GetUserLoginData> getUserLoginDatas = new ArrayList<GetUserLoginData>();
    ArrayList<UserPurohitBookingGet> getuserPurohitBookingGet = new ArrayList<UserPurohitBookingGet>();
    ArrayList<PurohitLoginGet> purohitLoginGet = new ArrayList<PurohitLoginGet>();
    ArrayList<GetPurohitPujaList> getPurohitPujaList = new ArrayList<GetPurohitPujaList>();
    ArrayList<GetPurohitLanguages> getPurohitLanguage = new ArrayList<GetPurohitLanguages>();

    public MainDatabase(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    // ---opens the database---
    public MainDatabase open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        db = DBHelper.getReadableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            exportDB();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(TABLE_LOGIN);
                db.execSQL(TABLE_USER_INBOX);
                db.execSQL(TABLE_PUROHIT_INBOX);
                db.execSQL(TABLE_PUROHIT_BOOKING_INBOX);
                db.execSQL(LOGIN_RESPONCE_USER);
                db.execSQL(TABLE_USER_BOOKING_DETAILS);
                db.execSQL(TABLE_PUROHIT_LOGIN);
                db.execSQL(TABLE_PUROHIT_PUJA_LIST);
                db.execSQL(TABLE_PUROHIT_LANGUAGE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void exportDB() {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = "/data/" + "vydik.jjbytes.com.Activities" + "/databases/" + DATABASE_NAME;
            String backupDBPath = "vydik.sqlite";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
/*login insertion access and delete methods start*/

    //----login insert query----
    public long insertLogin(String email, String mobile, String type) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("email", email);
        initialValues.put("mobile", mobile);
        initialValues.put("logintype", type);
        return db.insert(DATABASE_LOGIN_TABLE, null, initialValues);
    }

    //----login readable query----
    public ArrayList<LoginGetDB> getMyLogin() {
        Cursor cursor = db.rawQuery("select * from " + DATABASE_LOGIN_TABLE, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    LoginGetDB getdata = new LoginGetDB();
                    getdata.setId(cursor.getString(0));
                    getdata.setEmail(cursor.getString(1));
                    getdata.setMobile(cursor.getString(2));
                    getdata.setType(cursor.getString(3));
                    getdata.setChecked(false);
                    loginlist.add(getdata);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            cursor.close();
        }
        return loginlist;
    }

    //------delete function for login-------
    public int deleteReplace() {
        return db.delete(DATABASE_LOGIN_TABLE,null, null);
    }

/*login insertion access delete methods ends hear*/

/*user inbox insertion access delete call back start*/
    //----- user inbox insertion query-----
    public long InsertUserInbox(String message,String date){
        ContentValues initialValues = new ContentValues();
        initialValues.put("message",message);
        initialValues.put("date",date);
        return db.insert(DATABASE_USER_INBOX_TABLE, null, initialValues);
    }

    //----- user inbox access query ------
    public ArrayList<UserInboxGetDB> getUserInbox(){
        Cursor cursor = db.rawQuery("select * from " + DATABASE_USER_INBOX_TABLE, null);
        try{
            if (cursor.moveToFirst()) {
                do{
                    UserInboxGetDB userInboxGetDB = new UserInboxGetDB();
                    userInboxGetDB.setId(cursor.getString(0));
                    userInboxGetDB.setMessage(cursor.getString(1));
                    userInboxGetDB.setDate(cursor.getString(2));
                    userInboxGetDBs.add(userInboxGetDB);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            cursor.close();
        }
        return userInboxGetDBs;
    }
/*user inbox insertion access delete call back end*/

/*purohith inbox insertion access delete call back start*/
    //------ purohit inbox insersion query -------
    public long InsertPurohitInbox(String message,String date){
        ContentValues initialValues = new ContentValues();
        initialValues.put("message",message);
        initialValues.put("date",date);
        return db.insert(DATABASE_PUROHIT_INBOX_TABLE, null, initialValues);
    }

    //------ purohit inbox access query -------
    public ArrayList<PurohitInboxGetDB> getPurohitInbox(){
        Cursor cursor = db.rawQuery("select * from " + DATABASE_PUROHIT_INBOX_TABLE, null);
        try{
            if (cursor.moveToFirst()) {
                do{
                    PurohitInboxGetDB purohitInboxGetDB = new PurohitInboxGetDB();
                    purohitInboxGetDB.setId(cursor.getString(0));
                    purohitInboxGetDB.setMessage(cursor.getString(1));
                    purohitInboxGetDB.setDate(cursor.getString(2));
                    purohitInboxGetDBs.add(purohitInboxGetDB);
                }while (cursor.moveToNext());
            }

        }catch (Exception e){

        }finally {
            cursor.close();
        }
        return purohitInboxGetDBs;
    }
/*purohith inbox insertion access delete call back end*/

/*purohith book inbox insertion access delete call back start*/
    //------- insert purohit booking details query --------
    public long InsertPurohitBookingInbox(String user_phone,String puja_name,String user_location,String user_address,
                                          String puja_date, String user_name, String date, String purohit_id, String user_id){
        ContentValues initialValues = new ContentValues();
        initialValues.put("user_phone_number",user_phone);
        initialValues.put("puja_name",puja_name);
        initialValues.put("location",user_location);
        initialValues.put("address",user_address);
        initialValues.put("puja_date",puja_date);
        initialValues.put("user_name",user_name);
        initialValues.put("app_date",date);
        initialValues.put("purohit_id",purohit_id);
        initialValues.put("user_id",user_id);
        return db.insert(DATABSE_PUROHIT_PUJABOOKED_INBOX, null, initialValues);
    }

    //-------- access book details access query -------
    public ArrayList<PurohitBookingInboxGetDB> getPurohitBookingDetails(){
        Cursor cursor = db.rawQuery("select * from " + DATABSE_PUROHIT_PUJABOOKED_INBOX, null);
        try{
            if (cursor.moveToFirst()) {
                do {
                    PurohitBookingInboxGetDB purohitBookingInboxGetDB = new PurohitBookingInboxGetDB();
                    purohitBookingInboxGetDB.setId(cursor.getString(0));
                    purohitBookingInboxGetDB.setPhone_number(cursor.getString(1));
                    purohitBookingInboxGetDB.setPuja_name(cursor.getString(2));
                    purohitBookingInboxGetDB.setUser_location(cursor.getString(3));
                    purohitBookingInboxGetDB.setUser_address(cursor.getString(4));
                    purohitBookingInboxGetDB.setPuja_date(cursor.getString(5));
                    purohitBookingInboxGetDB.setUser_name(cursor.getString(6));
                    purohitBookingInboxGetDB.setDate(cursor.getString(7));
                    purohitBookingInboxGetDB.setPurohit_it(cursor.getString(8));
                    purohitBookingInboxGetDB.setUser_id(cursor.getString(9));
                    purohitBookingInboxGetDBs.add(purohitBookingInboxGetDB);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){

        }finally {
            cursor.close();
        }
        return purohitBookingInboxGetDBs;
    }

/*purohith book inbox insertion access delete call back end*/

/*user login data insertion*/
    public long InserUserLoginData(String FirstName,String LastName,String Email,String Mobile,String Locality,String State,
                                   String Address,String Userid,String Image){
        ContentValues initialValues = new ContentValues();
        initialValues.put("first_name",FirstName);
        initialValues.put("last_name",LastName);
        initialValues.put("email",Email);
        initialValues.put("mobile",Mobile);
        initialValues.put("locality",Locality);
        initialValues.put("state",State);
        initialValues.put("address",Address);
        initialValues.put("userid",Userid);
        initialValues.put("image",Image);
        return db.insert(DATABASE_USER_TABLE, null, initialValues);
    }

/*user login data get*/
  public ArrayList<GetUserLoginData> getUserLoginDetails(){
      Cursor cursor = db.rawQuery("select * from " + DATABASE_USER_TABLE, null);
      try{
          if (cursor.moveToFirst()) {
              do{
                  GetUserLoginData getUserLoginData = new GetUserLoginData();
                  getUserLoginData.setId(cursor.getString(0));
                  getUserLoginData.setFname(cursor.getString(1));
                  getUserLoginData.setLname(cursor.getString(2));
                  getUserLoginData.setEmail(cursor.getString(3));
                  getUserLoginData.setMobile(cursor.getString(4));
                  getUserLoginData.setLocality(cursor.getString(5));
                  getUserLoginData.setState(cursor.getString(6));
                  getUserLoginData.setAddress(cursor.getString(7));
                  getUserLoginData.setUserid(cursor.getString(8));
                  getUserLoginData.setImage(cursor.getString(9));
                  getUserLoginDatas.add(getUserLoginData);
              }while (cursor.moveToNext());
          }
      }catch (Exception e){

      }finally {
          cursor.close();
      }
      return getUserLoginDatas;
  }

    //------delete function for login-------
    public int deleteUserData() {
        return db.delete(DATABASE_USER_TABLE, null, null);
    }
//--------------update quesry-------------
public void UpdateUserProfile(String _id, String fName, String lName,String Email,String Mobile,String Locality,String Address){
    ContentValues values = new ContentValues();
    values.put("first_name", fName);
    values.put("last_name",lName);
    values.put("email",Email);
    values.put("mobile",Mobile);
    values.put("locality",Locality);
    values.put("address",Address);
    db.update(DATABASE_USER_TABLE, values, "mobile = " + Mobile, null);
}

//------------user image path updation------------
public void UpdateUserProfileImage(String Mobile,String image_path){
    ContentValues values = new ContentValues();
    values.put("image",image_path);
    db.update(DATABASE_USER_TABLE, values, "mobile = " + Mobile, null);
}
/*user booking table insertion updating deletion starts from hear */
    //-----------user booking data insertion query------------
    public long InserBookingDetails(String UserId,String pujaname,String purohitname,String date,String price,String address,String cancell_flag){
        ContentValues initialValues = new ContentValues();
        initialValues.put("user_id",UserId);
        initialValues.put("puja_name",pujaname);
        initialValues.put("purohit_name",purohitname);
        initialValues.put("date",date);
        initialValues.put("price",price);
        initialValues.put("address",address);
        initialValues.put("cancel_flag",cancell_flag);
        return db.insert(DATABSE_USER_BOOKING_DETAILS, null, initialValues);
    }

    //---------- retrieve the booking list ----------------
    public ArrayList<UserPurohitBookingGet> getUserPurohitBooking(String userid){
        Cursor cursor = db.rawQuery("select * from " + DATABSE_USER_BOOKING_DETAILS +" where user_id = " + userid, null);
        try{
            if (cursor.moveToFirst()) {
                do{
                    UserPurohitBookingGet userPurohitBookingGet = new UserPurohitBookingGet();
                    userPurohitBookingGet.setId(cursor.getString(0));
                    userPurohitBookingGet.setUser_id(cursor.getString(1));
                    userPurohitBookingGet.setPujaname(cursor.getString(2));
                    userPurohitBookingGet.setPurohitname(cursor.getString(3));
                    userPurohitBookingGet.setData(cursor.getString(4));
                    userPurohitBookingGet.setPrice(cursor.getString(5));
                    userPurohitBookingGet.setAddress(cursor.getString(6));
                    userPurohitBookingGet.setFlagvalue(cursor.getString(7));
                    getuserPurohitBookingGet.add(userPurohitBookingGet);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){

        }finally {
            cursor.close();
        }
        return getuserPurohitBookingGet;
    }

    //---------------cancell booking list---------------
    public int DeleteBooking(String id) {
        return db.delete(DATABSE_USER_BOOKING_DETAILS,"_id=?", new String[] { id });
    }

    //--------------Reschedule Booking List---------------
    public void updateRescheduleDate(String id,String date){
        ContentValues values = new ContentValues();
        values.put("date", date);
        db.update(DATABSE_USER_BOOKING_DETAILS, values, "_id" +"="+id, null);
    }

/*---------purohit login general info input-------------*/
    public long PurohitLoginDeailInsurt(String fName,String lName,String Email, String DOB,String Phone,String Education,String Univercity,String State,
                                        String Address,String City,String ZipCode,String GuruName,String Location){
        ContentValues initialValues = new ContentValues();
        initialValues.put("fName",fName);
        initialValues.put("lName",lName);
        initialValues.put("email",Email);
        initialValues.put("dob",DOB);
        initialValues.put("phone",Phone);
        initialValues.put("education",Education);
        initialValues.put("university",Univercity);
        initialValues.put("state",State);
        initialValues.put("address",Address);
        initialValues.put("city",City);
        initialValues.put("zipcode",ZipCode);
        initialValues.put("guruname",GuruName);
        initialValues.put("location",Location);
        return db.insert(DATABASE_PUROHIT_LOGIN_TABLE, null, initialValues);
    }

/*------------get values from PurohitLoginDeailInsurt table------------*/
    public ArrayList<PurohitLoginGet> getPurohitBasicData(){
        Cursor cursor = db.rawQuery("select * from " + DATABASE_PUROHIT_LOGIN_TABLE , null);
        try{
            if (cursor.moveToFirst()) {
                do{
                    PurohitLoginGet purohitLoginGetInner = new PurohitLoginGet();
                    purohitLoginGetInner.setId(cursor.getString(0));
                    purohitLoginGetInner.setfName(cursor.getString(1));
                    purohitLoginGetInner.setlName(cursor.getString(2));
                    purohitLoginGetInner.setEmail(cursor.getString(3));
                    purohitLoginGetInner.setDOB(cursor.getString(4));
                    purohitLoginGetInner.setPhone(cursor.getString(5));
                    purohitLoginGetInner.setEducation(cursor.getString(6));
                    purohitLoginGetInner.setUniversity(cursor.getString(7));
                    purohitLoginGetInner.setState(cursor.getString(8));
                    purohitLoginGetInner.setAddress(cursor.getString(9));
                    purohitLoginGetInner.setCity(cursor.getString(10));
                    purohitLoginGetInner.setZipCode((cursor.getString(11)));
                    purohitLoginGetInner.setGuruName(cursor.getString(12));
                    purohitLoginGetInner.setLocality(cursor.getString(13));
                    purohitLoginGet.add(purohitLoginGetInner);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){

        }finally {
            cursor.close();
        }
        return purohitLoginGet;
    }

 /*---------------purohit puja insersion----------------------*/
    public long PurohitPujaListInsert(String PujaName,String PriceWS,String PriceWOS,String ExpertLevel){
        ContentValues initialValues = new ContentValues();
        initialValues.put("PujaName",PujaName);
        initialValues.put("PriceWithSamagri",PriceWS);
        initialValues.put("PriceWithoutSamagri",PriceWOS);
        initialValues.put("ExpertLevel",ExpertLevel);
        return db.insert(DATABASE_PUROHIT_PUJA_LOGIN_LIST, null, initialValues);
    }

/*-------------------get PurohitPujaListInsert values from db----------------------*/
    public ArrayList<GetPurohitPujaList> getPujaList(){
        Cursor cursor = db.rawQuery("select * from " + DATABASE_PUROHIT_PUJA_LOGIN_LIST , null);
        try {
            if (cursor.moveToFirst()) {
                do{
                    GetPurohitPujaList PujaList = new GetPurohitPujaList();
                    PujaList.setId(cursor.getString(0));
                    PujaList.setPujaName(cursor.getString(1));
                    PujaList.setPujaWithPackage(cursor.getString(2));
                    PujaList.setPujaWithoutPackage(cursor.getString(3));
                    PujaList.setExpertLevel(cursor.getString(4));
                    getPurohitPujaList.add(PujaList);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){

        }finally {
            cursor.close();
        }
        return getPurohitPujaList;
    }

/*-----------------purohit language-----------------*/
    public long PurohitLanguage(String Language){
        ContentValues initialValues = new ContentValues();
        initialValues.put("Languages",Language);
        return db.insert(DATABASE_PUROHIT_Langagres, null, initialValues);
    }

/*------------read languages of purohit------------*/
    public ArrayList<GetPurohitLanguages> PurohitLanguages(){
        Cursor cursor = db.rawQuery("select * from " + DATABASE_PUROHIT_Langagres , null);
        try{
            if (cursor.moveToFirst()) {
                do{
                    GetPurohitLanguages Languages = new GetPurohitLanguages();
                    Languages.setId(cursor.getString(0));
                    Languages.setLanguages(cursor.getString(1));
                    getPurohitLanguage.add(Languages);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){

        }finally {
            cursor.close();
        }
        return getPurohitLanguage;
    }
}