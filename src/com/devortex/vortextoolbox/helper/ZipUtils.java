package com.devortex.vortextoolbox.helper;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

import org.apache.commons.io.IOUtils;

public class ZipUtils {
	final static int BUFFER = 2048;
	
	public static void Zip(File workingDir, File ZipFile) throws IOException
	{
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(ZipFile)));
		addDir(workingDir, out, workingDir.getAbsolutePath());
		out.flush();
		out.close();
	}
	
	public static void addDir(File dirObj, ZipOutputStream out, String workingDir) throws IOException
	{
		BufferedInputStream in = null;
		byte[] data = new byte[BUFFER];
		File files[] = dirObj.listFiles();
		for (int i=0; i<files.length; i++)
		{
			if (files[i].isDirectory())
			{
				addDir(files[i], out, workingDir);
				continue;
			}
			in = new BufferedInputStream(new FileInputStream(files[i]), BUFFER);
			
			out.putNextEntry(new ZipEntry(files[i].getAbsolutePath().replace(workingDir + "/", "")));
			int count;
			while ((count = in.read(data, 0, BUFFER)) != -1)
			{
				out.write(data, 0, count);
			}
			out.closeEntry();
		}
	}
	
	public static void UnZip(File workingDir, String sZipFile)
	{
		if (!workingDir.exists())
			workingDir.mkdir();
		try {
	         
	         ZipEntry entry;
	         ZipFile zipfile = new ZipFile(sZipFile);
	         Enumeration<? extends ZipEntry> e = zipfile.entries();
	         while(e.hasMoreElements()) {
	            entry = (ZipEntry) e.nextElement();
	            UnzipEntry(zipfile, entry, workingDir);
	         }
	      } catch(Exception e) { }
	}
	
	private static void UnzipEntry(ZipFile zipfile, ZipEntry entry, File workingDir) throws IOException
	{
		BufferedInputStream is = null;
		
		if (entry.isDirectory())
        {
        	createDir(new File(workingDir, entry.getName()));
        	return;
        }
        File outputFile = new File(workingDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
        	createDir(outputFile.getParentFile());
        }
        
        is = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(workingDir + "/" + entry.getName()));
        try {
        	IOUtils.copy(is, bos);
        } finally {
        	bos.close();
        	is.close();
        }
	}
	
	private static void createDir(File dir) {
		dir.mkdirs();
	}
	
}
