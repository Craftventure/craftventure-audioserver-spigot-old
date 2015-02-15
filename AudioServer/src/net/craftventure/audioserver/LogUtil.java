package net.craftventure.audioserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogUtil {

	public static void log(String file, String msg)
	{
		try
		{
			(new File(AudioServer.get().getDataFolder() + "/logs/")).mkdirs();
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(AudioServer.get().getDataFolder() + "/logs/" + file), true));

			Calendar cal = Calendar.getInstance();
			cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

			writer.println("[" + sdf.format(cal.getTime()) + "] " + msg);
			writer.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void logException(String msg, Exception exception)
	{
		try
		{
			(new File(AudioServer.get().getDataFolder() + "/logs/")).mkdirs();
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(AudioServer.get().getDataFolder() + "/logs/error.log"), true));

			Calendar cal = Calendar.getInstance();
			cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

			writer.println("=======================================================================");
			writer.println("---------------------- [" + sdf.format(cal.getTime()) + "] ----------------------");
			if (msg != null)
			{
				writer.println(msg);
				writer.println("-----------------------------------------------------------------------");
			}
			exception.printStackTrace(writer);
			AudioServer.get().getLogger().warning("§4Error logged to file: " + msg);
			writer.println("=======================================================================");
			writer.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
