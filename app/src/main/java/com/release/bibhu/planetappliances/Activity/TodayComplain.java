package com.release.bibhu.planetappliances.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TodayComplain extends AppCompatActivity {

    Toolbar mToolbar;
    PrefferenceManager prefferenceManager;
    ListView listView;

    ArrayList<String> complains = new ArrayList<>();
    ArrayList<String> service = new ArrayList<>();;
    ArrayAdapter adapter;

    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){

        prefferenceManager = PrefferenceManager.getFeaturePreference(TodayComplain.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        listView = (ListView) findViewById(R.id.listview);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("Today's Assigned Complains");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_complain);

        _init();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generic, menu);//Menu Resource, Menu
        // Display date with a short day and month name
        Date date = Calendar.getInstance().getTime();

        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter = new SimpleDateFormat("dd MMM yyyy");
        String today = formatter.format(date);


        MenuItem item = menu.getItem(0);
        SpannableString s = new SpannableString(today);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#9ea6ee")), 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }

    /**
     * This method is used to retrieve the current assign complain.
     */
    private void getCurrentAssignedComplain(){

    }
}
