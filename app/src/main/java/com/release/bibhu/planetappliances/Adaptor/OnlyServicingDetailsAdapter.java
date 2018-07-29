package com.release.bibhu.planetappliances.Adaptor;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Activity.EnquiryDetails;
import com.release.bibhu.planetappliances.Model.Complain;
import com.release.bibhu.planetappliances.Model.OnlyServiceDetailsModel;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Util.Util;

import java.util.ArrayList;

public class OnlyServicingDetailsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<OnlyServiceDetailsModel> onlyServiceDetailsModels = new ArrayList<>();
    TextView service_no,service_date;
    CheckBox is_positive;
    ImageView action;

    public OnlyServicingDetailsAdapter(Activity context, ArrayList<OnlyServiceDetailsModel> onlyServiceDetailsModels) {
        super(context, R.layout.only_servicing_details_adapter);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.onlyServiceDetailsModels = onlyServiceDetailsModels;

    }

    @Override
    public int getCount() {
        return onlyServiceDetailsModels.size();
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.only_servicing_details_adapter, null, true);

        service_no = (TextView) rowView.findViewById(R.id.service_no);
        service_date = (TextView) rowView.findViewById(R.id.service_date);
        is_positive = (CheckBox) rowView.findViewById(R.id.is_positive);

        service_no.setText("Service No : "+onlyServiceDetailsModels.get(position).getService_no());
        service_date.setText("Service Date : "+onlyServiceDetailsModels.get(position).getService_date());

        is_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.amc_service_id = onlyServiceDetailsModels.get(position).getService_id();
                Util.selected_amc = position;
                notifyDataSetChanged();
            }
        });

        if(Util.selected_amc == position){
            is_positive.setChecked(true);
            Util.amc_service_id = onlyServiceDetailsModels.get(position).getService_id();
            Util.selected_amc = position;
        }else {
            is_positive.setChecked(false);
        }

        return rowView;

    };
}