package com.example.adil.checkup.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.DatabaseHelper;
import com.example.adil.checkup.Helpers.SwipeHelper;
import com.example.adil.checkup.MainActivity;
import com.example.adil.checkup.R;
import com.example.adil.checkup.SQLiteHandler;

import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Tag;
import com.example.adil.checkup.models.Todo;
import com.example.adil.checkup.models.meds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FloatingActionButton fab;
    private DatabaseHelper db ;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    protected String[] mDataset;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MedicationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicationFragment newInstance(String param1, String param2) {
        MedicationFragment fragment = new MedicationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medication, container, false);
        fab= (FloatingActionButton) view.findViewById(R.id.fab_add);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.medication));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"fabclircke",Toast.LENGTH_LONG).show();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Add_medication dialogFragment = new Add_medication ();
                dialogFragment.setOnSaveListener(new Add_medication.OnSaveListener() {
                    @Override
                    public void onSave() {
                        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
                        EntityDataStore<Persistable> dataStore = app.getDataStore();
                        List<Medicine> all=dataStore.select(Medicine.class).get().toList();
                        mAdapter = new CustomAdapter(getContext(),new ArrayList<Medicine>(all));
                        // Set CustomAdapter as the adapter for RecyclerView.
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
                dialogFragment.show(fm,"sssss");

            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;



        db= new DatabaseHelper(view.getContext());
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        EntityDataStore<Persistable> dataStore = app.getDataStore();
        List<Medicine> all=dataStore.select(Medicine.class).get().toList();
//        Medicine.toArray();
//        List <meds> all=db.getAllMeds();


        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        // Set CustomAdapter as the adapter for RecyclerView.
        mAdapter = new CustomAdapter(getContext(),new ArrayList(all));
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

      ItemTouchHelper.Callback callback=new SwipeHelper((CustomAdapter) mAdapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        mLinearLayoutRadioButton = (RadioButton) view.findViewById(R.id.linear_layout_rb);
        mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
            }
        });

        mGridLayoutRadioButton = (RadioButton) view.findViewById(R.id.grid_layout_rb);
        mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
            }
        });




                return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
//    new functions
public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
    int scrollPosition = 0;

    // If a layout manager has already been set, get current scroll position.
    if (mRecyclerView.getLayoutManager() != null) {
        scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();
    }

    switch (layoutManagerType) {
        case GRID_LAYOUT_MANAGER:
            mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
            break;
        case LINEAR_LAYOUT_MANAGER:
            mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
            break;
        default:
            mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
    }

    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.scrollToPosition(scrollPosition);
}
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }
}
