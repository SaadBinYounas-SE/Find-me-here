package com.example.findmehere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

        msgModel msg=msgAdaptorArray.get(position);
        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.tvSenderMsg.setText(msg.getMessege());

            Glide.with(context.getApplicationContext())
                    .load(msg.getProfile())
                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                    .into(viewHolder.ivSenderImg);
        } else {
            recieverViewHolder viewHolder = (recieverViewHolder) holder;
            viewHolder.tvRecieverMsg.setText(msg.getMessege());

            Glide.with(context.getApplicationContext())
                    .load(msg.getProfile())
                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                    .into(viewHolder.ivRecieverImg);


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
