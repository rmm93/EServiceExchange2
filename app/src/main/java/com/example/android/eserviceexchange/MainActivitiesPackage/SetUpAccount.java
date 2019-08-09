package com.example.android.eserviceexchange.MainActivitiesPackage;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.eserviceexchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SetUpAccount extends AppCompatActivity {
    private EditText name, userName, phone, age;
    private Button setUpSub;
    private Spinner cityDropDown;

    // to create and insert in a db
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_account);

        Toolbar accTool = (Toolbar) findViewById(R.id.newposttoolbar);
        setSupportActionBar(accTool);

        name = (EditText) findViewById(R.id.name);
        userName=(EditText) findViewById(R.id.username);
        age = (EditText) findViewById(R.id.age);
        phone = (EditText) findViewById(R.id.phonenum);
        setUpSub = (Button) findViewById(R.id.setupbtn);
        cityDropDown= (Spinner) findViewById(R.id.citydropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.cities_array, android.R.layout.simple_spinner_item);
        cityDropDown.setAdapter(adapter);

        // db initialization
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        setUpSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUp = name.getText().toString();
                String userNameUp= userName.getText().toString();
                String ageUp = age.getText().toString();
                int AGE = Integer.parseInt(ageUp);
                String phn = phone.getText().toString();
                String cityUp= cityDropDown.getSelectedItem().toString();

                if (!TextUtils.isEmpty(phn) && !TextUtils.isEmpty(nameUp) && !TextUtils.isEmpty(userNameUp) && !TextUtils.isEmpty(ageUp)) {
                    setUpSub.setEnabled(false);
                    if(AGE<18){
                        //parents guidance is advised
                    }
                    Map<String, String> userAccMap = new HashMap<>();
                    userAccMap.put("Name", nameUp);
                    userAccMap.put("Username",userNameUp);
                    userAccMap.put("Age", ageUp);
                    userAccMap.put("Phone", phn);
                    userAccMap.put("City", cityUp);
                    firestore.collection("UsersAccount").document(user_id)
                            .set(userAccMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SetUpAccount.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SetUpAccount.this, MainActivity_for_Logged_Users.class));
                                        finish();
                                    } else {
                                        setUpSub.setEnabled(true);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SetUpAccount.this, "DB Error: " + error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {Toast.makeText(SetUpAccount.this,"Please fill all required fields",Toast.LENGTH_LONG).show();}
            }
        });
    }
}




