package com.android.example.attendencemanagemnetsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.example.attendencemanagemnetsystem.Interfaces.ClassItemCallbacks;
import com.android.example.attendencemanagemnetsystem.Models.ClassModel;
import com.android.example.attendencemanagemnetsystem.R;


import java.util.ArrayList;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ClassesHolder> {
    private ArrayList<ClassModel> classesList;
    private Context context;
    private ClassItemCallbacks classItemCallbacks;

    public ClassesAdapter(ArrayList<ClassModel> classesList, Activity activity) {
        this.classesList = classesList;
        this.context = activity;
        this.classItemCallbacks = (ClassItemCallbacks) activity;

    }

    @NonNull
    @Override
    public ClassesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ClassesHolder(LayoutInflater.from(context).inflate(R.layout.classes_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesHolder classesHolder, final int i) {
        ClassModel currentClass = classesList.get(i);
        String title = currentClass.getTitle();
        String session = currentClass.getSession();
        String classId = currentClass.getClassId();
        classesHolder.nameTextView.setText(title + " ( " + session + " )");

        classesHolder.mainContainerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classItemCallbacks.onCLassItemClick(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    class ClassesHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        LinearLayout mainContainerLinearLayout;

        public ClassesHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_classes_li_name);
            mainContainerLinearLayout = itemView.findViewById(R.id.ll_classess_li_main_container);
        }
    }
}
