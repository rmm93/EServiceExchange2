package com.example.android.eserviceexchange.MainActivitiesPackage;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.eserviceexchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    private EditText signinEmail;
    private EditText signinPass;
    private Button signinBtn, signUpInBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        signinEmail = (EditText) findViewById(R.id.signupemail);
        signinPass = (EditText) findViewById(R.id.signuppass);
        signinBtn = (Button) findViewById(R.id.signup_btn);
        signUpInBtn = (Button) findViewById(R.id.signUpinSignIn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailSignIn = signinEmail.getText().toString();
                String passSignIn = signinPass.getText().toString();

                if (!TextUtils.isEmpty(emailSignIn) && !TextUtils.isEmpty(passSignIn)) {
                    //simpleProgressBar.setVisibility(View.VISIBLE);
                    signinBtn.setEnabled(false);
                    mAuth.signInWithEmailAndPassword(emailSignIn, passSignIn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //login is successful so send to main and enable posting
                                startActivity(new Intent(SignIn.this, MainActivity_for_Logged_Users.class));
                            } else {

                                String errorMsg = task.getException().getMessage();
                                Toast.makeText(SignIn.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                                signinBtn.setEnabled(true);
                            }
                            //simpleProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }else {Toast.makeText(SignIn.this,"Please fill all required fields",Toast.LENGTH_LONG).show();}
            }
        });

        signUpInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpInBtn.setEnabled(false);
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(SignIn.this, "Welcome back", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
