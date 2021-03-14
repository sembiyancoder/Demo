package com.sembiyan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sembiyan.app.R;
import com.sembiyan.app.model.CustomerModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends ArrayAdapter<String> {
    private Context mContext; //context
    private ArrayList<String> mCustomerNameList; //data source of the list adapter

    public CustomerListAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
        this.mContext = context;
        this.mCustomerNameList = items;
    }


    @Override
    public int getCount() {
        return mCustomerNameList.size(); //returns total of items in the list
    }

    @Override
    public String getItem(int position) {
        return mCustomerNameList.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_customer_name_list, parent, false);
        }
        String item = (String) getItem(position);
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.txt_customer_name);
        View view = (View) convertView.findViewById(R.id.layout);
        textViewItemName.setText(item);
        return convertView;
    }
}