package net.craftventure.audioserver.packets;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

public class PacketAreaStop extends BasePacket {
	private int audioid = 0;
	private int fadetime = 0;

	public PacketAreaStop() {
		this(-1, 0);
	}

	public PacketAreaStop(int audioid, int fadetime) {
		this.id = PacketID.AREA_STOP.ID;

		this.audioid = audioid;
		this.fadetime = fadetime;
	}

	public int getAudioID()
	{
		return this.audioid;
	}

	@Override
	public PacketAreaStop fromJSON(JsonObject obj)
	{
		try
		{
			this.audioid = obj.get("audioid").getAsInt();
			this.fadetime = obj.get("fadetime").getAsInt();
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
			obj.addProperty("fadetime", fadetime);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
