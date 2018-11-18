package hakaton.hakaton_brench1;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class Game extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView text;
    Button[] button= new Button[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*********************************************************************************/
        //onResume();
        /****************************************************************************************/
        button[0] = (Button)findViewById(R.id.button1);
        button[1] = (Button)findViewById(R.id.button2);
        button[2] = (Button)findViewById(R.id.button3);
        button[3] = (Button)findViewById(R.id.button4);
        text = (TextView) findViewById(R.id.textView1);
        sqlHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_game) {
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
        } else if (id == R.id.nav_vocabulary) {
            Intent intent = new Intent(this, Vocabulary.class);
            startActivity(intent);
        } else if (id == R.id.nav_vocabulary_user) {
            Intent intent = new Intent(this, Vocabulary_user.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
        }
        return true;
    }
    /*********************************************************************************************/



    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    Cursor userCursor1;
    int count =0;

    //@Override
    public void onResume()
    {
        super. onResume();
        try {
            db = sqlHelper.open();
            //String[] array_eng = new String[]{DatabaseHelper.COLUMN_ENG};
            String[] array_eng=new String[55];
            int y = 0;
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
           while (userCursor.moveToNext()) {
                String uname = userCursor.getString(userCursor.getColumnIndex("eng"));// тут меняем какую колонку берем
                array_eng[y] = uname;
                y++;
            }
           // String[] array_rus = new String[]{DatabaseHelper.COLUMN_RUS};
            String[] array_rus = new String[55];
            int z = 0;
            userCursor1 = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
            while (userCursor1.moveToNext()) {
                String uname = userCursor1.getString(userCursor1.getColumnIndex("rus"));// тут меняем какую колонку берем
                array_rus[z] = uname;
                z++;
            }
            Random random = new Random();
            int index = random.nextInt(55) + 1;
            String word_eng = array_eng[index];
            String word_rus = array_rus[index];
            for (int i = 0; i <array_eng.length ; i++) {
                System.out.println(array_eng[i]);
            }
            for (int i = 0; i <array_eng.length ; i++) {
                System.out.println(array_rus[i]);
            }

            System.out.println(word_eng);
            text.setText(word_eng);
            int btn_index = random.nextInt(4) + 1;
            for (int i = 0; i < 4; i++) {
                button[i].setText(array_rus[random.nextInt(55) + 1]);
                /*button1.setText(array_rus[random.nextInt(55) + 1]);
                button2.setText(array_rus[random.nextInt(55) + 1]);
                button3.setText(array_rus[random.nextInt(55) + 1]);
                button4.setText(array_rus[random.nextInt(55) + 1]);*/
            }
            button[btn_index].setText("" + word_rus);


        }
        catch (SQLException ex){}
    }

    public void Check (String word){
        Intent intent1 = new Intent(this, result_game.class);
        if (word.equals(text.getText())){
            count++;

        }
        if (count==15)
            startActivity(intent1);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button1:
                //text.setText("Первая");
                break;
            case R.id.button2:
                //text.setText("Вторая");
                break;
            case R.id.button3:
                //text.setText("Третья");
                break;
            case R.id.button4:
                //text.setText("Четвертая");
                break;
        }
    }





}






