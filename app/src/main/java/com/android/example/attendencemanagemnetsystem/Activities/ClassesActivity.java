package com.android.example.attendencemanagemnetsystem.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.example.attendencemanagemnetsystem.Adapters.ClassesAdapter;
import com.android.example.attendencemanagemnetsystem.R;

import java.util.ArrayList;

public class ClassesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> classesList;
    private ClassesAdapter classesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        getSupportActionBar().setTitle("Classes");

        initFields();

        loadClasses();

    }


    private void initFields() {
        recyclerView = findViewById(R.id.rv_classes);
        classesList = new ArrayList<>();
        classesAdapter = new ClassesAdapter(classesList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classesAdapter);
    }

    private void loadClasses() {
        // TODO: 5/24/2019 load clases from firebase
    }
}
