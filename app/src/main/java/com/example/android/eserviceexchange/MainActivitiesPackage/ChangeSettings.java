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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChangeSettings extends AppCompatActivity {
    private EditText userName, phone;
    private Button setUpSub;
    // to create and insert in a db
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_settings);

        Toolbar accTool = (Toolbar) findViewById(R.id.newposttoolbar);
        setSupportActionBar(accTool);

        userName = (EditText) findViewById(R.id.usernames);
        phone = (EditText) findViewById(R.id.phonenums);
        setUpSub = (Button) findViewById(R.id.setupbtns);

        // db initialization
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firestore.collection("UsersAccount").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()== true) {


                        String userNameR = task.getResult().getString("Username");

                        String phoneR = task.getResult().getString("Phone");
                        phone.setText(phoneR);
                        userName.setText(userNameR);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(ChangeSettings.this, "Data retrieval Error: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });


        setUpSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userNameUp = userName.getText().toString();
                String phn = phone.getText().toString();
                if (!TextUtils.isEmpty(phn) && !TextUtils.isEmpty(userNameUp)) {
                    setUpSub.setEnabled(false);
                    //change this to update format
                    firestore.collection("UsersAccount").document(user_id)
                            .update("Username", userNameUp,"Phone", phn)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangeSettings.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ChangeSettings.this, MainActivity_for_Logged_Users.class));
                                        finish();
                                    } else {
                                        setUpSub.setEnabled(true);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ChangeSettings.this, "DB Error: " + error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {Toast.makeText(ChangeSettings.this,"Please fill all required fields",Toast.LENGTH_LONG).show();}
            }
        });
    }
}




