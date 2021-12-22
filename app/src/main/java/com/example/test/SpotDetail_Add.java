package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

public class SpotDetail_Add extends AppCompatActivity {
    public static final int REQUEST_CODE = 303;
    ImageButton ib_photo_add;
    EditText et_content_add;
    Button detail_AddOk;

    private Bitmap bitmapImg;
    String KEY_DETAILADD = "key_detailAdd";

    private String currentUriPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail_add);
        ib_photo_add = findViewById(R.id.ib_photo_add);
        et_content_add = findViewById(R.id.et_content_add);
        detail_AddOk = findViewById(R.id.detail_AddOk);

        ib_photo_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, REQUEST_CODE);
            }
        });

        detail_AddOk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpotDetail_Add.this, SpotDetail.class);
                intent.putExtra("content", et_content_add.getText().toString());
                if (currentUriPath == null) {
                    intent.putExtra("img", "");
                } else {
                    intent.putExtra("img", currentUriPath);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uriPath = data.getData();
                //ib_photo_add.setImageURI(uriPath);
                currentUriPath = uriPath.toString();
                Glide.with(this).load(currentUriPath).into(ib_photo_add);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //비트맵을 비트맵어레이로 변경함.
        String imgString = java.util.Base64.getEncoder().encodeToString(byteArray);
        // 비트맵어레이를 스트링 형태로 변경함
        return imgString;
        // 스트링을 리턴해줌
    }
}