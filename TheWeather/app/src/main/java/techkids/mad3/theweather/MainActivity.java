package techkids.mad3.theweather;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {
//    public static String NOTIFICATION_ID = "notification-id";
//    public static String NOTIFICATION = "notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, WeatherService.class);
        startService(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

//        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                Notification notification = intent.getParcelableExtra(NOTIFICATION);
//                int id = intent.getIntExtra(NOTIFICATION_ID, 0);
//                notificationManager.notify(id, notification);
//                Log.d("Tao dayyyyyy", "okkkkkkkkkkkkkkkkkkkkk");
//            }
//        };
//
//        registerReceiver(broadcastReceiver, new IntentFilter("FILTER_ALARM_WEATHER"));
    }
}
