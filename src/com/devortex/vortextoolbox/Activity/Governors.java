package com.devortex.vortextoolbox.Activity;

import java.io.File;
import java.io.IOException;

import com.devortex.vortextoolbox.R;
import com.devortex.vortextoolbox.helper.commandRunner;

import android.app.Activity;
import android.content.Context;
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
			
	}
	
	protected void SetInnitialChoice()
	{
		String pbFile = getString(R.string.pbgov_script);
		String vFile = getString(R.string.defaultgov_script);
        File pbGov = new File("/system/etc/init.d/" + pbFile);
        File vGov = new File("/system/etc/init.d/" + vFile);
        if (pbGov.exists())
        {
        	((RadioButton) findViewById(R.id.rbGovernorPowerBoost)).setChecked(true);
        }
        else if (vGov.exists())
        {
        	((RadioButton) findViewById(R.id.rbGovernorInteractive)).setChecked(true);
        }
        else
        	((RadioButton) findViewById(R.id.rbGovernorOnDemand)).setChecked(true);
	}
}
