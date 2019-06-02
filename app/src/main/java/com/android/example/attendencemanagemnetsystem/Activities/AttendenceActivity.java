package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.Adapters.AttendenceAdapter;
import com.android.example.attendencemanagemnetsystem.Interfaces.StudentItemCallbacks;
import com.android.example.attendencemanagemnetsystem.Models.ClassModel;
import com.android.example.attendencemanagemnetsystem.Models.StudentModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AttendenceActivity extends AppCompatActivity implements StudentItemCallbacks {
    private TextView titleTextView, sessionTextView, dateTextView;
    private RecyclerView recyclerView;
    private ArrayList<StudentModel> studentModelArrayList;
    private AttendenceAdapter attendenceAdapter;
    private Button submitButton;

    private ClassModel classModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        initFields();
        classModel = getIntent().getParcelableExtra("class");
        setAndLoadInitialValues();
        attachListeners();

    }


    private void setAndLoadInitialValues() {
        titleTextView.setText(classModel.getTitle());
        sessionTextView.setText(classModel.getSession());
//date related
        long time = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        String date = simpleDateFormat.format(time);
        dateTextView.setText(date);

        loadStudents(classModel.getClassId());
    }

    private void loadStudents(String classId) {
        FirebaseDatabase.getInstance().getReference().child("classes")
                .child(classId).child("students")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> childIdsList = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot stdSnap : dataSnapshot.getChildren()) {
                                {
                                    childIdsList.add(stdSnap.getKey());
                                }
                            }
                            loadChildrens(childIdsList);
                        } else {
                            Toast.makeText(AttendenceActivity.this, "no students added in this class", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void loadChildrens(ArrayList<String> childIdsList) {
        for (String childId : childIdsList) {
            FirebaseDatabase.getInstance().getReference().child("students")
                    .child(childId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String id = dataSnapshot.getKey();
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String rollNum = dataSnapshot.child("roll_num").getValue(String.class);
                        StudentModel studentModel = new StudentModel(id, name, rollNum);
                        studentModel.setSelected(true);
                        studentModelArrayList.add(studentModel);
                        attendenceAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AttendenceActivity.this, "student was not found with the specified key", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void initFields() {
        titleTextView = findViewById(R.id.tv_attendence_title);
        sessionTextView = findViewById(R.id.tv_attendence_session);
        dateTextView = findViewById(R.id.tv_attendence_date);
        submitButton = findViewById(R.id.bt_attendence_submit);

        //recycler view related
        recyclerView = findViewById(R.id.rv_attendence_std_list);
        studentModelArrayList = new ArrayList<>();
        attendenceAdapter = new AttendenceAdapter(studentModelArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(attendenceAdapter);
    }

    private void attachListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AttendenceActivity.this)
                        .setTitle("Confirm?")
                        .setMessage("Are you sure to submit this attendence")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                submitToFireBase();

                                // TODO: 6/2/2019 make all the studenst fade and unresponsive along with submit
                                //  option and display only an option for exporting to exel sheet the attrendence of current month or any other may be . it should be thinked later
                            }
                        }).setNegativeButton("cancel", null)
                        .show();

            }
        });

    }

    private void submitToFireBase() {
        HashMap<String, Object> attendanceMap = new HashMap<>();
        // TODO: 6/2/2019  add subject functionality too

        for (StudentModel studentModel : studentModelArrayList) {
            HashMap<String, Object> subMap = new HashMap<>();
            subMap.put("name", studentModel.getName());
            subMap.put("roll_num", studentModel.getRollNumber());
            subMap.put("status", studentModel.isSelected());
            attendanceMap.put(studentModel.getStudentId(), subMap);
        }


        //// TODO: 6/2/2019  later make it dynamic and each organization has uts record separate from others
        FirebaseDatabase.getInstance().getReference().child("attendances")
                .child(classModel.getClassId()).child(Calendar.getInstance().getTimeInMillis() + "").setValue(attendanceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AttendenceActivity.this, "successfully added to the databse", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public synchronized void onStudentClicked(int position) {
        Toast.makeText(this, studentModelArrayList.get(position).getName() + " is clicked", Toast.LENGTH_SHORT).show();
        if (studentModelArrayList.get(position).isSelected()) {
            studentModelArrayList.get(position).setSelected(false);
        } else {
            studentModelArrayList.get(position).setSelected(true);
        }
    }
}
