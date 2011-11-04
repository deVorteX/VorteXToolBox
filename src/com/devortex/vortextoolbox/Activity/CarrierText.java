package com.devortex.vortextoolbox.Activity;

import java.io.IOException;

import com.devortex.vortextoolbox.*;
import com.devortex.vortextoolbox.helper.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CarrierText extends Activity {
	private ProgressDialog pd;
	private Thread _thread;
	private Context _context = CarrierText.this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carriertext);
        
        Button mSaveCarrierTextButton = (Button) findViewById(R.id.btnSaveCarrierText);
        // Register handler for UI elements
        mSaveCarrierTextButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
        	   Toast toast = null;
        	   String mTxtCarrierText = ((EditText) findViewById(R.id.txtCarrierText)).getText().toString();
        	   if (mTxtCarrierText.length() <= 16)
        		{
        		   doCarrierLabel(mTxtCarrierText);					
				} 
        	   else
        	   {
        		   	toast = Toast.makeText(_context, R.string.error_stringlength16, 5);
        		   	toast.show();
        	   }        		   
           }
       });
       }
	
	protected void doCarrierLabel(final String mTxtCarrierText)
	{
		pd = ProgressDialog.show(_context, "Preparing...", "Preparing Mod", true, false);
		_thread = new Thread()
		{
			public void run() {
				try {
					commandRunner.setCarrierLabel(_context, mTxtCarrierText);
					installHandler.sendEmptyMessage(0);
				} catch (IOException e) {
					pd.dismiss();
					Toast toast = Toast.makeText(_context, R.string.error_carriertext, 5);
					toast.show();
				} catch (InterruptedException e) {
					pd.dismiss();
					Toast toast = Toast.makeText(_context, R.string.error_carriertext, 5);
           			toast.show();
				}
			}
		};
		_thread.start();
	}
	
	public Handler installHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
//			commandRunner.cwrDialog(_context);
		}
	};
}
