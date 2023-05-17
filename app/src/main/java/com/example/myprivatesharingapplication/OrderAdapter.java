package com.example.myprivatesharingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    private Context context;
    private List<Order> orderList;
    private OrderAdapter.OnRowClickListener listener;

    public OrderAdapter(Context context, List<Order> orderList, OrderAdapter.OnRowClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    public interface OnRowClickListener {
        void onItemClick (int position);
        void onShareButtonClick(View v, int position);


    }
    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_row,parent,false);
        return new OrderAdapter.OrderViewHolder(itemView, listener );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        holder.orderNameText.setText("Recipient: " + orderList.get(position).getReceiverName());
        holder.orderDescriptionText.setText("Drop-off time: " + orderList.get(position).getDropoffTime());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView orderImage ;
        public TextView orderNameText, orderDescriptionText;
        public ImageButton shareOrderButton ;
        public OnRowClickListener OnRowClickListener;

        public OrderViewHolder(@NonNull View itemView, OnRowClickListener OnRowClickListener) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.deliveryImage);
            orderNameText = itemView.findViewById(R.id.deliveryTitleVehicle);
            orderDescriptionText = itemView.findViewById(R.id.deliveryDescText);
            shareOrderButton = itemView.findViewById(R.id.shareDetailsButton);

            shareOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnRowClickListener.onShareButtonClick(view,getAdapterPosition());
                }
            });
            this.OnRowClickListener = OnRowClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            OnRowClickListener.onItemClick(getAdapterPosition());
        }
    }
}
