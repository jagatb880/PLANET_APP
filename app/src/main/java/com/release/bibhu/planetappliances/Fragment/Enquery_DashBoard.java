package com.release.bibhu.planetappliances.Fragment;

/**
 * Created by MUVI on 03-Feb-18.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Activity.AddCustomer;
import com.release.bibhu.planetappliances.Activity.ComplaintDetails;
import com.release.bibhu.planetappliances.Adaptor.AssignedComplainCustomAdapter;
import com.release.bibhu.planetappliances.Adaptor.EnqueryAdapter;
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
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_DASHBOARD;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_DASHBOARD_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.ASSIGNED_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ENQUERY_DASHBOARD_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ENQUERY_LIST_REQUEST;


public class Enquery_DashBoard extends Fragment implements ConnectivityInterface.ApiInterafce {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    FloatingActionButton add_customer;
    ListView listview;
    TextView no_data;
    ArrayList<com.release.bibhu.planetappliances.Model.Complain> complains_list = new ArrayList<>();
    LinearLayout high_complain, low_complain, medium_complain;
    EnqueryAdapter enqueryAdapter;


    public Enquery_DashBoard() {
        // Required empty public constructor
    }

    TextView complain, normal_complain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.enquery_dashboard, container, false);

        medium_complain = (LinearLayout) rootView.findViewById(R.id.medium_complain);
        low_complain = (LinearLayout) rootView.findViewById(R.id.low_complain);
        complain = (TextView) rootView.findViewById(R.id.complain);
        no_data = (TextView) rootView.findViewById(R.id.no_data);
        normal_complain = (TextView) rootView.findViewById(R.id.normal_complain);
        listview = (ListView) rootView.findViewById(R.id.listview);
        add_customer = (FloatingActionButton) rootView.findViewById(R.id.add_customer);
        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());

        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCustomer.class);
                startActivity(intent);
            }
        });

        getEnqueryCount(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));


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
        if (Util.loadHomePage) {
            getEnqueryCount(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));
            Util.loadHomePage = false;
        }

        getActivity().registerReceiver(Complain, new IntentFilter("Complain_Recievre"));
    }

    BroadcastReceiver Complain = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getEnqueryCount(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

        }
    };

    /**
     * This API is responsible to get assigned Product against an agent .
     *
     * @param token
     */
    public void getEnqueryCount(String token) {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", token);
        inputHashMap.put("agent_specific", "1");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ENQUERY_DASHBOARD, inputHashMap, ENQUERY_DASHBOARD_REQUEST, this, ENQUERY_DASHBOARD_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    /**
     * This API is responsible to get assigned Product against an agent .
     */
    public void getEnqueryList() {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));
        inputHashMap.put("user_id", prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID, PrefferenceManager.DEFAULT_USER_ID));
        inputHashMap.put("all_enquery", "0");

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ENQUERY_LIST, inputHashMap, ENQUERY_LIST_REQUEST, this, ENQUERY_LIST_BASE_URL);
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

        if (requestData == ENQUERY_LIST_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {


                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray.length() > 0) {

                            Complain complain1 = new Complain();
                            complain1.setMobile("Mobile");
                            complain1.setCustomer_name("Cusomer Name");
                            complain1.setAddress("Enquery ID");
                            complains_list.add(complain1);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                Complain complain = new Complain();
                                complain.setMobile(jsonArray.optJSONObject(i).optString("mobile").trim());
                                complain.setCustomer_name(jsonArray.optJSONObject(i).optString("name").trim());
                                complain.setAddress(jsonArray.optJSONObject(i).optString("enquiryid").trim());
                                complain.setModel(jsonArray.optJSONObject(i).optString("is_positive").trim());

                                try {
                                    complain.setProduct_name((jsonArray.optJSONObject(i)).optJSONObject("product").optString("name"));

                                } catch (Exception e) {
                                }

                                complains_list.add(complain);

                            }

                            enqueryAdapter = new EnqueryAdapter(getActivity(), complains_list);
                            listview.setAdapter(enqueryAdapter);

                        } else {
                            no_data.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
                        }
                    } else {
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

        if (requestData == ENQUERY_DASHBOARD_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        normal_complain.setText(jsonObject1.optString("today_positive_data").toString().trim());
                        complain.setText(jsonObject1.optString("today_data").toString().trim());

                        getEnqueryList();
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