package com.example.androidnc_b1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    private static final String[] keyThreads = {"thread1", "thread2", "thread3"};
    Button bt1, bt2, bt3;
    TextView tv1, tv2, tv3;
    boolean isStop1=true, isStop2=true, isStop3=true;
    // start sau 2 giây sau khi màn hình hiện ra
    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            bt1.setEnabled(true);
            bt2.setEnabled(true);
            bt3.setEnabled(true);
            isStop1 = toggle(isStop1,bt1);
            isStop2 = toggle(isStop2,bt2);
            isStop3 = toggle(isStop3,bt3);
            thread1.start();
            thread2.start();
            thread3.start();
        }, 2000);
    }
    // xử lý button
    private boolean toggle(boolean isStop,Button button){
        if (!isStop) {
            button.setText(R.string.start);
        } else {
            button.setText(R.string.stop);
        }
        return !isStop;
    }
    // xử lý logic của các thread
    static void logic(Handler handler, String key, int value, int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Message msg = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt(key, value);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            tv1.setText(String.valueOf(msg.getData().getInt(keyThreads[0])));
        }
    };

    Thread thread1 = new Thread(() -> {
        while (true) {
            if (!isStop1) {
                logic(handler1, keyThreads[0], (new Random().nextInt(51) + 50), 1000);
            }
        }
    });

    @SuppressLint("HandlerLeak")
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            tv2.setText(String.valueOf(msg.getData().getInt(keyThreads[1])));
        }
    };

    Thread thread2 = new Thread(() -> {
        int start = 1;
        while (true) {
            if (!isStop2) {
                logic(handler2, keyThreads[1], 2 * (start++) + 1, 2500);
            }
        }
    });

    @SuppressLint("HandlerLeak")
    Handler handler3 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            tv3.setText(String.valueOf(msg.getData().getInt(keyThreads[2])));
        }
    };

    Thread thread3 = new Thread(() -> {
        int start = 1;
        while (true) {
            if (!isStop3) {
                logic(handler3, keyThreads[2], start++, 2000);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bindingView();
        setListener();
    }

    private void bindingView() {
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
    }

    private void setListener() {
        bt1.setOnClickListener(view -> isStop1 = toggle(isStop1,bt1));
        bt2.setOnClickListener(view -> isStop2 = toggle(isStop2,bt2));
        bt3.setOnClickListener(view -> isStop3 = toggle(isStop3,bt3));
    }
}