package com.example.staffcafeposapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffcafeposapp.Adapter.TablesRecyclerViewAdapter;
import com.example.staffcafeposapp.Model.Tables;
import com.example.staffcafeposapp.R;

import java.util.ArrayList;

public class TablesFragment extends Fragment {

    //RecyclerView and Adapter
    private RecyclerView table_recyclerView;
    private TablesRecyclerViewAdapter table_Adapter;
    private ArrayList<Tables> tablesArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tables, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tablesArrayList = new ArrayList<>();

        tablesArrayList.add(new Tables("A001", 4, "Available"));
        tablesArrayList.add(new Tables("A002", 4, "Available"));
        tablesArrayList.add(new Tables("A003", 6, "Available"));
        tablesArrayList.add(new Tables("A004", 2, "Available"));
        tablesArrayList.add(new Tables("A005", 10, "Available"));
        tablesArrayList.add(new Tables("A005", 10, "Available"));
        tablesArrayList.add(new Tables("A005", 10, "Available"));
        tablesArrayList.add(new Tables("A005", 10, "Available"));
        tablesArrayList.add(new Tables("A005", 10, "Available"));

        table_recyclerView =view.findViewById(R.id.recyclerView_Tables);
        table_recyclerView.setHasFixedSize(true);
        table_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        table_Adapter = new TablesRecyclerViewAdapter(this.getContext(), tablesArrayList);
        table_recyclerView.setAdapter(table_Adapter);
        table_recyclerView.addItemDecoration(new DividerItemDecoration(table_recyclerView.getContext(), DividerItemDecoration.VERTICAL));


    }
}
