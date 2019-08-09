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

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ProgressBar;
import com.google.firebase.auth.AuthResult;


public class SignUp extends AppCompatActivity {
    private EditText signupemail,password,confirmPass;
    private Button signupBtn;

    private ProgressBar progressBarUp;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth= FirebaseAuth.getInstance();

        signupemail= (EditText) findViewById(R.id.signupemail);
        password=(EditText) findViewById(R.id.signuppass);
        confirmPass= (EditText) findViewById(R.id.signuppass_confirm);
        signupBtn= (Button) findViewById(R.id.signup_btn);
        progressBarUp= (ProgressBar) findViewById(R.id.iprogressbarup);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUp = signupemail.getText().toString();
                String passUp = password.getText().toString();
                String conPassUp = confirmPass.getText().toString();
                progressBarUp.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(emailUp) && !TextUtils.isEmpty(passUp) &&
                        !TextUtils.isEmpty(conPassUp) ){
                    if(passUp.equals(conPassUp)){
                        signupBtn.setEnabled(false);
                        mAuth.createUserWithEmailAndPassword(emailUp,passUp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //add email verificationhere
                                    startActivity(new Intent(SignUp.this, SetUpAccount.class));
                                    Toast.makeText(SignUp.this,"Please Verify Your Account",Toast.LENGTH_LONG).show();
                                    /*var user = firebase.auth().currentUser;

                                     user.sendEmailVerification().then(function() {
                                     // Email sent.
                                     }).catch(function(error) {
                                        // An error happened.
                                      });
                                       */
                                    finish();
                                }else{
                                    signupBtn.setEnabled(true);
                                    String errorMSG= task.getException().getMessage();
                                    Toast.makeText(SignUp.this,"Error: "+ errorMSG,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(SignUp.this,"Passwords don't match...",Toast.LENGTH_LONG).show();
                    }
                }else {Toast.makeText(SignUp.this,"Please fill all required fields",Toast.LENGTH_LONG).show();}
                progressBarUp.setVisibility(View.INVISIBLE);
            }
        });
    }
}
