package com.ase.myapp;

import android.content.Context;
import android.os.Message;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;

public interface Contract {
    interface View{
        void PoiResearch(Message msg);
       // void loginResponse(Message msg);
       // void jump();
    }
    interface Model{
        void startPoiSearch(Context context,String city,String n);
       // void login(String a,String p);
    }
}
