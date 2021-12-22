

package com.example.test;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.test.YouTube.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class AppMain extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener , GoogleMap.OnInfoWindowClickListener{
    public static final int REQUEST_CODE= 101;
    // OnMapReadyCallback : googleSdk 에서 제공하는 인터페이스로 맵 사용준비가 되면 콜백하는 기능을 구현.
    //ActivityCompat(액티비티 특성에 접근을 돕는 클래스).OnRequestPermissionsResultCallback(permission request 에 대한 결과를 받기위한 인터페이스)
    // PlacesListener : 장소 선택 이벤트에 대한 리스너   내 생각 : googleSdk 에서 제공하는 인터페이스로 주변 장소에 대한 정보를 받아옴.

    private ImageButton imageButton_btn_friends;
    private ImageButton imageButton_btn_search;
    private Button button_btn_recordSpot, button_btn_profile, btn_appmain_searchRestaurant, btn_appmain_searchCafe;
    private View frg_map;

    private TextView textView;
    private int num = 1;
    firstHandler ohandler = new firstHandler();
    secondHandler twhandler = new secondHandler();
    thirdHandler thandler = new thirdHandler();
    fourthHandler fhandler = new fourthHandler();
    List<Marker> previous_marker = null;

    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String TAG= "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE= 2001;
    private static final int UPDATE_INTERVAL_MS= 1000;  // 1000 이 1초임
    private static final int FASTEST_UPDATE_INTERVAL_MS= 500;

    private static final int PERMISSIONS_REQUEST_CODE= 100; //onRequestPermissionsResult 에서 수신된 결과에서 ActivityCompatRequestPermissions 를 사용한 퍼미션 요청을구별하기 위해서 사용됨.
    boolean needRequest = false;

    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    // 앱을 실행하기 위해 필요한 퍼미션을 정의함.
    // 외부저장소

    Location mCurrentLocation;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout; // Snackbar 사용하기 위해서는 View가 필요함.

    class BackThread extends Thread {
        @Override
        public void run() {
            super.run();
            Bundle bundle = new Bundle();
            while (true) {
                Message omessage = ohandler.obtainMessage();
                Message twmessage = twhandler.obtainMessage();
                Message tmessage = thandler.obtainMessage();
                Message fmessage = fhandler.obtainMessage();
                if (num == 1) {
                    bundle.putInt("1", num);
                    omessage.setData(bundle);
                    ohandler.sendMessage(omessage);
                    num = 2;
                } else if (num == 2) {
                    bundle.putInt("2", num);
                    twmessage.setData(bundle);
                    twhandler.sendMessage(twmessage);
                    num = 3;
                } else if (num == 3) {
                    bundle.putInt("3", num);
                    tmessage.setData(bundle);
                    thandler.sendMessage(tmessage);
                    num = 4;
                } else if (num == 4) {
                    bundle.putInt("4", num);
                    fmessage.setData(bundle);
                    fhandler.sendMessage(fmessage);
                    num = 1;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class firstHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int num;
            Bundle bundle = msg.getData();
            num = bundle.getInt("1");
            switch (num) {
                case 1:
                    textView.setText("와구와우");
                    break;
            }
        }
    }

    class secondHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int num;
            Bundle bundle = msg.getData();
            num = bundle.getInt("2");
            switch (num) {
                case 2:
                    textView.setText("한식");
                    break;
            }
        }
    }

    class thirdHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int num;
            Bundle bundle = msg.getData();
            num = bundle.getInt("3");
            switch (num) {
                case 3:
                    textView.setText("중식");
                    break;
            }
        }
    }

    class fourthHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int num;
            Bundle bundle = msg.getData();
            num = bundle.getInt("4");
            switch (num) {
                case 4:
                    textView.setText("일식");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SEA", "내용 1: ");
        Log.e("TAG", "onCreate 1 : ");
        // setContentView(R.layout.activity_appmain);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // FLAG_KEEP_SCREEN_ON : WindowManager 클래스의 기능 중 하나로 이 창이 사용자에게 표시되는 한 기기의 화면을 켜고 밝게 유지함.
        Log.e("TAG", "onCreate  2: ");

        setContentView(R.layout.activity_appmain);
        Log.e("TAG", "onCreate  3: ");

        mLayout = findViewById(R.id.layout_main);
        Log.e("TAG", "onCreate  4: ");

        previous_marker = new ArrayList<>();
        Log.e("TAG", "onCreate  5: ");

        textView = findViewById(R.id.textView_main);
        BackThread thread = new BackThread();
        thread.start();


       /* Intent intent = getIntent();
        String message = intent.getExtras().getString("userId", "");
        Toast.makeText(this, message + "님 환영합니다.", Toast.LENGTH_SHORT).show();*/

        imageButton_btn_friends = findViewById(R.id.imageButton_btn_friends);
        imageButton_btn_search = findViewById(R.id.imageButton_btn_search);
        button_btn_recordSpot = findViewById(R.id.button_btn_recordSpot);
        button_btn_profile = findViewById(R.id.button_btn_profile);
        Log.e("TAG", "onCreate  6: ");
        btn_appmain_searchRestaurant = findViewById(R.id.btn_appmain_searchRestaurant);
        Log.e("TAG", "onCreate  7: ");
        btn_appmain_searchRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onCreate  8: ");
                showPlaceInformationRes(currentPosition);
                Log.e("TAG", "onCreate  8-2: ");

            }
        });
        btn_appmain_searchCafe = findViewById(R.id.btn_appmain_searchCafe);
        btn_appmain_searchCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaceInformationCafe(currentPosition);
            }
        });

       /* frg_map = findViewById(R.id.map);
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        // googleMap 호출, onMapReady 콜백이 실행

        locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS).setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
        // locationRequest : 앱에서 위치를 요청하거나 권한 업데이트를 수신할 때 필요한 LocationRequest 클래스의 변수명, 위치 서비스에 사용할 위치 소스 제공(정확한 위치정보 제공)/
        // 위치업데이트 수신간격 밀리초 단위로 설정 / 위치 업데이트 처리할 수 있는 가장 빠른 간격을 밀리초 단위로 설정
        Log.e("TAG", "locationRequest 9: ");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        // LocationSettingsRequest.Builder : 현재 위치를 받아오는 기능.
        Log.e("TAG", "locationRequest 10: ");
        builder.addAllLocationRequests(Collections.singleton(locationRequest));
        // Collections 는 클래스로 Collection 인터페이스를 가지고 놀기 위함. (Collection 인터페이스는 자료구조 형으로 set,list,queue 인터페이스의 부모 인터페이스)
        Log.e("TAG", "locationRequest 11: ");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // 마지막에 사용된 위치정보를 사용함.
        Log.e("TAG", "mFusedLocationClient 12: ");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // SupportMapFragment 를 통해 레이아웃에 만든 fragment 의 ID 를 참조하고 구글맵을 호출한다.
        // 앱에 map 을 나타내는 가장 간단한 방법중 하나이며 레이아웃 XML 파일에서 구성요소가 있어야 함.  *프래그먼트는 UI를 재사용하기 위해 사용
        mapFragment.getMapAsync(this);
        // 메인스레드에서 동작하기 위해서는 getMapAsync 이 반드시 필요. -> 메인스레드에서는 UI를 변경과 인터넷 서버 접속을 해서는 안됨. 둗 중 하나가 이유.
        Log.e("TAG", "내용 : 13");

        imageButton_btn_friends.setOnClickListener(v -> {
            Log.e("TAG", "내용 : 14");
            Intent friends = new Intent(AppMain.this, MainActivity.class);
            Log.e("TAG", "내용 : 15");
            startActivity(friends);
            Log.e("TAG", "내용 : 16");
        });
        imageButton_btn_search.setOnClickListener(v -> {
            Log.e("TAG", "내용 : 17");
            Intent search = new Intent(AppMain.this, Search.class);
            Log.e("TAG", "내용 : 18");
            startActivity(search);
            Log.e("TAG", "내용 : 19");
        });

        button_btn_recordSpot.setOnClickListener(v -> {
            Log.e("TAG", "내용 : 20");
            Intent recordSpot = new Intent(AppMain.this, RecordSpot.class);
            Log.e("TAG", "내용 : 21");
            startActivity(recordSpot);
            Log.e("TAG", "내용 : 22");
        });
        button_btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(AppMain.this, Profile.class);
                startActivity(profile);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("SEA", "내용 2: ");
        // 구글 MapSdk 에서 제공하는 OnMapReadyCallback 인터페이스를 상속받아야 이 메서드를 사용할 수 있음, 콜백메서드로 맵이 사용될 준비가 되면 호출됨, 권한 여부 체크하고 실행 됨.
        Log.e("TAG", "내용 : 23");
        mMap = googleMap;
        Log.e("TAG", "내용 : 24");
        setDefaultLocation();
        // 런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        // 지도의 초기위치를 서울로 이동
        Log.e("TAG", "내용 : 25");
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크를 한다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // Context 는 안드로이드 환경에 접근하기 위한 추상클래스(특정 리소스나 클래스등 여러군데 접근 가능)
        // ContextCompat 은 Context 에 접근을 도와주는 클래스
        Log.e("TAG", "내용 : 26");
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        Log.e("TAG", "내용 : 27");
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED&& hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG", "내용 : 28");
            // 2. 이미 퍼미션을 가지고 있다면
            //startLocationUpdate(); // 3. 위치 업데이트 시작
            Log.e("TAG", "내용 : 29");
        } else { // 2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우가 존재 (3-1 , 4-1)
            Log.e("TAG", "내용 : 30");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                // ActivityCompat 는 ContextCompat 을 상속받은 클래스로 Activity 에 접근을 도와줌.
                // shouldShowRequestPermissionRationale : 권한을 요청하기 전에 UI를 보여줘도 되는지 근거를 가져옴.
                Log.e("TAG", "내용 : 31");
                // 3-1. 사용자가 퍼미션을 거부를 한 적이 있는 경우
                Snackbar.make(mLayout, "이 앱을 실항하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    // 3-2 요청을 진행하기 전에 사용자에게 퍼미션이 필요한 이유를 설명해줄 필요가 있음.
                    @Override
                    public void onClick(View v) {
                        Log.e("TAG", "내용 : 32");
                        ActivityCompat.requestPermissions(AppMain.this, REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
                        Log.e("TAG", "내용 : 33");
                        // 3-3 사용자에게 퍼미션 요청을 한다. 요청 결과는 onRequestPermissionResult 에서 수신됨.
                    }
                }).show();
                Log.e("TAG", "내용 : 34");
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
                Log.e("TAG", "내용 : 35");
                // 4-1 사용자가 퍼미션을 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 함.
                // 요청 결과는 onRequestPermissionResult 에서 수신됨.
            }
        }
        googleMap.setOnInfoWindowClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        Log.e("TAG", "내용 : 36");
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("TAA", "내용 : 37");
            }
        });
    }

    LocationCallback locationCallback = new LocationCallback() {
        // 사용이 가능하다고 권한이 승인 되었으면 콜백메서드가 실행.
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.e("SEA", "내용 3: ");
            super.onLocationResult(locationResult);
            Log.e("TAG", "내용 : 38");
            List<Location> locationList = locationResult.getLocations();
            //  Location : 위도 경도로 지리적 위치의 데이터를 담는 클래스
            Log.e("TAG", "내용 : 39");

            if (locationList.size() > 0) {
                Log.e("TAG", "내용 : 40");
                location = locationList.get(locationList.size() - 1);
                Log.e("TAG", "내용 : 41");
                // location = locationList.get(0);

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                Log.e("TAG", "내용 : 42");

                String markerTitle = getCurrentAddress(currentPosition);
                Log.e("TAG", "내용 : 43");
                String markerSnippet = "위도 : " + String.valueOf(location.getLatitude() + " / 경도 : " + String.valueOf(location.getLongitude()));
                Log.e("TAG", "내용 : 44");
                Log.d(TAG, "onLocationResult : " + markerSnippet);

                setCurrentLocation(location, markerTitle, markerSnippet);
                Log.e("TAG", "내용 : 45");
                // 현재 위치에 마커 생성하고 이동

                mCurrentLocation = location;
                Log.e("TAG", "내용 : 46");
            }
        }
    };

    private void startLocationUpdate() {
        Log.e("SEA", "내용 4: ");
        Log.e("SEA", "내용 1: ");
        Log.e("TAG", "내용 : 47");
        if (!checkLocationServiceStatus()) {
            Log.e("TAG", "내용 : 48");
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
            Log.e("TAG", "내용 : 49");
        } else {
            Log.e("TAG", "내용 : 50");
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            Log.e("TAG", "내용 : 51");
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            Log.e("TAG", "내용 : 52");

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED|| hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "내용 : 53");
                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }
            Log.e("TAG", "내용 : 54");
            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            // 내 마지막에 표시된 위치정보를 사용함.
            Log.e("TAG", "내용 : 55");

            if (checkPermission()) {
                Log.e("TAG", "내용 : 56");
                mMap.setMyLocationEnabled(true);
                // setMyLocationEnabled : 현재 내 위치를 나타내는 메소드.
                Log.e("TAG", "내용 : 57");
            }
        }
    }

    @Override
    protected void onStart() {
        Log.e("SEA", "내용 5: ");
        super.onStart();
        Log.e("TAG", "내용 : 58");
        Log.d(TAG, "onStart");

        if (checkPermission()) {
            Log.e("TAG", "내용 : 59");
            Log.d(TAG, "onStart : call ,FusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            // 마지막에 표시된 위치정보를 사용함.
            Log.e("TAG", "내용 : 60");

            if (mMap != null) {
                Log.e("TAG", "내용 : 61");
                mMap.setMyLocationEnabled(true);
                // setMyLocationEnabled : 내 위치를 표시하는 기능.
                Log.e("TAG", "내용 : 62");
            }
        }
    }

    @Override
    protected void onStop() {
        Log.e("SEA", "내용 6: ");
        super.onStop();
        Log.e("TAG", "내용 : 63");
        if (mFusedLocationClient != null) {
            Log.e("TAG", "내용 : 64");
            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            // removeLocationUpdates : 위치 업데이트 중지
            Log.e("TAG", "내용 : 65");
        }
    }

    public String getCurrentAddress(LatLng latLng) {
        Log.e("SEA", "내용 7: ");
        Log.e("TAG", "내용 : 66");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Log.e("TAG", "내용 : 67");
        List<Address> addresses;
        Log.e("TAG", "내용 : 68");
        try {
            Log.e("TAG", "내용 : 69");
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Log.e("TAG", "내용 : 70");
        } catch (IOException e) {
            // 네트워크 문제
            Log.e("TAG", "내용 : 71");
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_SHORT).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e("TAG", "내용 : 72");
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_SHORT).show();
            return "잘못된 GPS 좌표";
        }
        Log.e("TAG", "내용 : 73");

        if (addresses == null || addresses.size() == 0) {
            Log.e("TAG", "내용 : 74");
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_SHORT).show();
            return "주소 미발견";
        } else {
            Log.e("TAG", "내용 : 75");
            Address address = addresses.get(0);
            Log.e("TAG", "내용 : 76");
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServiceStatus() {
        Log.e("SEA", "내용 8: ");
        Log.e("TAG", "내용 : 77");
        LocationManager locationManager = (LocationManager) getApplication().getSystemService(LOCATION_SERVICE);
        Log.e("TAG", "내용 : 78");
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    } // GPS 나 NETWORK 제공이 가능하면 값을 리턴 해주는 메소드


    // 현재 내 위치에 대한 마커표시임.
    // 마커의 위치, 이름, 설명을 설정하며 마커에 값이 들어있으면 다 삭제하고 다시 내용을 담고, 값이 없다면 제목이나 설명등을 마커에 add 하고 다시 마커를 표시한 후 카메라를 그 위치로 이동함.
    // 즉, 내가 이동 할 때마다 마커가 내 위치 기준으로 최신화됨(마지막에 위치했던 위도 경도 값기 준으로)
    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        Log.e("SEA", "내용 9: ");
        Log.e("TAG", "내용 : 79");
        if (currentMarker != null) {
            Log.e("TAG", "내용 : 80");
            currentMarker.remove();
            Log.e("TAG", "내용 : 81");
        }
        Log.e("TAG", "내용 : 82");
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e("TAG", "내용 : 83");

        MarkerOptions markerOptions = new MarkerOptions();
        Log.e("TAG", "내용 : 84");
        markerOptions.position(currentLatLng);
        Log.e("TAG", "내용 : 85");
        markerOptions.title(markerTitle);
        Log.e("TAG", "내용 : 86");
        markerOptions.snippet(markerSnippet);
        Log.e("TAG", "내용 : " + markerSnippet);
        Log.e("TAG", "내용 : 87");
        markerOptions.draggable(true);
        Log.e("TAG", "내용 : 88");
        currentMarker = mMap.addMarker(markerOptions);
        Log.e("TAG", "내용 : 89");
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        Log.e("TAG", "내용 : 90");
        mMap.moveCamera(cameraUpdate);
        Log.e("TAG", "내용 : 91");
    }

    // 위치정보 초기값 설정. , 미국에서 시작되는거 서울로 하고 카메라 화면 이동하는 거.
    public void setDefaultLocation() {
        Log.e("SEA", "내용 10: ");
        Log.e("TAG", "내용 : 92");
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        Log.e("TAG", "내용 : 93");
        String markerTitle = "위치정보 가져올 수 없음";
        Log.e("TAG", "내용 : 94");
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인하세요";
        Log.e("TAG", "내용 : 95");

        // if (currentMarker != null) {
        Log.e("TAG", "내용 : 96");
        //currentMarker.remove();
        Log.e("TAG", "내용 : 97");
        MarkerOptions markerOptions = new MarkerOptions();
        Log.e("TAG", "내용 : 98");
        markerOptions.position(DEFAULT_LOCATION);
        Log.e("TAG", "내용 : 99");
        markerOptions.title(markerTitle);
        Log.e("TAG", "내용 : 100");
        markerOptions.snippet(markerSnippet);
        Log.e("TAG", "내용 : 101");
        markerOptions.draggable(true);
        Log.e("TAG", "내용 : 102");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        Log.e("TAG", "내용 : 103");
        currentMarker = mMap.addMarker(markerOptions);
        Log.e("TAG", "내용 : 104");
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 18);
        Log.e("TAG", "내용 : 105");
        mMap.moveCamera(cameraUpdate);
        Log.e("TAG", "내용 : 106");
        //}
    }

    // 여기부터는 런타임 퍼미션 처리를 위한 메소드들
    private boolean checkPermission() {
        Log.e("SEA", "내용 11: ");
        Log.e("TAG", "내용 : 107");
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // 매니페스트에서 ACCESS_FINE_LOCATION 권한 부여해줬는지 확인(사용자 기기 위치에 접근할 수 있는 권한) , ACCESS_COARSE_LOCATION 보다 더 정확한 위치제공
        Log.e("TAG", "내용 : 108");
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        // 매니페스트에서 ACCESS_COARSE_LOCATION 권한 부여해줬는지 확인(사용자 기기 위치에 접근할 수 있는 권한), 도시 블록 내에 위치 정확성을 제공

        Log.e("TAG", "내용 : 109");

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED&& hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG", "내용 : 110");
            return true;
        }
        Log.e("TAG", "내용 : 111");
        return false;
    }

    // ActivityCompat.OnRequestPermissionsResultCallback 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("SEA", "내용 12: ");
        Log.e("TAG", "내용 : 112");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("TAG", "내용 : 113");
        if (requestCode ==PERMISSIONS_REQUEST_CODE&& grantResults.length == REQUIRED_PERMISSIONS.length) {
            Log.e("TAG", "내용 : 114");
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grantResults) {
                Log.e("TAG", "내용 : 115");
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Log.e("TAG", "내용 : 116");
                    check_result = false;
                    Log.e("TAG", "내용 : 117");
                    break;
                }
            }

            if (check_result) {
                Log.e("TAG", "내용 : 118");
                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdate();
                Log.e("TAG", "내용 : 119");
            } else {
                Log.e("TAG", "내용 : 120");
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다. 2가지 경우가 존재
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Log.e("TAG", "내용 : 121");

                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해 주세요",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Log.e("TAG", "내용 : 122");
                            finish();
                            Log.e("TAG", "내용 : 123");
                        }
                    }).show();
                    Log.e("TAG", "내용 : 124");
                } else {
                    Log.e("TAG", "내용 : 125");
                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, " 퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("TAG", "내용 : 126");
                            finish();
                            Log.e("TAG", "내용 : 127");
                        }
                    }).show();
                    Log.e("TAG", "내용 : 128");
                }
            }
        }
    }

    // 여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        Log.e("SEA", "내용 13: ");
        Log.e("TAG", "내용 : 129");
        AlertDialog.Builder builder = new AlertDialog.Builder(AppMain.this);
        Log.e("TAG", "내용 : 130");
        builder.setTitle("위치 서비스 비활성화");
        Log.e("TAG", "내용 : 131");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다. \n" + "위치 설정을 수정하실래요?");
        Log.e("TAG", "내용 : 132");
        builder.setCancelable(true);
        Log.e("TAG", "내용 : 133");
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("TAG", "내용 : 134");
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Log.e("TAG", "내용 : 135");

                startActivityForResult(callGPSSettingIntent,GPS_ENABLE_REQUEST_CODE);
                Log.e("TAG", "내용 : 136");
            }
        });
        Log.e("TAG", "내용 : 137");
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("TAG", "내용 : 138");
                dialog.cancel();
                Log.e("TAG", "내용 : 139");
            }
        });
        builder.show();
        Log.e("TAG", "내용 : 140");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("SEA", "내용 14: ");
        Log.e("TAG", "내용 : 141");
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "내용 : 142");
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE: //사용자가 GPS 활성 시켰는지 검사
            Log.e("TAG", "내용 : 144");
            if (checkLocationServiceStatus()) {
                Log.e("TAG", "내용 : 145");
                Log.d(TAG, "onActivityResult : GPS 활성화 되있음");

                needRequest = true;

                Log.e("TAG", "내용 : 146");
                return;
            }

            Log.e("TAG", "내용 : 147");
            break;
        }
    }

    @Override
    public void onPlacesFailure(PlacesException e) {
        Log.e("SEA", "내용 15: ");
    }

    @Override
    public void onPlacesStart() {
        Log.e("SEA", "내용 16: ");
    }

    // 장소를 불러오는 메소드, 사용자 정의 메소드
    @Override
    public void onPlacesSuccess(final List<Place> places) {
        Log.e("SEA", "내용 17: ");
        Log.e("TAG", "내용 : 148");
        // Place 클래스는 지리적 장소를 표시하고 추가적인 정보를 화면에 표시할 수 있게 해줌.
        runOnUiThread(new Runnable() {
            // UI 건드리는 건 메인메소드에서 불가능해서 runOnUiThread 를 이용해서 접근.
            @Override
            public void run() {
                Log.e("TAG", "내용 : 149");
                for (noman.googleplaces.Place place : places) {
                    Log.e("TAG", "내용 : 150");
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    Log.e("TAG", "내용 : 151");

                    String markerSnippet = getCurrentAddress(latLng);
                    Log.e("TAG", "내용 : 152");

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(markerSnippet);
                    Marker item = mMap.addMarker(markerOptions);
                    previous_marker.add(item);
                    Log.e("TAG", "내용 : 158");
                }
                // 중복 마커 제거
                Log.e("TAG", "내용 : 159");
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);
                Log.e("TAG", "내용 : 163");
            }
        });
        Log.e("TAG", "내용 : 164");

    }

    public void showPlaceInformationRes(LatLng latLng) {
        Log.e("SEA", "내용 18: ");
        Log.e("TAG", "내용 : 165");
        mMap.clear(); //지도 클리어
        Log.e("TAG", "내용 : 166");
        if (previous_marker != null) {
            Log.e("TAG", "내용 : 167");
            previous_marker.clear(); //지역정보 마커 클리어
            Log.e("TAG", "내용 : 168");

            new NRPlaces.Builder().listener(AppMain.this).key("AIzaSyCoyDcqsUpc0eHPwWVKhS7dP1YD_fiYWdw")
                    .latlng(location.getLatitude(), location.getLongitude()) //현재위치
                    .radius(100) // 500 미터 내에서 검색
                    .type(PlaceType.RESTAURANT) // 음식점
                    .build()
                    .execute();
            Log.e("TAG", "내용 : 169");
        }
    }

    public void showPlaceInformationCafe(LatLng latLng) {
        Log.e("SEA", "내용 18: ");
        Log.e("TAG", "내용 : 165");
        mMap.clear(); //지도 클리어
        Log.e("TAG", "내용 : 166");
        if (previous_marker != null) {
            Log.e("TAG", "내용 : 167");
            previous_marker.clear(); //지역정보 마커 클리어
            Log.e("TAG", "내용 : 168");

            new NRPlaces.Builder().listener(AppMain.this).key("AIzaSyCoyDcqsUpc0eHPwWVKhS7dP1YD_fiYWdw")
                    .latlng(location.getLatitude(), location.getLongitude()) //현재위치
                    .radius(100) // 500 미터 내에서 검색
                    .type(PlaceType.CAFE) // 카페
                    .build()
                    .execute();
            Log.e("TAG", "내용 : 169");
        }
    }

    @Override
    public void onPlacesFinished() {
        Log.e("SEA", "내용 19: ");
    }

    /* LatLng SEOUL = new LatLng(37.56, 126.97);

        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions.position(SEOUL);
        makerOptions.title("서울");
        makerOptions.snippet("한국의 수도");
        googleMap.addMarker(makerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));*/



    public void onInfoWindowClick(Marker marker) {
        String markerTitle = marker.getTitle();
        String markerSnippet = marker.getSnippet();
        Intent intent = new Intent(AppMain.this, AddSpot.class);
        intent.putExtra("Appmain", 1);
        intent.putExtra("title", markerTitle);
        intent.putExtra("snippet", markerSnippet);
        startActivity(intent);
    }
}
