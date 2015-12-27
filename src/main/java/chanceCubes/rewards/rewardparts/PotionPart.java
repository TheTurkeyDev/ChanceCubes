package chanceCubes.rewards.rewardparts;

import net.minecraft.potion.PotionEffect;

public class PotionPart
{
	public static String[] elements = new String[]{"potionid", "duration", "delay"};
	
	private PotionEffect effect;

	private int delay = 0;

	public PotionPart(PotionEffect effect)
	{
		this.effect = effect;
	}

	public PotionEffect getEffect()
	{
		return effect;
	}

	public int getDelay()
	{
		return delay;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}
}