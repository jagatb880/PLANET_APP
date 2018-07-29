package com.release.bibhu.planetappliances.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.release.bibhu.planetappliances.Activity.UserDashBoard;
import com.release.bibhu.planetappliances.Netwok.NetworkStatus;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.ProgressHandler;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static com.release.bibhu.planetappliances.Util.ApiConstants.CUSTOMER_ENQUERY;
import static com.release.bibhu.planetappliances.Util.ApiConstants.CUSTOMER_ENQUERY_BASE_URL;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_LIST;
import static com.release.bibhu.planetappliances.Util.ApiConstants.PRODUCT_LIST_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.CUSTOMER_ENQUERY_REQUEST;
import static com.release.bibhu.planetappliances.Util.Constants.PRODUCT_LIST_REQUEST;

/**
 * Created by Bibhu on 03-Feb-18.
 */


public class CustomerFeedback extends Fragment implements LocationListener ,ConnectivityInterface.ApiInterafce ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public CustomerFeedback() {
        // Required empty public constructor
    }

    EditText Name, Phone, Address, FeedBack, Email;
    Spinner Product;
    Button Add;
    String Product_Id = "";
    CheckBox is_positive;

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    private Toolbar mToolbar;
    String token;
    Calendar myCalendar, myCalendar1;
    ArrayList<String> product_name = new ArrayList<>();
    ArrayList<String> product_id = new ArrayList<>();

    String provider;
    // Declaring a Location Manager
    Location location;
    double latitude, longitude;
    String Address_From_Google = "";

    public static final int REQUEST_LOCATION = 001;

    GoogleApiClient googleApiClient;

    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    Context context;

    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(View view) {

        Name = (EditText) view.findViewById(R.id.name);
        Phone = (EditText) view.findViewById(R.id.mobile);
        Address = (EditText) view.findViewById(R.id.address);
        Email = (EditText) view.findViewById(R.id.email);
        FeedBack = (EditText) view.findViewById(R.id.cs_feed_back);
        Product = (Spinner) view.findViewById(R.id.product);
        Add = (Button) view.findViewById(R.id.add);
        is_positive = (CheckBox) view.findViewById(R.id.is_positive);


        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());
        token = prefferenceManager.getDataFromPref(PrefferenceManager.LOGIN_TOKEN, "");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.customer_feedback, container, false);

        _init(rootView);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
            }
        });

        Product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Product_Id = "" + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        // Add here



        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getProductList(token);
            getLocation();
        } else {
            mEnableGps();
        }


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
     * Getting product list form server.
     *
     * @param token
     */
    private void getProductList(String token) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(PRODUCT_LIST, hashMap, PRODUCT_LIST_REQUEST, this, PRODUCT_LIST_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * This method is applicable to validate user data;
     */
    private void validateUser() {
        String cName = Name.getText().toString().trim();
        String cPhone = Phone.getText().toString().trim();
        String cAddress = Address.getText().toString().trim();
        String eMail = Email.getText().toString().trim();
        String customerFeedback = FeedBack.getText().toString().trim();

        if (cName.equals("")) {
            CommonUtility.showMessage(getActivity(), "Please enter customer name.");
            return;
        }
        if (cPhone.equals("")) {
            CommonUtility.showMessage(getActivity(), "Please enter customer mobile no.");
            return;
        }

        if (cPhone.length() != 10) {
            CommonUtility.showMessage(getActivity(), "Please enter valid mobile no.");
            return;
        }

        if (cAddress.equals("")) {
            CommonUtility.showMessage(getActivity(), "Please enter customer address.");
            return;
        }
        if (!eMail.equals("")) {
            if (!CommonUtility.isValidMail(eMail)) {
                CommonUtility.showMessage(getActivity(), "Please enter valid email address.");
                return;
            }
        }

        if (!NetworkStatus.getInstance().isConnected(getActivity())) {
            CommonUtility.showMessage(getActivity(), "Please check your internet connection.");
            return;
        }
        if (Product_Id.trim().equals("0")) {
            CommonUtility.showMessage(getActivity(), "Please select your product.");
            return;
        }


        /**
         * Start API Call.
         */
        addEnquery(token, cName, cPhone, cAddress, customerFeedback);
    }

    /**
     * This method is responsible to send data to server for new customer creation .
     */
    private void addEnquery(String token, String name, String phone, String address, String customerFeedback) {


        HashMap<String, JSONObject> dataMap = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("mobile", phone);
            jsonObject.put("email", Email.getText().toString().trim());
            jsonObject.put("address", address);
            jsonObject.put("location", Address_From_Google);

            try{
                jsonObject.put("product_id", product_id.get(Integer.parseInt(Product_Id)));
            }catch (Exception e){}

            jsonObject.put("feedback", customerFeedback);
            jsonObject.put("user_id", prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID, ""));

            String is_pos = "0";
            if (is_positive.isChecked())
                is_pos = "1";

            jsonObject.put("is_positive", is_pos);

            dataMap = new HashMap<>();
            dataMap.put("enquiry", jsonObject);

        } catch (Exception e) {
        }


        ConnectivityInterface connectivityInterface = new ConnectivityInterface(CUSTOMER_ENQUERY, CUSTOMER_ENQUERY_REQUEST, this, CUSTOMER_ENQUERY_BASE_URL, dataMap, token);
        connectivityInterface.startApiProcessing();
    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(getActivity());
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }

        if (requestData == CUSTOMER_ENQUERY_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        Name.setText("");
                        Phone.setText("");
                        Address.setText("");
                        Email.setText("");
                        FeedBack.setText("");
                        Product.setSelection(0);
                        is_positive.setChecked(false);

                        CommonUtility.showMessage(getActivity(), "", "Enquery details saved successfully.");

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


        //***************************************************************************************************************//

        if (requestData == PRODUCT_LIST_REQUEST) {

            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if (response.contains("SUCCESS")) {

                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        product_id.clear();
                        product_name.clear();
                        product_id.add("0");
                        product_name.add("----- Select Product Name -----");

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                if(!jsonArray.optJSONObject(i).optString("flag").trim().equals("1")) {
                                    product_id.add(jsonArray.optJSONObject(i).optString("id"));
                                    product_name.add(jsonArray.optJSONObject(i).optString("model"));
                                }
                            }

                            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.produt_spinner, product_name);
                            Product.setAdapter(adapter);

                        } else {
                            finishActivity("Please add product information before adding customer.");
                        }
                    } else {
                        finishActivity(message);
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

    public void finishActivity(String message) {


        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(message);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(getActivity(), UserDashBoard.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        dlgAlert.create().show();

    }


    //////////////////////////////////////////////////////////////////////////////////




    private void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }

    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        mResult();

    }

    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.


                        break;
                }
            }

        });
    }

    //callback method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        /**
                         * Getting Product List form Server.
                         */
                        getProductList(token);
                        getLocation();

                        break;
                    case Activity.RESULT_CANCELED:
                        getActivity().finish();
                        break;
                    default:
                        break;
                }
                break;

        }
    }

    private void getLocation() {
            try {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
            }
            catch(SecurityException e) {
                e.printStackTrace();
            }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Address_From_Google = location.getLatitude()+","+location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }
}
