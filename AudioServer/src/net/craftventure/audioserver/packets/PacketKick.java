package net.craftventure.audioserver.packets;

import com.google.gson.JsonObject;

public class PacketKick extends BasePacket {
	private String message = "";
	private String reason = "";

	public PacketKick() {
		this("", "");
	}

	public PacketKick(String message) {
		this(message, "");
	}

	public PacketKick(String kickmessage, String reason) {
		this.id = PacketID.KICK.ID;

		this.message = kickmessage;
		this.reason = "DEFAULT";
	}

	public String getMessage()
	{
		return this.message;
	}

	public String getReason()
	{
		return this.reason;
	}

	@Override
	public PacketKick fromJSON(JsonObject obj)
	{
		try
		{
			this.reason = obj.get("reason").getAsString();
			this.message = obj.get("message").getAsString();
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
			obj.addProperty("message", message);
			obj.addProperty("reason", reason);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
