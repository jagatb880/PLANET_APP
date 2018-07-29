package com.release.bibhu.planetappliances.Fragment;

/**
 * Created by MUVI on 03-Feb-18.
 */
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Adaptor.*;
import com.release.bibhu.planetappliances.Model.MotorServiceModel;
import com.release.bibhu.planetappliances.Model.OnlyServiceDetailsModel;
import com.release.bibhu.planetappliances.Model.OnlyServiceModel;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static com.release.bibhu.planetappliances.Util.ApiConstants.MOTOR_SERVICING;
import static com.release.bibhu.planetappliances.Util.ApiConstants.MOTOR_SERVICING_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ONLY_SERVICING;
import static com.release.bibhu.planetappliances.Util.ApiConstants.ONLY_SERVICING_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.MOTOR_SERVICING_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.ONLY_SERVICING_REQUEST;


public class AmcList extends Fragment implements ConnectivityInterface.ApiInterafce {

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    LinearLayout motor_servicing, only_servicing;
    TextView no_data;
    ListView listview;

    ArrayList<OnlyServiceModel> onlyServiceModels = new ArrayList<>();
    ArrayList<MotorServiceModel> motorServiceModels = new ArrayList<>();

    public AmcList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.amc_list, container, false);
        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());

        motor_servicing = (LinearLayout) rootView.findViewById(R.id.motor_servicing);
        only_servicing = (LinearLayout) rootView.findViewById(R.id.only_servicing);
        no_data = (TextView) rootView.findViewById(R.id.no_data);
        listview = (ListView) rootView.findViewById(R.id.listview);

        only_servicing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                only_servicing.setBackgroundColor(Color.parseColor("#1c7768"));
                motor_servicing.setBackgroundColor(Color.parseColor("#d4d4d4"));

                getOnlyServicingList();
            }
        });

        motor_servicing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motor_servicing.setBackgroundColor(Color.parseColor("#1c7768"));
                only_servicing.setBackgroundColor(Color.parseColor("#d4d4d4"));

                getMotorServicingList();
            }
        });

        getOnlyServicingList();


        return rootView;
    }

    /**
     * Get only servicelist.
     */
    public void getOnlyServicingList() {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(ONLY_SERVICING, inputHashMap, ONLY_SERVICING_REQUEST, this, ONLY_SERVICING_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * Get Motor service list
     */
    public void getMotorServicingList() {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(MOTOR_SERVICING, inputHashMap, MOTOR_SERVICING_REQUEST, this, MOTOR_SERVICING_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(getActivity());
        progressHandler.show();

        onlyServiceModels.clear();
        motorServiceModels.clear();

        no_data.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }

        if (requestData == ONLY_SERVICING_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        OnlyServiceModel onlyServiceModel = new OnlyServiceModel();
                        onlyServiceModel.setName("Customer Name");
                        onlyServiceModel.setPhone("Phone");
                        onlyServiceModel.setAmc_number("AMC Number");
                        onlyServiceModels.add(onlyServiceModel);


                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (!(jsonArray.optJSONObject(i).optString("flag").trim().equals("1"))) {

                                    OnlyServiceModel onlyServiceModel1 = new OnlyServiceModel();
                                    onlyServiceModel1.setName(jsonArray.optJSONObject(i).optString("name").trim());
                                    onlyServiceModel1.setPhone(jsonArray.optJSONObject(i).optString("phone").trim());
                                    onlyServiceModel1.setAmc_number(jsonArray.optJSONObject(i).optString("amc_number").trim());
                                    onlyServiceModel1.setAmc_type(jsonArray.optJSONObject(i).optString("amc_type").trim());
                                    onlyServiceModel1.setId(jsonArray.optJSONObject(i).optString("id").trim());
                                    onlyServiceModel1.setUser_id(jsonArray.optJSONObject(i).optString("user_id").trim());


                                    JSONArray jsonArray1 = jsonArray.optJSONObject(i).getJSONArray("amc_servicing");
                                    if (jsonArray1.length() > 0) {

                                        String details = "";


                                        ArrayList<OnlyServiceDetailsModel> onlyServiceDetailsModel_arraylist = new ArrayList<>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {

                                            String date = jsonArray1.optJSONObject(j).optString("date");
                                            String id = jsonArray1.optJSONObject(j).optString("id");
                                            String service_no = jsonArray1.optJSONObject(j).optString("service_no");

                                            details = details + "Service Date : " + convertDate(date) + "\n" + "Service No : " + service_no  + "\n\n";

                                            OnlyServiceDetailsModel onlyServiceDetailsModel = new OnlyServiceDetailsModel();
                                            onlyServiceDetailsModel.setService_date(convertDate(date));
                                            onlyServiceDetailsModel.setService_no(service_no);
                                            onlyServiceDetailsModel.setService_id(id);
                                            onlyServiceDetailsModel_arraylist.add(onlyServiceDetailsModel);

                                        }
                                        onlyServiceModel1.setService_list_details(details);
                                        onlyServiceModel1.setOnlyServiceDetailsModels(onlyServiceDetailsModel_arraylist);


                                    } else {
                                        onlyServiceModel1.setService_list_details("");
                                    }

                                    onlyServiceModels.add(onlyServiceModel1);

                                }
                            }

                            OnlyServicingAdapter onlyServicingAdapter = new OnlyServicingAdapter(getActivity(), onlyServiceModels);
                            listview.setAdapter(onlyServicingAdapter);

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


        if (requestData == MOTOR_SERVICING_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        MotorServiceModel motorServiceModel = new MotorServiceModel();
                        motorServiceModel.setName("Customer Name");
                        motorServiceModel.setPhone("Phone");
                        motorServiceModel.setAmc_number("AMC Number");
                        motorServiceModels.add(motorServiceModel);


                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                if (!(jsonArray.optJSONObject(i).optString("flag").trim().equals("1"))) {

                                    MotorServiceModel motorServiceModel1 = new MotorServiceModel();
                                    motorServiceModel1.setName(jsonArray.optJSONObject(i).optString("name").trim());
                                    motorServiceModel1.setPhone(jsonArray.optJSONObject(i).optString("phone").trim());
                                    motorServiceModel1.setAmc_number(jsonArray.optJSONObject(i).optString("amc_number").trim());
                                    motorServiceModel1.setAmc_type(jsonArray.optJSONObject(i).optString("amc_type").trim());
                                    motorServiceModel1.setId(jsonArray.optJSONObject(i).optString("id").trim());
                                    motorServiceModel1.setUser_id(jsonArray.optJSONObject(i).optString("user_id").trim());
                                    motorServiceModel1.setAddress(jsonArray.optJSONObject(i).optString("address").trim());
                                    motorServiceModel1.setAmc_date(convertDate(jsonArray.optJSONObject(i).optString("amc_date").trim()));
                                    motorServiceModel1.setAmc_type(jsonArray.optJSONObject(i).optString("amc_type").trim());


                                    String details = "";
                                    details = details + "Phone : " + jsonArray.optJSONObject(i).optString("phone").trim() + "\n" + "Address : " + jsonArray.optJSONObject(i).optString("address").trim() + "\n"
                                            + "AMC Date : " + convertDate(jsonArray.optJSONObject(i).optString("amc_date").trim())  ;

                                    motorServiceModel1.setService_list_details(details.trim());
                                    motorServiceModels.add(motorServiceModel1);
                                }
                            }

                            MotorServicingAdapter motorServicingAdapter = new MotorServicingAdapter(getActivity(), motorServiceModels);
                            listview.setAdapter(motorServicingAdapter);

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

    String convertDate(String inputDate) {

        DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
        } catch (Exception parseException) {
            // Date is invalid. Do what you want.
        }
            theDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            return theDateFormat.format(date);
        }

    }

