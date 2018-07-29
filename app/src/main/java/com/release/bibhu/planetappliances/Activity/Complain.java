package com.release.bibhu.planetappliances.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import static com.release.bibhu.planetappliances.Util.Constants.CUSTOMER_DETAILS_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.CUSTOMER_ENQUERY_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.CUSTOMER_INFO_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.MODEL_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.NEW_CUSTOMER_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.PRODUCT_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.PRODUCT_USER_DETAILS_FOR_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.REGISTER_COMPLAIN_REQUEST;

public class Complain extends AppCompatActivity implements ConnectivityInterface.ApiInterafce {

    Toolbar mToolbar;
    PrefferenceManager prefferenceManager;
    ProgressHandler progressHandler;

    EditText cid,bill_no,issue;
    Button product_button,submit;
    ArrayAdapter model_adapter;


    LinearLayout name_layout,mobile_layout,address_layout,email_layout;
    TextView customer_name,address,mobile,email;
    RecyclerView recyclerView;
    ComplainAdapter complainAdapter;
    ArrayList<Product> products = new ArrayList<>();

    String CID ;

    EditText name_new,mobile_new,address_new,dop,issue_new;
    Spinner service_type,product,model;
    Button create_new;
    Calendar myCalendar;

    EditText name_enc,mobile_enc,address_enc,email_enc,cs_feed_back;
    Spinner product_enc;
    CheckBox is_positive;
    Button add;

    RelativeLayout enc_ll,exist_ll;
    ScrollView new_customer_ll;

    ArrayList<String> product_name = new ArrayList<>();
    ArrayList<String> product_id = new ArrayList<>();
    ArrayList<String> serviceType = new ArrayList<>();

    ArrayList<String> model_name = new ArrayList<>();
    ArrayList<String> model_id = new ArrayList<>();

    String PRODUCT_ID = "";
    String MODEL_ID = "";
    String SERVICE_ID = "";

    LinearLayout ll1,ll2,ll3;
    LinearLayout details_text,product_text,submit_layout,customer_details_layout;

    EditText new_bill_no,new_dealer;



    /**

     * This method is only responsible to initialize the view components.
     */
    private void _init(){


        Util.selected_page_row = 0;
        prefferenceManager = PrefferenceManager.getFeaturePreference(Complain.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        ll1 = (LinearLayout)findViewById(R.id.ll1);
        ll2 = (LinearLayout)findViewById(R.id.ll2);
        ll3 = (LinearLayout)findViewById(R.id.ll3);


        details_text = (LinearLayout)findViewById(R.id.details_text);
        product_text = (LinearLayout)findViewById(R.id.product_text);
        submit_layout = (LinearLayout)findViewById(R.id.submit_layout);
        customer_details_layout = (LinearLayout)findViewById(R.id.customer_details_layout);

        enc_ll = (RelativeLayout) findViewById(R.id.enc_ll);
        exist_ll = (RelativeLayout) findViewById(R.id.exist_ll);
        new_customer_ll = (ScrollView) findViewById(R.id.new_customer_ll);

        cid = (EditText) findViewById(R.id.cid);
        bill_no = (EditText) findViewById(R.id.bill_no);
        issue = (EditText) findViewById(R.id.issue);
        product_button = (Button) findViewById(R.id.product_button);
        submit = (Button) findViewById(R.id.submit);

        name_layout = (LinearLayout) findViewById(R.id.name_layout);
        mobile_layout = (LinearLayout) findViewById(R.id.mobile_layout);
        address_layout = (LinearLayout) findViewById(R.id.address_layout);
        email_layout = (LinearLayout) findViewById(R.id.email_layout);

        customer_name = (TextView) findViewById(R.id.customer_name);
        address = (TextView) findViewById(R.id.address);
        mobile = (TextView) findViewById(R.id.mobile);
        email = (TextView) findViewById(R.id.email);

        name_new = (EditText) findViewById(R.id.name_new);
        mobile_new = (EditText) findViewById(R.id.mobile_new);
        address_new = (EditText) findViewById(R.id.address_new);
        dop = (EditText) findViewById(R.id.dop);
        issue_new = (EditText) findViewById(R.id.issue_new);
        new_bill_no = (EditText) findViewById(R.id.new_bill_no);
        new_dealer = (EditText) findViewById(R.id.new_dealer);
        service_type = (Spinner) findViewById(R.id.service_type);
        product = (Spinner) findViewById(R.id.product);
        model = (Spinner) findViewById(R.id.model);
        create_new = (Button) findViewById(R.id.create_new);

        name_enc = (EditText) findViewById(R.id.name_enc);
        mobile_enc = (EditText) findViewById(R.id.mobile_enc);
        address_enc = (EditText) findViewById(R.id.address_enc);
        email_enc = (EditText) findViewById(R.id.email_enc);
        cs_feed_back = (EditText) findViewById(R.id.cs_feed_back);
        product_enc = (Spinner) findViewById(R.id.product_enc);
        add = (Button) findViewById(R.id.add);
        is_positive = (CheckBox) findViewById(R.id.is_positive);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);



        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle("Complain");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        _init();
        getProductList(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN,""));


        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exist_ll.setVisibility(View.VISIBLE);
                new_customer_ll.setVisibility(View.GONE);
                enc_ll.setVisibility(View.GONE);

