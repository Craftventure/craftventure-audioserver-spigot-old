package net.craftventure.audioserver.packets;

import com.google.gson.JsonObject;

public class PacketAudioSync extends BasePacket {
	private int audioid = 0;
	private double seconds = 1.0f;
	private double margin = 0.0f;

	public PacketAudioSync() {
		this(-1, 0.0f);
	}

	public PacketAudioSync(int audioid, float volume) {
		this.id = PacketID.AUDIO_SYNC.ID;

		this.audioid = audioid;
		this.seconds = volume;
	}

	public PacketAudioSync(int audioid, float volume, double margin) {
		this(audioid, volume);
		this.margin = margin;
	}

	public void setMargin(double margin)
	{
		this.margin = margin;
	}

	public double getMargin()
	{
		return margin;
	}

	public double getSeconds()
	{
		return this.seconds;
	}

	public int getAudioID()
	{
		return this.audioid;
	}

	@Override
	public PacketAudioSync fromJSON(JsonObject obj)
	{
		try
		{
			this.audioid = obj.get("audioid").getAsInt();
			this.seconds = obj.get("volume").getAsDouble();
			this.margin = obj.get("margin").getAsDouble();
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
			obj.addProperty("seconds", seconds);
			obj.addProperty("margin", margin);
		} catch (Exception e)
		{
			return null;
		}
		return obj;
	}
}
