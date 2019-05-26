package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.Models.TeacherModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView titleTextView, sessionTextView;
    private Button addTeacherButton, addStudentButton;
    private int RC_SELECT_TEACHERS = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);

        initFields();
        attachListeners();

        setDataTofields(getIntent());

    }


    private void initFields() {
        sessionTextView = findViewById(R.id.tv_class_item_details_session);
        titleTextView = findViewById(R.id.tv_class_item_details_title);
        addStudentButton = findViewById(R.id.bt_class_item_details_add_student);
        addTeacherButton = findViewById(R.id.bt_class_item_details_add_teacher);

    }

    private void attachListeners() {
        addTeacherButton.setOnClickListener(this);
        addStudentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_class_item_details_add_teacher:
                startActivityForResult(new Intent(this, TeachersListActivity.class).putExtra("select", true), RC_SELECT_TEACHERS);
                Toast.makeText(this, "add teacher ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_class_item_details_add_student:
                Toast.makeText(this, "add student ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SELECT_TEACHERS) {
            if (resultCode == RESULT_OK) {
                ArrayList<TeacherModel> selectedTeachersList = data.getParcelableArrayListExtra("selected_teachers");

                addTeachersToClass(selectedTeachersList);
                Toast.makeText(this, selectedTeachersList.size() + " number of teachers selected", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "teacher selection was cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addTeachersToClass(final ArrayList<TeacherModel> selectedTeachersList) { //note duplicate teachers wont be added automatically as the new duplicated will override the old one
        HashMap<String, Object> teachersMap = new HashMap<>();
        for (TeacherModel currentTeacher : selectedTeachersList) {
            teachersMap.put(currentTeacher.getTeacher_id(), currentTeacher.getTeacherName());
        }
        FirebaseDatabase.getInstance().getReference()
                .child("classes").child(getIntent().getStringExtra("classId"))
                .child("teachers").updateChildren(teachersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ClassDetailsActivity.this, selectedTeachersList.size() + " teahers added  to database", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void setDataTofields(Intent intent) {
        if (intent.hasExtra("title")) {
            titleTextView.setText(intent.getStringExtra("title"));
        }
        if (intent.hasExtra("session")) {
            sessionTextView.setText(intent.getStringExtra("session"));
        }

    }

}
