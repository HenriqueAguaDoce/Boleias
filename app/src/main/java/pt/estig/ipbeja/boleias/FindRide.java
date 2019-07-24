package pt.estig.ipbeja.boleias;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

public class FindRide extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private String txtStart, txtEnd, selectedDate;
    private Spinner spinnerStart, spinnerEnd;
    private CalendarView calendarView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.findRide);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        spinnerStart = (Spinner) findViewById(R.id.spinnerStart);
        spinnerEnd = (Spinner) findViewById(R.id.spinnerEnd);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerStart.setAdapter(adapter);
        spinnerEnd.setAdapter(adapter);

        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtStart = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtEnd = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String d = String.valueOf(dayOfMonth);
                String m = String.valueOf(month+1);
                String y = String.valueOf(year);

                selectedDate = d + "/" + m + "/" + y;
                Toast.makeText(FindRide.this, selectedDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.logOut:
                Toast.makeText(this, "Loging out...", Toast.LENGTH_SHORT).show();

                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBoxKeepIn", false).commit();
                finish();
                //TODO
                //LoginActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, FindRide.class);
        context.startActivity(starter);
    }

    public void btnRideSearch_onClick(View view) {
        Intent i = new Intent(this, RidesAvailableActivity.class);
        i.putExtra("start", txtStart);
        i.putExtra("end", txtEnd);
        i.putExtra("date", selectedDate);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.createRide:
                Toast.makeText(this, "Create Ride...", Toast.LENGTH_SHORT).show();
                OfferRide.start(this);
                return true;
            case R.id.profileSettings:
                Toast.makeText(this, "Profile Settings...", Toast.LENGTH_SHORT).show();
                //TODO
                //MainActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
