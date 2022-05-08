package com.example.fetching;

import android.net.Uri;

import com.google.firebase.database.Exclude;

public class Post {
    //  public String body;

    private String subject, bloodtypeReq, info, date ;
    private String imageURl;
    private String mKey;

    public Post() {
// Empty Constructor Needed
    }


    public Post(String info, String subject, String bloodTReq, String date , String imageURl ) {
        this.date = date;
        this.subject = subject;
        this.bloodtypeReq = bloodTReq;
        this.info = info;
        this.imageURl= imageURl;

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    public String getSubject() {
        return subject;
    }

    public String getBloodtypeReq() {
        return bloodtypeReq;
    }

    public String getInfo() {
        return info;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBloodtypeReq(String bloodtypeReq) {
        this.bloodtypeReq = bloodtypeReq;
    }

    public void setInfo(String info) {
        this.info = info;
    }



    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}

