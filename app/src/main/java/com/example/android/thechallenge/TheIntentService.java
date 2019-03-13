package com.example.android.thechallenge;

import android.app.IntentService;


import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;


/**
 * Created by TOSHIBA on 6/29/2018.
 */

public class TheIntentService extends IntentService
{
    public TheIntentService()
    {
        super("TheIntentService");
    }

    @Override
    protected void onHandleIntent( Intent intent)
    {

//        Will deal with the action which needs to be undertaken later
//      retrieving the action
        DatabaseActivities mine = new DatabaseActivities();
       String action = intent.getAction();
       mine.actionTaken(action);

        Log.d("Intent","Database started");

    }
}
