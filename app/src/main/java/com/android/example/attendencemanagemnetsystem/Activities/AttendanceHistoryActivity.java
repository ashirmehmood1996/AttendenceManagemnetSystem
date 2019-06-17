package com.android.example.attendencemanagemnetsystem.Activities;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.Adapters.ClassesSpinnerAdapter;
import com.android.example.attendencemanagemnetsystem.Models.ClassModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AttendanceHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner classSpinner;
    private TextView dataTextView;
    private Button pickDateButton;
    private Button displayAttendenceButton;
    private long selectedTimeInMilliSeconds;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);
        initFields();
        setDefaultDate();
        loadClassesAndAddToSpinner();
        attachListenersButton();

    }


    private void attachListenersButton() {
        pickDateButton.setOnClickListener(this);
        displayAttendenceButton.setOnClickListener(this);
    }

    private void initFields() {
        classSpinner = findViewById(R.id.sp_attendance_history_class);
        dataTextView = findViewById(R.id.tv_attendance_history_date_holder);
        pickDateButton = findViewById(R.id.bt_attendance_history_pick_date);
        displayAttendenceButton = findViewById(R.id.bt_attendance_history_display_attendence);
        tableLayout = findViewById(R.id.tl_attendance_history);

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
                            String title = classSnap.child("title").getValue(String.class);
                            String classId = classSnap.getKey();
                            String classSession = classSnap.child("session").getValue(String.class);
                            classesArray.add(new ClassModel(classId, title, classSession));
                        }
                        ClassesSpinnerAdapter classesSpinnerAdapter = new ClassesSpinnerAdapter(AttendanceHistoryActivity.this, classesArray);

//                        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(TeacherActivity.this, android.R.layout.simple_spinner_item, classesArray);
//                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        classesSpinner.setAdapter(spinnerAdapter);
                        classSpinner.setAdapter(classesSpinnerAdapter);
                        displayAttendenceButton.setEnabled(true);
                        // TODO: 6/1/2019 add loading screen
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        setTimetoStartOfDay(calendar);
        selectedTimeInMilliSeconds = calendar.getTimeInMillis();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        setDateOnDateTextView(dayOfMonth, month, year);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_attendance_history_pick_date:
                showDatePickerDialogue();
                break;
            case R.id.bt_attendance_history_display_attendence:
                diaplayAttendencce();
                break;
        }
    }

    //method to get the current data in the textView and let the user change it if desired
    private void showDatePickerDialogue() {
        //creating a date picker dialogue
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setDateOnDateTextView(dayOfMonth, month, year);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setTimetoStartOfDay(calendar);
                        selectedTimeInMilliSeconds = calendar.getTimeInMillis();
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR), //default year to be shown at the time of selection
                Calendar.getInstance().get(Calendar.MONTH), //default year to be shown at the time of selection
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)); //default year to be shown at the time of selection
        datePickerDialog.show();
    }

    private void setTimetoStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    //this method is to set date to the date textview
    private void setDateOnDateTextView(int day, int month, int year) {
        month++; // FIXME: 6/2/2019 why this was done
        dataTextView.setText(day + " / " + month + " / " + year);
    }

    private void diaplayAttendencce() {
        // TODO: 6/4/2019  add a field in attendence to know that which teacher took the attendence
        // TODO: 6/2/2019  empty all the zeros at the end may be at string and retriving both t make a query that can take attendence of a single day

        String classId = ((ClassModel) classSpinner.getSelectedItem()).getClassId();
        FirebaseDatabase.getInstance().getReference().child("attendances")
                .child(classId).child(selectedTimeInMilliSeconds + "")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {


                            StringBuilder builder = new StringBuilder();
                            for (DataSnapshot attendenceSnap : dataSnapshot.getChildren()) {
                                TableRow tableRow =
                                        (TableRow) LayoutInflater.from(AttendanceHistoryActivity.this).inflate(R.layout.attendence_history_list_item, tableLayout, false);
                                String name = attendenceSnap.child("name").getValue(String.class);
                                String rollNumber = attendenceSnap.child("roll_num").getValue(String.class);
                                boolean status = attendenceSnap.child("status").getValue(Boolean.class);
                                ((TextView) tableRow.findViewById(R.id.tv_att_history_list_item_roll_num)).setText(rollNumber);
                                ((TextView) tableRow.findViewById(R.id.tv_att_history_list_item_name)).setText(name);
                                ((TextView) tableRow.findViewById(R.id.tv_att_history_list_item_status)).setText(""+status);

                                tableLayout.addView(tableRow, tableLayout.getChildCount());

                            }
                        } else {
                            Toast.makeText(AttendanceHistoryActivity.this, "no attendence found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }
    //todo after that provide an option to export it to an exel sheet
}
