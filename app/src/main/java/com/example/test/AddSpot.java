package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddSpot extends AppCompatActivity {

    private int key = 0;

    public static final int REQUEST_CODE = 202;
    private ArrayList<SpotItem> itemList;
    private RecordSpotAdapter adapter;
    String KEY_ITEM = "key_item";

    private Button btn_insertR;
    private Button btn_insertPhoto;
    private ImageView imageView = null;
    private EditText et_Rname;
    private EditText et_Adress;
    private RatingBar Rrate;  //수정
    String currentUriPath;

    private static final String NAME_RECORDSPOT = "recordSpot";




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);
        itemList = new ArrayList<>();
        adapter = new RecordSpotAdapter(itemList);

        Intent appmain = getIntent();
        key = appmain.getIntExtra("Appmain", 0);
        String title = appmain.getStringExtra("title");
        String snippet = appmain.getStringExtra("snippet");

        imageView = findViewById(R.id.imageView_addSpot);
        et_Rname = findViewById(R.id.textRname);
        et_Adress = findViewById(R.id.textAdress);
        if (key == 1) {
            et_Rname.setText(title);
            et_Adress.setText(snippet);
        }
        Rrate = findViewById(R.id.rate_add);  //수정

        /*Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);//MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        gallery.setType("image/*");

        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pick.setType("image/*");

        Intent chooser = Intent.createChooser(getIntent(), "Select Image");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pick});
        startActivityForResult(chooser, REQUEST_CODE);*/

        btn_insertPhoto = findViewById(R.id.btn_insertPhoto);
        btn_insertPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, REQUEST_CODE);

            }
        });
        btn_insertR = findViewById(R.id.btn_insertR);
        btn_insertR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key == 1) {
                    getItemShared();

                    String name = et_Rname.getText().toString();
                    String adress = et_Adress.getText().toString();
                    String rate = String.valueOf(Rrate.getRating()); //수정
                    if (currentUriPath == null) {
                        currentUriPath = "";
                    }
                    SpotItem item = new SpotItem(currentUriPath, name, adress, rate);
                    itemList.add(item);

                    setItemShared();

                    key = 0;
                    Intent intent = new Intent(AddSpot.this, AppMain.class);
                    startActivity(intent);
                }
                if (currentUriPath == null) {
                    currentUriPath = "";
                }
                String name = et_Rname.getText().toString();
                String adress = et_Adress.getText().toString();
                String rate = String.valueOf(Rrate.getRating());  //수정

                Intent intent = new Intent(AddSpot.this, RecordSpot.class);
                intent.putExtra("img", currentUriPath);
                intent.putExtra("name", name);
                intent.putExtra("adress", adress);
                intent.putExtra("rate", rate);
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

    public void setItemShared() {

        SharedPreferences pref = getSharedPreferences(NAME_RECORDSPOT, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        try {
            JSONObject wrapObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < itemList.size(); i++) {
                JSONObject jsonObject = new JSONObject(); // {name : value, name : value 형태로 중괄호 안에 여러개 저장}
                jsonObject.put("img", itemList.get(i).getIv_Rphoto());
                jsonObject.put("name", itemList.get(i).getTv_Rname());
                jsonObject.put("adress", itemList.get(i).getTv_Rspot());
                jsonObject.put("rate", itemList.get(i).getTv_Rrate());
                jsonArray.put(jsonObject); // [{ name : value, name : value }]
            }
            wrapObject.put("item", jsonArray); // {item : [{name1 : value, name2 : value}] }
            String str = wrapObject.toString();
            editor.putString(KEY_ITEM, str);
            editor.commit();
            Log.e("item", "내용 : " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setStringArrayPref(String key, ArrayList<String> values) {
        SharedPreferences prefs = getSharedPreferences(NAME_RECORDSPOT, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();
    }

    private ArrayList getStringArrayPref(String key) {
        SharedPreferences prefs = getSharedPreferences(NAME_RECORDSPOT, MODE_PRIVATE);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uriPath = data.getData();
        currentUriPath = uriPath.toString();

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(currentUriPath).into(imageView);
                //imageView.setImageURI(uriPath);
                Log.e("onActivityResult", "currentUriPath");
                System.out.println(currentUriPath); // this.getLocalClassName()
               /* ContentResolver resolver = getContentResolver();
                try {
                    InputStream inputStream = resolver.openInputStream(uriPath);
                    bitmapImg = BitmapFactory.decodeStream(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        }
    }
}
