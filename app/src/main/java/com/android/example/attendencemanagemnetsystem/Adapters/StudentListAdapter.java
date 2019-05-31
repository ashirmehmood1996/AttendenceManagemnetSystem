package com.android.example.attendencemanagemnetsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.preference.TwoStatePreference;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.example.attendencemanagemnetsystem.Activities.StudentsListActivty;
import com.android.example.attendencemanagemnetsystem.Activities.TeachersListActivity;
import com.android.example.attendencemanagemnetsystem.Interfaces.StudentItemCallbacks;
import com.android.example.attendencemanagemnetsystem.Models.StudentModel;
import com.android.example.attendencemanagemnetsystem.R;

import java.util.ArrayList;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentHolder> {
    private ArrayList<StudentModel> studentModelArrayList;
    private Context context;
    private StudentItemCallbacks studentItemCallbacks;

    public StudentListAdapter(ArrayList<StudentModel> studentModelArrayList, Activity activity) {
        this.studentModelArrayList = studentModelArrayList;
        this.context = activity;
        this.studentItemCallbacks = (StudentItemCallbacks) activity;
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StudentHolder(LayoutInflater.from(context).inflate(R.layout.student_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentHolder studentHolder, final int i) {
        StudentModel currentStudent = studentModelArrayList.get(i);
        String name = currentStudent.getName();
        String rollnumber = currentStudent.getRollNumber();

        studentHolder.nameTextView.setText(name);
        studentHolder.rollNumTextView.setText("roll number " + rollnumber);

        if (StudentsListActivty.isInActonMode) {
            studentHolder.checkBox.setVisibility(View.VISIBLE);
            if (currentStudent.isSelected()) studentHolder.checkBox.setChecked(true);
            else studentHolder.checkBox.setChecked(false);
        } else {
            studentHolder.checkBox.setVisibility(View.GONE);
        }
        studentHolder.containerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentHolder.checkBox.isChecked()) {
                    studentHolder.checkBox.setChecked(false);

                } else {
                    studentHolder.checkBox.setChecked(true);
                }
                studentItemCallbacks.onStudentClicked(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return studentModelArrayList.size();
    }

    class StudentHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView nameTextView, rollNumTextView;
        LinearLayout containerLinearLayout;

        StudentHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_std_list_li_name);
            rollNumTextView = itemView.findViewById(R.id.tv_std_list_li_roll_num);
            containerLinearLayout = itemView.findViewById(R.id.ll_std_list_li_container);
            checkBox = itemView.findViewById(R.id.cb_std_list_li);
        }
    }
}
