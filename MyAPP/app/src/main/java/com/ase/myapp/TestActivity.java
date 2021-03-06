package com.ase.myapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.daobao.asus.dbbaseframe.mvp.view.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import static android.widget.Toast.LENGTH_SHORT;

public class TestActivity extends BaseActivity<Testpresenter> implements Contract.View, View.OnClickListener, OnGetGeoCoderResultListener, OnGetRoutePlanResultListener {
    public LocationClient locationClient;
    private boolean isFristLoctation=true;
    private MapView mapView;
    private BaiduMap baiduMap;
//    private GeoCoder mSearch;
    private RoutePlanSearch search;
    private LatLng point,ePoint;
    private float myCurrentX=-1;
    private  MyOrientationListener myOrientationListener;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton floatingActionButton1;
    private ImageButton imageButton;
    private EditText editText,editText1;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker=null;
    private LatLng latLng;
    private List<CityInfo> cityInfoList;
    private BikeNaviLaunchParam param;
    private String edtext,edtext1;
    @Override
    public Testpresenter binPresenter() {
        return new Testpresenter(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.map);
        mapView=findViewById(R.id.bmapView);
        floatingActionButton=findViewById(R.id.flbt);
        floatingActionButton1=findViewById(R.id.flbt1);
        imageButton=findViewById(R.id.route);
        imageButton.setOnClickListener(this);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        askforacc();
        initLocation();
        editText=findViewById(R.id.city);
        editText1=findViewById(R.id.district);
//        mSearch=GeoCoder.newInstance();
        search=RoutePlanSearch.newInstance();//路线实例
        search.setOnGetRoutePlanResultListener(this);
//        point=new LatLng(40.047416,116.312143);
//        ePoint=new LatLng(40.048424, 116.313513);
//        mSearch.setOnGetGeoCodeResultListener(this);
        //骑行导航
//        BitmapDescriptor bitmap=BitmapDescriptorFactory.fromResource(R.drawable.bb);
//        OverlayOptions options=new MarkerOptions()
//                .position(point)
//                .icon(bitmap)
//                .draggable(true);
//        baiduMap.addOverlay(options);
//        Marker marker=(Marker) baiduMap.addOverlay(options);
//        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDrag(Marker marker) {
//                Toast.makeText(TestActivity.this,"moving",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                Toast.makeText(TestActivity.this,"stop",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//                Toast.makeText(TestActivity.this,"start",Toast.LENGTH_SHORT).show();
//            }
//        });
        myOrientationListener=new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(x -> myCurrentX=x);
}
    private void initLocation() {
        locationClient=new LocationClient(getApplicationContext());
        MyLocationListener myLocationListener=new MyLocationListener();
        LocationClientOption option=new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setIsNeedAltitude(true);
        option.setIsNeedLocationDescribe(true);
        option.setCoorType("gcj02");
        option.setNeedDeviceDirect(true);
        option.setIsNeedLocationPoiList(true);
        option.setScanSpan(3000);
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(myLocationListener);
    }

    @Override
    public void PoiResearch(Message msg) {
        MyPoiOverlay poiOverlay=new MyPoiOverlay(baiduMap);
        poiOverlay.setData((PoiResult) msg.obj);
        baiduMap.setOnMarkerClickListener(poiOverlay);
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();

    }

    @Override
    public void PoiResearchD(Message msg) {
        ePoint= (LatLng) msg.obj;
    }

    @Override
    public void DisplayRoute(Message msg) {

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        BikingRouteOverlay overlay=new BikingRouteOverlay(baiduMap);
        if(bikingRouteResult.getRouteLines().size()>0){
            overlay.setData(bikingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
        }

    }

    private class MyLocationListener  implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
                LatLng latLng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                point=latLng;
                if(isFristLoctation){
                    MapStatusUpdate update=MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(update);
                    MapStatusUpdate update1=MapStatusUpdateFactory.zoomTo(16f);
                    isFristLoctation=false;
                }
                MyLocationData.Builder builder=new MyLocationData.Builder();
                builder.longitude(bdLocation.getLongitude());
                builder.latitude(bdLocation.getLatitude());
                builder.accuracy(10);
                builder.direction(myCurrentX);
                MyLocationData data=builder.build();
            baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode,
                    true,mCurrentMarker,
                    0xFFA4F198,
                    0xFFA4F198));
                baiduMap.setMyLocationData(data);
                editText.setText(bdLocation.getCity());
        }
    }

    private  void addMyLocation(){
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        baiduMap.clear();
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background);
        OverlayOptions overlayOptions=new MarkerOptions()
                .position(pt1)
                .icon(bitmapDescriptor);
        baiduMap.addOverlay(overlayOptions);

    }


    private void addCircleOverlay(LatLng latLng){
        CircleOptions circleOptions=new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.fillColor(0xFFA4F198);
        circleOptions.radius(25);
        circleOptions.stroke(new Stroke(5,0xFFA4F198));
        baiduMap.addOverlay(circleOptions);
    }
