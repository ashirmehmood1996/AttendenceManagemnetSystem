package com.android.example.attendencemanagemnetsystem.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.attendencemanagemnetsystem.Models.StudentModel;
import com.android.example.attendencemanagemnetsystem.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentHolder> {
    ArrayList<StudentModel> studentModelArrayList;
    Context context;

    public StudentAdapter(ArrayList<StudentModel> studentModelArrayList, Context context) {
        this.studentModelArrayList = studentModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StudentHolder(LayoutInflater.from(context).inflate(R.layout.student_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentHolder studentHolder, int i) {
        StudentModel currentStudent = studentModelArrayList.get(i);
        String name = currentStudent.getName();
        String rollnumber = currentStudent.getRollNumber();

        studentHolder.nameTextView.setText(name);
        studentHolder.rollNumTextView.setText("roll number " + rollnumber);


    }

    @Override
    public int getItemCount() {
        return studentModelArrayList.size();
    }

    class StudentHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, rollNumTextView;

        StudentHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_std_list_li_name);
            rollNumTextView = itemView.findViewById(R.id.tv_std_list_li_roll_num);
        }
    }
}
