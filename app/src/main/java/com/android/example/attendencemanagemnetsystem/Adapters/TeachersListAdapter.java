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

public class TeachersListAdapter extends RecyclerView.Adapter<TeachersListAdapter.TeacherHolder> {
    private ArrayList<String> teachersArrayList;
    private Context context;

    public TeachersListAdapter(ArrayList<String> teachersArrayList, Context context) {
        this.teachersArrayList = teachersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TeacherHolder(LayoutInflater.from(context).inflate(R.layout.teacher_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder teacherHolder, int i) {
        String name = teachersArrayList.get(i);
        teacherHolder.nameTextView.setText(name);

    }

    @Override
    public int getItemCount() {
        return teachersArrayList.size();
    }

    class TeacherHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public TeacherHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_teacher_list_li_name);
        }
    }
}
