package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    private EditText signUp_name;
    private EditText signUp_id;
    private EditText signUp_pw;
    private EditText signUp_pwcheck;

    private Button button_cancleButton;
    private Button button_btn_signUp;

    private Button button_btn_existenceCheck;
    private Toast toast;

    public void existenceCheck_ID() {
        String s = signUp_id.getText().toString();
        Boolean isExitId = idList.contains(s);
        if (!isExitId) {
            Toast.makeText(SignUp.this, "중복확인 완료", Toast.LENGTH_SHORT);
            if (toast == null)
                toast = Toast.makeText(SignUp.this, "중복확인 완료", Toast.LENGTH_SHORT);
            else
                toast.setText("중복확인");
            toast.show();
        } else {
            Toast.makeText(this, "동일한 ID가 존재합니다.", Toast.LENGTH_SHORT).show();
        }

    }

    private static final String USER_INFO = "user_info";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_ID = "key_id";
    private static final String KEY_PWD = "key_pwd";

    ArrayList<String> idList = new ArrayList();
    ArrayList<String> pwdList = new ArrayList();
    ArrayList<String> nameList = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameList = getStringArrayPref(KEY_NAME);
        idList = getStringArrayPref(KEY_ID);
        pwdList = getStringArrayPref(KEY_PWD);

        button_btn_signUp = findViewById(R.id.button_btn_signUp);
        button_cancleButton = findViewById(R.id.button_cancleButton);

        signUp_name = findViewById(R.id.signUp_name);
        signUp_id = findViewById(R.id.signUp_id);
        signUp_pw = findViewById(R.id.signUp_pw);
        signUp_pwcheck = findViewById(R.id.signUp_pwcheck);

        button_btn_existenceCheck = findViewById(R.id.button_btn_existenceCheck);
        button_btn_existenceCheck.setOnClickListener(v -> existenceCheck_ID());

        button_btn_signUp.setOnClickListener(v -> {
                    String pw = signUp_pw.getText().toString();
                    String pwc = signUp_pwcheck.getText().toString();
                    if (pw.equals(pwc)) {
                        String id = signUp_id.getText().toString();
                        String name = signUp_name.getText().toString();
                        String pwd = signUp_pw.getText().toString();

                        idList.add(id);
                        pwdList.add(pwd);
                        nameList.add(name);

                        setStringArrayPref(KEY_ID, idList);
                        setStringArrayPref(KEY_NAME, nameList);
                        setStringArrayPref(KEY_PWD, pwdList);

                        Toast.makeText(this, "가입완료", Toast.LENGTH_SHORT).show();
                        Intent signUp = new Intent(SignUp.this, Start.class);
                        startActivity(signUp);

                    } else {
                        Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        button_cancleButton.setOnClickListener(v -> {
            Intent cancle = new Intent(SignUp.this, Start.class);
            startActivity(cancle);
        });
    }

    private void setStringArrayPref(String key, ArrayList<String> values) {
        //연습
        SharedPreferences prefs = getSharedPreferences(USER_INFO, MODE_PRIVATE);
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
        SharedPreferences prefs = getSharedPreferences(USER_INFO, MODE_PRIVATE);
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
    protected void onStop() {
        super.onStop();
        finish();
    }

    /*public static class UserInfo {
        private String name;
        private String id;
        private String pwd;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }
*/

}


