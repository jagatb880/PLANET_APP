package com.release.bibhu.planetappliances.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Activity.ComplaintDetails;
import com.release.bibhu.planetappliances.Adaptor.CustomAdapter;
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

import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGNED_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ASSIGNED_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.ASSIGNED_COMPLAIN_REQUEST;
import static com.release.bibhu.planetappliances.Util.Util.page_navigation_form_task_assigned_page;

/**
 * Created by Bibhu on 03-Feb-18.
 */


public class TaskAssigned extends Fragment implements ConnectivityInterface.ApiInterafce {

    public TaskAssigned() {
        // Required empty public constructor
    }


    Toolbar mToolbar;
    PrefferenceManager prefferenceManager;
    ListView listView;

    ArrayList<String> Product = new ArrayList<>();
    ArrayList<String> Complain_Info = new ArrayList<>();;
    ArrayAdapter adapter;
    ProgressHandler progressHandler;
    ArrayList<com.release.bibhu.planetappliances.Model.Complain> complains_list = new ArrayList<>();
    TextView no_data;

    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(View view){

        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());
        listView = (ListView) view.findViewById(R.id.listview);
        no_data = (TextView) view.findViewById(R.id.no_data);


        adapter  = new CustomAdapter(getActivity(), Product, Complain_Info,complains_list);
        listView.setAdapter(adapter);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_assigned, container, false);
        _init(rootView);
        getAssignedComplain(prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN,PrefferenceManager.DEFAULT_LOGIN_TOKEN));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                page_navigation_form_task_assigned_page = true;
                Intent intent = new Intent(getActivity(), ComplaintDetails.class);
                intent.putExtra("complain_info",complains_list.get(i));
                intent.putExtra("hide_data","yes");
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

    /**
     * This API is responsible to get assigned Product against an agent .
     * @param token
     */
    public void getAssignedComplain(String token){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("token",token);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ASSIGNED_COMPLAIN,inputHashMap,ASSIGNED_COMPLAIN_REQUEST,this,ASSIGNED_COMPLAIN_BASE_URL);
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
        listView.setVisibility(View.VISIBLE);
    }


    /**
     * This method will handle postExecute processing.
     */
    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }

        if(requestData == ASSIGNED_COMPLAIN_REQUEST){

            if(response!=null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {
                    } else {
                        CommonUtility.showMessage(getActivity(), "Alert", message);
                        return;
                    }
                    complains_list.clear();
                    Product.clear();
                    Complain_Info.clear();


                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Complain complain = new Complain();



                                complain.setIssue(jsonArray.optJSONObject(i).optString("issue").trim());
                                complain.setId(jsonArray.optJSONObject(i).optString("id").trim());
                                complain.setPayment(jsonArray.optJSONObject(i).optString("payment").trim());
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
                                    Product.add((jsonArray.optJSONObject(i)).optJSONObject("product").optString("name"));
                                }catch (Exception e){
                                    Product.add("");
                                }

                                try {
                                    complain.setMobile((jsonArray.optJSONObject(i)).optJSONObject("customer").optString("phone"));
                                    complain.setCustomer_name((jsonArray.optJSONObject(i)).optJSONObject("customer").optString("name"));
                                    complain.setAddress((jsonArray.optJSONObject(i)).optJSONObject("customer").optString("address"));

                                }catch (Exception e){}



                                complains_list.add(complain);
                                Complain_Info.add(jsonArray.optJSONObject(i).optString("issue").trim());


                        }

                        adapter  = new CustomAdapter(getActivity(), Product, Complain_Info,complains_list);
                        listView.setAdapter(adapter);

                }else{
                    no_data.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }


                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(getActivity(),"Alert","Something went wrong,please try again.");
                }
            }else {
                CommonUtility.showMessage(getActivity(),"Alert","Something went wrong,please try again.");
            }

        }

    }
}