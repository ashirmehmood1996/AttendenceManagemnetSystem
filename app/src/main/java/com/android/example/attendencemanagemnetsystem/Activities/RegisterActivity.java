package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final int RC_REGISTER = 101;
    private EditText numberEditText, codeEditText;
    private Button registerButton;
    Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initFields();

        attachListeners();
    }


    private void initFields() {
        numberEditText = findViewById(R.id.et_register_number);
        codeEditText = findViewById(R.id.et_register_code);
        registerButton = findViewById(R.id.bt_register_register);
        typeSpinner = findViewById(R.id.sp_register_type);
        //setSpinner
        setSpinner();
    }

    private void setSpinner() {
        ArrayAdapter typeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.user_type, R.layout.support_simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);
        if (typeSpinner.getSelectedItemPosition() == 0) codeEditText.setVisibility(View.GONE);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//then its admin
                    codeEditText.setVisibility(View.GONE);
                } else if (position == 1) {//its teacher
                    codeEditText.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void attachListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numebr = numberEditText.getText().toString();
                String type = "";
                if (typeSpinner.getSelectedItemPosition() == 0) {
                    type = "admin";
                } else {
                    type = "teacher";
                }
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
            } else {
                Toast.makeText(this, "login unsuccessfull", Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateDatabaseAndSendToReleventActivity() {

        HashMap<String, Object> userMap = new HashMap<>();
        final String type;
        if (typeSpinner.getSelectedItemPosition() == 0) {
            type = "admin";
        } else {
            type = "teacher";
            userMap.put("admin_id",codeEditText.getText().toString());// TODO: 5/31/2019 later check that if admin id exists or not
        }
        userMap.put("type", type);

        userMap.put("number", numberEditText.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendToRelevantActivity(type);
                } else {
                    Toast.makeText(RegisterActivity.this, "unable to register please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToRelevantActivity(String type) {
        if (type.equals("admin")) {
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, TeacherActivity.class));
            finish();
        }
    }
}
