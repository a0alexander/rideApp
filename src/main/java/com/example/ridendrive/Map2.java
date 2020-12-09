package com.example.ridendrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.dpro.widgets.WeekdaysPicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.clustering.ClusterManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Map2 extends FragmentActivity implements OnMapReadyCallback{


    GoogleMap mMap;
    LatLng userclickLatLng;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FloatingActionButton fabA, fabViewcurrentsites;


    //ClusterManager<Clustering> mCluster;
    CardView mCardView2, viewpeoplecard;
    ConstraintLayout cl;
    Button join;
    Button viewPeople;
    ImageButton closebutton1, closebutton2;
    TextView sitenamelabel, datetext2, timetext2, owner, numberofusrs;
    SearchView searchView;
    Spinner siteSearchspin;
    String searchType;
    FusedLocationProviderClient client;
    ObjectAnimator cardViewanimator;


    CardView mCardView;
    ImageButton exitCard, dateButton, timeButton;
    EditText dateText, timeText, siteText;
    Button addsiteBtn;
    WeekdaysPicker widget;
    ObjectAnimator getCardViewanimator, fabViewanimator;
    Bundle bundle;
    LinearLayout fab_gruoup1;

    float offset = 400f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        LinearLayout l1 = findViewById(R.id.linearLayout31);
        fab_gruoup1 = findViewById(R.id.fab_group);
        l1.setTranslationY(600f);


        fabA = findViewById(R.id.addDriversHome);
        fab_gruoup1.setTranslationY(500f);
        //fabA.setTranslationY(l1.getHeight());




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


//        mCardView = findViewById(R.id.cardView1);
//        exitCard = findViewById(R.id.closeCard);
//        timeButton = findViewById(R.id.timeButton);
//        //dateButton = findViewById(R.id.dateButton);
//        //dateText = findViewById(R.id.dateText);
//        timeText = findViewById(R.id.timeText);
        //     addsiteBtn = findViewById(R.id.addbutton);
//        siteText = findViewById(R.id.eventName);
//        widget = findViewById(R.id.weekdays);


        final LatLng sydney = new LatLng(10.7295612, 106.6937702);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("RMIT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12f));

        ArrayList<String> cordinates = getIntent().getStringArrayListExtra("cords");

        if(cordinates!=null){
            try{
                double v1 = Double.parseDouble(cordinates.get(0));
                double v2 = Double.parseDouble(cordinates.get(1));

                double d1 = Double.parseDouble(cordinates.get(2));
                double d2 = Double.parseDouble(cordinates.get(3));
                List<LatLng> decode = PolyUtil.decode(cordinates.get(4));

                Polyline  p = mMap.addPolyline(new PolylineOptions().addAll(decode));
                //polylines.add(p);
               // Toast.makeText(this, cordinates.get(4), Toast.LENGTH_SHORT).show();


                LatLng driverHomeCordinates = new LatLng(v1,v2);
                LatLng ridersHomeCordinates = new LatLng(d1,d2);


                mMap.addMarker(new MarkerOptions().position(driverHomeCordinates).title("Driver's Destination"));
                mMap.addMarker(new MarkerOptions().position(ridersHomeCordinates).title("Your Destination"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ridersHomeCordinates, 12f));

                Toast.makeText(this, cordinates.get(0).toString(), Toast.LENGTH_SHORT).show();



            }
            catch (Exception e ){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            Marker m1;

            @Override
            public void onMapClick(LatLng latLng) {


                if (m1 != null) {
                    m1.remove();
                }

                m1 = mMap.addMarker(new MarkerOptions().position(latLng));

                userclickLatLng = latLng;


                //getRouteToPlace(sydney,userclickLatLng);
                reverseCard();
                clearPolyLines();


            }
        });


        fabA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userclickLatLng!=null){
                    getDrivers();
                }

            }
        });


        //View all Drivers
        findViewById(R.id.viewAllpaths).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Sites").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if(e==null){
                            for(QueryDocumentSnapshot d : queryDocumentSnapshots){

                                Events e1 = d.toObject(Events.class);
                                LatLng markerloc = new LatLng(e1.loc.getLatitude(),e1.loc.getLongitude());

                                mMap.addMarker(new MarkerOptions().position(markerloc)
                                        .title(e1.owner +"\n"+e1.gettimeNormal(e1.eventDate)));



                            }


                        }

                    }
                }) ;
            }
        });










    }


    Events events = new Events();
    ArrayList<Events> placeList = new ArrayList<>();

    public void getDrivers() {
        events = new Events();
        placeList = new ArrayList<>();

        db.collection("Sites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot d : task.getResult()) {

                        placeList.add(d.toObject(Events.class));

                    }


                    checkInVicinity();

                }


            }
        });


    }

    ArrayList<Events> positiveResults = new ArrayList<>();

    public void checkInVicinity() {
        LinearLayout l1 = findViewById(R.id.linearLayout31);
        cardViewanimator = ObjectAnimator.ofFloat(l1, "translationY", l1.getHeight(), 0f);
        cardViewanimator.setInterpolator(new DecelerateInterpolator());

        fabViewanimator = ObjectAnimator.ofFloat(fab_gruoup1, "translationY", l1.getHeight(), 0f);
        fabViewanimator.setInterpolator(new DecelerateInterpolator());

        positiveResults = new ArrayList<>();
        for (Events event : placeList) {

            List<LatLng> latLngs = PolyUtil.decode(event.encodedPath);
            if (PolyUtil.isLocationOnEdge(userclickLatLng, latLngs, false, 500)) {

                positiveResults.add(event);
            }


        }

        if (positiveResults.size() > 0) {

            Toast.makeText(this, "present", Toast.LENGTH_SHORT).show();

            recyclerSetup(positiveResults);
            cardViewanimator.start();
            fabViewanimator.start();
            findClosest(positiveResults);
        } else {

            Toast.makeText(this, "absent", Toast.LENGTH_SHORT).show();

        }


    }

    ArrayList<Double> distances = new ArrayList<>();

    public void findClosest(ArrayList<Events> positiveResults) {

        distances = new ArrayList<>();

        for (Events events : positiveResults) {
            distances = new ArrayList<>();
            events.getEncodedPath();
            List<LatLng> latLngs = PolyUtil.decode(events.encodedPath);

            for (int i = 0; i < latLngs.size() - 1; i++) {

                distances.add(PolyUtil.distanceToLine(userclickLatLng, latLngs.get(i), latLngs.get(i + 1)));


            }


        }
        Collections.sort(distances);
        Toast.makeText(this, String.valueOf(distances.get(0).intValue()), Toast.LENGTH_SHORT).show();


    }


