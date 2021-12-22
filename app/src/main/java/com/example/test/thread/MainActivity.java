package com.example.test.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
///////2

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ValueHandler handler = new ValueHandler();
    int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView = findViewById(R.id.backthreas_text);
        Button button = findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundThread thread = new BackgroundThread();
                thread.start();
            }
        });

    }

    class BackgroundThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                num++;
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("num", num);
                message.setData(bundle);
                handler.sendMessage(message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int value = bundle.getInt("num");
            textView.setText("asd"+value);
        }
    }
}

    /*    textView = findViewById(R.id.hi);

        mHandler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (i == 1) {
                    textView.setText("미ㅏㅓ");
                    i = 0;
                } else {
                    textView.setText("28723");
                    i = 1;
                }
            }

        };


        class NewRunnable implements Runnable {

            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(1000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.post(runnable);
                }
            }
        }

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();

    }
}*/