                product.setSelection(0);
                service_type.setSelection(0);
                product_enc.setSelection(0);

                ll1.setBackgroundColor(Color.parseColor("#1c7768"));
                ll2.setBackgroundColor(Color.parseColor("#d4d4d4"));
                ll3.setBackgroundColor(Color.parseColor("#d4d4d4"));

            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exist_ll.setVisibility(View.GONE);
                new_customer_ll.setVisibility(View.VISIBLE);
                enc_ll.setVisibility(View.GONE);

                product.setSelection(0);
                service_type.setSelection(0);
                product_enc.setSelection(0);

                ll1.setBackgroundColor(Color.parseColor("#d4d4d4"));
                ll2.setBackgroundColor(Color.parseColor("#1c7768"));
                ll3.setBackgroundColor(Color.parseColor("#d4d4d4"));

            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exist_ll.setVisibility(View.GONE);
                new_customer_ll.setVisibility(View.GONE);
                enc_ll.setVisibility(View.VISIBLE);

                product.setSelection(0);
                service_type.setSelection(0);
                product_enc.setSelection(0);

                ll1.setBackgroundColor(Color.parseColor("#d4d4d4"));
                ll2.setBackgroundColor(Color.parseColor("#d4d4d4"));
                ll3.setBackgroundColor(Color.parseColor("#1c7768"));
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



