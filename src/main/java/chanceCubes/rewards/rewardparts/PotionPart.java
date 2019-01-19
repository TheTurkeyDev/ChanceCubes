package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionPart
{
	private IntVar id = new IntVar(0);
	private IntVar duration = new IntVar(0);

	private IntVar delay = new IntVar(0);

	public PotionPart(Potion pot, int duration)
	{
		this(new IntVar(Potion.getIdFromPotion(pot)), new IntVar(duration));
	}
	
	public PotionPart(int id, int duration)
	{
		this(new IntVar(id), new IntVar(duration));
	}

	public PotionPart(IntVar id, IntVar duration)
	{
		this.id = id;
		this.duration = duration;
	}

	public PotionEffect getEffect()
	{
		return new PotionEffect(Potion.getPotionById(id.getIntValue()), duration.getIntValue() * 20);
	}

	public int getDelay()
	{
		return delay.getIntValue();
	}

	public void setDelay(int delay)
	{
		this.setDelay(new IntVar(delay));
	}

	public void setDelay(IntVar delay)
	{
		this.delay = delay;
	}
}