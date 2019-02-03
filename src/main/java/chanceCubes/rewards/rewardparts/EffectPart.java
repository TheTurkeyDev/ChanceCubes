package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class EffectPart extends BasePart
{
	private IntVar radius = new IntVar(1);

	private IntVar id = new IntVar(0);
	private IntVar duration = new IntVar(0);
	private IntVar amplifier = new IntVar(0);

	public EffectPart(Potion pot, int duration, int amplifier)
	{
		this(new IntVar(Potion.getIdFromPotion(pot)), new IntVar(duration), new IntVar(amplifier));
	}

	public EffectPart(int id, int duration, int amplifier)
	{
		this(new IntVar(id), new IntVar(duration), new IntVar(amplifier));
	}

	public EffectPart(IntVar id, IntVar duration, IntVar amplifier)
	{
		this.id = id;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	public IntVar getRadius()
	{
		return radius;
	}

	public EffectPart setRadius(int radius)
	{
		return this.setRadius(new IntVar(radius));
	}

	public EffectPart setRadius(IntVar radius)
	{
		this.radius = radius;
		return this;
	}

	public PotionEffect getEffect()
	{
		return new PotionEffect(Potion.getPotionById(id.getIntValue()), duration.getIntValue() * 20, amplifier.getIntValue());
	}
}
