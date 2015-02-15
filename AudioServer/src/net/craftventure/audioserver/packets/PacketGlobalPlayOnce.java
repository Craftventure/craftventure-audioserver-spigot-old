package net.craftventure.audioserver.packets;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

public class PacketGlobalPlayOnce extends BasePacket {
	private int audioid = 0;
	private String name = "";
	private float volume = 1.0f;

	public PacketGlobalPlayOnce() {
		this(-1, "", 1.0f);
	}

	public PacketGlobalPlayOnce(int audioid, String name) {
		this(audioid, name, 1.0f);
	}

	public PacketGlobalPlayOnce(int audioid, String name, float volume) {
		this.id = PacketID.GLOBAL_PLAY_ONCE.ID;

		this.audioid = audioid;
		this.name = name;
		this.volume = volume;
	}

	public float getVolume()
	{
		return this.volume;
	}

	public int getAudioID()
	{
		return this.audioid;
	}

	public String getName()
	{
		return this.name;
	}

	@Override
	public PacketGlobalPlayOnce fromJSON(JsonObject obj)
	{
		try
		{
			this.audioid = obj.get("audioid").getAsInt();
			this.name = obj.get("name").getAsString();
			this.volume = obj.get("volume").getAsFloat();
		} catch (Exception e)
		{
			return null;
		}
		return this;
	}

	@Override
	public JsonObject getJSON()
	{
		JsonObject obj = new JsonObject();

		try
		{
			obj.addProperty("id", id);
			obj.addProperty("audioid", audioid);
			obj.addProperty("name", name);
			obj.addProperty("volume", volume);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
