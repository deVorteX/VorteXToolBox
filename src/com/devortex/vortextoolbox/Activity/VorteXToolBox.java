package com.devortex.vortextoolbox.Activity;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devortex.vortextoolbox.*;
import com.devortex.vortextoolbox.helper.*;
import com.koushikdutta.rommanager.api.IROMManagerAPIService;

public class VorteXToolBox extends Activity {
	public static final String PREF_FILE_NAME = "vortextoolbox";
	public static final String PREF_SHOW_MODEL_WARNING = "showDeviceWarning";
	public static final String PREF_CWR_INSTALLED = "rommanagerInstalled";
	public StateSaver ss;
	private Context _context = VorteXToolBox.this;
	private static enum rmMessage { NOROMMANAGER, NOCWRSET, ROMMANAGEROK };
	
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
        	doPreChecks();
    	}
    };
    
    @Override
    protected void onStart() {
    	super.onStart();
    	if (!mBound)
    	{
		    bindService(RomManagerIntent, mConnection, Context.BIND_AUTO_CREATE);
    	}
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
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.restart_toolbox:
    		ss.setRMAlreadyWarned(false);
    		ss.setShowRMStatus(true);
    		startActivity(getIntent());
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	ss = ((StateSaver)getApplicationContext());
    	
    	RomManagerIntent = new Intent("com.koushikdutta.rommanager.api.BIND");
	    bindService(RomManagerIntent, mConnection, Context.BIND_AUTO_CREATE);
	    
    	Assests.unzipAssets(_context);
    	
    	boolean setCustomTitle = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		        
        setContentView(R.layout.main);
        
        if (setCustomTitle)
        {
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
			final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
	        if ( myTitleText != null ) {
	            myTitleText.setText(getTitle(_context));
	        }
        }
        
        Button mChangeCarrierTextButton = (Button) findViewById(R.id.btnCarrierText);
        Button mOnePercentButton = (Button) findViewById(R.id.btnBatteryIcons);
        Button mStartupTweaks = (Button) findViewById(R.id.btnVorteXStartup);
        Button mPowerBoost = (Button) findViewById(R.id.btnPowerBoost);
        Button mCalibrateBatt = (Button) findViewById(R.id.btnCalibrateBatt);
        Button mFixRecovery = (Button) findViewById(R.id.btnFixCWR);
        Button mSetGovernor = (Button) findViewById(R.id.btnSetGov);
        Button mSetInstallLocation = (Button) findViewById(R.id.btnInstallLocation);
        
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
        mFixRecovery.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		fixCWR();
        	}
        });
        mSetGovernor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				launchSetGovernorActivity();				
			}
		});
        mSetInstallLocation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				launchSetInstallLocationActivity();
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
	
	protected void launchSetGovernorActivity()
    {
    	Intent i = new Intent(this, Governors.class);
        startActivity(i);
    }
	
	protected void launchSetInstallLocationActivity()
	{
		Intent i = new Intent(this, AppInstallLocation.class);
		startActivity(i);
	}
	
	public static String getTitle(Context context)
	{
		String app_ver = null;
		try
		{
		    app_ver = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		}
		catch (NameNotFoundException e)
		{
		    
		}
		return String.format("%s: %s", context.getString(R.string.app_name), app_ver);
	}
	
	protected void calibrateBattery()
    {
		String batteryPercent = commandRunner.retrieveSingleCommandLineReturnLine("cat /sys/class/power_supply/battery/charge_counter");
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
 	   	builder.setMessage(String.format(getString(R.string.calibrate_battery_notice), batteryPercent))
 	          	.setCancelable(false)
 	          	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
 	          		public void onClick(DialogInterface dialog, int id) {
 	          			commandRunner.calibrateBattery(_context);
 	          	    	Toast toast = Toast.makeText(_context, "Battery Calibration Complete...", Toast.LENGTH_SHORT);
 	          	    	toast.show();
 	          		}
 	          	})
 	          	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 	          		public void onClick(DialogInterface dialog, int id) {
 	          			
 	          		}
 	          	});
 	   	AlertDialog alert = builder.create();
 	   	alert.show();
    }
	protected void fixCWR()
	{
		commandRunner.fixCWR(_context);
		Toast toast = Toast.makeText(_context, "Fixed Always booting to CWR", Toast.LENGTH_SHORT);
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
	
	protected void doPreChecks()
	{
		Thread thread = new Thread() {
			public void run() {
				checkDeviceModel();
		    	preCheckHandler.sendMessage(checkRomManager());
			}
		};
		thread.start();
	}
	
	static rmMessage[] messages = rmMessage.values();
	
	private Handler preCheckHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast toast = null;
			switch(messages[msg.what]) { 
			case NOROMMANAGER:
				Editor e = ((Activity)_context).getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
				e.putBoolean(PREF_CWR_INSTALLED, false);
				e.commit();
				toast = Toast.makeText(_context, "Rom Manager not found, switching to manual mode", Toast.LENGTH_LONG);
				toast.show();
				break;
			case NOCWRSET:
				if (!ss.getRMAlreadyWarned())
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(_context);
			 	   	builder.setMessage(R.string.nocwr_dialog)
			 	          	.setCancelable(false)
			 	          	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			 	          		public void onClick(DialogInterface dialog, int id) {
			 	                   Intent i = new Intent(Intent.ACTION_MAIN);
			 	                   i.setComponent(new ComponentName("com.koushikdutta.rommanager", "com.koushikdutta.rommanager.RomManager"));
			 	                   startActivity(i);
			 	          		}
			 	          	})
			 	          	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			 	          		public void onClick(DialogInterface dialog, int id) {
			 	          			Editor e = ((Activity)_context).getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
			 	          			e.putBoolean(PREF_CWR_INSTALLED, false);
			 	          			e.commit();
			 	          			Toast toast = Toast.makeText(_context, "Rom Manager not found, switching to manual mode", Toast.LENGTH_LONG);
			 	          			toast.show();
			 	          		}
			 	          	});
			 	   	AlertDialog alert = builder.create();
			 	   	alert.show();
			 	   ss.setRMAlreadyWarned(true);
				}
				else
				{
					toast = Toast.makeText(_context, "Rom Manager not found, switching to manual mode", Toast.LENGTH_LONG);
	          		toast.show();
				}
				break;
			default:
				if (ss.getShowRMStatus())
				{
					toast = Toast.makeText(_context, "Rom Manager Checks Out OK!", Toast.LENGTH_LONG);
					toast.show();
					ss.setShowRMStatus(false);
					//((StateSaver)getApplication()).setShowRMStatus(false);
				}
				break;
			}
		}
	};

	private void checkDeviceModel() {
		if (this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).getBoolean(PREF_SHOW_MODEL_WARNING, true))
		{
			String DeviceModel = android.os.Build.MODEL;
			if (!DeviceModel.equals(getString(R.string.expected_model)))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(_context);
			 	   builder.setMessage(R.string.model_mismatch)
			 	          .setCancelable(false)
			 	          .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			 	              public void onClick(DialogInterface dialog, int id) {
			 	                   
			 	              }
			 	          });
			 	   AlertDialog alert = builder.create();
			 	   alert.show();
			}			
	    	Editor e = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
	    	e.putBoolean(PREF_SHOW_MODEL_WARNING, false);
	    	e.commit();
		}
	}
	
	private Message checkRomManager()
	{
		Message rmCheckMessage = new Message();
		
		File rmRoot = new File("/data/data/com.koushikdutta.rommanager");
		
		if (!rmRoot.exists())
		{
			rmCheckMessage.what = rmMessage.NOROMMANAGER.ordinal();
			return rmCheckMessage;
		}
		

		String cwrVersion = null;
		
		if (mService != null)
		{
			try {
				cwrVersion = mService.getClockworkModRecoveryVersion();
			} catch (RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		if (cwrVersion == null)
		{
			rmCheckMessage.what = rmMessage.NOCWRSET.ordinal();
			return rmCheckMessage;
		}
		
		rmCheckMessage.what = rmMessage.ROMMANAGEROK.ordinal();
		return rmCheckMessage;
		
	}

}