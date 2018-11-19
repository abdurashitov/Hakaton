package hakaton.hakaton_brench1;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

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
    boolean flag =true;
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        someTask();
        return START_STICKY;
    }
    void dsOpen(){
        sqlHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();
        db = sqlHelper.open();
        String[] array_eng=new String[55];
        int y = 0;
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        while (userCursor.moveToNext()) {
            String uname = userCursor.getString(userCursor.getColumnIndex("eng"));// тут меняем какую колонку берем
            array_eng[y] = uname;
            y++;
        }
        String[] array_rus = new String[55];
        int z = 0;
        userCursor1 = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        while (userCursor1.moveToNext()) {
            String uname = userCursor1.getString(userCursor1.getColumnIndex("rus"));// тут меняем какую колонку берем
            array_rus[z] = uname;
            z++;
        }
        Random random = new Random();
        int index = random.nextInt(54) + 1;
        word_eng = array_eng[index];
        word_rus = array_rus[index];

    }



    void someTask() {
        Thread myThread = new Thread( // создаём новый поток
                new Runnable() { // описываем объект Runnable в конструкторе
                    public void run() {
                        try {
                            TimeUnit.SECONDS.sleep(15);
                            dsOpen();
                            sendNotif();
                        } catch (InterruptedException e) {
                        }
                    }
                }
        );
        myThread.start();
       /* new Thread (new Runnable() {
            public void run() {
               // while (true){
                    try {
                        TimeUnit.SECONDS.sleep(15);
                        dsOpen();
                        sendNotif();
                    } catch (InterruptedException e) {
                   }
                //}
            }
        }).start();*/
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
        builder.setSmallIcon(R.drawable.ic_stat_notification);
//установливаем изображение в уведомление
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
//устанавливаем текст который види при выводе уведомления
        builder.setContentTitle(word_eng+"   "+word_rus);
//
//       builder.setContentText("тут будет что то");
//       builder.setSubText("тут будед еще что-то");
// отправляем уведомление
        System.out.println("это анг слово " + word_eng);
        System.out.println("это рус слово " + word_rus);
        nm.notify(1, builder.build());
        someTask();

    }
    @Override
    public void onDestroy() {

        nm.cancelAll();
        flag=false;
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}