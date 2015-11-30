package vydik.jjbytes.com.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import vydik.jjbytes.com.Adapters.PurohithBookingInboxAdapter;
import vydik.jjbytes.com.Database.MainDatabase;
import vydik.jjbytes.com.Fragments.NavigationDrawerFragment;
import vydik.jjbytes.com.Interfaces.NavigationDrawerCallbacks;

/**
 * Created by Manoj on 10/27/2015.
 */
public class PurohithMainActivity extends ActionBarActivity implements OnClickListener,NavigationDrawerCallbacks {
    private static final String tag = "SimpleCalendarViewActivity";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private TextView currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    Toolbar mToolbar;
    Button AddEventCalendar,ViewEventList;
    private Calendar _calendar;
    private int month, year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    private GridCellAdapter adapter;
    MainDatabase database;
    ArrayList<Integer> ServerFrezed = new ArrayList<Integer>();
    int Valies = 1;
    int PositionValue = 0;
    int ServerValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purohith_home_activity);

        database = new MainDatabase(this);
        database = database.open();

        AddFrezedDates();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AddEventCalendar = (Button) findViewById(R.id.add_event_calendar);
        AddEventCalendar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CallAddEvent();
            }
        });

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

    }

    private void CallAddEvent() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PromtView = inflater.inflate(R.layout.purohith_add_eventpopup, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().getAttributes().windowAnimations = R.style.AddEventAnimation;
        alertD.setCancelable(false);
            Button Submit = (Button) PromtView.findViewById(R.id.event_submit);
            Button Cancel = (Button) PromtView.findViewById(R.id.event_cancel);

            Submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertD.dismiss();
                }
            });

            Cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertD.dismiss();
                }
            });
        alertD.setView(PromtView);
        alertD.show();
    }

    private void AddFrezedDates() {
        ServerFrezed.add(2);
        ServerFrezed.add(14);
        ServerFrezed.add(16);
        ServerFrezed.add(18);
        ServerFrezed.add(24);
    }

    /**
     *
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year)
    {
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        if (v == prevMonth)
        {
            AddFrezedDates();
            if (month <= 1)
            {
                month = 12;
                year--;
            }
            else
            {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth)
        {
            AddFrezedDates();
            if (month > 11)
            {
                month = 1;
                year++;
            }
            else
            {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onDestroy()
    {
        Log.d(tag, "Destroying View ...");
        database.close();
        super.onDestroy();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if(position == 0){

        }else if(position == 1){
            Intent intent = new Intent(PurohithMainActivity.this,PurohitInboxActivity.class);
            startActivity(intent);
        }else if(position == 2){
            Intent intent = new Intent(PurohithMainActivity.this,PurohitBookingInboxActivity.class);
            startActivity(intent);
        }else if(position == 3){
            Intent intent = new Intent(PurohithMainActivity.this,PurohitProfileView.class);
            startActivity(intent);
        }else if(position == 4){
            database.deleteReplace();

            SplashScreenActivity.LoginType.equals("non");
            LoginActivity.type_for_login.equals("");

            Intent intent = new Intent(PurohithMainActivity.this,SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }


    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements OnClickListener
    {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private final int month, year;
        private int daysInMonth, prevMonthDays;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId, int month, int year)
        {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            this.month = month;
            this.year = year;

            Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }
        private String getMonthAsString(int i)
        {
            return months[i];
        }

        private String getWeekDayAsString(int i)
        {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i)
        {
            return daysOfMonth[i];
        }

        public String getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy)
        {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            // The number of days to leave blank at
            // the start of this month.
            int trailingSpaces = 0;
            int leadSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

            // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11)
            {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            }
            else if (currentMonth == 0)
            {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            }
            else
            {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            }

            // Compute how much to leave before before the first day of the
            // month.
            // getDay() returns 0 for Sunday.
            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
            {
                ++daysInMonth;
            }

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++)
            {
                Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
            }

            // Current Month Days

            for (int i = 1; i <= daysInMonth; i++)
            {
                Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
                int Size = ServerFrezed.size();
                System.out.println("server freezed int Size" + Size);

                if(Size >= Valies){
                    ServerValue = ServerFrezed.get(PositionValue);
                    System.out.println("server freezed int value" + Valies);
                }

                if (i == getCurrentDayOfMonth())
                {
                    System.out.println("current day"+currentMonth);
                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }
                else
                {
                    if(i == ServerValue){
                        list.add(String.valueOf(i) + "-ORANGE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                }

                if(i==ServerValue){
                    Valies++;
                    PositionValue++;
                }
                /*freeze the date based on server received values hear*/
                /*pass int array list check*/

                /*if(i == 28){
                    list.add(String.valueOf(i) + "-ORANGE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }*/

            }
            /*to freeze the server received values*/
            /*for(int j=0;j<ServerFrezed.size();j++){
                int k = ServerFrezed.get(j);
                System.out.println("server freezed int value"+k);
                for(int i=1; i<= daysInMonth;i++){
                    if(i == k){
                        list.add(String.valueOf(i) + "-ORANGE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                }
            }*/
            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++)
            {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap findNumberOfEventsPerMonth(int year, int month)
        {
            HashMap map = new HashMap<String, Integer>();
            // DateFormat dateFormatter2 = new DateFormat();
            //
            // String day = dateFormatter2.format("dd", dateCreated).toString();
            //
            // if (map.containsKey(day))
            // {
            // Integer val = (Integer) map.get(day) + 1;
            // map.put(day, val);
            // }
            // else
            // {
            // map.put(day, 1);
            // }
            return map;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            if (row == null)
            {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
            {
                if (eventsPerMonthMap.containsKey(theday))
                {
                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

            if (day_color[1].equals("GREY"))
            {
                gridcell.setTextColor(Color.GRAY);
            }
            if (day_color[1].equals("WHITE"))
            {
                gridcell.setTextColor(Color.WHITE);
            }
            if (day_color[1].equals("BLUE"))
            {
                gridcell.setTextColor(getResources().getColor(R.color.blue));
            }
            if(day_color[1].equals("ORANGE"))
            {
                gridcell.setBackgroundColor(getResources().getColor(R.color.orange));
            }
            return row;
        }
        @Override
        public void onClick(View view)
        {
            String date_month_year = (String) view.getTag();

            try
            {
                Date parsedDate = dateFormatter.parse(date_month_year);

                Log.d(tag, "Parsed Date: " + parsedDate.toString());
                CallEventPopup();
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth()
        {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth)
        {
            this.currentDayOfMonth = currentDayOfMonth;
        }
        public void setCurrentWeekDay(int currentWeekDay)
        {
            this.currentWeekDay = currentWeekDay;
        }
        public int getCurrentWeekDay()
        {
            return currentWeekDay;
        }
    }

    private void CallEventPopup() {

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }
}
