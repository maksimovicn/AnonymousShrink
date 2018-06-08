package com.project.anonymousshrink.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Home.QuestionAdapter;
import com.project.anonymousshrink.Utilities.Model.Questions;
import com.project.anonymousshrink.Utilities.Model.UsersData;

import java.util.ArrayList;

public class DashboardGuest extends AppCompatActivity {



    private LinearLayout llNotFound;
    private LinearLayout llNoQuestionsFound;
    private ProgressBar pbHome;
    private RecyclerView rvHome;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference questionReference;
    private DatabaseReference rootReference;
    private ChildEventListener childEventListener;
    private ArrayList<Questions> questionsArrayList;
    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_guest);
        Initialization();
    }

    private void Initialization() {
        llNotFound = (LinearLayout) findViewById(R.id.llNotFoundGuest);
        llNoQuestionsFound = (LinearLayout)findViewById(R.id.ll_No_Questions_Guest);
        pbHome = (ProgressBar) findViewById(R.id.pbHomeGuest);
        rvHome = (RecyclerView) findViewById(R.id.rvHomeGuest);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();
        questionReference = firebaseDatabase.getReference("Questions");
        questionsArrayList = new ArrayList<>();
        fetchFromFireBase();
    }

    private void fetchFromFireBase() {

        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Questions")) {
                    childEventListener = questionReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            LoadData(dataSnapshot);
                            if (questionsArrayList.isEmpty()) {
                                rvHome.setVisibility(View.GONE);
                                pbHome.setVisibility(View.GONE);
                                llNotFound.setVisibility(View.VISIBLE);
                                llNoQuestionsFound.setVisibility(View.VISIBLE);
                            } else {

                                llNotFound.setVisibility(View.GONE);
                                rvHome.setVisibility(View.VISIBLE);
                                UsersData.setQuestionsArrayList(questionsArrayList);
                                questionAdapter = new QuestionAdapter(DashboardGuest.this, questionsArrayList);
                                rvHome.setAdapter(questionAdapter);
                                rvHome.setLayoutManager(new LinearLayoutManager(DashboardGuest.this));
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                } else {
                    rvHome.setVisibility(View.GONE);
                    pbHome.setVisibility(View.GONE);
                    llNotFound.setVisibility(View.VISIBLE);
                    llNoQuestionsFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LoadData(DataSnapshot dataSnapshot) {
        Questions questions = dataSnapshot.getValue(Questions.class);

        if (questions.getVisibility().equals("Regular Users") || questions.getVisibility().equals("All")) {
            questionsArrayList.add(questions);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_guest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit_guest) {
            moveTaskToBack(true);
            return true;
        }
        if (id == R.id.action_login_guest) {
            startActivity(new Intent(DashboardGuest.this, Login.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
