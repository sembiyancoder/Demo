package com.sembiyan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sembiyan.app.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class ViewSalesListAdapter extends RecyclerView.Adapter<ViewSalesListAdapter.MyViewHolder> {

    onItemSelectedListener onItemSelectedListener;
    private Context mContext;
    private JSONArray mSalesJsonArray;

    public ViewSalesListAdapter(Context context, JSONArray jsonArray, onItemSelectedListener onItemSelectedListener) {
        this.mContext = context;
        this.mSalesJsonArray = jsonArray;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sales_order_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        JSONObject object = mSalesJsonArray.optJSONObject(position);
        holder.mCustomerName.setText(object.optString("name"));
    }

    @Override
    public int getItemCount() {
        return mSalesJsonArray.length();
    }


    public interface onItemSelectedListener {
        void onSalesItemSelected(JSONObject jsonObject);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mCustomerName;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCustomerName = itemView.findViewById(R.id.txt_customer_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelectedListener.onSalesItemSelected(mSalesJsonArray.optJSONObject(getAdapterPosition()));
                }
            });
        }
    }


}