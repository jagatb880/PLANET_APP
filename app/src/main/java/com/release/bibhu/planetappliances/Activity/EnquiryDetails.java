package com.release.bibhu.planetappliances.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Adaptor.ComplainAdapter;
import com.release.bibhu.planetappliances.Model.Product;
import com.release.bibhu.planetappliances.Netwok.NetworkStatus;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;
import com.release.bibhu.planetappliances.Util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.release.bibhu.planetappliances.Util.ApiConstants.CUSTOMER_ENQUERY;
import static com.release.bibhu.planetappliances.Util.ApiConstants.CUSTOMER_ENQUERY_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.MODEL_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.MODEL_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.NEW_CUSTOMER_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.NEW_CUSTOMER_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_USER_DETAILS_FOR_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_USER_DETAILS_FOR_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.CUSTOMER_ENQUERY_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.MODEL_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.NEW_CUSTOMER_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.PRODUCT_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.PRODUCT_USER_DETAILS_FOR_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.REGISTER_COMPLAIN_REQUEST;

public class EnquiryDetails extends AppCompatActivity {

    Toolbar mToolbar;
    PrefferenceManager prefferenceManager;

    TextView product_name,feed_back,enquiry_id,is_positive;
    TextView name,phone,email,address;
    TextView emp_name,emp_id,emp_phone,emp_desig;
    LinearLayout emp_ll;
    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){

        prefferenceManager = PrefferenceManager.getFeaturePreference(EnquiryDetails.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle("Enquiry Details");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        product_name = (TextView) findViewById(R.id.product_name);
        feed_back = (TextView) findViewById(R.id.feed_back);
        enquiry_id = (TextView) findViewById(R.id.enquiry_id);
        is_positive = (TextView) findViewById(R.id.is_positive);

        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        address = (TextView) findViewById(R.id.address);

        emp_name = (TextView) findViewById(R.id.emp_name);
        emp_id = (TextView) findViewById(R.id.emp_id);
        emp_phone = (TextView) findViewById(R.id.emp_phone);
        emp_desig = (TextView) findViewById(R.id.emp_desig);
        emp_ll = (LinearLayout) findViewById(R.id.emp_ll);

        product_name.setText(getIntent().getStringExtra("product_name"));
        feed_back.setText(getIntent().getStringExtra("feedback"));
        enquiry_id.setText(getIntent().getStringExtra("enquiry_id"));
        is_positive.setText(getIntent().getStringExtra("is_positive"));


        name.setText(getIntent().getStringExtra("name"));
        phone.setText(getIntent().getStringExtra("phone"));
        email.setText(getIntent().getStringExtra("email"));
        address.setText(getIntent().getStringExtra("address"));

        if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("0")) {
            emp_ll.setVisibility(View.VISIBLE);

            emp_name.setText(getIntent().getStringExtra("emp_name"));
            emp_id.setText(getIntent().getStringExtra("emp_id"));
            emp_phone.setText(getIntent().getStringExtra("emp_phone"));
            emp_desig.setText(getIntent().getStringExtra("emp_desig"));
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry_details);

        _init();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

}
