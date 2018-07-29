package com.release.bibhu.planetappliances.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;

public class PreLogin extends AppCompatActivity {

    PrefferenceManager prefferenceManager;
    LinearLayout main_layout;
    EditText register_complain,Agent_login;
    public static final int REQUEST_LOCATION_PERMISSION = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        prefferenceManager = PrefferenceManager.getFeaturePreference(PreLogin.this);

        Agent_login = (EditText) findViewById(R.id.Agent_login);
        register_complain = (EditText) findViewById(R.id.register_complain);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);

      /*  if(CommonUtility.isTablet(PreLogin.this)){
            main_layout.setBackgroundResource(R.drawable.tab_login_bg);
        }else {
            main_layout.setBackgroundResource(R.drawable.phone_login_bg);
        }*/

        if(userHasRememberMePermission()){
            Intent intent = new Intent(PreLogin.this,UserDashBoard.class);
            startActivity(intent);
            finish();
        }

        Agent_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreLogin.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        register_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreLogin.this,Complain.class);
                startActivity(intent);

            }
        });

        if (ContextCompat.checkSelfPermission(PreLogin.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PreLogin.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PreLogin.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }


    }

    /**
     * This method is used to check whether user has remember me permission or not .
     */
    private boolean userHasRememberMePermission() {

        if (prefferenceManager.getDataFromPref(PrefferenceManager.REMEMBER_ME, PrefferenceManager.DEFAULT_REMEMBER_ME).equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onBackPressed() {
        CommonUtility.getConformation(PreLogin.this);
    }

}
