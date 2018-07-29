package com.release.bibhu.planetappliances.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.release.bibhu.planetappliances.Activity.PreLogin;
import com.release.bibhu.planetappliances.Activity.UserDashBoard;
import com.release.bibhu.planetappliances.Database.DBHelper;
import com.release.bibhu.planetappliances.Netwok.NetworkStatus;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Retrofit.ConnectivityInterface;
import com.release.bibhu.planetappliances.Util.CommonUtility;
import com.release.bibhu.planetappliances.Util.CustomProgressHandler;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.release.bibhu.planetappliances.Util.ProgressHandler;

import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static com.release.bibhu.planetappliances.Util.ApiConstants.CHANGE_PASSWORD;
import static com.release.bibhu.planetappliances.Util.ApiConstants.CHANGE_PASSWORD_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.CHANGE_PASSWORD_REQUEST;

/**
 * Created by Bibhu on 03-Feb-18.
 */


public class ChangePassword extends Fragment implements ConnectivityInterface.ApiInterafce {


    EditText old_password, new_password, conf_password;
    CheckBox RememberMe;
    Button change_password;

    CustomProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    private Toolbar mToolbar;
    String old_password1, new_password1, conf_password1;

    LinearLayout child_layout, main_layout;
    SQLiteDatabase DB;
    String OLD_PASSWORD = "";
    String ID = "";
    String TOKEN = "";


    public ChangePassword() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(View view) {
        old_password = (EditText) view.findViewById(R.id.old_password);
        new_password = (EditText) view.findViewById(R.id.new_password);
        conf_password = (EditText) view.findViewById(R.id.conf_password);
        change_password = (Button) view.findViewById(R.id.change_password);
        child_layout = (LinearLayout) view.findViewById(R.id.child_layout);
        main_layout = (LinearLayout) view.findViewById(R.id.main_layout);


        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.change_password, container, false);

        _init(rootView);

        DB = getActivity().openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);

        Cursor cursor = DB.rawQuery("SELECT Password,Id,Token FROM " + DBHelper.USER_TABLE, null);
        int count = cursor.getCount();

        if (count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    OLD_PASSWORD = cursor.getString(0).trim();
                    ID = cursor.getString(1).trim();
                    TOKEN = cursor.getString(2).trim();
                } while (cursor.moveToNext());
            }
        }

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
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
     * This method is used to validate user credentials.
     */
    private void validateUser() {
        old_password1 = old_password.getText().toString().trim();
        new_password1 = new_password.getText().toString().trim();
        conf_password1 = conf_password.getText().toString().trim();

        if (old_password1.equals("")) {
            CommonUtility.showMessage(getActivity(), "Please enter your old password.");
            return;
        }

        if (new_password1.equals("")) {
            CommonUtility.showMessage(getActivity(), "Please enter your new password.");
            return;
        }

        if (conf_password1.equals("")) {
            CommonUtility.showMessage(getActivity(), "Please enter your confirm password.");
            return;
        }
        if (!old_password1.equals(OLD_PASSWORD)) {
            CommonUtility.showMessage(getActivity(), "Please enter valid old password.");
            return;
        }
        if (!new_password1.equals(new_password1)) {
            CommonUtility.showMessage(getActivity(), "Confirm password doesn't match.");
            return;
        }


        if (!NetworkStatus.getInstance().isConnected(getActivity())) {
            CommonUtility.showMessage(getActivity(), "Please check your internet connectivity.");
            return;
        }


        /**
         * Call method to authenticate the user.
         */
        callAPI(TOKEN, ID, new_password1);
    }

    public void callAPI(String token, String Id, String password) {
        HashMap<String, String> inputHashMap = new HashMap<>();
        inputHashMap.put("token", token);
        inputHashMap.put("id", Id);
        inputHashMap.put("password", password);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(CHANGE_PASSWORD, inputHashMap, CHANGE_PASSWORD_REQUEST, this, CHANGE_PASSWORD_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new CustomProgressHandler(getActivity());
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if (progressHandler != null && progressHandler.isShowing()) {
            progressHandler.hide();
        }

        if (requestData == CHANGE_PASSWORD_REQUEST) {


            if (response != null && response.contains("status")) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");

                    if (response.contains("SUCCESS")) {

                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                        dlgAlert.setMessage("Success! Your Password has been changed! Please login with you new password.");
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        PrefferenceManager prefferenceManager = new PrefferenceManager(getActivity());
                                        prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME, "0");
                                        prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN, "");
                                        prefferenceManager.setDataInPref(PrefferenceManager.USER_ID, "");

                                        SQLiteDatabase DB = getActivity().openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);

                                        String Qry = "DELETE FROM " + DBHelper.USER_TABLE;
                                        DB.execSQL(Qry);

                                        Intent intent = new Intent(getActivity(),PreLogin.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                        dlgAlert.create().show();
                    } else {
                        CommonUtility.showMessage(getActivity(), "Alert", message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(getActivity(), "Alert", "Something went wrong,please try again.");
                }
            } else {
                CommonUtility.showMessage(getActivity(), "Alert", "Something went wrong,please try again.");
            }
        }
    }


}
