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

import com.android.example.attendencemanagemnetsystem.Models.StudentModel;
import com.android.example.attendencemanagemnetsystem.Models.TeacherModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SELECT_STUDENTS = 20000;
    private TextView titleTextView, sessionTextView, stdListTextView, teachersListTextView;

    private Button addTeacherButton, addStudentButton;

    private int RC_SELECT_TEACHERS = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);

        getSupportActionBar().setTitle("Class Details");

        initFields();
        attachListeners();
        setDataTofields(getIntent());

    }


    private void initFields() {
        sessionTextView = findViewById(R.id.tv_class_item_details_session);
        titleTextView = findViewById(R.id.tv_class_item_details_title);
        addStudentButton = findViewById(R.id.bt_class_item_details_add_student);
        addTeacherButton = findViewById(R.id.bt_class_item_details_add_teacher);
        stdListTextView = findViewById(R.id.tv_class_item_details_students_list);
        teachersListTextView = findViewById(R.id.tv_class_item_details_teachers_list);

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

                break;
            case R.id.bt_class_item_details_add_student:

                startActivityForResult(new Intent(this, StudentsListActivty.class).putExtra("select", true), RC_SELECT_STUDENTS);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SELECT_TEACHERS) {
            if (resultCode == RESULT_OK) {
                ArrayList<TeacherModel> selectedTeachersList = data.getParcelableArrayListExtra("selected_teachers");
                if (selectedTeachersList.size() == 0)
                    Toast.makeText(this, "No teacher was selected", Toast.LENGTH_SHORT).show();
                else
                    addTeachersToClass(selectedTeachersList);

            } else {
                Toast.makeText(this, "teacher selection was cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == RC_SELECT_STUDENTS) {
            if (resultCode == RESULT_OK) {
                ArrayList<StudentModel> selectedStudentsList = data.getParcelableArrayListExtra("selected_teachers");
                if (selectedStudentsList.size() == 0)
                    Toast.makeText(this, "No teacher was selected", Toast.LENGTH_SHORT).show();
                else
                    addStudentsToClass(selectedStudentsList);

            } else {
                Toast.makeText(this, "teacher selection was cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void addStudentsToClass(final ArrayList<StudentModel> studentModelArrayList) { //note duplicate teachers wont be added automatically as the new duplicated will override the old one
        HashMap<String, Object> studentsMAp = new HashMap<>();
        for (StudentModel currentStudent : studentModelArrayList) {
            studentsMAp.put(currentStudent.getStudentId(), currentStudent.getName());
        }
        FirebaseDatabase.getInstance().getReference()
                .child("classes").child(getIntent().getStringExtra("classId"))
                .child("students").updateChildren(studentsMAp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ClassDetailsActivity.this, studentModelArrayList.size() + " Students added  to database", Toast.LENGTH_SHORT).show();

                }
            }
        });

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

        String classId = intent.getStringExtra("classId");
        loadTeachers(classId);
        loadStudents(classId);

    }


    private void loadTeachers(String classId) {
        FirebaseDatabase.getInstance().getReference()
                .child("classes").child(classId)
                .child("teachers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long total = dataSnapshot.getChildrenCount();
                    String teachers = "total teachers : " + total+"\n";
                    for (DataSnapshot teacherSnap : dataSnapshot.getChildren()) {
                        teachers = teachers + "\n" + teacherSnap.getValue(String.class);

                    }
                    teachersListTextView.setText(teachers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadStudents(String classId) {
        FirebaseDatabase.getInstance().getReference()
                .child("classes").child(classId)
                .child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long total = dataSnapshot.getChildrenCount();
                    String students = "total Students : " + total+"\n";
                    for (DataSnapshot teacherSnap : dataSnapshot.getChildren()) {
                        students = students + "\n" + teacherSnap.getValue(String.class);
                    }
                    stdListTextView.setText(students);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
