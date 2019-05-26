package com.android.example.attendencemanagemnetsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.example.attendencemanagemnetsystem.Activities.TeachersListActivity;
import com.android.example.attendencemanagemnetsystem.Interfaces.TeacherItemCallbacks;
import com.android.example.attendencemanagemnetsystem.Models.TeacherModel;
import com.android.example.attendencemanagemnetsystem.R;

import java.util.ArrayList;

public class TeachersListAdapter extends RecyclerView.Adapter<TeachersListAdapter.TeacherHolder> {
    private ArrayList<TeacherModel> teachersArrayList;
    private Context context;
    private TeacherItemCallbacks teacherItemCallbacks;

    public TeachersListAdapter(ArrayList<TeacherModel> teachersArrayList, Activity activity) {
        this.teachersArrayList = teachersArrayList;
        this.context = activity;
        teacherItemCallbacks = (TeacherItemCallbacks) activity;
    }

    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TeacherHolder(LayoutInflater.from(context).inflate(R.layout.teacher_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TeacherHolder teacherHolder, final int i) {
        TeacherModel teacherModel = teachersArrayList.get(i);
        String name = teacherModel.getTeacherName();
        teacherHolder.nameTextView.setText(name);

        if (TeachersListActivity.isInActonMode) {
            teacherHolder.checkBox.setVisibility(View.VISIBLE);
            if (teacherModel.isSelected()) teacherHolder.checkBox.setChecked(true);
            else teacherHolder.checkBox.setChecked(false);
        } else {
            teacherHolder.checkBox.setVisibility(View.GONE);
        }

        teacherHolder.containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teacherHolder.checkBox.isChecked()) {
                    teacherHolder.checkBox.setChecked(false);

                } else {
                    teacherHolder.checkBox.setChecked(true);
                }
                teacherItemCallbacks.onTeacherClciked(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return teachersArrayList.size();
    }

    class TeacherHolder extends RecyclerView.ViewHolder {
        LinearLayout containerLayout;
        TextView nameTextView;
        CheckBox checkBox;

        TeacherHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_teacher_list_li_name);
            containerLayout = itemView.findViewById(R.id.ll_teacher_list_li_container);
            checkBox = itemView.findViewById(R.id.cb_teacher_list_li_checkbox);

        }
    }
}
