package com.example.ridendrive;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dpro.widgets.WeekdaysPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class rider_dash_recycler extends RecyclerView.Adapter<rider_dash_recycler.ViewHolder1> {

    private View.OnClickListener mOnItemClickListener;
    public ArrayList<users> userDetails;
    private Context context;
    int ly;
    private View.OnClickListener onItemClickListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();




    public  class ViewHolder1 extends  RecyclerView.ViewHolder {


        TextView ridername, ridertime,datetext;
        ImageButton button,button2_viewMap,button3_viewMap;
        WeekdaysPicker weekdaysPicker_recyc;
        Button accept_button,decline_button, pending_requestbtn;
        ImageView user_image;
        FloatingActionButton callbutton1,chatwithdriver1;


        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

//            mtextview1 = itemView.findViewById(R.id.nameOfSiteinRow);
//            timetext = itemView.findViewById(R.id.timeOfSiteinRow);
//            datetext = itemView.findViewById(R.id.dateOfSiteinRow);

            ridername = itemView.findViewById(R.id.nameOfRider);
            ridertime = itemView.findViewById(R.id.timeOfRider);
            accept_button = itemView.findViewById(R.id.accept_request);
            decline_button = itemView.findViewById(R.id.cancel_request);
            pending_requestbtn = itemView.findViewById(R.id.pending_request);
//            weekdaysPicker_recyc = itemView.findViewById(R.id.weekdays_recycler);
//            button = itemView.findViewById(R.id.addsiteButtonRecycler);
            button = itemView.findViewById(R.id.viewSitesinMap);
            user_image = itemView.findViewById(R.id.userImage_in_rider_dash);
//            button3_viewMap = itemView.findViewById(R.id.viewMySitesinMap);
            callbutton1 = itemView.findViewById(R.id.callButton);

            chatwithdriver1 = itemView.findViewById(R.id.chatwithDriver);

            getmyPersonalInfo();




            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

        }

    }

//    public recyclerviewAdapter(ArrayList<UserItem_recycler> list){
//            arraylist = list;
//
//    }

    public rider_dash_recycler(ArrayList<users> list1, @LayoutRes int layout, View.OnClickListener m1){
        this.userDetails = list1;
        this.ly =layout;
        this.mOnItemClickListener = m1;

    }


    public void setOnItemClickListener(View.OnClickListener itemClickListener) {


        mOnItemClickListener = itemClickListener;


    }



    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        if(viewType==R.layout.user_request_row){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rider_dash_row, parent,false);
        ViewHolder1 fe = new ViewHolder1(v);
        context = parent.getContext();
        return  fe;
//        }
//        else{
//            View v1 = LayoutInflater.from(parent.getContext()).inflate(ly, parent,false);
//            ViewHolder1 fe1 = new ViewHolder1(v1);
//            context = parent.getContext();
//
//            return  fe1;
//        }




    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder1 holder, final int position) {



        if(position==userDetails.size()&& holder.button!=null){


            // Toast.makeText(context, "Hey", Toast.LENGTH_SHORT).show();

        }

//        else if(position==driverLocations.size() && holder.button2_viewMap!=null){
//
//            holder.button2_viewMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context,Map2.class);
//                    Bundle bundle = new Bundle();
//                    CollectionReference d = FirebaseFirestore.getInstance().collection("Sites");
//                    bundle.putString("firestoreRef",d.getPath());
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
//            });
//
//        }

//        else if(position==driverLocations.size() && holder.button3_viewMap!=null){
//
//            holder.button3_viewMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context,Map2.class);
//                    Bundle bundle = new Bundle();
//
//                    CollectionReference d = FirebaseFirestore.getInstance()
//                            .collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .collection("myPlaces");
//
//                    bundle.putString("firestoreRef",d.getPath());
//
//                    intent.putExtras(bundle);
//
//
//
//                    context.startActivity(intent);
//                }
//            });
//
//        }


        else{

            final users u =   userDetails.get(position);
            //TODO add time the user requests here
            holder.ridername.setText(capitalize(u.firstName +" "+ u.lastName));
            holder.ridertime.setText(u.gettimeNormal(u.eventDate));


            holder.callbutton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phonenumber = u.phone;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phonenumber));
                    context.startActivity(intent);



                }
            });

