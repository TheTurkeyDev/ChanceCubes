package chanceCubes.rewards.rewardparts;

public class SoundPart
{
	private String sound;
	
	private int delay = 0;
	
	private boolean serverWide = false;
	private int range = 16;
	
	public SoundPart(String sound)
	{
		this.sound = sound;
	}

	public String getSound()
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
