package com.release.bibhu.planetappliances.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Adaptor.MotorServicingAdapter;
import com.release.bibhu.planetappliances.Adaptor.OnlyServicingAdapter;
import com.release.bibhu.planetappliances.Model.MotorServiceModel;
import com.release.bibhu.planetappliances.Model.OnlyServiceDetailsModel;
import com.release.bibhu.planetappliances.Model.OnlyServiceModel;
import com.release.bibhu.planetappliances.Netwok.NetworkStatus;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;
import com.release.bibhu.planetappliances.Util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_ONLY_SERVICING;
import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_ONLY_SERVICING_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.MOTOR_SERVICING;
import static com.release.bibhu.planetappliances.Util.ApiConstants.MOTOR_SERVICING_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.MOTOR_SERVICING_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ONLY_SERVICING_REQUEST;
import static com.release.bibhu.planetappliances.Util.PrefferenceManager.DEFAULT_USER_ID;
import static com.release.bibhu.planetappliances.Util.Util.amc_list_of_a_day;

public class AMCLIST extends AppCompatActivity implements ConnectivityInterface.ApiInterafce{

    Toolbar mToolbar;
    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    LinearLayout motor_servicing, only_servicing;
    TextView no_data;
    ListView listview;

    ArrayList<OnlyServiceModel> onlyServiceModels = new ArrayList<>();
    ArrayList<MotorServiceModel> motorServiceModels = new ArrayList<>();


    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){

        prefferenceManager = PrefferenceManager.getFeaturePreference(AMCLIST.this);


        motor_servicing = (LinearLayout) findViewById(R.id.motor_servicing);
        only_servicing = (LinearLayout) findViewById(R.id.only_servicing);
        no_data = (TextView) findViewById(R.id.no_data);
        listview = (ListView) findViewById(R.id.listview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle("AMC List");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        only_servicing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                only_servicing.setBackgroundColor(Color.parseColor("#1c7768"));
                motor_servicing.setBackgroundColor(Color.parseColor("#d4d4d4"));

                getOnlyServicingList();
            }
        });

        motor_servicing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motor_servicing.setBackgroundColor(Color.parseColor("#1c7768"));
                only_servicing.setBackgroundColor(Color.parseColor("#d4d4d4"));

                getMotorServicingList();
            }
        });

        getOnlyServicingList();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amclist_activity);

        _init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        if(Util.motor_service_resolved){
            Util.motor_service_resolved = false;
            getMotorServicingList();
        }

        if(Util.only_service_resolved){
            Util.only_service_resolved = false;
            getOnlyServicingList();
        }

    }


    /**
     * Get only servicelist.
     */
    public void getOnlyServicingList() {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));
        inputHashMap.put("agent_specific", "1");

        if(amc_list_of_a_day){
            inputHashMap.put("today_only", "1");
        }else{
            inputHashMap.put("today_only", "0");
        }

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(AGENT_ONLY_SERVICING, inputHashMap, ONLY_SERVICING_REQUEST, this, AGENT_ONLY_SERVICING_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * Get Motor service list
     */
    public void getMotorServicingList() {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));
        inputHashMap.put("agent_specific", "1");

        if(amc_list_of_a_day){
            inputHashMap.put("today_only", "1");
        }else{
            inputHashMap.put("today_only", "0");
        }


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(MOTOR_SERVICING, inputHashMap, MOTOR_SERVICING_REQUEST, this,MOTOR_SERVICING_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(AMCLIST.this);
        progressHandler.show();

        onlyServiceModels.clear();
        motorServiceModels.clear();

        no_data.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }

        if (requestData == ONLY_SERVICING_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        OnlyServiceModel onlyServiceModel = new OnlyServiceModel();
                        onlyServiceModel.setName("Customer Name");
                        onlyServiceModel.setPhone("Phone");
                        onlyServiceModel.setAmc_number("AMC Number");
                        onlyServiceModels.add(onlyServiceModel);


                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        boolean no_data_available = true;
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (!(jsonArray.optJSONObject(i).optString("flag").trim().equals("1"))) {

                                    no_data_available = false;

                                    OnlyServiceModel onlyServiceModel1 = new OnlyServiceModel();

                                    JSONObject jsonObject1 = jsonArray.optJSONObject(i).optJSONObject("amc");

                                    onlyServiceModel1.setName(jsonObject1.optString("name").trim());
                                    onlyServiceModel1.setPhone(jsonObject1.optString("phone").trim());
                                    onlyServiceModel1.setAmc_number(jsonObject1.optString("amc_number").trim());
                                    onlyServiceModel1.setAmc_type(jsonObject1.optString("amc_type").trim());
                                    onlyServiceModel1.setId(jsonObject1.optString("id").trim());
                                    onlyServiceModel1.setUser_id(jsonObject1.optString("user_id").trim());



                                    String details = "";
                                    ArrayList<OnlyServiceDetailsModel> onlyServiceDetailsModel_arraylist = new ArrayList<>();

                                    String date = jsonArray.optJSONObject(i).optString("date");
                                    String id = jsonArray.optJSONObject(i).optString("id");
                                    String status = jsonArray.optJSONObject(i).optString("status");

                                    String user_id="";
                                    if(jsonArray.optJSONObject(i).optString("user_id") !=null){
                                        user_id = jsonArray.optJSONObject(i).optString("user_id");
                                    }

                                    String service_no = jsonArray.optJSONObject(i).optString("service_no");
                                    details = details + "Service Date : " + convertDate(date) + "\n" + "Service No : " + service_no  + "\n\n";

                                    OnlyServiceDetailsModel onlyServiceDetailsModel = new OnlyServiceDetailsModel();
                                    onlyServiceDetailsModel.setService_date(convertDate(date));
                                    onlyServiceDetailsModel.setService_no(service_no);
                                    onlyServiceDetailsModel.setService_id(id);
                                    onlyServiceDetailsModel.setUser_id(user_id);
                                    onlyServiceDetailsModel.setStatus(status);
                                    onlyServiceDetailsModel_arraylist.add(onlyServiceDetailsModel);

                                    onlyServiceModel1.setService_list_details(details);
                                    onlyServiceModel1.setOnlyServiceDetailsModels(onlyServiceDetailsModel_arraylist);

                                    onlyServiceModels.add(onlyServiceModel1);

                                }
                            }

                            OnlyServicingAdapter onlyServicingAdapter = new OnlyServicingAdapter(AMCLIST.this, onlyServiceModels);
                            listview.setAdapter(onlyServicingAdapter);

                            if(no_data_available){
                                no_data.setVisibility(View.VISIBLE);
                                listview.setVisibility(View.GONE);
                            }

                        } else {
                            no_data.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
                        }
                    } else {
                        no_data.setText(message);
                        no_data.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    no_data.setText("Something went wrong,please try again.");
                    no_data.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
            } else {
                no_data.setText("Something went wrong,please try again.");
                no_data.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            }

        }


        if (requestData == MOTOR_SERVICING_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        MotorServiceModel motorServiceModel = new MotorServiceModel();
                        motorServiceModel.setName("Customer Name");
                        motorServiceModel.setPhone("Phone");
                        motorServiceModel.setAmc_number("AMC Number");
                        motorServiceModels.add(motorServiceModel);
                        boolean no_data_available = true;


                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {



                                if ((jsonArray.optJSONObject(i).optString("user_id").trim()!=null)
                                        && ((prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID,DEFAULT_USER_ID).equals(jsonArray.optJSONObject(i).optString("user_id"))) && !(jsonArray.optJSONObject(i).optString("flag").trim().equals("1")))) {

                                    MotorServiceModel motorServiceModel1 = new MotorServiceModel();
                                    motorServiceModel1.setName(jsonArray.optJSONObject(i).optString("name").trim());
                                    motorServiceModel1.setPhone(jsonArray.optJSONObject(i).optString("phone").trim());
                                    motorServiceModel1.setAmc_number(jsonArray.optJSONObject(i).optString("amc_number").trim());
                                    motorServiceModel1.setAmc_type(jsonArray.optJSONObject(i).optString("amc_type").trim());
                                    motorServiceModel1.setId(jsonArray.optJSONObject(i).optString("id").trim());
                                    motorServiceModel1.setUser_id(jsonArray.optJSONObject(i).optString("user_id").trim());
                                    motorServiceModel1.setAddress(jsonArray.optJSONObject(i).optString("address").trim());
                                    motorServiceModel1.setAmc_date(convertDate(jsonArray.optJSONObject(i).optString("amc_date").trim()));
                                    motorServiceModel1.setAmc_type(jsonArray.optJSONObject(i).optString("amc_type").trim());
                                    motorServiceModel1.setStatus(jsonArray.optJSONObject(i).optString("status").trim());


                                    String details = "";
                                    details = details + "Phone : " + jsonArray.optJSONObject(i).optString("phone").trim() + "\n" + "Address : " + jsonArray.optJSONObject(i).optString("address").trim() + "\n"
                                            + "AMC Date : " + convertDate(jsonArray.optJSONObject(i).optString("amc_date").trim());

                                    motorServiceModel1.setService_list_details(details.trim());
                                    motorServiceModels.add(motorServiceModel1);

                                    no_data_available = false;
                                }
                            }

                            MotorServicingAdapter motorServicingAdapter = new MotorServicingAdapter(AMCLIST.this, motorServiceModels);
                            listview.setAdapter(motorServicingAdapter);

                            if(no_data_available){
                                no_data.setVisibility(View.VISIBLE);
                                listview.setVisibility(View.GONE);
                            }

                        } else {
                            no_data.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
                        }
                    } else {
                        no_data.setText(message);
                        no_data.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    no_data.setText("Something went wrong,please try again.");
                    no_data.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
            } else {
                no_data.setText("Something went wrong,please try again.");
                no_data.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            }

        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    String convertDate(String inputDate) {

        DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
        } catch (Exception parseException) {
            // Date is invalid. Do what you want.
        }
        theDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return theDateFormat.format(date);
    }


}
