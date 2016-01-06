package net.craftventure.audioserver.packets;

import com.google.gson.JsonObject;

public class PacketClientAccept extends BasePacket {
	private String servername = "";

	public PacketClientAccept(String servername) {
		this.id = PacketID.CLIENT_ACCEPTED.ID;

		this.servername = servername;
	}

	@Override
	public PacketClientAccept fromJSON(JsonObject obj)
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
