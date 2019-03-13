package com.example.android.thechallenge;

import android.app.LoaderManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecyclerViewItemTouchHelper.RecyclerViewItemTouchHelperListener{


//    Get a reference to the recyclerView and the Adapter

    private RecyclerView recyclerView;
    private TheJournalAdpater theJounalAdapter;

    public String Id;

    private UserViewModel viewModel;
    private static TheUser result;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<TheUser> usersJournal = new ArrayList<>();

    private int onCreateBoolean;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Log.d("OnCreate","The onCreate method has been called ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateBoolean = 0;

        //        get the recyclerView

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Id = currentUser.getUid();
        Log.d("The user id ", "This is the user id " + Id);

//        this is the code for creating of the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        theJounalAdapter = new TheJournalAdpater(this);
//        this code is for setting the adapter of the recyclerView
        recyclerView.setAdapter(theJounalAdapter);

        FloatingActionButton fabButton = findViewById(R.id.fab);

//        This is the data that is responsible for listening to data from firebase
//        This is the code for reading data from the database
        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();


            Log.d("OnCreate","This is the value of the boolean " + onCreateBoolean);
            livedata.observe(this, new Observer<DataSnapshot>()
            {

                    @Override
                    public void onChanged (@Nullable DataSnapshot dataSnapshot)
                    {
                        Log.d("Livedata","This method has been called ");
                        DataSnapshot userID = dataSnapshot.child(Id);

                        Boolean exist = userID.exists();
                        Log.d("Confirming", "This confirms that the datasnapshot exists " + exist);
                        Iterable<DataSnapshot> journals = userID.getChildren();
                        for (DataSnapshot journal : journals) {
                            TheUser maggie = new TheUser();
                            maggie = journal.getValue(TheUser.class);
                            usersJournal.add(maggie);
                        }

                        onCreateBoolean = 1;
                        Log.d("TheListRead", "This are the number of journals found " + usersJournal.size());
                        theJounalAdapter.setTasks(usersJournal);
                    }

            });




//This is for adding a new entry into the journal
//        Hence adding a new entry into the databasse
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(MainActivity.this, AddAThought.class).putExtra("theAccountId", Id);
                startActivity(addTaskIntent);
            }
        });

//        This code is for deleting an item from the database
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT  | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);



    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position)
    {
          result = usersJournal.get(viewHolder.getAdapterPosition());
          theJounalAdapter.removeItem(viewHolder.getAdapterPosition());
        Intent addTaskIntent = new Intent(MainActivity.this,TheIntentService.class)
                .setAction(DatabaseActivities.DELETE_ENTRY);
        startService(addTaskIntent);
    }

    public static TheUser returnUserToDelete()
    {
        return result;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        onCreateBoolean = 1;
        if (viewModel != null && viewModel.getLiveData().hasObservers())
        {
            viewModel.getLiveData().removeObservers(this);
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Log.d("OnResume","The onresume method has been called ");
        if(onCreateBoolean == 1)
        {
            viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();


            Log.d("OnCreate","This is the value of the boolean " + onCreateBoolean);
            livedata.observe(this, new Observer<DataSnapshot>()
            {

                @Override
                public void onChanged (@Nullable DataSnapshot dataSnapshot)
                {
                    usersJournal.clear();
                    Log.d("LivedataResume","This method has been called ");
                    DataSnapshot userID = dataSnapshot.child(Id);

                    Boolean exist = userID.exists();
                    Log.d("Confirming", "This confirms that the datasnapshot exists " + exist);
                    Iterable<DataSnapshot> journals = userID.getChildren();
                    for (DataSnapshot journal : journals) {
                        TheUser maggie = new TheUser();
                        maggie = journal.getValue(TheUser.class);
                        usersJournal.add(maggie);
                    }
                    onCreateBoolean = 1;
                    Log.d("TheListRead", "This are the number of journals found " + usersJournal.size());
                    theJounalAdapter.setTasks(usersJournal);
                }

            });

        }
    }
}