//            holder.chatwithdriver1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    String driverUserIDforChat = u.ownerId;
////                    String username = u.firstName;
////                    String profilepicture = u.profilePic;
//                    Intent intent = new Intent(context.getApplicationContext(),chatActivity.class);
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("driverUID", driverUserIDforChat);
//                    bundle.putString("username",usernamefromfb);
//                  bundle.putString("profilePic",profilePicfromfb);
//                    intent.putExtras(bundle);
//
//                    context.startActivity(intent);
//
//                }
//            });

            holder.chatwithdriver1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ChatActivity.class);
                    intent.putExtra("userid", userDetails.get(position).ownerId);
                    context.startActivity(intent);
                }
            });

            if(u.profilePic!=null){
                Picasso.get().load(u.profilePic).into(holder.user_image);


            }
            else{

                Picasso.get().load(R.drawable.icons8_cat_profile_60px).into(holder.user_image);
            }


            try{
                if(u.accepted.equals("yes")){
                        holder.pending_requestbtn.setBackgroundResource(R.drawable.req_pending);
                        holder.pending_requestbtn.setText("Accepted");
                }
            }
            catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


//            holder.accept_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    holder.accept_button.setVisibility(View.GONE);
//                    holder.decline_button.setVisibility(View.VISIBLE);
//                    holder.accept_button.setEnabled(false);
//                    holder.decline_button.setEnabled(true);
//                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                    acceptRequest(position,"yes");
//                    markAsAcceptedforMe(position,"yes");
//
//
//
//
//                }
//            });


//            holder.decline_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    holder.decline_button.setVisibility(View.GONE);
//                    holder.accept_button.setVisibility(View.VISIBLE);
//                    holder.accept_button.setEnabled(true);
//                    holder.decline_button.setEnabled(false);
//                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                    acceptRequest(position,"no");
//                    markAsAcceptedforMe(position,"no");
//
//
//
//
//                }
//            });



            try{

                holder.ridertime.setText(u.gettimeNormal(u.eventDate));

            }
            catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


            //holder.mtextview1.setText(capitalize(u.siteName));
            //holder.datetext.setText(u.getDateNormal(u.eventDate));
            try{
                //holder.weekdaysPicker_recyc.setSelectedDays(u.daysofWeekarray);
            }
            catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            //holder.timetext.setText(u.gettimeNormal(u.eventDate));
        }



    }

    @Override
    public int getItemCount() {
        return userDetails.size();
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

    String usernamefromfb;
    String profilePicfromfb;
    public void getmyPersonalInfo(){
        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                            if(documentSnapshot.exists()){

                                usernamefromfb = documentSnapshot.getString("firstName");
                                profilePicfromfb = documentSnapshot.getString("profilePic");


                            }
                            Toast.makeText(context, usernamefromfb, Toast.LENGTH_SHORT).show();






                    }
                });



    }











//
//    public void acceptRequest(int position, final String answer){
//
//        db.collection("Users").document(userDetails.get(position).uid_for_ref)
//                .collection("sentRequests").document(mAuth.getCurrentUser().getUid())
//                .update("accepted",answer).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(context, "set to "+answer, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//
//
//
//
//    }

//    public void markAsAcceptedforMe(int position, final String answer){
//
//        db.collection("Users").document(mAuth.getCurrentUser().getUid())
//                .collection("requests").document(userDetails.get(position).uid_for_ref)
//                .update("haveIAccepted",answer)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//
//                        Toast.makeText(context, "I have said "+answer, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//
//
//
//    }








}


