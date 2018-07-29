package com.release.bibhu.planetappliances.Activity;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.release.bibhu.planetappliances.Database.DBHelper;
import com.release.bibhu.planetappliances.Fragment.AddNewComplain;
import com.release.bibhu.planetappliances.Fragment.Admin_DashBoard;
import com.release.bibhu.planetappliances.Fragment.AmcList;
import com.release.bibhu.planetappliances.Fragment.ChangePassword;
import com.release.bibhu.planetappliances.Fragment.CustomerFeedback;
import com.release.bibhu.planetappliances.Fragment.DashBoard;
import com.release.bibhu.planetappliances.Fragment.Enquery_DashBoard;
import com.release.bibhu.planetappliances.Fragment.Enquery_List;
import com.release.bibhu.planetappliances.Fragment.FragmentDrawer;
import com.release.bibhu.planetappliances.Fragment.Office_Admin_DashBoard;
import com.release.bibhu.planetappliances.Fragment.RegisterAMC;
import com.release.bibhu.planetappliances.Fragment.TaskAssigned;
import com.release.bibhu.planetappliances.Fragment.With_BillNo;
import com.release.bibhu.planetappliances.Fragment.Without_BillNo;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;
import com.release.bibhu.planetappliances.Util.Util;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.release.bibhu.planetappliances.Util.Constants.LOGOUT_REQUEST;
import static com.release.bibhu.planetappliances.Util.Util.amc_list_of_a_day;

public class UserDashBoard extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener, ConnectivityInterface.ApiInterafce {

    private static String TAG = UserDashBoard.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dash_board);

      Util.notification_clicked = getIntent().getIntExtra("notification",0);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        prefferenceManager = PrefferenceManager.getFeaturePreference(UserDashBoard.this);

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        String TITEL = FragmentDrawer.TitleArrayList.get(position);
        if (TITEL.equals(getString(R.string.title_dashboard))) {

            if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("0")) {
                fragment = new Admin_DashBoard();
                title = getString(R.string.title_dashboard);
            }
            if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("1")) {
                fragment = new DashBoard();
                title = getString(R.string.title_dashboard);
            }
            if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("2")){
                fragment = new Office_Admin_DashBoard();
                title = getString(R.string.title_dashboard);
            }
            if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("3")) {
                fragment = new Enquery_DashBoard();
                title = getString(R.string.title_dashboard);
            }

        } else if (TITEL.equals(getString(R.string.title_complain_with_bill))) {
            fragment = new With_BillNo();
            title = getString(R.string.title_complain_with_bill);
        } else if (TITEL.equals(getString(R.string.title_add_complain))) {
            fragment = new AddNewComplain();
            title = getString(R.string.title_add_complain);
        } else if (TITEL.equals(getString(R.string.title_complain_without_bill))) {
            fragment = new Without_BillNo();
            title = getString(R.string.title_complain_without_bill);
        } else if (TITEL.equals(getString(R.string.title_amc_list))) {


            if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("1")) {
                amc_list_of_a_day = false;
                Intent intent = new Intent(UserDashBoard.this, AMCLIST.class);
                startActivity(intent);
                return;
            }

            fragment = new AmcList();
            title = getString(R.string.title_amc_list);

        } else if (TITEL.equals("Complaint ")) {
            fragment = new TaskAssigned();
            title = "Complaint";
        } else if (TITEL.equals(getString(R.string.title_customer_feedback))) {
            fragment = new CustomerFeedback();
            title = getString(R.string.title_customer_feedback);
        } else if (TITEL.equals(getString(R.string.title_change_password))) {
            fragment = new ChangePassword();
            title = getString(R.string.title_change_password);
        } else if (TITEL.equals(getString(R.string.title_customer_enquery_list))) {
            fragment = new Enquery_List();
            title = getString(R.string.title_customer_enquery_list);
        } else if (TITEL.equals(getString(R.string.title_register_amc))) {
            fragment = new RegisterAMC();
            title = getString(R.string.title_register_amc);
        } else if (TITEL.equals(getString(R.string.title_logout))) {
            logout();
        }



        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    /**
     * This method is applicable for logout .
     */
    private void logout() {
        progressHandler = new ProgressHandler(UserDashBoard.this);
        progressHandler.show();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                timer.purge();
                if (progressHandler != null && progressHandler.isShowing()) {
                    progressHandler.hide();
                }
                afterLogout();
            }
        }, 1500, 2000);

/*
       SQLiteDatabase DB = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = DB.rawQuery("SELECT Token FROM " + DBHelper.USER_TABLE , null);
        int count = cursor.getCount();
        String token = "";
        if(count>0){
            if (cursor.moveToFirst()) {
                do {
                    token = cursor.getString(0).trim();
                } while (cursor.moveToNext());
            }
        }

        if(token==null || token.equals("")){
            CommonUtility.showMessage(UserDashBoard.this,"Something went wrong please try again later.");
            return;
        }

        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("token",token);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(LOGOUT,inputHashMap,LOGOUT_REQUEST,this,LOGOUT_BASE_URL);
        connectivityInterface.startApiProcessing();*/
    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(UserDashBoard.this);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }


        if (requestData == LOGOUT_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {
                        afterLogout();
                    } else {
                        CommonUtility.showMessage(UserDashBoard.this, "Alert", message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(UserDashBoard.this, "Alert", "Something went wrong,please try again.");
                }
            } else {
                CommonUtility.showMessage(UserDashBoard.this, "Alert", "Something went wrong,please try again.");
            }
        }
    }

    private void afterLogout() {
        PrefferenceManager prefferenceManager = new PrefferenceManager(UserDashBoard.this);
        prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME, "0");
        prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN, "");
        prefferenceManager.setDataInPref(PrefferenceManager.USER_ID, "");

        SQLiteDatabase DB = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);

        String Qry = "DELETE FROM " + DBHelper.USER_TABLE;
        DB.execSQL(Qry);

        Intent intent = new Intent(UserDashBoard.this, PreLogin.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CustomerFeedback fragment = (CustomerFeedback) getSupportFragmentManager().findFragmentByTag("Customer Enquery");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        CommonUtility.getConformation(UserDashBoard.this);
    }

}