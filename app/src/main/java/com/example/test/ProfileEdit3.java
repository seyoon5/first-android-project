/*
package com.example.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.InputStream;


public class ProfileEdit3 extends AppCompatActivity {

    */
/*private static final int REQUEST_CODE = 0;*//*

    Button button_btn_profileEditCancle;
    Button button_btn_profileEditOk;

    ImageView imageView_profileEdit_userImage;
    Button button_profileEdit_userImageCameraBtn;
    Button button_profileEdit_userImagePhotoBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

       */
/* button_profileEdit_userImagePhotoBtn = findViewById(R.id.button_profileEdit_userImagePhotoBtn);
        imageView_profileEdit_userImage = findViewById(R.id.imageView_profileEdit_userImage);
        button_profileEdit_userImageCameraBtn = findViewById(R.id.button_profileEdit_userImageCameraBtn);

        button_btn_profileEditCancle = (Button) findViewById(R.id.button_btn_profileEditCancle);
        button_btn_profileEditOk = (Button) findViewById(R.id.button_btn_profileEditOk);

        button_btn_profileEditOk.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileEdit3.this, Profile.class);
            startActivity(intent);
        });
        button_btn_profileEditCancle.setOnClickListener(v -> {
            Intent profile = new Intent(ProfileEdit3.this, Profile.class);
            startActivity(profile);
        });


        button_profileEdit_userImagePhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_CODE);
        });*//*

    }

    */
/*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && requestCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

            } catch (Exception e) {

            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
        }
    }*//*

    }
*/
