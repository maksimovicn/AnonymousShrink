package com.project.anonymousshrink.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Answer.AnswerAdapter;
import com.project.anonymousshrink.Utilities.Internet;
import com.project.anonymousshrink.Utilities.Model.Answers;
import com.project.anonymousshrink.Utilities.Model.Questions;
import com.project.anonymousshrink.Utilities.Model.UsersData;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import thebat.lib.validutil.ValidUtils;

public class AnswerQuestion extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDate;
    private TextView tvAnswers;
    private TextView tvQuestion;

    private LinearLayout llRV;
    private ProgressBar pbAnswer;
    private TextView tvNoAnswers;
    private RecyclerView rvAnswers;
    private Questions questions;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference rootReference;
    private DatabaseReference answersReference;
    private DatabaseReference questionReference;
    private DatabaseReference userReference;
    private ArrayList<Answers> answersArrayList;
    private AnswerAdapter answerAdapter;
    private LinearLayout llAnswer;
    private Button btnPostAnswer;
    private ValidUtils validUtils;
    private ProgressDialog progressDialog;
    private EditText etAnswer;
    private HashMap<String, String> hashMap;
    private CardView cvNoAnswer;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        Initialization();
        fetchFromFireBase();

    }

    private void Initialization() {
        Intent intent = getIntent();
         position = intent.getIntExtra("position", 0);
        questions = UsersData.getQuestionsArrayList().get(position);

        tvDate = (TextView) findViewById(R.id.tvDateA);
        tvAnswers = (TextView) findViewById(R.id.tvAnswersPostedA);
        tvQuestion = (TextView) findViewById(R.id.tvQuestionsA);
        cvNoAnswer = (CardView) findViewById(R.id.cvNoAnswers);
        llRV = (LinearLayout) findViewById(R.id.llRV);
        tvNoAnswers = (TextView) findViewById(R.id.tvNoAnswers);
        rvAnswers = (RecyclerView) findViewById(R.id.rvAnswers);
        pbAnswer = (ProgressBar) findViewById(R.id.pbAnswers);
        btnPostAnswer = (Button) findViewById(R.id.btnPostAnswer);
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        llAnswer=(LinearLayout)findViewById(R.id.llAnswer) ;

        btnPostAnswer.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();
        answersReference = firebaseDatabase.getReference("Answers").child(questions.getQid());
        questionReference=firebaseDatabase.getReference("Questions").child(UsersData.getQuestionsArrayList().get(position).getQid());
        if (firebaseUser == null) {
            llAnswer.setVisibility(View.GONE);
        }else {
            userReference = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid());

        }
        answersArrayList = new ArrayList<>();
        validUtils = new ValidUtils();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Posting your answer please wait...");


        setQuestionDetails();
    }

    private void setQuestionDetails() {
        tvDate.setText(questions.getDate());
        tvQuestion.setText(questions.getQuestion());
        tvAnswers.setText(questions.getAnswers());
    }

    private void fetchFromFireBase() {
        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Answers")) {
                    rootReference.child("Answers").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(questions.getQid())) {
                                answersReference.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                        LoadData(dataSnapshot);
                                        if (answersArrayList.isEmpty()) {
                                            cvNoAnswer.setVisibility(View.VISIBLE);
                                            pbAnswer.setVisibility(View.GONE);
                                            llRV.setVisibility(View.GONE);
                                            tvNoAnswers.setVisibility(View.VISIBLE);
                                        } else {
                                            llRV.setVisibility(View.VISIBLE);
                                            cvNoAnswer.setVisibility(View.GONE);
                                            answerAdapter = new AnswerAdapter(AnswerQuestion.this, answersArrayList);
                                            rvAnswers.setAdapter(answerAdapter);
                                            rvAnswers.setLayoutManager(new LinearLayoutManager(AnswerQuestion.this));

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
                                cvNoAnswer.setVisibility(View.VISIBLE);
                                pbAnswer.setVisibility(View.GONE);
                                llRV.setVisibility(View.GONE);
                                tvNoAnswers.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    cvNoAnswer.setVisibility(View.VISIBLE);
                    pbAnswer.setVisibility(View.GONE);
                    llRV.setVisibility(View.GONE);
                    tvNoAnswers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LoadData(DataSnapshot dataSnapshot) {
        Answers answers = dataSnapshot.getValue(Answers.class);
        answersArrayList.add(answers);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPostAnswer:
                postAnswer(v);
                break;
            default:
                break;
        }
    }

    private void postAnswer(View v) {
        if (!validUtils.validateEditTexts(etAnswer)) {
            etAnswer.setError("Enter answer");
        } else if (!validUtils.isNetworkAvailable(AnswerQuestion.this)) {
            new Internet().internetAlert(AnswerQuestion.this, v);
        } else {
            postAnswerInFireBase();
        }
    }

    private void postAnswerInFireBase() {
        progressDialog.show();

        String key = answersReference.push().getKey();

        hashMap = new HashMap<>();
        hashMap.put("answer", etAnswer.getText().toString());
        hashMap.put("name", UsersData.getUser().getF_name() + " " + UsersData.getUser().getL_name());
        hashMap.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        hashMap.put("aid", key);
        hashMap.put("uid", auth.getCurrentUser().getUid());

        answersReference.child(key).setValue(hashMap);


        int postedAnswers = Integer.parseInt(UsersData.getUser().getAnswers());
        UsersData.getUser().setAnswers(String.valueOf(postedAnswers + 1));
        userReference.child("answers").setValue(String.valueOf(postedAnswers + 1));

        int answers=Integer.parseInt(UsersData.getQuestionsArrayList().get(position).getAnswers());
        UsersData.getQuestionsArrayList().get(position).setAnswers(String.valueOf(answers+1));
        questionReference.child("answers").setValue(String.valueOf(answers+1));

        tvAnswers.setText(UsersData.getQuestionsArrayList().get(position).getAnswers());


        etAnswer.setText(null);
        progressDialog.dismiss();
        Toast.makeText(this, "Answer posted successfully", Toast.LENGTH_SHORT).show();

    }
}
