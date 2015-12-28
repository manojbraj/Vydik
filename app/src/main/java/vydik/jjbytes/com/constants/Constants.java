package vydik.jjbytes.com.constants;

import org.apache.http.message.BasicNameValuePair;

import java.security.PublicKey;

/**
 * Created by Manoj' on 10/2/2015.
 */
public class Constants {
    /*url list*/
    public static String getPoojaList = "http://www.vydik.com/android/puja_purohit.php";
    public static String submitPujariRegistration = "http://www.vydik.com/android/final_purohit_registration.php";
    public static String getStateList = "http://www.vydik.com/state_purohit.php";
    public static String getCityNames = "http://www.vydik.com/android/city_purohit.php?state_id=6";
    public static String getLocalityAndPin = "http://www.vydik.com/android/location_pur.php?city=9";
    public static String getLanguages = "http://www.vydik.com/android/purohit_language.php";
    public static String getBankNameAndBranchName = "http://www.vydik.com/android/bank_detail_purohit.php";
    public static String SubmitUserRegistration = "http://www.vydik.com/android_user_reg.php";
    public static String LoginUrl = "http://www.vydik.com/android_user_reg.php";
    public static String PurohitLogin = "http://www.vydik.com/android/login.php";
    public static String PurohithSect = "http://www.vydik.com/android/purohit_sect.php";
    public static String SendSearchParameters = "http://www.vydik.com/search_pujas.php";
    public static String PurohithDetailUrl = "http://www.vydik.com/pur_info_moblity.php?pur_info=";
    public static String SendBookingDetails = "http://www.vydik.com/android_book_puja.php";
    public static String CancelPujaRequestURL = "http://www.vydik.com/admin_vydik/admin_vydhik_project/android_cancelation_booking.php";
    public static String UserProfileUpdateURL = "http://www.vydik.com/android_user_profile_upd.php";
    public static String UserProfilePickChangeURL = "http://www.vydik.com/android_edit_userpic.php";
    public static String AccessPanchangForDay = "http://www.vydik.com/vydik_panchang/Vedic-Rishi-Astro-API-PHP-Client-master/android_panchang.php";
    public static String GenerateRandomNumber ="http://www.vydik.com/android/android_bank_token.php";
    /*get request*/
    public static String RequestOTPUrl = "http://www.vydik.com/android_user_otp.php?phoneno=";
    public static String UpdateForgotPassword = "http://www.vydik.com/android_fgt_pwd_chk.php?phoneno=";

    /*constant messages*/
    public static String getPoojaListProgress = "Accessing Puja list, please wait...";
    public static String regPurohithProgress = "Purohit registration in process, please wait...";
    public static String satecityProgress = "Please wait while we are accessing state,city & locality data for you...";
    public static String getingBankDetails = "Please wait while we are accessing Bank details...";
    public static String UserRegistrationProgress = "User registration in process, please wait...";
    public static String LoginProgress = "Accessing user data for login, please wait...";
    public static String LoginPurohitProgress = "Accessing Purohit data for login, please wait...";
    public static String SearchProgress = "Accessing data based on search, please wait...";
    public static String PurohithDetails = "Accessing purohit details, please wait...";
    public static String BookingPujaProgress = "Submitting booking details, please wait...";
    public static String RequestForReschedule = "Submitting details for rescheduling the Puja, Please wait...";
    public static String RequestForCancelation = "Cancellation of Puja request in process, please wait...";
    public static String UserProfileUpdateProgress = "Updating Profile, please wait...";
    public static String UserPickUploading = "Updating Profile picture, please wait...";
    public static String GettingPanchang = "Accessing Panchanga, please wait...";

    /*unique constant value for database usage user_id*/
    public static String UserIdData;

    /*string constants for Registration activity one*/
    public static String FirstNameValue, LastNameValue, EmailValue, PasswordValue, ConfirmPasswordValue,
            PhoneNumberValue, UnivercityNameValue, EducationValue, DateOfBirthValue = "0/0/0", ImagePath;

    /*string constants for Registration Activity two*/
    public static String GetState, GetCity, GetLocality, GetPinCode, GetLanguage, GetAddress, GetGuruName = " ", GetEmergencyCheck, GetTravelCheck, Image = "";

    /*string constants for Registration three*/
    public static String BankName, SchemeName, BankBranch, BankIFSC, BankAcNum, RefPName = " ", RefPNumber = " ", RefPEmail = " ",
            RefCName = " ", RefCNumber = " ", RefCEmail = " ";

