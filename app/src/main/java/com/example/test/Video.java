package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class
Video extends AppCompatActivity {
    EditText et_search;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        btn = findViewById(R.id.btn_ok_video);
        et_search = findViewById(R.id.search_video);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Video.this, VideoPlay.class);
                intent.putExtra("keyword", et_search.getText().toString());
                startActivity(intent);
            }
        });

    }
}