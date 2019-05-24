package com.android.example.attendencemanagemnetsystem.Activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.example.attendencemanagemnetsystem.Adapters.TeachersListAdapter;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TeachersListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TeachersListAdapter teachersListAdapter;
    private ArrayList<String> teachersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_list);
        initFields();
        loadTeachers();
    }


    private void initFields() {
        recyclerView = findViewById(R.id.rv_teachers_list);
        teachersList = new ArrayList<>();
        teachersListAdapter = new TeachersListAdapter(teachersList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teachersListAdapter);
    }

    private void loadTeachers() {
        // TODO: 5/24/2019 load teachers from users where teacher is type
        FirebaseDatabase.getInstance().getReference().child("users")
                .orderByChild("type").equalTo("teacher").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String number = dataSnapshot.child("number").getValue(String.class);
                teachersList.add(number);
                teachersListAdapter.notifyDataSetChanged();
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
