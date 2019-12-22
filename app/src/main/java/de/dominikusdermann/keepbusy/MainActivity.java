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
    private ConstraintLayout constraintLayout;
    public Context main_context = this;
    public String CHANNEL_ID = "keepBusy";


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

        // build notification channel
        createNotificationChannel();
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

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



        buttonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(constraintLayout, "You triggered " +  notifcatication_progress + " notifications in the next minute.", Snackbar.LENGTH_LONG);
                snackbar.show();
                headline.setText("Busy acting in progress...");
                NotificationCompat.Builder n_builder = new NotificationCompat.Builder(main_context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.messages)
                        .setColorized(true)
                        .setContentTitle("Success.")
                        .setContentText("This worked so well!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(1, n_builder.build());
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

}
