package com.release.bibhu.planetappliances.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Adaptor.OnlyServicingDetailsAdapter;
import com.release.bibhu.planetappliances.Model.OnlyServiceDetailsModel;
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
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGN_ONLY_AMC_SERVICING;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGN_ONLY_AMC_SERVICING_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.RESOLVE_ONLY_AMC;
import static com.release.bibhu.planetappliances.Util.ApiConstants.RESOLVE_ONLY_AMC_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.AGENT_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ASSIGN_ONLY_AMC_SERVICING_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.RESOLVE_ONLY_AMC_REQUEST;
import static com.release.bibhu.planetappliances.Util.Util.only_service_resolved;

public class AgentOnlyServicngDetailsActivity extends AppCompatActivity implements ConnectivityInterface.ApiInterafce{

    Toolbar mToolbar;
    LinearLayout main_layout;
    PrefferenceManager prefferenceManager;
    ProgressHandler progressHandler;
    ArrayList<OnlyServiceDetailsModel> onlyServiceDetailsModels = new ArrayList<>();
    EditText feed_back;
    Button resolve;
    String amc_id ="";
    String AGENT_ID ="";
    String status = "";

    TextView name,amc_no,service_date,service_no;

    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){

        prefferenceManager = PrefferenceManager.getFeaturePreference(AgentOnlyServicngDetailsActivity.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

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

        name = (TextView) findViewById(R.id.name);
        amc_no = (TextView) findViewById(R.id.amc_no);
        service_date = (TextView) findViewById(R.id.service_date);
        service_no = (TextView) findViewById(R.id.service_no);
        feed_back = (EditText) findViewById(R.id.feed_back);
        resolve = (Button) findViewById(R.id.resolve);

        AGENT_ID = prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID, PrefferenceManager.DEFAULT_USER_ID);


        onlyServiceDetailsModels =(ArrayList<OnlyServiceDetailsModel>)(getIntent().getSerializableExtra("details"));
        name.setText("Customer Name : "+getIntent().getStringExtra("name"));
        amc_no.setText("AMC No : "+getIntent().getStringExtra("amc_no"));

        for(int i=0;i<onlyServiceDetailsModels.size();i++){
            if(onlyServiceDetailsModels.get(i).getUser_id().trim().equals(AGENT_ID.trim())){
                service_date.setText("Service Date : "+onlyServiceDetailsModels.get(i).getService_date());
                service_no.setText("Service No : "+onlyServiceDetailsModels.get(i).getService_no());
                amc_id = onlyServiceDetailsModels.get(i).getService_id();
                status = onlyServiceDetailsModels.get(i).getStatus().trim();
            }
        }

        if(status.equals("1")){
            feed_back.setVisibility(View.INVISIBLE);
            resolve.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_only_servicing_details_activity);

        _init();

        resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        progressHandler = new ProgressHandler(AgentOnlyServicngDetailsActivity.this);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }




        if (requestData == RESOLVE_ONLY_AMC_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        only_service_resolved = true;
                        Toast.makeText(getApplicationContext(),"AMC Resolved Successfully.",Toast.LENGTH_LONG).show();
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


        HashMap<String,JSONObject> dataMap =null;

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",amc_id);
            jsonObject.put("status","1");
            jsonObject.put("feedback",feed_back.getText().toString());

            dataMap = new HashMap<>();
            dataMap.put("amc_servicing",jsonObject);

        }catch (Exception e){}


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(RESOLVE_ONLY_AMC,RESOLVE_ONLY_AMC_REQUEST,this,RESOLVE_ONLY_AMC_BASE_URL,dataMap,TOKEN);
        connectivityInterface.startApiProcessing();


    }


}
