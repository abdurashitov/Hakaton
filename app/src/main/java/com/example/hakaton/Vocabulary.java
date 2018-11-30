package com.example.hakaton;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.FilterQueryProvider;

public class Vocabulary extends Fragment {
    EditText userFilter;
    ListView userList;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    Cursor Cursorword;
    SimpleCursorAdapter userAdapter;
    UserActivity yfc = new UserActivity();
    Bundle bundle = new Bundle();
    FragmentManager fragmentManager = getFragmentManager();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putLong("id", 0);
                yfc.setArguments(bundle);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_fragment,  yfc).commit();
            }
        });

        userList = (ListView)view.findViewById(R.id.userList);
        userFilter = (EditText)view.findViewById(R.id.userFilter);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bundle.putLong("id", id);
                yfc.setArguments(bundle);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.container_fragment,  yfc).commit();
            }
        });
        sqlHelper = new DatabaseHelper(getActivity().getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            db = sqlHelper.open();
            String[] headers = new String[]{DatabaseHelper.COLUMN_ENG,DatabaseHelper.COLUMN_RUS};

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);

            userAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2,
                    userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            // если в текстовом поле есть текст, выполняем фильтрацию
            // данная проверка нужна при переходе от одной ориентации экрана к другой
            if(!userFilter.getText().toString().isEmpty())
                userAdapter.getFilter().filter(userFilter.getText().toString());
            // установка слушателя изменения текста
            userFilter.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) { }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                // при изменении текста выполняем фильтрацию
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    userAdapter.getFilter().filter(s.toString());
                }
            });
            // устанавливаем провайдер фильтрации
            userAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence constraint) {

                    if (constraint == null || constraint.length() == 0) {

                        Cursorword =  db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
                    }
                    else {

                        Cursorword = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                                DatabaseHelper.COLUMN_RUS  +" like ?", new String[]{"%" + constraint.toString() + "%"} );

                        Cursorword = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                                DatabaseHelper.COLUMN_ENG  +" like ?", new String[]{"%" + constraint.toString() + "%"} );
                    }
                    return Cursorword;
                }
            });
            userList.setAdapter(userAdapter);
        }
        catch (SQLException ex){}
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
}

