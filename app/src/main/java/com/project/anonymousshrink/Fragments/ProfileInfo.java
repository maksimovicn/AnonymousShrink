package com.project.anonymousshrink.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.project.anonymousshrink.Activities.Forgot;
import com.project.anonymousshrink.Activities.Login;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Model.UsersData;

import thebat.lib.validutil.ValidUtils;

public class ProfileInfo extends Fragment {


    private View view;
    private TextView name;
    private TextView psychologist;
    private TextView gender;
    private TextView email;
    private TextView contact;
    private TextView question;
    private TextView answers;
    private TextView dob;
    private Button btnReset;
    private ValidUtils validUtils;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_profile_info, container, false);
        Initialization();
        return view;
    }

    private void Initialization() {

        name = (TextView)view. findViewById(R.id.tvNamePI);
        psychologist = (TextView)view. findViewById(R.id.tvPsychologistPI);
        gender = (TextView)view. findViewById(R.id.tvGenderPI);
        email = (TextView)view. findViewById(R.id.tvEmailPI);
        contact = (TextView)view. findViewById(R.id.tvContactPI);
        dob = (TextView)view. findViewById(R.id.tvDOBPI);
        question = (TextView)view. findViewById(R.id.tvQuestionPI);
        answers = (TextView)view. findViewById(R.id.tvAnswerPI);
        btnReset=(Button)view.findViewById(R.id.btnResetPassword);

        validUtils = new ValidUtils();
        auth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);


        setSelfDetails();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                resetPassword();
            }

        });

    }

    private void setSelfDetails() {
        name.setText(UsersData.getUser().getF_name() + " " + UsersData.getUser().getL_name());
        if (!UsersData.getUser().getPsychologist().equals("Yes")) {
            psychologist.setVisibility(View.GONE);
        }
        gender.setText(UsersData.getUser().getSex());
        email.setText(UsersData.getUser().getEmail());
        contact.setText(UsersData.getUser().getContact());
        dob.setText(UsersData.getUser().getDob());
        answers.setText(UsersData.getUser().getAnswers());
        question.setText(UsersData.getUser().getQuestions());
    }

    private void resetPassword() {
        auth.sendPasswordResetEmail(UsersData.getUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Successful")
                            .setMessage("Check your email address to reset your password.")
                            .setIcon(getResources().getDrawable(R.drawable.ic_done_black_24dp))
                            .setPositiveButton("OK", null)
                            .show();

                }
            }
        });
    }


}
