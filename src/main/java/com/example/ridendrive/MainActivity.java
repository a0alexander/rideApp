package com.example.ridendrive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {




    FirebaseAuth mAuth ;
    FirebaseFirestore db;

    Button signUp, signIn;
    EditText username, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser()!=null){

            //todo put map here
            LoadUI(mAuth.getCurrentUser().getUid());

        }

        signIn = findViewById(R.id.signInMain);
        signUp = findViewById(R.id.signUpwithEmail);
        username = findViewById(R.id.usernamefield);
        pass  = findViewById(R.id.passwordfield);






        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!username.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){

                    final drivers drivers = new drivers();
                    drivers.firstName = username.getText().toString();
                    drivers.lastName = pass.getText().toString();

                    mAuth.createUserWithEmailAndPassword(username.getText().toString().trim(),
                            pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                createUser(task.getResult().getUser().getUid(),drivers);
                            }

                        }
                    });
                    
                }

            }
        });
        
        
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!username.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){

                    mAuth.signInWithEmailAndPassword(username.getText().toString().trim(),
                            pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(MainActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
                                LoadUI(task.getResult().getUser().getUid());
                            }
                            else {

                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    

                }
                
                
                
                
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, intermediate.class);
                startActivity(intent);




            }
        });

//        findViewById(R.id.map1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(MainActivity.this,Map1.class);
//                startActivity(intent);
//
//
//            }
//        });







        





    }
    
    
    
    public void createUser(String uid, drivers drivers){

        db.collection("Users").document(uid)
                .set(drivers).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "User Added!", Toast.LENGTH_SHORT).show();
            }
        });




    }


    public void LoadUI(String uid1) {

        //        final userType userType1 = new userType();




        db.collection("Users").document(uid1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String userType = task.getResult().getString("typeOfUser");

                if (userType.equals("admin")) {
                    Intent intent1 = new Intent(MainActivity.this, driver_dash.class);
                    startActivity(intent1);
                    Toast.makeText(MainActivity.this, "admin user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("admin");
                    finish();
                } else if (userType.equals("normal")) {
                    Intent intent = new Intent(MainActivity.this, rider_dash.class);
                    startActivity(intent);
//                    Toast.makeText(Login.this, "normal user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("normal");
                    finish();

                }

//                else if (userType.equals("super")){
//                    Intent intent = new Intent(MainActivity.this, Super.class);
//                    startActivity(intent);
////                    Toast.makeText(Login.this, "super user logged in", Toast.LENGTH_SHORT).show();
////                    userType1.setUserType("normal");
//                    finish();
//
//                }


            }

        });

    }
    
    
    
    
    
    
    
}
