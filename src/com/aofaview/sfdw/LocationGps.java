package com.aofaview.sfdw;

import android.app.Application;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

public class LocationGps extends Application {

    public LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    public GpsBean bean =new GpsBean();
    public NotifyLister mNotifyer=null;
    public Vibrator mVibrator01;

    @Override
    public void onCreate() {
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener( myListener );
        super.onCreate(); 
        Log.d("GPS", "... Application onCreate... pid=");
    }

    /**
     * 监听函数，获取新位置信息
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.d("GPS","2124");
            if (location == null)
                return ;            
            bean.lat =location.getLatitude();
            bean.lng=location.getLongitude();
            Log.d("GPS", ""+bean.lat+"||"+bean.lat);
        }

        public void onReceivePoi(BDLocation location) {
            if (location == null){
                return ;
            }
            bean.lat =location.getLatitude();
            bean.lng=location.getLongitude();
        }

    }

    /**
     * 位置提醒回调函数
     */
    public class NotifyLister extends BDNotifyListener{
        public void onNotify(BDLocation mlocation, float distance){
            mVibrator01.vibrate(1000);
        }
    }
}