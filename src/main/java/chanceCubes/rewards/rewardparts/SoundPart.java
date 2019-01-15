package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import net.minecraft.util.SoundEvent;

public class SoundPart
{
	private SoundEvent sound;

	private IntVar delay = new IntVar(0);

	private BoolVar serverWide = new BoolVar(false);
	private IntVar range = new IntVar(16);

	private IntVar volume = new IntVar(1);
	private IntVar pitch = new IntVar(1);

	private BoolVar atPlayersLocation = new BoolVar(false);

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
		return delay.getValue();
	}

	public SoundPart setDelay(int delay)
	{
		return this.setDelay(new IntVar(delay));
	}

	public SoundPart setDelay(IntVar delay)
	{
		this.delay = delay;
		return this;
	}

	public boolean isServerWide()
	{
		return serverWide.getValue();
	}

	public SoundPart setServerWide(boolean serverWide)
	{
		return this.setServerWide(new BoolVar(serverWide));
	}

	public SoundPart setServerWide(BoolVar serverWide)
	{
		this.serverWide = serverWide;
		return this;
	}

	public int getRange()
	{
		return range.getValue();
	}

	public SoundPart setRange(int range)
	{
		return this.setRange(new IntVar(range));
	}

	public SoundPart setRange(IntVar range)
	{
		this.range = range;
		return this;
	}

	public int getVolume()
	{
		return volume.getValue();
	}

	public void setVolume(int volume)
	{
		this.setVolume(new IntVar(volume));
	}

	public void setVolume(IntVar volume)
	{
		this.volume = volume;
	}

	public int getPitch()
	{
		return pitch.getValue();
	}

	public void setPitch(int pitch)
	{
		this.setPitch(new IntVar(pitch));
	}

	public void setPitch(IntVar pitch)
	{
		this.pitch = pitch;
	}

	public boolean playAtPlayersLocation()
	{
		return atPlayersLocation.getValue();
	}

	public SoundPart setAtPlayersLocation(boolean atPlayersLocation)
	{
		return this.setAtPlayersLocation(new BoolVar(atPlayersLocation));
	}

	public SoundPart setAtPlayersLocation(BoolVar atPlayersLocation)
	{
		this.atPlayersLocation = atPlayersLocation;
		return this;
	}
}
