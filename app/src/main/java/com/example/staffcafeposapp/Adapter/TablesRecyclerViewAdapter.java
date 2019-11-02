package com.example.staffcafeposapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Model.Tables;
import com.example.staffcafeposapp.R;

import java.util.ArrayList;

public class TablesRecyclerViewAdapter extends RecyclerView.Adapter<TablesRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Tables> tablesArrayList;

    public TablesRecyclerViewAdapter(Context context, ArrayList<Tables> tablesArrayList) {
        this.context = context;
        this.tablesArrayList = tablesArrayList;
    }

    @NonNull
    @Override
    public TablesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.row_tables, parent,false);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TablesRecyclerViewAdapter.ViewHolder holder, int position) {

        Tables tables = tablesArrayList.get(position);
        holder.textView_tableNo.setText(String.valueOf(tables.getTableNo()));
        holder.textView_tableNoOfSeat.setText(String.valueOf(tables.getTableNoOfSeat()));
        holder.textView_tableStatus.setText(String.valueOf(tables.getTableStatus()));

    }

    @Override
    public int getItemCount() {
        return tablesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_tableNo, textView_tableNoOfSeat, textView_tableStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_tableNo = itemView.findViewById(R.id.textView_tableNo);
            textView_tableNoOfSeat = itemView.findViewById(R.id.textView_noOfSeat);
            textView_tableStatus = itemView.findViewById(R.id.textView_table_status);

        }
    }
}
