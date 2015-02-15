package net.craftventure.audioserver.packets;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

public class PacketAreaStart extends BasePacket {
	private int audioid = 0;
	private String name = "";
	private float volume = 1.0f;
	private int fadetime = 0;
	private boolean repeat = true;

	public PacketAreaStart() {
		this(-1, "", 1.0f, 0, true);
	}

	public PacketAreaStart(int audioid, String name, float volume, int fadetime, boolean repeat) {
		this.id = PacketID.AREA_START.ID;

		this.audioid = audioid;
		this.name = name;
		this.volume = volume;
		this.fadetime = fadetime;
		this.repeat = repeat;
	}

	public float getVolume()
	{
		return this.volume;
	}

	public int getAudioID()
	{
		return this.audioid;
	}

	public int getFadetime()
	{
		return this.fadetime;
	}

	public String getName()
	{
		return this.name;
	}

	@Override
	public PacketAreaStart fromJSON(JsonObject obj)
	{
		try
		{
			this.audioid = obj.get("audioid").getAsInt();
			this.name = obj.get("name").getAsString();
			this.volume = obj.get("volume").getAsFloat();
			this.fadetime = obj.get("fadetime").getAsInt();
			this.repeat = obj.get("repeat").getAsBoolean();
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
			obj.addProperty("fadetime", fadetime);
			obj.addProperty("repeat", repeat);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
