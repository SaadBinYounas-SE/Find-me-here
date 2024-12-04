package com.example.findmehere;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessegesContactAdaptor extends RecyclerView.Adapter<MessegesContactAdaptor.viewholder> {
    Context activity;
    ArrayList<User> contactArray;
    String userId;
    public MessegesContactAdaptor(FragmentActivity activity, ArrayList<User> msgsContactArray,String userid) {
        this.activity = activity;
        contactArray = msgsContactArray;
//        this.userId=userid;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_msg_contacts,parent,false);
        return new viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MessegesContactAdaptor.viewholder holder, int position) {

        User users = contactArray.get(position);
        holder.tvContactUsername.setText(users.getFullName());

        // Load profile picture using Base64 decoding
        String base64Image = contactArray.get(position).getProfilePic();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] bytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.ivContactImg.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // Handle exception for invalid Base64 strings
                holder.ivContactImg.setImageResource(R.drawable.placeholder_image);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId=users.getId();
                Intent intent = new Intent(activity, chatBoxActivity.class);
                intent.putExtra("userId",userId);
                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return contactArray.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        ImageView ivContactImg;
        TextView tvContactUsername;
        TextView tvContactMsgCount;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            ivContactImg = itemView.findViewById(R.id.ivContactImg);
            tvContactMsgCount = itemView.findViewById(R.id.tvContactMsgCount);
            tvContactUsername = itemView.findViewById(R.id.tvContactUsername);
        }
    }
}
