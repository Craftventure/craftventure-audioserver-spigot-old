package net.craftventure.audioserver.packets;

public enum PacketID {
	HEARTBEAT(0), LOGIN(1), KICK(2), GLOBAL_PLAY_ONCE(3), AREA_START(4), AREA_STOP(5), CLIENT_ACCEPTED(6), AUDIO_SYNC(7), NOTIFICATION(8), EXEC_SCRIPT(9), COMPUTER_SPEAK(10), INCOMING_WARP(11), SERVER_SWITCH(12);

	final int ID;

	PacketID(int ID) {
		this.ID = ID;
	}

	public int getID()
	{
		return ID;
	}
}
