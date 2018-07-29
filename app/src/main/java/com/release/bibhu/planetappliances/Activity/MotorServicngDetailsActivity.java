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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Adaptor.OnlyServicingDetailsAdapter;
import com.release.bibhu.planetappliances.Model.OnlyServiceDetailsModel;
import com.release.bibhu.planetappliances.Netwok.NetworkStatus;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;
import com.release.bibhu.planetappliances.Util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGN_ONLY_AMC_SERVICING;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGN_ONLY_AMC_SERVICING_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.FORGOT_PASSWORD;
import static com.release.bibhu.planetappliances.Util.ApiConstants.FORGOT_PASSWORD_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_AMC_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_AMC_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.AGENT_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ASSIGN_ONLY_AMC_SERVICING_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.FORGOT_PASSWORD_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.REGISTER_AMC_COMPLAIN_REQUEST;

public class MotorServicngDetailsActivity extends AppCompatActivity implements ConnectivityInterface.ApiInterafce{

    Toolbar mToolbar;
    LinearLayout main_layout;
    PrefferenceManager prefferenceManager;
    ProgressHandler progressHandler;
    Spinner agent_list;
    Button assign;
    String AGENT_ID ="";
    ArrayList<String> agent_name = new ArrayList<>();
    ArrayList<String> agentId = new ArrayList<>();
    ArrayAdapter adapter;
    public static String SELECT_AGENT1 = "---------- Select Technician ----------";

    String PHONE ="";
    String ADDRESS ="";
    String AMC_DATE="";
    String AMC_ID ="";

    TextView  phone,add,date;


    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){

        Util.selected_amc = 0;
        Util.amc_service_id = "";

        prefferenceManager = PrefferenceManager.getFeaturePreference(MotorServicngDetailsActivity.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        agent_list = (Spinner) findViewById(R.id.agent_list);

        phone = (TextView) findViewById(R.id.phone);
        add = (TextView) findViewById(R.id.add);
        date = (TextView) findViewById(R.id.date);

        assign = (Button) findViewById(R.id.assign);

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


        if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("1")) {
            assign.setVisibility(View.INVISIBLE);
            agent_list.setVisibility(View.INVISIBLE);
        }


        try{
            if(getIntent().getStringExtra("status").trim().equals("1")){
                assign.setVisibility(View.INVISIBLE);
                agent_list.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){}


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motor_servicing_details_activity);

        _init();
        if (!prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("1")) {
            getAgentList();

        }


        agent_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AGENT_ID = agentId.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AGENT_ID.trim().equals("0")){
                    Toast.makeText(MotorServicngDetailsActivity.this,"Please select one agent.",Toast.LENGTH_SHORT).show();
                }else{
                    //Api call for Assigning task
                    registerAmc();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    public void getAgentList(){

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token",prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, ""));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(AGENT_LIST,hashMap,AGENT_LIST_REQUEST,this,AGENT_LIST_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(MotorServicngDetailsActivity.this);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }


        if(requestData == AGENT_LIST_REQUEST){

            agent_name.clear();
            agentId.clear();
            agentId.add("0");
            agent_name.add(SELECT_AGENT1);
            AGENT_ID = "0";

            adapter = new ArrayAdapter(getApplicationContext(),R.layout.produt_spinner,agent_name);
            agent_list.setAdapter(adapter);

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    if(jsonArray.length() >0){

                        for(int i=0;i<jsonArray.length();i++){

                            if((!jsonArray.optJSONObject(i).optString("flag").trim().equals("1")) && (jsonArray.optJSONObject(i).optString("type").trim().equals("1"))){
                                agentId.add(jsonArray.optJSONObject(i).optString("id"));
                                agent_name.add(jsonArray.optJSONObject(i).optString("fname")+" "+jsonArray.optJSONObject(i).optString("mname")+" "+jsonArray.optJSONObject(i).optString("lname"));
                            }
                        }
                        adapter = new ArrayAdapter(getApplicationContext(),R.layout.produt_spinner,agent_name);
                        agent_list.setAdapter(adapter);
                    }

                } catch (Exception e) {
                }
            }
        }



        if (requestData == REGISTER_AMC_COMPLAIN_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        Toast.makeText(getApplicationContext(),"Assigned to agent successfully..",Toast.LENGTH_SHORT).show();
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


    public void registerAmc(){

        String TOKEN = prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN);


        HashMap<String,JSONObject> dataMap =null;

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id",AGENT_ID);
            jsonObject.put("id",AMC_ID);

            dataMap = new HashMap<>();
            dataMap.put("amc",jsonObject);

        }catch (Exception e){}


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(REGISTER_AMC_COMPLAIN,REGISTER_AMC_COMPLAIN_REQUEST,this,REGISTER_AMC_COMPLAIN_BASE_URL,dataMap,TOKEN);
        connectivityInterface.startApiProcessing();


    }


}
