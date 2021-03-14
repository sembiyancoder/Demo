package com.sembiyan.app.ui.sales_order;

import android.app.DownloadManager;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sembiyan.app.databinding.LayoutLineItemBinding;
import com.sembiyan.app.model.LineModel;
import com.sembiyan.app.utilities.LineSingleTon;

import java.util.List;

public class NewSaleOrderAdapter extends RecyclerView.Adapter<NewSaleOrderAdapter.MyViewHolder> {

    private List<LineModel> list;

    public NewSaleOrderAdapter(List<LineModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NewSaleOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutLineItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewSaleOrderAdapter.MyViewHolder holder, int position) {

        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LayoutLineItemBinding binding;

        public MyViewHolder(@NonNull LayoutLineItemBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }

        public void bind(LineModel data, int position) {
            binding.textItem.setText(data.getItem());

            String calc = data.getQuantity() + " x " + data.getRate();
            binding.textCalculation.setText(calc);

            String total = "\u20B9 " + (data.getQuantity() * data.getRate());
            binding.textTotal.setText(total);

            binding.imageViewDelete.setOnClickListener(v -> {
                list.remove(position);
                LineSingleTon.getInstance().getLineModels().remove(position);
                notifyDataSetChanged();

                undo(data, position, v);
            });
        }
    }

    private void undo(LineModel data, int position, View view) {
        Snackbar snackbar = Snackbar
                .make(view, "Deleted Successfully", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list.add(position, data);
                        LineSingleTon.getInstance().getLineModels().add(position, data);
                        notifyDataSetChanged();
                    }
                });

        snackbar.show();

    }
}
