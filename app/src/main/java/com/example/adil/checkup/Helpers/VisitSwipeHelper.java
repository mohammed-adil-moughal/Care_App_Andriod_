package com.example.adil.checkup.Helpers;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.Adapters.VisitAdapter;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;

import java.util.List;

/**
 * Created by adil on 5/15/17.
 */
public class VisitSwipeHelper extends ItemTouchHelper.SimpleCallback{
    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    VisitAdapter adapter;
    List<Hospital> hopsital;
        public VisitSwipeHelper(int dragDirs, int swipDirs) {
        super(dragDirs,swipDirs);
    }

    public VisitSwipeHelper(VisitAdapter adapter){
        super(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT);
        this.adapter=adapter;

    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        adapter.dele(adapter.getMeds().get(viewHolder.getAdapterPosition()));



    }
}
