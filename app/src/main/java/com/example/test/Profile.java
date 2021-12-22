package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Profile extends AppCompatActivity {

    public static final int REQUEST_CODE = 111;
    private Button button_btn_profileEdit;
    private ImageView iv_userPhoto;
    private Button getButton_btn_profileEditOk;
    private TextView userProfileId;
    String KEY_IMAGE = "key_image";
    private static final String USER_INFO = "user_info";
    private static final String KEY_USER = "key_user";
    String imgName = "img.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iv_userPhoto = findViewById(R.id.imageView_userPhoto);
        Log.e("Glide", "내용 : "+getSavedUriPath(KEY_IMAGE) );

        userProfileId = findViewById(R.id.userProfileId);
        SharedPreferences pref = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String id = pref.getString(KEY_USER,"");
        userProfileId.setText(id);
        button_btn_profileEdit = findViewById(R.id.button_btn_profileEdit);
        getButton_btn_profileEditOk = findViewById(R.id.button_btn_profileEditOk);
        getButton_btn_profileEditOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, AppMain.class);
                startActivity(intent);
            }
        });
        button_btn_profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileEdit = new Intent(Profile.this, ProfileEdit.class);
                profileEdit.putExtra("key", KEY_IMAGE);
                startActivity(profileEdit);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Profile", "onResume");
        Glide.with(this).load(Uri.parse(getSavedUriPath(KEY_IMAGE))).into(iv_userPhoto);
    }

    public String getSavedUriPath(String key) {
        SharedPreferences pref = getSharedPreferences("shared", MODE_PRIVATE);
        String str = pref.getString(key, "");
        return str;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Profile", "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Profile", "onDestroy");
        finish();
     /*   Intent intent = new Intent(Profile.this, AppMain.class);
        startActivity(intent);*/
    }
}
