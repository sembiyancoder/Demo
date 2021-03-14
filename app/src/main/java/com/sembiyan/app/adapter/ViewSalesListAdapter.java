package com.sembiyan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
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
        JSONArray jsonArray = object.optJSONArray("partner_id");
        String name = jsonArray.optString(1);
        holder.mNameTextView.setText(name);
        holder.mAmountTextView.setText("Rs." + object.optString("amount_total"));
        holder.mOrderNoTextView.setText(object.optString("name"));
        holder.mOrderStatusTextView.setText(object.optString("invoice_status"));
        holder.mDateTimeTextView.setText(object.optString("confirmation_date"));
    }

    @Override
    public int getItemCount() {
        return mSalesJsonArray.length();
    }


    public interface onItemSelectedListener {
        void onSalesItemSelected(JSONObject jsonObject);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView, mAmountTextView, mOrderNoTextView, mOrderStatusTextView, mDateTimeTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.txt_customer_name);
            mAmountTextView = itemView.findViewById(R.id.txt_customer_amount);
            mOrderNoTextView = itemView.findViewById(R.id.txt_order_no);
            mOrderStatusTextView = itemView.findViewById(R.id.txt_order_status);
            mDateTimeTextView = itemView.findViewById(R.id.txt_date_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelectedListener.onSalesItemSelected(mSalesJsonArray.optJSONObject(getAdapterPosition()));
                }
            });
        }
    }


}