package gafurov.developers_kipu.com.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;

import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SecondFragment extends FragmentActivity {

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    ListView userList;
    EditText userFilter;

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);
        userList = (ListView)view.findViewById(R.id.userList);
        // userText =(TextView)findViewById(R.id.userText);
        userFilter = (EditText)view.findViewById(R.id.userFilter);

        sqlHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            //РАНДОМ

            db = sqlHelper.open();
          /*  String product = "";
            String sql ="SELECT * FROM DB_Slovo ORDER BY RANDOM() LIMIT 1";
            mCurRandom = db.rawQuery(sql, null);
            mCurRandom.moveToFirst();
            while (!mCurRandom.isAfterLast()) {
                product += mCurRandom.getString(1) + " | ";
                mCurRandom.moveToNext();
            }
            mCurRandom.close();

            userText.setText(product);8*/

           /* String product = "";
            Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                product += cursor.getString(1) + " | ";
                cursor.moveToNext();
            }
            cursor.close();

            userText.setText(product);*/

            // String sql ="SELECT * FROM DB_Slovo";
            // userCursor = db.rawQuery(sql, null);
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
            String[] headers = new String[]{DatabaseHelper.COLUMN_ENG, DatabaseHelper.COLUMN_RUS};
            userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
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

                        return db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
                    }
                    else {
                        return db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                                DatabaseHelper.COLUMN_RUS  +" like ?", new String[]{"%" + constraint.toString() + "%"} );
                    }
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

