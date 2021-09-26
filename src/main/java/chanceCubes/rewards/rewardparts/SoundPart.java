package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import net.minecraft.sounds.SoundEvent;

public class SoundPart extends BasePart
{
	private final SoundEvent sound;

	private BoolVar serverWide = new BoolVar(false);
	private IntVar range = new IntVar(16);

	private FloatVar volume = new FloatVar(1);
	private FloatVar pitch = new FloatVar(1);

	private BoolVar atPlayersLocation = new BoolVar(false);

	public SoundPart(SoundEvent sound)
	{
		this(sound, 0);
	}

	public SoundPart(SoundEvent sound, int delay)
	{
		this(sound, new IntVar(delay));
	}

	public SoundPart(SoundEvent sound, IntVar delay)
	{
		this.sound = sound;
		this.setDelay(delay);
	}

	public SoundEvent getSound()
	{
		return sound;
	}

	public boolean isServerWide()
	{
		return serverWide.getBoolValue();
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
		return range.getIntValue();
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

	public float getVolume()
	{
		return volume.getFloatValue();
	}

	public void setVolume(float volume)
	{
		this.setVolume(new FloatVar(volume));
	}

	public void setVolume(FloatVar volume)
	{
		this.volume = volume;
	}

	public float getPitch()
	{
		return pitch.getFloatValue();
	}

	public void setPitch(float pitch)
	{
		this.setPitch(new FloatVar(pitch));
	}

	public void setPitch(FloatVar pitch)
	{
		this.pitch = pitch;
	}

	public boolean playAtPlayersLocation()
	{
		return atPlayersLocation.getBoolValue();
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
