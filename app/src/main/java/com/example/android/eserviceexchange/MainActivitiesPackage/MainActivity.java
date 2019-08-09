package com.example.android.eserviceexchange.MainActivitiesPackage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.eserviceexchange.R;
//import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {
    private Toolbar mainToolBar;
    private HomeFragment homeFragment;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    private FirebaseOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseApp.initializeApp(MainActivity.this);
        mainToolBar= (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("E-Service Exchange");
        homeFragment= new HomeFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // But FIRST check account info and verification!!!!
            startActivity(new Intent(MainActivity.this, MainActivity_for_Logged_Users.class));
        } else {
            // No user is signed in and can only view
            replaceFragment(homeFragment);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.signin_act:
                startActivity(new Intent(MainActivity.this, SignIn.class));
                return true;

            case R.id.signup_act:
                startActivity(new Intent(MainActivity.this, SignUp.class));
                return true;

            default:
                    return false;
        }
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainView, fragment);
        fragmentTransaction.commit();
    }
}
