package com.example.findmehere;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessegesContact extends Fragment {
    FirebaseAuth auth;
    RecyclerView rvMessegesContact;
    MessegesContactAdaptor messegesContactAdaptor;
    FirebaseDatabase database;
    String recieverId;
    ArrayList<User> msgsContactArray;
    String checkParent="false";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_messeges, container, false);

        Bundle args = getArguments();
        if (args != null) {
            recieverId = args.getString("userId");
            checkParent=args.getString("flag");
            Log.d("messeges contact",recieverId);
        }

        init(view);

        if(checkParent.contains("false")){

        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId=currentUser.getUid();

        DatabaseReference databaseReference = database.getReference().child("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!user.getId().equals(currentUserId)) {
                        msgsContactArray.add(user);
                    }

                }
                messegesContactAdaptor.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    void init(View view)
    {
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        msgsContactArray=new ArrayList<>();

        rvMessegesContact=view.findViewById(R.id.rvMessegesContact);
        rvMessegesContact.setLayoutManager(new LinearLayoutManager(getContext()));

        messegesContactAdaptor=new MessegesContactAdaptor(getActivity(),msgsContactArray,recieverId);
        rvMessegesContact.setAdapter(messegesContactAdaptor);

    }
}