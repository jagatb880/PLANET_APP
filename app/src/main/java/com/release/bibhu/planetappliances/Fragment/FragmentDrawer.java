package com.release.bibhu.planetappliances.Fragment;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Activity.LoginActivity;
import com.release.bibhu.planetappliances.Activity.NavDrawerItem;
import com.release.bibhu.planetappliances.Adaptor.NavigationDrawerAdapter;
import com.release.bibhu.planetappliances.Database.DBHelper;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    public static String[] titles = null;
    public static ArrayList<String> TitleArrayList = new ArrayList<>();
    private FragmentDrawerListener drawerListener;
    ArrayList<Integer>Nav_menu_image = new ArrayList<>();
    PrefferenceManager prefferenceManager;

    CircleImageView profile_image;
    TextView profile_name;
    TextView profile_id;

    SQLiteDatabase DB;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public  List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();
        TitleArrayList.clear();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {


            if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("0")){
                if(titles[i].contains("Dashboard") || titles[i].contains("Complaint")  || titles[i].contains("AMC List")||
                        titles[i].contains("Enquiry List")|| titles[i].contains("Change Password")
                        || titles[i].contains("Logout") ){

                    TitleArrayList.add(titles[i]);

                    NavDrawerItem navItem = new NavDrawerItem();
                    navItem.setTitle(titles[i]);
                    data.add(navItem);
                }
            }

            if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("1")){
                if(titles[i].contains("Dashboard") || titles[i].contains("Task Assigned") || titles[i].contains("Register AMC") || titles[i].contains("Change Password")
                        || titles[i].contains("Add New Complain") || titles[i].contains("Logout") || titles[i].contains("AMC List") ){

                    if(titles[i].contains("Task Assigned")){
                        TitleArrayList.add("Complaint ");
                    }else{
                        TitleArrayList.add(titles[i]);
                    }


                    NavDrawerItem navItem = new NavDrawerItem();
                    if(titles[i].contains("Task Assigned")){
                        navItem.setTitle("Complaint ");
                    }else{
                        navItem.setTitle(titles[i]);
                    }

                    data.add(navItem);
                }
            }

            if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("2")){
                if(titles[i].contains("Dashboard") || titles[i].contains("Complaint") || titles[i].contains("AMC List")||
                        titles[i].contains("Change Password")
                        || titles[i].contains("Logout") ){

                    TitleArrayList.add(titles[i]);

                    NavDrawerItem navItem = new NavDrawerItem();
                    navItem.setTitle(titles[i]);
                    data.add(navItem);
                }
            }

            if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("3")){
                if(titles[i].contains("Dashboard") || titles[i].contains("Add Enquiry") || titles[i].contains("Change Password")
                        || titles[i].contains("Enquiry List") || titles[i].contains("Logout") ){
                    TitleArrayList.add(titles[i]);

                    NavDrawerItem navItem = new NavDrawerItem();
                    navItem.setTitle(titles[i]);
                    data.add(navItem);
                }
            }


        }
        return data;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefferenceManager = PrefferenceManager.getFeaturePreference(getActivity());


        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        DB = getActivity().openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        profile_image = (CircleImageView) layout.findViewById(R.id.profile_image);
        profile_name = (TextView) layout.findViewById(R.id.name);
        profile_id = (TextView) layout.findViewById(R.id.user_id);

        if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("0") ||
                prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("2") ){
            profile_id.setVisibility(View.GONE);
        }

        Cursor cursor = DB.rawQuery("SELECT Name,EmpId,ProfileImage FROM " + DBHelper.USER_TABLE , null);
        int count = cursor.getCount();
        if(count>0){
            if (cursor.moveToFirst()) {
                do {

                    profile_name.setText(cursor.getString(0).trim());
                    profile_id.setText("ID : "+cursor.getString(1).trim());


                    if(!(cursor.getString(2).trim()).equals("")){
                        Picasso.with(getActivity()).load(cursor.getString(2).trim()).fit().centerCrop()
                                .placeholder(R.drawable.default_profile)
                                .error(R.drawable.default_profile)
                                .into(profile_image);
                    }else {
                        profile_image.setImageResource(R.drawable.default_profile);
                    }

                } while (cursor.moveToNext());
            }
        }


        profile_image.setImageResource(R.drawable.profile);


        if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("0")){
            Nav_menu_image.add(R.drawable.dashboard);
            Nav_menu_image.add(R.drawable.complian_with_bill_no);
            Nav_menu_image.add(R.drawable.amc_list);
            Nav_menu_image.add(R.drawable.customer_enquery);
            Nav_menu_image.add(R.drawable.change_password);
            Nav_menu_image.add(R.drawable.logout);
        }

        if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("1")){
            Nav_menu_image.add(R.drawable.dashboard);
            Nav_menu_image.add(R.drawable.complian_with_bill_no);
            Nav_menu_image.add(R.drawable.amc_list);
            Nav_menu_image.add(R.drawable.register_amc);
            Nav_menu_image.add(R.drawable.add_new_complain);
            Nav_menu_image.add(R.drawable.change_password);
            Nav_menu_image.add(R.drawable.logout);
        }

        if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("2")){
            Nav_menu_image.add(R.drawable.dashboard);
            Nav_menu_image.add(R.drawable.complian_with_bill_no);
            Nav_menu_image.add(R.drawable.complian_without_bill_no);
            Nav_menu_image.add(R.drawable.amc_list);
            Nav_menu_image.add(R.drawable.change_password);
            Nav_menu_image.add(R.drawable.logout);
        }

        if(prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE,"").equals("3")){
            Nav_menu_image.add(R.drawable.dashboard);
            Nav_menu_image.add(R.drawable.add_enquery);
            Nav_menu_image.add(R.drawable.customer_enquery);
            Nav_menu_image.add(R.drawable.change_password);
            Nav_menu_image.add(R.drawable.logout);
        }




        adapter = new NavigationDrawerAdapter(getActivity(), getData(),Nav_menu_image);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}