package com.example.findmehere;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class signup extends AppCompatActivity {
    EditText etFirstName, etLastName, etEmail,etPhone,etPassword,etRePassword;
    Button btnSign, btnAddPic;
    ImageView ivDp;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference signinDb;
    FirebaseStorage storage;
    StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        init();

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(signup.this, loginActivity.class);
        startActivity(intent);
        finish();
    }
    protected void init()
    {
        btnSign=findViewById(R.id.btnSign);
        etFirstName=findViewById(R.id.etFirstName);
        etLastName=findViewById(R.id.etLastName);
        etEmail=findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);
        etPassword=findViewById(R.id.etPassword);
        etRePassword=findViewById(R.id.etRePassword);
        btnAddPic=findViewById(R.id.btnAddPic);
        ivDp=findViewById(R.id.ivDp);
        progressBar=findViewById(R.id.progressBar);

        database=FirebaseDatabase.getInstance();
        signinDb=database.getReference("User");
        firebaseAuth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("images").child("my_image.jpg");
    }
    protected void registerUser() {
        progressBar.setVisibility(View.VISIBLE);

        btnSign.setEnabled(false);
        etFirstName.setEnabled(false);
        etLastName.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
        etRePassword.setEnabled(false);
        etPhone.setEnabled(false);

        String fname = etFirstName.getText().toString().trim();
        String lname = etLastName.getText().toString().trim();
        String fullName = fname + " " + lname;

        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String repassword = etRePassword.getText().toString();

        if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            Toast.makeText(this, "All Fields should be filled", Toast.LENGTH_SHORT).show();
            resetProgress();
            return;
        }

        if (!password.equals(repassword)) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
            resetProgress();
            return;
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            resetProgress();
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "Invalid Phone", Toast.LENGTH_SHORT).show();
            resetProgress();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
            resetProgress();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            saveUserData(userId, fullName, email, phone, password);
                        } else {
                            Toast.makeText(signup.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            resetProgress();
                        }
                    }
                });
    }

    private void saveUserData(String userId, String fullName, String email, String phone, String password) {
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child("profile_pictures/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUri) {
                                            String profileUrl = downloadUri.toString();
                                            User userData = new User(userId, fullName, email, phone, password, profileUrl);
                                            signinDb.child(userId).setValue(userData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(signup.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(signup.this, loginActivity.class);
                                                                FirebaseAuth.getInstance().signOut();
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(signup.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                                            }
                                                            resetProgress();
                                                        }
                                                    });
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(signup.this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            resetProgress();
                        }
                    });
        } else {
            // Save user data without profile image
            User userData = new User(userId, fullName, email, phone, password, null);
            signinDb.child(userId).setValue(userData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(signup.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(signup.this, loginActivity.class);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(signup.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                            }
                            resetProgress();
                        }
                    });
        }
    }

    private void resetProgress() {
        progressBar.setVisibility(View.GONE);
        btnSign.setEnabled(true);
        etFirstName.setEnabled(true);
        etLastName.setEnabled(true);
        etEmail.setEnabled(true);
        etPassword.setEnabled(true);
        etRePassword.setEnabled(true);
        etPhone.setEnabled(true);
    }

    void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivDp.setImageURI(data.getData());
        }
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+[0-9]+$");
    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
