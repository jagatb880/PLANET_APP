package com.release.bibhu.planetappliances.Activity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import static com.release.bibhu.planetappliances.Util.ApiConstants.LOGIN;
import static com.release.bibhu.planetappliances.Util.ApiConstants.LOGIN_BASE_URL;
import static com.release.bibhu.planetappliances.Util.Constants.LOGIN_REQUEST;

public class LoginActivity extends AppCompatActivity implements ConnectivityInterface.ApiInterafce {

    EditText Email,Password;
    CheckBox RememberMe;
    Button Login;
    TextView forgot_password;

    CustomProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    private Toolbar mToolbar;
    String Email_Info,Password_Info;

    LinearLayout child_layout,main_layout;


    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){
        Email = (EditText) findViewById(R.id.email);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        Password = (EditText) findViewById(R.id.password);
        RememberMe = (CheckBox) findViewById(R.id.remember_me);
        Login = (Button) findViewById(R.id.login);
        child_layout = (LinearLayout) findViewById(R.id.child_layout);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);





        prefferenceManager = PrefferenceManager.getFeaturePreference(LoginActivity.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        try{
            Email.setText(prefferenceManager.getDataFromPref(PrefferenceManager.DEFAULT_USER_EMAIL_NO,""));

        }catch (Exception e){}

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(4000);

// Start animating the image
        final ImageView splash = (ImageView) findViewById(R.id.logo);
//        splash.startAnimation(anim);

// Later.. stop the animation
       // splash.setAnimation(null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Follwing code is applicable for hide status bar of this activity.
         */
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login);
        _init();


        /**
         * Navigating form login page to Forgot password Activity .
         */
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgotPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });



        /**
         * This method is used to check whether user has remember me permission or not .
         */
        if(userHasRememberMePermission()){
            Intent intent = new Intent(LoginActivity.this,UserDashBoard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.hideKeyBoard(LoginActivity.this);
                validateUser();
            }
        });

    }

    /**
     * This method is used to check whether user has remember me permission or not .
     */
    private boolean userHasRememberMePermission() {

        if (prefferenceManager.getDataFromPref(PrefferenceManager.REMEMBER_ME, PrefferenceManager.DEFAULT_REMEMBER_ME).equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    /**
     * This method is used to validate user credentials.
     */
    private void validateUser(){
        Email_Info = Email.getText().toString().trim();
        Password_Info = Password.getText().toString().trim();

        if(Email_Info.equals("") && Password_Info.equals("")){
            CommonUtility.showMessage(LoginActivity.this,"Please enter your Eemail/Username & password.");
            return;
        }

        if(Email_Info.equals("")){
            CommonUtility.showMessage(LoginActivity.this,"Please enter your Email/Username.");
            return;
        }

        if(Password_Info.equals("")){
            CommonUtility.showMessage(LoginActivity.this,"Please enter your password.");
            return;
        }

        if(!NetworkStatus.getInstance().isConnected(LoginActivity.this)){
            CommonUtility.showMessage(LoginActivity.this,"Please check your internet connectivity.");
            return;
        }


        /**
         * Call method to authenticate the user.
         */
        authenticateUser(Email_Info,Password_Info);

    }


    /**
     * This method is used to connect to server and authenticate the user .
     * @param Email
     * @param Password
     */
    private void authenticateUser(String Email , String Password){

        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("email",Email);
        inputHashMap.put("password",Password);
        inputHashMap.put("isAndroid","1");
        inputHashMap.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        inputHashMap.put("google_id",prefferenceManager.getDataFromPref(PrefferenceManager.GOOGLE_FCM_TOKEN,""));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(LOGIN,inputHashMap,LOGIN_REQUEST,this,LOGIN_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * This method will handle preExecute processing.
     */
    @Override
    public void onTaskPreExecute() {
        progressHandler = new CustomProgressHandler(LoginActivity.this);
        progressHandler.show();
    }


    /**
     * This method will handle postExecute processing.
     */
    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }

        if(requestData == LOGIN_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){
                        prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"1");

                        String token = jsonObject.optString("token");
                        prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN,token);
                        prefferenceManager.setDataInPref(PrefferenceManager.DEFAULT_USER_EMAIL_NO,Email.getText().toString().trim());


                        JSONObject userData = jsonObject.optJSONObject("data");
                        String fname = userData.optString("fname").trim();
                        String mname = userData.optString("mname").trim();
                        String lname = userData.optString("lname").trim();
                        String prof_img = userData.optString("prof_image").trim();
                        String empid = userData.optString("empid").trim();
                        String Id = userData.optString("id").trim();
                        String type = userData.optString("type").trim();

//                        type = "2"; // Have to delete


                        prefferenceManager.setDataInPref(PrefferenceManager.USER_ID,Id);
                        prefferenceManager.setDataInPref(PrefferenceManager.USER_TYPE,type);


                        String fullName;
                        if(mname!=null && !mname.equals(""))
                            fullName= fname+" "+mname+" "+lname;
                        else
                            fullName= fname+" "+lname;

                        SQLiteDatabase DB = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                        Cursor cursor = DB.rawQuery("SELECT * FROM " + DBHelper.USER_TABLE + " WHERE Email = '" + Email_Info + "'", null);
                        int count = cursor.getCount();
                        if(count>0){
                            String Qry = "UPDATE " + DBHelper.USER_TABLE + " SET Name = '"+fullName+"' , Password = '"+Password_Info+"' , " +
                                    "Token = '"+token+"' , ProfileImage = '"+prof_img+"',EmpId='"+empid+"',Id='"+Id+"' WHERE Email = '"+Email_Info+"'";
                            DB.execSQL(Qry);
                        }else{
                            String Qry = "INSERT INTO " + DBHelper.USER_TABLE + "(Name,Password,Email,Token,ProfileImage,EmpId,Id) VALUES" +
                                    "('" + fullName + "','" + Password_Info + "','" + Email_Info + "'," +
                                    "'" + token + "','" + prof_img + "','" + empid + "','" + Id + "')";

                            DB.execSQL(Qry);
                        }

                        Intent intent = new Intent(LoginActivity.this,UserDashBoard.class);
                        intent.putExtra("notification",0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();



                    }else{
                        prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"0");
                        prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN,"");
                        CommonUtility.showMessage(LoginActivity.this,"Alert",message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"0");
                    prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN,"");
                    CommonUtility.showMessage(LoginActivity.this,"Alert","Something went wrong,please try again.");
                }
            }else {
                prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"0");
                prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN,"");
                CommonUtility.showMessage(LoginActivity.this,"Alert","Something went wrong,please try again.");
            }


        }

    }

}
