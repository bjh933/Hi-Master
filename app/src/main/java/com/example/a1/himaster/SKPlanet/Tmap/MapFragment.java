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
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.json.JSONArray;

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

    private Double selectLat = null;
    private Double selectLon = null;

    Double departLat = null;
    Double departLon = null;
    Double destLat = null;
    Double destLon = null;

    private TextView searchDepartTv, searchDestTv;
    private EditText departEt, destEt;
    TextView titleTv;
    HorizontalScrollView horizontalScrollView;
    LinearLayout recoLayout;
    LinearLayout mapView;
    TextView rTv1, rTv2, rTv3, rTv4, rTv5, rTv6, rTv7, myLocaTv;
    String recoCategory = "";
    MapListItem recoFood;
    ListView addressView;
    MapListItemAdapter listAdapter, recoListAdapter;
    String locaName, locaLat, locaLon, locaAddr;
    int gpsFlag = 0;
    JSONArray posts = null;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onLocationChange(Location location) {
        if(m_bTrackingMode) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            tmapView.setCenterPoint(lon, lat);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fifthfragment, container, false);
        myLocaTv = (TextView)view.findViewById(R.id.myLocaTv);
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
        rTv7 = (TextView) view.findViewById(R.id.rTv7);
        rTv1.setOnClickListener(this);
        rTv2.setOnClickListener(this);
        rTv3.setOnClickListener(this);
        rTv4.setOnClickListener(this);
        rTv5.setOnClickListener(this);
        rTv6.setOnClickListener(this);
        rTv7.setOnClickListener(this);
        myLocaTv.setOnClickListener(this);

        titleTv = (TextView) view.findViewById(R.id.titleTv);
        searchDepartTv = (TextView) view.findViewById(R.id.searchDepartTv);
        departEt = (EditText) view.findViewById(R.id.departEt);
        searchDestTv = (TextView) view.findViewById(R.id.searchDestTv);
        destEt = (EditText) view.findViewById(R.id.destEt);
        addressView = (ListView) view.findViewById(R.id.addressview);
        mContext = getActivity();

        tMapData = new TMapData();
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.mapview);
        tmapView = new TMapView(mContext);
        tmapView.setSKPMapApiKey(mApiKey);
        tmapView.setZoomLevel(15);  //  줌 레벨
        tmapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        searchDepartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 800);
                mapView.setLayoutParams(params);
                horizontalScrollView.setVisibility(View.GONE);
                recoLayout.setVisibility(View.GONE);
                recoCategory = "";
                addressView.setVisibility(View.VISIBLE);

                Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        String[] location = msg.getData().getString("Location").split("-");    /// 번들에 들어있는 값 꺼냄
                        locaName = location[0];
                        locaLat = location[1];
                        locaLon = location[2];
                        locaAddr = location[3];
                        selectLat = Double.parseDouble(locaLat);
                        selectLon = Double.parseDouble(locaLon);
                        departLat = selectLat;
                        departLon = selectLon;
                        myHome = new MapPoint(locaName, selectLat, selectLon);
                        showMarkerPoint(myHome);
                        tmapView.setCenterPoint(selectLon, selectLat);
                        tmapView.setTrackingMode(false);
                        tmapView.setCompassMode(false);
                    }
                };
                recoTvReset();
                horizontalScrollView.scrollTo(0, 0);
                convertToAddress(handler);

            }
        });

        searchDestTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 600);
                mapView.setLayoutParams(params);
                horizontalScrollView.setVisibility(View.GONE);
                recoLayout.setVisibility(View.GONE);
                recoCategory = "";
                addressView.setVisibility(View.VISIBLE);

                Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        String[] location = msg.getData().getString("Location").split("-");    /// 번들에 들어있는 값 꺼냄
                        locaName = location[0];
                        locaLat = location[1];
                        locaLon = location[2];
                        locaAddr = location[3];
                        selectLat = Double.parseDouble(locaLat);
                        selectLon = Double.parseDouble(locaLon);
                        destLat = selectLat;
                        destLon = selectLon;
                        myHome = new MapPoint(locaName, selectLat, selectLon);
                        showMarkerPoint(myHome);
                        tmapView.setCenterPoint(selectLon, selectLat);
                        getAroundPlace();
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        recoLayout.setVisibility(View.VISIBLE);
                        tmapView.setTrackingMode(false);
                        tmapView.setCompassMode(false);
                    }
                };
                recoTvReset();
                horizontalScrollView.scrollTo(0, 0);
                convertToDestAddress(handler);

            }
        });

        titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( (departLat != null && departLon != null) && (destLat != null && destLon != null) )
                {
                    tmapView.setTrackingMode(false);
                    tmapView.setCompassMode(false);
                    tmapView.setCenterPoint(departLon, departLat);
                    TMapPoint point1 = new TMapPoint(departLat, departLon);
                    TMapPoint point2 = new TMapPoint(destLat, destLon);
                    tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine polyLine) {
                            tmapView.addTMapPath(polyLine);
                        }
                    });
                }
                horizontalScrollView.setVisibility(View.GONE);
                recoLayout.setVisibility(View.GONE);
                addressView.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mapView.setLayoutParams(params);
            }
        });

        linearLayout.addView(tmapView);


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
        final String addressStr = departEt.getText().toString();

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
        //departEt.setText(null);
        InputMethodManager mInputMethodManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(departEt.getWindowToken(), 0);  //  입력 후 키보드 내리기

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

    public void convertToDestAddress(Handler handler) {
        final String addressStr = destEt.getText().toString();

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
        //destEt.setText(null);
        InputMethodManager mInputMethodManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(destEt.getWindowToken(), 0);  //  입력 후 키보드 내리기

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

    public void getAroundPlace() {
        TMapData tMapData = new TMapData();
        TMapPoint point = new TMapPoint(myHome.getLat(),
                myHome.getLon());

            if(locaAddr.contains("서울") || locaAddr.contains("경기")) {
                Handler recoHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        String[] location = msg.getData().getString("Location").split("-");    /// 번들에 들어있는 값 꺼냄
                        locaName = location[0];
                        locaLat = location[1];
                        locaLon = location[2];
                        locaAddr = location[3];
                        selectLat = Double.parseDouble(locaLat);
                        selectLon = Double.parseDouble(locaLon);
                        destLat = selectLat;
                        destLon = selectLon;
                        myHome = new MapPoint(locaName, selectLat, selectLon);
                        showMarkerPoint(myHome);
                        tmapView.setCenterPoint(selectLon, selectLat);

                    }
                };

                recoListAdapter = new MapListItemAdapter(recoHandler);

                if (recoCategory.equals("식음료")) {
                    final ArrayList<MapListItem> recoList = new ArrayList<MapListItem>();

                    tMapData.findAroundNamePOI(point, "한식;중식;일식;치킨;피자", 2, 30,
                            new TMapData.FindAroundNamePOIListenerCallback() {

                                @Override
                                public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                                    for (int i = 0; i < poiItem.size(); i++) {
                                        TMapPOIItem item = poiItem.get(i);

                                        Double recoLat = item.getPOIPoint().getLatitude();
                                        Double recoLon = item.getPOIPoint().getLongitude();

                                        recoFood = new MapListItem(item.getPOIName(),
                                                item.getPOIAddress().replace("null", ""), recoLat, recoLon);

                                        recoList.add(recoFood);
                                        recoListAdapter.add(recoList.get(i));

                                        Log.d("recoPlace", recoCategory + " : " + item.getPOIName() + ", " + "Address : " +
                                                item.getPOIAddress().replace("null", ""));
                                    }
                                }

                            });


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addressView.setAdapter(recoListAdapter);
                        }
                    }, 800);
                    // 0.8초 정도 딜레이를 준 후 시작

                }   //

                else if(recoCategory.equals("병원"))
                {

                    final ArrayList<MapListItem> recoList = new ArrayList<MapListItem>();
                    tMapData.findAroundNamePOI(point, "외과;치과;내과;안과;소아과;", 2, 30,
                            new TMapData.FindAroundNamePOIListenerCallback() {

                                @Override
                                public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                                    for (int i = 0; i < poiItem.size(); i++) {
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

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addressView.setAdapter(recoListAdapter);
                        }
                    }, 800);
                    // 0.8초 정도 딜레이를 준 후 시작
                }

                else if(!recoCategory.equals("")){

                    final ArrayList<MapListItem> recoList = new ArrayList<MapListItem>();
                    tMapData.findAroundNamePOI(point, recoCategory, 2, 30,
                            new TMapData.FindAroundNamePOIListenerCallback() {

                                @Override
                                public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                                    for (int i = 0; i < poiItem.size(); i++) {
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

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                recoCategory = "카페";
                rTv1.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                rTv7.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace();
                break;

            case R.id.rTv2 :
                recoCategory = "식음료";
                rTv2.setTextColor(Color.parseColor("#BC0003"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                rTv7.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace();
                break;

            case R.id.rTv3 :
                recoCategory = "영화관";
                rTv3.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                rTv7.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace();
                break ;

            case R.id.rTv4 :
                recoCategory = "쇼핑";
                rTv4.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                rTv7.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace();
                break ;

            case R.id.rTv5 :
                recoCategory = "숙박";
                rTv5.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                rTv7.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace();
                break ;

            case R.id.rTv6 :
                recoCategory = "공원";
                rTv6.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv7.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace();
                break ;

            case R.id.rTv7 :
                recoCategory = "병원";
                rTv7.setTextColor(Color.parseColor("#BC0003"));
                rTv2.setTextColor(Color.parseColor("#BABABA"));
                rTv3.setTextColor(Color.parseColor("#BABABA"));
                rTv1.setTextColor(Color.parseColor("#BABABA"));
                rTv5.setTextColor(Color.parseColor("#BABABA"));
                rTv4.setTextColor(Color.parseColor("#BABABA"));
                rTv6.setTextColor(Color.parseColor("#BABABA"));
                getAroundPlace();
                break ;

            case R.id.myLocaTv :

                tmapGps = new TMapGpsManager(mContext);
                tmapGps.setMinTime(1000);
                tmapGps.setMinDistance(5);
                tmapGps.setProvider(tmapGps.NETWORK_PROVIDER);  //  연결된 인터넷으로 현 위치를 받음, 실내에 유용
                // tmapGps.setProvider(tmapGps.GPS_PROVIDER);
                tmapGps.OpenGps();

                tmapView.setCompassMode(true);
                tmapView.setIconVisibility(true);   //  현 위치 아이콘 표시
                tmapView.setTrackingMode(true);
                tmapView.setSightVisible(true);
                Log.d("gps", String.valueOf(tmapGps.setLocationCallback()));
                break ;

        }
    }
}