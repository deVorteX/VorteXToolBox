package com.devortex.vortextoolbox.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.devortex.vortextoolbox.R;

import android.content.Context;
import android.os.Environment;

public class Downloader {
		
	public static String downloadFile(Context context, URL downloadUrl) throws IOException, InterruptedException
	{
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			return null;
		}
		BufferedInputStream inStream;
		BufferedOutputStream outStream;
		URLConnection conn;
		int lastSlash;
		File outFile;
		FileOutputStream fileStream;
		String saveDir = "/" + context.getString(R.string.app_name).replace(' ', '_') + "Downloads";
		
		File folder = new File(Environment.getExternalStorageDirectory() + saveDir);
		if (!folder.exists())
		{
			folder.mkdir();
		}
		
		conn = downloadUrl.openConnection();
		conn.setUseCaches(false);
				
		lastSlash = downloadUrl.toString().lastIndexOf('/');
		String fileName = downloadUrl.toString().substring(lastSlash +1);
		
		inStream = new BufferedInputStream(conn.getInputStream());
		outFile = new File(Environment.getExternalStorageDirectory() + saveDir + "/" + fileName);
		fileStream = new FileOutputStream(outFile);
		outStream = new BufferedOutputStream(fileStream, 1024);
		byte[] data = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = inStream.read(data, 0, data.length)) >= 0)
		{
			outStream.write(data, 0, bytesRead);
		}
		outStream.close();
		fileStream.close();
		inStream.close();
				
		return outFile.getAbsolutePath();
	}
    
}
