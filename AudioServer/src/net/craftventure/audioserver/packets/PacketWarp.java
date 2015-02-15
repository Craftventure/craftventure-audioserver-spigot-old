package net.craftventure.audioserver.packets;

import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

public class PacketWarp extends BasePacket {
	private String warp = "";

	public PacketWarp() {
		this("");
	}

	public PacketWarp(String warp) {
		this.id = PacketID.INCOMING_WARP.ID;

		this.warp = warp;
	}

	public String getWarpName()
	{
		return this.warp;
	}

	@Override
	public PacketWarp fromJSON(JsonObject obj)
	{
		try
		{
			this.warp = obj.get("warp").getAsString();
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
			obj.addProperty("warp", warp);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
