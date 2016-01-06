package net.craftventure.audioserver.packets;

import com.google.gson.JsonObject;

public class PacketExecScript extends BasePacket {
	private String script = "";

	public PacketExecScript() {
		this("");
	}

	public PacketExecScript(String script) {
		this.id = PacketID.EXEC_SCRIPT.ID;

		this.script = script;
	}

	public String getScript()
	{
		return this.script;
	}

	@Override
	public PacketExecScript fromJSON(JsonObject obj)
	{
		try
		{
			this.script = obj.get("script").getAsString();
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
			obj.addProperty("script", script);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
