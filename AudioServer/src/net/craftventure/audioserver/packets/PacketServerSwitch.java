package net.craftventure.audioserver.packets;

import com.google.gson.JsonObject;

public class PacketServerSwitch extends BasePacket {
	private String servername = "";

	public PacketServerSwitch(String servername) {
		this.id = PacketID.SERVER_SWITCH.ID;

		this.servername = servername;
	}

	public String getServer()
	{
		return this.servername;
	}

	@Override
	public PacketServerSwitch fromJSON(JsonObject obj)
	{
		try
		{
			this.servername = obj.get("servername").getAsString();
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
			obj.addProperty("servername", servername);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
