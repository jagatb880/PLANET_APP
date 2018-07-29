package com.release.bibhu.planetappliances.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Model.Complain;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGN_COMPLAIN_OLD;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGN_COMPLAIN_OLD_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.CREATE_OTP;
import static com.release.bibhu.planetappliances.Util.ApiConstants.CREATE_OTP_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.OTP_VALIDATE;
import static com.release.bibhu.planetappliances.Util.ApiConstants.OTP_VALIDATE_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.UPDATE_ASSIGNED_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.UPDATE_ASSIGNED_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.UPDATE_ASSIGNED_COMPLAIN_OLD;
import static com.release.bibhu.planetappliances.Util.ApiConstants.UPDATE_ASSIGNED_COMPLAIN_OLD_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.AGENT_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.CREATE_OTP_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.OLD_UPDATE_ASSIGNED_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.OTP_VALIDATE_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.UPDATE_ASSIGNED_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Util.page_navigation_form_task_assigned_page;

public class ComplaintDetails extends AppCompatActivity implements ConnectivityInterface.ApiInterafce {

    EditText feed_back,collected_amt,aten_date;
    Button fix;
    Toolbar mToolbar;
    PrefferenceManager prefferenceManager;
    Intent intent;
    Complain complain;
    ProgressHandler progressHandler;
    Spinner agent;
    Calendar myCalendar;
    Button assign;
    String agent_id = "";
    public static String SELECT_AGENT = "---- Select Technician ----";
    ArrayList<String> agent_name = new ArrayList<>();
    ArrayList<String> agentId = new ArrayList<>();
    boolean assigned_clicked = true;


    TextView product_name,make,model;
    TextView name,phone,address;
    TextView issue,complain_id,priority,payment,service_type,status;

    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init() {

        feed_back = (EditText) findViewById(R.id.feed_back);
        aten_date = (EditText) findViewById(R.id.aten_date);
        collected_amt = (EditText) findViewById(R.id.collected_amt);
        agent = (Spinner) findViewById(R.id.agent);
        fix = (Button) findViewById(R.id.fix);
        assign = (Button) findViewById(R.id.assign);


        prefferenceManager = PrefferenceManager.getFeaturePreference(ComplaintDetails.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitle("Complain Details");
        getSupportActionBar().setTitle("Complain Details");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




        product_name = (TextView) findViewById(R.id.product_name);
        make = (TextView) findViewById(R.id.make);
        model = (TextView) findViewById(R.id.model);

        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);

        issue = (TextView) findViewById(R.id.issue);
        complain_id = (TextView) findViewById(R.id.complain_id);
        priority = (TextView) findViewById(R.id.priority);
        payment = (TextView) findViewById(R.id.payment);
        service_type = (TextView) findViewById(R.id.service_type);
        status = (TextView) findViewById(R.id.status);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);

        _init();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        complain = (Complain) getIntent().getSerializableExtra("complain_info");
        if(complain.getProduct_name()!=null)
            product_name.setText(complain.getProduct_name());

        if(complain.getMake()!=null && !complain.getMake().equals("null"))
            make.setText(complain.getMake());

        if(complain.getModel()!=null)
            model.setText(complain.getModel());


        name.setText(complain.getCustomer_name());
        phone.setText(complain.getMobile());
        address.setText(complain.getAddress());

        issue.setText(complain.getIssue());
        complain_id.setText(complain.getComplainId());
        priority.setText(complain.getPriority());


        if(complain.getPayment()!=null && !complain.getPayment().trim().equals("null") && !complain.getPayment().trim().equals("")){
            payment.setText(complain.getPayment()+".00");
        }else {
            payment.setText("0");
        }

        service_type.setText(complain.getService_type());
        status.setText(complain.getStatus());




        if((getIntent().getStringExtra("hide_data")).equals("yes")){
            feed_back.setVisibility(View.GONE);
            collected_amt.setVisibility(View.GONE);
            fix.setVisibility(View.GONE);
        }

