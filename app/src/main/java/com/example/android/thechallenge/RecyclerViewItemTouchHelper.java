package com.example.android.thechallenge;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerViewItemTouchHelperListener listener;
    public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs,RecyclerViewItemTouchHelperListener listener)
    {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }
    public interface RecyclerViewItemTouchHelperListener
    {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
