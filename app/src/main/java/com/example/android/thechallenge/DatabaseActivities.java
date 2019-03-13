package com.example.android.thechallenge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.android.thechallenge.AddAThought.getData;
import static com.example.android.thechallenge.Login.returnRegistrationToken;
import static com.example.android.thechallenge.MainActivity.returnUserToDelete;

/**
 * Created by TOSHIBA on 6/29/2018.
 */

//Will be using email to identify a user uniquely

public class DatabaseActivities {
//    Changed firebase to public static and the others to public
    public  FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;
    public FirebaseAuth mAuth;
    public static final String UPDATE_DATABASE = "updating the database";
    public static final String DELETE_ENTRY = "deleting an entry";
    public static final String SEND_A_NOTIFICATION = "send a notification";
    private static final String LEGACY_SERVER = "AIzaSyALmn8y8Jnuzww1RYNYGcms_3-cA-T8l5M";

    public String Id;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
//    Will need to do modifications since the user object will have to be created from the
//    data entered

    public void insert() {
//        This is now the code necessary for obtaining the data from the other previous activity
//        Will have to check the action before proceeding

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mref = mFirebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();

            if (mAuth.getCurrentUser() != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                 Id = user.getUid();
                Log.d("USER", "there is a user");
            }

             String[] results = getData();
//            This code is for the current time the data was added
//        This time will be retrieved as a long object type


        Log.d("TheCurrentTine","This is the current time " + ServerValue.TIMESTAMP);
            TheUser maggie = new TheUser(results[0],results[1]);

//            maggie.setTitle(results[0]);
//            maggie.setThoughts(results[1]);
//            maggie.setHashMap(map);


//            Added push to the db reference
//            the set value is supposed to be the java object
            mref.child("users").child(Id).push().setValue(maggie).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid)
                {
                    Log.d("Firebase","Data has been succesfully added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase","No data added");
                }
            });

        }
//        public void query()
//        {
//
//            mref.child("users").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot)
//                {
//                       showData(dataSnapshot);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError)
//                {
//
//
//                }
//            });
//
//        }
//
        public void actionTaken(String action){
            if(UPDATE_DATABASE.equals(action))
            {
                insert();
            }
            if(DELETE_ENTRY.equals(action))
            {
                deleteFromDatabase();
            }
            if(SEND_A_NOTIFICATION.equals(action))
            {
                sendNotification();
            }

        }

    private void deleteFromDatabase()
    {
        TheUser maggie = new TheUser();
        maggie = returnUserToDelete();
        String title = maggie.getTitle();
        Log.d("TheTitle","This is the title of what is to be deleted " + maggie.getTitle());

//        Arguemnets declaration
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Id = user.getUid();

//        End of arguements declaration
        Query JournalQuery = ref.child("users").child(Id).orderByChild("title").equalTo(title);
        JournalQuery.addListenerForSingleValueEvent(new ValueEventListener()
                {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//
//    private void showData(DataSnapshot dataSnapshot)
//    {
//        List<TheUser> usersJournal = new ArrayList<>();
//        DataSnapshot userID = dataSnapshot.child(Id);
//
//        Boolean exist = userID.exists();
//        Log.d("Confirming","This confirms that the datasnapshot exists " + exist);
//        Iterable<DataSnapshot> journals = userID.getChildren();
//        for(DataSnapshot journal : journals)
//        {
//            TheUser maggie = new TheUser();
//            maggie = journal.getValue(TheUser.class);
//            usersJournal.add(maggie);
//        }
//
//        Log.d("TheListRead","This are the number of journals found " + usersJournal.size());
//
//    }
//
//    public String[] queryResult()
//    {
//        query();
//      results = new String[]{Title,thoughts};
//      return results;
//    }
      public void sendNotification()
      {
          Log.d("NotificationSending","The method has been called to send a notification");
          String regToken = returnRegistrationToken();
          OkHttpClient client = new OkHttpClient();
          JSONObject json=new JSONObject();
          JSONObject dataJson=new JSONObject();
          try {
              dataJson.put("body", "Reminder about the journal");
              dataJson.put("title", "Journal entry");
              json.put("notification", dataJson);
              json.put("to", regToken);
          }
          catch (Exception e)
          {
              Log.d("NotificationSettingUp", "An error was caught : details " + e.getMessage());
          }
          RequestBody body = RequestBody.create(JSON, json.toString());
          Request request = new Request.Builder()
                  .header("Authorization","key="+LEGACY_SERVER)
                  .url("https://fcm.googleapis.com/fcm/send")
                  .post(body)
                  .build();
          try {
              Response response = client.newCall(request).execute();
              String finalResponse = response.body().string();
          }
          catch (Exception e)
          {
              Log.d("NotificationExecution","An error was caught : details " + e.getMessage());
          }
//          String finalResponse = response.body().string();

          Log.d("NotificationSending","The method has completed");
      }

}
