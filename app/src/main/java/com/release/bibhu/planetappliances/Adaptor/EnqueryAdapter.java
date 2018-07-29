package com.release.bibhu.planetappliances.Adaptor;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Model.Complain;
import com.release.bibhu.planetappliances.R;

import java.util.ArrayList;

public class EnqueryAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<Complain> complains = new ArrayList<>();
    TextView name,phone,add;

    public EnqueryAdapter(Activity context, ArrayList<Complain> complains) {
        super(context, R.layout.enquery_dashboard_adapter);
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
        View rowView = inflater.inflate(R.layout.enquery_dashboard_adapter, null, true);

        name = (TextView) rowView.findViewById(R.id.name);
        phone = (TextView) rowView.findViewById(R.id.phone);
        add = (TextView) rowView.findViewById(R.id.add);

        name.setText(complains.get(position).getCustomer_name());
        phone.setText(complains.get(position).getMobile());
        add.setText(complains.get(position).getAddress());

        if(position == 0)
        {
            name.setTypeface(null,Typeface.BOLD);
            phone.setTypeface(null,Typeface.BOLD);
            add.setTypeface(null,Typeface.BOLD);

            name.setTextSize(16);
            phone.setTextSize(16);
            add.setTextSize(16);
        }else {
            name.setTextSize(14);
            phone.setTextSize(14);
            add.setTextSize(14);
        }

        return rowView;

    };
}