package com.project.anonymousshrink.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Model.User;

public class ProfileView extends AppCompatActivity {


    private TextView name;
    private TextView psychologist;
    private TextView gender;
    private TextView email;
    private TextView contact;
    private TextView question;
    private TextView answers;
    private TextView dob;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;
    private ValueEventListener valueEventListener;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Initializations();

    }

    private void Initializations() {

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        name = (TextView) findViewById(R.id.tvNamePV);
        psychologist = (TextView) findViewById(R.id.tvPsychologistPV);
        gender = (TextView) findViewById(R.id.tvGenderPV);
        email = (TextView) findViewById(R.id.tvEmailPV);
        contact = (TextView) findViewById(R.id.tvContactPV);
        dob = (TextView) findViewById(R.id.tvDOBPV);
        question = (TextView) findViewById(R.id.tvQuestionPV);
        answers = (TextView) findViewById(R.id.tvAnswerPV);

        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users").child(uid);

        setProfileDetails();


    }

    private void setProfileDetails() {
      userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                name.setText(user.getF_name() + " " + user.getL_name());
                if (!user.getPsychologist().equals("Yes")) {
                    psychologist.setVisibility(View.GONE);
                }
                gender.setText(user.getSex());
                email.setText(user.getEmail());
                contact.setText(user.getContact());
                dob.setText(user.getDob());
                answers.setText(user.getAnswers());
                question.setText(user.getQuestions());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
