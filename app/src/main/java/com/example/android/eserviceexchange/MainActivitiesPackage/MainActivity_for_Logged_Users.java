package com.example.android.eserviceexchange.MainActivitiesPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.eserviceexchange.NotificationsAndRepliesPackage.NotificationFragment;
import com.example.android.eserviceexchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity_for_Logged_Users extends AppCompatActivity {
    //firebase related declarations
    private FirebaseFirestore firestore;
    private String registered_user_id;
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    //UI declarations
    private Toolbar mainLogToolBar;
    private FloatingActionButton addPostBtn;
    private BottomNavigationView bttmNavBar;
    private LoggedUserHomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private UserAccountFragment userAccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for__logged__users);
        mAuth = FirebaseAuth.getInstance();
        //UI related initializations
        mainLogToolBar = (Toolbar) findViewById(R.id.mainLoggedtoolbar);
        setSupportActionBar(mainLogToolBar);
        getSupportActionBar().setTitle("E-Service Exchange");
        addPostBtn = (FloatingActionButton) findViewById(R.id.addpostbtn);
        if (mAuth.getCurrentUser() != null) {

            bttmNavBar = (BottomNavigationView) findViewById(R.id.bottomNavMenu);

            //Fragments
            homeFragment = new LoggedUserHomeFragment();
            notificationFragment = new NotificationFragment();
            userAccountFragment = new UserAccountFragment();

            //on click listeners
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity_for_Logged_Users.this, NewPostActivity.class));
                }
            });
            replaceFragment(homeFragment);
            bttmNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.bottomHome:
                            replaceFragment(homeFragment);
                            return true;
                        case R.id.bottomNotify:
                            replaceFragment(notificationFragment);
                            return true;
                        case R.id.bottomAccount:
                            replaceFragment(userAccountFragment);
                            return true;

                        default:
                            return false;
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_for_logged_users, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.signout_act:
                mAuth.signOut();
                startActivity(new Intent(MainActivity_for_Logged_Users.this, MainActivity.class));
                return true;

            case R.id.acc_settings:
                startActivity(new Intent(MainActivity_for_Logged_Users.this, ChangeSettings.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        registered_user_id = firebaseAuth.getCurrentUser().getUid();
        firestore.collection("UsersAccount").document(registered_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists() == false) {
                        startActivity(new Intent(MainActivity_for_Logged_Users.this, SetUpAccount.class));
                        finish();
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(MainActivity_for_Logged_Users.this, "Data retrieval Error: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainViewForLoggedUser, fragment);
        fragmentTransaction.commit();
    }
}
