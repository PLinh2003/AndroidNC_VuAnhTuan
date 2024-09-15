package com.example.androidnc_b1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity3 extends AppCompatActivity {
    TimePicker timePicker;
    NumberPicker hour, minute, second;
    Button btnTimer, btnAlarm, btnStopTimer, btnStopAlarm;
    Vibrator vibrator;
    Timer timer, timer1;
    Handler handlerTimer = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            btnStopTimer.setEnabled(false);
            btnTimer.setEnabled(true);
            hasTimer = false;
        }
    };
    Handler handlerAlarm = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            btnStopAlarm.setEnabled(false);
            btnAlarm.setEnabled(true);
            timePicker.setEnabled(true);
            hasAlarm = false;
        }
    };
    boolean hasAlarm, hasTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        bindingView();
        setPropertyViews();
        setListener();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    }

    void setListener() {
        btnAlarm.setOnClickListener(v -> {
            if (!hasAlarm) {
                long h = timePicker.getHour();
                long m = timePicker.getMinute();
                Calendar currentTime = Calendar.getInstance();
                long ms = 1000 * (60 * (h * 60 + m)) -
                        (1000 * (60 * (60 * currentTime.get(Calendar.HOUR_OF_DAY)
                                + currentTime.get(Calendar.MINUTE))
                                + currentTime.get(Calendar.SECOND))
                                + currentTime.get(Calendar.MILLISECOND));
                if (ms > 0) {
                    btnAlarm.setEnabled(false);
                    btnStopAlarm.setEnabled(true);
                    timePicker.setEnabled(false);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            vibrator.vibrate(2000);
                            handlerAlarm.sendMessage(new Message());
                        }
                    }, ms);
                    hasAlarm = true;
                }
                Toast.makeText(MainActivity3.this, "" + ms, Toast.LENGTH_LONG).show();
            }
        });
        btnStopAlarm.setOnClickListener(v -> {
            if (hasAlarm) {
                timer.cancel();
                btnStopAlarm.setEnabled(false);
                btnAlarm.setEnabled(true);
                timePicker.setEnabled(true);
            }
            hasAlarm = false;
        });
        btnTimer.setOnClickListener(v -> {
            if (!hasTimer) {
                long h = hour.getValue();
                long m = minute.getValue();
                long s = second.getValue();
                long ms = 1000 * (60 * (h * 60 + m) + s);
                if (ms > 0) {
                    btnTimer.setEnabled(false);
                    btnStopTimer.setEnabled(true);
                    timer1 = new Timer();
                    timer1.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            vibrator.vibrate(2000);
                            handlerTimer.sendMessage(new Message());
                        }
                    }, ms);
                    hasTimer = true;
                }
            }
        });
        btnStopTimer.setOnClickListener(v -> {
            if (hasTimer) {
                timer1.cancel();
                btnStopTimer.setEnabled(false);
                btnTimer.setEnabled(true);
            }
            hasTimer = false;
        });
    }

    void setPropertyViews() {
        timePicker.setIs24HourView(true);
        hour.setMinValue(0);
        hour.setMaxValue(23);
        minute.setMinValue(0);
        minute.setMaxValue(59);
        second.setMinValue(0);
        second.setMaxValue(59);
    }

    void bindingView() {
        timePicker = findViewById(R.id.timePicker);
        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);
        second = findViewById(R.id.second);
        btnAlarm = findViewById(R.id.btnAlarm);
        btnTimer = findViewById(R.id.btnTimer);
        btnStopAlarm = findViewById(R.id.stopAlarm);
        btnStopTimer = findViewById(R.id.stopTimer);
    }
}