package com.example.adil.checkup.Helpers;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.Adapters.ImageAdapter;
import com.example.adil.checkup.models.Image;
import com.example.adil.checkup.models.Medicine;

import java.util.List;

/**
 * Created by adil on 8/9/17.
 */

public class ImageSwipeHelper extends ItemTouchHelper.SimpleCallback{
    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #( RecyclerView )}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    ImageAdapter adapter;
    List<Image> mMeds ;
    public ImageSwipeHelper(int dragDirs,int swipDirs) {
        super(dragDirs,swipDirs);
    }

    public ImageSwipeHelper(ImageAdapter adapter){
        super(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT);
        this.adapter=adapter;

    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {




         adapter.notifyDataSetChanged();
        //Log.d("asss",String.valueOf(adapter.getMeds().get(viewHolder.getAdapterPosition())));
        adapter.dele(adapter.getMeds().get(viewHolder.getAdapterPosition()));


//        Log.d("ITEM ID" , String.valueOf(postition));

    }
}
