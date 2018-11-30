package com.example.hakaton;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    NotificationManager nm;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    Cursor userCursor1;
    String word_eng;
    String word_rus;
    Thread myThread;
    ArrayList<String> array_eng = new ArrayList<>();
    ArrayList<String> array_rus = new ArrayList<>();
    public static final String STORAGE_NAME = "StorageName";

    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        dsOpen();
        someTask();
        return START_STICKY;
    }
    void dsOpen(){
        sqlHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();
        db = sqlHelper.open();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        while (userCursor.moveToNext()) {
            String uname = userCursor.getString(userCursor.getColumnIndex("eng"));// тут меняем какую колонку берем
            array_eng.add(uname);
        }
        int z = 0;
        userCursor1 = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        while (userCursor1.moveToNext()) {
            String uname = userCursor1.getString(userCursor1.getColumnIndex("rus"));// тут меняем какую колонку берем
            array_rus.add(uname);
        }
        System.out.println("размер русс" +array_rus.size());
    }

    void someTask() {
        myThread = new Thread( // создаём новый поток
                new Runnable() { // описываем объект Runnable в конструкторе
                    SharedPreferences settings = getApplicationContext().getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    Calendar calendar = GregorianCalendar.getInstance();
                    int n=settings.getInt("переодичность", 10);
                    public void run() {
                        Random random = new Random();
                        int index = random.nextInt(array_eng.size()-2) + 1;
                        word_eng = array_eng.get(index);
                        word_rus = array_rus.get(index);
                        try {
                            TimeUnit.MINUTES.sleep(n);
                            int start_hour= settings.getInt("часы_от", 8);
                            int end_hour=settings.getInt("часы_до", 18);
                            int start_minute=settings.getInt("минуты_от", 30);
                            int end_minute=settings.getInt("минуты_до", 30);
                            int hour=calendar.get(Calendar.HOUR_OF_DAY);
                            int minute=calendar.get(Calendar.MINUTE);
                            int h1=(start_hour*60)+start_minute;
                            int h2=(end_hour*60)+end_minute;
                            int c=(hour*60)+minute;
                            if(h1<h2)
                                if (c > h1 && c < h2)
                                    sendNotif();
                                else
                                    someTask();
                            else
                                if (c>h1 || c<h2)
                                    sendNotif();
                                else
                                    someTask();
                        } catch (InterruptedException e) {
                        }
                    }
                }
        );
        myThread.start();
    }
    void sendNotif() {
        // Собирем уведомление
        Notification.Builder builder = new Notification.Builder(this);
        Intent intent = new Intent(this, Setting.class);
        // intent с сылкой на активити которое запустится по нажатию на уведомление
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        //передаем в уведомление intetn с сылкой на активити
        builder.setContentIntent(pIntent);
        //устанавливаем иконку уведомления
        builder.setSmallIcon(R.drawable.ic_stat_name);
        //установливаем изображение в уведомление
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_icon));
        //устанавливаем текст который види при выводе уведомления
        builder.setContentTitle("Время учить слова");
        builder.setContentText(word_eng+" - "+word_rus);
        // отправляем уведомление
        System.out.println("это анг слово " + word_eng);
        System.out.println("это рус слово " + word_rus);
        nm.notify(1, builder.build());
        someTask();
    }
    @Override
    public void onDestroy() {
        myThread.interrupt();
        nm.cancelAll();
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}