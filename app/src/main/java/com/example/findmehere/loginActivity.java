package com.example.findmehere;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    TextView forget;
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

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etEmail = new EditText(view.getContext());
                AlertDialog.Builder forgetPasswordDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Forget Password Email...")
                        .setView(etEmail)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String email = etEmail.getText().toString().trim();
                                if(TextUtils.isEmpty(email))
                                {
                                    etEmail.setError("Give valid email address");
                                }
                                else
                                {
                                    ProgressDialog progressDialog = new ProgressDialog(loginActivity.this);
                                    progressDialog.show();
                                    firebaseAuth.sendPasswordResetEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(loginActivity.this, "check your inbox...", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(loginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    progressDialog.dismiss();
                                                }
                                            });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                forgetPasswordDialog.show();
            }
        });
    }
    protected void init()
    {
        btnSignup=findViewById(R.id.btnSignup);
        btnLogin=findViewById(R.id.btnLogin);

        etEmailLogin=findViewById(R.id.etEmailLogin);
        etPasswordLogin=findViewById(R.id.etPasswordLogin);
        forget=findViewById(R.id.forgotlogin);
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