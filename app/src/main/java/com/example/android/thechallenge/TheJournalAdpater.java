package com.example.android.thechallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by TOSHIBA on 6/29/2018.
 */

//for this class I will have to deal with the string that is theJournalData and the bind method
public class TheJournalAdpater extends RecyclerView.Adapter<TheJournalAdpater.theJournalViewHolder>
{

    //this specifies the number
    private List<TheUser> mTaskEntries;
    Context mContext;
    @NonNull
    @Override
    public theJournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        //        This code is for getting the context of the viewGroup
        Context context = parent.getContext();
//        this is for getting the id of the layout with the data textView
        int id = R.layout.the_journal_list;
//        This is for inflating of the layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(id,parent,shouldAttachToParentImmediately);
        return new theJournalViewHolder(view);
    }

//    the constructor

    public TheJournalAdpater(Context context)
    {
      mContext = context;
    }

    @Override
    public void onBindViewHolder( theJournalViewHolder holder, int position)
    {
//        to be properly implemented later

        // Determine the values of the wanted data
        TheUser maggie = mTaskEntries.get(position);
        String title = maggie.getTitle();
        String thought = maggie.getThoughts();
        Long date = (Long) maggie.getCreatedTimeStamp();

//        Conerting the long value into a date value
        Date currentDate = new Date(date);
        DateFormat df = new SimpleDateFormat("EEE ,d MMM yyyy HH:mm:ss ");
        String dateString = df.format(currentDate);

        //Set values to the textBoxes
        holder.title.setText(title);
        holder.data.setText(thought);
        holder.date.setText(dateString);


    }

    @Override
    public int getItemCount() {
        if(mTaskEntries==null)
        {
            return 0;
        }
//        This is the correct version
        return mTaskEntries.size();

//        For trying purposes
//        return 3;
    }

    public List<TheUser> getTasks() {
        return mTaskEntries;
    }

    public void setTasks(List<TheUser> taskEntries) {
        mTaskEntries = taskEntries;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mTaskEntries.remove(mTaskEntries.get(position));
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

//    this is the inner class for the viewHolder

    class theJournalViewHolder extends RecyclerView.ViewHolder
    {

//        code for the three textViews
        TextView title;
        TextView date;
        TextView data;


        public theJournalViewHolder(View itemView)
        {
            super(itemView);
            title =  itemView.findViewById(R.id.the_journal_title);
            date = itemView.findViewById(R.id.the_journal_date);
            data =  itemView.findViewById(R.id.the_journal_thoughts);

        }



    }
}
