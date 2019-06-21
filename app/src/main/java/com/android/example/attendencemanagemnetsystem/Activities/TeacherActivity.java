package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.Adapters.ClassesSpinnerAdapter;
import com.android.example.attendencemanagemnetsystem.Models.ClassModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TeacherActivity extends AppCompatActivity {
    private Spinner classesSpinner;
    private Button takeAttendenceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        getSupportActionBar().setTitle("Teacher..");

        takeAttendenceButton = findViewById(R.id.bt_teacher_take_attendence);
        setSpinner();


        //display time
        long time = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        String date = simpleDateFormat.format(time);
        ((TextView) findViewById(R.id.tv_teacher_date)).setText(date);


        setAttendenceButton();


    }

    private void setAttendenceButton() {

        takeAttendenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClassModel classModel = (ClassModel) classesSpinner.getSelectedItem();
                Intent intent = new Intent(TeacherActivity.this, AttendenceActivity.class);
                intent.putExtra("class", classModel);
                startActivity(intent);
            }
        });
    }

    private void setSpinner() {
        classesSpinner = findViewById(R.id.sp_teacher_pick_class);
        loadClassesAndAddToSpinner();

    }

    private void loadClassesAndAddToSpinner() {
// TODO: 6/1/2019 later query this if possible for now getting all teachers
        FirebaseDatabase.getInstance().getReference().child("classes")
                /*.orderByChild("teachers/" + FirebaseAuth.getInstance().getCurrentUser().getUid())*//*.equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())*/
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<ClassModel> classesArray = new ArrayList<>();
                        for (DataSnapshot classSnap : dataSnapshot.getChildren()) {


                            if (classSnap.child("teachers").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                String title = classSnap.child("title").getValue(String.class);
                                String classId = classSnap.getKey();
                                String classSession = classSnap.child("session").getValue(String.class);
                                classesArray.add(new ClassModel(classId, title, classSession));
                            }
                        }
                        ClassesSpinnerAdapter classesSpinnerAdapter = new ClassesSpinnerAdapter(TeacherActivity.this, classesArray);

//                        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(TeacherActivity.this, android.R.layout.simple_spinner_item, classesArray);
//                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        classesSpinner.setAdapter(spinnerAdapter);
                        classesSpinner.setAdapter(classesSpinnerAdapter);
                        takeAttendenceButton.setEnabled(true);
                        // TODO: 6/1/2019 add loading screen


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.nav_teacher_logout) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(TeacherActivity.this, "logout successfull", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(TeacherActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "already logged out", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

// TODO: 6/18/2019  now
//  tweek the database so that each admin has its users in its own node in firebase and also assiciate the subject in the student teacher relation
//  add more fields to the firebase so that app can be used in real institutions
//  polish the UI in colouring , specially attendecne taking park HINT: get help from the school managemnet app




