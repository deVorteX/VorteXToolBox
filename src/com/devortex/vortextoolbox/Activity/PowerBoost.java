package com.devortex.vortextoolbox.Activity;

import com.devortex.vortextoolbox.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PowerBoost extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.powerboost);
        
        Button mGovernors = (Button) findViewById(R.id.btnGovernors);
        Button mPBCWR = (Button) findViewById(R.id.btnPBCWR);
        Button mPBThrottle = (Button) findViewById(R.id.btnPBThrottle);
        
        mGovernors.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startGovernorsActivity();				
			}
		});
        
        mPBCWR.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startPBCWRActivity();
			}
		});
        
        mPBThrottle.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startPBThrottleActivity();
			}
		});
	}
	
	public void startGovernorsActivity()
	{
		Intent i = new Intent(this, Governors.class);
		startActivity(i);
	}
	
	public void startPBCWRActivity()
	{
		Intent i = new Intent(this, PowerBoostCWR.class);
		startActivity(i);
	}
	
	public void startPBThrottleActivity()
	{
		Intent i = new Intent(this, ThrottleSelector.class);
		startActivity(i);
	}
}
