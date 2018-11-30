package com.example.hakaton;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Game extends Fragment {
    TextView text;
    Button[] button= new Button[4];
    Button but_ok;
    TextView text_count;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        text_count =(TextView)v.findViewById(R.id.count);
        button[0] = (Button)v.findViewById(R.id.button);
        button[1] = (Button)v.findViewById(R.id.button2);
        button[2] = (Button)v.findViewById(R.id.button3);
        button[3] = (Button)v.findViewById(R.id.button4);
        text = (TextView) v.findViewById(R.id.textView1);
        sqlHelper = new DatabaseHelper(getActivity().getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();
        button[0].setOnClickListener(myButtonClick);
        button[1].setOnClickListener(myButtonClick);
        button[2].setOnClickListener(myButtonClick);
        button[3].setOnClickListener(myButtonClick);
        but_ok=(Button)v.findViewById(R.id.button5);
        but_ok.setOnClickListener(myButtonClick);
        word();
        return v;
    }
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    Cursor userCursor1;
    int count =0;
    int check;
    int right;
    ArrayList<String> array_eng = new ArrayList<>();
    ArrayList<String> array_rus = new ArrayList<>();
    public void word()
    {
        //берем слова из бд
        db = sqlHelper.open();
        int y = 0;
        //заполнякм масив английских слов

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        while (userCursor.moveToNext()) {
            String uname = userCursor.getString(userCursor.getColumnIndex("eng"));
            array_eng.add(uname);
            y++;
        }
        //заполняем массив русских слов

        int z = 0;
        userCursor1 = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        while (userCursor1.moveToNext()) {
            String uname = userCursor1.getString(userCursor1.getColumnIndex("rus"));
            array_rus.add(uname);
            z++;
        }
        //выбираем рандомное слова для игры
        Random random = new Random();
        int index = random.nextInt(array_eng.size()-2) + 1;
        String word_eng = array_eng.get(index);
        String word_rus = array_rus.get(index);
        text.setText(word_eng);
        int btn_index = random.nextInt(3) + 1;
        // выбираем рандомные слова для вариантов ответа
        int key;
        int [] mas = {-1, -1, -1, -1};
        for (int i = 0; i < 4; i++) {
            key = random.nextInt(array_eng.size()-2) + 1;                                       //Размер массива забивается
            if (key != mas[0] && key != mas[1] && key != mas[2] && key != mas[3] && key !=index )
            {
                mas[i]=key;
                button[i].setText(array_rus.get(key));
            }
            else i--;
        }
        check=btn_index;
        button[btn_index].setText(word_rus);
        but_ok.setEnabled(false);
    }
    int pos=0;
    //обрабатываем нажатия
    View.OnClickListener myButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button:
                    pos=0;
                    Check();
                    break;
                case R.id.button2:
                    pos=1;
                    Check();
                    break;
                case R.id.button3:
                    pos=2;
                    Check();
                    break;
                case R.id.button4:
                    pos=3;
                    Check();
                    break;
                case R.id.button5:
                    begin();
                    break;
            }
        }
    };
    //показываем правильный и неправильный вариант
    public void Check (){
        button[0].setEnabled(false);
        button[1].setEnabled(false);
        button[2].setEnabled(false);
        button[3].setEnabled(false);
        button[pos].setBackgroundResource(R.drawable.wrong);
        button[check].setBackgroundResource(R.drawable.right);
        but_ok.setEnabled(true);
    }
    //перезаписываем
    public  void begin (){
        button[0].setEnabled(true);
        button[1].setEnabled(true);
        button[2].setEnabled(true);
        button[3].setEnabled(true);
        button[pos].setBackgroundResource(R.drawable.button_styles);
        button[check].setBackgroundResource(R.drawable.button_styles);
        if (pos==check)
            right++;
        count++;
        text_count.setText(right+"/"+count);

        word();
    }



}

