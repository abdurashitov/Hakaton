package hakaton.hakaton_brench1;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.concurrent.TimeUnit;


public class MyService extends Service {
    NotificationManager nm;
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        sendNotif();
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        someTask();
        return START_STICKY;
    }
    void someTask() {
        new Thread(new Runnable() {
            public void run() {
                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(15);
                        sendNotif();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
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
        builder.setContentTitle("тут будет слово с переводом");
//
//       builder.setContentText("тут будет что то");
//       builder.setSubText("тут будед еще что-то");
// отправляем уведомление
        nm.notify(1, builder.build());

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}