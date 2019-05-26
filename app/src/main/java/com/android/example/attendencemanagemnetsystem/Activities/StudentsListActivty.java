package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.Adapters.StudentAdapter;
import com.android.example.attendencemanagemnetsystem.Models.StudentModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentsListActivty extends AppCompatActivity {
    FloatingActionButton addStdFloatingButton;

    //recylcer view related
    private RecyclerView recyclerView;
    private ArrayList<StudentModel> studentArrayList;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list_activty);

        initFields();
        attachListeners();
        loadStudents();
    }


    private void initFields() {
        addStdFloatingButton = findViewById(R.id.fab_std_list_add_new);

        recyclerView = findViewById(R.id.rv_std_list);
        studentArrayList = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);
    }

    private void attachListeners() {
        addStdFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout addStudentLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_student, null);
                final EditText nameEditText = addStudentLayout.findViewById(R.id.et_dialog_add_std_name);
                final EditText rollNumEditText = addStudentLayout.findViewById(R.id.et_dialog_add_std_roll_num);
                // TODO: 5/26/2019  add the logic to add new students to the pool
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentsListActivty.this);
                builder.setView(addStudentLayout);
                builder.setTitle("Add student")
                        .setMessage("please provide the following details");
                builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rollNum = rollNumEditText.getText().toString().trim();
                        String name = nameEditText.getText().toString().trim();
                        if (rollNum.isEmpty() || name.isEmpty()) {
                            Toast.makeText(StudentsListActivty.this, "please fill out all the fields", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            addStudentToFirebase(name, rollNum);
                        }


                    }

                    private void addStudentToFirebase(String name, String rollNum) {
                        HashMap<String, Object> studentMap = new HashMap<>();
                        studentMap.put("name", name);
                        studentMap.put("roll_num", rollNum);
                        FirebaseDatabase.getInstance().getReference().child("students")

                                .push().setValue(studentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(StudentsListActivty.this, "successfully added ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }).setNegativeButton("cancel", null).show();

            }
        });
    }

    private void loadStudents() {
        FirebaseDatabase.getInstance().getReference().child("students")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String key = dataSnapshot.getKey();
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String rollNum = dataSnapshot.child("roll_num").getValue(String.class);

                        studentArrayList.add(new StudentModel(key, name, rollNum));
                        studentAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
