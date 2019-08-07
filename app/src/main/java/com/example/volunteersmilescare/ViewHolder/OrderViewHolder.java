package com.example.volunteersmilescare.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.volunteersmilescare.R;


public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOrderId, txtOrderName, txtOrderPhone, txtOrderAmount, txtOrdeDate;

    public Button btnGenerate;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderName = itemView.findViewById(R.id.order_name);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderAmount = itemView.findViewById(R.id.order_amount);
        txtOrdeDate = itemView.findViewById(R.id.order_date);

        btnGenerate = itemView.findViewById(R.id.btnGenerate);

    }
}