    /*registration url key value pair constant*/
    public static String k1 = "submit", k2 = "firstname", k3 = "lastname", k4 = "email", k5 = "pass", k6 = "dob", k7 = "phone", k8 = "education",
            k9 = "university", k10 = "Address", k11 = "city", k12 = "zip_code", k13 = "travel", k14 = "guruname", k15 = "emergency", k16 = "location",
            k17 = "bank_name", k18 = "Schemes", k19 = "branch_name", k20 = "ifsc_code", k21 = "acc_no", k22 = "perfor_puja", k23 = "languge_id",
            k24 = "price_with_samagri", k25 = "price_with_out_samagri", k26 = "expertise_level", k27 = "rp1name", k28 = "rp1phone", k29 = "rp1email",
            k30 = "rc1name", k31 = "rc1phone", k32 = "rc1email", k33 = "image", submit = "submit";

    /*user edit text constants*/
    public static String UserFName, UserLName, UserEmail, UserPassword, UserContact, UserAddress, OTPString, ReferredFrom;

    /*user reference drop down selection edit box value*/
    public static String URefFName ="no value", URefNNumber="no value", URefPName="no value", URefPNumber="no value";

    /*user dob and aniversery*/
    public static String UserDOB, UserAnneversery="00-00-0000";

    /*user regisration key value pair*/
    public static String UK1 = "submit", UK2 = "firstname", UK3 = "lastname", UK4 = "email", UK5 = "pass", UK6 = "city", UK7 = "state",
            UK8 = "usr_location", UK9 = "zip_code", UK10 = "dob", UK11 = "aniversry", UK12 = "get_to_know", UK13 = "phone", UK14 = "Address", UK15 = "friend_email",
            UK16 = "friend_phoneno", UK17 = "purohit_email", UK18 = "purohit_phoneno",UK19 = "image";

    /*login key for user*/
    public static String Ls = "log-in", Lm = "ph_number", Lp = "password_id", LType = "submit", MyType;

    /*login key for purohit*/
    public static String PurSubmit = "submit",PurPhone = "phone",PurPass = "pwd";

    /*login responce strings*/
    public static String UL1 = "nl",UL2 = "nl",UL3 = "nl",UL4 = "nl",UL5 = "nl",UL6 = "nl",UL7 = "nl",UL8 = "nl",UL9 = "nl",UL10 = "image";

    /*search puja constants*/
    public static String SPujaName, SLocationName = "null", SLanguageName = "null", SSectId ="null", SDate;

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    /*puja search send parameters(key)*/
    public static String searchSubmit = "submit_pur", searchPackage = "puja_Package", searchPujaId = "puja_id", searchSectId = "sect_id",
            searchDate = "date_pur", searchLanguage = "lang_id", searchLocation = "loc_id";

    /*purohith detail constants*/
    public static String Education, Sect, Univercity;

    /*package purohith constants*/
    public static String package_Name, package_rating, package_price, package_expertise, package_sect, package_language, package_education, package_location, package_university, package_image,purohith_id;

    /*book puja inbox constants*/
    public static String BUser_Phone, BPuja_Name, BUser_Address, BUser_Location, BPuja_Date, BUser_Name, BPurohit_ID, BUser_ID,BUserF_Name,BUserL_Name;

    /*booking submission constants*/
    public static String Booking1 = "puja_id",Booking2 = "purohit_id", Booking3 = "user_id", Booking4 = "puja_date", Booking5 ="puja_price",
    Booking6 = "location",Booking7 = "puja_type",Booking8 = "sect",Booking9 = "country",Booking10 = "state",Booking11 = "city",
    Booking12 = "Address",Booking13 = "purohit_name",Booking14 = "user_name";

    /*retrieve puja id from search*/
    public static String SearchPujaId,SearchPriceBooking,PujaDescription,SearchMessage="null";

    /*payment amount package and no package*/
    public static String AdvanceAmount,BalanceAmount="000",PayementGatewayAmount,PaymentErrorAdvance;

    /*user profile update key*/
    public static String UpdateFName = "first_name",UpdateLName = "last_name",UpdateMobileNo = "mob_no",UpdateLocality="locality",UpdateAddress = "address",UpdateEmail = "emailid";

    /*panchang constants post method*/
    public static String PDate = "date",PTime = "time",PMinutes = "min",PSubmit = "panchang_sub";
}