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
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.devortex.vortextoolbox.*;
import com.devortex.vortextoolbox.helper.*;
import com.koushikdutta.rommanager.api.IROMManagerAPIService;

public class VorteXToolBox extends Activity {
	public static final String PREF_FILE_NAME = "vortextoolbox";
	public static final String PREF_SHOW_MODEL_WARNING = "showDeviceWarning";
	public static final String PREF_CWR_INSTALLED = "rommanagerInstalled";
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
    	doPreChecks();
    	
		Assests.unzipAssets(_context);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button mChangeCarrierTextButton = (Button) findViewById(R.id.btnCarrierText);
        Button mOnePercentButton = (Button) findViewById(R.id.btnBatteryIcons);
        Button mStartupTweaks = (Button) findViewById(R.id.btnStartup);
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
	
	protected void calibrateBattery()
    {
    	commandRunner.calibrateBattery(_context);
    	Toast toast = Toast.makeText(_context, "Battery Calibration Complete...", Toast.LENGTH_SHORT);
    	toast.show();
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
		checkDeviceModel();
    	checkRomManager();
	}

	private void checkDeviceModel() {
		if (this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).getBoolean(PREF_SHOW_MODEL_WARNING, true))
		{
			String DeviceModel = android.os.Build.MODEL;
			if (DeviceModel != getString(R.string.expected_model))
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
	
	private void checkRomManager()
	{
		File rmRoot = new File("/data/data/com.koushikdutta.rommanager");
		File rmFileDir = new File(rmRoot.getAbsolutePath() + "/files");
		File rmScriptFile = new File(rmFileDir.getAbsolutePath() + "/rommanager.sh");
		File rmExtendedFile = new File(rmFileDir.getAbsolutePath() + "/extendedcommand");
		Editor e = null;
		Toast toast = null;
		if (!rmRoot.exists())
		{
			e = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
			e.putBoolean(PREF_CWR_INSTALLED, false);
			e.commit();
			toast = Toast.makeText(_context, "Rom Manager not found, switching to manual mode", Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		
		String cmd = "ls -l /data/data/ | grep \"com.koushikdutta.rommanager\" | cut -d ' ' -f3";
		String uid = commandRunner.retrieveSingleCommandLineReturnLine(cmd);
		String cmdMakeFileDir = "mkdir " + rmFileDir.getAbsolutePath();
		String cmdSetFolderPerms = "chown " + uid + "." + uid + " " + rmFileDir.getAbsolutePath() + "; $BB chmod 771 " + rmFileDir.getAbsolutePath();
		String cmdMoveFiles = "cp " + _context.getFilesDir() + "/rommanager.sh " + rmScriptFile.getAbsolutePath() + "; cp " + _context.getFilesDir() + "/extendedcommand " + rmExtendedFile.getAbsolutePath() + "; " +
				"chmod 660 " + rmFileDir.getAbsolutePath() + "/*";
		Command suCommand = new Command();
		toast = Toast.makeText(_context, "Rom Manager Permissions Fixed", Toast.LENGTH_LONG);
		if (!rmFileDir.exists())
		{
			suCommand.Add(cmdMakeFileDir);
			suCommand.Add(cmdSetFolderPerms);
			suCommand.Add(cmdMoveFiles);
			try {
				commandRunner.runSuCommand(_context, suCommand.get()).waitFor();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			toast.show();
			return;
		}
		cmd = "ls -l " + rmRoot.getAbsolutePath() + " | grep \"files\"" + " | cut -d ' ' -f1";
		String filePermissions = commandRunner.retrieveSingleCommandLineReturnLine(cmd);
		cmd = "ls -l " + rmRoot.getAbsolutePath() + " | grep \"files\"" + " | cut -d ' ' -f3";
		String fileUid = commandRunner.retrieveSingleCommandLineReturnLine(cmd);
		if (filePermissions != "drwxrwx--x" || uid != fileUid)
		{
			suCommand.Add(cmdSetFolderPerms);
			try {
				commandRunner.runSuCommand(_context, suCommand.get()).waitFor();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			toast.show();
			return;
		}
		cmd = "ls -l " + rmScriptFile.getAbsolutePath() + " | cut -d ' ' -f1";
		String cmd2 = "ls -l " + rmExtendedFile.getAbsolutePath() + " | cut -d ' ' -f1";
		String cmd3 = "ls -l " + rmScriptFile.getAbsolutePath() + " | cut -d ' ' -f3";
		String cmd4 = "ls -l " + rmExtendedFile.getAbsolutePath() + " | cut -d ' ' -f3";
		if (rmScriptFile.exists() && rmExtendedFile.exists())
		{
			if (uid != commandRunner.retrieveSingleCommandLineReturnLine(cmd3) || uid != commandRunner.retrieveSingleCommandLineReturnLine(cmd4))
			{
				suCommand.Add(cmdMoveFiles);
				try {
					commandRunner.runSuCommand(_context, suCommand.get()).waitFor();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				toast.show();
				return;
			}
			else if (commandRunner.retrieveSingleCommandLineReturnLine(cmd) != "-rw-rw----" || commandRunner.retrieveSingleCommandLineReturnLine(cmd2) != "-rw-rw----")
			{
				suCommand.Add(cmdMoveFiles);
				try {
					commandRunner.runSuCommand(_context, suCommand.get()).waitFor();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				toast.show();
				return;
			}
		}
		else
		{
			suCommand.Add(cmdMoveFiles);
			try {
				commandRunner.runSuCommand(_context, suCommand.get()).waitFor();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			toast.show();
			return;
		}
		
	}

}