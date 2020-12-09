package com.example.ridendrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class driver_dash extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1 ;
    FirebaseFirestore db  = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CardView mViewmyCard2, mViewmyCard;
    Switch availability_switch;
    StorageReference mStorageRef;

    ImageView userimage;
    TextView usersname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash);

        mViewmyCard = findViewById(R.id.cardtohide1_creator);
        mViewmyCard2 = findViewById(R.id.cardtohide2_creator);
        availability_switch = findViewById(R.id.im_available_switch);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        userimage = findViewById(R.id.userImage);
        usersname = findViewById(R.id.users_name_info_creator);

        setupRecyclerMySites();
        setupRecyclerMyRequests();
        setupPersonalInfo();


        findViewById(R.id.signout1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent intent = new Intent(driver_dash.this,MainActivity.class);
                startActivity(intent);
                finish();



            }
        });


        availability_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                        updateAvailability("available");
                }
                else{

                    updateAvailability("unavailable");

                }



            }
        });



        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });


            notificationToken();


    }



    ArrayList<Events> eventsSites = new ArrayList<>();
    getAdapterCount count = new getAdapterCount();
    public void setupRecyclerMySites(){

        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("places")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {






                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        eventsSites = new ArrayList<>();
                        RecyclerView mRecycler = findViewById(R.id.eventRecycler);
                        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(driver_dash.this,LinearLayoutManager.HORIZONTAL,true);
                        RecyclerView.Adapter madapter;


                        for(QueryDocumentSnapshot d: queryDocumentSnapshots){

                            Events e1 = d.toObject(Events.class);

                            eventsSites.add(e1);



                        }

                        madapter = new driver_Locations_adapter(eventsSites,R.layout.addsite, onClickListenerMylocations);


                        count = new getAdapterCount();
                        count.count = madapter.getItemCount();



                        mRecycler.setLayoutManager(mlayoutmanager);
                        mRecycler.setAdapter(madapter);

                        mlayoutmanager.smoothScrollToPosition(mRecycler,new RecyclerView.State(),madapter.getItemCount());
                        mRecycler.setHasFixedSize(true);



                        if(madapter.getItemCount()==1){

//                            Toast.makeText(Creater_Dashboard.this, "zero", Toast.LENGTH_SHORT).show();

                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(driver_dash.this,RecyclerView.HORIZONTAL,false);
                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);
                            mViewmyCard.setVisibility(View.VISIBLE);


                        }

                        if(madapter.getItemCount()==2){
                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(driver_dash.this,RecyclerView.HORIZONTAL,true);
                            LinearLayout linearLayout1 = findViewById(R.id.mysiterecycler_linear_layout);


                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);

                            mViewmyCard.setVisibility(View.GONE);

                        }




                    }
                });



    }

    public View.OnClickListener onClickListenerRequests = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder =(RecyclerView.ViewHolder) v.getTag();
           int position = viewHolder.getAdapterPosition();
            Log.d("Fwae", position+"");

            //Toast.makeText(driver_dash.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

            Toast.makeText(driver_dash.this, userReq.get(position).riderGetOffPoint.toString(), Toast.LENGTH_SHORT).show();

            ArrayList<String> cordinates = new ArrayList<>();
            cordinates.add(String.valueOf(userReq.get(position).driverDestination.getLatitude()));
            cordinates.add(String.valueOf(userReq.get(position).driverDestination.getLongitude()));
            cordinates.add(String.valueOf(userReq.get(position).riderGetOffPoint.getLatitude()));
            cordinates.add(String.valueOf(userReq.get(position).riderGetOffPoint.getLongitude()));
            cordinates.add(userReq.get(position).encodedPath);
            //Toast.makeText(driver_dash.this, userReq.get(position).encodedPath, Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            Intent intent = new Intent(driver_dash.this, Map1.class);
            bundle.putStringArrayList("cords",cordinates);
            intent.putExtras(bundle);
            startActivity(intent);




        }




    };

    public View.OnClickListener onClickListenerMylocations = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder =(RecyclerView.ViewHolder) v.getTag();
            int position =viewHolder.getAdapterPosition();
            Log.d("Fwae", position+"");

            Toast.makeText(driver_dash.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

            ArrayList<String> cordinates = new ArrayList<>();
            cordinates.add(String.valueOf(eventsSites.get(position).loc.getLatitude()));
            cordinates.add(String.valueOf(eventsSites.get(position).loc.getLongitude()));
//            cordinates.add(String.valueOf(userReq.get(position).riderGetOffPoint.getLatitude()));
//            cordinates.add(String.valueOf(userReq.get(position).riderGetOffPoint.getLongitude()));
            cordinates.add(eventsSites.get(position).encodedPath);
            //Toast.makeText(driver_dash.this, userReq.get(position).encodedPath, Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            Intent intent = new Intent(driver_dash.this, Map1.class);
            bundle.putStringArrayList("cordsPrivate",cordinates);
            intent.putExtras(bundle);
            startActivity(intent);




        }




    };


    ArrayList<users> userReq = new ArrayList<>();
    public void setupRecyclerMyRequests(){

        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {





                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        userReq = new ArrayList<>();
                        RecyclerView mRecycler = findViewById(R.id.viewRequests);
                        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(driver_dash.this,LinearLayoutManager.HORIZONTAL,false);
                        RecyclerView.Adapter madapter;


                        for(QueryDocumentSnapshot d: queryDocumentSnapshots){

                            users u1 = d.toObject(users.class);
                            u1.uid_for_ref=d.getId();

                            userReq.add(u1);



                        }

                        madapter = new rider_reqeusts_for_driverAdapter(userReq,R.layout.empty_recycler_placehold_rqsts, onClickListenerRequests);






                        mRecycler.setLayoutManager(mlayoutmanager);
                        mRecycler.setAdapter(madapter);

                        mlayoutmanager.smoothScrollToPosition(mRecycler,new RecyclerView.State(),0);
                        mRecycler.setHasFixedSize(true);



                        if(madapter.getItemCount()==0){

//                            Toast.makeText(Creater_Dashboard.this, "zero", Toast.LENGTH_SHORT).show();

                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(driver_dash.this,RecyclerView.HORIZONTAL,false);
                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);
                            mViewmyCard2.setVisibility(View.VISIBLE);


                        }

//                        if(madapter.getItemCount()==2){
//                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(driver_dash.this,RecyclerView.HORIZONTAL,false);
//                            LinearLayout linearLayout1 = findViewById(R.id.mysiterecycler_linear_layout);
//
//
//                            mRecycler.setLayoutManager(mlayoutmanager1);
//                            mRecycler.setAdapter(madapter);
//
//                           // mViewmyCard2.setVisibility(View.GONE);
//
//                        }




                    }
                });



    }



    public void updateAvailability(final String availability){

        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("places").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot d:task.getResult()){

                        statusIterator(d.getId(), availability);




                    }


                }

            }
        });




    }


    public void statusIterator(final String uid, final String availability){

        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("places").document(uid).update("requestStatus",availability)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(driver_dash.this, "updated", Toast.LENGTH_SHORT).show();

                        if(task.isSuccessful()){

                            copySite(uid,availability);

                        }
                    }
                });




    }

    public void copySite(String taskid, String status) {


        db.collection("Sites").document(taskid).update("requestStatus", status)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()) {
//                    Toast.makeText(Map1.this, "Copy Complete", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }


    public void chooseFile(){


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);




    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null & data.getData()!=null){

            Uri mimageuri = data.getData().normalizeScheme();

            Picasso.get().load(mimageuri).into(userimage);
            sendtoFirebase(mimageuri);
            // Toast.makeText(this, mimageuri.toString(), Toast.LENGTH_SHORT).show();



        }


    }

    public void sendtoFirebase(Uri uri){

        Uri file = uri;

        StorageReference profilePicRef = mStorageRef.child(mAuth.getCurrentUser().getUid()+".jpg");


        profilePicRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .update("profilePic",url.toString());

                    }
                });







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(driver_dash.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


public void setupPersonalInfo(){

        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if(documentSnapshot.exists()){

                      String first = documentSnapshot.getString("firstName");
                      String last = documentSnapshot.getString("lastName");

                        usersname.setText(capitalize(first + " " + last));
                        String uri = documentSnapshot.getString("profilePic");
                        Picasso.get().load(uri).into(userimage);




                    }




                    }
                });



}

    public String capitalize(String str){
        try{
            String[] strArray = str.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }
            return builder.toString();
        }
        catch (Exception e){

            return "Users Name";
        }



    }

    public class getAdapterCount{


        public void getAdapterCount(){

        }

        int count;


        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }


    public void notificationToken(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){

                            String token = task.getResult().getToken();

                            db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                    .update("token",token);






                        }
                    }
                });




    }















}



