package com.example.findmehere;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class messgesAdaptor extends RecyclerView.Adapter {
    Context context;
    ArrayList<msgModel> msgAdaptorArray;
    int ITEM_SEND=1;
    int ITEM_RECIEVER=2;


    public messgesAdaptor(Context context, ArrayList<msgModel> msgAdaptorArray) {
        this.context = context;
        this.msgAdaptorArray = msgAdaptorArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view=LayoutInflater.from(context).inflate(R.layout.reciever_layout,parent,false);
            return new recieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModel msg = msgAdaptorArray.get(position);

        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.tvSenderMsg.setText(msg.getMessege());

            // Load profile picture using Base64 decoding
            String base64Image = msg.getProfile();
            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    byte[] bytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    viewHolder.ivSenderImg.setImageBitmap(bitmap);
                } catch (IllegalArgumentException e) {
                    // Handle exception for invalid Base64 strings
                    viewHolder.ivSenderImg.setImageResource(R.drawable.placeholder_image);
                }
            } else {
                viewHolder.ivSenderImg.setImageResource(R.drawable.placeholder_image); // Default placeholder if no profile image
            }

        } else {
            recieverViewHolder viewHolder = (recieverViewHolder) holder;
            viewHolder.tvRecieverMsg.setText(msg.getMessege());

            // Load profile picture using Base64 decoding
            String base64Image = msg.getProfile();
            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    byte[] bytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    viewHolder.ivRecieverImg.setImageBitmap(bitmap);
                } catch (IllegalArgumentException e) {
                    // Handle exception for invalid Base64 strings
                    viewHolder.ivRecieverImg.setImageResource(R.drawable.placeholder_image);
                }
            } else {
                viewHolder.ivRecieverImg.setImageResource(R.drawable.placeholder_image); // Default placeholder if no profile image
            }
        }
    }
    @Override
    public int getItemCount() {
        return msgAdaptorArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModel msg = msgAdaptorArray.get(position);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getUid().equals(msg.getSenderId())) {
            // Message sent by the current user
            return ITEM_SEND;
        } else {
            // Message received by the current user
            return ITEM_RECIEVER;
        }
    }


    static class senderViewHolder extends RecyclerView.ViewHolder{
        ImageView ivSenderImg;
        TextView tvSenderMsg;


        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            ivSenderImg=itemView.findViewById(R.id.ivSenderImg);
            tvSenderMsg=itemView.findViewById(R.id.tvSenderMsg);

        }
    }

    static class recieverViewHolder extends RecyclerView.ViewHolder{
        ImageView ivRecieverImg;
        TextView tvRecieverMsg;
        public recieverViewHolder(@NonNull View itemView) {
            super(itemView);

            ivRecieverImg=itemView.findViewById(R.id.ivRecieverImg);
            tvRecieverMsg=itemView.findViewById(R.id.tvRecieverMsg);
        }
    }
}
