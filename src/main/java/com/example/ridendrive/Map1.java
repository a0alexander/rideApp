package com.example.ridendrive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.clustering.ClusterManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Map1 extends FragmentActivity implements OnMapReadyCallback, RoutingListener, TimePickerDialog.OnTimeSetListener {



    GoogleMap mMap;
    LatLng userclickLatLng;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FloatingActionButton fabA,fabViewcurrentsites;


    //ClusterManager<Clustering> mCluster;
    CardView mCardView2, viewpeoplecard;
    ConstraintLayout cl;
    Button join;
    Button viewPeople;
    ImageButton closebutton1, closebutton2;
    TextView sitenamelabel,datetext2,timetext2, owner, numberofusrs;
    SearchView searchView;
    Spinner siteSearchspin;
    String searchType;
    FusedLocationProviderClient client;
    ObjectAnimator cardViewanimator;


    CardView mCardView;
    ImageButton exitCard,dateButton, timeButton;
    EditText dateText, timeText,siteText;
    Button addsiteBtn;
    WeekdaysPicker widget;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fabA = findViewById(R.id.addDriversHome);






    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;



        mCardView = findViewById(R.id.cardView1);
        exitCard = findViewById(R.id.closeCard);
        timeButton = findViewById(R.id.timeButton);
        //dateButton = findViewById(R.id.dateButton);
        //dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        addsiteBtn = findViewById(R.id.addbutton);
        siteText = findViewById(R.id.eventName);
        widget = findViewById(R.id.weekdays);


        cardViewanimator = ObjectAnimator.ofFloat(mCardView,"translationY",-2000f,0f);
        cardViewanimator.setInterpolator(new DecelerateInterpolator());


        final LatLng sydney = new LatLng(10.7295612,106.6937702);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("RMIT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12f));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            Marker m1;
            @Override
            public void onMapClick(LatLng latLng) {


                if(m1!=null){
                    m1.remove();
                }

                m1  = mMap.addMarker(new MarkerOptions().position(latLng));

                userclickLatLng = latLng;


                getRouteToPlace(sydney,userclickLatLng);



            }
        });





        fabA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Events events = new Events();

                //events.loc = new GeoPoint(userclickLatLng.latitude,userclickLatLng.longitude);
                clearFields();
                addsiteBtn.setOnClickListener(null);
                addsiteBtn.setOnClickListener(addsiteButtonListener);

//                db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
//                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                                String a =  task.getResult().getString("firstName");
//                                Toast.makeText(Map1.this, a, Toast.LENGTH_SHORT).show();
//
//                            }
//                        });

                if(userclickLatLng!=null){


                    cardViewanimator.start();
                    mCardView.setVisibility(View.VISIBLE);


                }


            }
        });






        exitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardViewanimator.reverse();


            }
        });



//        View.OnClickListener dateButtonListen = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePicker();
//            }
//        };


        View.OnClickListener timeButtonListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        };




//        dateButton.setOnClickListener(dateButtonListen);
//        dateText.setOnClickListener(dateButtonListen);

        timeButton.setOnClickListener(timeButtonListen);
        timeText.setOnClickListener(timeButtonListen);



        setupLocationsfromBundle();





    }


    View.OnClickListener addsiteButtonListener = new View.OnClickListener() {

        SimpleDateFormat timeStamp = new SimpleDateFormat("hh : mm a");
        StringBuilder sb= new StringBuilder();
        Events event = new Events();


        users user;
        String uid = mAuth.getCurrentUser().getUid();
        @Override
        public void onClick(View v) {


            cardViewanimator.reverse();

            if(!timeText.getText().toString().isEmpty()
                    && !siteText.getText().toString().isEmpty()){

//                sb.append(dateText.getText().toString()+" ");
                sb.append(timeText.getText().toString());

                try {
                    Date d = timeStamp.parse(sb.toString());
                    Toast.makeText(Map1.this, d.toString(), Toast.LENGTH_SHORT).show();

                    event.setDaysofWeekarray(widget.getSelectedDays());

                    event.setRequestStatus("available");
                    event.setSiteName(siteText.getText().toString().trim());
                    event.setEventDate(d);
                    event.setLoc(new GeoPoint(userclickLatLng.latitude,userclickLatLng.longitude));
                    event.setEncodedPath(PolyUtil.encode(polyOptions1.getPoints()));
                    /*get firstName and lastname and set as owner*/
                    db.collection("Users").document(uid).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    user = task.getResult().toObject(users.class);
                                    event.setOwner(user.firstName+" "+user.lastName);
                                    event.setOwnerId(uid);




                                    db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("places")
                                            .add(event).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            event.setPlaceId(task.getResult().getId());
                                            copySite(event, task.getResult().getId());
                                            Toast.makeText(Map1.this, "Successfully Added!", Toast.LENGTH_SHORT).show();

                                            clearFields();

                                        }
                                    });
                                }
                            });




                } catch (ParseException e) {
                    Toast.makeText(Map1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }


        }
    };


    public void copySite(Events event, final String taskid) {





        db.collection("Sites").document(taskid).set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
//                    Toast.makeText(Map1.this, "Copy Complete", Toast.LENGTH_SHORT).show();

                    try{
                         db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        db.collection("Sites").document(taskid).update("profilePic",task.getResult()
                                        .getString("profilePic"));
                                    }
                                });



                    }
                    catch (Exception e){


                    }

                }

            }
        });






    }





    /*SHOW DATE PICKER*/
