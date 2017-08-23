package com.example.a1.himaster.SKPlanet.Tmap;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1.himaster.R;

import java.util.ArrayList;


public class MapListItemAdapter extends BaseAdapter {
    Handler handler;
    Double lat;
    Double lon;
    private ArrayList<MapListItem> mItems;  //리스트아이템을 보관

    public MapListItemAdapter(Handler handler) {  //생성자
        this.handler = handler;
        mItems = new ArrayList<MapListItem>();
    }

    @Override
    public int getCount() {  //현재 아이템의 수를 리턴
        return mItems.size();
    }

    //현재 아이템의 오브젝트를 리턴
    //Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  //출력될 아이템 관리
        final int pos = position;
        final Context context = parent.getContext();

        TextView lvName = null;
        TextView lvAddress = null;
        CustomHolder holder = null;

        //리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 convertView가 null인 상태로 들어옴
        if (convertView == null) {
            //view가 null일 경우 커스텀 레이아웃을 얻어옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.maplistitem, parent, false);

            //textView에 현재 position의 문자열 추가
            lvName = (TextView) convertView.findViewById(R.id.lvName);
            lvAddress = (TextView) convertView.findViewById(R.id.lvAddress);

            //홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.nameText = lvName;
            holder.addressText = lvAddress;
            convertView.setTag(holder);
        }
        else{
            holder = (CustomHolder)convertView.getTag();
            lvName = holder.nameText;
            lvAddress = holder.addressText;
        }

        //텍스트 등록
        lvName.setText(mItems.get(position).getName());
        lvAddress.setText(mItems.get(position).getAddress());

        //리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : " + mItems.get(pos) + " lat : " + mItems.get(pos).getLat().toString() + " lon : " + mItems.get(pos).getLon().toString(), Toast.LENGTH_SHORT).show();
                lat = mItems.get(pos).getLat();
                lon = mItems.get(pos).getLon();

                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("Location", mItems.get(pos).getName() + "-"+ lat + "-" + lon);
                msg.setData(bundle);
                handler.sendMessage(msg);

            }
        });

        //리스트 아이템을 길게 터치 했을 때 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //롱 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : " + mItems.get(pos), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return convertView;
    }

    private class CustomHolder{
        TextView nameText;
        TextView addressText;
    }

    //외부에서 아이템 추가 요청시 사용
    public void add(MapListItem item) {
        mItems.add(item);
    }

    //외부에서 아이템 삭제 요청시 사용
    public void remove(int position) {
        mItems.remove(position);
    }
}