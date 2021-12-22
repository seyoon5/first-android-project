package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditSpot extends AppCompatActivity {

    public static final int REQUEST_CODE = 205;
    String NAME_RECORDSPOT = "recordSpot";

    ImageView imageView_editSpot;
    EditText et_editName, et_editAdress;
    RatingBar ratingEdit;
    Button btn_EditR, btn_EditR_Photo;
    private String currentUriPath;
    private Bitmap bitmapImg;

    private ArrayList<SpotItem> itemList;
    String KEY_ITEM = "key_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spot);
        imageView_editSpot = findViewById(R.id.imageView_editSpot);
        et_editName = findViewById(R.id.et_editName);
        et_editAdress = findViewById(R.id.et_editAdress);
        ratingEdit = findViewById(R.id.rate_edit);

        btn_EditR = findViewById(R.id.btn_EditR);
        btn_EditR_Photo = findViewById(R.id.btn_EditR_Photo);

        Intent i = getIntent();
        String img = i.getStringExtra("img");
        String name = i.getStringExtra("name");
        String adress = i.getStringExtra("adress");
        String rate = i.getStringExtra("rate");

        if (!img.equals("")) {
            Glide.with(this).load(Uri.parse(img)).into(imageView_editSpot);
        } else if (img.equals("")) {
            imageView_editSpot.setImageResource(R.drawable.iconfinder_home);
        }
        et_editName.setText(name);
        et_editAdress.setText(adress);
        ratingEdit.setRating(Float.parseFloat(rate));

        btn_EditR_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, REQUEST_CODE);
            }
        });

        btn_EditR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSpot.this, RecordSpot.class);
                String name = et_editName.getText().toString();
                String adress = et_editAdress.getText().toString();
                String rate = String.valueOf(ratingEdit.getRating());
                intent.putExtra("name", name);
                intent.putExtra("adress", adress);
                intent.putExtra("rate", rate);
                if (currentUriPath == null) {
                    intent.putExtra("img", img);
                } else {
                    intent.putExtra("img", currentUriPath);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void getItemShared() {

        SharedPreferences pref = getSharedPreferences(NAME_RECORDSPOT, MODE_PRIVATE);

        try {
            JSONObject wrapObject = new JSONObject(pref.getString(KEY_ITEM, ""));
            //String 으로 들어온 값 JSONObject 로 1차 파싱.
            JSONArray jsonArray = wrapObject.getJSONArray("item");
            for (int i = 0; i < jsonArray.length(); i++) {
                SpotItem items = new SpotItem();
                items.setIv_Rphoto(jsonArray.getJSONObject(i).getString("img"));
                items.setTv_Rname(jsonArray.getJSONObject(i).getString("name"));
                items.setTv_Rspot(jsonArray.getJSONObject(i).getString("adress"));
                items.setTv_Rrate(jsonArray.getJSONObject(i).getString("rate"));
                itemList.add(items);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uriPath;
                uriPath = data.getData();
                //imageView_editSpot.setImageURI(uriPath);
                currentUriPath = uriPath.toString();
                Glide.with(this).load(currentUriPath).into(imageView_editSpot);

            }
        }
    }
}