package me.rooshi.stayawake;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final int shortTiming = 20000;
    final int mediumTiming = 45000;
    final int longTiming = 90000;

    TextView mainTextView;
    Button startButton;
    SeekBar intervalSeekBar;
    TextView intervalTextView;

    Vibrator vibrator;
    Random random;

    int timing;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTextView = findViewById(R.id.mainTextView);
        startButton = findViewById(R.id.startButton);
        intervalSeekBar = findViewById(R.id.intervalSeekBar);
        intervalTextView = findViewById(R.id.intervalLengthTextView);

        intervalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        intervalTextView.setText("Short");
                        break;
                    case 1:
                        intervalTextView.setText("Medium");
                        break;
                    case 2:
                        intervalTextView.setText("Long");
                        break;
                }
                timing = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        random = new Random();
    }

    public void onStart(View view) {
        vibrate(50);

        int randTime;
        int intervalTime;
        switch (timing) {
            case 0:
                intervalTime = shortTiming;
                break;
            case 1:
                intervalTime = mediumTiming;
                break;
            default:
                intervalTime = longTiming;
        }
        randTime = (int)((intervalTime - (intervalTime*0.2)) + random.nextInt((int)(intervalTime*0.2)));
        countDownTimer = new CountDownTimer() {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                
            }
        }


    }

    private void vibrate(int millis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(millis,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            vibrator.vibrate(millis);
        }
    }

}
