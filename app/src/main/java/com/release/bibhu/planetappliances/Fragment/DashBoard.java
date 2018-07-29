package com.release.bibhu.planetappliances.Fragment;

/**
 * Created by MUVI on 03-Feb-18.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Activity.AMCLIST;
import com.release.bibhu.planetappliances.Activity.AddCustomer;
import com.release.bibhu.planetappliances.Activity.ComplaintDetails;
import com.release.bibhu.planetappliances.Activity.LoginActivity;
import com.release.bibhu.planetappliances.Adaptor.AssignedComplainCustomAdapter;
import com.release.bibhu.planetappliances.Model.Complain;
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

import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGNED_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGNED_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGNED_COMPLAIN_WITHOUT_BILLNO;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGNED_COMPLAIN_WITHOUT_BILLNO_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.COMPLIAN_COUNT;
import static com.release.bibhu.planetappliances.Util.ApiConstants.COMPLIAN_COUNT_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.ASSIGNED_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ASSIGNED_COMPLAIN_WITHOUT_BILLNO_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.COMPLIAN_COUNT_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.INSTANT_COMPLIAN_COUNT_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ONLY_COMPLIAN_COUNT_REQUEST;
import static com.release.bibhu.planetappliances.Util.Util.ERROR_MSG;
import static com.release.bibhu.planetappliances.Util.Util.amc_list_of_a_day;


public class DashBoard extends Fragment implements ConnectivityInterface.ApiInterafce {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    FloatingActionButton add_customer;
    ListView listview;
    TextView no_data;
    ArrayList<com.release.bibhu.planetappliances.Model.Complain> complains_list = new ArrayList<>();
    ArrayList<com.release.bibhu.planetappliances.Model.Complain> LOW = new ArrayList<>();
    ArrayList<com.release.bibhu.planetappliances.Model.Complain> HIGH = new ArrayList<>();
    ArrayList<com.release.bibhu.planetappliances.Model.Complain> MEDIUM = new ArrayList<>();

    AssignedComplainCustomAdapter assignedComplainCustomAdapter;
    boolean showNodata = true;

    int low_counter = 0;
    int medium_counter = 0;
    int high_counter = 0;


    public DashBoard() {
        // Required empty public constructor
    }

    TextView complain, complain_solved, complain_pending;
    LinearLayout complain_layout, complain_solved_layout, complain_pending_layout;
    TextView complain_with_bill_no, complain_without_bill_no, complain_amc;
    LinearLayout complain_with_bill_no_layout, complain_without_bill_no_layout, complain_amc_layout;

    TextView complain_type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard, container, false);

        complain_with_bill_no_layout = (LinearLayout) rootView.findViewById(R.id.complain_with_bill_no_layout);
        complain_without_bill_no_layout = (LinearLayout) rootView.findViewById(R.id.complain_without_bill_no_layout);
        complain_amc_layout = (LinearLayout) rootView.findViewById(R.id.complain_amc_layout);

        complain_layout = (LinearLayout) rootView.findViewById(R.id.complain_layout);
        complain_solved_layout = (LinearLayout) rootView.findViewById(R.id.complain_solved_layout);
        complain_pending_layout = (LinearLayout) rootView.findViewById(R.id.complain_pending_layout);

        no_data = (TextView) rootView.findViewById(R.id.no_data);
        complain_type = (TextView) rootView.findViewById(R.id.complain_type);

        complain = (TextView) rootView.findViewById(R.id.complain);
        complain_solved = (TextView) rootView.findViewById(R.id.complain_solved);
        complain_pending = (TextView) rootView.findViewById(R.id.complain_pending);

        complain_with_bill_no = (TextView) rootView.findViewById(R.id.complain_with_bill_no);
        complain_without_bill_no = (TextView) rootView.findViewById(R.id.complain_without_bill_no);
        complain_amc = (TextView) rootView.findViewById(R.id.complain_amc);

        listview = (ListView) rootView.findViewById(R.id.listview);
        add_customer = (FloatingActionButton) rootView.findViewById(R.id.add_customer);
        add_customer.setVisibility(View.GONE);
        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());


        if(Util.notification_clicked == 1){
            getOnlyComplainCountInstant(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));
            Util.notification_clicked = 0;
        }else {
            getComplainCount(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    return;
                }

                Intent intent = new Intent(getActivity(), ComplaintDetails.class);
                intent.putExtra("complain_info", complains_list.get(i));
                intent.putExtra("hide_data", "no");
                startActivity(intent);
            }
        });

        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCustomer.class);
                startActivity(intent);
            }
        });

/*        low_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complains_list = LOW;
                assignedComplainCustomAdapter = new AssignedComplainCustomAdapter(getActivity(),complains_list);
                listview.setAdapter(assignedComplainCustomAdapter);
                assignedComplainCustomAdapter.notifyDataSetChanged();
            }
        });

        high_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complains_list = HIGH;
                assignedComplainCustomAdapter = new AssignedComplainCustomAdapter(getActivity(),complains_list);
                listview.setAdapter(assignedComplainCustomAdapter);
                assignedComplainCustomAdapter.notifyDataSetChanged();
            }
        });

        medium_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complains_list = MEDIUM;
                assignedComplainCustomAdapter = new AssignedComplainCustomAdapter(getActivity(),complains_list);
                listview.setAdapter(assignedComplainCustomAdapter);
                assignedComplainCustomAdapter.notifyDataSetChanged();
            }
        });*/


        complain_with_bill_no_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complain_with_bill_no_layout.setBackgroundColor(Color.parseColor("#1c7768"));
                complain_without_bill_no_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));
                complain_amc_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));

