package com.devortex.vortextoolbox.Activity;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.devortex.vortextoolbox.*;
import com.devortex.vortextoolbox.helper.*;

public class StartupScript extends Activity{
	private Context _context = StartupScript.this;
	private ToggleButton mStartupToggle;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.startuptweaks);
        
        TextView tvToggleLabel = (TextView) findViewById(R.id.tvScripts);
        tvToggleLabel.setText(getString(R.string.vortextweaks_togglelabel));
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(getString(R.string.vortexscript_description));
        mStartupToggle = (ToggleButton) findViewById(R.id.toggleButton1);
        
        SetInnitialToggle();
        
        mStartupToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton button, boolean checked) {
				if (doSwitchTweaks(checked))
				{
					commandRunner.rebootDialog(_context, true);
				}
				else
				{
					mStartupToggle.setChecked(!checked);
				}
			}
        	
        });             
		
	}
	
	protected boolean doSwitchTweaks(boolean checked) {
		String[] vortexScripts = getResources().getStringArray(R.array.vortex_scripts);
		if (checked)
		{
			
			try {
				commandRunner.enableTweaks(_context, vortexScripts);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try {
				commandRunner.disableTweaks(_context, vortexScripts);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	protected void SetInnitialToggle()
	{
		String checkFile = getResources().getStringArray(R.array.vortex_scripts)[0];
        File startup = new File("/system/etc/init.d/" + checkFile);
        if (startup.exists())
        {
        	mStartupToggle.setChecked(true);
        }
        else
        {
        	mStartupToggle.setChecked(false);
        }
	}
	
}
