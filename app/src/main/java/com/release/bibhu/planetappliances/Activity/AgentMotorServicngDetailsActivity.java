package com.release.bibhu.planetappliances.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;
import com.release.bibhu.planetappliances.Util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_AMC_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_AMC_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.RESOLVE_MOTOR_AMC;
import static com.release.bibhu.planetappliances.Util.ApiConstants.RESOLVE_MOTOR_AMC_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.AGENT_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.REGISTER_AMC_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.RESOLVE_MOTOR_AMC_REQUEST;

public class AgentMotorServicngDetailsActivity extends AppCompatActivity implements ConnectivityInterface.ApiInterafce{

    Toolbar mToolbar;
    LinearLayout main_layout;
    PrefferenceManager prefferenceManager;
    ProgressHandler progressHandler;
    EditText feed_back;
    Button resolve;
    ArrayList<String> agent_name = new ArrayList<>();
    ArrayList<String> agentId = new ArrayList<>();
    ArrayAdapter adapter;

    String PHONE ="";
    String ADDRESS ="";
    String AMC_DATE="";
    String AMC_ID ="";

    TextView  phone,add,date;


    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){

        prefferenceManager = PrefferenceManager.getFeaturePreference(AgentMotorServicngDetailsActivity.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        feed_back = (EditText) findViewById(R.id.feed_back);

        phone = (TextView) findViewById(R.id.phone);
        add = (TextView) findViewById(R.id.add);
        date = (TextView) findViewById(R.id.date);
        resolve = (Button) findViewById(R.id.resolve);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle("AMC Details");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        PHONE = getIntent().getStringExtra("phone");
        ADDRESS = getIntent().getStringExtra("address");
        AMC_DATE = getIntent().getStringExtra("amc_date");
        AMC_ID = getIntent().getStringExtra("amc_id");

        phone.setText("Phone : "+PHONE);
        add.setText("Address : "+ADDRESS);
        date.setText("AMC Date : "+AMC_DATE);

        if(getIntent().getStringExtra("status").trim().equals("1")){
            resolve.setVisibility(View.INVISIBLE);
            feed_back.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_motor_servicing_details_activity);

        _init();

        resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               resolveAMC();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }



    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(AgentMotorServicngDetailsActivity.this);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }


        if (requestData == RESOLVE_MOTOR_AMC_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {


                        Util.motor_service_resolved = true;
                        Toast.makeText(getApplicationContext(),"AMC Resolved Successfully.",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"Try Again..",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Try Again..",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Try Again..",Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void resolveAMC(){

        String TOKEN = prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN);
        String AGENT_ID = prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID, PrefferenceManager.DEFAULT_USER_ID);

        HashMap<String,JSONObject> dataMap =null;

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status","1");
            jsonObject.put("id",AMC_ID);
            jsonObject.put("feedback",feed_back.getText().toString().trim());

            dataMap = new HashMap<>();
            dataMap.put("amc",jsonObject);

        }catch (Exception e){}


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(RESOLVE_MOTOR_AMC,RESOLVE_MOTOR_AMC_REQUEST,this,RESOLVE_MOTOR_AMC_BASE_URL,dataMap,TOKEN);
        connectivityInterface.startApiProcessing();
    }


}
