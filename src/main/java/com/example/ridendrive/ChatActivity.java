package com.example.ridendrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridendrive.Adapter.MessageAdapter;
import com.example.ridendrive.Model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CircleImageView profile_image;
    TextView username;
    FirebaseUser fuser;
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    Intent intent;
    String userid;
    String profile;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setTitle("");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    Toast.makeText(ChatActivity.this, "You can't send an empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        Toast.makeText(ChatActivity.this,userid,Toast.LENGTH_SHORT).show();

        final DocumentReference docRef = db.collection("Users").document(userid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                String user = document.getString("firstName");
                username.setText(capitalize(user));
                String picture = document.getString("profilePic");
                if (picture==null){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(picture).into(profile_image);
                }

            }
        });

        CollectionReference notebookRef = db.collection("Users");
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                readMesages(fuser.getUid(), userid, profile);

//                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
//                    DocumentSnapshot documentSnapshot = dc.getDocument();
//
//                    users user = documentSnapshot.toObject(users.class);
//                    username.setText(user.getFirstName());
//                    profile = user.getProfilePic();
//                    if (user.profilePic==null){
//                        profile_image.setImageResource(R.mipmap.ic_launcher);
//                    } else {
//                        Picasso.get().load(user.profilePic).into(profile_image);
//                    }
//
//                    readMesages(fuser.getUid(), userid, profile);
//                }
            }
        });
        CollectionReference noteRef = db.collection("Chats");
        noteRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                readMesages(fuser.getUid(), userid, profile);
            }
        });

    }

    private void sendMessage(String sender, String receiver, String message){
        Chat chat = new Chat(sender,receiver,message);
        db.collection("Chats").document(""+Timestamp.now()).set(chat);
    }

    private void readMesages(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();

        CollectionReference notebookRef = db.collection("Chats");

        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                mchat.clear();
                if (e != null) {
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();

                    Chat chat = documentSnapshot.toObject(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(ChatActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }
        });
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

