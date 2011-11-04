package com.devortex.vortextoolbox.Activity;

import java.io.IOException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.devortex.vortextoolbox.*;
import com.devortex.vortextoolbox.helper.*;
import com.koushikdutta.rommanager.api.IROMManagerAPIService;

public class VorteXToolBox extends Activity {
	private Context _context = VorteXToolBox.this;
    public static IROMManagerAPIService mService = null;
    boolean mBound = false;
    Intent RomManagerIntent = null;
    
    public static IROMManagerAPIService getService()
    {
    	return mService;
    }
    
    public ServiceConnection mConnection = new ServiceConnection() {
    	public void onServiceDisconnected(ComponentName className) {
    		mBound = false;
    	}
    	public void onServiceConnected(ComponentName name, IBinder service) {
    		mService = IROMManagerAPIService.Stub.asInterface(service);
    		mBound = true;
    	}
    };
    
    @Override
    protected void onStart() {
    	super.onStart();
    	RomManagerIntent = new Intent("com.koushikdutta.rommanager.api.BIND");
    	bindService(RomManagerIntent, mConnection, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	if (mBound) {
    		unbindService(mConnection);
    		mBound = false;
    	}
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	if (mBound) {
    		unbindService(mConnection);
    		mBound = false;
    	}
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (!mBound) {
    		bindService(RomManagerIntent, mConnection, Context.BIND_AUTO_CREATE);
    		mBound = true;
    	}
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		Assests.unzipAssets(_context);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button mChangeCarrierTextButton = (Button) findViewById(R.id.btnCarrierText);
        Button mOnePercentButton = (Button) findViewById(R.id.btnBatteryIcons);
        Button mStartupTweaks = (Button) findViewById(R.id.btnStartup);
        Button mPowerBoost = (Button) findViewById(R.id.btnPowerBoost);
        Button mCalibrateBatt = (Button) findViewById(R.id.btnCalibrateBatt);
        
     // Register handler for UI elements
        mChangeCarrierTextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	launchCarrierTextEdit();
            }
        });
        mOnePercentButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					startHack();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
        mStartupTweaks.setOnClickListener(new View.OnClickListener() {
        				
			public void onClick(View v) {
				launchStartupTweaksActivity();				
			}
		});
        mPowerBoost.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				launchPowerBoostActivity();
			}
		});
        mCalibrateBatt.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				calibrateBattery();
			}
		});
    }
	
	protected void launchCarrierTextEdit()
    {
    	Intent i = new Intent(this, CarrierText.class);
        startActivity(i);
    }
	
	protected void launchStartupTweaksActivity() {
		try {
		Intent i = new Intent(this, StartupScript.class);
        startActivity(i);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	protected void launchPowerBoostActivity()
    {
    	Intent i = new Intent(this, PowerBoost.class);
        startActivity(i);
    }
	
	protected void calibrateBattery()
    {
    	commandRunner.calibrateBattery(_context);
    	Toast toast = Toast.makeText(_context, "Battery Calibration Complete...", Toast.LENGTH_SHORT);
    	toast.show();
    }
	
	protected void startHack() throws IOException, InterruptedException
	{
		try {
			Intent i = new Intent(this, BatteryIconSelector.class);
	        startActivity(i);
			}
			catch (Exception e){
				e.printStackTrace();
			}
	}

}