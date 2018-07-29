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
import com.release.bibhu.planetappliances.R;

import java.util.ArrayList;

public class EnqueryListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<Complain> complains = new ArrayList<>();
    TextView name,phone,product,add;
    CheckBox is_positive;
    ImageView action;

    public EnqueryListAdapter(Activity context, ArrayList<Complain> complains) {
        super(context, R.layout.enquery_adapter_list);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.complains = complains;

    }

    @Override
    public int getCount() {
        return complains.size();
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.enquery_adapter_list, null, true);

        name = (TextView) rowView.findViewById(R.id.name);
        phone = (TextView) rowView.findViewById(R.id.phone);
        product = (TextView) rowView.findViewById(R.id.product);
        add = (TextView) rowView.findViewById(R.id.add);
        is_positive = (CheckBox) rowView.findViewById(R.id.is_positive);
        action = (ImageView) rowView.findViewById(R.id.action);

        name.setText(complains.get(position).getCustomer_name());
        phone.setText(complains.get(position).getMobile());
        product.setText("Enq Id. : "+complains.get(position).getEnquiryId());
        add.setText(complains.get(position).getAddress());

        if(complains.get(position).getPositive().trim().equals("1")){
            is_positive.setChecked(true);
        }

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EnquiryDetails.class);
                intent.putExtra("product_name",complains.get(position).getProduct_name());
                intent.putExtra("feedback",complains.get(position).getFeedBack());
                intent.putExtra("enquiry_id",complains.get(position).getEnquiryId());

                if(complains.get(position).getPositive().trim().equals("1")){
                    intent.putExtra("is_positive","Yes");
                }else{
                    intent.putExtra("is_positive","No");
                }

                intent.putExtra("name",complains.get(position).getCustomer_name());
                intent.putExtra("phone",complains.get(position).getMobile());
                intent.putExtra("email",complains.get(position).getEmail());
                intent.putExtra("address",complains.get(position).getAddress());

                intent.putExtra("emp_name",complains.get(position).getEmp_name());
                intent.putExtra("emp_id",complains.get(position).getEmp_id());
                intent.putExtra("emp_phone",complains.get(position).getEmp_phone());
                intent.putExtra("emp_desig",complains.get(position).getEmp_desig());

                context.startActivity(intent);
            }
        });

        return rowView;

    };
}