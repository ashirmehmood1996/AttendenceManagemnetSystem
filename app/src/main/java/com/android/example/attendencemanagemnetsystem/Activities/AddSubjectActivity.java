package com.android.example.attendencemanagemnetsystem.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.Models.SubjectModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AddSubjectActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText titleEditText, idEditText;
    private TextView totalCountTextView;
    private Button addMoreButton, doneButton;
    private ArrayList<SubjectModel> subjectModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        getSupportActionBar().setTitle("Assign Subjects");

        initFields();
        attachListeners();


    }

    private void initFields() {
        titleEditText = findViewById(R.id.et_add_subject_title);
        idEditText = findViewById(R.id.et_add_subject_id);
        totalCountTextView = findViewById(R.id.tv_add_subject_total_count);
        addMoreButton = findViewById(R.id.bt_add_subject_more);
        doneButton = findViewById(R.id.bt_add_subject_done);
    }


    private void attachListeners() {
        addMoreButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_subject_more:
                String name = titleEditText.getText().toString().trim();
                String subjectID = idEditText.getText().toString().trim();
                subjectModelArrayList.add(new SubjectModel(name, subjectID));

                titleEditText.setText("");
                idEditText.setText("");
                totalCountTextView.setText(subjectModelArrayList.size() + " subjects added");
                break;
            case R.id.bt_add_subject_done:

                break;
        }
    }
}
