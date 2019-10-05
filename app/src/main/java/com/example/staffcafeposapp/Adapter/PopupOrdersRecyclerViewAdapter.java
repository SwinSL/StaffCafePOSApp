package com.example.staffcafeposapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Model.MenuItem;
import com.example.staffcafeposapp.R;
import java.util.List;

public class PopupOrdersRecyclerViewAdapter extends RecyclerView.Adapter<PopupOrdersRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<MenuItem> menuItemList;
    private double order_total;

    public PopupOrdersRecyclerViewAdapter(Context context, List<MenuItem> menuItemList) {
        this.context = context;
        this.menuItemList = menuItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.orders_popup_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = menuItemList.get(position);

        String price = String.format("%.2f", menuItem.getItem_price());
        price = "RM" + price;

        holder.item_name.setText(menuItem.getItem_name());
        holder.item_price.setText(price);
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView item_name;
        private TextView item_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.orders_item_name);
            item_price = itemView.findViewById(R.id.orders_item_price);
        }
    }

    public String getOrder_total() {
        order_total = 0;

        for(MenuItem item: menuItemList){
            order_total+=item.getItem_price();
        }

        String total = String.format("%.2f", order_total);
        total = "RM" + total;

        return total;
    }
}
