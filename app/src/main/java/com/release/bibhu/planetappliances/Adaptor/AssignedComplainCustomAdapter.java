package com.release.bibhu.planetappliances.Adaptor;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Activity.UserDashBoard;
import com.release.bibhu.planetappliances.Model.Complain;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Util.PrefferenceManager;

import java.util.ArrayList;

public class AssignedComplainCustomAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<Complain> complains = new ArrayList<>();
    TextView Desc;
    TextView complainId;
    LinearLayout ll;
    ImageView assign_complain,indicator;
    PrefferenceManager prefferenceManager;

    public AssignedComplainCustomAdapter(Activity context, ArrayList<Complain> complains) {
        super(context, R.layout.assigned_complain_list);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.complains = complains;

    }

    @Override
    public int getCount() {
        return complains.size();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.assigned_complain_list, null, true);

        Desc = (TextView) rowView.findViewById(R.id.desc);
        complainId = (TextView) rowView.findViewById(R.id.complainId);
        assign_complain = (ImageView) rowView.findViewById(R.id.assign_complain);
        indicator = (ImageView) rowView.findViewById(R.id.indicator);
        ll = (LinearLayout) rowView.findViewById(R.id.ll);

        Desc.setText(complains.get(position).getIssue());
        complainId.setText(complains.get(position).getComplainId());

        prefferenceManager = PrefferenceManager.getFeaturePreference(context);
        if (prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("1") ||
                prefferenceManager.getDataFromPref(PrefferenceManager.USER_TYPE, "").equals("3")) {
            assign_complain.setVisibility(View.GONE);
        }else {
            assign_complain.setVisibility(View.VISIBLE);
        }


        if(position == 0){
            Desc.setTextSize(16);
            complainId.setTextSize(16);
            Desc.setTypeface(null, Typeface.BOLD);
            complainId.setTypeface(null, Typeface.BOLD);
            assign_complain.setVisibility(View.GONE);
            indicator.setVisibility(View.GONE);
        }


        if(complains.get(position).getPriority().endsWith("High")){
            indicator.setImageResource(R.drawable.high_praiority);
        }else {
            indicator.setImageResource(R.drawable.low_praiority);
        }


        return rowView;

    };
}