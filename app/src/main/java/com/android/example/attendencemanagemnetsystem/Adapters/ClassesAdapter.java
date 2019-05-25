package com.android.example.attendencemanagemnetsystem.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.attendencemanagemnetsystem.R;


import java.util.ArrayList;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ClassesHolder> {
    private ArrayList<String> classesList;
    private Context context;

    public ClassesAdapter(ArrayList<String> classesList, Context context) {
        this.classesList = classesList;
        this.context = context;

    }

    @NonNull
    @Override
    public ClassesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ClassesHolder(LayoutInflater.from(context).inflate(R.layout.classes_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesHolder classesHolder, int i) {
        String className = classesList.get(i);
        classesHolder.nameTextView.setText(className);

    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    class ClassesHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ClassesHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_classes_li_name);
        }
    }
}
