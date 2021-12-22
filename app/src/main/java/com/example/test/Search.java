package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Search extends AppCompatActivity {
    Button btn_go;
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_search = findViewById(R.id.et_search);

        btn_go = findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = et_search.getText().toString();
                Intent intent = new Intent(Search.this, SearchHome.class);
                intent.putExtra("keyword",keyword);
                startActivity(intent);
            }
        });

    }


}
