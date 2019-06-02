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

import com.android.example.attendencemanagemnetsystem.Interfaces.StudentItemCallbacks;
import com.android.example.attendencemanagemnetsystem.Models.StudentModel;
import com.android.example.attendencemanagemnetsystem.R;

import java.util.ArrayList;

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.AttendenceHolder> {
    ArrayList<StudentModel> studentModelArrayList;
    Context context;
    StudentItemCallbacks studentItemCallbacks;

    public AttendenceAdapter(ArrayList<StudentModel> studentModelArrayList, Activity activity) {
        this.studentModelArrayList = studentModelArrayList;
        this.context = activity;
        this.studentItemCallbacks= (StudentItemCallbacks) activity;
    }

    @NonNull
    @Override
    public AttendenceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AttendenceHolder(LayoutInflater.from(context).inflate(R.layout.attendence_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendenceHolder attendenceHolder, final int i) {
        final StudentModel studentModel = studentModelArrayList.get(i);

        if (studentModel.isSelected()) {
            attendenceHolder.checkBox.setChecked(true);
        } else {
            attendenceHolder.checkBox.setChecked(false);
        }

        attendenceHolder.nameTextView.setText(studentModel.getName());

        attendenceHolder.containerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendenceHolder.checkBox.isChecked()) {
                    attendenceHolder.checkBox.setChecked(false);
                    studentModel.setSelected(false);
                } else {
                    attendenceHolder.checkBox.setChecked(true);
                    studentModel.setSelected(true);
                }
                //studentItemCallbacks.onStudentClicked(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return studentModelArrayList.size();
    }

    class AttendenceHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        CheckBox checkBox;
        LinearLayout containerLinearLayout;

        public AttendenceHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_attend_li_name);
            checkBox = itemView.findViewById(R.id.cb_attend_li);
            containerLinearLayout = itemView.findViewById(R.id.ll_attend_li_container);
        }
    }
}
