package com.example.a1.himaster.SKPlanet.Tmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a1.himaster.R;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

public class MapFragment extends Fragment implements TMapGpsManager.onLocationChangedCallback, View.OnClickListener {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapData tMapData = null;
    private TMapGpsManager tmapGps = null;
    private TMapView tmapView = null;
    private static String mApiKey = "b42a1814-4abc-36c2-a743-43c5f81cd73d";
    private static int mMarkerID;

    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private MapPoint myHome;

    private String subwayName = null;
    private Double subwayLat = null;
    private Double subwayLon = null;
    private Double selectLat = null;
    private Double selectLon = null;

    private TextView searchTv, okTv;
    private EditText searchEt;
    TextView titleTv;
    String residenceName = "";
    String destinationName = "";

    HorizontalScrollView horizontalScrollView;
    LinearLayout recoLayout;
    LinearLayout mapView;
    TextView rTv1, rTv2, rTv3, rTv4, rTv5, rTv6;
    String recoCategory = "";

    ListView addressView;
    MapListItemAdapter listAdapter, recoListAdapter;
    String locaName, locaLat, locaLon, locaAddr;
    int gpsFlag = 0;

    @Override
    public void onLocationChange(Location location) {
        if(m_bTrackingMode) {
            tmapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.destact, container, false);

        mapView = (LinearLayout) view.findViewById(R.id.mapview);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
        horizontalScrollView.setVisibility(View.GONE);
        recoLayout = (LinearLayout) view.findViewById(R.id.recoLayout);
        recoLayout.setVisibility(View.GONE);
        rTv1 = (TextView) view.findViewById(R.id.rTv1);
        rTv2 = (TextView) view.findViewById(R.id.rTv2);
        rTv3 = (TextView) view.findViewById(R.id.rTv3);
        rTv4 = (TextView) view.findViewById(R.id.rTv4);
        rTv5 = (TextView) view.findViewById(R.id.rTv5);
        rTv6 = (TextView) view.findViewById(R.id.rTv6);
        rTv1.setOnClickListener(this);
        rTv2.setOnClickListener(this);
        rTv3.setOnClickListener(this);
        rTv4.setOnClickListener(this);
        rTv5.setOnClickListener(this);
        rTv6.setOnClickListener(this);

        titleTv = (TextView) view.findViewById(R.id.titleTv);
        searchTv = (TextView) view.findViewById(R.id.searchTv);
        okTv = (TextView) view.findViewById(R.id.okTv);
        searchEt = (EditText) view.findViewById(R.id.searchEt);
        addressView = (ListView) view.findViewById(R.id.addressview);
        mContext = getContext();
        okTv.setEnabled(false);
        okTv.setTextColor(Color.parseColor("#DDDDDD"));

        tMapData = new TMapData();
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.mapview);
        tmapView = new TMapView(mContext);

        linearLayout.addView(tmapView);
        tmapView.setSKPMapApiKey(mApiKey);

        tmapView.setIconVisibility(true);   //  현 위치 아이콘 표시

        tmapView.setZoomLevel(15);  //  줌 레벨
        tmapView.setMapType(TMapView.MAPTYPE_STANDARD);
        ;
        tmapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapGps = new TMapGpsManager(mContext);
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
                Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
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
                        destinationName = locaAddr + locaName;
                        getAroundPlace(1);
                        okTv.setEnabled(true);
                        okTv.setTextColor(Color.parseColor("#000000"));


                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, 800);
                        mapView.setLayoutParams(params);
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        recoLayout.setVisibility(View.VISIBLE);


                    }
                };
                recoTvReset();
                horizontalScrollView.scrollTo(0, 0);
                convertToAddress(handler);

            }
        });

        return view;
    }

    public void showMarkerPoint(MapPoint myHome) {
            TMapPoint point = new TMapPoint(myHome.getLat(),
                    myHome.getLon());

            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            item1.setAutoCalloutVisible(true);
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
            item1.setAutoCalloutVisible(true);

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
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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

    public void getAroundPlace(int flag) {
        TMapData tMapData = new TMapData();
        TMapPoint point = new TMapPoint(myHome.getLat(),
                myHome.getLon());

        if(flag == 1)
        {
            if(locaAddr.contains("서울") || locaAddr.contains("경기"))
            {
                tMapData.findAroundNamePOI(point, "지하철", 5, 99,
                    new TMapData.FindAroundNamePOIListenerCallback() {

                        @Override
                        public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                                TMapPOIItem item = poiItem.get(0);
                                Log.d("근처 지하철역", "POI Name : "+ item.getPOIName() + ", " + "Address : " +
                                        item.getPOIAddress().replace("null", ""));
                                subwayName = item.getPOIName();
                                subwayLat = item.getPOIPoint().getLatitude();
                                subwayLon = item.getPOIPoint().getLongitude();
                            }

                    });
            }
        }

        else if(flag == 2)  //  일반 장소추천
        {
            if(locaAddr.contains("서울") || locaAddr.contains("경기"))
            {
                Handler recoHandler = new Handler() {
                    public void handleMessage(Message msg) {
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
                        destinationName = locaAddr + locaName;
                        getAroundPlace(1);
                    }
                };

                recoListAdapter = new MapListItemAdapter(recoHandler);
                final ArrayList<MapListItem> recoList = new ArrayList<MapListItem>();
                tMapData.findAroundNamePOI(point, recoCategory, 1, 50,
                        new TMapData.FindAroundNamePOIListenerCallback() {

                            @Override
                            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                                for(int i=0;i<poiItem.size();i++) {
                                    TMapPOIItem item = poiItem.get(i);

                                    Double recoLat = item.getPOIPoint().getLatitude();
                                    Double recoLon = item.getPOIPoint().getLongitude();

                                    MapListItem recoItem = new MapListItem(item.getPOIName(),
                                            item.getPOIAddress().replace("null", ""), recoLat, recoLon);

                                    recoList.add(recoItem);
                                    recoListAdapter.add(recoList.get(i));

                                    Log.d("recoPlace", recoCategory + " : " + item.getPOIName() + ", " + "Address : " +
                                            item.getPOIAddress().replace("null", ""));
                                }
                            }

                        });

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        addressView.setAdapter(recoListAdapter);
                    }
                }, 800);
                // 0.8초 정도 딜레이를 준 후 시작
            }
        }
    }


    public void recoTvReset()
    {
        rTv1.setTextColor(Color.parseColor("#BABABA"));
        rTv2.setTextColor(Color.parseColor("#BABABA"));
        rTv3.setTextColor(Color.parseColor("#BABABA"));
        rTv4.setTextColor(Color.parseColor("#BABABA"));
        rTv5.setTextColor(Color.parseColor("#BABABA"));
        rTv6.setTextColor(Color.parseColor("#BABABA"));
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.rTv1 :
                recoCategory = "식음료";
                rTv1.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace(2);
                break;

            case R.id.rTv2 :
                recoCategory = "놀거리";
                rTv2.setTextColor(Color.parseColor("#BC0003"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace(2);
                break;

            case R.id.rTv3 :
                recoCategory = rTv3.getText().toString();
                rTv3.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace(2);
                break ;

            case R.id.rTv4 :
                recoCategory = rTv4.getText().toString();
                rTv4.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace(2);
                break ;

            case R.id.rTv5 :
                recoCategory = rTv5.getText().toString();
                rTv5.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace(2);
                break ;

            case R.id.rTv6 :
                recoCategory = "공원";
                rTv6.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace(2);
                break ;

        }
    }
}