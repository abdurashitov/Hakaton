package gafurov.developers_kipu.com.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {

    EditText EngBox;
    EditText  RusBox;
    Button delButton;
    Button saveButton;

    DatabaseHelper_2 sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        EngBox = (EditText) findViewById(R.id.name);
        RusBox = (EditText) findViewById(R.id.year);
        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper_2(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DatabaseHelper_2.TABLE + " where " +
                    DatabaseHelper_2.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            EngBox.setText(userCursor.getString(1));
            RusBox.setText(userCursor.getString(1));
            userCursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper_2.COLUMN_ENG_2, EngBox.getText().toString());
        cv.put(DatabaseHelper_2.COLUMN_RUS_2, RusBox.getText().toString());

        if (userId > 0) {
            db.update(DatabaseHelper_2.TABLE, cv, DatabaseHelper_2.COLUMN_ID + "=" + String.valueOf(userId), null);
        } else {
            db.insert(DatabaseHelper_2.TABLE, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DatabaseHelper_2.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}