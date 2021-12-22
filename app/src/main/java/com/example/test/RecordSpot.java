package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecordSpot extends AppCompatActivity {

    public static final int RESULT_OK = -1;
    public static final int REQUEST_CODE = 101;
    public static final int REQUEST_CODE1 = 100;

    String NAME_RECORDSPOT = "recordSpot";
    String NAME_DETAIL = "name_detail";
    String KEY_DETAILITEM;
    String KEY_ITEM = "key_item";


    private Button btn_insert;
    private Button btn_modify;
    private Button btn_delete;
    private RecyclerView recyclerView;

    private Context mContext;
    private RecordSpotAdapter adapter;
    private ArrayList<SpotItem> itemList;
    private LinearLayoutManager linearLayoutManager;

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> adressList = new ArrayList<>();
    ArrayList<String> rateList = new ArrayList<>();
    ArrayList<String> imgList = new ArrayList<>();

    ImageView iv_photo;
    String content_item;
    String currentUriPath;
    Bitmap bitmapImg;

    private static final String SETTING_ITEMLIST = "setting_itemList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_adapter_record_spot);
        Log.e("TAG", "onCreate");
        recyclerView = findViewById(R.id.recyclerView_spot);
        linearLayoutManager = new LinearLayoutManager(RecordSpot.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("추억의 장소들");

        itemList = new ArrayList<>();
        adapter = new RecordSpotAdapter(itemList);
        recyclerView.setAdapter(adapter);

        getItemShared();
        adapter.notifyDataSetChanged();


        // 추가
        bindRecord();
        // 수정
        bindModify();
        // 삭제
        bindDelete();
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
    protected void onStart() {
        super.onStart();
        Log.e("TAG", "onStart : ");
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
        Log.e("TAG", "onResumeS");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("TAG", "onPause");
        System.out.println(this.getLocalClassName()); // this.getLocalClassName()
    }

    private void setStringArrayPref(String key, ArrayList<SpotItem> values) {
        SharedPreferences prefs = getSharedPreferences(NAME_RECORDSPOT, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
            a.put(values.get(i).getIv_Rphoto());
            a.put(values.get(i).getTv_Rname());

        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TAG", "onStop itemList.size()); : " + itemList.size());

        SharedPreferences pref = getSharedPreferences(NAME_RECORDSPOT, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (itemList.size() > 0) {
            editor.clear();
            editor.apply();
        }

        try {
            JSONObject wrapObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < itemList.size(); i++) {
                JSONObject jsonObject = new JSONObject(); // {name : value, name : value 형태로 중괄호 안에 여러개 저장}
                if (itemList.get(i).getIv_Rphoto() != null) {
                    jsonObject.put("img", itemList.get(i).getIv_Rphoto());
                } else if (itemList.get(i).getIv_Rphoto().equals("")) {
                    jsonObject.put("img", "");
                }
                jsonObject.put("name", itemList.get(i).getTv_Rname());
                jsonObject.put("adress", itemList.get(i).getTv_Rspot());
                jsonObject.put("rate", itemList.get(i).getTv_Rrate());
                jsonArray.put(jsonObject); // [{ name : value, name : value }]
            }
            wrapObject.put("item", jsonArray); // {item : [{name1 : value, name2 : value}] }
            String str = wrapObject.toString();
            editor.putString(KEY_ITEM, str);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setJsonArrayPref(String key, ArrayList<String> value) {
        SharedPreferences pref = getSharedPreferences(NAME_RECORDSPOT, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < value.size(); i++) {
            a.put(value.get(i));
        }
        if (!value.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy");
    }


/*itemList.add(new SpotItem(bitmapImg, data.getStringExtra("name"), data.getStringExtra("adress"), data.getStringExtra("rate")));
                adapter.notifyDataSetChanged();

    final SpotItem recycleItem = adapter.getSelected();
      recycleItem.setIv_Rphoto(bitmapImg);
                recycleItem.setTv_Rname(data.getStringExtra("name"));
                recycleItem.setTv_Rspot(data.getStringExtra("adress"));
                recycleItem.setTv_Rrate(data.getStringExtra("rate"));
    //선택항목 초기화
                adapter.clearSelected();
    //리스트 반영
                adapter.notifyDataSetChanged();*/


    //추가버튼
    public void bindRecord() {
        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordSpot.this, AddSpot.class);
                startActivityForResult(intent, REQUEST_CODE1);
            }
        });
    }

    //수정
    public void bindModify() {
        findViewById(R.id.btn_modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpotItem recycleItem = adapter.getSelected();
                if (recycleItem == null) {
                    Toast.makeText(RecordSpot.this, R.string.err_no_selected_item, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(RecordSpot.this, EditSpot.class);
                intent.putExtra("img", recycleItem.getIv_Rphoto());
                intent.putExtra("name", recycleItem.getTv_Rname());
                intent.putExtra("adress", recycleItem.getTv_Rspot());
                intent.putExtra("rate", recycleItem.getTv_Rrate());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final SpotItem recycleItem = adapter.getSelected();

                String name = data.getStringExtra("name");
                String adress = data.getStringExtra("adress");
                String rate = data.getStringExtra("rate");
                String img = data.getStringExtra("img");

                recycleItem.setIv_Rphoto(img);
                recycleItem.setTv_Rname(name);
                recycleItem.setTv_Rspot(adress);
                recycleItem.setTv_Rrate(rate);
                //선택항목 초기화
                adapter.clearSelected();
                //리스트 반영
                adapter.notifyDataSetChanged();

                nameList.add(name);
                adressList.add(adress);
                rateList.add(rate);
                imgList.add(img);
            }
        }
        if (requestCode == REQUEST_CODE1) {
            if (resultCode == RESULT_OK) {
                String img = data.getStringExtra("img");
                String name = data.getStringExtra("name");
                String adress = data.getStringExtra("adress");
                String rate = data.getStringExtra("rate");

                SpotItem items = new SpotItem(img, name, adress, rate);
                itemList.add(items);
                adapter.notifyDataSetChanged();

                nameList.add(name);
                adressList.add(adress);
                imgList.add(img);
            }
        }
    }

    //삭제
    public void bindDelete() {
        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {

            SharedPreferences pref = getSharedPreferences(NAME_DETAIL, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            ArrayList<String> photoList = new ArrayList();
            ArrayList<String> contentList = new ArrayList();

            @Override
            public void onClick(View v) {
                final SpotItem recyclerItem = adapter.getSelected();
                DetailItem dItem = new DetailItem();
                SharedPreferences pref = getSharedPreferences(NAME_DETAIL, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if (recyclerItem == null) {
                    Toast.makeText(RecordSpot.this, R.string.err_no_selected_item, Toast.LENGTH_SHORT).show();
                    return;
                }

               /* try {
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        JSONObject wrapObj = new JSONObject(pref.getString(String.valueOf(i), ""));
                        JSONArray jsonArray = wrapObj.getJSONArray("item");
                        for (int k = 0; k < jsonArray.length(); k++) {
                            photoList.add(jsonArray.getJSONObject(i).getString("img"));
                            contentList.add(jsonArray.getJSONObject(i).getString("content"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                int num = adapter.getSelectedItem(recyclerItem);
                editor.remove(String.valueOf(num));
                editor.commit();
                //선택한 아이템 삭제
                itemList.remove(recyclerItem);
                //선택항목 초기화
                adapter.clearSelected();
                //리스트 반영
                adapter.notifyDataSetChanged();

            /*    try {
                    JSONObject wrapObj = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < itemList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("img", photoList.get(i));
                        jsonObject.put("content", contentList.get(i));
                        jsonArray.put(jsonObject);
                        wrapObj.put("item", jsonArray);
                        String str = wrapObj.toString();
                        editor.putString(String.valueOf(i), str);
                    }
                    editor.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });

     /* //List 생성
        adapter.addItem(new SpotItem(R.drawable.ic_launcher_slack_home, getRname,getRadress, getRrate));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/

        /*SpotItem item = new SpotItem(R.drawable.ic_launcher_slack_home, getRname, getRadress, getRrate);
        itemList.add(item);
        itemList.set(0,item);*/
/*
        itemList.get()
*/

//액션바인데 필요 없음
/*
    @Override
    public boolean onCreateOptionsMenu(Menu add) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, add);

        return super.onCreateOptionsMenu(add);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(RecordSpot.this, AddSpot.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }*/

    }

}
/*

    String key = getIntent().getStringExtra("key");

                if (key == null) {
                        {
                        key = "empty string";
                        }
                        if (key.equalsIgnoreCase("key")) {
                        SharedPreferences pref = getSharedPreferences("imgName", MODE_PRIVATE);
                        String imgstr = pref.getString(KEY_BITIMG, "");
                        bitmapImg = stringToBitmap(imgstr);
                        itemList.add(new SpotItem(bitmapImg));
                        adapter.notifyDataSetChanged();
                        }

                        }*/
