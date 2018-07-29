package com.release.bibhu.planetappliances.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.release.bibhu.planetappliances.Activity.LoginActivity;
import com.release.bibhu.planetappliances.Activity.PreLogin;
import com.release.bibhu.planetappliances.Activity.UserDashBoard;
import com.release.bibhu.planetappliances.Database.DBHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.release.bibhu.planetappliances.Util.Util.ERROR_MSG;

/**
 * Created by MUVI on 01-Feb-18.
 */

public class CommonUtility {

    /**
     * This method is applicable to show messages to user.
     * @param context
     * @param title
     * @param message
     */

    public static void showMessage(Context context ,String title ,String message){

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();
    }


    /**
     * This method is applicable to show the error message.
     * @param context
     * @param message
     */
    public static void showMessage(Context context ,String message){

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();
    }


    /**
     * This method is applicable to show a message after that it finishes that page instantly.
     * @param context
     * @param message
     * @param activity
     */
    public static void finishPage(final Context context , String message,final Activity activity){

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        activity.finish();
                    }
                });
        dlgAlert.create().show();
    }

    /**
     * This method is applicable to show the error message.
     * @param context
     * @param message
     * @param finishActivity
     */
    public static void showMessage(final Activity context , String message , final boolean finishActivity){

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        context.finish();
                    }
                });
        dlgAlert.create().show();
    }


    /**
     * This method is used to valiadte Email Id .
     * @param email
     * @return
     */
    public static boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email);
        check = m.matches();
        if (!check) {
        }
        return check;
    }

    public static void getConformation(final Activity context){

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage("Do you want to leave the application ?");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        context.finish();
                    }
                });
        dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dlgAlert.create().show();
    }



    /**
     * This method is used to hide soft keyboaed.
     * @param activity
     */
    public static void hideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * This method is used to detect Tab or Mobile
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * This Method is  used to handle error message coming form api.
     * @param activity
     */
    public static void naviagteToLoginPage(Activity activity){

        PrefferenceManager prefferenceManager = new PrefferenceManager(activity);
        prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME, "0");
        prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN, "");
        prefferenceManager.setDataInPref(PrefferenceManager.USER_ID, "");

        SQLiteDatabase DB = activity.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);

        String Qry = "DELETE FROM " + DBHelper.USER_TABLE;
        DB.execSQL(Qry);

        Toast.makeText(activity,ERROR_MSG,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }



}
