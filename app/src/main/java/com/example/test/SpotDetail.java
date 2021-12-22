package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpotDetail extends AppCompatActivity {

    private static final int REQUEST_CODE = 10;
    private static final int REQUEST_CODE1 = 11;
    Button btn_add_detail;
    Button btn_modify_detail;
    Button btn_delete_detail;
    ImageView imageView;
    RecyclerView recyclerView;
    DetailAdapter adapter;
    ArrayList<DetailItem> itemList;
    LinearLayoutManager mLayoutManager;

    String NAME_DETAIL = "name_detail";
    String KEY_DETAILITEM;

    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<String> contentList = new ArrayList<>();

    private String currentUriPath;

    private Bitmap bitmapedit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_spot_detail);
        Log.e("SpotDetail", "onCreate");

        btn_add_detail = findViewById(R.id.btn_add_detail);
        btn_modify_detail = findViewById(R.id.btn_modify_detail);
        btn_delete_detail = findViewById(R.id.btn_delete_detail);
        imageView = findViewById(R.id.iv_photo);

        recyclerView = findViewById(R.id.recyclerView_detail);
        mLayoutManager = new LinearLayoutManager(SpotDetail.this);
        recyclerView.setLayoutManager(mLayoutManager);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("가게 정보");

        itemList = new ArrayList<>();
        adapter = new DetailAdapter(itemList);
        recyclerView.setAdapter(adapter);


        Intent intentKey = getIntent();
        int key = intentKey.getIntExtra("key", 0);
        KEY_DETAILITEM = String.valueOf(key);

        SharedPreferences pref = getSharedPreferences(NAME_DETAIL, MODE_PRIVATE);
        try {
            JSONObject wrapObj = new JSONObject(pref.getString(KEY_DETAILITEM, ""));
            JSONArray jsonArray = wrapObj.getJSONArray("item");
            for (int i = 0; i < jsonArray.length(); i++) {
                DetailItem items = new DetailItem();
                items.setIv_photo(jsonArray.getJSONObject(i).getString("img"));
                items.setEt_content(jsonArray.getJSONObject(i).getString("content"));
                itemList.add(items);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged(   );

        addBtn();
        editBtn();
        deleteBtn();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String content = data.getStringExtra("content");
                currentUriPath = data.getStringExtra("img");

                itemList.add(new DetailItem(currentUriPath, content));
                adapter.notifyDataSetChanged();
            }
        }
        if (requestCode == REQUEST_CODE1) {
            if (resultCode == RESULT_OK) {
                final DetailItem recycleItem = adapter.getSelected();
                String img = data.getStringExtra("img");
                currentUriPath = data.getStringExtra("content");

                recycleItem.setIv_photo(img);
                recycleItem.setEt_content(currentUriPath);
                //선택항목 초기화
                adapter.clearSelected();
                //리스트 반영
                adapter.notifyDataSetChanged();
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap stringToBitmap(String str) {
        byte[] decode = java.util.Base64.getDecoder().decode(str);
        //비트맵이 비트맵어레이로 변경됨
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        //비트맵어레이를 비트맵으로 바꿔줌
        return bitmap;
        //리트맵을 리턴
    }


    public void addBtn() {
        btn_add_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpotDetail.this, SpotDetail_Add.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public void editBtn() {
        btn_modify_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DetailItem recycleItem = adapter.getSelected();
                if (recycleItem == null) {
                    Toast.makeText(SpotDetail.this, R.string.err_no_selected_item, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SpotDetail.this, SpotDetail_Edit.class);
                startActivityForResult(intent, REQUEST_CODE1);
            }
        });
    }


    public void deleteBtn() {
        btn_delete_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DetailItem recyclerItem = adapter.getSelected();
                if (recyclerItem == null) {
                    Toast.makeText(SpotDetail.this, "항목을 선택해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                itemList.remove(recyclerItem);
                adapter.clearSelected();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("SpotDetail", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("SpotDetail", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("SpotDetail", "onPause");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences pref = getSharedPreferences(NAME_DETAIL, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (itemList.size() > 0) {
            editor.remove(KEY_DETAILITEM);
            editor.apply();
        }
        try {
            JSONObject wrapObj = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < itemList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("img", itemList.get(i).getIv_photo());
                jsonObject.put("content", itemList.get(i).getEt_content());
                jsonArray.put(jsonObject);
            }
            Log.e("TAG", "내용 :jsonArray " + jsonArray);
            wrapObj.put("item", jsonArray);
            String str = wrapObj.toString();
            editor.putString(KEY_DETAILITEM, str);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SpotDetail", "onDestroy");
    }
}