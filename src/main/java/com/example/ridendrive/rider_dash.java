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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class rider_dash extends AppCompatActivity {




    FirebaseFirestore db;
    FirebaseAuth mAuth;
    CardView mViewmyCard2;
    TextView usersname;
    ImageView userimage;
    StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST =1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_dash);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mViewmyCard2 = findViewById(R.id.cardtohide2_creator);

        userimage = findViewById(R.id.userImage);
        usersname = findViewById(R.id.users_name_rider);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setupRecyclerMyRequests();
        setupPersonalInfo();



        findViewById(R.id.signout1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(rider_dash.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        findViewById(R.id.map2_Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rider_dash.this, Map2.class);
                startActivity(intent);

            }
        });

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupRecyclerMyRequests();


    }
    ArrayList<users> userReq = new ArrayList<>();
    public void setupRecyclerMyRequests(){

        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("sentRequests")
                .addSnapshotListener(this,new EventListener<QuerySnapshot>() {





                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        userReq = new ArrayList<>();
                        RecyclerView mRecycler = findViewById(R.id.viewRequests);
                        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(rider_dash.this,LinearLayoutManager.HORIZONTAL,true);
                        RecyclerView.Adapter madapter;


                        for(QueryDocumentSnapshot d: queryDocumentSnapshots){

                            userReq.add(d.toObject(users.class));



                        }

                        madapter = new rider_dash_recycler(userReq,R.layout.empty_recycler_placehold_rqsts, onClickListener);






                        mRecycler.setLayoutManager(mlayoutmanager);
                        mRecycler.setAdapter(madapter);

                        mlayoutmanager.smoothScrollToPosition(mRecycler,new RecyclerView.State(),madapter.getItemCount());
                        mRecycler.setHasFixedSize(true);



                        if(madapter.getItemCount()==0){

//                            Toast.makeText(Creater_Dashboard.this, "zero", Toast.LENGTH_SHORT).show();

                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(rider_dash.this,RecyclerView.HORIZONTAL,false);
                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);
                            mViewmyCard2.setVisibility(View.VISIBLE);


                        }

                        if(madapter.getItemCount()==2){
                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(rider_dash.this,RecyclerView.HORIZONTAL,true);
                            LinearLayout linearLayout1 = findViewById(R.id.mysiterecycler_linear_layout);


                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);

                            mViewmyCard2.setVisibility(View.GONE);

                        }




                    }
                });



    }



    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder =(RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Log.d("Fwae", position+"");

            Toast.makeText(rider_dash.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

            ArrayList<String> cordinates = new ArrayList<>();
            cordinates.add(String.valueOf(userReq.get(position).loc.getLatitude()));
            cordinates.add(String.valueOf(userReq.get(position).loc.getLongitude()));
            cordinates.add(String.valueOf(userReq.get(position).riderGetOffPoint.getLatitude()));
            cordinates.add(String.valueOf(userReq.get(position).riderGetOffPoint.getLongitude()));
            cordinates.add(userReq.get(position).encodedPath);
           // Toast.makeText(rider_dash.this, userReq.get(position).encodedPath, Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            Intent intent = new Intent(rider_dash.this, Map2.class);
            bundle.putStringArrayList("cords",cordinates);
            intent.putExtras(bundle);
            startActivity(intent);



        }
    };

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
                Toast.makeText(rider_dash.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
//                            Intent intent = new Intent(rider_dash.this,chatActivity.class);
//                            bundle.putString("username",first);
//                            bundle.putString("profilePic",uri);
//                            intent.putExtras(bundle);

                            if(uri!=null){
                                Picasso.get().load(uri).into(userimage);
                            }
                            else {
                                Picasso.get().load(R.drawable.icons8_cat_profile_60px).into(userimage);
                            }





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
























}
