package com.aofaview.sfdw;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button startService;  	  
    private Button stopService;  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent startIntent = new Intent(MainActivity.this, services.class);  
        startService(startIntent);  
        finish();
        setContentView(R.layout.activity_main);
        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button)findViewById(R.id.stop_service);
        
        startService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startIntent = new Intent(MainActivity.this, services.class);  
	            startService(startIntent);  
			}
		});
        stopService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent stopIntent = new Intent(MainActivity.this, services.class);  
	            stopService(stopIntent);  
			}
		});
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
