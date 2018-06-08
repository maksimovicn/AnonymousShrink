package com.project.anonymousshrink.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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


public class ViewQuestions extends Fragment {


    private View view;
    private LinearLayout llPostedNotFound;
    private LinearLayout llNoAsked;
    private ProgressBar pbPosted;
    private RecyclerView rvPosted;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference questionReference;
    private DatabaseReference rootReference;
    private ChildEventListener childEventListener;
    private ArrayList<Questions> questionsArrayList;
    private QuestionAdapter questionAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();
        questionReference = firebaseDatabase.getReference("Questions");
        questionsArrayList=new ArrayList<>();
        fetchFromFireBase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_questions, container, false);
        Initialization();
        return view;
    }

    private void Initialization() {
        llPostedNotFound = (LinearLayout) view.findViewById(R.id.llPostedNotFound);
        llNoAsked = (LinearLayout) view.findViewById(R.id.ll_No_Asked);
        pbPosted = (ProgressBar) view.findViewById(R.id.pbPosted);
        rvPosted = (RecyclerView) view.findViewById(R.id.rvPosted);
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
                                rvPosted.setVisibility(View.GONE);
                                pbPosted.setVisibility(View.GONE);
                                llNoAsked.setVisibility(View.VISIBLE);
                                llPostedNotFound.setVisibility(View.VISIBLE);
                            } else {
                                llPostedNotFound.setVisibility(View.GONE);
                                rvPosted.setVisibility(View.VISIBLE);
                                questionAdapter = new QuestionAdapter(getContext(), questionsArrayList);
                                rvPosted.setAdapter(questionAdapter);
                                rvPosted.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                    rvPosted.setVisibility(View.GONE);
                    pbPosted.setVisibility(View.GONE);
                    llNoAsked.setVisibility(View.VISIBLE);
                    llPostedNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LoadData(DataSnapshot dataSnapshot) {
        Questions questions = dataSnapshot.getValue(Questions.class);
        if (questions.getUid().equals(auth.getCurrentUser().getUid())) {
            questionsArrayList.add(questions);
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
