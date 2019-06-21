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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final int RC_REGISTER = 101;
    private EditText numberEditText, codeEditText;
    private LinearLayout registerationCodeContainerLinearLayout;
    private Button registerButton;
    private Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register..");

        initFields();

        attachListeners();
    }


    private void initFields() {
        numberEditText = findViewById(R.id.et_register_number);
        registerationCodeContainerLinearLayout = findViewById(R.id.ll_register_code_container);
        codeEditText = findViewById(R.id.et_register_code);
        registerButton = findViewById(R.id.bt_register_register);
        typeSpinner = findViewById(R.id.sp_register_type);
        //setSpinner
        setSpinner();
    }

    private void setSpinner() {
        ArrayAdapter typeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.user_type, R.layout.support_simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);
        if (typeSpinner.getSelectedItemPosition() == 0)
            registerationCodeContainerLinearLayout.setVisibility(View.GONE);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//then its admin
                    registerationCodeContainerLinearLayout.setVisibility(View.GONE);
                } else if (position == 1) {//its teacher
                    registerationCodeContainerLinearLayout.setVisibility(View.VISIBLE);
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
                //perform necessary checks
                String numebr = numberEditText.getText().toString();// TODO: 6/21/2019 check that of number is in valid format take help from tracker
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
                    checkAvailabilityOfAdminAndNumber(numebr, type);
                } else {
                    Toast.makeText(RegisterActivity.this, "invalid type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAvailabilityOfAdminAndNumber(String numebr, final String type) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .orderByChild("number").equalTo(numebr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    if (type.equals("admin")) {
                        proceedToRegister();
                    } else {
                        checkValidifyOfCode();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "this number is already registered", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkValidifyOfCode() {
        FirebaseDatabase.getInstance().getReference()
                .child("users").orderByChild("circle_code")
                .equalTo(codeEditText.getText().toString().trim())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            proceedToRegister();
                        } else {
                            Toast.makeText(RegisterActivity.this, "INVALID code , please try another code", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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
            // TODO: 6/21/2019  later check if that user has already created an account
            type = "admin";
            userMap.put("circle_code", FirebaseAuth.getInstance().getCurrentUser().getUid());
//            userMap.put("type", type); type is removed because admins are in admin node and teachers are in teachers node

        } else {
            type = "teacher";
            userMap.put("circle_code", codeEditText.getText().toString());
        }
        userMap.put("number", numberEditText.getText().toString());
        userMap.put("type", type);

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

            FirebaseDatabase.getInstance().getReference()
                    .child("circles").child(codeEditText.getText().toString().trim())//as admins user id is circle code
                    .child("teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(numberEditText.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(RegisterActivity.this, TeacherActivity.class));
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, "something went wrong please contact the app developers", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


        }
    }
}
