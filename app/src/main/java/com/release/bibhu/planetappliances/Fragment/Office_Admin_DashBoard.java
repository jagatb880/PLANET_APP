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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Activity.AddCustomer;
import com.release.bibhu.planetappliances.Adaptor.AssignedComplainCustomAdapter;
import com.release.bibhu.planetappliances.Adaptor.EnqueryAdapter;
import com.release.bibhu.planetappliances.Model.Complain;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;
import com.release.bibhu.planetappliances.Util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.release.bibhu.planetappliances.Util.ApiConstants.ADMIN_DASHBOARD;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ADMIN_DASHBOARD_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_DASHBOARD;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_DASHBOARD_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.ADMIN_DASHBOARD_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ENQUERY_DASHBOARD_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ENQUERY_LIST_REQUEST;


public class Office_Admin_DashBoard extends Fragment implements ConnectivityInterface.ApiInterafce {

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


    public Office_Admin_DashBoard() {
        // Required empty public constructor
    }

    TextView complain, complain_solved, complain_pending,complain_prority;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_dashboard, container, false);


        complain = (TextView) rootView.findViewById(R.id.complain);
        complain_solved = (TextView) rootView.findViewById(R.id.complain_solved);
        complain_pending = (TextView) rootView.findViewById(R.id.complain_pending);
        complain_prority = (TextView) rootView.findViewById(R.id.complain_prority);

        add_customer = (FloatingActionButton) rootView.findViewById(R.id.add_customer);
        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());

        getComplainCount(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));


        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCustomer.class);
                startActivity(intent);
            }
        });

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

        getActivity().registerReceiver(Complain, new IntentFilter("Complain_Recievre"));
    }

    BroadcastReceiver Complain = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

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

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ADMIN_DASHBOARD, inputHashMap, ADMIN_DASHBOARD_REQUEST, this, ADMIN_DASHBOARD_BASE_URL);
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

        if (requestData == ADMIN_DASHBOARD_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("complain");

                        complain.setText(jsonObject2.optString("total"));
                        complain_pending.setText(jsonObject2.optString("assigned"));
                        complain_solved.setText(jsonObject2.optString("fixed"));
                        complain_prority.setText(jsonObject2.optString("high"));

                    } else {

                    }
                } catch (Exception e) {

                }
            } else {

            }

        }

    }
}