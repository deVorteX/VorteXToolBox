package com.devortex.vortextoolbox.Activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.devortex.vortextoolbox.R;
import com.devortex.vortextoolbox.helper.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class BatteryIconSelector extends Activity implements OnClickListener {
	private ProgressDialog pd;
	private XMLHandler myHandler;
	private LinearLayout ll;
	private ScrollView sv;
	private String _fileName;
	private Thread _thread;
	private Context _context = BatteryIconSelector.this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (!commandRunner.isSdPresent())
        	commandRunner.warnNoSD(_context);
        
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        
        pd = ProgressDialog.show(_context, "Loading...", "Loading Choices", true, false);
		Thread parseXML = new Thread()
			{
				public void run() {
					loadMenu(_context);
				}
			};
		parseXML.start();
	}
	
	protected void loadMenu(Context context)
	{
		SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = null;
        try {
			sp = spf.newSAXParser();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SAXException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        XMLReader xr = null;
        try {
			xr = sp.getXMLReader();
		} catch (SAXException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        myHandler = new XMLHandler(_context);
        xr.setContentHandler(myHandler);
        URL iconListUrl = null;
		try {
			iconListUrl = new URL(getString(R.string.battery_xmlurl));
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			xr.parse(new InputSource(iconListUrl.openStream()));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SAXException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		menuHandler.sendEmptyMessage(0);
	}
	
	protected void finishMenu()
	{
		ArrayList<IconsList> iconsList = myHandler.getIconsList();
		
		for (int i = 0; i < iconsList.size(); i++)
		{
	        URL iconUrl = null;
			try {
				iconUrl = new URL(iconsList.get(i).getImageURL());
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        Bitmap mIcon_val = null;
			try {
				mIcon_val = BitmapFactory.decodeStream(iconUrl.openConnection().getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			URL iconSetZipURL = null;
			try {
				iconSetZipURL = new URL(iconsList.get(i).getDownloadURL());
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	        
			
		        ImageView iv = new ImageView(this);
		        iv.setImageBitmap(mIcon_val);
		        
		        ll.addView(iv);
		        
		        Button b = new Button(this);
				b.setText("Install " + iconsList.get(i).getName() + "...");
				b.setTag(iconSetZipURL);
				b.setOnClickListener(this);
				ll.addView(b);
		}
		
        this.setContentView(sv);
	}
	
	protected void startBatterySwap(final URL downloadUrl) throws IOException, InterruptedException
	{
		pd = ProgressDialog.show(_context, "Downloading...", "Downloading Icons", true, false);
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
					commandRunner.swapBatteryIcons(_context, _fileName);
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
	
	private Handler menuHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			finishMenu();
		}
	};

	public void onClick(View v) {
		URL url = (URL)v.getTag();
		try {
			startBatterySwap(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
