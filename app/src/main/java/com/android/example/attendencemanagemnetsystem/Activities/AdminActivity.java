package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    private Button teachersButton, classesButton, studentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().setTitle("Welcome Admin");

        initFields();
        attachListeners();
    }

    private void initFields() {
        classesButton = findViewById(R.id.bt_admin_classes);
        teachersButton = findViewById(R.id.bt_admin_teachers);
        studentsButton = findViewById(R.id.bt_admin_students);
    }

    private void attachListeners() {
        classesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableClasses();

            }
        });
        teachersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableTeachersData();
            }
        });
        studentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, StudentsListActivty.class));
            }
        });
    }

    private void showAvailableClasses() {
        startActivity(new Intent(this, ClassesActivity.class));

    }

    private void showAvailableTeachersData() {
        startActivity(new Intent(this, TeachersListActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.nav_admin_logout) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminActivity.this, "logout successfull", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "already logged out", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.nav_admin_share_registration_code) {
            // TODO: 5/24/2019 later sharer using a dialogue with copy option and share option
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("registerationCode", FirebaseAuth.getInstance().getCurrentUser().getUid());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Copied to clip board", Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
