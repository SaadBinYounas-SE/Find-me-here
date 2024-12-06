package com.example.findmehere;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile1_fragment extends Fragment {
    ImageView ivProfileDp;
    TextView tvtotalPost, tvFound, tvLost,tvUsernameProfile;
    ViewPager vpViewPosts;
    TabLayout tlPosts;
    int countLost=0,countFound=0;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot= inflater.inflate(R.layout.fragment_profile, container, false);

        init(viewRoot);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String profilePicUrl = dataSnapshot.child("profilePicUrl").getValue(String.class);

                        // Update your UI elements with the retrieved data
                        tvUsernameProfile.setText(fullName);
                        Glide.with(getContext())
                                .load(profilePicUrl)
                                .placeholder(R.drawable.placeholder_image)
                                .into(ivProfileDp);
                    } else {
                        // Handle the case where user data doesn't exist
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }



        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
            Query query = postsRef.orderByChild("userId").equalTo(currentUserId);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String postStatus = postSnapshot.child("status").getValue(String.class);

                        if(postStatus.equals("LOST"))
                            countLost++;
                        else if (postStatus.equals("FOUND")) {
                            countFound++;
                        }
                    }

                    String found= String.valueOf(countFound);
                    String lost= String.valueOf(countLost);
                    String total= String.valueOf((countFound+countLost));
                    tvFound.setText(found);
                    tvLost.setText(lost);
                    tvtotalPost.setText(total);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }


        return viewRoot;
    }
    void init(View viewRoot)
    {
        ivProfileDp=viewRoot.findViewById(R.id.ivProfileDp);
        tvtotalPost=viewRoot.findViewById(R.id.tvTotalPost);
        tvFound=viewRoot.findViewById(R.id.tvFound);
        tvLost=viewRoot.findViewById(R.id.tvLost);
        tvUsernameProfile=viewRoot.findViewById(R.id.tvUsernameProfile);
        vpViewPosts=viewRoot.findViewById(R.id.vpViewPosts);
        tlPosts=viewRoot.findViewById(R.id.tlPosts);

        tlPosts.setupWithViewPager(vpViewPosts);


        tlPosts.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.white), ContextCompat.getColor(getActivity(), R.color.peach));


    }
}