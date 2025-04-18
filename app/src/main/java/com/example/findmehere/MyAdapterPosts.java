package com.example.findmehere;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterPosts extends RecyclerView.Adapter<MyAdapterPosts.ViewHolder> {

    ArrayList<modelPosts> Posts;
    private ViewHolder selectedViewHolder;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(modelPosts post);
    }

    MyAdapterPosts()
    {
        Posts=new ArrayList<>();
    }
    MyAdapterPosts(ArrayList<modelPosts> P)
    {
        Posts=P;
    }
    public MyAdapterPosts(ArrayList<modelPosts> P, OnItemClickListener listener) {
        this.Posts = P;
        this.listener = listener;
    }


    public void setSelectedViewHolder(ViewHolder viewHolder) {
        this.selectedViewHolder = viewHolder;
    }

    public ViewHolder getSelectedViewHolder() {
        return selectedViewHolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvItemName.setText(Posts.get(position).getItemName());
        holder.tvLocation.setText(Posts.get(position).getLocation());
        holder.tvStatus.setText(Posts.get(position).getStatus());
        holder.tvNameProfile.setText(Posts.get(position).getPostedBy());
        holder.tvPostTime.setText(Posts.get(position).getPostTime());

        // Load profile picture using Base64 decoding
        String base64Image = Posts.get(position).getPostedByPic();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] bytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.ivProfilePost.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // Handle exception for invalid Base64 strings
                holder.ivProfilePost.setImageResource(R.drawable.placeholder_image);
            }
        } else {
            holder.ivProfilePost.setImageResource(R.drawable.placeholder_image);
        }
    }

    @Override
    public int getItemCount() {
        return Posts.size();
    }

    public void setFilteredPosts(ArrayList<modelPosts> filteredPosts) {
        this.Posts = filteredPosts;
        notifyDataSetChanged(); // Notify RecyclerView of dataset change
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfilePost;
        TextView tvItemName,tvNameProfile, tvStatus,tvLocation,tvPostTime;
        Button btnView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            init();

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(Posts.get(position));
                        }
                    }
                }
            });
        }


        protected void init()
        {

            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvNameProfile=itemView.findViewById(R.id.tvNameProfile);
            tvLocation=itemView.findViewById(R.id.tvLocation);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            btnView=itemView.findViewById(R.id.btnView);
            tvPostTime=itemView.findViewById(R.id.tvPostTime);
            ivProfilePost=itemView.findViewById(R.id.ivProfilePost);

        }


        @Override
        public void onClick(View v) {

        }
    }
}