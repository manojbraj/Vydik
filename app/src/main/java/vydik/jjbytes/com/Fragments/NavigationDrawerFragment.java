package vydik.jjbytes.com.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vydik.jjbytes.com.Activities.LoginActivity;
import vydik.jjbytes.com.Activities.R;
import vydik.jjbytes.com.Activities.SplashScreenActivity;
import vydik.jjbytes.com.Adapters.NavigationDrawerAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Interfaces.NavigationDrawerCallbacks;
import vydik.jjbytes.com.Models.GetUserLoginData;
import vydik.jjbytes.com.Models.NavigationItem;
import vydik.jjbytes.com.view.ScrimInsetsFrameLayout;

/**
 * Created by Manoj on 10/18/2015.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallbacks {
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREFERENCES_FILE = "my_app_settings"; //TODO: change this to your file
    private NavigationDrawerCallbacks mCallbacks;
    private RecyclerView mDrawerList;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    public static List<NavigationItem> items = new ArrayList<NavigationItem>();

    TextView UserName,UserEmail;
    MainDatabase database;
    ArrayList<GetUserLoginData> getUserLoginData;
    private ArrayList<String> UserFirstName = new ArrayList<String>();
    private ArrayList<String> UserLastName= new ArrayList<String>();
    private ArrayList<String> UserEmailAddress = new ArrayList<String>();
    private ArrayList<String> UserImage = new ArrayList<String>();
    String UFName,ULName,UEmail,UImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_google, container, false);
        mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(layoutManager);
        mDrawerList.setHasFixedSize(true);

        database = new MainDatabase(getActivity());
        database = database.open();
        getUserLoginData = database.getUserLoginDetails();

        if(getUserLoginData.size() == 0){

        }else {
            /*later chage this one to getMyLogin*/
            for(int i=0;i<getUserLoginData.size();i++){
                UserFirstName.add(getUserLoginData.get(i).getFname());
                UserLastName.add(getUserLoginData.get(i).getLname());
                UserEmailAddress.add(getUserLoginData.get(i).getEmail());
                UserImage.add(getUserLoginData.get(i).getImage());

                UFName = UserFirstName.get(0).toString();
                ULName = UserLastName.get(0).toString();
                UEmail = UserEmailAddress.get(0).toString();
                UImage = UserImage.get(0).toString();
            }
        }

        database.close();

        UserName = (TextView) view.findViewById(R.id.txtUsername);
        UserEmail = (TextView) view.findViewById(R.id.txtUserEmail);

        UserName.setText(UFName + " " + ULName);
        UserEmail.setText(UEmail);

        final List<NavigationItem> navigationItems = getMenu();
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(navigationItems);
        adapter.setNavigationDrawerCallbacks(this);
        mDrawerList.setAdapter(adapter);
        selectItem(mCurrentSelectedPosition);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        mActionBarDrawerToggle = actionBarDrawerToggle;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        if(mFragmentContainerView.getParent() instanceof ScrimInsetsFrameLayout){
            mFragmentContainerView = (View) mFragmentContainerView.getParent();
        }
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.colorPrimaryDark));

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "true");
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
            mDrawerLayout.openDrawer(mFragmentContainerView);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public List<NavigationItem> getMenu() {
        items.removeAll(items);
        if(SplashScreenActivity.LoginType.equals("purohit")){
            items.add(new NavigationItem("Home", getResources().getDrawable(R.drawable.nav_home_icon)));
            items.add(new NavigationItem("Notifications", getResources().getDrawable(R.drawable.inbox)));
            items.add(new NavigationItem("Puja Booking", getResources().getDrawable(R.drawable.book_puja)));
            items.add(new NavigationItem("Profile", getResources().getDrawable(R.drawable.nav_profile)));
            items.add(new NavigationItem("LogOut", getResources().getDrawable(R.drawable.logout)));
        }else if(LoginActivity.type_for_login.equals("purohit")){
            items.add(new NavigationItem("Home", getResources().getDrawable(R.drawable.nav_home_icon)));
            items.add(new NavigationItem("Notifications", getResources().getDrawable(R.drawable.inbox)));
            items.add(new NavigationItem("Puja Booking", getResources().getDrawable(R.drawable.book_puja)));
            items.add(new NavigationItem("Profile", getResources().getDrawable(R.drawable.nav_profile)));
            items.add(new NavigationItem("LogOut", getResources().getDrawable(R.drawable.logout)));
        }
        else {
            items.add(new NavigationItem("Home", getResources().getDrawable(R.drawable.nav_home_icon)));
            items.add(new NavigationItem("Notifications", getResources().getDrawable(R.drawable.inbox)));
            items.add(new NavigationItem("Book a Purohit", getResources().getDrawable(R.drawable.book_puja)));
            items.add(new NavigationItem("Bhajans", getResources().getDrawable(R.drawable.bhajans)));
            items.add(new NavigationItem("Book Puja @Temple", getResources().getDrawable(R.drawable.puja_at_temple)));
            items.add(new NavigationItem("Panchanga", getResources().getDrawable(R.drawable.panchang)));
            items.add(new NavigationItem("Booking Details", getResources().getDrawable(R.drawable.book_puja)));
            items.add(new NavigationItem("Profile", getResources().getDrawable(R.drawable.nav_profile)));
            items.add(new NavigationItem("LogOut", getResources().getDrawable(R.drawable.logout)));
        }
        return items;
    }

    /**
     * Changes the icon of the drawer to back
     */
    public void showBackButton() {
        if (getActivity() instanceof ActionBarActivity) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Changes the icon of the drawer to menu
     */
    public void showDrawerButton() {
        if (getActivity() instanceof ActionBarActivity) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mActionBarDrawerToggle.syncState();
    }

    void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
        ((NavigationDrawerAdapter) mDrawerList.getAdapter()).selectPosition(position);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }
}
