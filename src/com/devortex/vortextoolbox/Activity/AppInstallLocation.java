package com.devortex.vortextoolbox.Activity;

import java.io.IOException;
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

public class AppInstallLocation extends Activity{
	private Context _context = AppInstallLocation.this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.appinstalllocation);
        
        Button bSetGov = (Button) findViewById(R.id.btnSaveAppInstall);
        
        SetInnitialChoice();
        
        bSetGov.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				try {
					setAppInstallLocation();
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
	
	protected void setAppInstallLocation() throws InterruptedException, IOException
	{
		if (((RadioButton) findViewById(R.id.rbAppInstallAuto)).isChecked())
		{
			commandRunner.setInstallLocation(_context, "0");
		}
		else if (((RadioButton) findViewById(R.id.rbAppInstallInternal)).isChecked())
		{
			commandRunner.setInstallLocation(_context, "1");
		}
		else
			commandRunner.setInstallLocation(_context, "2");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		builder.setMessage(R.string.appinstall_set)
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
		String line = commandRunner.retrieveSingleCommandLineReturnLine("pm getInstallLocation");
				
		if (line != null) {
			if (line.equalsIgnoreCase("1[internal]"))
	        	((RadioButton) findViewById(R.id.rbAppInstallInternal)).setChecked(true);
			else if (line.equalsIgnoreCase("0[auto]"))
	        	((RadioButton) findViewById(R.id.rbAppInstallAuto)).setChecked(true);
			else
	        	((RadioButton) findViewById(R.id.rbAppInstallSD)).setChecked(true);
		}
		else
			((RadioButton) findViewById(R.id.rbAppInstallAuto)).setChecked(true);
	}
}
