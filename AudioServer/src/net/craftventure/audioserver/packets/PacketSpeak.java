package net.craftventure.audioserver.packets;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

public class PacketSpeak extends BasePacket {
	private String voiceText = "";

	public PacketSpeak() {
		this("");
	}

	public PacketSpeak(String script) {
		this.id = PacketID.COMPUTER_SPEAK.ID;

		this.voiceText = script;
	}

	public String getScript()
	{
		return this.voiceText;
	}

	@Override
	public PacketSpeak fromJSON(JsonObject obj)
	{
		try
		{
			this.voiceText = obj.get("voicetext").getAsString();
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
			obj.addProperty("voicetext", voiceText);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
