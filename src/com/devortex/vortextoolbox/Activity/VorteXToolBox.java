package com.devortex.vortextoolbox.Activity;

import java.io.IOException;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import com.devortex.vortextoolbox.*;
import com.devortex.vortextoolbox.helper.*;
import com.koushikdutta.rommanager.api.IROMManagerAPIService;

public class VorteXToolBox extends Activity {
	private Context _context = VorteXToolBox.this;
    public static IROMManagerAPIService mService = null;
    
    public static IROMManagerAPIService getService()
    {
    	return mService;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		Intent i = new Intent("com.koushikdutta.rommanager.api.BIND");
		try {
			bindService(i, new ServiceConnection() {
				public void onServiceDisconnected(ComponentName name){
					
				}
				public void onServiceConnected(ComponentName name, IBinder service) {
					mService = IROMManagerAPIService.Stub.asInterface(service);
				}								
			}, Service.BIND_AUTO_CREATE);
		}
		catch (Exception e) {}
		
		Assests.unzipAssets(_context);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button mChangeCarrierTextButton = (Button) findViewById(R.id.btnCarrierText);
        Button mOnePercentButton = (Button) findViewById(R.id.btnBatteryIcons);
        Button mStartupTweaks = (Button) findViewById(R.id.btnStartup);
        Button mPowerBoost = (Button) findViewById(R.id.btnPowerBoost);
        
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