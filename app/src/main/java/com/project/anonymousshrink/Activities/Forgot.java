package com.project.anonymousshrink.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Internet;

import thebat.lib.validutil.ValidUtils;

public class Forgot extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private Button btnSend;
    private EditText etEmail;
    private ValidUtils validUtils;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Initialization();

    }


    //Initialize elements for forgot password.
    private void Initialization() {
        etEmail = (EditText) findViewById(R.id.etEmailForgot);
        btnSend = (Button) findViewById(R.id.btnSendPassword);

        validUtils = new ValidUtils();
        auth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait");

        // when send button clicked it check the validation and then send email to reset password
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validUtils.validateEmail(etEmail)) {
                    etEmail.setError("Invalid email address");
                } else if (!validUtils.isNetworkAvailable(Forgot.this)) {
                    new Internet().internetAlert(Forgot.this, v);
                } else {
                    progressDialog.show();
                    resetPassword();
                }
            }
        });
    }

    private void resetPassword() {
        auth.sendPasswordResetEmail(etEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(Forgot.this);
                    alert.setTitle("Successful")
                            .setMessage("Check you email address to reset you password.")
                            .setIcon(getResources().getDrawable(R.drawable.ic_done_black_24dp))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Forgot.this,Login.class));
                                    finish();
                                }
                            })
                            .show();

                } else {
                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(Forgot.this);
                    alert.setTitle("Operation Failed")
                            .setMessage("The email address you have entered is not registered.")
                            .setIcon(getResources().getDrawable(R.drawable.ic_warning_black_24dp))
                            .setPositiveButton("Retry", null)
                            .show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(Forgot.this,Login.class));finish();
    }
}
