package com.example.findmehere;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class homeFragment extends Fragment {

    ArrayList<modelPosts> Posts = new ArrayList<>();
    MyAdapterPosts myAdapter;
    RecyclerView rvPosts;
    SearchView search_view;


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


        return rootView;
    }
    protected void init(View rootView)
    {
        rvPosts = rootView.findViewById(R.id.rvPosts);
        search_view=rootView.findViewById(R.id.search_view);

        rvPosts.setHasFixedSize(true);

//        myAdapter = new MyAdapterPosts(Posts);

        myAdapter = new MyAdapterPosts(Posts, new MyAdapterPosts.OnItemClickListener() {
            @Override
            public void onItemClick(modelPosts post) {
//                Intent intent=new Intent(getContext(),viewPostDetails.class);
                String userid=post.getUserId();
                String itemName = post.getItemName();
                String description = post.getDescription();
                String location = post.getLocation();
                String status=post.getStatus();
                String messege = post.getMessege();
                String itemPostImageUrl = post.getItemPostImageUrl();

                Log.d("home fragment ","item name : "+itemName);

//                intent.putExtra("userid",userid);
//                intent.putExtra("itemName",itemName);
//                intent.putExtra("description",description);
//                intent.putExtra("status",status);
//                intent.putExtra("location",location);
//                intent.putExtra("messege",messege);
//                intent.putExtra("itemPostImageUrl",itemPostImageUrl);
//
//                startActivity(intent);
            }
        });
        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPosts.setAdapter(myAdapter);
    }
    private void filterPosts(String query) {
        ArrayList<modelPosts> filteredList = new ArrayList<>();
        for (modelPosts post : Posts) {
            // Check if the post's username or item name contains the search query
            if (post.getItemName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(post);
            }
        }
        // Update the RecyclerView adapter with the filtered list
        myAdapter.setFilteredPosts(filteredList);
    }
    protected void getDataFromDb()
    {
//        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

//        postsRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Clear the existing list before adding new data
//                Posts.clear();
//
//                // Iterate through the dataSnapshot to retrieve each post
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    // Convert the dataSnapshot to a model object and add it to the list
//                    modelPosts post = postSnapshot.getValue(modelPosts.class);
//
//                    Posts.add(post);
//                }
//
//                // Notify the adapter of the data change
//                myAdapter.notifyDataSetChanged();
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle errors
//            }
//        });
    }

}