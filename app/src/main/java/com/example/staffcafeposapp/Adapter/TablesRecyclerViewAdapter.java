package com.example.staffcafeposapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Model.Tables;
import com.example.staffcafeposapp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TablesRecyclerViewAdapter extends RecyclerView.Adapter<TablesRecyclerViewAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener {

    private Context context;
    private ArrayList<Tables> tablesArrayList;
    private Spinner statusSpinner;
    private Button button_confirmStatus;
    private String tableStatus;
    private TextView textView_popup_tableNo;

    public TablesRecyclerViewAdapter(Context context, ArrayList<Tables> tablesArrayList) {
        this.context = context;
        this.tablesArrayList = tablesArrayList;
    }

    @NonNull
    @Override
    public TablesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.table_item, parent,false);
        final ViewHolder holder =new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(tablesArrayList.get(holder.getAdapterPosition()));

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TablesRecyclerViewAdapter.ViewHolder holder, int position) {

        Tables tables = tablesArrayList.get(position);
        holder.textView_tableNo.setText(String.valueOf(tables.getTableNo()));
        holder.textView_tableNoOfSeat.setText(String.format("(%d)", tables.getTableNoOfSeat()));
        holder.textView_tableStatus.setText(String.valueOf(tables.getTableStatus()));

        String tablestatus = holder.textView_tableStatus.getText().toString();

        if(tablestatus.equals("Available"))
        {
            holder.textView_tableStatus.setTextColor(Color.GREEN);
        }
        else
        {
            holder.textView_tableStatus.setTextColor(Color.RED);
        }
    }

    private void showPopup(final Tables tables)
    {
        View popUpView = LayoutInflater.from(context).inflate(R.layout.tables_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popUpView, 400, 250,true);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

        statusSpinner = popUpView.findViewById(R.id.spinner_popupTable);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.table_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setOnItemSelectedListener(this);

        textView_popup_tableNo = popUpView.findViewById(R.id.textView_popup_tableNumber);
        textView_popup_tableNo.setText(tables.getTableNo());

        button_confirmStatus = popUpView.findViewById(R.id.button_confirmTableStatus);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        button_confirmStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = db.collection("Tables").document(tables.getTableNo());

                tables.setTableStatus(tableStatus);
                documentReference.set(tables);
                notifyDataSetChanged();
                popupWindow.dismiss();


            }
        });
    }

    @Override
    public int getItemCount() {
        return tablesArrayList.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        tableStatus = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textView_tableNo, textView_tableNoOfSeat, textView_tableStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_tableNo = itemView.findViewById(R.id.textView_tableNumber);
            textView_tableNoOfSeat = itemView.findViewById(R.id.textView_tableNoOfSeat);
            textView_tableStatus = itemView.findViewById(R.id.textView_tablestatus);

        }
    }
}
