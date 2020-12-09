package com.example.ridendrive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpro.widgets.WeekdaysPicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;




public class driver_Locations_adapter extends RecyclerView.Adapter<driver_Locations_adapter.ViewHolder1> {

    private View.OnClickListener mOnItemClickListener;
    public ArrayList<Events> driverLocations;
    private Context context;
    int ly;
    private View.OnClickListener onItemClickListener;



    public  class ViewHolder1 extends  RecyclerView.ViewHolder {


        public TextView mtextview1, timetext,datetext;
        ImageButton button,button2_viewMap,button3_viewMap;
        WeekdaysPicker weekdaysPicker_recyc;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            mtextview1 = itemView.findViewById(R.id.nameOfSiteinRow);
            timetext = itemView.findViewById(R.id.timeOfSiteinRow);
//            datetext = itemView.findViewById(R.id.dateOfSiteinRow);

            weekdaysPicker_recyc = itemView.findViewById(R.id.weekdays_recycler);
            button = itemView.findViewById(R.id.addsiteButtonRecycler);
//            button2_viewMap = itemView.findViewById(R.id.viewSitesinMap);
//            button3_viewMap = itemView.findViewById(R.id.viewMySitesinMap);

                itemView.setTag(this);
                itemView.setOnClickListener(mOnItemClickListener);

        }

    }

//    public recyclerviewAdapter(ArrayList<UserItem_recycler> list){
//            arraylist = list;
//
//    }

    public driver_Locations_adapter(ArrayList<Events> list1, @LayoutRes int layout, View.OnClickListener m1){
        this.driverLocations = list1;
        this.ly =layout;
        this.mOnItemClickListener = m1;

    }


    public void setOnItemClickListener(View.OnClickListener itemClickListener) {


        mOnItemClickListener = itemClickListener;


    }



    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==R.layout.event_row){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent,false);
            ViewHolder1 fe = new ViewHolder1(v);
            context = parent.getContext();
            return  fe;
        }
        else{
            View v1 = LayoutInflater.from(parent.getContext()).inflate(ly, parent,false);
            ViewHolder1 fe1 = new ViewHolder1(v1);
            context = parent.getContext();

            return  fe1;
        }




    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder1 holder, int position) {



        if(position==driverLocations.size()&& holder.button!=null){

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,Map1.class);
                    Bundle bundle = new Bundle();
                    CollectionReference d = FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("places");
                    bundle.putString("firestoreRef",d.getPath());
                    intent.putExtras(bundle);

                    context.startActivity(intent);
                }
            });
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
            Events u =   driverLocations.get(position);


            holder.mtextview1.setText(capitalize(u.siteName));
            //holder.datetext.setText(u.getDateNormal(u.eventDate));
            try{
                holder.weekdaysPicker_recyc.setSelectedDays(u.daysofWeekarray);
            }
            catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            holder.timetext.setText(u.gettimeNormal(u.eventDate));
        }



    }

    @Override
    public int getItemCount() {
        return driverLocations.size() + 1;
    }





    @Override
    public int getItemViewType(int position) {
        return (position == driverLocations.size()) ? ly : R.layout.event_row;
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


