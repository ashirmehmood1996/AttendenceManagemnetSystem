package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.example.attendencemanagemnetsystem.Adapters.ClassesAdapter;
import com.android.example.attendencemanagemnetsystem.R;

import java.util.ArrayList;

public class ClassesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> classesList;
    private ClassesAdapter classesAdapter;
    private FloatingActionButton addCalssFloatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        getSupportActionBar().setTitle("Classes");

        initFields();
        attachListeners();

        loadClasses();

    }


    private void initFields() {
        recyclerView = findViewById(R.id.rv_classes);
        classesList = new ArrayList<>();
        classesAdapter = new ClassesAdapter(classesList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classesAdapter);

        addCalssFloatingButton = findViewById(R.id.fab_classes_add_new);

    }

    private void attachListeners() {
        addCalssFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClassesActivity.this, AddClassActivity.class));
            }
        });
    }

    private void loadClasses() {
        // TODO: 5/24/2019 load clases from firebase
    }
}