        dop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Complain.this, purchase_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProduct();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateComplainDetails();
            }
        });

        serviceType.add("----- Select Service Type -----");
        serviceType.add("Under Warranty");
        serviceType.add("Out Of Warranty");
        ArrayAdapter adapter = new ArrayAdapter(Complain.this,R.layout.produt_spinner,serviceType);
        service_type.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener1(Complain.this, recyclerView, new ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.selected_page_row = position;
                complainAdapter.notifyDataSetChanged();

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PRODUCT_ID = product_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        product_enc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PRODUCT_ID = product_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        service_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SERVICE_ID = serviceType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mobile_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mobile_new.getText().length() == 10){
                    /**
                     * Call API to get customer details.
                     */

                    getCustomerDetails();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        create_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobile_new.length() == 0){
                    Toast.makeText(getApplicationContext(),"Please enter customer mobile no.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mobile_new.length() != 10){
                    Toast.makeText(getApplicationContext(),"Please enter valid mobile number.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name_new.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter customer name.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(address_new.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter customer address.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dop.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter date of purchase.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(product.getSelectedItem().toString().contains("----- Select Product Name -----")){
                    Toast.makeText(getApplicationContext(),"Please select your product.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(service_type.getSelectedItem().toString().contains("----- Select Service Type -----")){
                    Toast.makeText(getApplicationContext(),"Please select service type.",Toast.LENGTH_SHORT).show();
                    return;
                }

                registerComplain_new();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             

                validateUser();
            }
        });
    }

    private void updateLabel() {
//        String myFormat = "dd/MM/yy"; //In which you need put here
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dop.setText("  "+sdf.format(myCalendar.getTime()));
    }
    public static class RecyclerTouchListener1 implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener1 clickListener;

        public RecyclerTouchListener1(Context context, final RecyclerView recyclerView, final ClickListener1 clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener1 {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     * This method is used to check validation before getting product list.
     */
    private void validateProduct (){
        Util.selected_page_row = 0;
        CID = "";



        String billNo = bill_no.getText().toString().trim();

        if(billNo.equals("")){
            CommonUtility.showMessage(Complain.this,"Please enter your Bill No.");
            return;
        }
        if(!NetworkStatus.getInstance().isConnected(Complain.this)){
            CommonUtility.showMessage(Complain.this,"Please check your internet connectivity.");
            return;
        }

        submit_layout.setVisibility(View.GONE);
        details_text.setVisibility(View.GONE);
        product_text.setVisibility(View.GONE);
        customer_details_layout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Start API calling to get user details as well as product details.

        Get_User_And_Product_Deatils (billNo);
    }



    /**
     * This method is used to check validation before registering complain.
     */
    private void validateComplainDetails (){

        String issue_details = issue.getText().toString().trim();

        if(issue_details.equals("")){
            CommonUtility.showMessage(Complain.this,"Please enter your complain details. ");
            return;
        }

        if(products.size()<1){
            CommonUtility.showMessage(Complain.this,"You have no product to register a complain. ");
            return;
        }

        if(!NetworkStatus.getInstance().isConnected(Complain.this)){
            CommonUtility.showMessage(Complain.this,"Please check your internet connectivity.");
            return;
        }

        // Start API calling to get user details as well as product details.
        registerComplain(CID,issue_details,products.get(Util.selected_page_row).getProduct_id());
    }



    /**
     * API Calling to get customer and product details.
     * @param bill_no
     */
    private void Get_User_And_Product_Deatils(String bill_no){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("phone_no",bill_no);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(PRODUCT_USER_DETAILS_FOR_COMPLAIN,inputHashMap,PRODUCT_USER_DETAILS_FOR_COMPLAIN_REQUEST,this,PRODUCT_USER_DETAILS_FOR_COMPLAIN_BASE_URL);
        connectivityInterface.startApiProcessing();
    }



    /**
     * This method is responsible for complain register.
     */
    private void registerComplain_new(){

        HashMap<String,JSONObject> dataMap =null;

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("issue",issue_new.getText().toString().trim());
            jsonObject.put("product_id",PRODUCT_ID);
            jsonObject.put("priority","Medium");
            jsonObject.put("service_type",service_type.getSelectedItem().toString());
            jsonObject.put("status","New");
            jsonObject.put("mobile",mobile_new.getText().toString().trim());
            jsonObject.put("dop",dop.getText().toString().trim());
            jsonObject.put("name",name_new.getText().toString().trim());
            jsonObject.put("address",address_new.getText().toString().trim());
            jsonObject.put("billno",new_bill_no.getText().toString().trim());
            jsonObject.put("dealer_name",new_bill_no.getText().toString().trim());
            jsonObject.put("received_by","ANDROID APP");

            dataMap = new HashMap<>();
            dataMap.put("complain",jsonObject);

        }catch (Exception e){}

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(NEW_CUSTOMER_COMPLAIN,NEW_CUSTOMER_COMPLAIN_REQUEST,this,NEW_CUSTOMER_COMPLAIN_BASE_URL,dataMap,"");
        connectivityInterface.startApiProcessing();

    }




    /**
     * This method is responsible for complain register.
     * @param customer_id
     * @param issue_details
     * @param product_id
     */
    private void registerComplain(String customer_id,String issue_details ,String product_id){

        HashMap<String,JSONObject> dataMap =null;

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("issue",issue_details);
            jsonObject.put("customer_id", customer_id);
            jsonObject.put("product_id",product_id);
            jsonObject.put("priority","High");
            jsonObject.put("service_type","Not Defined");
            jsonObject.put("status","New");
            jsonObject.put("received_by","ANDROID APP");

            Calendar cal = Calendar.getInstance();
            String myFormat = "MM/dd/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            try{
                jsonObject.put("complain_date",""+cal.getTime());

            }catch (Exception e){}

            dataMap = new HashMap<>();
            dataMap.put("complain",jsonObject);





        }catch (Exception e){}


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(REGISTER_COMPLAIN,REGISTER_COMPLAIN_REQUEST,this,REGISTER_COMPLAIN_BASE_URL,dataMap,"");
        connectivityInterface.startApiProcessing();


    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(Complain.this);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }


        if (requestData == CUSTOMER_ENQUERY_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {
                        CommonUtility.showMessage(Complain.this, "", message);

                    } else {
                        CommonUtility.showMessage(Complain.this, "", message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(Complain.this, "", "Something went wrong,please try again.");
                }
            } else {
                CommonUtility.showMessage(Complain.this, "", "Something went wrong,please try again.");
            }

        }
        

        //***************************************************************************************************************//

        if(requestData == PRODUCT_LIST_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");

                    product_id.clear();
                    product_name.clear();
                    product_id.add("0");
                    product_name.add("----- Select Product Name -----");


                        JSONArray jsonArray = jsonObject.optJSONArray("data");


                        if(jsonArray.length() >0){

                            for(int i=0;i<jsonArray.length();i++){

                                if(!jsonArray.optJSONObject(i).optString("flag").trim().equals("1")){
                                    product_id.add(jsonArray.optJSONObject(i).optString("id"));
                                    product_name.add(jsonArray.optJSONObject(i).optString("model"));
                                }
                            }

                            ArrayAdapter adapter = new ArrayAdapter(Complain.this,R.layout.produt_spinner,product_name);
                            product.setAdapter(adapter);
                            product_enc.setAdapter(adapter);

                        }else{
                            ArrayAdapter adapter = new ArrayAdapter(Complain.this,R.layout.produt_spinner,product_name);
                            product.setAdapter(adapter);
                            product_enc.setAdapter(adapter);                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
            }

        }
        
        //*****************************************************************************************************************//




        if(requestData == MODEL_LIST_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");

                    model_name.clear();
                    model_id.clear();
                    model_id.add("0");
                    model_name.add("----- Select Model Name -----");


                    JSONArray jsonArray = jsonObject.optJSONArray("data");

                    if(jsonArray.length() >0){

                        for(int i=0;i<jsonArray.length();i++){
                            model_id.add(jsonArray.optJSONObject(i).optString("id"));
                            model_name.add(jsonArray.optJSONObject(i).optString("name"));
                        }

                        model_adapter = new ArrayAdapter(Complain.this,R.layout.produt_spinner,model_name);
                        model.setAdapter(model_adapter);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }




        //*****************************************************************************************************************//

        if(requestData == PRODUCT_USER_DETAILS_FOR_COMPLAIN_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){

                        products.clear();

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = data.optJSONArray("product_map");

                        for(int i=0 ; i<jsonArray.length();i++){

                            Log.v("BIBHU123","xxxx=="+jsonArray.optJSONObject(i).optString("id"));
                            jsonArray.optJSONObject(i).optString("id");

                            Product product = new Product();

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i).optJSONObject("product");
                            product.setProduct_id(jsonObject1.optString("id"));
                            product.setProduct_name(jsonObject1.optString("name"));
                            product.setProduct_make(jsonObject1.optString("make"));
                            product.setProduct_model(jsonObject1.optString("model"));

                            products.add(product);


                            Log.v("BIBHU123","xxxssssssssx=="+jsonObject1.optString("id"));

                        }

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        complainAdapter = new ComplainAdapter(Complain.this,products);
                        recyclerView.setAdapter(complainAdapter);

                        JSONObject jsonObject1 = data.getJSONObject("customer");
                        CID = jsonObject1.optString("id").trim();

                        if(!(jsonObject1.optString("name").trim().equals(""))){
                            name_layout.setVisibility(View.VISIBLE);
                            customer_name.setText(jsonObject1.optString("name").trim());
                        }else {
                            name_layout.setVisibility(View.GONE);
                        }

                        if(!(jsonObject1.optString("phone").trim().equals(""))){
                            mobile_layout.setVisibility(View.VISIBLE);
                            mobile.setText(jsonObject1.optString("phone").trim());
                        }else {
                            mobile_layout.setVisibility(View.GONE);
                        }

                        if(!(jsonObject1.optString("address").trim().equals(""))){
                            address_layout.setVisibility(View.VISIBLE);
                            address.setText(jsonObject1.optString("address").trim());
                        }else {
                            address_layout.setVisibility(View.GONE);
                        }

                        if(!(jsonObject1.optString("email").trim().equals("")) && !(jsonObject1.optString("email").trim().equals("null"))){
                            email_layout.setVisibility(View.VISIBLE);
                            email.setText(jsonObject1.optString("email").trim());
                        }else {
                            email_layout.setVisibility(View.GONE);
                        }

                        submit_layout.setVisibility(View.VISIBLE);
                        details_text.setVisibility(View.VISIBLE);
                        product_text.setVisibility(View.VISIBLE);
                        customer_details_layout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);


                    }else{
                        CommonUtility.showMessage(Complain.this,"","Your number is not registered with us.Kindly give complain as New Customer.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(Complain.this,"","Something went wrong,please try again.");
                }
            }else {
                CommonUtility.showMessage(Complain.this,"","Something went wrong,please try again.");
            }
        }

