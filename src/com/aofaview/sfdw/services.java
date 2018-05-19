package com.aofaview.sfdw;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption.LocationMode;

import com.baidu.location.BDLocationListener;

import com.baidu.location.LocationClient;

import com.baidu.location.LocationClientOption;

public class services extends Service {

	//public static final String TAG = "MyService";
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private boolean isStop = false;
	private static int delay = 1000; // 1s
	private static int period = 1000; // 1s
	private static String strUrl = "http://192.168.1.113:9191/sf/";
	private LocationClient locationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	public void onCreate() {
		super.onCreate();
		locationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		locationClient.registerLocationListener(myListener); // ע���������

		LocationClientOption option = new LocationClientOption();

		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		
		option.setPriority(LocationClientOption.NetWorkFirst); // ���ö�λ���ȼ�

		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02

		option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ

		option.setNeedDeviceDirect(true);// ���صĶ�λ��������ֻ���ͷ�ķ���

		locationClient.setLocOption(option);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// ������ʱ��
		if (!isStop) {
			//Log.i("K", "��ʼ����");
			startTimer();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {

		//Log.d("Res", "onDestroy");
		locationClient.stop();
		super.onDestroy();
		// ֹͣ��ʱ��
		if (isStop) {
			Log.i("T", "����ֹͣ");
			stopTimer();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * ��ʱ�� ÿ60����ִ��һ��
	 */
	private void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		//Log.i(TAG, "count: " + String.valueOf(count++));
		isStop = true;
		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					//Log.i(TAG, "count: " + String.valueOf(count++));
					do {
						try {
							locationClient.start();
							if (locationClient != null
									&& locationClient.isStarted())

								locationClient.requestLocation();
							else

								//Log.d("LocSDK3","locClient is null or not started");

							Thread.sleep(10000*60*60);//��ͣ1Сʱ
						} catch (InterruptedException e) {
						}
					} while (isStop);
				}
			};
		}

		if (mTimer != null && mTimerTask != null)
			mTimer.schedule(mTimerTask, delay, period);

	}

	private void stopTimer() {

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		isStop = false;

	}

	/*
	 * ��ȡ��λ��γ�� ��Ϣ
	 */
	@SuppressLint("SimpleDateFormat")
	private void getLocationInfo(BDLocation location) {

		//String latLongInfo;
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			//latLongInfo = "Lat:" + lat + "nLong:" + lng;
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			String date = sDateFormat.format(new java.util.Date());
			//Log.d("����", latLongInfo + "," + date);
			TelephonyManager phoneMgr = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);
			//Log.d("", phoneMgr.getLine1Number());
			PostData(lat, lng, date, phoneMgr.getLine1Number());
		} else {
			//latLongInfo = "No location found";
			//Log.d("", latLongInfo);
		}
	}

	/*
	 * ���̨Post��λ����
	 */
	public void PostData(double lat, double lng, String time, String phone) {
		phone = phone.substring(3, phone.length());// ȥ���ֻ���ǰ�� "+86"
		HttpClient httpclient = new DefaultHttpClient();
		// ���URL
		HttpPost httppost = new HttpPost(strUrl);
		final String Json = "{\"VARYLON\":\"" + lng + "\",\"VARYLAT\":\"" + lat
				+ "\",\"SJH\":\"" + phone + "\",\"POSTTIME\":\"" + time + "\"}";
		//Log.d("Json", Json);
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(),Json,Toast.LENGTH_LONG).show();
			}
		});
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			// Your DATA
			nameValuePairs.add(new BasicNameValuePair("SERVICETYPE", "1"));
			nameValuePairs.add(new BasicNameValuePair("OPERTIONTYPE", "142"));
			nameValuePairs.add(new BasicNameValuePair("JSON", Json));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//��ȡ��λ����
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null)

				return;	

			locationClient.stop();
			
			getLocationInfo(location);
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

}
