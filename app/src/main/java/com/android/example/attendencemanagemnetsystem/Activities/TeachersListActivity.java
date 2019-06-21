package com.android.example.attendencemanagemnetsystem.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.example.attendencemanagemnetsystem.Adapters.TeachersListAdapter;
import com.android.example.attendencemanagemnetsystem.Interfaces.TeacherItemCallbacks;
import com.android.example.attendencemanagemnetsystem.Models.TeacherModel;
import com.android.example.attendencemanagemnetsystem.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.function.ToDoubleBiFunction;

public class TeachersListActivity extends AppCompatActivity implements ActionMode.Callback, TeacherItemCallbacks {
    private RecyclerView recyclerView;
    private TeachersListAdapter teachersListAdapter;
    private ArrayList<TeacherModel> teachersArrayList;

    private Button doneSelectionButton;

    //actionmode related
    private ActionMode mActionMode;
    public static boolean isInActonMode = false;

    private ArrayList<TeacherModel> selectedTeachersArrayList;
    private SearchView searchView;
    private int totalSelectedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_list);
        getSupportActionBar().setTitle("Teachers");

        initFields();

        loadTeachers();

        if (getIntent().hasExtra("select")) {
            doneSelectionButton.setVisibility(View.VISIBLE);
            prepareActionMode();
            doneSelectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("selected_teachers", selectedTeachersArrayList);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }


    private void initFields() {
        recyclerView = findViewById(R.id.rv_teachers_list);
        teachersArrayList = new ArrayList<>();
        teachersListAdapter = new TeachersListAdapter(teachersArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teachersListAdapter);

        selectedTeachersArrayList = new ArrayList<>();

        doneSelectionButton = findViewById(R.id.bt_teachers_list_done);
    }

    private void loadTeachers() {
        // TODO: 5/24/2019 load teachers from users where teacher is type
        FirebaseDatabase.getInstance().getReference().child("users")
                .orderByChild("type").equalTo("teacher").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String number = dataSnapshot.child("number").getValue(String.class);
                String teacher_id = dataSnapshot.getKey();
                teachersArrayList.add(new TeacherModel(teacher_id, number));
                teachersListAdapter.notifyDataSetChanged();
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


    @Override
    public boolean onCreateActionMode(ActionMode mode, final Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.action_mode_teachers_list, menu);


        final MenuItem searchMenuItem = menu.findItem(R.id.nav_teachers_action_mode_search);
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
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {


        switch (item.getItemId()) {
            case R.id.nav_teachers_action_mode_select_all:
                for (TeacherModel currentTeacher : teachersArrayList) {
                    currentTeacher.setSelected(false);
                }
                if (selectedTeachersArrayList.size() == teachersArrayList.size()) { //if we want to unselect the list
                    selectedTeachersArrayList.clear();

                } else {
                    selectedTeachersArrayList.clear();//if some items are already selected then remove them to sustain integrity
                    selectedTeachersArrayList.addAll(teachersArrayList);
                    for (TeacherModel currentTeacher : selectedTeachersArrayList) {
                        currentTeacher.setSelected(true);
                    }

                }
                totalSelectedCount = selectedTeachersArrayList.size();
                updateCounterUI();


                teachersListAdapter.notifyDataSetChanged();
                break;
            case  R.id.nav_teachers_action_mode_search:
                Toast.makeText(this, "implement later", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        isInActonMode = false;//action mode is no longer
        selectedTeachersArrayList.clear();
        totalSelectedCount = 0;
        teachersListAdapter.notifyDataSetChanged();
//        if (teachersArrayList.isEmpty())// TODO: 5/26/2019  add empty view later
//            mEmptyViewContainer.setVisibility(View.VISIBLE);


    }


    //action mode relatd
    private void prepareActionMode() {
        mActionMode = startActionMode(this);
        isInActonMode = true;

    }


    @Override
    public void onTeacherClciked(int position) {
        TeacherModel teacherModel = teachersArrayList.get(position);

        if (teacherModel.isSelected()) {
            totalSelectedCount--;
            teacherModel.setSelected(false);
            selectedTeachersArrayList.remove(teacherModel);


        } else {
            totalSelectedCount++;
            teacherModel.setSelected(true);
            selectedTeachersArrayList.add(teacherModel);

        }
        updateCounterUI();
    }

    public void updateCounterUI() {
        if (mActionMode != null)

            mActionMode.setTitle(totalSelectedCount + " Selected");
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {

        super.onActionModeFinished(mode);
        onBackPressed(); //// TODO: 5/31/2019  late handle it more professionaly
    }

}