//*****************************************************************************************************************//


        if(requestData == CUSTOMER_INFO_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){


                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject jsonObject1 = data.getJSONObject("customer");

                        if(!(jsonObject1.optString("name").trim().equals(""))){
                            name_new.setText(jsonObject1.optString("name").trim());
                        }

                        if(!(jsonObject1.optString("address").trim().equals(""))){
                            address_new.setText(jsonObject1.optString("address").trim());
                        }
                    }else{
                        name_new.setText("");
                        address_new.setText("");
                    }
                } catch (Exception e) {
                    name_new.setText("");
                    address_new.setText("");
                }
            }else {
                name_new.setText("");
                address_new.setText("");
            }
        }


//*****************************************************************************************************************//


        if(requestData == REGISTER_COMPLAIN_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){

                    String complain_id = jsonObject.optString("complainid");
                    // Show custom dialog for user.

                        showCustomDialog(complain_id);

                    }else{
                        CommonUtility.showMessage(Complain.this,"",message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(Complain.this,"","Something went wrong,please try again.");
                }
            }else {
                CommonUtility.showMessage(Complain.this,"","Something went wrong,please try again.");
            }
        }

        //*****************************************************************************************************************//


        if(requestData == NEW_CUSTOMER_COMPLAIN_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){

                        String complain_id = jsonObject.optString("complainid");
                        // Show custom dialog for user.

                        showCustomDialog(complain_id);

                    }else{
                        CommonUtility.showMessage(Complain.this,"",message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(Complain.this,"","Something went wrong,please try again.");
                }
            }else {
                CommonUtility.showMessage(Complain.this,"","Something went wrong,please try again.");
            }
        }


