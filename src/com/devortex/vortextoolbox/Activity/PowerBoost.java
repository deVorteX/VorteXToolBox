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
        
        Button mPBScript = (Button) findViewById(R.id.btnPBMods);
        Button mPBCWR = (Button) findViewById(R.id.btnPBCWR);
        Button mPBThrottle = (Button) findViewById(R.id.btnPBThrottle);
        
        mPBScript.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startPBScriptActivity();				
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
	
	public void startPBScriptActivity()
	{
		Intent i = new Intent(this, PowerBoostScript.class);
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
