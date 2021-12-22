package com.example.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileEdit extends AppCompatActivity {
    //보안 코드 번호
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    //각 타입의 변수이름
    Button button_btn_profileEditCancle;
    Button button_btn_profileEditOk;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn;
    String currentPhotoPath;
    EditText et_profileEdit_Name;
    EditText et_profileEdit_Hi;
    String KEY_IMAGE = "key_image";
    private static final String USER_INFO = "user_info";
    private static final String KEY_USER = "key_user";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Log.e("ProfileEdit", "onCreate");
        et_profileEdit_Name = findViewById(R.id.et_profileEdit_Name);
        SharedPreferences prefr = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String id = prefr.getString(KEY_USER,"");
        et_profileEdit_Name.setText(id);
        selectedImage = findViewById(R.id.imageView_profileEdit_userImage);
        Glide.with(this).load(Uri.parse(getSavedUriPath(KEY_IMAGE))).into(selectedImage);

        SharedPreferences pref = getSharedPreferences("shared", MODE_PRIVATE);
        String strImg = pref.getString(KEY_IMAGE, "");
        selectedImage.setImageURI(Uri.parse(strImg));

        cameraBtn = findViewById(R.id.button_profileEdit_userImageCameraBtn);
        galleryBtn = findViewById(R.id.button_profileEdit_userImagePhotoBtn);

        button_btn_profileEditCancle = (Button) findViewById(R.id.button_btn_profileEditCancle);
        button_btn_profileEditOk = (Button) findViewById(R.id.button_btn_profileEditOk);
        cameraBtn.setOnClickListener(v -> askCameraPermissions());

        //사진첩 들어가기
        galleryBtn.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);// 정수값 입력 후 컨트롤알트씨 ㅡ> 문자로 비밀번호 입력
        });

        button_btn_profileEditOk.setOnClickListener(v -> {
            saveUriPath(currentPhotoPath, KEY_IMAGE);
            Log.e("currentPhotoPath", "currentPhotoPath");
            System.out.println(currentPhotoPath); // this.getLocalClassName()

            Intent intent = new Intent(ProfileEdit.this, Profile.class);
            startActivity(intent);
        });
        button_btn_profileEditCancle.setOnClickListener(v -> {
            Intent profile = new Intent(ProfileEdit.this, Profile.class);
            startActivity(profile);
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap stringToBitmap(String str ) {
        byte[] decode = java.util.Base64.getDecoder().decode(str);
        //비트맵이 비트맵어레이로 변경됨
        Bitmap bitmap = BitmapFactory.decodeByteArray( decode, 0, decode.length ) ;
        //비트맵어레이를 비트맵으로 바꿔줌
        return bitmap ;
        //리트맵을 리턴
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ProfileEdit", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ProfileEdit", "onResume");
        //selectedImage.setImageURI(Uri.parse(getSavedUriPath(KEY_IMAGE)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ProfileEdit", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ProfileEdit", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ProfileEdit", "onDestroy");
    }

    //권한 확인
    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE); // 코드갑 정수(101)로 입력하고 ctrl+alr+c 눌러서 변환
        } else {
            dispatchTakePictureIntent();
            //사진찾으러 가는 함수
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override //허가여부에 대한 결과
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openCamera();
            } else {
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingSuperCall")
    @Override //결과에 대한 활동
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                //ACTION_MEDIA_SCANNER_SCAN_FILE : 미디어 스캐너로 하여금 해당 파일을 스캔하고 데이터베이스에 추가하도록 한다.
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                //selectedImage.setImageURI(contentUri);
                Glide.with(this).load(Uri.parse(String.valueOf(contentUri))).into(selectedImage);
                currentPhotoPath = contentUri.toString();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri currentPhotoPathh = data.getData();
                //selectedImage.setImageURI(currentPhotoPathh);
                Glide.with(this).load(Uri.parse(String.valueOf(currentPhotoPathh))).into(selectedImage);
                currentPhotoPath = currentPhotoPathh.toString();
            }
        }
    }


    public void saveUriPath(String uriPath, String key) {
        SharedPreferences pref = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, uriPath);
        editor.commit();
    }

    public String getSavedUriPath(String key) {
        SharedPreferences pref = getSharedPreferences("shared", MODE_PRIVATE);
        String str = pref.getString(key, "");
        return str;
    }

    /* Uri uriFile = data.getData();
     ContentResolver resolver = getContentResolver();
                try{
        InputStream inputStream = resolver.openInputStream(uriFile);
        FileInputStream inputStream = new FileInputStream(f);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        //selectedImage.setImageBitmap(bitmap);
        saveBitmapToCache(bitmap);
        selectedImage.setImageBitmap(bitmap);
    }catch (Exception e){
    }*/


    /*  Uri uriFile = data.getData();
      ContentResolver resolver = getContentResolver();
                 try{
          InputStream inputStream = resolver.openInputStream(uriFile);
          Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
          inputStream.close();
          saveBitmapToCache(bitmap);
          selectedImage.setImageBitmap(bitmap);
      }catch (Exception e){

      }*/

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // 이미지 파일 이름
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //이미지가 저장될 폴더명
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //getExternalStoragePublicDirectory : 데이터 유형에 따른 외부 저장소의 저장 공간 경로를 반환. 인자로 디렉터리의 유형을 넘김.
        // Environment.DIRECTORY_PICTURES  그림 파일이 저장됩니다.
        File image = File.createTempFile(imageFileName, /* prefix */     ".jpg",  /* suffix */    storageDir /* directory */);
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 사진앱을 띄워줌
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.test",
                        photoFile);
                //fileprovider는 앱 내부파일을 공유하는데 사용 .getUriForFile(내가 정의한 매니페스트안에 있는 애플리케이션ID, 내가 열고자 하는 파일)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //사진 찍은 결과물을 photoUri에 저장해 주세요.
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}
