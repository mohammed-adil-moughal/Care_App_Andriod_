package com.example.adil.checkup.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.adil.checkup.Activities.AddVisitActivity;
import com.example.adil.checkup.Activities.BloodGlucose;
import com.example.adil.checkup.Activities.BloodPressure;
import com.example.adil.checkup.Activities.chatactivity;
import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.Adapters.VisitAdapter;
import com.example.adil.checkup.AppConfig;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.Helpers.SwipeHelper;
import com.example.adil.checkup.Helpers.VisitSwipeHelper;
import com.example.adil.checkup.MainActivity;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Chat;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Reminder;
import com.example.adil.checkup.models.Timer;
import com.example.adil.checkup.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by adil on 7/8/17.
 */
public class HospitalFragment extends Fragment {

    private boolean shouldRefreshOnResume = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public Button BtnBloodPressure;
    public Button BtnBloodGlucose;
    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    private static final int SPAN_COUNT = 2;
    private RecyclerView.Adapter mAdapter;
    private static final int DATASET_COUNT = 60;
    protected String[] mDataset;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HospitalFragment() {
        // Required empty public constructor
    }
//    @Override
//    public void onStart()
//    {
//        CheckUp app = ((CheckUp)getActivity().getApplicationContext());
//        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
//        final User user=mdataStore.select(User.class).get().first();
//        try {
//            mdataStore.delete(Hospital.class).get().value();
//            mdataStore.delete(Reminder.class).get().value();
//        }
//        catch (Exception e)
//        {
//            Log.d("DELETE",e.toString());
//        }
//        // TODO Auto-generated method stub
//
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        Log.d("User123", String.valueOf(user.getUser_UID()));
//        final String url = AppConfig.GET_VISIT+user.getUser_UID();
//        Log.d("User123", url);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                       // Log.d("User123", String.valueOf(response));
//                        for (int i = 0; i < response.length(); i++) {
//
//                            JSONObject doc = null;
//                            try {
//                                doc = response.getJSONObject(i);
//                                Log.d("User123",doc.toString());
//                                Hospital hospital=new Hospital();
//                                hospital.setHospital_visit_height_feet(doc.getInt("height_feet"));
//                                hospital.setHospital_visit_height_inches(doc.getInt("height_inches"));
//                                hospital.setHospital_visit_weight(doc.getInt("weight"));
//                                hospital.setHospital_visit_bloodPressure(doc.getInt("bloodpressure"));
//                                hospital.setHospital_visit_temperature(doc.getInt("temperature"));
//                                hospital.setHospital_visit_diagnosis(doc.getString("diagnosis"));
//                                hospital.setHospital_visit_treatment(doc.getString("treatment"));
//                                SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");
//
//                                Calendar cal1 = Calendar.getInstance();
//                                cal1.add(Calendar.DAY_OF_WEEK, 7);
//                                final Date date1=cal1.getTime();
//
//                                Date parsedDate = new Date();
//                                try {
//                                  parsedDate = formatter.parse(doc.getString("created_at"));
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                                hospital.setHospital_visit_date(parsedDate);
//                                mdataStore.insert(hospital);
//                                Log.d("User12345", String.valueOf(hospital.getHospital_visit_id()));
//                                Reminder reminder=new Reminder();
//                                reminder.setHospital_visit_id(hospital.getHospital_visit_id());
//                                reminder.setReminder_date(date1);
//                                mdataStore.insert(reminder);
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                error.printStackTrace();
//                Log.d("User123", String.valueOf(error));
//            }
//        }

//        );
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        requestQueue.add(jsonArrayRequest);
//        super.onStart();
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HospitalFragment newInstance(String param1, String param2) {
        HospitalFragment fragment = new HospitalFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.hospital_visit));
        fab= (FloatingActionButton) view.findViewById(R.id.fab_add);



     DatePickerDialog fromDatePickerDialog;
        final DatePickerDialog[] toDatePickerDialog = new DatePickerDialog[1];

 SimpleDateFormat dateFormatter = null;


        final SimpleDateFormat finalDateFormatter = dateFormatter;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     Intent addVisit=  new Intent(v.getContext(), AddVisitActivity.class);
                  startActivity(addVisit);

                }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
        EntityDataStore<Persistable> dataStore = app.getDataStore();
        List<Hospital> all=dataStore.select(Hospital.class).where((Hospital.HOSPITAL_VISIT_DATE).notNull()).get().toList();
        Log.d("User1234",all.toString());
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new VisitAdapter(getContext(), new ArrayList(all));
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback=new VisitSwipeHelper((VisitAdapter) mAdapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
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
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(shouldRefreshOnResume){
            Toast.makeText(getContext(),"resuems",Toast.LENGTH_LONG).show();
            CheckUp app =   ((CheckUp)getContext().getApplicationContext());
            EntityDataStore<Persistable> dataStore = app.getDataStore();
            List<Hospital> all=dataStore.select(Hospital.class).where((Hospital.HOSPITAL_VISIT_DATE).notNull()).get().toList();
            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
            mAdapter = new VisitAdapter(getContext(), new ArrayList(all));
            // Set CustomAdapter as the adapter for RecyclerView.
            mRecyclerView.setAdapter(mAdapter);




        }
    }
    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }
}
