package com.release.bibhu.planetappliances.Fragment;

/**
 * Created by MUVI on 03-Feb-18.
 */

        import android.app.Activity;
        import android.app.DatePickerDialog;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.release.bibhu.planetappliances.Activity.AddCustomer;
        import com.release.bibhu.planetappliances.Activity.UserDashBoard;
        import com.release.bibhu.planetappliances.Adaptor.EnqueryListAdapter;
        import com.release.bibhu.planetappliances.Model.Complain;
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

        import static com.release.bibhu.planetappliances.Util.ApiConstants.AMC_LIST;
        import static com.release.bibhu.planetappliances.Util.ApiConstants.AMC_LIST_BASE_URL;
        import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST;
        import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST_BASE_URL;
        import static com.release.bibhu.planetappliances.Util.ApiConstants.NEW_CUSTOMER_COMPLAIN;
        import static com.release.bibhu.planetappliances.Util.ApiConstants.NEW_CUSTOMER_COMPLAIN_BASE_URL;
        import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_AMC_COMPLAIN;
        import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_AMC_COMPLAIN_BASE_URL;
        import static com.release.bibhu.planetappliances.Util.Constants.AMC_LIST_REQUEST;
        import static com.release.bibhu.planetappliances.Util.Constants.ENQUERY_LIST_REQUEST;
        import static com.release.bibhu.planetappliances.Util.Constants.NEW_CUSTOMER_COMPLAIN_REQUEST;
        import static com.release.bibhu.planetappliances.Util.Constants.REGISTER_AMC_COMPLAIN_REQUEST;


public class RegisterAMC extends Fragment implements ConnectivityInterface.ApiInterafce {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;

    EditText name, mobile, paymnet, customer_address,amc_date;
    Button create;
    Spinner amc_type;
    Calendar myCalendar;

    ArrayList<String> amc_name = new ArrayList<>();
    ArrayList<String> amc_id = new ArrayList<>();
    String AMC_ID = "";

    public RegisterAMC() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.registeramc, container, false);

        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());
        name = (EditText) rootView.findViewById(R.id.name);
        mobile = (EditText) rootView.findViewById(R.id.mobile);
        paymnet = (EditText) rootView.findViewById(R.id.paymnet);
        customer_address = (EditText) rootView.findViewById(R.id.customer_address);
        create = (Button) rootView.findViewById(R.id.create);
        amc_type = (Spinner) rootView.findViewById(R.id.amc_type);
        amc_date = (EditText) rootView.findViewById(R.id.amc_date);

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


        /**
         *
         */
        amcType();

        amc_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), purchase_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        amc_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AMC_ID = amc_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAmc();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        amc_date.setText("  "+sdf.format(myCalendar.getTime()));
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
     * This API is responsible to get assigned Product against an agent .
     */
    public void amcType() {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(AMC_LIST, inputHashMap, AMC_LIST_REQUEST, this, AMC_LIST_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    /**
     * This method is used to complain AMC type.
     */
    public void registerAmc(){

        if(name.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter customer name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mobile.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter customer mobile no.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(mobile.getText().toString().trim().length() != 10) {
                Toast.makeText(getActivity(), "Please enter valid mobile no.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(AMC_ID.trim().equals("0")){
            Toast.makeText(getActivity(),"Please select service type.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(amc_date.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter AMC date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(customer_address.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter customer address.", Toast.LENGTH_SHORT).show();
            return;
        }


        String TOKEN = prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN);
        String USER_ID = prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID, PrefferenceManager.DEFAULT_USER_ID);


        HashMap<String,JSONObject> dataMap =null;

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id",USER_ID);
            jsonObject.put("name",name.getText().toString());
            jsonObject.put("phone",mobile.getText().toString().trim());
            jsonObject.put("amc_date",amc_date.getText().toString().trim());
            jsonObject.put("amc_type",AMC_ID);
            jsonObject.put("payment",paymnet.getText().toString().trim());
            jsonObject.put("address",customer_address.getText().toString().trim());

            if(AMC_ID.trim().equals("1")){
                JSONArray jsonArray = new JSONArray();

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("status","1");

                JSONObject jsonObject11 = new JSONObject();
                jsonObject11.put("status","0");

                JSONObject jsonObject111 = new JSONObject();
                jsonObject111.put("status","0");

                JSONObject jsonObject1111 = new JSONObject();
                jsonObject1111.put("status","0");

                jsonArray.put(jsonObject1);
                jsonArray.put(jsonObject11);
                jsonArray.put(jsonObject111);
                jsonArray.put(jsonObject1111);

                jsonObject.put("amc_servicing",jsonArray);
            }

            dataMap = new HashMap<>();
            dataMap.put("amc",jsonObject);

        }catch (Exception e){}


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(REGISTER_AMC_COMPLAIN,REGISTER_AMC_COMPLAIN_REQUEST,this,REGISTER_AMC_COMPLAIN_BASE_URL,dataMap,TOKEN);
        connectivityInterface.startApiProcessing();


    }

    /**
     * This method will handle preExecute processing.
     */
    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(getActivity());
        progressHandler.show();

    }


    /**
     * This method will handle postExecute processing.
     */
    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }

        if (requestData == AMC_LIST_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        amc_name.clear();
                        amc_id.clear();

                        amc_name.add("----- Select Service Type -----");
                        amc_id.add("0");


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(jsonArray.length()>0){
                            for(int i=0;i<jsonArray.length();i++){
                                amc_name.add(jsonArray.getJSONObject(i).getString("value"));
                                amc_id.add(jsonArray.getJSONObject(i).getString("id"));
                            }

                            ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.produt_spinner,amc_name);
                            amc_type.setAdapter(adapter);
                        }


                    } else {
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(),UserDashBoard.class);
                        startActivity(intent);
                        getActivity().finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.naviagteToLoginPage(getActivity());
                }
            } else {
                CommonUtility.naviagteToLoginPage(getActivity());
            }

        }

        if (requestData == REGISTER_AMC_COMPLAIN_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        name.setText("");
                        mobile.setText("");
                        amc_type.setSelection(0);
                        amc_date.setText("");
                        paymnet.setText("");
                        customer_address.setText("");

                        CommonUtility.showMessage(getActivity(), "", "AMC registered successfully.");

                    } else {
                        CommonUtility.showMessage(getActivity(), "", message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.naviagteToLoginPage(getActivity());
                }
            } else {
                CommonUtility.naviagteToLoginPage(getActivity());
            }

        }

    }
}