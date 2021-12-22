    package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Start extends AppCompatActivity {
    private Button button_signUp;
    private Button button_btn_login;
    private TextInputEditText textInputEditText_Id;
    private TextInputEditText textInputEditText_pw;

    private static final String USER_INFO = "user_info";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_ID = "key_id";
    private static final String KEY_PWD = "key_pwd";
    private static final String KEY_USER = "key_user";

    ArrayList<String> idList = new ArrayList();
    ArrayList<String> pwdList = new ArrayList();
    ArrayList<String> nameList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.e("TAG", "내용");
        System.out.println(nameList.size()); // this.getLocalClassName()

        textInputEditText_Id = findViewById(R.id.textInputEditText_id); //실험
        textInputEditText_pw = findViewById(R.id.textInputEditText_pw);

        button_btn_login = findViewById(R.id.button_btn_login);
        button_signUp = findViewById(R.id.button_signUp);

        button_btn_login.setOnClickListener(v -> {
            idList = getStringArrayPref(KEY_ID);
            pwdList = getStringArrayPref(KEY_PWD);
            String userId = textInputEditText_Id.getText().toString();

            SharedPreferences pref = getSharedPreferences(USER_INFO, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(KEY_USER, userId);
            editor.commit();

            String id = textInputEditText_Id.getText().toString();
            String pw = textInputEditText_pw.getText().toString();

            if (idList.contains(id) && pwdList.contains(pw)) {
                Intent intent = new Intent(Start.this, AppMain.class);
//                intent.putExtra("userId", textInputEditText_Id.getText().toString());
                startActivity(intent);

            } else {
                Toast.makeText(this, "사용자 정보를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        button_signUp.setOnClickListener(lv -> {
            Intent intent = new Intent(Start.this, SignUp.class);
            startActivity(intent);
        });

    }

    private ArrayList getStringArrayPref(String key) {
        SharedPreferences prefs = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    @Override
    protected void onPause() {
        super.onPause();
/*
        Intent i = new Intent(Start.this, ThreadLoading.class);
        startActivity(i);*/

        Log.e("onPause", "내용");
        System.out.println(); // this.getLocalClassName()
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop", "내용");
        System.out.println(); // this.getLocalClassName()
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "내용");
        System.out.println(); // this.getLocalClassName()
    }
}
