package com.example.findmehere;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class newPostFragment extends Fragment {

    DatabaseReference userId;

    Button btnPost;
    TextInputEditText etItemName, etDescription, etLocation, etMessege;
    RadioButton rbLost, rbFound;
    ProgressBar progressBarNewPost;

    String status;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_post, container, false);

        init(rootView);

        rbLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "LOST";
            }
        });

        rbFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "FOUND";
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });

        return rootView;
    }

    protected void init(View rootView) {

        etItemName = rootView.findViewById(R.id.etItemName);
        etDescription = rootView.findViewById(R.id.etDescription);
        etLocation = rootView.findViewById(R.id.etLocation);
        etMessege = rootView.findViewById(R.id.etPostMessege);
        btnPost = rootView.findViewById(R.id.btnPost);
        rbLost = rootView.findViewById(R.id.rbLost);
        rbFound = rootView.findViewById(R.id.rbFound);
        progressBarNewPost = rootView.findViewById(R.id.progressBarNewPost);
    }

    void postData() {
        progressBarNewPost.setVisibility(View.VISIBLE);

        etItemName.setEnabled(false);
        etDescription.setEnabled(false);
        etLocation.setEnabled(false);
        etMessege.setEnabled(false);
        btnPost.setEnabled(false);

        String itemName = Objects.requireNonNull(etItemName.getText()).toString().trim();
        String description = Objects.requireNonNull(etDescription.getText()).toString();
        String location = Objects.requireNonNull(etLocation.getText()).toString();
        String messege = Objects.requireNonNull(etMessege.getText()).toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userId = FirebaseDatabase.getInstance().getReference().child("User").child(currentUser.getUid());
            userId.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("fullName").getValue(String.class);
                        String id = snapshot.child("id").getValue(String.class);

                        // Save post details to Firebase Realtime Database
                        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts").push();
                        modelPosts newPost = new modelPosts(itemName, fullName, description, messege, status, location, id);

                        postsRef.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBarNewPost.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    etItemName.setEnabled(true);
                                    etDescription.setEnabled(true);
                                    etLocation.setEnabled(true);
                                    etMessege.setEnabled(true);
                                    btnPost.setEnabled(true);

                                    Toast.makeText(getActivity(), "Post added successfully", Toast.LENGTH_SHORT).show();

                                    etItemName.setText("");
                                    etDescription.setText("");
                                    etLocation.setText("");
                                    etMessege.setText("");
                                } else {
                                    Toast.makeText(getActivity(), "Failed to add post", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBarNewPost.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBarNewPost.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
