package me.rooshi.stayawake;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import android.support.design.widget.BottomNavigationView;


import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static final int[] SEEK_VALUES = new int[]{15000, 30000, 60000, 120000, 180000, 240000, 300000, 600000, 900000, 1200000};
    static final String[] SEEK_VALUES_AS_STRINGS = new String[]{"15 Seconds", "30 Seconds", "1 Minute", "2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes", "10 Minutes", "15 Minutes", "20 Minutes"};

    TextView intervalTextView;
    SeekBar intervalSeekBar;
    TextView intervalLengthTextView;

    TextView numOfQsTextView;
    SeekBar numQsSeekBar;
    TextView numQsTextView;

    Button startButton;
    boolean running;

    Vibrator vibrator;
    Random random;

    int timing;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Timer";
            String description = "Timer after wait time";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("666", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        /*
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        */

        intervalTextView = findViewById(R.id.intervalTextView);
        intervalSeekBar = findViewById(R.id.intervalSeekBar);
        intervalLengthTextView = findViewById(R.id.intervalLengthTextView);

        numOfQsTextView = findViewById(R.id.numOfQsTextView);
        numQsSeekBar = findViewById(R.id.numQuestionsSeekBar);
        numQsTextView = findViewById(R.id.numQuestionsTextView);

        startButton = findViewById(R.id.startButton);
        running = false;

        intervalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intervalLengthTextView.setText(SEEK_VALUES_AS_STRINGS[progress]);
                timing = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        numQsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String tv = (progress+1) + " Questions";
                numQsTextView.setText(tv);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        random = new Random();
    }

    public void onStart(View view) {

        vibrateForMillis(100);

        if (!running) {

            //ack vibrate

            intervalTextView.setVisibility(View.INVISIBLE);
            intervalSeekBar.setVisibility(View.INVISIBLE);
            intervalLengthTextView.setVisibility(View.INVISIBLE);

            numOfQsTextView.setVisibility(View.INVISIBLE);
            numQsSeekBar.setVisibility(View.INVISIBLE);
            numQsTextView.setVisibility(View.INVISIBLE);

            startButton.setText("Stop");

            newTimer();

        } else {

            countDownTimer.cancel();

            intervalTextView.setVisibility(View.VISIBLE);
            intervalSeekBar.setVisibility(View.VISIBLE);
            intervalLengthTextView.setVisibility(View.VISIBLE);

            numOfQsTextView.setVisibility(View.VISIBLE);
            numQsSeekBar.setVisibility(View.VISIBLE);
            numQsTextView.setVisibility(View.VISIBLE);
            startButton.setText("Start");

        }

    running = !running;
    }

    private void newTimer() {
        //get random amount of time to wait
        //Its a set time (short/medium/long) +- 20%
        int randTime;
        int intervalTime = SEEK_VALUES[intervalSeekBar.getProgress()];
        randTime = (int) ((intervalTime - (intervalTime * 0.2)) + random.nextInt((int) (intervalTime * 0.2)));

        //Create new countdowntimer to wait for the times up
        countDownTimer = new CountDownTimer(randTime, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getApplicationContext(), "666")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Wake up!")
                        .setContentText("Click here to solve problems!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);


                Intent nIntent = new Intent(getApplicationContext(), MathActivity.class);
                nIntent.putExtra("numQ", numQsSeekBar.getProgress() + 1);
                nIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, nIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                notiBuilder.setContentIntent(pendingIntent);


                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(1, notiBuilder.build());


                vibrateIndefinitely();


                //Intent mathIntent = new Intent(getApplicationContext(), MathActivity.class);
                //mathIntent.putExtra("numQ", numQsSeekBar.getProgress() + 1);
                startActivityForResult(nIntent, 66);

            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        vibrator.cancel();
        newTimer();

    }

    private void vibrateIndefinitely() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 500, 200},0));
        }
        /* else{
            //deprecated in API 26
            vibrator.vibrate(millis);
        } */
    }

    private void vibrateForMillis(int millis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(millis, 100));
        }
        else{
            //deprecated in API 26
            vibrator.vibrate(millis);
        }

    }

}
