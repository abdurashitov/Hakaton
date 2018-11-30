package com.example.hakaton;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

public class Setting extends Fragment {

    public static final String STORAGE_NAME = "StorageName";
    Calendar dateAndTime=Calendar.getInstance();
    Button button;
    Button button1;
    Button button2;
    String time_from;
    String time_up;
    String[] hour_from = new String[5];
    String[] time_from_on = new String[2];
    String[] hour_up = new String[5];
    String[] time_up_on = new String[2];
    Spinner spinner;
    boolean flag=true;
    SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        button = v.findViewById(R.id.but_time_from);
        button1 = v.findViewById(R.id.but_time_up);
        button2 = (Button) v.findViewById(R.id.btn_notif);
        button.setOnClickListener(myButtonClickListener);
        button1.setOnClickListener(myButtonClickListener);
        button2.setOnClickListener(myButtonClickListener);
        spinner=v.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(spiner_select);
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        button.setText(settings.getString("время_от", "От"));
        button1.setText(settings.getString("время_до", "До"));
        if (settings.getBoolean("уведомления",false))
            button2.setText("Выключить уведомления");
        else
            button2.setText("Включить уведомления");
        return v;
    }

    AdapterView.OnItemSelectedListener spiner_select = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            int h=10;
            switch (position) {
                case (0):
                    h=10;
                    break;
                case (1):
                    h=20;
                    break;
                case (2):
                    h=30;
                    break;
                case (3):
                    h=45;
                    break;
                case (4):
                    h=60;
                    break;
                case (5):
                    h=1;
                    break;
            }
            editor.putInt( "переодичность",h);
            editor.commit();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt( "переодичность",10);
            editor.commit();
        }
    };


    View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePickerDialog tpd = new TimePickerDialog(getActivity(), t,
                    dateAndTime.get(Calendar.HOUR_OF_DAY),
                    dateAndTime.get(Calendar.MINUTE), true);
            switch (v.getId()) {
                case R.id.but_time_from:
                    tpd.show();
                    flag = true;
                    break;
                case R.id.but_time_up:
                    flag = false;
                    tpd.show();
                    break;
                case R.id.btn_notif:
                    notifications_button();
                    break;
            }
        }
    };

    public void notifications_button(){
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        boolean check_notification;
        check_notification=settings.getBoolean("уведомления",false);
        check_notification=!check_notification;
        editor.putBoolean( "уведомления",check_notification);
        editor.commit();
        if (check_notification){
            button2.setText("Выключить уведомления");
            getActivity().startService(new Intent(getActivity(),MyService.class));
        }
        else {
            button2.setText("Включить уведомления");
            getActivity().stopService(new Intent(getActivity(),MyService.class));
        }
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            if (flag){
                time_from = dateAndTime.getTime().toString();
                hour_from = time_from.split("\\s");
                time_from_on = hour_from[3].split(":");
                System.out.println("время от "+time_from_on[0] + " "+ time_from_on[1]);
                button.setText("От  "+time_from_on[0]+":"+time_from_on[1]);
                editor.putString( "время_от",time_from_on[0] + ":"+ time_from_on[1] );
                editor.putInt( "часы_от",Integer.parseInt(time_from_on[0]));
                editor.putInt( "минуты_от",Integer.parseInt(time_from_on[1]));
                editor.commit();
            }
            else{
                time_up = dateAndTime.getTime().toString();
                hour_up = time_up.split("\\s");
                time_up_on = hour_up[3].split(":");
                System.out.println("время_до "+time_up_on[0] + " "+ time_up_on[1]);
                button1.setText("До  "+time_up_on[0]+":"+time_up_on[1]);
                editor.putString( "время_до",time_up_on[0] + ":"+ time_up_on[1]);
                editor.putInt( "часы_до",Integer.parseInt(time_up_on[0]));
                editor.putInt( "минуты_до",Integer.parseInt(time_up_on[1]));
                editor.commit();
            }
        }
    };
}