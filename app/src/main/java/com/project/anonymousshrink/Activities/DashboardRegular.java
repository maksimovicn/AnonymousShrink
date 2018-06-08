package com.project.anonymousshrink.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.anonymousshrink.Fragments.Home;
import com.project.anonymousshrink.Fragments.PostQuestion;
import com.project.anonymousshrink.Fragments.ProfileInfo;
import com.project.anonymousshrink.Fragments.SearchQuestion;
import com.project.anonymousshrink.Fragments.ViewQuestions;
import com.project.anonymousshrink.R;
import com.project.anonymousshrink.Utilities.Model.User;
import com.project.anonymousshrink.Utilities.Model.UsersData;

public class DashboardRegular extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static int navItemIndex = 0;
    public static final String TAG_HOME = "Home";
    public static final String TAG_POST = "Post";
    public static final String TAG_POSTED = "Posted";
    public static final String TAG_SEARCH = "Search";
    public static final String TAG_PROFILE_INFO = "ProfileInfo";
    private boolean shouldLoadHomeFragOnBackPress = true;
    public static String CURRENT_TAG = TAG_HOME;
    private Handler mHandler;
    private String[] activityTitles;
    private Uri imageUri;
    private AlertDialog.Builder alertDialog;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;
    private TextView tvNavName;
    private TextView tvNavEmail;
    private View navHeader;
    private TextView tvPsychologist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Initialization();
        setNavigationHeader();
        setNavigationView();


        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void Initialization() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        tvNavName = (TextView) navHeader.findViewById(R.id.tvName);
        tvNavEmail = (TextView) navHeader.findViewById(R.id.tvEmail);
        tvPsychologist = (TextView) navHeader.findViewById(R.id.tvPsychologist);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid());


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mHandler = new Handler();
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_title);
        alertDialog = new AlertDialog.Builder(this);
    }

    private void setNavigationHeader() {

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvNavName.setText(user.getF_name() + " " + user.getL_name());
                tvNavEmail.setText(user.getEmail());
                if (user.getPsychologist().toString().equals("Yes")) {
                    tvPsychologist.setVisibility(View.VISIBLE);
                }
                UsersData.setUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                } else if (id == R.id.nav_post) {
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_POST;
                } else if (id == R.id.nav_search) {
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_SEARCH;
                } else if (id == R.id.nav_posted) {
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_POSTED;
                } else if (id == R.id.nav_profile_info) {
                    navItemIndex = 4;
                    CURRENT_TAG = TAG_PROFILE_INFO;
                } else if (id == R.id.nav_share) {
                    shareApp();
                } else if (id == R.id.nav_feedback) {
                    feedback();
                } else if (id == R.id.nav_log_out) {
                    logout();
                } else {
                    navItemIndex = 0;
                }

                drawer.closeDrawer(Gravity.START);
                // loadFragment on navigation item click
                loadHomeFragment();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_regular, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_exit) {
            moveTaskToBack(true);
            return true;
        }

        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                if (shouldLoadHomeFragOnBackPress) {
                    if (navItemIndex != 0) {
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        loadHomeFragment();
                    }
                }

                Home homeFrag = new Home();
                return homeFrag;

            case 1:
                PostQuestion postQuestion = new PostQuestion();
                return postQuestion;

            case 2:
                SearchQuestion searchQuestion = new SearchQuestion();
                return searchQuestion;

            case 3:
                ViewQuestions viewQuestions = new ViewQuestions();
                return viewQuestions;

            case 4:
                ProfileInfo profileInfo = new ProfileInfo();
                return profileInfo;


            default:
                return new Home();

        }
    }

    public void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();


        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            return;
        }

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frameUser, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mRunnable != null) {
            mHandler.post(mRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void selectNavMenu() {

        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                navigationView.getMenu().getItem(navItemIndex).setChecked(true);
                loadHomeFragment();

                return;
            }
        }

        if (navItemIndex == 0) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setTitle("Exit!")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DashboardRegular.this, Login.class));
        finish();
    }

    public void feedback() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(intent);
    }

    public void shareApp() {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Download the app from https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(sharingIntent.createChooser(sharingIntent, "Share Via"));
    }
}
