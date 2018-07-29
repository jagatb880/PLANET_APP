package com.release.bibhu.planetappliances.Adaptor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Activity.AgentOnlyServicngDetailsActivity;
import com.release.bibhu.planetappliances.Activity.OnlyServicngDetailsActivity;
import com.release.bibhu.planetappliances.Activity.UserDashBoard;
import com.release.bibhu.planetappliances.Fragment.Enquery_DashBoard;
import com.release.bibhu.planetappliances.Model.Complain;
import com.release.bibhu.planetappliances.Model.OnlyServiceModel;
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

import static com.release.bibhu.planetappliances.Activity.ComplaintDetails.SELECT_AGENT;
import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.AGENT_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_AMC_COMPLAIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.REGISTER_AMC_COMPLAIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.AGENT_LIST_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.REGISTER_AMC_COMPLAIN_REQUEST;

public class OnlyServicingAdapter extends ArrayAdapter<String> implements ConnectivityInterface.ApiInterafce {

    private final Activity context;
    ArrayList<OnlyServiceModel> onlyServiceModels = new ArrayList<>();
    TextView name, amc_no;
    ImageView view_data, assign;
    PrefferenceManager prefferenceManager;
    ProgressHandler progressHandler;
    ArrayList<String> agent_name = new ArrayList<>();
    ArrayList<String> agentId = new ArrayList<>();

    Spinner agent;
    Button assign_task;
    Dialog dialog;
    String AGENT_ID = "";
    String amc_id = "";

    public OnlyServicingAdapter(Activity context, ArrayList<OnlyServiceModel> onlyServiceModels) {
        super(context, R.layout.onlyservicing_adapter);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.onlyServiceModels = onlyServiceModels;
        prefferenceManager = PrefferenceManager.getFeaturePreference(context);


    }

    @Override
    public int getCount() {
        return onlyServiceModels.size();
    }

    public View getView(final int position, View view, ViewGroup parent) {


        final LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.onlyservicing_adapter, null, true);

        name = (TextView) rowView.findViewById(R.id.name);
        amc_no = (TextView) rowView.findViewById(R.id.amc_no);
        view_data = (ImageView) rowView.findViewById(R.id.view_data);
        assign = (ImageView) rowView.findViewById(R.id.assign);

        name.setText(onlyServiceModels.get(position).getName());
        amc_no.setText(onlyServiceModels.get(position).getAmc_number());

        if (position == 0) {
            name.setTypeface(null, Typeface.BOLD);
            amc_no.setTypeface(null, Typeface.BOLD);

            name.setTextSize(16);
            amc_no.setTextSize(16);
            view_data.setVisibility(View.INVISIBLE);
            assign.setVisibility(View.INVISIBLE);
        } else {
            name.setTextSize(14);
            amc_no.setTextSize(14);
            view_data.setVisibility(View.VISIBLE);
            assign.setVisibility(View.VISIBLE);
        }

        if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("0") ||
                prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("2")) {
            view_data.setVisibility(View.INVISIBLE);
        }

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                amc_id = onlyServiceModels.get(position).getId();
//                getAgentList();

                if(!onlyServiceModels.get(position).getService_list_details().equals("")){

                    Intent intent = new Intent(context, OnlyServicngDetailsActivity.class);
                    intent.putExtra("details",onlyServiceModels.get(position).getOnlyServiceDetailsModels());

                    context.startActivity(intent);


                }else{
                    Toast.makeText(context,"No Service Available.",Toast.LENGTH_SHORT).show();
                }


            }
        });

        view_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onlyServiceModels.get(position).getService_list_details().equals("")){
                    if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("1") ) {
                        Intent intent = new Intent(context, AgentOnlyServicngDetailsActivity.class);
                        intent.putExtra("details",onlyServiceModels.get(position).getOnlyServiceDetailsModels());
                        intent.putExtra("name",onlyServiceModels.get(position).getName());
                        intent.putExtra("amc_no",onlyServiceModels.get(position).getAmc_number());
                        context.startActivity(intent);
                    }else{
                        CommonUtility.showMessage(context,"Service List",onlyServiceModels.get(position).getService_list_details());
                    }

                }else{
                    Toast.makeText(context,"No Service Available.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("1")) {
            assign.setVisibility(View.GONE);
        }




        return rowView;
    }

    public void getAgentList(){

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("");

        // set the custom dialog components - text, image and button
        agent = (Spinner) dialog.findViewById(R.id.agent);
        assign_task = (Button) dialog.findViewById(R.id.assign_task);

        // if button is clicked, close the custom dialog
        assign_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AGENT_ID.trim().equals("0")){
                    Toast.makeText(context,"Please select one agent.",Toast.LENGTH_SHORT).show();
                }else{
                    //Api call for Assigning task
                    registerAmc();
                }


            }
        });

        agent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AGENT_ID = agentId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token",prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, ""));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(AGENT_LIST,hashMap,AGENT_LIST_REQUEST,this,AGENT_LIST_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(context);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }


        if(requestData == AGENT_LIST_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");

                    agent_name.clear();
                    agentId.clear();
                    agentId.add("0");
                    agent_name.add(SELECT_AGENT);
                    AGENT_ID = "0";


                    JSONArray jsonArray = jsonObject.optJSONArray("data");


                    if(jsonArray.length() >0){

                        for(int i=0;i<jsonArray.length();i++){

                            if((!jsonArray.optJSONObject(i).optString("flag").trim().equals("1")) && (jsonArray.optJSONObject(i).optString("type").trim().equals("1"))){
                                agentId.add(jsonArray.optJSONObject(i).optString("id"));
                                agent_name.add(jsonArray.optJSONObject(i).optString("fname")+" "+jsonArray.optJSONObject(i).optString("mname")+" "+jsonArray.optJSONObject(i).optString("lname"));
                            }
                          }
                        ArrayAdapter adapter = new ArrayAdapter(context,R.layout.produt_spinner,agent_name);
                        agent.setAdapter(adapter);
                        dialog.show();


                    }else{
                        Toast.makeText(context,"No agent found..",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(context,"No agent found..",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(context,"No agent found..",Toast.LENGTH_SHORT).show();
            }
        }



        if (requestData == REGISTER_AMC_COMPLAIN_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        Toast.makeText(context,"Assigned to agent successfully..",Toast.LENGTH_SHORT).show();

                        try{

                            dialog.dismiss();
                            agent.setSelection(0);

                        }catch (Exception e){}

                    } else {
                        Toast.makeText(context,"Try Again..",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(context,"Try Again..",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context,"Try Again..",Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void registerAmc(){

        String TOKEN = prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, PrefferenceManager.DEFAULT_LOGIN_TOKEN);


        HashMap<String,JSONObject> dataMap =null;

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id",AGENT_ID);
            jsonObject.put("id",amc_id); // amc_id

            dataMap = new HashMap<>();
            dataMap.put("amc",jsonObject);

        }catch (Exception e){}


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(REGISTER_AMC_COMPLAIN,REGISTER_AMC_COMPLAIN_REQUEST,this,REGISTER_AMC_COMPLAIN_BASE_URL,dataMap,TOKEN);
        connectivityInterface.startApiProcessing();


    }
}