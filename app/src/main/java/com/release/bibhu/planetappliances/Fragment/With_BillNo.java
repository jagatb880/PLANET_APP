package com.release.bibhu.planetappliances.Fragment;

/**
 * Created by MUVI on 03-Feb-18.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Activity.ComplaintDetails;
import com.release.bibhu.planetappliances.Adaptor.AssignedComplainCustomAdapter;
import com.release.bibhu.planetappliances.Adaptor.EnqueryListAdapter;
import com.release.bibhu.planetappliances.Model.Complain;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.release.bibhu.planetappliances.Util.ApiConstants.ADMIN_ASSIGNED_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ADMIN_ASSIGNED_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGNED_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGNED_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.ASSIGNED_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ENQUERY_LIST_REQUEST;


public class With_BillNo extends Fragment implements ConnectivityInterface.ApiInterafce {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    ListView listview;
    TextView no_data;
    ArrayList<Complain> complains_list = new ArrayList<>();
    private AssignedComplainCustomAdapter assignedComplainCustomAdapter;
    private boolean showNodata = true;


    public With_BillNo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.with_billno, container, false);

        no_data = (TextView) rootView.findViewById(R.id.no_data);
        listview = (ListView) rootView.findViewById(R.id.listview);
        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    return;
                }

                Intent intent = new Intent(getActivity(), ComplaintDetails.class);
                intent.putExtra("complain_info", complains_list.get(i));
                intent.putExtra("hide_data", "bill_no");
                startActivity(intent);
            }
        });

        WithBillNoComplainList();


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
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * This API is responsible to get assigned Product against an agent .
     */
    public void WithBillNoComplainList() {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ADMIN_ASSIGNED_COMPLAIN, inputHashMap, ASSIGNED_COMPLAIN_REQUEST, this, ADMIN_ASSIGNED_COMPLAIN_BASE_URL);
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
        if (requestData == ASSIGNED_COMPLAIN_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {
                        complains_list.clear();

                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray.length() > 0) {

                            Complain complain1 = new Complain();
                            complain1.setIssue("Issue");
                            complain1.setComplainId("Complain ID");
                            complain1.setComplainType(1);
                            complains_list.add(complain1);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                if(!jsonArray.optJSONObject(i).optString("flag").trim().equals("1")){
                                    showNodata = false;

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




                                    if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("Low")) {
                                        complain.setPriority("Low");
                                    } else if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("Medium")) {
                                        complain.setPriority("Medium");
                                    } else if (jsonArray.optJSONObject(i).optString("priority").trim().equalsIgnoreCase("High")) {
                                        complain.setPriority("High");
                                    } else {
                                        complain.setPriority("Low");
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
}