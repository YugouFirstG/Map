package com.ase.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class InerActivity extends AppCompatActivity {
    private BikeNavigateHelper navigateHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigateHelper = BikeNavigateHelper.getInstance();
        View view = navigateHelper.onCreate(InerActivity.this);
        if (view != null) {
            setContentView(view);
        }
        navigateHelper.startBikeNavi(InerActivity.this);
        Log.d("InerAct", "ineract");
//        textView.setText(intent.getStringExtra("search"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigateHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigateHelper.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigateHelper.quit();
    }
}
