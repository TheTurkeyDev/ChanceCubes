package chanceCubes.rewards.rewardparts;

import net.minecraft.util.SoundEvent;

public class SoundPart
{
	public static String[] elements = new String[]{"sound:S", "delay:I", "serverWide:B", "range:I"};
	
	private SoundEvent sound;
	
	private int delay = 0;
	
	private boolean serverWide = false;
	private int range = 16;
	
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
}
