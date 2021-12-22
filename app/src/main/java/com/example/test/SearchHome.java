package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchHome extends AppCompatActivity {

    static public String clientId = "B2RJ37eYLlBpTpuMmKlg"; //애플리케이션 클라이언트 아이디 값
    static public String clientSecret = "iExRZlEJJI"; //애플리케이션 클라이언트 시크릿 값

    String keyword;
    private ArrayList<SearchHomeItem> itemList;
    private SearchHomeAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recycle);
        RecyclerView recyclerView = findViewById(R.id.searchResult_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchHome.this));

        itemList = new ArrayList<>();
        adapter = new SearchHomeAdapter(context, itemList);

        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");


        if (keyword.equals("") || keyword == null) {
            Log.e("1", "1");
            Toast.makeText(this, "검색어를 입력해 주세요,", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.e("TAG", "내용 keyword: " + keyword);
        }
        SearchResultThread thread = new SearchResultThread();
        Log.e("1", "1");
        thread.start();
    }

    public class SearchResultThread extends Thread {
        public void run() {
            String text = null;
            try {
                text = URLEncoder.encode(keyword, "UTF-8");
                Log.e("1", "1");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패", e);
            }
            //
            // keyword 단어를 UTF-8 형식으로 변경.
            String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text + "&display=5"; // json결과
            // apiURL 은 블로그 자료에 keyword의 결과
            Log.e("1", "1");
            Map<String, String> requestHeaders = new HashMap<>();
            // HashMap 형식 저장공간 생성Log.e("1","1");
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);

            String responseBody = get(apiURL, requestHeaders);
            // apiURL 값과 저장소 (requestHeaders) 를 받아온 것이 responseBody
            runOnUiThread(new Runnable() { // runnable 객체에 구현된 코드를 메인스레드에서 실행시킬 때 사용.
                @Override
                public void run() {
                    parseData(responseBody);
                }
            });
            // URL 읽을 수 있는 데이터로 파싱함.
        }

        public String get(String apiUrl, Map<String, String> requestHeaders) {
            HttpURLConnection con = connect(apiUrl);
            // apiUrl 과 소통이 가능한 클래스 생성, 이름은 con
            try {
                con.setRequestMethod("GET"); // GET 기본값, 웹 서버로 부터 리소스를 가져온다.
                for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                } // Map 전체출력(entrySet, keySet, lterator) header 에 requestHeaders에 담긴거 전체를 차레대로 출력함

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { //정상호출
                    return readBody(con.getInputStream());
                } else { // 에러발생
                    return readBody(con.getErrorStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            return null;
        }

        private HttpURLConnection connect(String apiUrl) { // HttpURLConnection 다리를 지나다니는 사람, URL 다닐 수 있는 다리
            try {
                URL url = new URL(apiUrl);
                return (HttpURLConnection) url.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String readBody(InputStream body) {
            //바디의 데이터는 위에서 매개변수에서 갖고옴.
            InputStreamReader streamReader = new InputStreamReader(body);

            try {
                //BufferedReader Enter 만 경계로 인식해서 space 같은 공백도 같은 문자열로 해서 한줄씩 효율적으로 읽음.
                BufferedReader lineReader = new BufferedReader(streamReader);
                StringBuilder responseBody = new StringBuilder();

                String line;
                while ((line = lineReader.readLine()) != null) {
                    responseBody.append(line);
                }
                return responseBody.toString();
            } catch (IOException e) {
                throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
            }
        }

        private Message parseData(String responseBody) {
            String title;
            String description;
            String link;

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(responseBody);
                // String 형태의 URL 주소(responseBody)를 {responseBody} 객체로 만듦.
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                // jArr = items : [{item},{item}]

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    title = item.getString("title");
                    String titleC = changeString(title);
                    description = item.getString("description");
                    String descriptionC = changeString(description);
                    link = item.getString("link");
                    SearchHomeItem items = new SearchHomeItem();
                    items.setTitle(titleC);
                    items.setDescription(descriptionC);
                    items.setLink(link);
                    itemList.add(items);
                } // 들어온 자료정보에 title 수 만큼 생김
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
            return null;
        }
    }

    private String changeString(String str){

        return str.replaceAll("&", "")

                .replaceAll("[<]", "<")

                .replaceAll("[>]", ">")

                .replaceAll("", "")
                .replaceAll("<b>", "")
                .replaceAll("< b >", "")
                .replaceAll("</b>", "")

                .replaceAll("'", "");
    }

}
