package com.project.anonymousshrink.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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


public class SearchQuestion extends Fragment {


    private View view;
    private ProgressBar pbSearchQuestion;
    private LinearLayout llQuestionFound;
    private RecyclerView rvQuestions;
    private QuestionAdapter questionAdapter;
    private TextView tvNoQuestionFound;
    private EditText etSearchQuestion;
    private Button btnSearch;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference questionReference;
    private DatabaseReference rootReference;
    private ChildEventListener childEventListener;
    private ArrayList<Questions> questionsArrayList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();
        questionReference = firebaseDatabase.getReference("Questions");
        questionsArrayList = new ArrayList<>();
        fetchFromFireBase();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_question, container, false);
        Initializations();
        return view;
    }

    private void Initializations() {
        pbSearchQuestion = (ProgressBar) view.findViewById(R.id.pbSearchQuestion);
        llQuestionFound = (LinearLayout) view.findViewById(R.id.llQuestionFound);
        rvQuestions = (RecyclerView) view.findViewById(R.id.rvQuestionSearhc);
        tvNoQuestionFound = (TextView) view.findViewById(R.id.tvQuestionFound);
        etSearchQuestion = (EditText) view.findViewById(R.id.etQuestionSearch);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        etSearchQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearchQuestion.getText().toString().isEmpty()) {

                    if (!questionsArrayList.isEmpty()) {
                        llQuestionFound.setVisibility(View.GONE);
                        rvQuestions.setVisibility(View.VISIBLE);
                        questionAdapter = new QuestionAdapter(getContext(), questionsArrayList);
                        rvQuestions.setAdapter(questionAdapter);
                        rvQuestions.setLayoutManager(new LinearLayoutManager(getActivity()));
                    } else {

                        rvQuestions.setVisibility(View.GONE);
                        pbSearchQuestion.setVisibility(View.GONE);
                        tvNoQuestionFound.setVisibility(View.VISIBLE);
                        llQuestionFound.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSearchQuestion.getText().toString().isEmpty()) {
                    etSearchQuestion.setError("Enter text ");
                } else {
                    searchInFirebase();
                }
            }
        });
    }

    private void searchInFirebase() {
        ArrayList<Questions> searchedArraList = new ArrayList<>();
        if (!questionsArrayList.isEmpty()) {
            for (int i = 0; i < questionsArrayList.size(); i++) {
                if (questionsArrayList.get(i).getQuestion().toLowerCase().contains(etSearchQuestion.getText().toString().toLowerCase())) {
                    searchedArraList.add(questionsArrayList.get(i));
                }
            }

            if (!searchedArraList.isEmpty()) {
                questionAdapter = new QuestionAdapter(getContext(), searchedArraList);
                rvQuestions.setAdapter(questionAdapter);
                rvQuestions.setLayoutManager(new LinearLayoutManager(getActivity()));

            } else {
                rvQuestions.setVisibility(View.GONE);
                pbSearchQuestion.setVisibility(View.GONE);
                tvNoQuestionFound.setVisibility(View.VISIBLE);
                llQuestionFound.setVisibility(View.VISIBLE);
            }

        } else {
            rvQuestions.setVisibility(View.GONE);
            pbSearchQuestion.setVisibility(View.GONE);
            tvNoQuestionFound.setVisibility(View.VISIBLE);
            llQuestionFound.setVisibility(View.VISIBLE);
        }


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
                                rvQuestions.setVisibility(View.GONE);
                                pbSearchQuestion.setVisibility(View.GONE);
                                tvNoQuestionFound.setVisibility(View.VISIBLE);
                                llQuestionFound.setVisibility(View.VISIBLE);
                            } else {

                                llQuestionFound.setVisibility(View.GONE);
                                rvQuestions.setVisibility(View.VISIBLE);
                                UsersData.setQuestionsArrayList(questionsArrayList);
                                questionAdapter = new QuestionAdapter(getContext(), questionsArrayList);
                                rvQuestions.setAdapter(questionAdapter);
                                rvQuestions.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                    rvQuestions.setVisibility(View.GONE);
                    pbSearchQuestion.setVisibility(View.GONE);
                    tvNoQuestionFound.setVisibility(View.VISIBLE);
                    llQuestionFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LoadData(DataSnapshot dataSnapshot) {
        Questions questions = dataSnapshot.getValue(Questions.class);

        if (UsersData.getUser().getPsychologist().equals("Yes")) {
            questionsArrayList.add(questions);

        } else {
            if (questions.getVisibility().toString().equals("Regular Users") || questions.getVisibility().toString().equals("All")) {
                questionsArrayList.add(questions);
            }
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (childEventListener != null) {
            questionReference.removeEventListener(childEventListener);
        }
    }

}
