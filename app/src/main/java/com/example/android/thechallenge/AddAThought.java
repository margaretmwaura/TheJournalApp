package com.example.android.thechallenge;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    public Button setUpANotification,noNotification;
    private Context mContext;
    private BottomSheetDialog dialog;
    BottomSheetBehavior bottomSheetBehavior;
    private static int time;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_athought);


        mContext = this;

        final View viewBottom = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(viewBottom);

        //Get the Edit-Texts
        final EditText title = (EditText) findViewById(R.id.the_thoughts_title);
        final EditText thoughts = (EditText) findViewById(R.id.the_thoughts_data);


        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//       This is the code for the button that is needed to set up a notification
       setUpANotification = (Button) viewBottom.findViewById(R.id.set_up_notification);
       setUpANotification.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {

               Intent addTaskIntent = new Intent(AddAThought.this,TheIntentService.class)
                       .setAction(DatabaseActivities.SEND_A_NOTIFICATION);
               startService(addTaskIntent);

               AlertDialog.Builder alert = new AlertDialog.Builder(AddAThought.this);

               alert.setTitle("Enter time");
               alert.setMessage("This is the time after which notification will be sent \n (enter in hours)");

// Set an EditText view to get user input
               final EditText input = new EditText(AddAThought.this);
               alert.setView(input);

               alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton)
                   {
                       if(input.getText() == null) {
                           time = Integer.parseInt(input.getText().toString());
                           dialog.dismiss();
                       }
                   }
               });

               alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton) {
                       // Canceled.

                       dialog.dismiss();
                   }
               });

               alert.show();
               dialog.dismiss();

           }
       });

       noNotification = (Button) viewBottom.findViewById(R.id.no_setting_up_notification);
       noNotification.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
              dialog.dismiss();
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


                dialog.show();


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
    public static int returnTime()
    {
        return time;
    }

}