//访问权限
    private void askforacc() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(TestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(TestActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(TestActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(TestActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(TestActivity.this,permissions,1);
        }else{
            Log.d("Testa","requestPosition");
//            mPresenter.requestPosition(locationClient);
//            bt.setOnClickListener(this);
            floatingActionButton.setOnClickListener(this);
            floatingActionButton1.setOnClickListener(this);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ACT","destroy");
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        search.destroy();
        myOrientationListener.stop();
        Intent intent=new Intent(TestActivity.this,LocationService.class);
        stopService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myOrientationListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ACT","pause");
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ACT","resume");
        mapView.onResume();
    }
    //    @Override
//    public void PositionResponse(Message message) {
//        baiduMap.animateMapStatus((MapStatusUpdate) message.obj);
////        pTv.setText((CharSequence) message.obj);
//        Log.d("Testa","s");
//    }

//    @Override
//    public void DispMyLocation(Message message) {
//        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode,
//                true,mCurrentMarker,
//                0xFFA4F198,
//                0xFFA4F198));
//        baiduMap.setMyLocationData((MyLocationData) message.obj);
        ;
//        LatLng lng=new LatLng(((MyLocationData) message.obj).latitude,((MyLocationData) message.obj).longitude);
//        addCircleOverlay(lng);


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意才能使用",LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }else{
                    Toast.makeText(this,"未知错误",LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.flbt:
//                param=new BikeNaviLaunchParam().stPt(point).endPt(ePoint).vehicle(0);
//                BikeNavigateHelper.getInstance().initNaviEngine(TestActivity.this, new IBEngineInitListener() {
//                    @Override
//                    public void engineInitSuccess() {
//
//                    }
//                    @Override
//                    public void engineInitFail() {
//
//                    }
//                });
//                BikeNavigateHelper.getInstance().routePlanWithParams(param, new IBRoutePlanListener() {
//                    @Override
//                    public void onRoutePlanStart() {
//
//                    }
//                    @Override
//                    public void onRoutePlanSuccess() {
//                        Log.d("onRoutePlanSuccess","算路成功");
//                        Intent intent=new Intent(TestActivity.this,InerActivity.class);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onRoutePlanFail(BikeRoutePlanError bikeRoutePlanError) {
//                        Log.d("onRoutePlanFail","算路失败");
//                    }
//                });
                isFristLoctation=true;
                myOrientationListener.start();
                locationClient.start();
                break;
            case R.id.flbt1:
                baiduMap.clear();
                Log.d("方向",""+myCurrentX);
//                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
                edtext=editText.getText().toString();
                edtext1=editText1.getText().toString();
                mPresenter.startPoiSearch(TestActivity.this,edtext,edtext1,null);
//                mSearch.geocode(new GeoCodeOption().city(edtext).address(edtext1));
                break;
            case R.id.route:
                baiduMap.clear();
                PlanNode stNode = PlanNode.withLocation(point);
                PlanNode enNode = PlanNode.withLocation(ePoint);
                search.bikingSearch((new BikingRoutePlanOption()).from(stNode)
                        .to(enNode).ridingType(0));
                default:
                    break;
        }
    }
//地址编码（地址经纬度互转）
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if(geoCodeResult==null||geoCodeResult.error!=SearchResult.ERRORNO.NO_ERROR){
            Toast.makeText(TestActivity.this,"检索失败",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(TestActivity.this, "检索结果为" + geoCodeResult.getLocation(), Toast.LENGTH_LONG).show();
        }
//        mSearch.destroy();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if(reverseGeoCodeResult==null||reverseGeoCodeResult.error!=SearchResult.ERRORNO.NO_ERROR){
            Toast.makeText(TestActivity.this,"检索失败",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(TestActivity.this, "检索结果为" + reverseGeoCodeResult.getAddress(), Toast.LENGTH_SHORT).show();
        }
//        mSearch.destroy();
    }


    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            super.onPoiClick(i);
            PoiInfo poiInfo=getPoiResult().getAllPoi().get(i);
            edtext=editText.getText().toString();
            edtext1=editText1.getText().toString();
            mPresenter.startPoiSearch(TestActivity.this,edtext,edtext1,poiInfo);
            return true;
        }
    }
}