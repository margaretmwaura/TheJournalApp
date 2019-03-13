package com.example.android.thechallenge;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TOSHIBA on 6/29/2018.
 */

//this is the class that will be used for data binding
public class TheUser implements Parcelable
{
    @SerializedName("thoughts")
    private String Thoughts;
    @SerializedName("title")
    private String Title;

//    private HashMap<String,Object> hashMap;

//    This object instance takes the current time when data is being added to the database
    @SerializedName("createdTimeStamp")
    Object createdTimeStamp;

//This are the methods for setting data
    public TheUser()
    {

    }
    public TheUser(String title,String thoughts)
    {
        this.Thoughts = thoughts;
        this.Title = title;

//         HashMap<String,Object> hashMapNew = new HashMap() ;
//         hashMapNew.put("timestamp", ServerValue.TIMESTAMP);
//         this.hashMap = hashMapNew;
        createdTimeStamp = ServerValue.TIMESTAMP;

    }


    public void setTitle(String Title)
    {
        this.Title = Title;

    }
    public void setThoughts(String Thoughts)
    {
        this.Thoughts= Thoughts;
    }
//     public void setHashMap(Map hashMapSecond)
//     {
//         hashMap.putAll(hashMapSecond);
//     }

//    This are the methods for retrieving the data

    public String getTitle()
    {
        return this.Title;
    }

    public String getThoughts()
    {
        return this.Thoughts;
    }

    public Object getCreatedTimeStamp()
    {
        return this.createdTimeStamp;
    }

    protected TheUser(Parcel in) {
        Thoughts = in.readString();
        Title = in.readString();

    }

    public static final Creator<TheUser> CREATOR = new Creator<TheUser>() {
        @Override
        public TheUser createFromParcel(Parcel in) {
            return new TheUser(in);
        }

        @Override
        public TheUser[] newArray(int size) {
            return new TheUser[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(Thoughts);
        parcel.writeString(Title);

    }

    }


