package com.example.ridendrive;


import com.google.firebase.firestore.GeoPoint;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class users {

    String firstName;
    String lastName;
    Date birthday;

    String email;

    String phone;

    String gender;
    String typeOfUser;
    String profilePic;
    String uid_for_ref;

    Date ridetime;
    String haveIAccepted;
    String accepted;
    Date eventDate;
    GeoPoint loc;
    GeoPoint riderGetOffPoint;
    String encodedPath;
     GeoPoint driverDestination;

     String ownerId;
     String myfirstname;



    public users(String firstName, String lastName, Date birthday, String email, String phone, String gender, String typeOfUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;

        this.phone = phone;
        this.gender = gender;
        this.typeOfUser = typeOfUser;
    }

    public String getMyfirstname() {
        return myfirstname;
    }

    public void setMyfirstname(String myfirstname) {
        this.myfirstname = myfirstname;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public GeoPoint getDriverDestination() {
        return driverDestination;
    }

    public void setDriverDestination(GeoPoint driverDestination) {
        this.driverDestination = driverDestination;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public GeoPoint getRiderGetOffPoint() {
        return riderGetOffPoint;
    }

    public void setRiderGetOffPoint(GeoPoint riderGetOffPoint) {
        this.riderGetOffPoint = riderGetOffPoint;
    }

    public GeoPoint getLoc() {
        return loc;
    }

    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getHaveIAccepted() {
        return haveIAccepted;
    }

    public void setHaveIAccepted(String haveIAccepted) {
        this.haveIAccepted = haveIAccepted;
    }

    public users(){

    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getUid() {
        return uid_for_ref;
    }

    public void setUid(String uid) {
        this.uid_for_ref = uid;
    }

    public Date getRidetime() {
        return ridetime;
    }

    public void setRidetime(Date ridetime) {
        this.ridetime = ridetime;
    }

    public  String gettimeNormal(Date date1){
        SimpleDateFormat dt = new SimpleDateFormat("hh : mm a");
        return dt.format(date1);


    }
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }



    public String getPhone() {
        return phone;
    }

    public String isGender() {
        return gender;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }
}


class Events{

    String siteName;
    Date eventDate;
    String owner;
    //LatLng loc;
    String ownerId;
    GeoPoint loc;
    String placeId;
    String mainrootSiteID;
    int sitePop;
    int garbageWeight;
    String encodedPath;
    List<Integer> daysofWeekarray;

    String requestStatus;
    String profilePic;
    GeoPoint riderGetOffPoint;


    public Events(){

    }

//        public Events(String siteName, Date eventDate, Timestamp time, String Owner, LatLng location,String ownerid) {
//            this.siteName = siteName;
//            this.eventDate = eventDate;
//            this.owner = Owner;
//            this.loc = new GeoPoint(location.latitude, location.longitude);
//            this.ownerId = ownerid;
//        }

    public Events(String siteName, Date eventDate, Timestamp time, String Owner, GeoPoint location, String ownerid) {
        this.siteName = siteName;
        this.eventDate = eventDate;
        this.owner = Owner;
        this.loc = location;
        this.ownerId = ownerid;
    }

    public List<Integer> getDaysofWeekarray() {
        return daysofWeekarray;
    }


    public GeoPoint getRiderGetOffPoint() {
        return riderGetOffPoint;
    }

    public void setRiderGetOffPoint(GeoPoint riderGetOffPoint) {
        this.riderGetOffPoint = riderGetOffPoint;
    }

    public void setDaysofWeekarray(List<Integer> daysofWeekarray) {
        this.daysofWeekarray = daysofWeekarray;
    }

    public int getGarbageWeight() {
        return garbageWeight;
    }


    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public void setGarbageWeight(int garbageWeight) {
        this.garbageWeight = garbageWeight;
    }

    public int getSitePop() {
        return sitePop;
    }

    public void setSitePop(int sitePop) {
        this.sitePop = sitePop;
    }

    public String getMainrootSiteID() {
        return mainrootSiteID;
    }

    public void setMainrootSiteID(String mainrootSiteID) {
        this.mainrootSiteID = mainrootSiteID;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSiteName() {
        return siteName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getOwner(){
        return owner;
    }

    public GeoPoint getLoc() {
        return loc;
    }

    public  String getDateNormal(Date date1){
        SimpleDateFormat dt = new SimpleDateFormat("dd/MMM/yyyy");
        return dt.format(date1);


    }

    public  String gettimeNormal(Date date1){
        SimpleDateFormat dt = new SimpleDateFormat("hh : mm a");
        return dt.format(date1);


    }



    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }



}


        class chats{

            String Id_Driver;
            String Id_Rider;
            String message;
            com.google.firebase.Timestamp timestamp;
            String profilePic;
            String username;


            public chats(String id_Driver, String id_Rider, String message1) {
                Id_Driver = id_Driver;
                Id_Rider = id_Rider;
                message = message1;
            }

            public chats() {

            }


            public String getProfilePic() {
                return profilePic;
            }

            public void setProfilePic(String profilePic) {
                this.profilePic = profilePic;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public com.google.firebase.Timestamp getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(com.google.firebase.Timestamp timestamp) {
                this.timestamp = timestamp;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getId_Driver() {
                return Id_Driver;
            }

            public void setId_Driver(String id_Driver) {
                Id_Driver = id_Driver;
            }

            public String getId_Rider() {
                return Id_Rider;
            }

            public void setId_Rider(String id_Rider) {
                Id_Rider = id_Rider;
            }
        }

