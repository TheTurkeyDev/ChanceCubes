package chanceCubes.rewards.rewardparts;

import net.minecraft.util.SoundEvent;

public class SoundPart
{
	public static String[] elements = new String[] { "sound:S", "delay:I", "serverWide:B", "range:I" };

	private SoundEvent sound;

	private int delay = 0;

	private boolean serverWide = false;
	private int range = 16;

	private int volume = 1;
	private int pitch = 1;

	private boolean atPlayersLocation = false;

	public SoundPart(SoundEvent sound)
	{
		this.sound = sound;
	}

	public SoundEvent getSound()
	{
		return sound;
	}

	public int getDelay()
	{
		return delay;
	}

	public SoundPart setDelay(int delay)
	{
		this.delay = delay;
		return this;
	}

	public boolean isServerWide()
	{
		return serverWide;
	}

	public SoundPart setServerWide(boolean serverWide)
	{
		this.serverWide = serverWide;
		return this;
	}

	public int getRange()
	{
		return range;
	}

	public SoundPart setRange(int range)
	{
		this.range = range;
		return this;
	}

	public int getVolume()
	{
		return volume;
	}

	public void setVolume(int volume)
	{
		this.volume = volume;
	}

	public int getPitch()
	{
		return pitch;
	}

	public void setPitch(int pitch)
	{
		this.pitch = pitch;
	}

	public boolean playAtPlayersLocation()
	{
		return atPlayersLocation;
	}

	public SoundPart setAtPlayersLocation(boolean atPlayersLocation)
	{
		this.atPlayersLocation = atPlayersLocation;
		return this;
	}
}
