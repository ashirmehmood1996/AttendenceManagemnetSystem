package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
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
    private String circleCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        fetchCircleCode();

        getSupportActionBar().setTitle("Teacher..");
        takeAttendenceButton = findViewById(R.id.bt_teacher_take_attendence);
        //display time
        long time = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        String date = simpleDateFormat.format(time);
        ((TextView) findViewById(R.id.tv_teacher_date)).setText(date);
        setAttendenceButton();


    }

    private void fetchCircleCode() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("circle_code")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            circleCode = dataSnapshot.getValue(String.class);
                            setSpinner();
                        } else {
                            Toast.makeText(TeacherActivity.this, "failded to fetch circle code", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setAttendenceButton() {

        takeAttendenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleCode == null || circleCode.isEmpty()) {
                    Toast.makeText(TeacherActivity.this, "circle code was not fetched please try again in few seconds", Toast.LENGTH_SHORT).show();
                    return;
                }
                ClassModel classModel = (ClassModel) classesSpinner.getSelectedItem();
                Intent intent = new Intent(TeacherActivity.this, AttendenceActivity.class);
                intent.putExtra("class", classModel);
                intent.putExtra("circle_code", circleCode);
                startActivity(intent);
            }
        });
    }

    private void setSpinner() {
        classesSpinner = findViewById(R.id.sp_teacher_pick_class);
        loadClassesAndAddToSpinner();

    }

    private void loadClassesAndAddToSpinner() {

        if (circleCode == null || circleCode.isEmpty()) {
            Toast.makeText(TeacherActivity.this, "circle code was not fetched please try again in few seconds", Toast.LENGTH_SHORT).show();
            return;
        }
// TODO: 6/1/2019 later query this if possible for now getting all teachers
        FirebaseDatabase.getInstance().getReference()
                .child("circles").child(circleCode)
                .child("classes")
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
                        if (classesArray.isEmpty()) {
                            Toast.makeText(TeacherActivity.this, "you are not assigned any classes by the admin", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(TeacherActivity.this, "classes loaded successfully", Toast.LENGTH_SHORT).show();
                            takeAttendenceButton.setEnabled(true);
                        }
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
//   assiciate the subject in the student teacher relation
//  add more fields to the firebase so that app can be used in real institutions
//  polish the UI in colouring , specially attendecne taking park HINT: get help from the school managemnet app




