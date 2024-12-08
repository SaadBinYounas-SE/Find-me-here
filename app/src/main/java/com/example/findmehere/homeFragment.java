package com.example.findmehere;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class homeFragment extends Fragment {

    ArrayList<modelPosts> Posts = new ArrayList<>();
    MyAdapterPosts myAdapter;
    RecyclerView rvPosts;
    SearchView search_view;
    View btnLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        init(rootView);

        getDataFromDb();

        search_view.setOnQueryTextListener(new  SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the dataset based on the search query
                filterPosts(newText);
                return true;
            }
        });

        btnLogout.setOnClickListener(v -> showLogoutDialog()); // Set the Logout action

        return rootView;
    }
    protected void init(View rootView)
    {
        rvPosts = rootView.findViewById(R.id.rvPosts);
        search_view=rootView.findViewById(R.id.search_view);
        btnLogout = rootView.findViewById(R.id.btnLogout);

        rvPosts.setHasFixedSize(true);

//        myAdapter = new MyAdapterPosts(Posts);

        myAdapter = new MyAdapterPosts(Posts, new MyAdapterPosts.OnItemClickListener() {
            @Override
            public void onItemClick(modelPosts post) {
                Intent intent=new Intent(getContext(),viewPostDetails.class);
                String userid=post.getUserId();
                String itemName = post.getItemName();
                String description = post.getDescription();
                String location = post.getLocation();
                String status=post.getStatus();
                String messege = post.getMessege();



                intent.putExtra("userid",userid);
                intent.putExtra("itemName",itemName);
                intent.putExtra("description",description);
                intent.putExtra("status",status);
                intent.putExtra("location",location);
                intent.putExtra("messege",messege);

                startActivity(intent);
            }
        });
        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPosts.setAdapter(myAdapter);
    }
    private void filterPosts(String query) {
        ArrayList<modelPosts> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase(); // Convert query to lowercase for case-insensitive search

        for (modelPosts post : Posts) {
            // Check if the query matches any of the relevant fields
            if (post.getItemName().toLowerCase().contains(lowerCaseQuery) ||
                    post.getStatus().toLowerCase().contains(lowerCaseQuery) ||
                    post.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                    post.getLocation().toLowerCase().contains(lowerCaseQuery) ||
                    post.getPostedBy().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(post);
            }
        }
        // Update the RecyclerView adapter with the filtered list
        myAdapter.setFilteredPosts(filteredList);

        // Display a message if no posts match the search query
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No results found!", Toast.LENGTH_SHORT).show();
        }
    }

    //jab home load hua to porana post aa gyi
    @Override
    public void onResume() {
        super.onResume();
        getDataFromDb();
    }

    protected void getDataFromDb()
    {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list before adding new data
                Posts.clear();

                // Iterate through the dataSnapshot to retrieve each post
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // Convert the dataSnapshot to a model object and add it to the list
                    modelPosts post = postSnapshot.getValue(modelPosts.class);

                    Posts.add(post);
                }

                // Notify the adapter of the data change
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void logout() {
        // Firebase logout
        FirebaseAuth.getInstance().signOut();

        // Navigate to Login activity
        Intent intent = new Intent(getActivity(), loginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

}