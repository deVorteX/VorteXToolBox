package com.devortex.vortextoolbox.Activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.devortex.vortextoolbox.R;
import com.devortex.vortextoolbox.helper.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class ThrottleSelector extends Activity implements OnClickListener {
	private ProgressDialog pd;
	private LinearLayout ll;
	private ScrollView sv;
	private String _fileName;
	private Thread _thread;
	private Context _context = ThrottleSelector.this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        
        String[] menuChoices = getResources().getStringArray(R.array.services_jar_options);
        String[] throttleUrls = getResources().getStringArray(R.array.services_jar_urls);
        
        for (int i = 0; i < menuChoices.length; i++)
        {
        	Button b = new Button(this);
			b.setText("Install " + menuChoices[i] + "...");
			b.setTag(throttleUrls[i]);
			b.setOnClickListener(this);
			ll.addView(b);
        }
        
        this.setContentView(sv);
	}
	
	protected void startServicesSwap(final URL downloadUrl) throws IOException, InterruptedException
	{
		pd = ProgressDialog.show(_context, "Downloading...", "Downloading Mod", true, false);
		_thread = new Thread()
		{
			public void run() {
				try {
					_fileName = Downloader.downloadFile(_context, downloadUrl);
					downloadHandler.sendEmptyMessage(0);
					pd.dismiss();					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					downloadHandler.sendEmptyMessage(0);
					pd.dismiss();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					downloadHandler.sendEmptyMessage(0);
					pd.dismiss();
				}
			}
		};
		_thread.start();
	}
	
	protected void startMod() throws InterruptedException
	{
		pd = ProgressDialog.show(_context, "Preparing...", "Preparing Mod", true, false);
		_thread = new Thread() {
			public void run() {
				try {
					commandRunner.swapServicesJar(_context, _fileName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				installHandler.sendEmptyMessage(0);
			}
		};
		_thread.start();		
	}
	
	private Handler downloadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			try {
				if (_fileName != null)
				{
					startMod();
				}
				else
				{
					Toast toast = Toast.makeText(_context, R.string.error_download, Toast.LENGTH_SHORT);
					toast.show();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private Handler installHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			commandRunner.cwrDialog(_context);
		}
	};
	
	public void onClick(View v) {
		URL url = null;
		try {
			url = new URL((String) v.getTag());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		try {
			startServicesSwap(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
