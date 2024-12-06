package com.example.findmehere;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class chatBoxActivity extends AppCompatActivity {
    String recieverName, recieverPhone, recieverEmail,recieverProfile;
    String senderName, senderPhone, senderId,senderProfile;
    String recieverId;
    ImageView ivCallChat,ivSend,ivRecieverProfile;
    RecyclerView rvChats;
    EditText etEnterMessege;
    TextView tvRecieverName;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderRoom, recieverRoom;
    ArrayList<msgModel> messegesArray;


    messgesAdaptor msgAdaptor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbox_activity);

        recieverId = getIntent().getStringExtra("userId");

        Log.d("reciever id", "id: " + recieverId);

        init();

        getRecieverData();
        getSenderData();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String thisUser = currentUser.getUid();
        senderRoom=thisUser+recieverId;
        recieverRoom=recieverId+thisUser;

        DatabaseReference rec_ref=FirebaseDatabase.getInstance().getReference().child("User").child(recieverId);

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messege=etEnterMessege.getText().toString();

                if(messege.isEmpty())
                {
                    Toast.makeText(chatBoxActivity.this,"Enter a Message first",Toast.LENGTH_SHORT).show();
                    return;
                }
                etEnterMessege.setText("");

                Date date=new Date();
                Log.d("sender id", "id: " + senderId);



                msgModel msg = new msgModel(messege,senderId,senderProfile,recieverId,date.getTime());
                firebaseDatabase=FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("chats").child(senderRoom).child("messeges").push()
                        .setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                firebaseDatabase.getReference().child("chats").child(recieverRoom).child("messeges")
                                        .push().setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });


            }
        });

        ivCallChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                rec_ref.child("phone").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        String phoneNumber = task.getResult().getValue(String.class);
                        if (phoneNumber != null) {
                            // Start the call intent
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(callIntent);
                        }
                    } else {
                        // Handle the case where the phone number is not found
                        Toast.makeText(chatBoxActivity.this, "Phone number not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        firebaseDatabase=FirebaseDatabase.getInstance();


        DatabaseReference chatReferenece=firebaseDatabase.getReference().child("chats").child(recieverRoom)
                .child("messeges");
        chatReferenece.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messegesArray.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    msgModel msgs=dataSnapshot.getValue(msgModel.class);
                    messegesArray.add(msgs);
                    Log.d("MessagesArraySize", "Size: " + messegesArray.size());
                }
                msgAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void init()
    {
        ivCallChat=findViewById(R.id.ivCallChat);
        ivSend=findViewById(R.id.ivSend);
        etEnterMessege=findViewById(R.id.etEnterMessege);
        ivRecieverProfile=findViewById(R.id.ivRecieverProfile);
        tvRecieverName=findViewById(R.id.tvRecieverName);

        messegesArray=new ArrayList<>();

        rvChats=findViewById(R.id.rvChats);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvChats.setLayoutManager(linearLayoutManager);

        msgAdaptor=new messgesAdaptor(chatBoxActivity.this,messegesArray);
        rvChats.setAdapter(msgAdaptor);

    }

    void getRecieverData()
    {
        DatabaseReference userRef;

        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(recieverId);

        if (recieverId != null) {
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // User data exists, retrieve the data
                        recieverName = snapshot.child("fullName").getValue(String.class);
                        recieverPhone = snapshot.child("phone").getValue(String.class);
                        recieverEmail = snapshot.child("email").getValue(String.class);
                        recieverProfile = snapshot.child("profilePic").getValue(String.class);

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        if (currentUser != null) {
                            String thisUser = currentUser.getUid();
                            if(!thisUser.equals(recieverId))
                            {
                                tvRecieverName.setText(recieverName);

                                Glide.with(getApplicationContext())
                                        .load(recieverProfile)
                                        .placeholder(R.drawable.placeholder_image)
                                        .into(ivRecieverProfile);
                            }

                        }

                    } else {
                        Toast.makeText(getParent(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                }
            });

        }
    }
    void getSenderData()
    {
        DatabaseReference currentUser;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();

        if (User != null) {
            currentUser = FirebaseDatabase.getInstance().getReference().child("User").child(User.getUid());

            currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        senderName = snapshot.child("fullName").getValue(String.class);
                        senderPhone = snapshot.child("phone").getValue(String.class);
                        senderId = snapshot.child("id").getValue(String.class);
                        senderProfile = snapshot.child("profilePic").getValue(String.class);




                    } else {
                        Toast.makeText(getParent(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getParent(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}

