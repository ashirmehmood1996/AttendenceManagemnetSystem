package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.example.attendencemanagemnetsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set action bar
    }

    @Override
    protected void onStart() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            sendToLogin();
        } else {
            sendToRelevantActivity();
        }
        super.onStart();
    }

    private void sendToRelevantActivity() {

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("type").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String type = dataSnapshot.getValue(String.class);
                    if (type.equals("admin")) {
                        startActivity(new Intent(MainActivity.this, AdminActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, TeacherActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }


}
