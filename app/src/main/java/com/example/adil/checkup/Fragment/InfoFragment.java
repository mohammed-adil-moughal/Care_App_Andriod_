package com.example.adil.checkup.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adil.checkup.Activities.CameraActivity;
import com.example.adil.checkup.Activities.GalleryActivity;
import com.example.adil.checkup.Adapters.CustomAdapter;
import com.example.adil.checkup.Adapters.GlucoseAdapter;
import com.example.adil.checkup.Adapters.ImageAdapter;
import com.example.adil.checkup.Adapters.VisitAdapter;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.Helpers.ImageSwipeHelper;
import com.example.adil.checkup.Helpers.SwipeHelper;
import com.example.adil.checkup.MainActivity;
import com.example.adil.checkup.R;
import com.example.adil.checkup.models.Glucose;
import com.example.adil.checkup.models.Hospital;
import com.example.adil.checkup.models.Image;
import com.example.adil.checkup.models.Medicine;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.series.DataPoint;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FloatingActionButton fab;
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    private static final int SPAN_COUNT = 2;
    private RecyclerView.Adapter mAdapter;
    private static final int DATASET_COUNT = 60;
    protected String[] mDataset;
//    private ImageAdapter imageAdapter;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.important_docs));
        final FloatingActionButton camera = (FloatingActionButton)view.findViewById(R.id.camera_action);
        camera
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePicture, 0);//zero can be replaced with any action code

      Intent camera=new Intent(getContext(), CameraActivity.class);
                startActivity(camera);
               getActivity().overridePendingTransition( R.anim.slide_in_up,R.anim.slide_out_up );

            }
        });

        final FloatingActionButton gallery= (FloatingActionButton) view.findViewById(R.id.gallery_action);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent gallery=new Intent(getContext(),GalleryActivity.class);
                startActivity(gallery);
                getActivity().overridePendingTransition( R.anim.slide_in_up,R.anim.slide_out_up );

            }
        });
//        getFromSdcard();
//        GridView imagegrid = (GridView)view.findViewById(R.id.PhoneImageGrid);
//        imageAdapter = new ImageAdapter();
//        imagegrid.setAdapter(imageAdapter);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        CheckUp app = ((CheckUp) getActivity().getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
        List<Image> all= mdataStore.select(Image.class).get().toList();
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new ImageAdapter(getContext(), new ArrayList(all));
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback=new ImageSwipeHelper((ImageAdapter) mAdapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        Log.d("IMAGES", String.valueOf(all));
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
//    public void getFromSdcard()
//    {
//        File file= new File(android.os.Environment.getExternalStorageDirectory(),"/"+"Checkup");
//
//        if (file.isDirectory())
//        {
//            listFile = file.listFiles();
//
//
//            for (int i = 0; i < listFile.length; i++)
//            {
//
//                f.add(listFile[i].getAbsolutePath());
//
//            }
//        }
//    }
//
//    public class ImageAdapter extends BaseAdapter {
//        private LayoutInflater mInflater;
//
//        public ImageAdapter() {
//            mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        public int getCount() {
//            return f.size();
//        }
//
//        public Object getItem(int position) {
//            return position;
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = mInflater.inflate(
//                        R.layout.galleryitem, null);
//                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
//
//                convertView.setTag(holder);
//            }
//            else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
//            holder.imageview.setImageBitmap(myBitmap);
//            return convertView;
//        }
//    }
//    class ViewHolder {
//        ImageView imageview;
//
//
//    }
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
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        //Toast.makeText(getContext(),"resume",Toast.LENGTH_LONG).show();
        CheckUp app = ((CheckUp) getActivity().getApplicationContext());
        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
        List<Image> all= mdataStore.select(Image.class).get().toList();
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new ImageAdapter(getContext(), new ArrayList(all));

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.removeAllViewsInLayout();
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(null);
        mRecyclerView.setAdapter(mAdapter);



    }
}


