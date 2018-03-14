package com.example.adil.checkup.Helpers;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.TextView;

import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.models.Medicine;

import java.util.List;

/**
 * Created by adil on 5/15/17.
 */
public class SwipeHelper extends ItemTouchHelper.SimpleCallback{
    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    CustomAdapter adapter;
    List<Medicine> mMeds ;
        public SwipeHelper(int dragDirs,int swipDirs) {
        super(dragDirs,swipDirs);
    }

    public SwipeHelper(CustomAdapter adapter){
        super(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT);
        this.adapter=adapter;

    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

//        Medicine contact = mMeds.get(viewHolder.getAdapterPosition());
//
//
//       int id= contact.getMedicine_id();

//        int postition= (int) viewHolder.getItemId();

//        Medicine med=mMeds.get(postition);

//   adapter.dele(viewHolder.getAdapterPosition());

        adapter.dele(adapter.getMeds().get(viewHolder.getAdapterPosition()));


//        Log.d("ITEM ID" , String.valueOf(postition));

    }
}
