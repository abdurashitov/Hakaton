package com.example.hakaton;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class UserActivity extends Fragment {
    EditText ENG;
    EditText RUS;
    Button delButton;
    Button saveButton;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;
    ArrayList<String> array_eng = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        ENG = (EditText) v.findViewById(R.id.eng);
        RUS = (EditText) v.findViewById(R.id.rus);
        delButton = (Button) v.findViewById(R.id.deleteButton);
        saveButton = (Button) v.findViewById(R.id.saveButton);
        sqlHelper = new DatabaseHelper(getActivity());
        db = sqlHelper.open();

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " +DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            ENG.setText(userCursor.getString(1));
            RUS.setText(userCursor.getString(2));
            userCursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
        if (userId < 500 && userId > 0){
            delButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            RUS.setEnabled(false);
            ENG.setEnabled(false);
            RUS.setTextColor(getResources().getColor(R.color.black));
            ENG.setTextColor(getResources().getColor(R.color.black));
        }
        saveButton.setOnClickListener(myButtonClick);
        delButton.setOnClickListener(myButtonClick);

        dsOpen();
        return v;

    }

    View.OnClickListener myButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.saveButton) {
                if (array_eng.contains(ENG.getText().toString())) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Такое слово уже существует", Toast.LENGTH_SHORT);
                    toast.show();
                } else
                    save();
            } else
                delete();

        }
    };

    void dsOpen(){
        sqlHelper = new DatabaseHelper(getActivity().getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();
        db = sqlHelper.open();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        while (userCursor.moveToNext()) {
            String uname = userCursor.getString(userCursor.getColumnIndex("eng"));// тут меняем какую колонку берем
            array_eng.add(uname);
        }
    }

    public void save(){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ENG, ENG.getText().toString());
        cv.put(DatabaseHelper.COLUMN_RUS, RUS.getText().toString());
        if (userId > 0) {
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
        } else {
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        goHome();
    }
    public void delete(){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        Vocabulary yfc = new Vocabulary();
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_fragment,  yfc).commit();
    }


}
