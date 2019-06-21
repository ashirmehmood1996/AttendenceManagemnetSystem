package com.android.example.attendencemanagemnetsystem.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddClassActivity extends AppCompatActivity {
    private EditText titleEditText, sessionEditText;
    private Button createClassButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        getSupportActionBar().setTitle("Add Class");

        initFields();
        attachListeners();

    }


    private void initFields() {
        titleEditText = findViewById(R.id.et_add_class_title);
        sessionEditText = findViewById(R.id.et_add_class_session);
        createClassButton = findViewById(R.id.bt_add_class_create);
    }

    private void attachListeners() {
        createClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String session = sessionEditText.getText().toString();
                if (session.isEmpty() || title.isEmpty()) {
                    Toast.makeText(AddClassActivity.this, "please fill out the fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    HashMap<String, Object> classMap = new HashMap<>();
                    classMap.put("title",title);
                    classMap.put("session",session);
                    FirebaseDatabase.getInstance().getReference()
                            .child("classes").push()
                            .setValue(classMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddClassActivity.this, "new class added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddClassActivity.this, "failed to add new class", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}
