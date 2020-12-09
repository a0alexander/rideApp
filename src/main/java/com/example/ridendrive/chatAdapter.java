package com.example.ridendrive;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ViewHolder1> {

    ArrayList<chats> usersArrayList;
    Context  context;
    public class ViewHolder1 extends RecyclerView.ViewHolder {



        Button sendmessagebtn;
        TextView messageText, username;
        ImageView userImageinChat;







        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            //sendmessagebtn = itemView.findViewById(R.id.sendMessageButton);
            messageText = itemView.findViewById(R.id.message_inchat_recycler);
            userImageinChat = itemView.findViewById(R.id.userImage_in_chat);
            username = itemView.findViewById(R.id.username_in_chat);








        }




    }

    public chatAdapter(ArrayList<chats> list1){

        this.usersArrayList = list1;


    }






    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatrecyle_item, parent,false);
        ViewHolder1 fe = new chatAdapter.ViewHolder1(v);
        context = parent.getContext();
        return  fe;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder1 holder, int position) {

        chats eachMessage = usersArrayList.get(position);



            holder.messageText.setText(eachMessage.message);
            try{
                holder.username.setText(capitalize(eachMessage.username));
            }
            catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }




            if(eachMessage.profilePic!=null){
                Picasso.get().load(eachMessage.profilePic).into(holder.userImageinChat);

            }
            else{
                Picasso.get().load(R.drawable.icons8_cat_profile_60px).into(holder.userImageinChat);
            }





    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public String capitalize(String str){


        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }

        return builder.toString();
    }






}