//                complain_type.setText("WIth Bill No");
                complain_type.setText("Complaint");
                getComplainWithBillNo(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

            }
        });
        complain_without_bill_no_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                complain_with_bill_no_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));
                complain_without_bill_no_layout.setBackgroundColor(Color.parseColor("#1c7768"));
                complain_amc_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));

                complain_type.setText("WIthout Bill No");
                getComplainWithOutBillNo(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

            }
        });
        complain_amc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                complain_with_bill_no_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));
                complain_without_bill_no_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));
                complain_amc_layout.setBackgroundColor(Color.parseColor("#1c7768"));

                amc_list_of_a_day = true;
                Intent intent = new Intent(getActivity(), AMCLIST.class);
                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
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
        getActivity().unregisterReceiver(Complain);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isLoadHomePage_new) {
            getOnlyComplainCount(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

        }

        if (Util.isLoadHomePage_old) {
            getOnlyComplainCount(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

        }

        getActivity().registerReceiver(Complain, new IntentFilter("Complain_Recievre"));
    }

    BroadcastReceiver Complain = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            getOnlyComplainCountInstant(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));



        }
    };


    /**
     * This API is responsible to get assigned Product against an agent .
     *
     * @param token
     */
    public void getComplainCount(String token) {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", token);
        inputHashMap.put("all_complain_count", "0");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(COMPLIAN_COUNT, inputHashMap, COMPLIAN_COUNT_REQUEST, this, COMPLIAN_COUNT_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * This API is responsible to get assigned Product against an agent .
     *
     * @param token
     */
    public void getOnlyComplainCount(String token) {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", token);
        inputHashMap.put("all_complain_count", "0");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(COMPLIAN_COUNT, inputHashMap, ONLY_COMPLIAN_COUNT_REQUEST, this, COMPLIAN_COUNT_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * This API is responsible to get assigned Product against an agent .
     *
     * @param token
     */
    public void getOnlyComplainCountInstant(String token) {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", token);
        inputHashMap.put("all_complain_count", "0");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(COMPLIAN_COUNT, inputHashMap, INSTANT_COMPLIAN_COUNT_REQUEST, this, COMPLIAN_COUNT_BASE_URL);
        connectivityInterface.startApiProcessing();
    }



    /**
     * This API is responsible to get assigned Product against an agent .
     *
     * @param token
     */
    public void getComplainWithBillNo(String token) {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", token);
        inputHashMap.put("all_complain", "0");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ASSIGNED_COMPLAIN, inputHashMap, ASSIGNED_COMPLAIN_REQUEST, this, ASSIGNED_COMPLAIN_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    /**
     * This API is responsible to get assigned Product against an agent .
     *
     * @param token
     */
    public void getComplainWithOutBillNo(String token) {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", token);
        inputHashMap.put("all_complain", "0");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ASSIGNED_COMPLAIN_WITHOUT_BILLNO, inputHashMap, ASSIGNED_COMPLAIN_WITHOUT_BILLNO_REQUEST, this, ASSIGNED_COMPLAIN_WITHOUT_BILLNO_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * This method will handle preExecute processing.
     */
    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(getActivity());
        progressHandler.show();

        no_data.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
    }


    /**
     * This method will handle postExecute processing.
     */
    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }



        if (requestData == INSTANT_COMPLIAN_COUNT_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        complain.setText(jsonObject1.optString("total").toString().trim());
                        complain_pending.setText(jsonObject1.optString("assigned").toString().trim());
                        complain_solved.setText(jsonObject1.optString("fixed").toString().trim());
                        complain_with_bill_no.setText(jsonObject1.optString("totalComplainWithBill").toString().trim());
                        complain_without_bill_no.setText(jsonObject1.optString("totalComplainWithOutBill").toString().trim());
                        complain_amc.setText(jsonObject1.optString("totalAmc").toString().trim());

                        if(Util.complain_type ==1){
                            complain_with_bill_no_layout.setBackgroundColor(Color.parseColor("#1c7768"));
                            complain_without_bill_no_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));
                            complain_amc_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));

                            complain_type.setText("Complaint");
                            getComplainWithBillNo(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

                        }

                        if(Util.complain_type ==2){

                            complain_with_bill_no_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));
                            complain_without_bill_no_layout.setBackgroundColor(Color.parseColor("#1c7768"));
                            complain_amc_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));

                            complain_type.setText("WIthout Bill No");
                            getComplainWithOutBillNo(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

                        }

                        if(Util.complain_type ==3){

                            complain_with_bill_no_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));
                            complain_without_bill_no_layout.setBackgroundColor(Color.parseColor("#d4d4d4"));
                            complain_amc_layout.setBackgroundColor(Color.parseColor("#1c7768"));

                            Intent intent1 = new Intent(getActivity(), AMCLIST.class);
                            startActivity(intent1);
                        }
                    } else {
                        no_data.setText(message);
                        no_data.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    CommonUtility.naviagteToLoginPage(getActivity());
                }
            } else {
                CommonUtility.naviagteToLoginPage(getActivity());
            }

        }



        if (requestData == ONLY_COMPLIAN_COUNT_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        complain.setText(jsonObject1.optString("total").toString().trim());
                        complain_pending.setText(jsonObject1.optString("assigned").toString().trim());
                        complain_solved.setText(jsonObject1.optString("fixed").toString().trim());
                        complain_with_bill_no.setText(jsonObject1.optString("totalComplainWithBill").toString().trim());
                        complain_without_bill_no.setText(jsonObject1.optString("totalComplainWithOutBill").toString().trim());
                        complain_amc.setText(jsonObject1.optString("totalAmc").toString().trim());


                        if (Util.isLoadHomePage_new) {
                            getComplainWithBillNo(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));
                            Util.isLoadHomePage_new = false;
                        }

                        if (Util.isLoadHomePage_old) {
                            getComplainWithOutBillNo(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));
                            Util.isLoadHomePage_old = false;
                        }




                    } else {
                        no_data.setText(message);
                        no_data.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    CommonUtility.naviagteToLoginPage(getActivity());
                }
            } else {
                CommonUtility.naviagteToLoginPage(getActivity());
            }

        }

        if (requestData == COMPLIAN_COUNT_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        complain.setText(jsonObject1.optString("total").toString().trim());
                        complain_pending.setText(jsonObject1.optString("assigned").toString().trim());
                        complain_solved.setText(jsonObject1.optString("fixed").toString().trim());
                        complain_with_bill_no.setText(jsonObject1.optString("totalComplainWithBill").toString().trim());
                        complain_without_bill_no.setText(jsonObject1.optString("totalComplainWithOutBill").toString().trim());
                        complain_amc.setText(jsonObject1.optString("totalAmc").toString().trim());

                        getComplainWithBillNo(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

                    } else {
                        no_data.setText(message);
                        no_data.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    CommonUtility.naviagteToLoginPage(getActivity());
                }
            } else {
                CommonUtility.naviagteToLoginPage(getActivity());
            }

        }

        if (requestData == ASSIGNED_COMPLAIN_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {
                        complains_list.clear();
                        LOW.clear();
                        MEDIUM.clear();
                        HIGH.clear();


                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray.length() > 0) {

                            Complain complain1 = new Complain();
                            complain1.setIssue("Issue");
                            complain1.setComplainId("Complain ID");
                            complain1.setComplainType(1);
                            complains_list.add(complain1);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                if (!jsonArray.optJSONObject(i).optString("flag").trim().equals("1")) {
                                    Complain complain = new Complain();
                                    complain.setIssue(jsonArray.optJSONObject(i).optString("issue").trim());
                                    complain.setId(jsonArray.optJSONObject(i).optString("id").trim());
                                    complain.setPayment(jsonArray.optJSONObject(i).optString("payment").trim());
                                    complain.setComplainId(jsonArray.optJSONObject(i).optString("complainid").trim());
                                    complain.setService_type(jsonArray.optJSONObject(i).optString("service_type").trim());
                                    complain.setStatus(jsonArray.optJSONObject(i).optString("status").trim());

                                    complain.setComplainType(1);


                                    try {
                                        complain.setProduct_name((jsonArray.optJSONObject(i)).optJSONObject("product").optString("name"));
                                    }catch (Exception e){
                                        complain.setProduct_name("");
                                    }
                                    try {
                                        complain.setMake((jsonArray.optJSONObject(i)).optJSONObject("product").optString("makeproduct"));
                                    }catch (Exception e){
                                        complain.setMake("");
                                    }
                                    try {
                                        complain.setModel((jsonArray.optJSONObject(i)).optJSONObject("product").optString("model"));
                                    }catch (Exception e){
                                        complain.setModel("");
                                    }
                                    try {
                                        complain.setMobile((jsonArray.optJSONObject(i)).optJSONObject("customer").optString("phone"));
                                        complain.setCustomer_name((jsonArray.optJSONObject(i)).optJSONObject("customer").optString("name"));
                                        complain.setAddress((jsonArray.optJSONObject(i)).optJSONObject("customer").optString("address"));

                                    } catch (Exception e) {
                                    }



//                                    if (!jsonArray.optJSONObject(i).optString("status").trim().equals("fixed")) {

                                        showNodata = false;

                                        if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("Low")) {
                                            low_counter = low_counter + 1;
                                            complain.setPriority("Low");
                                            LOW.add(complain);
                                        } else if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("Medium")) {
                                            medium_counter = medium_counter + 1;
                                            complain.setPriority("Medium");
                                            MEDIUM.add(complain);
                                        } else if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("High")) {
                                            high_counter = high_counter + 1;
                                            complain.setPriority("High");
                                            HIGH.add(complain);
                                        } else {
                                            low_counter = low_counter + 1;
                                            complain.setPriority("Low");
                                            LOW.add(complain);
                                        }

                                        complains_list.add(complain);
