package com.project.anonymousshrink.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.anonymousshrink.R;

import thebat.lib.validutil.ValidUtils;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnGuest;
    private FirebaseAuth auth;
    private FirebaseDatabase root;
    private DatabaseReference rootReference;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Initialization();
    }

    private void Initialization() {

        btnLogin = (Button) findViewById(R.id.btnLoginScreen);
        btnGuest = (Button) findViewById(R.id.btnGuestScreen);

        btnLogin.setOnClickListener(this);
        btnGuest.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        userReference = root.getReference("Users");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginScreen:
                startActivity(new Intent(Welcome.this, Login.class));
                finish();
                break;
            case R.id.btnGuestScreen:
                startActivity(new Intent(Welcome.this, DashboardGuest.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser=auth.getCurrentUser();
        if (firebaseUser!=null)
        {
            startActivity(new Intent(Welcome.this, DashboardRegular.class));
            finish();
        }

    }
}
