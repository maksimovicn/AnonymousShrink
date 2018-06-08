package com.project.anonymousshrink.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Internet;
import com.project.anonymousshrink.Utilities.Model.UsersData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import thebat.lib.validutil.ValidUtils;


public class PostQuestion extends Fragment implements View.OnClickListener {


    private View view;
    private EditText etQuestion;
    private Spinner spVisibility;
    private Button btnPost;
    private ValidUtils validUtils;
    private ProgressDialog progressDialog;
    private HashMap<String, String> hashMap;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference questionReference;
    private DatabaseReference userReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        questionReference = firebaseDatabase.getReference("Questions");
        userReference=firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_question, container, false);
        Initialization();
        return view;
    }

    private void Initialization() {
        etQuestion = (EditText) view.findViewById(R.id.etQuestion);
        spVisibility = (Spinner) view.findViewById(R.id.spVisibility);
        btnPost = (Button) view.findViewById(R.id.btnPost);

        btnPost.setOnClickListener(this);

        validUtils = new ValidUtils();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Posting your question please wait...");
        progressDialog.setCancelable(false);


        loadSpinnerValues();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPost:
                postQuestion(v);
                break;
            default:
                break;
        }
    }

    private void postQuestion(View v) {
        if (!validUtils.validateEditTexts(etQuestion)) {
            etQuestion.setError("Ask question");
        } else if (spVisibility.getSelectedItemPosition() == 0) {
            ((TextView) spVisibility.getSelectedView()).setError("Select price");
        } else if (!validUtils.isNetworkAvailable(getContext())) {
            new Internet().internetAlert(getContext(), v);
        } else {

            String qid = questionReference.push().getKey();
            progressDialog.show();
            hashMap = new HashMap<>();
            hashMap.put("question", etQuestion.getText().toString());
            hashMap.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            hashMap.put("answers", "0");
            hashMap.put("qid", qid);
            hashMap.put("uid", auth.getCurrentUser().getUid());
            hashMap.put("posted_by", UsersData.getUser().getF_name() + " " + UsersData.getUser().getL_name());
            hashMap.put("visibility", spVisibility.getSelectedItem().toString());

            questionReference.child(qid).setValue(hashMap);

            int postedQuestions=Integer.parseInt(UsersData.getUser().getQuestions());
            UsersData.getUser().setQuestions(String.valueOf(postedQuestions+1));
            userReference.child("questions").setValue(String.valueOf(postedQuestions+1));

            etQuestion.setText(null);
            spVisibility.setSelection(0);
            progressDialog.dismiss();

            Toast.makeText(getContext(), "Questions posted successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSpinnerValues() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.visibility_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVisibility.setAdapter(arrayAdapter);
    }


}
