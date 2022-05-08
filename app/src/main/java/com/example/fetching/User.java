package com.example.fetching;

import java.util.Date;

public class User {
    public String fullname, age, email ,  password, isAdmin, usersID_PA;
    public String lasttimeDonated;
    public String bloodtype;
    public int noDonated;


    public User(){

    }

    public User(String fullname,String age,String email, String password, String isAdmin,String bloodtype, String lasttimeDonated , int noDonated    ){
        this.fullname= fullname;
        this.age= age;
        this.email= email;
        this.password= password;
        this.isAdmin = isAdmin;
        this.bloodtype= bloodtype;
        this.lasttimeDonated= lasttimeDonated;
        this.noDonated= noDonated;

    }


}
