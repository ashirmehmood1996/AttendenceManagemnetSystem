package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.example.attendencemanagemnetsystem.Adapters.ClassesAdapter;
import com.android.example.attendencemanagemnetsystem.Interfaces.ClassItemCallbacks;
import com.android.example.attendencemanagemnetsystem.Models.ClassModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClassesListActivity extends AppCompatActivity implements ClassItemCallbacks {
    private RecyclerView recyclerView;
    private ArrayList<ClassModel> classesArrayList;
    private ClassesAdapter classesAdapter;
    private FloatingActionButton addCalssFloatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        getSupportActionBar().setTitle("Classes");

        getSupportActionBar().setTitle("Classes");

        initFields();
        attachListeners();


    }


    private void initFields() {
        recyclerView = findViewById(R.id.rv_classes);
        classesArrayList = new ArrayList<>();
        classesAdapter = new ClassesAdapter(classesArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classesAdapter);

        addCalssFloatingButton = findViewById(R.id.fab_classes_add_new_class);

    }

    private void attachListeners() {
        addCalssFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClassesListActivity.this, AddClassActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadClasses();
    }

    private void loadClasses() {
        if (classesArrayList.size() > 0)
            classesArrayList.clear();
        FirebaseDatabase.getInstance().getReference()
                .child("classes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();
                String title = dataSnapshot.child("title").getValue(String.class);
                String session = dataSnapshot.child("session").getValue(String.class);

                classesArrayList.add(new ClassModel(id, title, session));
                classesAdapter.notifyDataSetChanged();
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

    @Override
    public void onCLassItemClick(int position) {

        //start detail activity and populate wth the currunt class item
        ClassModel classModel = classesArrayList.get(position);
        String classId = classModel.getClassId();
        String title = classModel.getTitle();
        String session = classModel.getSession();
        Intent intent = new Intent(this, ClassDetailsActivity.class);
        intent.putExtra("classId", classId)
                .putExtra("session", session)
                .putExtra("title", title);
        startActivity(intent);
    }
}
