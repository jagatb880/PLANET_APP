package com.release.bibhu.planetappliances.Activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.release.bibhu.planetappliances.Netwok.NetworkStatus;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.ProgressHandler;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.release.bibhu.planetappliances.Util.ApiConstants.ADD_CUSTOMER;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ADD_CUSTOMER_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.ADD_CUSTOMER_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.PRODUCT_LIST_REQUEST;

public class AddCustomer extends AppCompatActivity implements ConnectivityInterface.ApiInterafce {

    EditText Name,Phone,Address,Purchase_Date,Expiry_Date,paymnet,Email,Bill_No;
    Spinner Product;
    Button Add;
    String Product_Id = "";

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    private Toolbar mToolbar;
    String token;
    Calendar myCalendar,myCalendar1;
    ArrayList<String> product_name = new ArrayList<>();
    ArrayList<String> product_id = new ArrayList<>();


    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){
        Name = (EditText) findViewById(R.id.name);
        Phone = (EditText) findViewById(R.id.mobile);
        Address = (EditText) findViewById(R.id.address);
        Email = (EditText) findViewById(R.id.email);
        Purchase_Date = (EditText) findViewById(R.id.purchase_date);
        Expiry_Date = (EditText) findViewById(R.id.expiry_date);
        Bill_No = (EditText) findViewById(R.id.bill_no);
        paymnet = (EditText) findViewById(R.id.paymnet);
        Product = (Spinner) findViewById(R.id.product);
        Add = (Button) findViewById(R.id.add);


        prefferenceManager = PrefferenceManager.getFeaturePreference(AddCustomer.this);
        token = prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN,"");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("Add Customer");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        _init();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
            }
        });
        myCalendar = Calendar.getInstance();
        myCalendar1 = Calendar.getInstance();
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



        final DatePickerDialog.OnDateSetListener expiry_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        Purchase_Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddCustomer.this, purchase_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Expiry_Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddCustomer.this,expiry_date, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Product_Id = ""+position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /**
         * Getting Product List form Server.
         */
        getProductList(token);

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Purchase_Date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Expiry_Date.setText(sdf.format(myCalendar1.getTime()));
    }


    /**
     * Getting product list form server.
     * @param token
     */
    private void getProductList(String token){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token",token);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(PRODUCT_LIST,hashMap,PRODUCT_LIST_REQUEST,this,PRODUCT_LIST_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    /**
     * This method is applicable to validate user data;
     */
    private void validateUser(){
        String cName = Name.getText().toString().trim();
        String cPhone = Phone.getText().toString().trim();
        String cAddress = Address.getText().toString().trim();
        String eMail = Email.getText().toString().trim();

        if(cName.equals("")){
            CommonUtility.showMessage(AddCustomer.this,"Please enter customer name.");
            return;
        }
        if(cPhone.equals("")){
            CommonUtility.showMessage(AddCustomer.this,"Please enter customer mobile no.");
            return;
        }

        if(cPhone.length() != 10){
            CommonUtility.showMessage(AddCustomer.this,"Please enter valid mobile no.");
            return;
        }

        if(cAddress.equals("")){
            CommonUtility.showMessage(AddCustomer.this,"Please enter customer address.");
            return;
        }
        if(!eMail.equals("")){
            if(!CommonUtility.isValidMail(eMail)){
            CommonUtility.showMessage(AddCustomer.this,"Please enter valid email address.");
            return;
            }
        }

        if(!NetworkStatus.getInstance().isConnected(AddCustomer.this)){
            CommonUtility.showMessage(AddCustomer.this,"Please check your internet connection.");
            return;
        }
        if(Product_Id.trim().equals("0")){
            Log.d("Product","Product");
            CommonUtility.showMessage(AddCustomer.this,"Please select yours product.");
            return;
        }

        if(Bill_No.getText().toString().trim().equals("")){
            CommonUtility.showMessage(AddCustomer.this,"Please enter bill no.");
            return;
        }


        /**
         * Start API Call.
         */
        addCustomer(token,cName,cPhone,cAddress);
    }

    /**
     * This method is responsible to send data to server for new customer creation .
     */
    private void addCustomer(String token , String name ,String phone,String address){


        HashMap<String,JSONObject> dataMap =null;
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",name);
            jsonObject.put("phone",phone);
            jsonObject.put("address",address);
//            jsonObject.put("product_id",Product_Id);
//            jsonObject.put("purchase_date",Purchase_Date.getText().toString().trim());
//            jsonObject.put("expiry_date",Expiry_Date.getText().toString().trim());
            jsonObject.put("payment_due",paymnet.getText().toString().trim());
            jsonObject.put("email",Email.getText().toString().trim());

            JSONObject BillNo = new JSONObject();
            BillNo.put("billno",Bill_No.getText().toString().trim());

            JSONArray product_array = new JSONArray();
            JSONObject  product_object = new JSONObject();

            product_object.put("purchase_date",Purchase_Date.getText().toString().trim());
            product_object.put("expiry_date",Expiry_Date.getText().toString().trim());
            product_object.put("product_id",Product_Id);
            product_array.put(product_object);


            jsonObject.put("product",product_array);
            jsonObject.put("billing",BillNo);

            dataMap = new HashMap<>();
            dataMap.put("customer",jsonObject);

        }catch (Exception e){}


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ADD_CUSTOMER,ADD_CUSTOMER_REQUEST,this,ADD_CUSTOMER_BASE_URL,dataMap,token);
        connectivityInterface.startApiProcessing();
    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(AddCustomer.this);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }

        if(requestData == ADD_CUSTOMER_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");

                    if(response.contains("SUCCESS")){

                        Name.setText("");
                        Phone.setText("");
                        Address.setText("");
                        Purchase_Date.setText("");
                        Expiry_Date.setText("");
                        paymnet.setText("");
                        Email.setText("");
                        Bill_No.setText("");
                        Product.setSelection(0);
                        CommonUtility.showMessage(AddCustomer.this,"","Customer added successfully.");

                    }else{
                        CommonUtility.showMessage(AddCustomer.this,"",message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(AddCustomer.this,"","Something went wrong,please try again.");
                }
            }else {
                CommonUtility.showMessage(AddCustomer.this,"","Something went wrong,please try again.");
            }
        }


        //***************************************************************************************************************//

        if(requestData == PRODUCT_LIST_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){

                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        product_id.clear();
                        product_name.clear();
                        product_id.add("0");
                        product_name.add("----- Select Product Name -----");

                        if(jsonArray.length() >0){

                            for(int i=0;i<jsonArray.length();i++){
                                if(!jsonArray.optJSONObject(i).optString("flag").trim().equals("1")) {
                                    product_id.add(jsonArray.optJSONObject(i).optString("id"));
                                    product_name.add(jsonArray.optJSONObject(i).optString("model"));
                                }
                            }

                            ArrayAdapter adapter = new ArrayAdapter(AddCustomer.this,R.layout.produt_spinner,product_name);
                            Product.setAdapter(adapter);

                        }else{
                            CommonUtility.finishPage(AddCustomer.this,"Please add product information before adding customer.",AddCustomer.this);
                        }
                    }else{
                        CommonUtility.finishPage(AddCustomer.this,message,AddCustomer.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.finishPage(AddCustomer.this,"Something went wrong,please try again.",AddCustomer.this);
                }
            }else {
                CommonUtility.finishPage(AddCustomer.this,"Something went wrong,please try again.",AddCustomer.this);
            }

        }




    }
}
