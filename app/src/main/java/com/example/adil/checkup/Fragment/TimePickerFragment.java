package com.example.adil.checkup.Fragment;

/**
 * Created by adil on 7/16/17.
 */

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

import android.view.View;
import com.example.adil.checkup.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public int time(int c)
    {
        return c;
    }
    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
//        TextView tv = (TextView) getActivity().findViewById(R.id.a_time);
       time(hourOfDay);
//        //Set a message for user
//        tv.setText("Your chosen time is...\n\n");
//        //Display the user changed time on TextView
//        tv.setText(tv.getText()+ "Hour : " + String.valueOf(hourOfDay)
//                + "\nMinute : " + String.valueOf(minute) + "\n");
    }


}
