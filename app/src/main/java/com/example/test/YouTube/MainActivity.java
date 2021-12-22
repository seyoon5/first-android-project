package com.example.test.YouTube;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    EditText search;
    Button button;

    AsyncTask<?, ?, ?> searchTask;

    ArrayList<SearchData> sdata = new ArrayList<SearchData>();

    final String serverKey="AIzaSyD0HhKqtuUPkcJjViED8bs53FfjXKSxYRU";


    RecyclerView recyclerview;

    UtubeAdapter utubeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        search = (EditText) findViewById(R.id.search);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //제목을 입력하면 제목을 키워드로 하여 비동기 식으로 영상에 대한 정보를 가지고 온다.
                searchTask = new searchTask().execute();
                // AsyncTask : 간단한 UI 작업을 백그라운드 스레드가 수행하는 클래스
                Log.e("video", "내용 : 백그라운드 스레드실행되면서 다음 코드 수행" );
            }
        });



        recyclerview= findViewById(R.id.recyclerview);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(mLinearLayoutManager);
    }

    private class searchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // ... : 백그라운드에서 작업한다는 소리
            Log.e("video", "내용 : 2" );

            try {
                parsingJsonData(getUtube());
            } catch (JSONException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.e("video", "내용 : 5" );
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 백그라운드에서 계산이 완료된 후 UI 스레드에서 호출 됨, 백그라운드의 계산 결과는 이 단계에서 매개변수로 전달됨.
            utubeAdapter = new UtubeAdapter(MainActivity.this, sdata);
            recyclerview.setAdapter(utubeAdapter);
            utubeAdapter.notifyDataSetChanged();
        }
    }

    //유튜브 url에 접근하여 검색한 결과들을 json 객체로 만들어준다 (Url 주소를 String 타입으로 만들고 그 정보를 JSON 객체에 담고 반환해줌)
    public JSONObject getUtube() throws IOException {
        Log.e("video", "내용 : 3" );

        String originUrl = "https://www.googleapis.com/youtube/v3/search?"
                + "part=snippet&q=" + search.getText().toString()
                + "&key="+ serverKey+"&maxResults=10";
        // 검색 결과에 대한 주소 값 : originUrl
        String myUrl = String.format(originUrl);
        // originUrl 를 스트링 형태로 만들어 준 것이 myUrl
        URL url = new URL(myUrl);
        // URL 클래스, 1. URL 을 추상화 해서 만든 클래스. 2. final 클래스라 상속불가 3. 반드시 예외처리 해야함.
        HttpURLConnection connection =(HttpURLConnection)url.openConnection();
        // URLConnection 객체를 생성해준다. URLConnection 클래스는 추상 클래스이기때문에 객체를 생성할 수 없고, URL 클래스의 openConnction() 메서드를 이용 해서 객체를 생성할 수 있다.
        // HttpURLConnection 클래스는 URL 내용을 읽어오거나 URL 주소에 데이터를 전달할 떄 사용, 서버 URL 이랑 소통하기 위해 필요
        connection.setRequestMethod("GET"); // 서버에게 동일한 요청을 여러 번 전송하더라도 동일한 응답이 돌아와야 한다는 것을 의미하며 주로 조회를 할 때 사용 됨.
        // <-> "POST" : 서버에게 동일한 요청을 여러 번 전송해도 응답은 항상 다를 수 있음. 게시글 변경같은 생성 수정 삭제등에 사용(일반적으로 생성은 post /수정은 put, patch/ 삭제는 delete)
        connection.setReadTimeout(10000); // 연결은 성공 했지만 시간 내에 data를 못 받으면 에러가 남.
        connection.setConnectTimeout(15000); // 통신 링크를 열 때 사용할 지정된 시간값을 설정.
        connection.connect();


        String line;
        String result="";
        InputStream inputStream=connection.getInputStream();
        // 안으로 들어오는 연결 통로
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        // Stream 으로 들어온 것을 일시적으로 저장(wrap)한 것. 여러 기능을 제공
        StringBuffer response = new StringBuffer();
        // 문자열 임시 저장공간
        while ((line = reader.readLine())!=null){
            response.append(line);
        }
        System.out.println("검색결과"+ response);
        result=response.toString();
        // Url 값이 스트링 타입의 result 가 됐음.

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }

    //json 객체를 가지고 와서 필요한 데이터를 파싱한다.
    //파싱을 하면 여러가지 값을 얻을 수 있는데 필요한 값들을 세팅하셔서 사용하면 됨.
    private void parsingJsonData(JSONObject jsonObject) throws JSONException {
        Log.e("video", "내용 : 4" );
        //재검색할때 데이터들이 쌓이는걸 방지하기 위해 리스트를 초기화 시켜준다.
        sdata.clear();

        JSONArray contacts = jsonObject.getJSONArray("items");

        for (int i = 0; i < contacts.length(); i++) {
            JSONObject c = contacts.getJSONObject(i);
            String kind =  c.getJSONObject("id").getString("kind"); // 종류를 체크하여 playlist도 저장 ,(ID 객체에서 kind 밸류 선택)
            if(kind.equals("youtube#video")){ //  {kind : youtube#video} 이게 맞다면
                vodid = c.getJSONObject("id").getString("videoId");
                // 유튜브 동영상 아이디 값. 재생 시 필요함.
            }else{
                vodid = c.getJSONObject("id").getString("playlistId"); // 유튜브 동영상이 재생목록인 경우 필요함.
            }

            String title = c.getJSONObject("snippet").getString("title"); //유튜브 제목을 받아옵니다
            String changString = stringToHtmlSign(title);


            String date = c.getJSONObject("snippet").getString("publishedAt") //등록날짜
                    .substring(0, 10);
            String imgUrl = c.getJSONObject("snippet").getJSONObject("thumbnails")
                    .getJSONObject("default").getString("url");  //썸네일 이미지 URL값
            // ( {snippet : { thumbnails : { default : url, width, height } } } 스니펫의 썸네일의 디폴트에서의 name "url" 빼오는데 반환값이 String 임.

            //JSON으로 파싱한 정보들을 객체화 시켜서 리스트에 담아준다.
            sdata.add(new SearchData(vodid, changString, imgUrl));
        }
    }

    String vodid = "";


    //영상 제목을 받아올때 " ' 문자가 그대로 출력되기 때문에 다른 문자로 대체 해주기 위해 사용하는 메서드 (굳이 안해도 됨... 실행 해보고 마음에 안드는 문자열 나오고 대체하면 될듯..)
    private String stringToHtmlSign(String str){

        return str.replaceAll("&", "[&]")

                .replaceAll("[<]", "<")

                .replaceAll("[>]", ">")

                .replaceAll("", "")

                .replaceAll("'", "");
    }


}
