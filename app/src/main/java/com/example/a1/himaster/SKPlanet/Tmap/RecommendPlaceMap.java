package com.example.a1.himaster.SKPlanet.Tmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.example.a1.himaster.R;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

public class RecommendPlaceMap extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

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

    private String address;
    private Double lat = null;
    private Double lon = null;

    private Button bt_find;
    private Button bt_fac;
    private Button searchBtn;
    private EditText searchEt;

    ListView addressView;
    MapListItemAdapter listAdapter;
    String locaName, locaLat, locaLon;


    @Override
    public void onLocationChange(Location location) {
        if(m_bTrackingMode) {
            tmapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendplace);

        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchEt = (EditText)findViewById(R.id.searchEt);
        mContext = this;

        bt_find = (Button)findViewById(R.id.bt_findadd);
        bt_fac = (Button)findViewById(R.id.bt_findfac);
        addressView = (ListView)findViewById(R.id.addressview);


        tMapData = new TMapData();
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.mapview);
        tmapView = new TMapView(this);

        linearLayout.addView(tmapView);
        tmapView.setSKPMapApiKey(mApiKey);

        addPoint();
        showMarkerPoint();


        tmapView.setCompassMode(true);  //  현재 보는 방향

        tmapView.setIconVisibility(true);   //  현 위치 아이콘 표시

        tmapView.setZoomLevel(15);  //  줌 레벨
        tmapView.setMapType(TMapView.MAPTYPE_STANDARD);;
        tmapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapGps = new TMapGpsManager(RecommendPlaceMap.this);
        tmapGps.setMinTime(1000);
        tmapGps.setMinDistance(5);
        tmapGps.setProvider(tmapGps.NETWORK_PROVIDER);  //  연결된 인터넷으로 현 위치를 받음, 실내에 유용
        //tmapGps.setProvider(tmapGps.GPS_PROVIDER);    //  gps로 현 위치를 잡음


        tmapGps.OpenGps();

        // 화면 중심을 단말의 현 위치로 이동
        tmapView.setTrackingMode(true);
        tmapView.setSightVisible(true);

        //풍선에서 우측 버튼 클릭시 할 행동
        tmapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {

                lat = markerItem.latitude;
                lon = markerItem.longitude;

                // 위도 경도로 주소 검색하기
                tMapData.convertGpsToAddress(lat, lon, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String strAddress) {
                        address = strAddress;
                    }
                });
                Toast.makeText(RecommendPlaceMap.this, "주소 : "+ address, Toast.LENGTH_SHORT).show();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(){
                    public void handleMessage(Message msg)
                    {
                        String[] location = msg.getData().getString("Location").split("-");    /// 번들에 들어있는 값 꺼냄
                        locaName = location[0];
                        locaLat = location[1];
                        locaLon = location[2];
                        Double lat = Double.parseDouble(locaLat);
                        Double lon = Double.parseDouble(locaLon);
                        addLocaPoint(locaName, lat, lon);
                        showMarkerPoint();

                    }};
                convertToAddress(handler);

            }
        });

        bt_fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAroundBizPoi();
            }
        });

    }


    public void addPoint() {
        m_mapPoint.add(new MapPoint("강남", 37.510350, 127.066847));
    }

    public void addLocaPoint(String addressName, Double lat, Double lon) {
        m_mapPoint.add(new MapPoint(addressName, lat, lon));
    }

    public void showMarkerPoint() {
        for (int i=0;i<m_mapPoint.size();i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLat(),
                    m_mapPoint.get(i).getLon());

            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;

            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.poi_dot);


            // 풍선뷰 안의 항목에 글 지정
            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle("서울");
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.i_go);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strId = String.format("pmarker%d", mMarkerID++);

            tmapView.addMarkerItem(strId, item1);
            mArrayMarkerID.add(strId);

        }
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
                //여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 800);
        // 0.8초 정도 딜레이를 준 후 시작



    }

    //  주변 편의시설 검색
    //  화면 중심의 위도 경도를 통한 검색
    public void getAroundBizPoi() {
        TMapData tMapData = new TMapData();
        TMapPoint point = tmapView.getCenterPoint();

        tMapData.findAroundNamePOI(point, "지하철", 1, 99,
                new TMapData.FindAroundNamePOIListenerCallback() {

                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                        for(int i=0;i<poiItem.size();i++) {
                            TMapPOIItem item = poiItem.get(i);
                            Log.d("편의시설", "POI Name : "+ item.getPOIName() + ", " + "Address : " +
                                    item.getPOIAddress().replace("null", ""));
                        }
                    }
                });
    }
}