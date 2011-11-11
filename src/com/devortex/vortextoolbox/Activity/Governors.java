package com.devortex.vortextoolbox.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.devortex.vortextoolbox.R;
import com.devortex.vortextoolbox.helper.commandRunner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class Governors extends Activity{
	private Context _context = Governors.this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.governors);
        
        Button bSetGov = (Button) findViewById(R.id.btnSaveGovernor);
        
        SetInnitialChoice();
        
        bSetGov.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				try {
					setGov();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
         
	}
	
	protected void setGov() throws InterruptedException, IOException
	{
		if (((RadioButton) findViewById(R.id.rbGovernorPowerBoost)).isChecked())
		{
			commandRunner.enablePBGovernor(_context);
		}
		else if (((RadioButton) findViewById(R.id.rbGovernorInteractive)).isChecked())
		{
			commandRunner.enableInteractiveGovernor(_context);
		}
		else
			commandRunner.disableGovernors(_context);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		builder.setMessage(R.string.governor_set)
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					((Activity)_context).finish();
				}
			});
		AlertDialog alert = builder.create();
	 	alert.show();
			
	}
	
	protected void SetInnitialChoice()
	{
		Process proc = null;
		String line = null;
		try {
			proc = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		try {
			line = in.readLine();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if (line.equalsIgnoreCase("conservative"))
        	((RadioButton) findViewById(R.id.rbGovernorPowerBoost)).setChecked(true);
		else if (line.equalsIgnoreCase("interactive"))
        	((RadioButton) findViewById(R.id.rbGovernorInteractive)).setChecked(true);
		else
        	((RadioButton) findViewById(R.id.rbGovernorOnDemand)).setChecked(true);
	}
}
