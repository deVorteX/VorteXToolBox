package com.devortex.vortextoolbox.Activity;

import java.io.File;
import java.io.IOException;

import com.devortex.vortextoolbox.R;
import com.devortex.vortextoolbox.helper.commandRunner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PowerBoostCWR extends Activity{
	private Context _context = PowerBoostCWR.this;
	private ToggleButton mStartupToggle;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.startuptweaks);
        
        TextView tvToggleLabel = (TextView) findViewById(R.id.tvScripts);
        tvToggleLabel.setText(getString(R.string.vortextweaks_togglelabel));
        mStartupToggle = (ToggleButton) findViewById(R.id.toggleButton1);
        
        SetInnitialToggle();
        
        mStartupToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton button, boolean checked) {
				try {
					doSwitchTweaks(checked);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				commandRunner.rebootDialog(_context, true);
			}
        	
        });          
	}
	
	public void doSwitchTweaks(boolean checked) throws InterruptedException, IOException
	{
		String[] script = { getString(R.string.pbcwr_script) };
		if (checked)
	 	{
	 		commandRunner.enableTweaks(_context, script);
	 	}
	 	else
	 	{
	 		commandRunner.disableTweaks(_context, script);
	 	}
	}
	
	protected void SetInnitialToggle()
	{
		String checkFile = getString(R.string.pbcwr_script);
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
