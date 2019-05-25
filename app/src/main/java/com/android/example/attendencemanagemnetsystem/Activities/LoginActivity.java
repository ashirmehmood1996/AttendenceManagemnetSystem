package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_LOGIN = 1001;

    private EditText numberEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFields();
        attachListeners();
    }


    private void initFields() {
        numberEditText = findViewById(R.id.et_login_number);
        loginButton = findViewById(R.id.bt_login_login);
        registerButton = findViewById(R.id.bt_login_register);
    }

    private void attachListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!numberEditText.getText().toString().isEmpty()) {
                    //// TODO: 5/24/2019 later check that if this account exists in database
                    proceedToLogin();
                } else {
                    Toast.makeText(LoginActivity.this, "please provide a number", Toast.LENGTH_SHORT).show();
                }

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                //LoginActivity.this.finish();
            }
        });
    }

    private void proceedToLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder()
                .setDefaultNumber(numberEditText.getText().toString()).build());
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build(), RC_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_LOGIN) {
            if (resultCode == RESULT_OK) {
                sendUserToRelevantActiviy();
                Toast.makeText(this, "Welcome user ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "login unsuccessfull", Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendUserToRelevantActiviy() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("type").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String type = dataSnapshot.getValue(String.class);
                    if (type.equals("admin")) {
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(LoginActivity.this, TeacherActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
