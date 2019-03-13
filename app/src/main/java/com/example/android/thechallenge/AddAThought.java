package com.example.android.thechallenge;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import okhttp3.MediaType;

public class AddAThought extends AppCompatActivity{

//    this is the String that will be representing the email
    String Email = "dummy";
    public static String theTitle;
    public  static String theThoughts;
    public RelativeLayout bottomSheetRelativeLayout;
    public Button setUpANotification;
    BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_athought);
//Get the Edit-Texts
        final EditText title = (EditText) findViewById(R.id.the_thoughts_title);
       final EditText thoughts = (EditText) findViewById(R.id.the_thoughts_data);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//       This is the code for the button that is needed to set up a notification
       setUpANotification = (Button) findViewById(R.id.set_up_notification);
       setUpANotification.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               Toast.makeText(AddAThought.this,"This is the bottomNavigation",Toast.LENGTH_LONG).show();
               Intent addTaskIntent = new Intent(AddAThought.this,TheIntentService.class)
                       .setAction(DatabaseActivities.SEND_A_NOTIFICATION);
               startService(addTaskIntent);
           }
       });
       bottomSheetRelativeLayout = (RelativeLayout) findViewById(R.id.bottom_sheet_layout);

       bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetRelativeLayout);


       bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
       {
           @Override
           public void onStateChanged(@NonNull View bottomSheet, int newState) {

           }

           @Override
           public void onSlide(@NonNull View bottomSheet, float slideOffset) {

           }
       });
//The date will be auto-generated


//        this is for the clicking of the add button
         Button add =(Button) findViewById(R.id.add);

          add.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

//                This is the method that is necessary for hiding the keyboard so that the bottom sheet can show
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);


                theTitle = title.getText().toString();
                theThoughts = thoughts.getText().toString();

//                This is the code for the bottomSheet behavour
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Log.d("BottomSheet","This method has been called to expand the bottom sheet");
                }
                else
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Log.d("BottomSheet","This method has been called to collapse the bottom sheet");
                }
                // Create a new intent to start an intent Service
//                Inturn the intent Service will trigger the database activity
//                the particular activity is to add an entry to the journal
//                the activity to be triggered will correspond to the action
                Intent addTaskIntent = new Intent(AddAThought.this,TheIntentService.class)
                        .setAction(DatabaseActivities.UPDATE_DATABASE);
                     startService(addTaskIntent);
                     Log.d("Intent", "Intent Service started");



            }
        });
    }

    public static String[] getData()
    {
        String[] results = new String[2];
        results[0] = theTitle;
        results[1] = theThoughts;

        return results;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}

