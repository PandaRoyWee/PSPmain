package com.example.pspmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {

    EditText signName, signEmail, signPhone, signPass;
    Button Btnsign;
    TextView signupTV;

    FirebaseAuth auth;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signName = findViewById(R.id.name_signup);
        signEmail = findViewById(R.id.email_signup);
        signPhone = findViewById(R.id.phone_signup);
        signPass = findViewById(R.id.pass_signup);
        Btnsign = findViewById(R.id.signupBtn);
        signupTV = findViewById(R.id.loginText);

        //Take firebase instance here
        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Account!");
        dialog.setCancelable(false);

        Btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailID, password, userName, userNumber;

                userName = signName.getText().toString();
                userNumber = signPhone.getText().toString();
                emailID = signEmail.getText().toString();
                password = signPass.getText().toString();

                if (userName.isEmpty()){

                    signName.setError("Please enter your Name");
                    signName.requestFocus();

                } else if (userNumber.isEmpty()){

                    signPhone.setError("Please enter your Phone Number");
                    signPhone.requestFocus();

                } else if (emailID.isEmpty()){

                    signEmail.setError("Please enter your Email");
                    signEmail.requestFocus();

                } else if (password.isEmpty()){

                    signPass.setError("Please enter your Password");
                    signPass.requestFocus();

                } else {
                    dialog.show();
                    auth.createUserWithEmailAndPassword(emailID, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                startActivity(new Intent(register.this, MainActivity.class));
                                finish();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });

        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(register.this, login.class));

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {

            startActivity(new Intent(register.this, MainActivity.class));
            finish();

        }
    }
}