package de.dominikusdermann.keepbusy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarNotifiactions;
    private TextView amountNotifications;
    private SeekBar seekBarTime;
    private TextView textViewTime;
    private TextView headline;
    private Button buttonNotifications;
    private int notifcatication_progress;
    private int time_progress;
    private ConstraintLayout constraintLayout;
    public Context main_context = this;
    public String CHANNEL_ID = "keepBusy";
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize views
        seekBarNotifiactions = (SeekBar) findViewById(R.id.seekBarNotifications);
        amountNotifications = (TextView) findViewById(R.id.textViewAmountNotifications);
        headline = (TextView) findViewById(R.id.headline);
        buttonNotifications = (Button) findViewById(R.id.buttonNotifications);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutMain);
        seekBarTime = (SeekBar) findViewById(R.id.seekBarTime);
        textViewTime = (TextView) findViewById(R.id.textViewTime);


        // initialize variables
        notifcatication_progress = seekBarNotifiactions.getProgress() * 10;
        time_progress = seekBarTime.getProgress();

        // build notification channel
        createNotificationChannel();
        notificationManager = NotificationManagerCompat.from(this);

        // set and control seekbar for amount of notifications
        amountNotifications.setText("Amount of notifications per minute: " + seekBarNotifiactions.getProgress() *10 + " / " + seekBarNotifiactions.getMax() *10);
        seekBarNotifiactions.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                notifcatication_progress = i * 10;
                amountNotifications.setText("Amount of notifications per minute: " + notifcatication_progress + " / " + seekBarNotifiactions.getMax() * 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                amountNotifications.setText("Amount of notifications per minute: " + notifcatication_progress + " / " + seekBarNotifiactions.getMax() * 10);
            }
        });

        // set and control seekbar for time
        textViewTime.setText("Time during which notificaitons are send: " + seekBarTime.getProgress() + " minutes.");
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                time_progress = i;
                textViewTime.setText("Time during which notificaitons are send: " + seekBarTime.getProgress() + " minutes.");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewTime.setText("Time during which notificaitons are send: " + seekBarTime.getProgress() + " minutes.");
            }
        });


        buttonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNotifications();
            }
        });

    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "fake_notificatoins";
            String description = "this channel is for your keepBusy app.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotifications() {
        // notify user of action
        Snackbar snackbar = Snackbar.make(constraintLayout, "You triggered " +  notifcatication_progress + " notifications in the next " + time_progress + " minute(s).", Snackbar.LENGTH_LONG);
        snackbar.show();
        headline.setText("Busy acting in progress...");

        // calculate distance between notifications
        final long delay_seconds = (time_progress * 60) / notifcatication_progress;

        // perform notifications according to specification
        final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("You have new messages.");

        for(int i = 0; i < notifcatication_progress; i++){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NotificationCompat.Builder n_builder = new NotificationCompat.Builder(main_context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.messages)
                            .setColorized(true)
                            .setContentTitle("New message from:")
                            .setContentText("John.")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    inboxStyle.addLine("New message from John.");
                    n_builder.setStyle(inboxStyle);
                    notificationManager.notify(1, n_builder.build());
                    handler.postDelayed(this, delay_seconds);
                }
            }, delay_seconds);
        }
        // done; reset headline
        headline.setText("Act real busy");
    }



}
