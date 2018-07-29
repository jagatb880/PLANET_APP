package com.release.bibhu.planetappliances.Adaptor;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.release.bibhu.planetappliances.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> complains = new ArrayList<>();
    ArrayList<String> service = new ArrayList<>();
    TextView sl_no,complain,service_details;
    ImageView action;
    ArrayList<com.release.bibhu.planetappliances.Model.Complain> complains_list = new ArrayList<>();

    public CustomAdapter(Activity context, ArrayList<String> complains, ArrayList<String> service,ArrayList<com.release.bibhu.planetappliances.Model.Complain> complains_list) {
        super(context, R.layout.complain_list, complains);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.complains = complains;
        this.service = service;
        this.complains_list = complains_list;

    }

    @Override
    public int getCount() {
        Log.v("BBB",""+service.size());
        return service.size();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.complain_list, null, true);

        sl_no = (TextView) rowView.findViewById(R.id.sl_no);
        complain = (TextView) rowView.findViewById(R.id.complain);
        service_details = (TextView) rowView.findViewById(R.id.service_details);
        action = (ImageView) rowView.findViewById(R.id.action);

        sl_no.setText(""+(position+1));
        complain.setText(complains.get(position));
        service_details.setText(service.get(position));

        return rowView;

    };
}