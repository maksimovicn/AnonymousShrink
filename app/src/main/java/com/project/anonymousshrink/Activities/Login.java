package com.project.anonymousshrink.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Internet;

import thebat.lib.validutil.ValidUtils;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText etPassword;
    private EditText etEmail;
    private Button btnSignIn;
    private Button btnSignUp;
    private TextView tvForgot;
    private ValidUtils validUtils;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private FirebaseAuth auth;

    private FirebaseDatabase root;
    private DatabaseReference rootReference;
    private DatabaseReference userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Initialization();

    }

    private void Initialization() {


        etEmail = (EditText) findViewById(R.id.etEmailLogIn);
        etPassword = (EditText) findViewById(R.id.etPasswordLogIn);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        tvForgot = (TextView) findViewById(R.id.tvForgotPassword);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        tvForgot.setOnClickListener(this);


        validUtils = new ValidUtils();
        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(this);

        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        rootReference = root.getReference();
        userReference = root.getReference("Users");
        validUtils = new ValidUtils();
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Signing in please wait");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                 logInUser(v);
                break;
            case R.id.btnSignUp:
                startActivity(new Intent(Login.this, Register.class));
                finish();
                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(Login.this, Forgot.class));
                finish();
                break;
            default:
                return;
        }
    }

    private void logInUser(View view) {
        if (!validUtils.validateEmail(etEmail)) {
            etEmail.setError("Invalid email address");
        } else if (!isValidPassword(etPassword.getText().toString())) {
            etPassword.setError("Invalid password minimum 6 characters");
        } else if (!validUtils.isNetworkAvailable(this)) {
            new Internet().internetAlert(this, view);
        } else {

            progressDialog.show();
            auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        startActivity(new Intent(Login.this, DashboardRegular.class));
                        finish();

                    } else {
                        progressDialog.dismiss();
                        alertDialog.setTitle("Operation Failed")
                                .setMessage("The email address or password you have entered is invalid.")
                                .setIcon(getResources().getDrawable(R.drawable.ic_warning_black_24dp))
                                .setPositiveButton("Retry", null)
                                .show();

                    }
                }
            });
        }
    }

    private boolean isValidPassword(String password) {
        boolean flag = true;

        if (password.isEmpty() || password.length() < 6) {
            flag = false;
        }
        return flag;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser=auth.getCurrentUser();
        if (firebaseUser!=null)
        {
            startActivity(new Intent(Login.this, DashboardRegular.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Login.this,Welcome.class));
        finish();
    }
}
