package com.sumit.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private Button pauseButton;

    private TextView timerValue;

    Intent intent;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerValue = (TextView) findViewById(R.id.timer_text_view);

        startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                intent = new Intent(MainActivity.this, TimerService.class);
                startService(intent);
                registerReceiver(broadcastReceiver, new IntentFilter(TimerService.BROADCAST_ACTION));
            }
        });
        pauseButton = (Button) findViewById(R.id.pause);

        pauseButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                unregisterReceiver(broadcastReceiver);
                stopService(intent);
            }
        });

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        int time = intent.getIntExtra("time", 0);

        Log.d("Hello", "Time " + time);

        int mins = time / 60;
        int secs = time % 60;
        timerValue.setText("" + mins + ":"
                + String.format("%02d", secs));
    }

}