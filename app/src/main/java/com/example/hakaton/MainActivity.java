package com.example.hakaton;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));
        Class fragmentClass = Vocabulary.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().add(R.id.container_fragment, fragment).commit();
    }
    /*обрабатываем нажатие в нижнем меню*/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Class fragmentClass = Vocabulary.class;
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentClass = Vocabulary.class;
                    break;
                case R.id.navigation_dashboard:
                    fragmentClass = Game.class;
                    break;
                case R.id.navigation_setting:
                    fragmentClass = Setting.class;
                    break;
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Вставляем фрагмент, заменяя текущий фрагмент
            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
            setTitle(item.getTitle());
            return true;
        }
    };
}