//    public void showDatePicker(){
//
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this
//                , this,
//                Calendar.getInstance().get(Calendar.YEAR),
//                Calendar.getInstance().get((Calendar.MONTH)),
//                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//
//        datePickerDialog.show();
//
//
//    }

    public void showTimePicker(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                3, 00,false);

        timePickerDialog.show();

    }



   // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//
//        EditText dateinput = findViewById(R.id.dateText);
//
//
//        Date date = new Date();
//        Calendar c = Calendar.getInstance();
//
//
//        c.set(year, month, dayOfMonth);
//
//
//        if(c.getTimeInMillis()>Calendar.getInstance().getTimeInMillis()){
//
//            dateinput.setText(dateFormat.format(c.getTime()));
//            getDateandTime getDate = new getDateandTime();
//            getDate.year  = year;
//            getDate.month = month;
//            getDate.day = dayOfMonth;
//
//
//        }
//        else{
////            Toast.makeText(this, "Invalid Date!", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//
//    }




    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh : mm a");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.set(0,0,0,hourOfDay,minute);


        timeText.setText(dateFormat.format(c.getTime()));
        getDateandTime getDate = new getDateandTime();
        getDate.hour  = hourOfDay;
        getDate.mins = minute;
        TimeStampMaker();



    }

    public long TimeStampMaker(){

        Calendar c = Calendar.getInstance();
        getDateandTime gt = new getDateandTime();
        c.set(gt.year,gt.month,gt.day,gt.hour,gt.mins,0);
//        timeText.setText(String.valueOf(c.getTimeInMillis()));

        return c.getTimeInMillis();


    }


    /*return date in String Form*/
    public class getDateandTime{

        int year;
        int month;
        int day;
        int hour;
        int mins;

        public getDateandTime(){}

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }

        public int getHour() {
            return hour;
        }

        public int getMins() {
            return mins;
        }
    }

    public void clearFields(){

        //EditText editText  = findViewById(R.id.garbage_weight);
        //editText.getText().clear();
        siteText.getText().clear();
        //dateText.getText().clear();
        timeText.getText().clear();

    }






































    public void getRouteToPlace(LatLng start, LatLng destination){


        if(start!=null && destination!=null) {
           // LatLng sydney = new LatLng(10.7295612, 106.6937702);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(start, destination).key("AIzaSyB5TwxTm0D-hMPrkV8A23sr4RD-RAW7iyA")
                    .build();



            routing.execute();
        }





    }





    @Override
    public void onRoutingFailure(RouteException e) {

        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRoutingStart() {

    }
    List<Polyline> polylines;
    PolylineOptions polyOptions1 = new PolylineOptions();
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        if(polylines!=null){

            if(polylines.size()>0) {
                for (Polyline poly : polylines) {
                    poly.remove();
                }

            }
            polylines.clear();
        }
        polylines= new ArrayList<>();



        //polylines = new ArrayList<>();
        //add route(s) to the map.


        polyOptions1 = new PolylineOptions();

        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i <route.size(); i++) {


            PolylineOptions polyOptions = new PolylineOptions();

            polyOptions1.addAll(route.get(i).getPoints());
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);


//            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

//        String encodedPath = PolyUtil.encode(polyOptions1.getPoints());
//        Toast.makeText(this, encodedPath, Toast.LENGTH_SHORT).show();











    }

    @Override
    public void onRoutingCancelled() {

    }

    public void setupLocationsfromBundle(){

        ArrayList<String> cordinates = getIntent().getStringArrayListExtra("cords");
        ArrayList<String> cordinatesPrivate = getIntent().getStringArrayListExtra("cordsPrivate");

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


                mMap.addMarker(new MarkerOptions().position(driverHomeCordinates).title("Your Destination"));
                mMap.addMarker(new MarkerOptions().position(ridersHomeCordinates).title("Passenger Drop"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ridersHomeCordinates, 12f));

                Toast.makeText(this, cordinates.get(0).toString(), Toast.LENGTH_SHORT).show();



            }
            catch (Exception e ){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }



        if(cordinatesPrivate!=null){
            try{
                double v1 = Double.parseDouble(cordinatesPrivate.get(0));
                double v2 = Double.parseDouble(cordinatesPrivate.get(1));

//        double d1 = Double.parseDouble(cordinates.get(2));
//        double d2 = Double.parseDouble(cordinates.get(3));
                List<LatLng> decode = PolyUtil.decode(cordinatesPrivate.get(2));

                Polyline  p = mMap.addPolyline(new PolylineOptions().addAll(decode));
                //polylines.add(p);
                // Toast.makeText(this, cordinates.get(4), Toast.LENGTH_SHORT).show();


                LatLng driverHomeCordinates = new LatLng(v1,v2);



                mMap.addMarker(new MarkerOptions().position(driverHomeCordinates).title("Your Destination"));


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverHomeCordinates, 12f));

                Toast.makeText(this, cordinates.get(0).toString(), Toast.LENGTH_SHORT).show();



            }
            catch (Exception e ){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }




        }
    }




}
