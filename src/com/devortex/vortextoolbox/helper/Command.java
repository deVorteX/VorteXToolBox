package com.devortex.vortextoolbox.helper;

public class Command {
	private StringBuilder _command;

		public Command()
		{
			_command = new StringBuilder();
			_command.append("BB=busybox ; ");
			_command.append("$BB mount -o remount,rw /data ;");
			_command.append("$BB mount -o remount,rw /system ;");
		}
		public void Add(String commandLine)
		{
			_command.append(commandLine);
			_command.append(" ; ");
		}
		public String get()
		{
			_command.append("$BB mount -o remount,ro /data ;");
			_command.append("$BB mount -o remount,ro /system ;");
			return _command.toString();
		}
	}
