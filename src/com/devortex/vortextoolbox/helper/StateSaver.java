package com.devortex.vortextoolbox.helper;

import android.app.Application;

public class StateSaver extends Application {
	private boolean _rmAlreadyWarned = false;
	private boolean _showRMStatus = true;
	
	public boolean getRMAlreadyWarned()
	{
		return _rmAlreadyWarned;
	}
	
	public boolean getShowRMStatus()
	{
		return _showRMStatus;
	}
	
	public void setRMAlreadyWarned(boolean value)
	{
		this._rmAlreadyWarned = value;
	}
	
	public void setShowRMStatus(boolean value)
	{
		this._showRMStatus = value;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
}
