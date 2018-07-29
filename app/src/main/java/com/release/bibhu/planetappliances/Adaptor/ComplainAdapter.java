package com.release.bibhu.planetappliances.Adaptor;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.release.bibhu.planetappliances.Model.Product;
import com.release.bibhu.planetappliances.R;
import com.release.bibhu.planetappliances.Util.Util;

import java.util.ArrayList;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ViewHolder> {

    private final Activity context;
    ArrayList<Product> products = new ArrayList<>();

    public ComplainAdapter(Activity context, ArrayList<Product> products) {
        // TODO Auto-generated constructor stub

        this.context = context;
        this.products = products;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.p_name.setText(products.get(position).getProduct_name());
        holder.p_make.setText(products.get(position).getProduct_make());
        holder.p_model.setText(products.get(position).getProduct_model());


        if (Util.selected_page_row == position) {
            holder.radioButton.setChecked(true);

        } else {
            holder.radioButton.setChecked(false);
        }

        if(position == products.size()-1){
            holder.line.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView p_name, p_make, p_model;
        RadioButton radioButton;
        View line;


        public ViewHolder(View rowView) {

            super(rowView);
            p_name = (TextView) rowView.findViewById(R.id.p_name);
            p_make = (TextView) rowView.findViewById(R.id.p_make);
            p_model = (TextView) rowView.findViewById(R.id.p_model);
            radioButton = (RadioButton) rowView.findViewById(R.id.selection);
            line = (View) rowView.findViewById(R.id.line);


        }
    }

}

