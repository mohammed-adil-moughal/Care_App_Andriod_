package com.example.adil.checkup.Fragment;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adil.checkup.Activities.AddVisitActivity;
import com.example.adil.checkup.Activities.BloodGlucose;
import com.example.adil.checkup.Activities.BloodPressure;
import com.example.adil.checkup.Activities.EmergencyInfo;
import com.example.adil.checkup.Activities.Profile;
import com.example.adil.checkup.Activities.chatactivity;
import com.example.adil.checkup.CheckUp;
import com.example.adil.checkup.LocaleHelper;
import com.example.adil.checkup.LoginActivity;
import com.example.adil.checkup.MainActivity;

import com.example.adil.checkup.R;
import com.example.adil.checkup.SQLiteHandler;
import com.example.adil.checkup.models.Chat;
import com.example.adil.checkup.models.Image;
import com.example.adil.checkup.models.User;

import java.util.HashMap;

import io.requery.Nullable;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SQLiteHandler db;
    private String name;
    private String email;
    private  ImageView profile;
    private  ImageView medication;
    private  ImageView bloodglucose;
    private ImageView bloodpressure;
    private ImageView hospitalvisits;
    private ImageView importantdocs;
    private ImageView emergecyinfo;
    private ImageView chat;
    private TextView Medic;
    private TextView Prof;
    private TextView BG;
    private TextView BP;
    private TextView HosVisits;
    private TextView ImpDocs;
    private TextView Ch;
    private TextView Em;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();


        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.home));
       Medic= (TextView) view.findViewById(R.id.textView17MED);
        Prof= (TextView) view.findViewById(R.id.textView18PROFILE);
        BG= (TextView) view.findViewById(R.id.textView23);
        BP= (TextView) view.findViewById(R.id.textView20BP);
        HosVisits=(TextView) view.findViewById(R.id.textView21HV);
        ImpDocs=(TextView) view.findViewById(R.id.textView22IMPDOC);
        Ch=(TextView) view.findViewById(R.id.textView24CHAT);
        Em=(TextView) view.findViewById(R.id.textView25EME);





        profile= (ImageView) view.findViewById(R.id.imageprofile);
        medication= (ImageView) view.findViewById(R.id.imagemedication);
        bloodglucose= (ImageView) view.findViewById(R.id.imageglucose);
        bloodpressure= (ImageView) view.findViewById(R.id.imagepressure);
        hospitalvisits= (ImageView) view.findViewById(R.id.imagehospital);
        importantdocs= (ImageView) view.findViewById(R.id.imagerecords);
        chat= (ImageView) view.findViewById(R.id.imagechat);
        emergecyinfo=(ImageView)view.findViewById(R.id.imageemergency);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addVisit=  new Intent(v.getContext(), Profile.class);
                startActivity(addVisit);
            }
        });
        medication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new MedicationFragment();
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fraghome, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });
        bloodpressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bp=new Intent(v.getContext(), BloodPressure.class);
                startActivity(bp);
            }
        });
        bloodglucose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bg=new Intent(v.getContext(),BloodGlucose.class);
                startActivity(bg);
            }
        });
        hospitalvisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new HospitalFragment();
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fraghome, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        importantdocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new InfoFragment();
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fraghome, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changed chat activity to EmergencyInfo
                Intent chat=new Intent(v.getContext(),chatactivity.class);
                startActivity(chat);
            }
        });
        emergecyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changed chat activity to EmergencyInfo
                Intent chat=new Intent(v.getContext(),EmergencyInfo.class);
                startActivity(chat);
            }
        });
//        db = new SQLiteHandler(getActivity().getApplicationContext());
//        CheckUp app =   ((CheckUp)getContext().getApplicationContext());
//        final EntityDataStore<Persistable> mdataStore = app.getDataStore();
//        HashMap<String, String> user = db.getUserDetails();
//
//        name = user.get("name");
//        email = user.get("email");
//

//        User first = mdataStore.select(User.class).get().firstOrNull();
//        if (first == null) {
//            Intent intent = new Intent(view.getContext(), LoginActivity.class);
//            startActivity(intent);
//
//        }
//
//        TextView txtName = (TextView) view.findViewById(R.id.namehome);
//        TextView txtEmail = (TextView) view.findViewById(R.id.emailhome);final ImageView profile= (ImageView) view.findViewById(R.id.imageprofile);


//        txtName.setText(first.getUser_name());
//        txtEmail.setText(first.getUser_email());


        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // TODO Add your menu entries here
        inflater.inflate(R.menu.main_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.action_language)
        {
            final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Language");
            alertDialog.setMessage("Select Language");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "English",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateViews("en");
                            dialog.dismiss();

                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Swahili",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateViews("sw");
                            dialog.dismiss();

                        }
                    });


            alertDialog.show();
        }
        // user is in notifications fragment
        // and selected 'Mark all as Read'


        // user is in notifications fragment
        // and selected 'Clear All'


        return super.onOptionsItemSelected(item);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();
        Medic.setText(resources.getString(R.string.medication));
        Prof.setText(resources.getString(R.string.profile));
        BG.setText(resources.getString(R.string.blood_glucose));
        BP.setText(resources.getString(R.string.blood_pressure));
        HosVisits.setText(resources.getString(R.string.hospital_visit));
        ImpDocs.setText(resources.getString(R.string.important_docs));
        Ch.setText(resources.getString(R.string.chat));
        Em.setText(resources.getString(R.string.emergency));


    }


}
