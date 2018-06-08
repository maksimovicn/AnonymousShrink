package com.project.anonymousshrink.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Internet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import thebat.lib.validutil.ValidUtils;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText etFName;
    private EditText etLName;
    private RadioGroup rgSex;
    private RadioGroup rgPsychologist;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbYes;
    private RadioButton rbNo;
    private EditText etContact;
    private EditText etDOB;
    private EditText etPassword;
    private EditText etConfirm;
    private EditText etEmail;
    private Button btnSignUp;
    private ValidUtils validUtils;
    private TextView alreadyRegistered;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private CompoundButton cbTC;
    private Calendar calendar;
    private HashMap<String, String> hashMap;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Initialization();

    }


    private void Initialization() {

        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etLName);
        etContact = (EditText) findViewById(R.id.etContactNumber);
        etDOB = (EditText) findViewById(R.id.etDOB);
        rbMale = (RadioButton) findViewById(R.id.rbFemale);
        rbFemale = (RadioButton) findViewById(R.id.rbMale);
        rbYes = (RadioButton) findViewById(R.id.rbYes);
        rbNo = (RadioButton) findViewById(R.id.rbNo);
        rgPsychologist = (RadioGroup) findViewById(R.id.rgPsychologist);
        rgSex = (RadioGroup) findViewById(R.id.rgGender);
        etEmail = (EditText) findViewById(R.id.etEmailRegister);
        etPassword = (EditText) findViewById(R.id.etPasswordRegister);
        etConfirm = (EditText) findViewById(R.id.etConfirmRegister);
        btnSignUp = (Button) findViewById(R.id.btnSinUpRegister);
        alreadyRegistered = (TextView) findViewById(R.id.alreadyRegistered);


        etDOB.setKeyListener(null);
        btnSignUp.setOnClickListener(this);
        etDOB.setOnClickListener(this);

        calendar = Calendar.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Signing up please wait");
        progressDialog.setCancelable(false);
        validUtils = new ValidUtils();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");

        etDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    selectDateOfBirth();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSinUpRegister:
                signUp(v);
                break;
            case R.id.etDOB:
                break;

            case R.id.alreadyRegistered:
                startActivity(new Intent(Register.this, Login.class));
                finish();
                break;
        }
    }

    private void signUp(View v) {
        if (!validUtils.validateEditTexts(etFName)) {
            etFName.setError("Invalid first name");
        } else if (!validUtils.validateEditTexts(etLName)) {
            etLName.setError("Invalid last name");
        } else if (rgSex.getCheckedRadioButtonId() == -1) {
            rbFemale.setError("Select sex");
        } else if (!validUtils.validateEditTexts(etDOB)) {
            etDOB.setError("Invalid date of birth");
        } else if (!validUtils.validateEditTexts(etContact)) {
            etContact.setError("Select contact number");
        } else if (rgPsychologist.getCheckedRadioButtonId() == -1) {
            rbNo.setError("Select yes or no");
        } else if (!validUtils.validateEmail(etEmail)) {
            etEmail.setError("Invalid email address");
        } else if (!isValidPassword(etPassword.getText().toString())) {
            etPassword.setError("Invalid password minimum 6 characters");
        } else if (!isValidConfirm(etPassword.getText().toString(), etConfirm.getText().toString())) {
            etConfirm.setError("Password mismatch");
        } else if (!validUtils.isNetworkAvailable(Register.this)) {
            new Internet().internetAlert(Register.this, v);
        } else {
            createUser();
        }
    }

    private void createUser() {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    hashMap=new HashMap<String, String>();
                    hashMap.put("f_name",etFName.getText().toString());
                    hashMap.put("l_name",etLName.getText().toString());
                    hashMap.put("email",etEmail.getText().toString());
                    hashMap.put("dob",etDOB.getText().toString());
                    hashMap.put("contact",etContact.getText().toString());
                    hashMap.put("sex",(String) ((RadioButton) findViewById(rgSex.getCheckedRadioButtonId())).getText());
                    hashMap.put("psychologist",(String) ((RadioButton) findViewById(rgPsychologist.getCheckedRadioButtonId())).getText());
                    hashMap.put("questions","0");
                    hashMap.put("answers","0");
                    usersReference.child(auth.getCurrentUser().getUid()).setValue(hashMap);

                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(Register.this);
                    alert.setTitle("Successful")
                            .setIcon(getResources().getDrawable(R.drawable.ic_done_black_24dp))
                            .setMessage("Your account is created successfully you can login now.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(Register.this, Login.class));
                                    finish();
                                }
                            })
                            .show();
                } else {

                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(Register.this);
                    alert.setTitle("Operation Failed")
                            .setMessage("The email address you have entered is already registered please login.")
                            .setIcon(getResources().getDrawable(R.drawable.ic_warning_black_24dp))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Register.this, Login.class));
                                    finish();
                                }
                            })
                            .setNegativeButton("Retry", null)
                            .show();
                }
            }
        });
    }

    private boolean isValidPassword(String password) {
        boolean flag = true;

        if (password.isEmpty() || password.length() < 6) {
            flag = false;
        }

        return flag;
    }

    private boolean isValidConfirm(String password, String con) {
        boolean flag = true;

        if (con.isEmpty() || !con.equals(password) || con.length() < 6) {
            flag = false;
        }

        return flag;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, Login.class));
        finish();
    }

    private void selectDateOfBirth() {
        new DatePickerDialog(Register.this, dob, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            etDOB.setText(sdf.format(calendar.getTime()));
        }
    };

}
