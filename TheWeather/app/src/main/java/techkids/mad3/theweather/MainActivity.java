package techkids.mad3.theweather;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {
    private Bundle bundleGetData;
    private String tempMain, tempMin, tempMax, tempDescription;

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

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                bundleGetData = intent.getBundleExtra("DataTemp");
                tempMain = bundleGetData.getString("TempMain");
                tempMin = bundleGetData.getString("TempMin");
                tempMax = bundleGetData.getString("TempMax");
                tempDescription = bundleGetData.getString("WeatherDescription");

                // Using RemoteViews to bind custom layouts into Notification
                RemoteViews remoteViews = new RemoteViews(getPackageName(),
                        R.layout.customnotification);

                // Open NotificationView.java Activity
                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                        // Set Icon
                        .setSmallIcon(R.drawable.cloudy)
                        // Set Ticker Message
                        .setTicker(tempDescription + " in Ha Noi, Viet Nam")
                        // Dismiss Notification
                        .setAutoCancel(true)
                        // Set PendingIntent into Notification
                        .setContentIntent(pIntent)
                        // Set RemoteViews into Notification
                        .setContent(remoteViews);

                // Locate and set the Image into customnotificationtext.xml ImageViews
                remoteViews.setImageViewResource(R.id.imgTemp, R.drawable.cloudy);

                // Locate and set the Text into customnotificationtext.xml TextViews
                remoteViews.setTextViewText(R.id.tvDescriptionTemp, tempDescription);
                remoteViews.setTextViewText(R.id.tvMinTemp, "Min: " + tempMin.substring(0, 5)  + " " + (char) 0x00B0 + "C");
                remoteViews.setTextViewText(R.id.tvMaxTemp, "Max: " + tempMax.substring(0, 5) + " " + (char) 0x00B0 + "C");
                remoteViews.setTextViewText(R.id.tvMainTemp, tempMain.substring(0, 5) + (char) 0x00B0 + "C");

                // Create Notification Manager
                NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Build Notification with Notification Manager
                notificationmanager.notify(0, builder.build());
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("FILTER_ALARM"));
    }
}
