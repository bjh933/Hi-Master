package com.example.a1.himaster.SKPlanet.Tmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1.himaster.MyInfo;
import com.example.a1.himaster.R;
import com.example.a1.himaster.num20_Main;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;
import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapData tMapData = null;
    private TMapGpsManager tmapGps = null;
    private TMapView tmapView = null;
    private static String mApiKey = "b42a1814-4abc-36c2-a743-43c5f81cd73d";
    private static int mMarkerID;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();
    private MapPoint myHome;

    private String address;
    private Double lat = null;
    private Double lon = null;
    private Double selectLat = null;
    private Double selectLon = null;

    private TextView searchTv, okTv;
    private EditText searchEt;
    String residenceName="";

    ListView addressView;
    MapListItemAdapter listAdapter;
    String locaName, locaLat, locaLon, locaAddr;
    int gpsFlag = 0;

    @Override
    public void onLocationChange(Location location) {
        if(m_bTrackingMode) {
            tmapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.residenceact);

        searchTv = (TextView) findViewById(R.id.searchTv);
        okTv = (TextView)findViewById(R.id.okTv);
        searchEt = (EditText)findViewById(R.id.searchEt);
        addressView = (ListView)findViewById(R.id.addressview);
        mContext = this;
        okTv.setEnabled(false);
        okTv.setTextColor(Color.parseColor("#DDDDDD"));



        tMapData = new TMapData();
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.mapview);
        tmapView = new TMapView(this);

        linearLayout.addView(tmapView);
        tmapView.setSKPMapApiKey(mApiKey);

        tmapView.setIconVisibility(true);   //  현 위치 아이콘 표시

        tmapView.setZoomLevel(15);  //  줌 레벨
        tmapView.setMapType(TMapView.MAPTYPE_STANDARD);;
        tmapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapGps = new TMapGpsManager(AddressActivity.this);
        tmapGps.setMinTime(1000);
        tmapGps.setMinDistance(5);
        tmapGps.setProvider(tmapGps.NETWORK_PROVIDER);  //  연결된 인터넷으로 현 위치를 받음, 실내에 유용
        //tmapGps.setProvider(tmapGps.GPS_PROVIDER);    //  gps로 현 위치를 잡음

        tmapGps.OpenGps();

        // 화면 중심을 단말의 현 위치로 이동
        tmapView.setTrackingMode(true);
        //tmapView.setSightVisible(true);

        searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(){
                    public void handleMessage(Message msg)
                    {
                        String[] location = msg.getData().getString("Location").split("-");    /// 번들에 들어있는 값 꺼냄
                        locaName = location[0];
                        locaLat = location[1];
                        locaLon = location[2];
                        locaAddr = location[3];
                        selectLat = Double.parseDouble(locaLat);
                        selectLon = Double.parseDouble(locaLon);

                        myHome = new MapPoint(locaName, selectLat, selectLon);
                        showMarkerPoint(myHome);
                        tmapView.setCenterPoint(selectLon, selectLat);
                        residenceName = locaAddr + locaName;

                        okTv.setEnabled(true);
                        okTv.setTextColor(Color.parseColor("#000000"));
                    }};
                convertToAddress(handler);

            }
        });

        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                SharedPreferences saveInfo = getSharedPreferences("loginFlag", MODE_PRIVATE);
                SharedPreferences.Editor editor = saveInfo.edit();
                editor.putString("RESIDENCE", residenceName);
                editor.putString("RESIDENCE_LAT", String.valueOf(selectLat));
                editor.putString("RESIDENCE_LON", String.valueOf(selectLon));
                editor.commit();
                */
                Intent intent = new Intent(AddressActivity.this, num20_Main.class);

                intent.putExtra("RESIDENCE", residenceName);
                intent.putExtra("RESIDENCE_LAT", String.valueOf(selectLat));
                intent.putExtra("RESIDENCE_LON", String.valueOf(selectLon));
                setResult(RESULT_OK, intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();

            }
        });

    }

    public void showMarkerPoint(MapPoint myHome) {
            TMapPoint point = new TMapPoint(myHome.getLat(),
                    myHome.getLon());

            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;

            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);

            item1.setTMapPoint(point);
            item1.setName(myHome.getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);


            // 풍선뷰 안의 항목에 글 지정
            item1.setCalloutTitle(myHome.getName());
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);


            String strId = String.format("pmarker%d", mMarkerID++);

            if(gpsFlag == 0) {
                tmapView.setTrackingMode(false);
                gpsFlag=1;
            }
            tmapView.removeAllMarkerItem();
            tmapView.addMarkerItem(strId, item1);
            mArrayMarkerID.add(strId);

    }

    // 주소검색으로 위도 경도 검색하기
    // 명칭 검색을 통한 주소 변환

    public void convertToAddress(Handler handler) {
        final String addressStr = searchEt.getText().toString();

        TMapData tMapData = new TMapData();
        listAdapter = new MapListItemAdapter(handler);

        tMapData.findAllPOI(addressStr, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                ArrayList<MapListItem> al = new ArrayList<MapListItem>();

                for(int i=0;i<poiItem.size();i++){
                    TMapPOIItem item = poiItem.get(i);

                    String name = item.getPOIName().toString();
                    String address = item.getPOIAddress().replace("null", "");

                    String location = item.getPOIPoint().toString();
                    String[] arrStr = location.split(" ");
                    String strLat = arrStr[1];
                    String strLon = arrStr[3];
                    Log.d("location", strLat+" "+strLon);
                    Double lat = Double.parseDouble(strLat);
                    Double lon = Double.parseDouble(strLon);

                    MapListItem lItem = new MapListItem(name, address, lat, lon);

                    al.add(lItem);
                    listAdapter.add(al.get(i));
                    Log.d("주소로 찾기", "POI Name : "+ item.getPOIName().toString() + ", " +
                            "Address : "+ item.getPOIAddress().replace("null", "") + ", " +
                            "Point : "+ item.getPOIPoint().toString());
                }
            }
        });
        searchEt.setText(null);
        InputMethodManager mInputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);  //  입력 후 키보드 내리기

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                addressView.setAdapter(listAdapter);
            }
        }, 800);
        // 0.8초 정도 딜레이를 준 후 시작

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddressActivity.this, num20_Main.class);
        setResult(RESULT_CANCELED, intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }



}