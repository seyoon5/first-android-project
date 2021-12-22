/*
package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.MacAddress;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class ProfileEdit2 extends AppCompatActivity {
    //
    private static final int REQUEST_IMAGE_1 = 1;
    private String imagePath1 = "";
    //

    private static final int REQUEST_CODE = 0;

    final private String TAG = "bomi";

    final static int TAKE_PICTURE = 1;

    Button button_btn_profileEditCancle;
    Button button_btn_profileEditOk;

    ImageView imageView_profileEdit_userImage;
    Button button_profileEdit_userImageCameraBtn;
    Button button_profileEdit_userImagePhotoBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        imageView_profileEdit_userImage = findViewById(R.id.imageView_profileEdit_userImage);
        button_profileEdit_userImageCameraBtn = findViewById(R.id.button_profileEdit_userImageCameraBtn);

        button_profileEdit_userImagePhotoBtn = findViewById(R.id.button_profileEdit_userImagePhotoBtn);
        imageView_profileEdit_userImage = findViewById(R.id.imageView_profileEdit_userImage);
        button_profileEdit_userImageCameraBtn = findViewById(R.id.button_profileEdit_userImageCameraBtn);

        button_btn_profileEditCancle = (Button) findViewById(R.id.button_btn_profileEditCancle);
        button_btn_profileEditOk = (Button) findViewById(R.id.button_btn_profileEditOk);

        button_btn_profileEditOk.setOnClickListener(v -> {

            Intent intent = new Intent(ProfileEdit2.this, Profile.class);
            startActivity(intent);
        });
        button_btn_profileEditCancle.setOnClickListener(v -> {
            Intent profile = new Intent(ProfileEdit2.this, Profile.class);
            startActivity(profile);
        });

        //사진촬영 버튼 클릭하면 아래의 과정들을 거쳐 유저사진이 설정됨.
        button_profileEdit_userImagePhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_CODE);
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(ProfileEdit2.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        button_profileEdit_userImageCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            //카메라 버튼 클릭해서 사진촬영
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_profileEdit_userImageCameraBtn:
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
    }




    @Override //권한요청
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionResult");
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Permission : "+permissions[0]+"was "+grantResults[0]);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE && requestCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(intent.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();


            } catch (Exception e) {

            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
        }
        //사진 파일 가져오기
        switch(requestCode){
            case TAKE_PICTURE:if(resultCode == RESULT_OK && intent.hasExtra("data")){
                Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                if(bitmap != null){
                    imageView_profileEdit_userImage.setImageBitmap(bitmap);
                }
            }
                break;
        }
    }


}*/
