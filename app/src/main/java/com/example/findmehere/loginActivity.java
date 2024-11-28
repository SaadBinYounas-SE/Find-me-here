package com.example.findmehere;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    Button btnSignup,btnLogin;
    TextInputEditText etEmailLogin, etPasswordLogin;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBarLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        init();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
            return;
        }



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginActivity.this, signup.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLogin();
            }
        });
    }
    protected void init()
    {
        btnSignup=findViewById(R.id.btnSignup);
        btnLogin=findViewById(R.id.btnLogin);

        etEmailLogin=findViewById(R.id.etEmailLogin);
        etPasswordLogin=findViewById(R.id.etPasswordLogin);

        progressBarLogin=findViewById(R.id.progressBarLogin);

        firebaseAuth=FirebaseAuth.getInstance();
    }
    protected void checkLogin()
    {
        progressBarLogin.setVisibility(View.VISIBLE);

        btnLogin.setEnabled(false);
        etEmailLogin.setEnabled(false);
        etEmailLogin.setEnabled(false);

        String email = etEmailLogin.getText().toString().trim();
        String password = etPasswordLogin.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBarLogin.setVisibility(View.GONE);
                            btnLogin.setEnabled(true);
                            etEmailLogin.setEnabled(true);
                            etEmailLogin.setEnabled(true);

                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                startActivity(new Intent(loginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(loginActivity.this, "Login failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

}