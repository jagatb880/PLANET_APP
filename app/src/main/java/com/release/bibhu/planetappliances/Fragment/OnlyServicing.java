package com.release.bibhu.planetappliances.Fragment;

/**
 * Created by MUVI on 03-Feb-18.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Adaptor.EnqueryListAdapter;
import com.release.bibhu.planetappliances.Model.Complain;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ENQUERY_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.ENQUERY_LIST_REQUEST;


public class OnlyServicing extends Fragment implements ConnectivityInterface.ApiInterafce {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    FloatingActionButton add_customer;
    ListView listview;
    TextView no_data;
    ArrayList<Complain> complains_list = new ArrayList<>();
    EnqueryListAdapter enqueryAdapter;



    public OnlyServicing() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.only_servicing, container, false);

        no_data = (TextView) rootView.findViewById(R.id.no_data);
        listview = (ListView) rootView.findViewById(R.id.listview);
        add_customer = (FloatingActionButton) rootView.findViewById(R.id.add_customer);
        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());
        add_customer.setVisibility(View.GONE);

        Toast.makeText(getActivity(),"tab1",Toast.LENGTH_SHORT).show();

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
    public void getEnqueryList(){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("token",prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN,PrefferenceManager.DEFAULT_LOGIN_TOKEN));
        inputHashMap.put("user_id",prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID,PrefferenceManager.DEFAULT_USER_ID));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ENQUERY_LIST,inputHashMap,ENQUERY_LIST_REQUEST,this,ENQUERY_LIST_BASE_URL);
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
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }

        if(requestData == ENQUERY_LIST_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){



                    JSONArray  jsonArray = jsonObject.optJSONArray("data");
                    if(jsonArray.length()>0){

                        for(int i=0;i<jsonArray.length();i++){
                            Complain complain= new Complain();
                            complain.setMobile(jsonArray.optJSONObject(i).optString("mobile").trim());
                            complain.setCustomer_name(jsonArray.optJSONObject(i).optString("name").trim());
                            complain.setAddress(jsonArray.optJSONObject(i).optString("address").trim());
                            complain.setModel(jsonArray.optJSONObject(i).optString("is_positive").trim());

                            try {
                                complain.setProduct_name((jsonArray.optJSONObject(i)).optJSONObject("product").optString("name"));

                            }catch (Exception e){}

                            complains_list.add(complain);

                        }

                        enqueryAdapter = new EnqueryListAdapter(getActivity(),complains_list);
                        listview.setAdapter(enqueryAdapter);

                    }else {
                        no_data.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);

                    }
                    }
                    else{
                        no_data.setText(message);
                        no_data.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    no_data.setText("Something went wrong,please try again.");
                    no_data.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
            }else {
                no_data.setText("Something went wrong,please try again.");
                no_data.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);            }

        }

    }
}