//    public void getRouteToPlace(LatLng start, LatLng destination) {
//
//
//        if (start != null && destination != null) {
//            // LatLng sydney = new LatLng(10.7295612, 106.6937702);
//            Routing routing = new Routing.Builder()
//                    .travelMode(AbstractRouting.TravelMode.DRIVING)
//                    .withListener(this)
//                    .alternativeRoutes(false)
//                    .waypoints(start, destination).key("AIzaSyB5TwxTm0D-hMPrkV8A23sr4RD-RAW7iyA")
//                    .build();
//
//
//            routing.execute();
//        }
//
//
//    }


//    @Override
//    public void onRoutingFailure(RouteException e) {
//
//        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onRoutingStart() {
//
//    }
//    List<Polyline> polylines;
//    PolylineOptions polyOptions1 = new PolylineOptions();
//    @Override
//    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
//
//
//        if(polylines!=null){
//
//            if(polylines.size()>0) {
//                for (Polyline poly : polylines) {
//                    poly.remove();
//                }
//
//            }
//            polylines.clear();
//        }
//        polylines= new ArrayList<>();
//
//
//
//        //polylines = new ArrayList<>();
//        //add route(s) to the map.
//
//
//        polyOptions1 = new PolylineOptions();
//
//        List<LatLng> latLngs = new ArrayList<>();
//        for (int i = 0; i <route.size(); i++) {
//
//
//            PolylineOptions polyOptions = new PolylineOptions();
//
//            polyOptions1.addAll(route.get(i).getPoints());
//            polyOptions.width(10);
//            polyOptions.addAll(route.get(i).getPoints());
//            Polyline polyline = mMap.addPolyline(polyOptions);
//            polylines.add(polyline);
//
//
////            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
//        }
//
////        String encodedPath = PolyUtil.encode(polyOptions1.getPoints());
////        Toast.makeText(this, encodedPath, Toast.LENGTH_SHORT).show();
//
//
//
//    }
//
//    @Override
//    public void onRoutingCancelled() {
//
//    }


    SnapHelper snapHelper = new PagerSnapHelper();

    public void recyclerSetup(ArrayList<Events> eventsSites) {


        //ArrayList<users> eventsSites = new ArrayList<>();
        final RecyclerView mRecycler = findViewById(R.id.driver_map_recycler);
        final RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(Map2.this, LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView.Adapter madapter;


        madapter = new map_driver_recycler(eventsSites, onClickListener1);


        snapHelper.attachToRecyclerView(mRecycler);

        findViewById(R.id.requestDriverbtn).setOnClickListener(null);
        findViewById(R.id.requestDriverbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (madapter.getItemCount() > 0) {
                    int positionView = ((LinearLayoutManager) mRecycler.getLayoutManager()).findFirstVisibleItemPosition();
                    getmyDataAndSendRequest(positionView);
                    Toast.makeText(Map2.this, String.valueOf(positionView), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Map2.this, "No Results", Toast.LENGTH_SHORT).show();
                }


            }
        });


        mRecycler.setLayoutManager(mlayoutmanager);
        mRecycler.setAdapter(madapter);

        mlayoutmanager.smoothScrollToPosition(mRecycler, new RecyclerView.State(), 0);
        mRecycler.setHasFixedSize(true);


    }


    public View.OnClickListener onClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Log.d("Fwae", position + "");

            drawPoly(positiveResults.get(position).encodedPath);
            Toast.makeText(Map2.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

        }
    };


    public void reverseCard() {

        LinearLayout l1 = findViewById(R.id.linearLayout31);
        cardViewanimator = ObjectAnimator.ofFloat(l1, "translationY", l1.getHeight(), 0f);
        cardViewanimator.setInterpolator(new DecelerateInterpolator());

        if (l1.getY() < 1460) {
            cardViewanimator.reverse();
            fabViewanimator.reverse();
        }


    }


    public void sendRequest(users user, final String ownerid) {


        if (positiveResults.size() > 0) {
            //user.ridetime = events.eventDate;

            db.collection("Users").document(ownerid).collection("requests")
                    .document(mAuth.getCurrentUser().getUid())
                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(Map2.this, ownerid, Toast.LENGTH_SHORT).show();
                        findViewById(R.id.reqested_pending).setVisibility(View.VISIBLE);

                    }


                }
            });


        }


    }


    public void getmyDataAndSendRequest(final int ndx) {
        final String ownerid = positiveResults.get(ndx).ownerId;
        //get personal data to create user object

        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    users u = new users();
                    u = task.getResult().toObject(users.class);
                    u.ridetime=positiveResults.get(ndx).eventDate;
                    u.riderGetOffPoint = new GeoPoint(userclickLatLng.latitude,userclickLatLng.longitude);
                    u.driverDestination = positiveResults.get(ndx).loc;
                    u.encodedPath = positiveResults.get(ndx).encodedPath;


                    sendRequest(u, ownerid);
                    //copytoSentRequests(ownerid);
                    copyDriverandEventContents(ownerid,ndx);
                    //Contents(ownerid);


                }


            }
        });


    }

    List<Polyline> polylines;
    PolylineOptions polyOptions1 = new PolylineOptions();

    public void drawPoly(String encodedPath) {


     clearPolyLines();
        polylines = new ArrayList<>();


        polyOptions1 = new PolylineOptions();



        List<LatLng> latlngs = PolyUtil.decode(encodedPath);
        polyOptions1.addAll(latlngs);
        polyOptions1.width(10);
        Polyline polyline = mMap.addPolyline(polyOptions1);
        polylines.add(polyline);



    }

    public void clearPolyLines(){
        if (polylines != null) {

            if (polylines.size() > 0) {
                for (Polyline poly : polylines) {
                    poly.remove();
                }

            }
            polylines.clear();
        }
    }

    public void copytoSentRequests(String uid){
            PendingRequests pendingRequests = new PendingRequests();
            pendingRequests.accepted = "no";

        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("sentRequests").document(uid)
                .set(pendingRequests).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Map2.this, "success!", Toast.LENGTH_SHORT).show();
            }
        });





    }

    public void copyDriverandEventContents(final String ownerid, final int ndx){

        db.collection("Users").document(ownerid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                        final users u = task.getResult().toObject(users.class);


                        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("sentRequests")
                                .document(ownerid).set(positiveResults.get(ndx)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                        .collection("sentRequests")
                                        .document(ownerid).update(
                                                "accepted", "no",
                                                "firstName",u.firstName,
                                        "lastName",u.lastName,
                                        "profilePic",u.profilePic,
                                        "phone", u.phone,
                                        "riderGetOffPoint",new GeoPoint(userclickLatLng.latitude,userclickLatLng.longitude),
                                        "driverDestination", new GeoPoint(positiveResults.get(ndx).loc.getLatitude(),
                                                                         positiveResults.get(ndx).loc.getLongitude()),
                                        "encodedPath",positiveResults.get(ndx).encodedPath




                                        );




                            }
                        });







                    }
                });





    }




}