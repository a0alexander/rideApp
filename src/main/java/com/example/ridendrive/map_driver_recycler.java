package com.example.ridendrive;

import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dpro.widgets.WeekdaysPicker;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class map_driver_recycler extends RecyclerView.Adapter<map_driver_recycler.ViewHolder1> {


    private View.OnClickListener mOnItemClickListener;
    public ArrayList<Events> userDetails;
    private Context context;
    int ly;
    private View.OnClickListener onItemClickListener;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public  class ViewHolder1 extends  RecyclerView.ViewHolder {

        ImageView user_image;
        TextView ridername, ridertime,datetext;
        ImageButton button,button2_viewMap,button3_viewMap;
        ImageView driverProfile;
        WeekdaysPicker weekdaysPicker_recyc;
        Button req_pending;



        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            ridername = itemView.findViewById(R.id.nameOfRider);
            ridertime = itemView.findViewById(R.id.timeOfRider);
            weekdaysPicker_recyc = itemView.findViewById(R.id.weekdays_driver_results);
            //driverProfile = itemView.findViewById(R.id.driver_pro_picture_map);
            req_pending = itemView.findViewById(R.id.reqested_pending);
            user_image = itemView.findViewById(R.id.userImage_in_driver_results);


            button = itemView.findViewById(R.id.viewSitesinMap);



            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

        }

    }


    public map_driver_recycler(ArrayList<Events> list1, View.OnClickListener m1){
        this.userDetails = list1;

        this.mOnItemClickListener = m1;

    }


    public void setOnItemClickListener(View.OnClickListener itemClickListener) {


        mOnItemClickListener = itemClickListener;


    }





    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_results, parent,false);
        ViewHolder1 fe = new ViewHolder1(v);
        context = parent.getContext();
        return  fe;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder1 holder, int position) {



        Events u =   userDetails.get(position);
        //TODO add time the user requests here
        holder.ridername.setText(capitalize(u.owner));
        holder.weekdaysPicker_recyc.setSelectedDays(u.daysofWeekarray);
        holder.ridertime.setText(u.gettimeNormal(u.eventDate));



        try{
            Picasso.get().load(u.profilePic).into(holder.user_image);
        }
        catch (Exception e){

            Picasso.get().load(R.drawable.icons8_cat_profile_60px).into(holder.user_image);
        }




        try{
            if(u.requestStatus.equals("unavailable")){

                holder.req_pending.setBackgroundResource(R.drawable.req_pening_unavailable);
                holder.req_pending.setText("unavailable");
               // holder.req_pending.setBackground(ContextCompat.getDrawable(context,R.drawable.req_pening_unavailable));
            }
            else if(u.requestStatus.equals(mAuth.getCurrentUser().getUid())){


                holder.req_pending.setBackgroundResource(R.drawable.req_pending);
                holder.req_pending.setText("Accepted");
              //  holder.req_pending.setBackground(ContextCompat.getDrawable(context,R.drawable.req_pending));

            }
        }
        catch (Exception e){

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

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

}
