package net.craftventure.audioserver.packets;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

public class BasePacket {
	protected int id = 0;

	public BasePacket fromJSON(JsonObject obj)
	{
		return this;
	}

	public JsonObject getJSON()
	{
		return null;
	}

	public byte[] getAsPluginMessage(String playername) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("AudioServer");
		out.writeUTF(playername);
		out.writeUTF(getJSON().toString());
		return out.toByteArray();
	}
}
