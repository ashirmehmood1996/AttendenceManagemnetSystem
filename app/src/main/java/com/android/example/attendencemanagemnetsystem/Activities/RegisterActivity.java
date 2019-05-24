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
import com.firebase.ui.auth.FirebaseUiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final int RC_REGISTER = 101;
    private EditText numberEditText, typeEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initFields();

        attachListeners();
    }


    private void initFields() {
        numberEditText = findViewById(R.id.et_register_number);
        typeEditText = findViewById(R.id.et_register_type);
        registerButton = findViewById(R.id.bt_register_register);
    }

    private void attachListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numebr = numberEditText.getText().toString();
                String type = typeEditText.getText().toString();
                if (numebr.isEmpty() || type.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "please fill out the fields first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type.equals("admin") || type.equals("teacher")) {
                    proceedToRegister();
                } else {
                    Toast.makeText(RegisterActivity.this, "invalid type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void proceedToRegister() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder()
                .setDefaultNumber(numberEditText.getText().toString()).build());
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build(), RC_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_REGISTER) {
            if (resultCode == RESULT_OK) {

                updateDatabaseAndSendToReleventActivity();
                // TODO: 5/24/2019 check user type and send to relevant acivity
                Toast.makeText(this, "Welcome user ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "login unsuccessfull", Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateDatabaseAndSendToReleventActivity() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("type", typeEditText.getText().toString());
        userMap.put("number", numberEditText.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendToRelevantActivity();
                } else {
                    Toast.makeText(RegisterActivity.this, "unable to register please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToRelevantActivity() {
        if (typeEditText.getText().toString().equals("admin")) {
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, TeacherActivity.class));
            finish();
        }
    }
}
