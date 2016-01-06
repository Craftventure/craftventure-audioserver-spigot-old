package net.craftventure.audioserver.packets;

import com.google.gson.JsonObject;

public class PacketNotification extends BasePacket {
	private String text = "";
	private String body = "";
	private String icon = "";

	public PacketNotification() {
		this("", "", "");
	}

	public PacketNotification(String text, String body, String icon) {
		this.id = PacketID.NOTIFICATION.ID;

		this.text = text;
		this.body = body;
		this.icon = icon;
	}

	public String getText()
	{
		return this.text;
	}

	public String getBody()
	{
		return this.body;
	}

	public String getIcon()
	{
		return this.icon;
	}

	@Override
	public PacketNotification fromJSON(JsonObject obj)
	{
		try
		{
			this.text = obj.get("text").getAsString();
			this.body = obj.get("body").getAsString();
			this.icon = obj.get("icon").getAsString();
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
			obj.addProperty("text", text);
			obj.addProperty("body", body);
			obj.addProperty("icon", icon);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