//                                    }
                                }

                            }

                            assignedComplainCustomAdapter = new AssignedComplainCustomAdapter(getActivity(), complains_list);
                            listview.setAdapter(assignedComplainCustomAdapter);
                            if (showNodata) {
                                no_data.setVisibility(View.VISIBLE);
                                listview.setVisibility(View.GONE);
                            }
                            showNodata = true;

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
                    CommonUtility.naviagteToLoginPage(getActivity());
                }
            } else {
                CommonUtility.naviagteToLoginPage(getActivity());
            }

        }


        //*************************************************************************************************//


        if (requestData == ASSIGNED_COMPLAIN_WITHOUT_BILLNO_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {
                        complains_list.clear();
                        LOW.clear();
                        MEDIUM.clear();
                        HIGH.clear();

                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray.length() > 0) {

                            Complain complain1 = new Complain();
                            complain1.setIssue("Issue");
                            complain1.setComplainId("Complain ID");
                            complain1.setComplainType(0);
                            complains_list.add(complain1);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                if (!jsonArray.optJSONObject(i).optString("flag").trim().equals("1")) {
                                    Complain complain = new Complain();
                                    complain.setIssue(jsonArray.optJSONObject(i).optString("issue").trim());
                                    complain.setId(jsonArray.optJSONObject(i).optString("id").trim());
                                    complain.setPayment(jsonArray.optJSONObject(i).optString("payment").trim());
                                    complain.setComplainId(jsonArray.optJSONObject(i).optString("complainid").trim());
                                    complain.setService_type(jsonArray.optJSONObject(i).optString("service_type").trim());
                                    complain.setStatus(jsonArray.optJSONObject(i).optString("status").trim());

                                    complain.setComplainType(0);

                                    try {
                                        complain.setProduct_name((jsonArray.optJSONObject(i)).optJSONObject("product").optString("name"));
                                    }catch (Exception e){
                                        complain.setProduct_name("");
                                    }
                                    try {
                                        complain.setMake((jsonArray.optJSONObject(i)).optJSONObject("product").optString("makeproduct"));
                                    }catch (Exception e){
                                        complain.setMake("");
                                    }
                                    try {
                                        complain.setModel((jsonArray.optJSONObject(i)).optJSONObject("product").optString("model"));
                                    }catch (Exception e){
                                        complain.setModel("");
                                    }

                                    try {
                                        complain.setMobile(jsonArray.optJSONObject(i).optString("mobile").trim());
                                        complain.setCustomer_name(jsonArray.optJSONObject(i).optString("name").trim());
                                        complain.setAddress(jsonArray.optJSONObject(i).optString("address").trim());

                                    } catch (Exception e) {
                                    }




                                        showNodata = false;

                                        if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("Low")) {
                                            low_counter = low_counter + 1;
                                            complain.setPriority("Low");
                                            LOW.add(complain);
                                        } else if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("Medium")) {
                                            medium_counter = medium_counter + 1;
                                            complain.setPriority("Medium");
                                            MEDIUM.add(complain);
                                        } else if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("High")) {
                                            high_counter = high_counter + 1;
                                            complain.setPriority("High");
                                            HIGH.add(complain);
                                        } else {
                                            low_counter = low_counter + 1;
                                            complain.setPriority("Low");
                                            LOW.add(complain);
                                        }

                                        complains_list.add(complain);
                                    }

                            }

                            assignedComplainCustomAdapter = new AssignedComplainCustomAdapter(getActivity(), complains_list);
                            listview.setAdapter(assignedComplainCustomAdapter);

                            if (showNodata) {

                                no_data.setVisibility(View.VISIBLE);
                                listview.setVisibility(View.GONE);
                            }
                            showNodata = true;

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
                    CommonUtility.naviagteToLoginPage(getActivity());
                }
            } else {
                CommonUtility.naviagteToLoginPage(getActivity());
            }
        }
    }
}