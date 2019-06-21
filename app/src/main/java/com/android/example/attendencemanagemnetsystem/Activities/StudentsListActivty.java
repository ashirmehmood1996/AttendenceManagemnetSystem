package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;//wee
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.Adapters.StudentListAdapter;
import com.android.example.attendencemanagemnetsystem.Interfaces.StudentItemCallbacks;
import com.android.example.attendencemanagemnetsystem.Models.StudentModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentsListActivty extends AppCompatActivity implements ActionMode.Callback, StudentItemCallbacks {
    private FloatingActionButton addStdFloatingButton;
    private Button doneSelectionButon;

    //recylcer view related
    private RecyclerView recyclerView;
    private ArrayList<StudentModel> studentArrayList;
    private StudentListAdapter studentListAdapter;
    private ArrayList<StudentModel> selectedStudentsArrayList;
    private ActionMode mActionMode;
    public static boolean isInActonMode = false;
    private int totalSelectedCount = 0;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        getSupportActionBar().setTitle("Students");

        initFields();

        if (getIntent().hasExtra("select")) {
            addStdFloatingButton.hide();
            doneSelectionButon.setVisibility(View.VISIBLE);
            prepareActionMode();
            doneSelectionButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("selected_teachers", selectedStudentsArrayList);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

        } else {
            attachListeners();
        }

        loadStudents();
    }


    private void initFields() {
        addStdFloatingButton = findViewById(R.id.fab_std_list_add_new);

        recyclerView = findViewById(R.id.rv_std_list);
        studentArrayList = new ArrayList<>();
        studentListAdapter = new StudentListAdapter(studentArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentListAdapter);
        doneSelectionButon = findViewById(R.id.bt_std_list_done);
        selectedStudentsArrayList = new ArrayList<>();
    }

    private void attachListeners() {
        addStdFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout addStudentLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_student, null);
                final EditText nameEditText = addStudentLayout.findViewById(R.id.et_dialog_add_std_name);
                final EditText rollNumEditText = addStudentLayout.findViewById(R.id.et_dialog_add_std_roll_num);
                // TODO: 5/26/2019  add the logic to add new students to the pool
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentsListActivty.this);
                builder.setView(addStudentLayout);
                builder.setTitle("Add student")
                        .setMessage("please provide the following details");
                builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rollNum = rollNumEditText.getText().toString().trim();
                        String name = nameEditText.getText().toString().trim();
                        if (rollNum.isEmpty() || name.isEmpty()) {
                            Toast.makeText(StudentsListActivty.this, "please fill out all the fields", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            addStudentToFirebase(name, rollNum);
                        }


                    }

                    private void addStudentToFirebase(String name, String rollNum) {
                        HashMap<String, Object> studentMap = new HashMap<>();
                        studentMap.put("name", name);
                        studentMap.put("roll_num", rollNum);
                        FirebaseDatabase.getInstance().getReference().child("students")

                                .push().setValue(studentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(StudentsListActivty.this, "successfully added ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }).setNegativeButton("cancel", null).show();

            }
        });
    }

    private void loadStudents() {
        FirebaseDatabase.getInstance().getReference().child("students")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String key = dataSnapshot.getKey();
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String rollNum = dataSnapshot.child("roll_num").getValue(String.class);

                        studentArrayList.add(new StudentModel(key, name, rollNum));
                        studentListAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //action mode relatd
    private void prepareActionMode() {
        mActionMode = startActionMode(this);
        isInActonMode = true;

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.action_mode_std_list, menu);
//        final MenuItem searchMenuItem = menu.findItem(R.id.nav_std_list_action_mode_search);
//        searchView = (SearchView) searchMenuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                if (!searchView.isIconified()) {
//                    searchView.setIconified(true);
//                }
//
//                searchMenuItem.collapseActionView();
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
///// TODO: 5/26/2019 add search a little later
////                ArrayList<> filteredList = filter(mCompletedDownlaodsDataArrayList, newText);
////
////                completedDownlaodsRecyclerVieAdapter.setFilter(filteredList, newText);
////                completedDownlaodsRecyclerVieAdapter.notifyDataSetChanged();
//                return true;
//            }
//        });
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_teachers_action_mode_select_all:
                for (StudentModel currentStudent : studentArrayList) {
                    currentStudent.setSelected(false);
                }
                if (selectedStudentsArrayList.size() == studentArrayList.size()) { //if we want to unselect the list
                    selectedStudentsArrayList.clear();

                } else {
                    selectedStudentsArrayList.clear();//if some items are already selected then remove them to sustain integrity
                    selectedStudentsArrayList.addAll(studentArrayList);
                    for (StudentModel currentStudent : selectedStudentsArrayList) {
                        currentStudent.setSelected(true);
                    }

                }
                totalSelectedCount = selectedStudentsArrayList.size();
                updateCounterUI();


                studentListAdapter.notifyDataSetChanged();
                break;
            case R.id.nav_std_list_action_mode_search:
                Toast.makeText(this, "Implement later", Toast.LENGTH_SHORT).show();
                /*searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        if (!searchView.isIconified()) {
                            searchView.setIconified(true);
                        }

                        item.collapseActionView();

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

//                ArrayList<> filteredList = filter(mCompletedDownlaodsDataArrayList, newText);
//
//                completedDownlaodsRecyclerVieAdapter.setFilter(filteredList, newText);
//                completedDownlaodsRecyclerVieAdapter.notifyDataSetChanged();
                        return true;
                    }
                });*/

                break;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    public void updateCounterUI() {
        if (mActionMode != null)

            mActionMode.setTitle(totalSelectedCount + " Selected");
    }

    @Override
    public void onStudentClicked(int position) {
        StudentModel studentModel = studentArrayList.get(position);

        if (studentModel.isSelected()) {
            totalSelectedCount--;
            studentModel.setSelected(false);
            selectedStudentsArrayList.remove(studentModel);


        } else {
            totalSelectedCount++;
            studentModel.setSelected(true);
            selectedStudentsArrayList.add(studentModel);

        }
        updateCounterUI();
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {

        super.onActionModeFinished(mode);
        onBackPressed(); //// TODO: 5/31/2019  late handle it more professionaly
    }
}
