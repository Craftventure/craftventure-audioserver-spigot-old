package net.craftventure.audioserver.packets;

public class ProtocolConfig {
	private static int currrentAudioID = 0;

	public static int getAudioID()
	{
		return currrentAudioID++;
	}
}