        if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("1")) {
            feed_back.setVisibility(View.VISIBLE);
            collected_amt.setVisibility(View.VISIBLE);
            fix.setVisibility(View.VISIBLE);

            if(complain.getStatus().trim().equalsIgnoreCase("fixed")){
                feed_back.setVisibility(View.GONE);
                collected_amt.setVisibility(View.GONE);
                fix.setVisibility(View.GONE);
            }
        }

        if(complain.getService_type().contains("free") || complain.getService_type().contains("Free")){
            collected_amt.setVisibility(View.GONE);
        }

        aten_date.setVisibility(View.GONE);
        agent.setVisibility(View.GONE);
        assign.setVisibility(View.GONE);

        if((getIntent().getStringExtra("hide_data")).equals("bill_no")){
            feed_back.setVisibility(View.GONE);
            collected_amt.setVisibility(View.GONE);
            fix.setVisibility(View.GONE);

            aten_date.setVisibility(View.VISIBLE);
            agent.setVisibility(View.VISIBLE);
            assign.setVisibility(View.VISIBLE);


            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("token",prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, ""));

            ConnectivityInterface connectivityInterface = new ConnectivityInterface(AGENT_LIST,hashMap,AGENT_LIST_REQUEST,this,AGENT_LIST_BASE_URL);
            connectivityInterface.startApiProcessing();
        }

        agent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agent_id = agentId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assigned_clicked = false;
                if (!NetworkStatus.getInstance().isConnected(ComplaintDetails.this)) {
                    CommonUtility.showMessage(ComplaintDetails.this, "Please check your internet connectivity.");
                    return;
                }

                createOTP(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, ""));

            }
        });


        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assigned_clicked = true;

                if (!NetworkStatus.getInstance().isConnected(ComplaintDetails.this)) {
                    CommonUtility.showMessage(ComplaintDetails.this, "Please check your internet connectivity.");
                    return;
                }

                // API CALL
                assignComplain(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, ""));


            }
        });


        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener purchase_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        aten_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ComplaintDetails.this,purchase_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myCalendar = Calendar.getInstance();



    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        aten_date.setText("  "+sdf.format(myCalendar.getTime()));
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

    /**
     * This method is used to retrieve the details of the complain.
     */
    private void upDateComplainDetails(String token) {

        HashMap<String,JSONObject> dataMap =null;
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("agent_feedback",feed_back.getText().toString().trim());
            jsonObject.put("status", "fixed");
            jsonObject.put("id",complain.getId());
            jsonObject.put("payment_received",collected_amt.getText().toString().trim());

            dataMap = new HashMap<>();
            dataMap.put("complain",jsonObject);

        }catch (Exception e){}

        if(complain.getComplainType() ==1){
            ConnectivityInterface connectivityInterface = new ConnectivityInterface(UPDATE_ASSIGNED_COMPLAIN,UPDATE_ASSIGNED_COMPLAIN_REQUEST,this,UPDATE_ASSIGNED_COMPLAIN_BASE_URL,dataMap,token);
            connectivityInterface.startApiProcessing();
        }
        if(complain.getComplainType() ==0){
            ConnectivityInterface connectivityInterface = new ConnectivityInterface(UPDATE_ASSIGNED_COMPLAIN_OLD,OLD_UPDATE_ASSIGNED_COMPLAIN_REQUEST,this,UPDATE_ASSIGNED_COMPLAIN_OLD_BASE_URL,dataMap,token);
            connectivityInterface.startApiProcessing();
        }

    }
    private void assignComplain(String token) {



        if(complain.getComplainType() ==1){

            HashMap<String,JSONObject> dataMap =null;
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status", "new");
                jsonObject.put("id",complain.getId());
                jsonObject.put("user_id",agent_id);
                jsonObject.put("attend_date",aten_date.getText().toString().trim());

                dataMap = new HashMap<>();
                dataMap.put("complain",jsonObject);

            }catch (Exception e){}

            ConnectivityInterface connectivityInterface = new ConnectivityInterface(UPDATE_ASSIGNED_COMPLAIN,UPDATE_ASSIGNED_COMPLAIN_REQUEST,this,UPDATE_ASSIGNED_COMPLAIN_BASE_URL,dataMap,token);
            connectivityInterface.startApiProcessing();
        }
        if(complain.getComplainType() ==0){

            HashMap<String ,String> inputHashMap = new HashMap<>();
            inputHashMap.put("token",prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN,PrefferenceManager.DEFAULT_LOGIN_TOKEN));
            inputHashMap.put("agent_id",agent_id);
            inputHashMap.put("complain_id",complain.getId());

            ConnectivityInterface connectivityInterface = new ConnectivityInterface(ASSIGN_COMPLAIN_OLD,inputHashMap,OLD_UPDATE_ASSIGNED_COMPLAIN_REQUEST,this,ASSIGN_COMPLAIN_OLD_BASE_URL);
            connectivityInterface.startApiProcessing();
        }

    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(ComplaintDetails.this);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }

        if (requestData == UPDATE_ASSIGNED_COMPLAIN_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        if(assigned_clicked){
                            CommonUtility.finishPage(ComplaintDetails.this,"Complain assigned to agent successfully.",ComplaintDetails.this);

                        }else {
                            CommonUtility.finishPage(ComplaintDetails.this,"Complain status updated successfully.",ComplaintDetails.this);

                        }

                        if(!page_navigation_form_task_assigned_page)
                            Util.isLoadHomePage_new = true;

                    } else {
                        CommonUtility.showMessage(ComplaintDetails.this, "", message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(ComplaintDetails.this, "", "Something went wrong,please try again.");
                }
            } else {
                CommonUtility.showMessage(ComplaintDetails.this, "", "Something went wrong,please try again.");
            }
        }



        if (requestData == OLD_UPDATE_ASSIGNED_COMPLAIN_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        if(assigned_clicked){
                            CommonUtility.finishPage(ComplaintDetails.this,"Complain assigned to agent successfully.",ComplaintDetails.this);

                        }else {
                            CommonUtility.finishPage(ComplaintDetails.this,"Complain status updated successfully.",ComplaintDetails.this);

                        }
                        if(!page_navigation_form_task_assigned_page)
                        Util.isLoadHomePage_old = true;

                    } else {
                        CommonUtility.showMessage(ComplaintDetails.this, "", message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(ComplaintDetails.this, "", "Something went wrong,please try again.");
                }
            } else {
                CommonUtility.showMessage(ComplaintDetails.this, "", "Something went wrong,please try again.");
            }
        }





        if (requestData == OTP_VALIDATE_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {
                        upDateComplainDetails(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, ""));
                    } else {
                        CommonUtility.showMessage(ComplaintDetails.this, "", message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(ComplaintDetails.this, "", "Something went wrong,please try again.");
                }
            } else {
                CommonUtility.showMessage(ComplaintDetails.this, "", "Something went wrong,please try again.");
            }
        }



        if (requestData == CREATE_OTP_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        showChangeLangDialog();

                    } else {
                        CommonUtility.showMessage(ComplaintDetails.this, "", message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(ComplaintDetails.this, "", "Something went wrong,please try again.");
                }
            } else {
                CommonUtility.showMessage(ComplaintDetails.this, "", "Something went wrong,please try again.");
            }
        }



        /////////////////////////////////////////////////////////////////////



        if(requestData == AGENT_LIST_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");

                    agent_name.clear();
                    agentId.clear();
                    agentId.add("0");
                    agent_name.add(SELECT_AGENT);


                    JSONArray jsonArray = jsonObject.optJSONArray("data");


                    if(jsonArray.length() >0){

                        for(int i=0;i<jsonArray.length();i++){

                            if(!jsonArray.optJSONObject(i).optString("flag").equals("1") && jsonArray.optJSONObject(i).optString("type").equals("1")){
                                agentId.add(jsonArray.optJSONObject(i).optString("id"));
                                agent_name.add(jsonArray.optJSONObject(i).optString("fname")+" "+jsonArray.optJSONObject(i).optString("mname")+" "+jsonArray.optJSONObject(i).optString("lname"));
                            }
                        }

                        ArrayAdapter adapter = new ArrayAdapter(ComplaintDetails.this,R.layout.produt_spinner,agent_name);
                        agent.setAdapter(adapter);

                    }else{
                        ArrayAdapter adapter = new ArrayAdapter(ComplaintDetails.this,R.layout.produt_spinner,agent_name);
                        agent.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
            }
        }

    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        edt.setText("");

        dialogBuilder.setTitle("Enter Your OTP");
        dialogBuilder.setMessage("");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String otp = edt.getText().toString().trim();
                if(!otp.equals("")){
                    if(otp.length()==6){

                        validateOTP(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, ""),otp);

                    }else{
                        Toast.makeText(getApplicationContext(),"Please enter a valid OTP.",Toast.LENGTH_LONG).toString();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter your OTP.",Toast.LENGTH_LONG).toString();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.show();
    }

    /**
     * This method is responsible to valiadte OTP at server end.
     * @param token
     * @param otp
     */
    private void validateOTP(String token , String otp){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("token",token);
        inputHashMap.put("otp",otp);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(OTP_VALIDATE,inputHashMap,OTP_VALIDATE_REQUEST,this,OTP_VALIDATE_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    /**
     * This method is responsible to create OTP at server end.
     * @param token
     */
    private void createOTP(String token){


        if(collected_amt.getVisibility() == View.VISIBLE){
            if(collected_amt.getText().toString().trim().equals("")){
                CommonUtility.showMessage(ComplaintDetails.this, "", "Please enter received amount.");
                return;
            }
        }

        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("token",token);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(CREATE_OTP,inputHashMap,CREATE_OTP_REQUEST,this,CREATE_OTP_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.page_navigation_form_task_assigned_page = false;
    }
}