//*****************************************************************************************************************//



    }

    private void showCustomDialog(String complainId){

        // Create custom dialog object
        final Dialog dialog = new Dialog(Complain.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.adv_custom_dialog);
        // Set dialog title
        dialog.setTitle("");

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.msg);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        text.setText("Your Compilain Id : "+complainId);
        dialog.show();

        // if decline button is clicked, close the custom dialog
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                Intent intent = new Intent(Complain.this,PreLogin.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    /**
     * Getting product list form server.
     * @param token
     */
    private void getProductList(String token){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token","");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(PRODUCT_LIST,hashMap,PRODUCT_LIST_REQUEST,this,PRODUCT_LIST_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    /**
     * Getting product list form server.
     * @param token
     */
    private void getModelList(String token){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token","");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(MODEL_LIST,hashMap,MODEL_LIST_REQUEST,this,MODEL_LIST_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * This method is applicable to validate user data;
     */
    private void validateUser() {
        String cName = name_enc.getText().toString().trim();
        String cPhone = mobile_enc.getText().toString().trim();
        String cAddress = address_enc.getText().toString().trim();
        String eMail = email_enc.getText().toString().trim();
        String customerFeedback = cs_feed_back.getText().toString().trim();

        if (cName.equals("")) {
            CommonUtility.showMessage(Complain.this, "Please enter customer name.");
            return;
        }
        if (cPhone.equals("")) {
            CommonUtility.showMessage(Complain.this, "Please enter customer mobile no.");
            return;
        }

        if (cPhone.length() != 10) {
            CommonUtility.showMessage(Complain.this, "Please enter valid mobile no.");
            return;
        }

        if (cAddress.equals("")) {
            CommonUtility.showMessage(Complain.this, "Please enter customer address.");
            return;
        }
        if (!eMail.equals("")) {
            if (!CommonUtility.isValidMail(eMail)) {
                CommonUtility.showMessage(Complain.this, "Please enter valid email address.");
                return;
            }
        }

        if(product_enc.getSelectedItem().toString().contains("----- Select Product Name -----")){
            CommonUtility.showMessage(getApplicationContext(),"Please select your product.");
            return;
        }

        if (!NetworkStatus.getInstance().isConnected(Complain.this)) {
            CommonUtility.showMessage(Complain.this, "Please check your internet connection.");
            return;
        }



        /**
         * Start API Call.
         */
        addEnquery("", cName, cPhone, cAddress, customerFeedback);
    }

    /**
     * This method is responsible to send data to server for new customer creation .
     */
    private void addEnquery(String token, String name, String phone, String address, String customerFeedback) {


        HashMap<String, JSONObject> dataMap = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("mobile", phone);
            jsonObject.put("email", email_enc.getText().toString().trim());
            jsonObject.put("address", address);
            jsonObject.put("product_id", PRODUCT_ID);
            jsonObject.put("feedback", customerFeedback);

            String is_pos = "0";
            if (is_positive.isChecked())
                is_pos = "1";

            jsonObject.put("is_positive", is_pos);

            dataMap = new HashMap<>();
            dataMap.put("enquiry", jsonObject);

        } catch (Exception e) {
        }


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(CUSTOMER_ENQUERY, CUSTOMER_ENQUERY_REQUEST, this, CUSTOMER_ENQUERY_BASE_URL, dataMap, token);
        connectivityInterface.startApiProcessing();
    }

    public void getCustomerDetails(){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("phone_no",mobile_new.getText().toString().trim());

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(PRODUCT_USER_DETAILS_FOR_COMPLAIN,inputHashMap,CUSTOMER_INFO_REQUEST,this,PRODUCT_USER_DETAILS_FOR_COMPLAIN_BASE_URL);
        connectivityInterface.startApiProcessing();
    }
}
