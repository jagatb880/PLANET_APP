package com.release.bibhu.planetappliances.Fragment;

/**
 * Created by MUVI on 03-Feb-18.
 */
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Activity.PreLogin;
import com.release.bibhu.planetappliances.Adaptor.ComplainAdapter;
import com.release.bibhu.planetappliances.Adaptor.EnqueryListAdapter;
import com.release.bibhu.planetappliances.Model.Complain;
import com.release.bibhu.planetappliances.Model.Product;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.NEW_CUSTOMER_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.NEW_CUSTOMER_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_USER_DETAILS_FOR_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_USER_DETAILS_FOR_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.CUSTOMER_DETAILS_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.CUSTOMER_ENQUERY_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.CUSTOMER_INFO_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ENQUERY_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.MODEL_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.NEW_CUSTOMER_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.PRODUCT_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.PRODUCT_USER_DETAILS_FOR_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.REGISTER_COMPLAIN_REQUEST;


public class AddNewComplain extends Fragment implements ConnectivityInterface.ApiInterafce {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    FloatingActionButton add_customer;
    ListView listview;
    TextView no_data;
    ArrayList<Complain> complains_list = new ArrayList<>();
    EnqueryListAdapter enqueryAdapter;

    ArrayList<String> product_name = new ArrayList<>();
    ArrayList<String> product_id = new ArrayList<>();
    ArrayList<String> serviceType = new ArrayList<>();

    ComplainAdapter complainAdapter;
    ArrayList<Product> products = new ArrayList<>();

    String CID ;
    String PRODUCT_ID = "";

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

    EditText new_bill_no,new_dealer;
    String SERVICE_ID = "";

    public AddNewComplain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_new_complian, container, false);
        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());


        name_new = (EditText) rootView.findViewById(R.id.name_new);
        mobile_new = (EditText) rootView.findViewById(R.id.mobile_new);
        address_new = (EditText) rootView.findViewById(R.id.address_new);
        dop = (EditText) rootView.findViewById(R.id.dop);
        issue_new = (EditText) rootView.findViewById(R.id.issue_new);
        new_bill_no = (EditText)rootView. findViewById(R.id.new_bill_no);
        new_dealer = (EditText) rootView.findViewById(R.id.new_dealer);
        service_type = (Spinner) rootView.findViewById(R.id.service_type);
        product = (Spinner) rootView.findViewById(R.id.product);
        model = (Spinner) rootView.findViewById(R.id.model);
        create_new = (Button) rootView.findViewById(R.id.create_new);



        getProductList(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN,""));


        serviceType.add("----- Select Service Type -----");
        serviceType.add("Under Warranty");
        serviceType.add("Out Of Warranty");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.produt_spinner,serviceType);
        service_type.setAdapter(adapter);

        service_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SERVICE_ID = serviceType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PRODUCT_ID = product_id.get(position);
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

                if(product.getSelectedItem().toString().contains("----- Select Product Name -----")){
                    Toast.makeText(getActivity(),"Please select your product.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(service_type.getSelectedItem().toString().contains("----- Select Service Type -----")){
                    Toast.makeText(getActivity(),"Please select service type.",Toast.LENGTH_SHORT).show();
                    return;
                }

                registerComplain_new();
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
                new DatePickerDialog(getActivity(), purchase_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dop.setText("  "+sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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



    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(getActivity());
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
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

                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.produt_spinner,product_name);
                        product.setAdapter(adapter);

                    }else{
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.produt_spinner,product_name);
                        product.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
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
                        CommonUtility.showMessage(getActivity(),"",message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(getActivity(),"","Something went wrong,please try again.");
                }
            }else {
                CommonUtility.showMessage(getActivity(),"","Something went wrong,please try again.");
            }
        }


//*****************************************************************************************************************//


    }

    private void showCustomDialog(String complainId){

        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity());
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


                try{
                    name_new.setText("");
                    address_new.setText("");
                    dop.setText("");
                    issue_new.setText("");
                    mobile_new.setText("");
                    new_bill_no.setText("");
                    new_dealer.setText("");
                    service_type.setSelection(0);
                    product.setSelection(0);
                }catch (Exception e){}
            }
        });

    }

    public void getCustomerDetails(){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("phone_no",mobile_new.getText().toString().trim());

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(PRODUCT_USER_DETAILS_FOR_COMPLAIN,inputHashMap,CUSTOMER_INFO_REQUEST,this,PRODUCT_USER_DETAILS_FOR_COMPLAIN_BASE_URL);
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


}